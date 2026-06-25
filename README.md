# ✈️ TripLog

Aplicação desktop desenvolvida em Java com Swing, como projeto final da disciplina de **Programação Orientada a Objetos – 2025.1**.

## 📝 Funcionalidades

- Sistema de **cadastro e login** de usuários.
- **Edição de perfil** do usuário.
- **Cadastro de viagens**, incluindo hospedagem, deslocamento e atividades realizadas.
- **Listagem de viagens** cadastradas.
- **Filtros** por destino, companhia e gasto mínimo.
- **Edição** de viagens.
- **Remoção** de viagens.

## 🚀 Como executar

1. **Pré-requisitos**: É necessário ter o Java JDK 11 ou superior instalado.

   Verifique a versão instalada com os comandos:
   ```bash
   $ java --version
   $ javac --version
   ```

2. **Baixe ou clone o repositório**:
   ```bash
   $ git clone https://github.com/SEU-USUARIO/TripLog.git
   $ cd TripLog
   ```

3. **Execute o arquivo `.jar`**:

   O arquivo `TripLog.jar` está localizado na raiz do repositório.

   No terminal (Linux/macOS) ou prompt de comando (Windows), execute:
   ```bash
   $ java -jar TripLog.jar
   ```

   > 💡 Dica: se o duplo clique no `.jar` não funcionar, use o comando acima.

## 🔁 Build reprodutível

A partir da versão Maven do projeto (`pom.xml`), o build completo pode ser reproduzido em
uma única linha de comando, a partir de um ambiente limpo:

```bash
$ ./build.sh
```

O script valida as ferramentas necessárias (JDK 21+, Maven), limpa artefatos de builds
anteriores, compila o código, executa a suíte de 72 testes automatizados (JUnit 5),
empacota o `.jar` final, gera um SBOM (CycloneDX, via Syft, se disponível), calcula hashes
SHA-256 dos artefatos gerados e verifica a integridade do resultado. Use `./build.sh --help`
para ver todas as etapas em detalhe.

## 👥 Integrantes do grupo

| Matrícula | Nome                   | GitHub                                  |
|----------:|------------------------|------------------------------------------|
| 202403075 | Hugo Pereira Borges    | [@Hugo-PBorges](https://github.com/Hugo-PBorges) |
| 202403088 | Omar Al Jawabri        | [@omaraljawabri](https://github.com/omaraljawabri) |
| 202403094 | Stephano Soares Viglio | [@StephanoViglio](https://github.com/StephanoViglio) |

## 🛠️ Tecnologias utilizadas

- **Java 11**
- **Java Swing** (para a interface gráfica)
- **FlatLaf** (tema moderno e responsivo)
- **JDK serialization** (para persistência dos dados em arquivos)

## 📂 Estrutura do projeto

```
TripLog/
├── src/
│   ├── backend/
│   │   └── main/
│   │       ├── entities/       # Classes como Viagem, Viajante etc.
│   │       ├── exceptions/     # Exceções customizadas
│   │       ├── repositories/   # Repositórios de persistência
│   │       ├── services/       # Lógica de negócio
│   │       └── utils/          # Utilitários diversos
│   ├── frontend/
│   │   ├── framesUI/           # Telas Swing (JFrames)
│   │   └── resources.images/   # Imagens e ícones da interface
│   └── test.unit/              # Testes unitários
├── javadoc                     # Diretório que contém a documentação Javadoc do projeto
├── TripLog.jar                 # Arquivo .jar
├── viagem.ser                  # Arquivo de dados serializados de viagens (gerado ao criar viagens)
├── viajante.ser                # Arquivo de dados serializados de usuários (gerado ao criar usuários)
└── README.md
```

## ❓ Dúvidas frequentes

**"O .jar não abre ao dar duplo clique!"**  
→ Tente rodar via terminal com: `java -jar TripLog.jar`

**"Onde os dados são salvos?"**  
→ Os dados são persistidos localmente em arquivos `.ser` (serialização Java).
