# Documento de Descrição de Versão (VDD) — TripLog

**Equipe:** Hugo Pereira Borges, Stephano Soares Viglio, Omar Al Jawabri

---

## 1. Identificação da Baseline

| Item | Valor |
|---|---|
| Produto | TripLog (trabalho-final-poo) |
| Repositório | github.com/omaraljawabri/trabalho-final-poo |
| Branch | `main` |
| Identificador da baseline (tag) | `v1.0.0-rc1` |
| Commit apontado pela tag | `cd32488cdd42080160fb7b8b7e61496982e72931` |
| Identificador do SBOM associado | `urn:uuid:89daa521-f5c6-424e-8ac3-11a709099bd4` (campo `serialNumber` do CycloneDX) |
| Total de commits incluídos na baseline | 107 (de `0634ffd`, 05/06/2025, até `cd32488`, 18/06/2026) |
| Documento de auditoria associado | `RELATORIO_AUDITORIA_CM.md` |
| Data de emissão deste VDD | 18/06/2026 |

A combinação **tag `v1.0.0-rc1` + commit `cd32488`** é o identificador único desta baseline:
qualquer pessoa que faça `git checkout v1.0.0-rc1` recupera exatamente o mesmo código-fonte
que foi auditado e descrito neste documento.

## 2. Inventário de Modificações (Matriz de Rastreabilidade)

A baseline reúne todo o histórico do projeto. Como o grupo não usou um sistema formal de
chamados, as "solicitações de mudança" rastreáveis são as Pull Requests do GitHub (para o
trabalho feito em branches separadas) e os commits diretos à `main` (para ajustes pontuais).

### 2.1 Funcionalidades e correções entregues por Pull Request

| PR | Branch | Tipo | O que entregou |
|---|---|---|---|
| #4 | `feature/telas-swing` | Nova funcionalidade | Primeiras telas da interface gráfica (Swing) |
| #6 | `UI/SwingInterface` | Nova funcionalidade | Evolução da interface Swing |
| #7 | `UI/Perfil_Usuário` | Nova funcionalidade | Tela de perfil do usuário |
| #8 | `feature/filtros-backend` | Nova funcionalidade | Filtros de busca no backend |
| #9 | `UI/Perfil_Usuário` | Nova funcionalidade | Ajustes na tela de perfil |
| #10 | `UI/Perfil_Usuário` | Nova funcionalidade | Ajustes na tela de perfil |
| #11 | `feature/integracao-front-back` | Nova funcionalidade | Integração da interface com o backend |
| #12 | `UI/home-botao-perfil` | Nova funcionalidade | Botão de acesso ao perfil na tela inicial |
| #13 | `feature/integracao-cadastro-viagem` | Nova funcionalidade | Integração da tela de cadastro de viagem |
| #15 | `refactor/retira-codigo-nao-usado` | Refatoração | Remoção de código morto |
| #16 | `fix/mudanca-nomes` | Correção de bug | Correção de nomes inconsistentes |
| #17 | `feature/integracao-lista-viagens` | Nova funcionalidade | Integração da listagem de viagens |
| #18 | `fix/implementar-botoes` | Correção de bug | Implementação/correção de botões não funcionais |
| #19 | `Fix/RedirecionamentoDeBotoes` | Correção de bug | Correção de redirecionamento de botões |
| #20 | `Fix/RedirecionamentoDeBotoes` | Correção de bug | Correção adicional de redirecionamento |
| #21 | `feature/javadoc` | Documentação | Javadoc das classes do backend |

### 2.2 Ajustes diretos na `main` após a última PR (foco desta auditoria de CM)

| Commit | Tipo | Descrição |
|---|---|---|
| `a7870ec` | Build/Infra | Criação do `.jar` (build pela IDE) |
| `0ee1f76` | Documentação | Geração do Javadoc |
| `5149325` | Build/Infra | Atualização do `.gitignore` |
| `7d6765e`, `44be7b3`, `78d07c0` | Documentação | Criação/atualização do `README.md` |
| `c1ba073` | Nova funcionalidade (build) | Adição do `pom.xml` — migração do build para Maven |
| `0cd74ae` | Build/Infra | Atualização do `.gitignore` para artefatos do Maven |
| `cd32488` | **Correção de bug** | Corrige mensagem de erro genérica ("Credenciais inválidas") exibida para qualquer falha no cadastro de viagem, incluindo campos obrigatórios não preenchidos |

### 2.3 Desenvolvimento inicial (antes do fluxo de Pull Request, 19 commits)

