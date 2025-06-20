package test.unit;

import main.entities.*;
import main.exceptions.EntityNotFoundException;
import main.exceptions.ValidationException;
import main.repositories.ViagemRepository;
import main.repositories.ViajanteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViajanteTest {

    private static final String NOME_ARQUIVO_VIAGEM = "viagem_test.ser";
    private static final String NOME_ARQUIVO_VIAJANTE = "viajante_test.ser";

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(NOME_ARQUIVO_VIAGEM));
        Files.deleteIfExists(Paths.get(NOME_ARQUIVO_VIAJANTE));
    }

    @Test
    void adicionarViagem_AdicionaViagemAoArquivo_QuandoBemSucedido() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        viajante.adicionarViagem(viagem);

        List<Viagem> viagens = viajante.listarViagens();

        assertFalse(viagens.isEmpty());
        assertEquals(viagem.getLugarDePartida(), viagens.getFirst().getLugarDePartida());
        assertEquals(viagem.getLugarDeChegada(), viagens.getFirst().getLugarDeChegada());
        assertEquals(1, viagens.size());
    }

    @Test
    void adicionarViagem_LancaValidationException_QuandoAtributosObrigatoriosNaoSaoPreenchidos(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);
        viagem.setLugarDeChegada(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> viajante.adicionarViagem(viagem));

        assertEquals("Atributos lugar de partida, lugar de chegada, deslocamentos, hospedagens e atividades devem ser preenchidos", exception.getMessage());
    }

    @Test
    void adicionarViagem_LancaRuntimeException_QuandoAlgumErroOcorreAoAdicionarViagem(){
        ViagemRepository viagemRepository = new ViagemRepository(null);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        assertThrows(RuntimeException.class, () -> viajante.adicionarViagem(viagem));
    }

    @Test
    void listarViagens_RetornaListaDeViagem_QuandoHouveremViagensRegistradas() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viagem viagem3 = criarViagem3();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        viajante.adicionarViagem(viagem);
        viajante.adicionarViagem(viagem2);
        viajante.adicionarViagem(viagem3);

        List<Viagem> viagens = viajante.listarViagens();

        assertEquals(3, viagens.size());
        assertEquals(viagem2.getLugarDeChegada(), viagens.get(1).getLugarDeChegada());
    }

    @Test
    void listarViagens_RetornaListaVazia_QUandoNaoHouveremViagensRegistradas(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);
        List<Viagem> viagens = viajante.listarViagens();

        assertTrue(viagens.isEmpty());
    }

    @Test
    void buscarViagemPorId_RetornaViagem_QuandoIdDaViagemBuscadoExistir() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);
        
        viajante.adicionarViagem(viagem);
        viajante.adicionarViagem(viagem2);
        
        Viagem viagemBuscada = viajante.buscarViagemPorId(1);

        assertNotNull(viagemBuscada);
        assertEquals(1, viagemBuscada.getId());
    }

    @Test
    void buscarViagemPorId_LancaEntityNotFoundException_QuandoNaoHouverViagemComIdPassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> viajante.buscarViagemPorId(2));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    void removerViagem_RemoveViagem_QuandoIdDaViagemPassadoExistir() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        viajante.adicionarViagem(viagem);
        viajante.adicionarViagem(viagem2);

        assertDoesNotThrow(() -> viajante.removerViagem(viagem.getId()));

        List<Viagem> viagens = viajante.listarViagens();

        assertEquals(1, viagens.size());
    }

    @Test
    void removerViagem_LancaEntityNotFoundException_QuandoIdDaViagemPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> viajante.removerViagem(2));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    void editarViagem_EditaViagemComIdPassado_QuandoIdPassadoExistir() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        viagem.setIdViajante(viajante.getId());
        viagem2.setIdViajante(viajante.getId());
        viajante.adicionarViagem(viagem);

        assertDoesNotThrow(() -> viajante.editarViagem(viagem.getId(), viagem2));

        List<Viagem> viagens = viajante.listarViagens();

        assertEquals(1, viagens.size());
        assertEquals(viagem2.getLugarDeChegada(), viagens.getFirst().getLugarDeChegada());
    }

    @Test
    void editarViagem_LancaEntityNotFoundException_QuandoIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> viajante.editarViagem(2, criarViagem1()));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    void cadastrar_CadastraViajante_QuandoDadosDoViajanteEstiveremCorretos(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        assertDoesNotThrow(viajante::cadastrar);
    }

    @Test
    void cadastrar_LancaValidationException_QuandoNomeEmailOuSenhaForemNull(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante(null, "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        ValidationException exception = assertThrows(ValidationException.class, () -> viajante.cadastrar());
        assertEquals("Email, nome e senha devem ser preenchidos", exception.getMessage());
    }

    @Test
    void cadastrar_RetornaFalse_QuandoAlgumErroOcorreAoSalvarViajante(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository("/erro/"+NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        RuntimeException exception = assertThrows(RuntimeException.class, viajante::cadastrar);
        assertEquals("Erro ao fazer cadastro", exception.getMessage());
    }

    @Test
    void login_RetornaTrue_QuandoUsuarioEAutenticadoComSucesso(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        viajante.cadastrar();

        viajante.setSenha("fulano123");

        assertDoesNotThrow(viajante::login);
    }

    @Test
    void login_LancaValidationException_QuandoEmailDoViajanteNaoExisteNoSistema(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        ValidationException exception = assertThrows(ValidationException.class, viajante::login);
        assertEquals("Viajante com email ou senha incorreto(s)!", exception.getMessage());
    }

    @Test
    void login_LancaValidationException_QuandoSenhaDoViajanteEstaIncorreta(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com", viagemRepository, viajanteRepository);

        viajante.cadastrar();

        viajante.setSenha("senhaincorreta123");

        ValidationException exception = assertThrows(ValidationException.class, viajante::login);
        assertEquals("Viajante com email ou senha incorreto(s)!", exception.getMessage());
    }

    private Viagem criarViagem1(){
        Viagem viagem1 = new Viagem();
        viagem1.setLugarDePartida("Rio de Janeiro");
        viagem1.setLugarDeChegada("Salvador");
        viagem1.setCompanhia("João");

        viagem1.setDeslocamentos(List.of(
                new Deslocamento("Avião", 800.0),
                new Deslocamento("Táxi", 50.0)
        ));

        viagem1.setHospedagens(List.of(
                new Hospedagem("Hotel Sol Bahia", 3, 200.0)
        ));

        List<Atividade> atividades1 = new ArrayList<>();
        atividades1.add(new Evento("Festival de Verão",
                List.of(new Gasto(150.0, "Entrada")),
                LocalDateTime.of(2025, 6, 10, 18, 0),
                "Música"));
        atividades1.add(new Passeio("Pelourinho Tour",
                List.of(new Gasto(30.0, "Guia turístico")),
                LocalDateTime.of(2025, 6, 11, 10, 0),
                "Centro Histórico"));
        atividades1.add(new Restaurante("Almoço na Yemanjá",
                List.of(new Gasto(80.0, "Refeição")),
                LocalDateTime.of(2025, 6, 11, 13, 0),
                "Restaurante Yemanjá", "Baiana", "Moqueca"));

        viagem1.setAtividades(atividades1);
        viagem1.setSaldo(1000.0);
        viagem1.setDiasPercorridos(4);
        viagem1.setIdViajante(1);
        return viagem1;
    }

    private Viagem criarViagem2(){
        Viagem viagem2 = new Viagem();
        viagem2.setLugarDePartida("São Paulo");
        viagem2.setLugarDeChegada("Florianópolis");
        viagem2.setCompanhia(null); // Sozinho

        viagem2.setDeslocamentos(List.of(
                new Deslocamento("Ônibus", 200.0),
                new Deslocamento("Uber", 40.0)
        ));

        viagem2.setHospedagens(List.of(
                new Hospedagem("Pousada das Ondas", 2, 150.0)
        ));

        List<Atividade> atividades2 = new ArrayList<>();
        atividades2.add(new Evento("Show de Rock",
                List.of(new Gasto(120.0, "Ingresso")),
                LocalDateTime.of(2025, 7, 20, 20, 0),
                "Música"));
        atividades2.add(new Passeio("Praia da Joaquina",
                List.of(new Gasto(25.0, "Aluguel de guarda-sol")),
                LocalDateTime.of(2025, 7, 21, 9, 0),
                "Litoral"));
        atividades2.add(new Restaurante("Cantina Italiana",
                List.of(new Gasto(70.0, "Almoço")),
                LocalDateTime.of(2025, 7, 21, 13, 0),
                "Cantina Italiana", "Italiana", "Lasanha"));

        viagem2.setAtividades(atividades2);
        viagem2.setSaldo(800.0);
        viagem2.setDiasPercorridos(3);
        viagem2.setIdViajante(1);
        return viagem2;
    }

    private Viagem criarViagem3(){
        Viagem viagem3 = new Viagem();
        viagem3.setLugarDePartida("Belo Horizonte");
        viagem3.setLugarDeChegada("Curitiba");
        viagem3.setCompanhia("Maria");

        viagem3.setDeslocamentos(List.of(
                new Deslocamento("Carro", 400.0)
        ));

        viagem3.setHospedagens(List.of(
                new Hospedagem("Hotel Curitiba Center", 4, 180.0)
        ));

        List<Atividade> atividades3 = new ArrayList<>();
        atividades3.add(new Evento("Congresso de Tecnologia",
                List.of(new Gasto(200.0, "Inscrição")),
                LocalDateTime.of(2025, 8, 15, 9, 0),
                "Tecnologia"));
        atividades3.add(new Passeio("Jardim Botânico",
                List.of(new Gasto(10.0, "Entrada")),
                LocalDateTime.of(2025, 8, 16, 15, 0),
                "Parque"));
        atividades3.add(new Restaurante("Jantar Alemão",
                List.of(new Gasto(90.0, "Jantar")),
                LocalDateTime.of(2025, 8, 16, 19, 30),
                "Bar do Alemão", "Alemã", "Eisbein"));

        viagem3.setAtividades(atividades3);
        viagem3.setSaldo(1500.0);
        viagem3.setDiasPercorridos(5);
        viagem3.setIdViajante(1);
        return viagem3;
    }
}