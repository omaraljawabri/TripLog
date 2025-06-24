package backend.test.unit;

import backend.main.entities.*;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViagemRepository;
import backend.main.services.ViagemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("adicionarViagem deve adicionar uma viagem ao arquivo quando a adição for bem sucedida")
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
    @DisplayName("adicionarViagem deve lançar uma ValidacaoException quando atributos obrigatorios nao forem preenchidos para adição da viagem")
    void adicionarViagem_LancaValidacaoException_QuandoAtributosObrigatoriosNaoSaoPreenchidos(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");
        viagem.setLugarDeChegada(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> viagemService.adicionarViagem(viagem, viajante));

        assertEquals("Atributos lugar de partida, lugar de chegada, deslocamentos, hospedagens e atividades devem ser preenchidos", exception.getMessage());
    }

    @Test
    @DisplayName("adicionarViagem deve lançar uma ErroInternoException quando algum erro interno ocorrer ao adicionar uma Viagem")
    void adicionarViagem_LancaErroInternoException_QuandoAlgumErroOcorreAoAdicionarViagem(){
        ViagemRepository viagemRepository = new ViagemRepository("erro/"+NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viagem viagem = criarViagem1();
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        ErroInternoException exception = assertThrows(ErroInternoException.class, () -> viagemService.adicionarViagem(viagem, viajante));
        assertEquals("Erro ao adicionar nova viagem", exception.getMessage());
    }

    @Test
    @DisplayName("listarViagens deve retornar uma Lista de Viagem quando houverem viagens registradas no sistema pertencentes ao viajante passado como parâmetro")
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
    @DisplayName("listarViagens deve retornar uma lista vazia quando não houverem viagens registradas pertencentes ao viajante passado como parâmetro")
    void listarViagens_RetornaListaVazia_QUandoNaoHouveremViagensRegistradas(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");
        List<Viagem> viagens = viagemService.listarViagens(viajante);

        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("removerViagem deve remover uma viagem do arquivo quando o id da viagem passado existir")
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
    @DisplayName("removerViagem deve lançar uma EntidadeNaoEncontradaException quando o id da viagem passado não existir")
    void removerViagem_LancaEntidadeNaoEncontradaException_QuandoIdDaViagemPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> viagemService.removerViagem(2, viajante));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    @DisplayName("editarViagem deve editar a viagem com o id passado quando o id da viagem passado existir")
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
    @DisplayName("editarViagem deve lançar uma EntidadeNaoEncontradaException quando não existir viagem com o id passado")
    void editarViagem_LancaEntidadeNaoEncontradaException_QuandoIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> viagemService.editarViagem(2, criarViagem1(), viajante));

        assertEquals("Viagem com id: 2, não encontrada!", exception.getMessage());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem com dados quando houverem viagens com o destino aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouveremViagensComDestinoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemService.buscarViagensFiltradas("fulano@example.com", "Salvador", null, null);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("Salvador", viagens.getFirst().getLugarDeChegada());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem com dados quando houverem viagens com a companhia aplicada")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouveremViagensComCompanhiaAplicada(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemService.buscarViagensFiltradas("fulano@example.com", null, "João", null);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("João", viagens.getFirst().getCompanhia());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem com dados quando houverem viagens com o gasto superior ao aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouverViagensComGastoSuperiorAoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        double gasto = 500;

        List<Viagem> viagens
                = viagemService.buscarViagensFiltradas("fulano@example.com", null, null, gasto);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertTrue(viagens.getFirst().calcularTotalGastos() > gasto);
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem vazia quando não houver viagens com o destino aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemVazia_QuandoNaoHouveremViagensComDestinoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens = viagemService.buscarViagensFiltradas("fulano@example.com", "sp", null, null);
        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem vazia quando não houver viagens com a companhia aplicada")
    void buscarViagensFiltradas_RetornaListaDeViagemVazia_QuandoNaoHouveremViagensComCompanhiaAplicada(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens = viagemService.buscarViagensFiltradas("fulano@example.com", null, "Fulano", null);
        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem vazia quando não houver viagens com o gasto superior ao aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemVazia_QuandoNaoHouverViagensComGastoSuperiorAoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens = viagemService.buscarViagensFiltradas("fulano@example.com", null, null, 60000D);
        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem com dados quando houver viagens com destino e companhia aplicados")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouverViagensComDestinoECompanhiaAplicados(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemService.buscarViagensFiltradas("fulano@example.com", "Salvador", "João", null);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("João", viagens.getFirst().getCompanhia());
        assertEquals("Salvador", viagens.getFirst().getLugarDeChegada());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem com dados quando houver viagens com destino, companhia aplicados e gasto superior ao aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouverViagensComDestinoCompanhiaEGastoAplicados(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(viagem);

        double gasto = 500;

        List<Viagem> viagens
                = viagemService.buscarViagensFiltradas("fulano@example.com", "Salvador", "João", gasto);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("João", viagens.getFirst().getCompanhia());
        assertEquals("Salvador", viagens.getFirst().getLugarDeChegada());
        assertTrue(viagens.getFirst().calcularTotalGastos() > gasto);
    }

    @Test
    @DisplayName("buscarTodasViagensPorEmailViajante deve retornar uma lista de viagem quando houver viajens com o email do viajante passado")
    void buscarTodasViagensPorEmailViajante_RetornaListaDeViagem_QuandoHouverViagensComEmailViajantePassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        viagemRepository.salvarViagem(criarViagem1());
        viagemRepository.salvarViagem(criarViagem2());
        Viagem viagem = criarViagem3();
        viagem.setEmailViajante("teste");
        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens = viagemService.buscarTodasViagensPorEmailViajante("fulano@example.com");
        assertFalse(viagens.isEmpty());
        assertEquals(2, viagens.size());
    }

    @Test
    @DisplayName("buscarTodasViagensPorEmailViajante deve retornar uma lista vazia quando não houver viagens com o email do viajante passado")
    void buscarTodasViagensPorEmailViajante_RetornaListaVazia_QuandoNaoHouverViagensComEmailViajantePassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        List<Viagem> viagens = viagemService.buscarTodasViagensPorEmailViajante("fulano@example.com");
        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarMaiorIdPorEmailViajante deve retornar o maior id da viagem quando houver viagens cadastradas com email do viajante passado")
    void buscarMaiorIdPorEmailViajante_RetornaMaiorIdDaViagem_QuandoHouverViagemCadastrada(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        Viagem.resetarContador();

        viagemRepository.salvarViagem(criarViagem1());
        viagemRepository.salvarViagem(criarViagem2());
        viagemRepository.salvarViagem(criarViagem3());

        int maiorId = viagemService.buscarMaiorIdPorEmailViajante("fulano@example.com");

        assertEquals(3, maiorId);
    }

    @Test
    @DisplayName("buscarMaiorIdPorEmailViajante deve retornar o valor zero quando não houver viagem cadastrada com o email do viajante passado")
    void buscarMaiorIdPorEmailViajante_RetornaZero_QuandoNaoHouverViagemCadastrada(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO_VIAGEM);
        ViagemService viagemService = new ViagemService(viagemRepository);

        int maiorId = viagemService.buscarMaiorIdPorEmailViajante("fulano@example.com");

        assertEquals(0, maiorId);
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
                150.0,
                LocalDateTime.of(2025, 6, 10, 18, 0),
                "Música"));
        atividades1.add(new Passeio("Pelourinho Tour",
                30.0,
                LocalDateTime.of(2025, 6, 11, 10, 0),
                "Centro Histórico"));
        atividades1.add(new Restaurante("Almoço na Yemanjá",
                80.0,
                LocalDateTime.of(2025, 6, 11, 13, 0),
                "Restaurante Yemanjá", "Baiana", "Moqueca"));

        viagem1.setAtividades(atividades1);
        viagem1.setSaldo(1000.0);
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
                120.0,
                LocalDateTime.of(2025, 7, 20, 20, 0),
                "Música"));
        atividades2.add(new Passeio("Praia da Joaquina",
                25.0,
                LocalDateTime.of(2025, 7, 21, 9, 0),
                "Litoral"));
        atividades2.add(new Restaurante("Cantina Italiana",
                70.0,
                LocalDateTime.of(2025, 7, 21, 13, 0),
                "Cantina Italiana", "Italiana", "Lasanha"));

        viagem2.setAtividades(atividades2);
        viagem2.setSaldo(800.0);
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
                200.0,
                LocalDateTime.of(2025, 8, 15, 9, 0),
                "Tecnologia"));
        atividades3.add(new Passeio("Jardim Botânico",
                10.0,
                LocalDateTime.of(2025, 8, 16, 15, 0),
                "Parque"));
        atividades3.add(new Restaurante("Jantar Alemão",
                90.0,
                LocalDateTime.of(2025, 8, 16, 19, 30),
                "Bar do Alemão", "Alemã", "Eisbein"));

        viagem3.setAtividades(atividades3);
        viagem3.setSaldo(1500.0);
        viagem3.setEmailViajante("fulano@example.com");
        return viagem3;
    }

}