Do commit `0634ffd` ("Init commit") até `a863fca` (PR #4), o desenvolvimento ocorreu direto na
`main`: criação das entidades do sistema, CRUD de viagem e viajante, exceções customizadas,
autenticação com senha criptografada e os primeiros testes unitários. Esse período corresponde
à fundação do backend sobre a qual todas as funcionalidades posteriores foram construídas.

## 3. Composição do Software / SBOM

Inventário gerado com **Syft v1.45.1** em formato **CycloneDX 1.6**, arquivo
`sbom-v1.0.0-rc1.json` (12 componentes identificados):

| Componente | Versão | Origem (`foundBy`) |
|---|---|---|
| TripLog | 1.0.0 | `pom.xml` (declarado) e `target/*.jar` (materializado) |
| com.formdev:flatlaf | 3.4 | `pom.xml` (declarado) |
| org.junit.jupiter:junit-jupiter | 5.8.1 | `pom.xml` (declarado), escopo `test` |
| TripLog | UNKNOWN | `.jar` legado pré-Maven, ainda versionado em `/TripLog.jar` e `/out/artifacts/` |

Apenas duas dependências de terceiros compõem o software: **FlatLaf** (tema visual das telas)
e **JUnit Jupiter** (usado somente para os testes automatizados, excluído do artefato final
pelo `maven-shade-plugin`).

### Hashes criptográficos (SHA-256) dos arquivos gerados nesta baseline

| Arquivo | SHA-256 |
|---|---|
| `sbom-v1.0.0-rc1.json` | `b0c6d6cac61a3d3e3e7ab995e85fa24799f6902c99ebea43cd755bbdc87f301c` |
| `RELATORIO_AUDITORIA_CM.md` | `332d69b0c088b6aa6d5ca3021d99fab352c533d3f7d184f4b8e6405f6a45ad85` |
| `pom.xml` | `2508ce5a5e10021f3c569255aa13d2b5dff8f601dd2c345554477f9e09079de6` |
| `target/TripLog-1.0.0.jar` (artefato de release, gerado por `mvn clean package`) | `070681edd3de08fe05b13ad872ea8a71dcac3aaeb00ade41f649945b4d49d735` |
| `trivy-v1.0.0-rc1.json` (resultado do scan de vulnerabilidades, seção 4) | `e090e182b39fe113a6b24a7d82214b075d815594200b5cb1f3257f41503deb22` |

## 4. Problemas Conhecidos e Limitações

**Vulnerabilidades conhecidas (CVEs):** rodamos o Trivy v0.71.1 (base de vulnerabilidades
`mirror.gcr.io/aquasec/trivy-db:2`, baixada em 18/06/2026) contra o `pom.xml` e contra o SBOM
gerado, salvando o resultado em `trivy-v1.0.0-rc1.json`. **Nenhuma CVE conhecida foi
encontrada** para `flatlaf 3.4` ou `junit-jupiter 5.8.1`
nesta data. Isso não é garantia permanente: bases de vulnerabilidade são atualizadas
continuamente, então uma biblioteca sem CVE hoje pode receber uma divulgação no futuro — por
isso recomendamos repetir esse scan a cada nova release, e não tratar este resultado como
definitivo. Uma segunda ferramenta (Grype) foi tentada como contraprova, mas não pôde
completar o download da própria base de dados por falta de espaço em disco no ambiente usado
nesta auditoria — a cobertura de vulnerabilidades desta baseline depende, por ora, só do Trivy.

**Duplicação de artefato de build:** o repositório ainda mantém, versionados no Git, dois
`.jar` antigos (`/TripLog.jar` e `/out/artifacts/TripLog_jar/TripLog.jar`), gerados pela IDE
antes da migração para Maven. Eles aparecem no SBOM ao lado do `target/TripLog-1.0.0.jar`
oficial, o que pode confundir quem for auditar versões futuras sobre qual artefato é o
realmente distribuído. Recomendação: remover esses arquivos do controle de versão antes da
release final.

**Persistência em arquivo, não em banco de dados:** viagens e viajantes são salvos em arquivos
serializados (`.ser`) no disco local. Isso significa que não há suporte a múltiplos usuários
simultâneos nem garantias transacionais — uma limitação arquitetural conhecida desta primeira
versão, não um defeito a ser corrigido com urgência, mas um ponto de atenção para releases
futuras que pretendam suportar mais de um usuário ao mesmo tempo.

**Escopo de dependências não visível no SBOM:** o CycloneDX gerado pelo Syft não registra que
`junit-jupiter` é uma dependência de teste (`scope=test` no Maven) — quem olhar só o JSON, sem
acesso ao `pom.xml`, não sabe que esse componente não deveria aparecer no artefato de produção
(e, de fato, não aparece — confirmado manualmente na auditoria). É uma limitação do formato,
não um erro de configuração do projeto.
