# Relatório de Auditoria de Configuração — TripLog

**Equipe:** Hugo Pereira Borges, Stephano Soares Viglio, Omar Al Jawabri

## 1. Identificação da Baseline

Esta auditoria avalia a **primeira release do TripLog** (`v1.0.0-rc1`), que reúne todo o
trabalho do grupo desde o início do projeto. A baseline corresponde a todo o histórico da
branch `main`, já publicada em `origin/main`:

| Item | Valor |
|---|---|
| Projeto | TripLog (trabalho-final-poo) |
| Repositório | github.com/omaraljawabri/trabalho-final-poo |
| Branch / estado | `main`, sincronizada com `origin/main` |
| Total de commits na baseline | 107 |
| Primeiro commit | `0634ffd` — "Init commit" (05/06/2025) |
| Commit mais recente da baseline | `cd32488` — "fix(frame): corrigir erro exibido ao não preenchimento de dados obrigatório" |
| Contribuintes | Omar Al Jawabri, Hugo Pereira Borges, Stephano Soares Viglio |
| Versão candidata à release | `v1.0.0-rc1` (ainda sem tag formal no Git — sugestão ao final do relatório) |
| Ferramenta de inventário | Syft v1.45.1 |
| Formato do inventário | CycloneDX 1.6 (JSON) |
| Arquivo gerado | `sbom-v1.0.0-rc1.json` |
| Build verificado | `mvn clean package` — sucesso, todos os 72 testes automatizados passando |
| Data da auditoria | 18/06/2026 |

## 2. O que essa release contém

Ao longo dos 107 commits, o grupo construiu o sistema completo de ponta a ponta:

- **Backend:** entidades (Viagem, Viajante, Hospedagem, Atividade, Deslocamento, Evento,
  Passeio, Restaurante), repositórios com persistência em arquivo serializado (`.ser`),
  serviços de autenticação (com criptografia de senha) e de gestão de viagens, exceções de
  domínio customizadas e utilitários (validação de e-mail, senha).
- **Frontend:** interface gráfica em Swing com telas de login, cadastro de usuário, home,
  perfil, cadastro/listagem/detalhamento de viagens, com tema visual FlatLaf.
- **Qualidade:** suíte de 72 testes unitários (JUnit 5) e documentação Javadoc completa das
  classes do backend.

Os três últimos commits desta baseline (`c1ba073`, `0cd74ae`, `cd32488`) são o ajuste final
que torna essa versão apta a ser uma release candidate, e são o foco específico desta
auditoria de Gerência de Configuração — já que tratam diretamente da rastreabilidade das
dependências e do empacotamento do produto:

- **Adição do `pom.xml`** (`c1ba073`) — o projeto, que antes só compilava e empacotava pelo
  IntelliJ, passa a ter um arquivo de build padrão de mercado (Maven), legível por qualquer
  pessoa ou ferramenta fora do grupo.
- **Ajuste do `.gitignore`** (`0cd74ae`) — passam a ser ignorados os artefatos que o Maven
  gera a cada build (pasta `target/` e o arquivo auxiliar `dependency-reduced-pom.xml`),
  evitando que lixo de build seja versionado por engano.
- **Correção de um bug de usabilidade no cadastro de viagem** (`cd32488`) — antes, qualquer
  erro ao cadastrar uma viagem (inclusive simplesmente esquecer de preencher um campo
  obrigatório) exibia a mensagem "Credenciais inválidas", que não tinha nada a ver com o
  problema real e confundia o usuário. Agora a mensagem mostra o motivo verdadeiro do erro.

## 3. Itens de Configuração Declarados (inspeção do `pom.xml`)

O `pom.xml` é o arquivo de configuração de dependências do projeto. Nele estão mapeados dois
Itens de Configuração abstratos:

| Dependência | Versão | Para que serve |
|---|---|---|
| FlatLaf | 3.4 | Biblioteca de aparência (tema visual) das telas do TripLog |
| JUnit Jupiter | 5.8.1 | Framework usado só para os testes automatizados, não entra no programa final |

Antes da release, o FlatLaf era citado por nome nos arquivos da IDE, mas sem nenhuma versão
associada — ou seja, ninguém conseguia dizer com certeza qual versão estava realmente em uso.
Agora essa informação está fixada e visível em um único lugar.

## 4. Inventário Gerado pela Ferramenta (Syft)

Rodamos o Syft (uma ferramenta de linha de comando que lê o projeto e devolve um inventário
em formato CycloneDX, o mesmo padrão usado por grandes empresas para documentar de forma
confiável o que compõe um software) apontando para a raiz do repositório. O resultado foi o
arquivo `sbom-v1.0.0-rc1.json`.

Dentro desse JSON, dois pontos merecem destaque:

- **Metadados da ferramenta:** o próprio arquivo se identifica — ele registra que foi gerado
  pelo Syft, versão 1.45.1, da empresa Anchore. Isso é importante porque permite rastrear,
  no futuro, com qual ferramenta e versão aquele inventário foi produzido.
- **Itens encontrados:** o Syft encontrou 12 itens no total. Os mais relevantes são o próprio
  TripLog (versão 1.0.0, lida diretamente do `pom.xml`), o FlatLaf (3.4) e o JUnit Jupiter
  (5.8.1) — os dois últimos também lidos diretamente do `pom.xml`, sem precisar abrir nenhum
  `.jar` manualmente. Isso mostra a diferença entre uma dependência **declarada** (o Syft leu
  do arquivo de configuração, antes mesmo de compilar) e uma dependência **materializada**
  (o Syft leu de um `.jar` já compilado, encontrado na pasta `target/` ou em arquivos `.jar`
  antigos que ainda estão no repositório).

Uma limitação que vale registrar: o inventário não indica que o JUnit é usado só nos testes
(não vai para o programa final) — essa informação de "escopo" existe no `pom.xml`, mas não
aparece no JSON gerado pela ferramenta. Não é um erro do projeto, é uma limitação do formato.

## 5. Resultado da Auditoria Física

A Auditoria Física verifica se o que existe fisicamente no repositório corresponde ao que
está documentado. O resultado foi **aprovado, com uma ressalva**:

- O programa final (`target/TripLog-1.0.0.jar`, gerado pelo Maven) contém exatamente o que
  deveria: o código da aplicação e o FlatLaf, sem nenhuma classe de teste do JUnit embarcada
  por engano — confirmamos isso abrindo o `.jar` e contando os arquivos internos.
- **Ressalva:** o repositório ainda mantém, versionados no Git, dois arquivos `.jar`
  antigos (`TripLog.jar` na raiz e uma cópia idêntica em `out/artifacts/TripLog_jar/`),
  produzidos pela IDE antes dessa release. Eles não são mais o artefato oficial do projeto,
  mas continuam aparecendo no inventário lado a lado com o `.jar` novo gerado pelo Maven, o
  que pode confundir uma futura auditoria sobre qual artefato é o de fato distribuído.

## 6. Recomendações

- Remover os `.jar` antigos (`TripLog.jar` e a pasta `out/`) do controle de versão e, se for
  necessário disponibilizar o executável, publicá-lo como anexo de uma release no GitHub.
- Quando o grupo decidir formalizar essa versão como release, criar uma tag anotada no commit
  `cd32488`, por exemplo:
  ```
  git tag -a v1.0.0-rc1 -m "Release candidate 1: build Maven, FlatLaf versionado, correção de erro no cadastro de viagem"
  git push origin v1.0.0-rc1
  ```
- Repetir a geração do inventário (Syft) a cada nova release e guardar o JSON junto da tag
  correspondente, para que auditorias futuras não dependam de execução manual.
