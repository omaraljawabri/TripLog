# Documento de Descrição de Versão (VDD) — TripLog — v1.0.0-rc2

**Equipe:** Hugo Pereira Borges, Stephano Soares Viglio, Omar Al Jawabri

---

## 1. Identificação da Baseline

| Item | Valor |
|---|---|
| Produto | TripLog (trabalho-final-poo) |
| Repositório | github.com/omaraljawabri/TripLog |
| Branch | `main` |
| Identificador da baseline (tag) | `v1.0.0-rc2` |
| Commit apontado pela tag | `9ead6ba03bcef3c1ad7a34499ec75e2c7177bab8` |
| Baseline anterior | `v1.0.0-rc1` (commit `cd32488`, ver `VDD.md`) |
| Commits desde a `v1.0.0-rc1` | 16 (`git log cd32488..9ead6ba`) |
| Identificador do SBOM associado | `urn:uuid:e3d35bbb-196c-41db-a797-6c46e11ed047` (campo `serialNumber` do CycloneDX) |
| Documento de auditoria associado | `RELATORIO_AUDITORIA_CM.md` (inalterado desde a rc1) |
| Data de emissão deste VDD | 25/06/2026 |

Esta baseline **não é uma nova release do produto do ponto de vista funcional** — nenhuma tela,
regra de negócio ou comportamento da aplicação mudou desde a `v1.0.0-rc1`. Ela existe porque a
auditoria de Gerência de Configuração feita sobre a rc1 (e uma revisão externa subsequente)
identificou lacunas reais no **processo de build** daquela candidate, que foram corrigidas e
agora precisam de sua própria baseline rastreável, em vez de serem remendadas dentro do VDD já
congelado da rc1.

A combinação **tag `v1.0.0-rc2` + commit `9ead6ba`** é o identificador único desta baseline:
`git checkout v1.0.0-rc2` recupera exatamente o código e o processo de build descritos aqui.

## 2. Inventário de Modificações (Matriz de Rastreabilidade)

Todas as mudanças abaixo aconteceram entre o commit da `v1.0.0-rc1` (`cd32488`) e o commit
desta baseline (`9ead6ba`). Comandos usados para levantar esta lista:
`git log --oneline cd32488..9ead6ba` e a leitura de cada diff individualmente.

| Commit | Tipo | Descrição |
|---|---|---|
| `6942db4` | Documentação | SBOM da `v1.0.0-rc1` (`sbom-v1.0.0-rc1.json`) |
| `9cb946b` | Documentação | Relatório inicial de auditoria de configuração (`RELATORIO_AUDITORIA_CM.md`) |
| `d05fa77` | Documentação | VDD da `v1.0.0-rc1` e scan de vulnerabilidades (Trivy) |
| `e50bc70` | Limpeza | Remoção de diretório `.claude` não utilizado |
| `87e3181` | Documentação | Atualização de status da tag `v1.0.0-rc1` no VDD |
| `597071c` | Merge | Merge de `main` (sem conteúdo próprio) |
| `b4cbc4c` | Ajuste | Correção de formatação do JSON do SBOM |
| `0cc4d7f` | Ajuste | Correção da URL do repositório no VDD |
| `7f3f94c` | **Nova funcionalidade (CM)** | Criação do `dependency-lock.json`: versões exatas e hashes SHA-256 de todas as dependências (diretas e transitivas) |
| `f0079d3` | Merge | Merge da PR #24 (`feat/dependency-lock`) |
| `60cbe27` | **Correção (determinismo)** | Fixação da versão do `maven-jar-plugin` (3.4.1) — a versão padrão antiga não respeitava `project.build.outputTimestamp`, fazendo o hash do `.jar` variar a cada build mesmo para o mesmo commit |
| `678c927` | Documentação | Seção sobre o build reprodutível no `README.md` |
| `7cab3c2` | **Nova funcionalidade (CM)** | Criação do `build.sh`: build completo, testes, empacotamento, SBOM e hashes em um único comando |
| `3dee4ab` | Correção de bug | Ajustes recomendados em revisão (fallback `shasum` no macOS, descoberta dinâmica do `.jar`, validação de versões mínimas de JDK/Maven) |
| `d6d2319` | Correção de bug | Remoção da dependência de `unzip` (substituída pela ferramenta `jar`, já exigida pelo JDK) |
| `9ead6ba` | **Nova funcionalidade (CM) / Correção de bug** | `dependency-lock.json` passa a ser **verificado automaticamente** (portão de build, falha em divergência de hash), aviso de divergência de patch do JDK, normalização determinística da ordem de entradas do `.jar` entre sistemas operacionais, `mvn --strict-checksums`, e correção de um bug que quebrava `./build.sh --help` |

Um commit adicional, **fora** desta tabela, está em `main` mas não compõe esta baseline:
`6bea5b7` adicionou ao `VDD.md` da rc1 uma seção descrevendo estes mesmos reforços de build —
decisão revertida (commit posterior a este VDD) por misturar conteúdo da rc2 dentro do
documento já congelado da rc1. Este `VDD-v1.0.0-rc2.md` é o lugar correto para esse conteúdo.

## 3. Composição do Software / SBOM

