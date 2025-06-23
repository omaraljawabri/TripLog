package backend.test.unit;

import backend.main.entities.*;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViagemRepository;
import backend.main.services.ViagemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViagemServiceTest {

    private static final String NOME_ARQUIVO_VIAGEM = "viagem_test.ser";

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(NOME_ARQUIVO_VIAGEM));
    }

    @Test
    void adicionarViagem_AdicionaViagemAoArquivo_QuandoBemSucedido() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viagemService.adicionarViagem(viagem, viajante);

        List<Viagem> viagens = viagemService.listarViagens(viajante);

        assertFalse(viagens.isEmpty());
        assertEquals(viagem.getLugarDePartida(), viagens.getFirst().getLugarDePartida());
        assertEquals(viagem.getLugarDeChegada(), viagens.getFirst().getLugarDeChegada());
        assertEquals(1, viagens.size());
    }

    @Test
    void adicionarViagem_LancaValidationException_QuandoAtributosObrigatoriosNaoSaoPreenchidos(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");
        viagem.setLugarDeChegada(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> viagemService.adicionarViagem(viagem, viajante));

        assertEquals("Atributos lugar de partida, lugar de chegada, deslocamentos, hospedagens e atividades devem ser preenchidos", exception.getMessage());
    }

    @Test
    void adicionarViagem_LancaErroInternoException_QuandoAlgumErroOcorreAoAdicionarViagem(){
        ViagemRepository viagemRepository = new ViagemRepository("erro/"+NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        ErroInternoException exception = assertThrows(ErroInternoException.class, () -> viagemService.adicionarViagem(viagem, viajante));
        assertEquals("Erro ao adicionar nova viagem", exception.getMessage());
    }

    @Test
    void listarViagens_RetornaListaDeViagem_QuandoHouveremViagensRegistradas() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viagem viagem3 = criarViagem3();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viagemService.adicionarViagem(viagem, viajante);
        viagemService.adicionarViagem(viagem2, viajante);
        viagemService.adicionarViagem(viagem3, viajante);

        List<Viagem> viagens = viagemService.listarViagens(viajante);

        assertEquals(3, viagens.size());
        assertEquals(viagem2.getLugarDeChegada(), viagens.get(1).getLugarDeChegada());
    }

    @Test
    void listarViagens_RetornaListaVazia_QUandoNaoHouveremViagensRegistradas(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");
        List<Viagem> viagens = viagemService.listarViagens(viajante);

        assertTrue(viagens.isEmpty());
    }

    @Test
    void buscarViagemPorId_RetornaViagem_QuandoIdDaViagemBuscadoExistir() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viagemService.adicionarViagem(viagem, viajante);
        viagemService.adicionarViagem(viagem2, viajante);

        Viagem viagemBuscada = viagemService.buscarViagemPorId(1, viajante);

        assertNotNull(viagemBuscada);
        assertEquals(1, viagemBuscada.getId());
    }

    @Test
    void buscarViagemPorId_LancaEntityNotFoundException_QuandoNaoHouverViagemComIdPassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> viagemService.buscarViagemPorId(2, viajante));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    void removerViagem_RemoveViagem_QuandoIdDaViagemPassadoExistir() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viagemService.adicionarViagem(viagem, viajante);
        viagemService.adicionarViagem(viagem2, viajante);

        assertDoesNotThrow(() -> viagemService.removerViagem(viagem.getId(), viajante));

        List<Viagem> viagens = viagemService.listarViagens(viajante);

        assertEquals(1, viagens.size());
    }

    @Test
    void removerViagem_LancaEntityNotFoundException_QuandoIdDaViagemPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> viagemService.removerViagem(2, viajante));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    void editarViagem_EditaViagemComIdPassado_QuandoIdPassadoExistir() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        Viagem viagem = criarViagem1();
        Viagem viagem2 = criarViagem2();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viagem.setEmailViajante(viajante.getEmail());
        viagem2.setEmailViajante(viajante.getEmail());
        viagemService.adicionarViagem(viagem, viajante);

        assertDoesNotThrow(() -> viagemService.editarViagem(viagem.getId(), viagem2, viajante));

        List<Viagem> viagens = viagemService.listarViagens(viajante);

        assertEquals(1, viagens.size());
        assertEquals(viagem2.getLugarDeChegada(), viagens.getFirst().getLugarDeChegada());
    }

    @Test
    void editarViagem_LancaEntityNotFoundException_QuandoIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> viagemService.editarViagem(2, criarViagem1(), viajante));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    void buscarViagensFiltradas_RetornaListaDeViagens_QuandoHouveremViagensComFiltrosAplicados(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);


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
        viagem1.setEmailViajante("fulano@example.com");
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
        viagem2.setEmailViajante("fulano@example.com");
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
        viagem3.setEmailViajante("fulano@example.com");
        return viagem3;
    }
}