#!/usr/bin/env bash
#
# build.sh — build reprodutível do TripLog em um único comando.
#
# Roda em qualquer ambiente (máquina local ou CI), a partir de um estado
# limpo:
#   1. Valida as ferramentas necessárias (JDK, Maven)
#   2. Limpa artefatos de builds anteriores
#   3. Ancora o build no commit (SOURCE_DATE_EPOCH/TZ=UTC) para que o mesmo
#      commit sempre produza o mesmo .jar, independente de quando/onde rodar
#   4. Compila, executa a suíte de testes (JUnit 5) e empacota o .jar
#   5. Gera um SBOM (CycloneDX via Syft), se a ferramenta estiver disponível
#   6. Calcula hashes SHA-256 dos artefatos e grava um manifesto do build
#      (build-info.json) com a proveniência (commit, branch, ferramentas)
#   7. Verifica a integridade do resultado final
#
# Uso:
#   ./build.sh           # build completo
#   ./build.sh --help    # mostra esta ajuda
#
set -Eeuo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

MIN_JAVA_VERSION="21"
MIN_MVN_VERSION="3.8.0"
SBOM_OUT="target/sbom.json"
HASHES_OUT="target/HASHES.sha256"
BUILD_INFO_OUT="target/build-info.json"

if [ -t 1 ]; then
    C_BLUE='\033[1;34m'; C_GREEN='\033[1;32m'; C_RED='\033[1;31m'; C_YELLOW='\033[1;33m'; C_RESET='\033[0m'
else
    C_BLUE=''; C_GREEN=''; C_RED=''; C_YELLOW=''; C_RESET=''
fi

step()  { printf "${C_BLUE}==>${C_RESET} %s\n" "$1"; }
ok()    { printf "${C_GREEN}[OK]${C_RESET} %s\n" "$1"; }
warn()  { printf "${C_YELLOW}[AVISO]${C_RESET} %s\n" "$1"; }
fail()  { printf "${C_RED}[FALHA]${C_RESET} %s\n" "$1"; exit 1; }

on_error() {
    local exit_code=$?
    printf "${C_RED}[FALHA]${C_RESET} linha %s (comando: '%s'). Código: %s\n" \
        "${BASH_LINENO[0]}" "${BASH_COMMAND}" "${exit_code}" >&2
    exit "$exit_code"
}
trap on_error ERR

usage() {
    cat <<EOF
build.sh — build completo e reprodutível do TripLog

Reproduz, a partir de um ambiente limpo, em um único comando:
  1. Validação das ferramentas necessárias e de suas versões mínimas
     (JDK ${MIN_JAVA_VERSION}+, Maven ${MIN_MVN_VERSION}+)
  2. Limpeza de artefatos de builds anteriores (target/, dependency-reduced-pom.xml)
  3. Âncora de determinismo: SOURCE_DATE_EPOCH derivado do último commit,
     TZ=UTC, LC_ALL=C e project.build.outputTimestamp do Maven — o mesmo
     commit sempre produz o mesmo .jar, em qualquer máquina ou data de build
  4. Compilação + execução da suíte de testes (JUnit 5) + empacotamento (.jar)
  5. Geração de SBOM em CycloneDX via Syft, se a ferramenta estiver instalada
     (saída em ${SBOM_OUT}; etapa pulada com aviso se o Syft não estiver no PATH)
  6. Cálculo de hashes SHA-256 dos artefatos gerados (${HASHES_OUT}) e
     gravação de um manifesto do build com a proveniência (${BUILD_INFO_OUT}):
     commit, branch, tag/describe, árvore limpa ou suja, versões das ferramentas
  7. Verificação de integridade: manifest do .jar, ausência de classes de
     teste no artefato final, e conferência dos hashes calculados

Uso:
  ./build.sh           Executa o build completo
  ./build.sh --help    Mostra esta ajuda

Pré-requisitos: JDK 21+, Maven 3.8+. Syft é opcional (apenas para o SBOM).
EOF
}