As dependências de terceiros **não mudaram** desde a `v1.0.0-rc1`: **FlatLaf 3.4** (tema visual)
e **JUnit Jupiter 5.8.1** (testes, escopo `test`, excluído do artefato final). O que muda nesta
baseline é a forma como essas dependências são verificadas: o `dependency-lock.json` (PR #24)
agora é aplicado automaticamente pelo `build.sh` — a cada build, o SHA-256 de cada dependência
resolvida em `~/.m2` é recalculado e comparado com o hash gravado no lockfile, falhando o build
em caso de divergência.

Inventário gerado com **Syft v1.45.1** em formato **CycloneDX 1.6**, arquivo
`sbom-v1.0.0-rc2.json` (10 componentes identificados, gerado a partir de um `git worktree`
limpo no commit `9ead6ba`, sem contaminação de arquivos não rastreados):

| Componente | Versão | Origem (`foundBy`) |
|---|---|---|
| TripLog | 1.0.0 | `pom.xml` (declarado) e `target/*.jar` (materializado) |
| com.formdev:flatlaf | 3.4 | `pom.xml` (declarado) |
| org.junit.jupiter:junit-jupiter | 5.8.1 | `pom.xml` (declarado), escopo `test` |
| TripLog | UNKNOWN | `.jar` legado pré-Maven, ainda versionado em `/TripLog.jar` |

### Hashes criptográficos (SHA-256) dos arquivos desta baseline

| Arquivo | SHA-256 |
|---|---|
| `sbom-v1.0.0-rc2.json` | `ad037d8b72277d8430d44365fd6300a2c133958a4572f27e21d97e87dc99bb54` |
| `pom.xml` | `54daed0e31a4f39c6407ad8bbc6a983e44306f2c4399542f9abb3d0e63d93d31` |
| `dependency-lock.json` | `ee34a914ab26ea89cfaa0fd3add5a309d7b443f0cb0e23ea688b07035f484bb5` |
| `build.sh` | `9316c6de5d56e45326b1e92c065a553ca6ae07cecf391d95ebba5edd66425c76` |
| `target/TripLog-1.0.0.jar` (gerado por `./build.sh` no commit `9ead6ba`) | `f1290e3e21940b496bad4fbabd4bec25e390775df5c5a9423ed7200de16f6c77` |
| `trivy-v1.0.0-rc2.json` (resultado do scan de vulnerabilidades, seção 4) | `bb8e7c6d2f7c3daab77bf17c2ffdef15fbe5ded083b0a2ad2e88fa2f8e1a7c1d` |

O hash do `.jar` foi confirmado reprodutível: builds repetidos do mesmo commit produzem
exatamente este mesmo SHA-256, incluindo a normalização determinística da ordem de entradas
introduzida nesta baseline.

## 4. Problemas Conhecidos e Limitações

**Vulnerabilidades conhecidas (CVEs):** rodamos o Trivy v0.71.2 (base de vulnerabilidades
`mirror.gcr.io/aquasec/trivy-db:2`, baixada em 25/06/2026 — atualizada havia poucas horas
nesse momento) contra o `pom.xml`, a partir do mesmo `git worktree` limpo no commit `9ead6ba`
usado para gerar o SBOM desta baseline, salvando o resultado em `trivy-v1.0.0-rc2.json`.
**Nenhuma CVE conhecida foi encontrada** para `flatlaf 3.4` ou `junit-jupiter 5.8.1` nesta
data — mesmo resultado da `v1.0.0-rc1`, o que é esperado já que nenhuma dependência de
terceiros mudou de versão entre as duas baselines. Como já registrado na rc1, isso não é
garantia permanente: recomendamos repetir o scan a cada nova release.

**Lockfile como portão de build:** a verificação automática do `dependency-lock.json` depende
da ferramenta `jq` estar disponível no ambiente de build. Sem `jq`, o `build.sh` segue
(não bloqueia), mas a verificação automática é pulada com aviso — o lockfile permanece como
documentação manual nessa execução específica. Recomendação: garantir `jq` no ambiente de CI.

**JDK: patch não é travado:** o build valida apenas a versão major mínima (21+); o patch exato
(ex.: `21.0.11` vs. o `21.0.7` registrado no `dependency-lock.json`) gera só um aviso
informativo, nunca falha o build. Decisão deliberada: travar o patch exato comprometeria a
portabilidade entre máquinas/CI sem ganho real de segurança.

**Ordem de entradas do `.jar` — resolvido nesta baseline:** a `v1.0.0-rc1` tinha uma lacuna real
e confirmada na prática: a ordem das entradas dentro do `.jar` refletia a travessia do sistema
de arquivos local (não era alfabética), então o mesmo commit podia gerar um `.jar` com hash
diferente em Linux e Windows mesmo com bytecode idêntico. Esta baseline resolve isso: o
`build.sh` agora reempacota o `.jar` final com `META-INF/MANIFEST.MF` primeiro e o restante em
ordem alfabética fixa, usando um timestamp único para todas as entradas.

**Build em árvore suja:** o `build.sh` detecta e avisa quando há alterações não commitadas na
árvore de trabalho (`dirty: true` no manifesto `build-info.json`), mas não bloqueia o build
localmente — decisão deliberada para não impedir builds de desenvolvimento do dia a dia.
Builds que representem oficialmente uma baseline, como esta, devem sempre partir de uma árvore
limpa em um commit com tag (foi assim que este SBOM e este hash do `.jar` foram gerados: a
partir de um `git worktree` limpo no commit exato `9ead6ba`, sem `dirty`).

**Itens herdados da `v1.0.0-rc1`, ainda válidos nesta baseline:** duplicação do `.jar` legado
no controle de versão (`/TripLog.jar`), persistência em arquivos `.ser` sem suporte a múltiplos
usuários simultâneos, e escopo de dependências (`test`) não visível no SBOM gerado pelo Syft.
Ver `VDD.md` (rc1) e `RELATORIO_AUDITORIA_CM.md` para o detalhamento original de cada um —
nenhum desses foi alterado por esta baseline.