if [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
    usage
    exit 0
fi

have() { command -v "$1" >/dev/null 2>&1; }

strip_ansi() { sed -E 's/\x1b\[[0-9;]*[a-zA-Z]//g'; }

sha256_of() {
    if have sha256sum; then sha256sum "$1" | awk '{print $1}'
    elif have shasum;   then shasum -a 256 "$1" | awk '{print $1}'
    else fail "Nem 'sha256sum' nem 'shasum' encontrados para calcular integridade."
    fi
}

sha256_check() {
    if have sha256sum; then sha256sum -c "$1"
    elif have shasum;   then shasum -a 256 -c "$1"
    else fail "Nem 'sha256sum' nem 'shasum' encontrados para verificar integridade."
    fi
}

json_str() { printf '%s' "$1" | sed -e 's/\\/\\\\/g' -e 's/"/\\"/g'; }

git_field() {
    if have git && git rev-parse --git-dir >/dev/null 2>&1; then
        git "$@" 2>/dev/null || true
    fi
}

version_ge() {
    local IFS=.
    local -a a=($1) b=($2)
    local i n=${#b[@]}
    for ((i = 0; i < n; i++)); do
        local av="${a[i]:-0}" bv="${b[i]:-0}"
        if ((10#$av > 10#$bv)); then return 0; fi
        if ((10#$av < 10#$bv)); then return 1; fi
    done
    return 0
}

step "Verificando ferramentas necessárias"
command -v java   >/dev/null 2>&1 || fail "java não encontrado no PATH. Instale o JDK ${MIN_JAVA_VERSION}+."
command -v mvn    >/dev/null 2>&1 || fail "mvn não encontrado no PATH. Instale o Maven ${MIN_MVN_VERSION}+."
command -v jar    >/dev/null 2>&1 || fail "jar não encontrado no PATH (faz parte do JDK). Verifique a instalação do JDK ${MIN_JAVA_VERSION}+."

JAVA_VERSION="$(java -version 2>&1 | head -n1 | strip_ansi)"
MVN_VERSION="$(mvn -version 2>&1 | head -n1 | strip_ansi)"
ok "java:  ${JAVA_VERSION}"
ok "mvn:   ${MVN_VERSION}"

JAVA_VERSION_NUM="$(printf '%s' "$JAVA_VERSION" | grep -oE '"[0-9]+(\.[0-9]+){0,2}' | tr -d '"' | head -n1)"
[ -n "$JAVA_VERSION_NUM" ] || fail "não foi possível determinar a versão do JDK a partir de: ${JAVA_VERSION}"
version_ge "$JAVA_VERSION_NUM" "$MIN_JAVA_VERSION" \
    || fail "JDK ${JAVA_VERSION_NUM} encontrado, mas o projeto requer JDK ${MIN_JAVA_VERSION}+ (veja maven.compiler.release no pom.xml)."
ok "versão do JDK ${JAVA_VERSION_NUM} atende ao mínimo (${MIN_JAVA_VERSION}+)"

MVN_VERSION_NUM="$(printf '%s' "$MVN_VERSION" | grep -oE '[0-9]+\.[0-9]+(\.[0-9]+)?' | head -n1)"
[ -n "$MVN_VERSION_NUM" ] || fail "não foi possível determinar a versão do Maven a partir de: ${MVN_VERSION}"
version_ge "$MVN_VERSION_NUM" "$MIN_MVN_VERSION" \
    || fail "Maven ${MVN_VERSION_NUM} encontrado, mas o projeto requer Maven ${MIN_MVN_VERSION}+."
ok "versão do Maven ${MVN_VERSION_NUM} atende ao mínimo (${MIN_MVN_VERSION}+)"

if command -v syft >/dev/null 2>&1; then
    SYFT_VERSION="$(syft version 2>&1 | head -n1 | strip_ansi)"
    ok "syft:  ${SYFT_VERSION}"
    SYFT_AVAILABLE=1
else
    warn "syft não encontrado no PATH — a etapa de geração de SBOM será pulada."
    SYFT_AVAILABLE=0
    SYFT_VERSION=""
fi

step "Ancorando o build no commit (determinismo)"
export TZ=UTC
export LC_ALL=C
GIT_COMMIT="$(git_field rev-parse HEAD)"
GIT_BRANCH="$(git_field rev-parse --abbrev-ref HEAD)"
GIT_DESCRIBE="$(git_field describe --tags --always --dirty)"
GIT_DIRTY="false"
if have git && git rev-parse --git-dir >/dev/null 2>&1; then
    if [[ -n "$(git status --porcelain)" ]]; then
        GIT_DIRTY="true"
        warn "árvore de trabalho com alterações não commitadas — este build não corresponde exatamente a um commit da baseline."
    fi
    SOURCE_DATE_EPOCH="$(git log -1 --pretty=%ct 2>/dev/null || true)"
fi
if [[ -z "${SOURCE_DATE_EPOCH:-}" ]]; then
    warn "SOURCE_DATE_EPOCH não pôde ser derivado do Git; usando o instante atual."
    SOURCE_DATE_EPOCH="$(date -u +%s)"
fi
if date -u -d "@${SOURCE_DATE_EPOCH}" +%Y-%m-%dT%H:%M:%SZ >/dev/null 2>&1; then
    BUILD_TS="$(date -u -d "@${SOURCE_DATE_EPOCH}" +%Y-%m-%dT%H:%M:%SZ)"   # GNU date
else
    BUILD_TS="$(date -u -r "${SOURCE_DATE_EPOCH}" +%Y-%m-%dT%H:%M:%SZ)"    # BSD/macOS date
fi
export SOURCE_DATE_EPOCH
ok "commit: ${GIT_COMMIT:-desconhecido} (${GIT_DESCRIBE:-sem tag}) — timestamp ancorado em ${BUILD_TS}"

step "Limpando artefatos de builds anteriores"
rm -rf target dependency-reduced-pom.xml
ok "target/ e dependency-reduced-pom.xml removidos"

step "Compilando, executando testes e empacotando (mvn clean package)"
mvn -B clean package "-Dproject.build.outputTimestamp=${BUILD_TS}"
ok "build Maven concluído com sucesso"

JAR_PATH=""
while IFS= read -r f; do JAR_PATH="$f"; break; done < <(
    find target -maxdepth 1 -name '*.jar' ! -name 'original-*' 2>/dev/null | sort
)
[ -n "$JAR_PATH" ] && [ -f "$JAR_PATH" ] || fail "nenhum .jar final encontrado em target/ (verifique o build do maven-shade-plugin)"
ok "artefato gerado: ${JAR_PATH}"

if [ "$SYFT_AVAILABLE" -eq 1 ]; then
    step "Gerando SBOM (Syft, CycloneDX JSON)"
    syft dir:. -o "cyclonedx-json=${SBOM_OUT}" >/dev/null
    ok "SBOM gerado em ${SBOM_OUT}"
else
    step "Pulando geração de SBOM (Syft ausente)"
fi

step "Calculando hashes SHA-256 dos artefatos gerados"
{
    printf '%s  %s\n' "$(sha256_of "$JAR_PATH")" "$JAR_PATH"
    [ -f "$SBOM_OUT" ] && printf '%s  %s\n' "$(sha256_of "$SBOM_OUT")" "$SBOM_OUT"
} > "$HASHES_OUT"
ok "hashes salvos em ${HASHES_OUT}"
cat "$HASHES_OUT"

step "Gravando manifesto do build (proveniência)"
ARTIFACTS=("$JAR_PATH")
[ -f "$SBOM_OUT" ] && ARTIFACTS+=("$SBOM_OUT")
artifacts_json="" first=true
for a in "${ARTIFACTS[@]}"; do
    hash="$(sha256_of "$a")"
    size="$(wc -c < "$a" | tr -d ' ')"
    [[ "$first" == true ]] && first=false || artifacts_json+=","
    artifacts_json+=$'\n    {'
    artifacts_json+="\"name\":\"$(json_str "$(basename "$a")")\",\"sha256\":\"$hash\",\"bytes\":$size"
    artifacts_json+='}'
done
cat > "$BUILD_INFO_OUT" <<EOF
{
  "project": "TripLog",
  "buildTimestampUtc": "$(json_str "$BUILD_TS")",
  "sourceDateEpoch": ${SOURCE_DATE_EPOCH},
  "git": {
    "commit": "$(json_str "${GIT_COMMIT:-}")",
    "branch": "$(json_str "${GIT_BRANCH:-}")",
    "describe": "$(json_str "${GIT_DESCRIBE:-}")",
    "dirty": ${GIT_DIRTY}
  },
  "tools": {
    "java": "$(json_str "$JAVA_VERSION")",
    "maven": "$(json_str "$MVN_VERSION")",
    "syft": "$(json_str "$SYFT_VERSION")"
  },
  "artifacts": [${artifacts_json}
  ]
}
EOF
ok "manifesto gravado em ${BUILD_INFO_OUT}"

step "Verificando integridade do build"
sha256_check "$HASHES_OUT" >/dev/null || fail "hashes não correspondem aos arquivos gerados"
ok "hashes verificados"

MANIFEST_DIR="$(mktemp -d)"
( cd "$MANIFEST_DIR" && jar --extract --file="$ROOT_DIR/$JAR_PATH" META-INF/MANIFEST.MF )
MAIN_CLASS=$(tr -d '\r' < "$MANIFEST_DIR/META-INF/MANIFEST.MF" | grep '^Main-Class:' || true)
rm -rf "$MANIFEST_DIR"
[ -n "$MAIN_CLASS" ] || fail "manifest do .jar não contém Main-Class"
ok "manifest: ${MAIN_CLASS}"

if jar tf "$JAR_PATH" | grep -qi '/test/'; then
    fail "o .jar final contém classes de teste — verifique a configuração do maven-shade-plugin"
fi
ok "nenhuma classe de teste embarcada no .jar final"

step "Build reprodutível concluído com sucesso"
printf "${C_GREEN}%s${C_RESET}\n" "Artefato: ${JAR_PATH}"
[ -f "$SBOM_OUT" ] && printf "${C_GREEN}%s${C_RESET}\n" "SBOM:     ${SBOM_OUT}"
printf "${C_GREEN}%s${C_RESET}\n" "Hashes:   ${HASHES_OUT}"
printf "${C_GREEN}%s${C_RESET}\n" "Manifesto: ${BUILD_INFO_OUT}"
