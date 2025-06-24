package backend.test.unit;

import backend.main.entities.*;
import backend.main.repositories.ViagemRepository;
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

class ViagemRepositoryTest {

    private static final String NOME_ARQUIVO = "test_viagem_repository.ser";

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(NOME_ARQUIVO));
    }

    @Test
    @DisplayName("salvarViagem deve retornar true quando a viagem for salva com sucesso")
    void salvarViagem_RetornaTrue_QuandoViagemESalvaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagem(criarViagem1());
        assertTrue(resultado);
    }

    @Test
    @DisplayName("salvarViagem deve retornar false quando algum erro interno ocorrer ao salvar viagem")
    void salvarViagem_RetornaFalse_QuandoAlgumErroOcorrerAoSalvarViagem(){
        ViagemRepository viagemRepository = new ViagemRepository("/erro/"+NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagem(criarViagem1());
        assertFalse(resultado);
    }

    @Test
    @DisplayName("salvarViagens deve retornar true quando a lista de viagem enviada for salva com sucesso")
    void salvarViagens_RetornaTrue_QuandoListaDeViagemESalvaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagens(List.of(criarViagem1(), criarViagem2(), criarViagem3()));
        assertTrue(resultado);
    }

    @Test
    @DisplayName("salvarViagens deve retornar false quando algum erro interno ocorrer ao salvar a lista de viagens")
    void salvarViagens_RetornaFalse_QuandoAlgumErroOcorrerAoSalvarViagens(){
        ViagemRepository viagemRepository = new ViagemRepository("/erro/" + NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagens(List.of(criarViagem2(), criarViagem1()));
        assertFalse(resultado);
    }

    @Test
    @DisplayName("buscarTodasViagens deve retornar uma lista de Viagem quando houverem viagens cadastradas no sistema")
    void buscarTodasViagens_RetornaListaDeViagem_QuandoHouveremViagensCadastradas() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagens(List.of(criarViagem1(), criarViagem2()));

        List<Viagem> viagens = viagemRepository.buscarTodasViagens();

        assertEquals(2, viagens.size());
        assertEquals(criarViagem1().getLugarDeChegada(), viagens.getFirst().getLugarDeChegada());
    }

    @Test
    @DisplayName("buscarTodasViagens deve retornar uma lista vazia quando não houverem viagens cadastradas no sistema")
    void buscarTodasViagens_RetornaListaVazia_QuandoNaoHouveremViagensCadastradas(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        List<Viagem> viagens = viagemRepository.buscarTodasViagens();

        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("removerViagemPorId deve retornar true quando uma viagem for removida pelo seu id com sucesso")
    void removerViagemPorId_RetornaTrue_QuandoViagemERemovidaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);
        Viagem viagem = criarViagem1();

        viagemRepository.salvarViagem(viagem);

        boolean resultado = viagemRepository.removerViagemPorId(viagem.getId(), viagem.getEmailViajante());

        assertTrue(resultado);
    }

    @Test
    @DisplayName("removerViagemPorId deve retornar false quando o id da viagem passado não existir")
    void removerViagemPorId_RetornaFalse_QuandoViagemComIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.removerViagemPorId(1, "fulano@example.com");

        assertFalse(resultado);
    }

    @Test
    @DisplayName("editarViagemPorId deve retornar true quando a viagem passada for editada com sucesso")
    void editarViagemPorId_RetornaTrue_QuandoViagemEEditadaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);
        Viagem viagem = criarViagem1();

        viagemRepository.salvarViagem(viagem);

        boolean resultado = viagemRepository.editarViagemPorId(viagem.getId(), viagem.getEmailViajante(), criarViagem2());

        assertTrue(resultado);
    }

    @Test
    @DisplayName("editarViagemPorId deve retornar false quando o id da viagem a ser editada passado não existir")
    void editarViagemPorId_RetornaFalse_QuandoViagemComIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.editarViagemPorId(1, "fulano@example.com", criarViagem1());

        assertFalse(resultado);
    }

    @Test
    @DisplayName("buscarViagensPorEmailViajante deve retornar uma lista de Viagem quando existirem viagens com o email do viajante passado")
    void buscarViagensPorEmailViajante_RetornaListaDeViagem_QuandoExistirViagemComEmailViajantePassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(criarViagem3());
        List<Viagem> viagens = viagemRepository.buscarViagensPorEmailViajante("fulano@example.com");

        assertEquals(1, viagens.size());
    }

    @Test
    @DisplayName("buscarViagensPorEmailViajante deve retornar uma lista vazia quando não existirem viagens com o email do viajante passado")
    void buscarViagensPorEmailViajante_RetornaListaVazia_QuandoNaoExistirViagemComEmailViajantePassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        List<Viagem> viagens = viagemRepository.buscarViagensPorEmailViajante("fulano@example.com");

        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de Viagem com dados quando houverem viagens com o destino aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouveremViagensComDestinoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", "Salvador", null, null);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("Salvador", viagens.getFirst().getLugarDeChegada());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem com dados quando houverem viagens com a companhia aplicada")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouveremViagensComCompanhiaAplicada(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", null, "João", null);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("João", viagens.getFirst().getCompanhia());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de viagem com dados quando houverem viagens com gasto superior ao aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouverViagensComGastoSuperiorAoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        double gasto = 500;

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", null, null, gasto);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertTrue(viagens.getFirst().calcularTotalGastos() > gasto);
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de Viagem vazia quando não houverem viagens com o destino aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemVazia_QuandoNaoHouveremViagensComDestinoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", "Goiânia", null, null);

        assertNotNull(viagens);
        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de Viagem vazia quando não houverem viagens com a companhia aplicada")
    void buscarViagensFiltradas_RetornaListaDeViagemVazia_QuandoNaoHouveremViagensComCompanhiaAplicada(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", null, "Felipe", null);

        assertNotNull(viagens);
        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de Viagem vazia quando não houverem viagens com o gasto superior ao aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemVazia_QuandoNaoHouverViagensComGastoSuperiorAoAplicado(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        double gasto = 50000;

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", null, null, gasto);

        assertNotNull(viagens);
        assertTrue(viagens.isEmpty());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de Viagem com dados quando houver viagens com o destino e a companhia aplicados")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouverViagensComDestinoECompanhiaAplicados(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", "Salvador", "João", null);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("João", viagens.getFirst().getCompanhia());
        assertEquals("Salvador", viagens.getFirst().getLugarDeChegada());
    }

    @Test
    @DisplayName("buscarViagensFiltradas deve retornar uma lista de Viagem com dados quando houver viagens com destino, companhia aplicados e gasto superior ao aplicado")
    void buscarViagensFiltradas_RetornaListaDeViagemPopulada_QuandoHouverViagensComDestinoCompanhiaEGastoAplicados(){
        Viagem viagem = criarViagem1();
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(viagem);

        double gasto = 500;

        List<Viagem> viagens
                = viagemRepository.buscarViagensFiltradas("fulano@example.com", "Salvador", "João", gasto);

        assertNotNull(viagens);
        assertFalse(viagens.isEmpty());
        assertEquals("João", viagens.getFirst().getCompanhia());
        assertEquals("Salvador", viagens.getFirst().getLugarDeChegada());
        assertTrue(viagens.getFirst().calcularTotalGastos() > gasto);
    }

    @Test
    @DisplayName("buscarMaiorId deve retornar o maior id cadastrado da classe Viagem quando houver viagens cadastradas")
    void buscarMaiorId_RetornaMaiorIdDaViagem_QuandoHouverViagemCadastrada(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        Viagem.resetarContador();

        viagemRepository.salvarViagem(criarViagem1());
        viagemRepository.salvarViagem(criarViagem2());
        Viagem viagem = criarViagem3();
        viagemRepository.salvarViagem(viagem);

        int maiorId = viagemRepository.buscarMaiorId();

        assertEquals(viagem.getId(), maiorId);
    }

    @Test
    @DisplayName("buscarMaiorId deve retornar o valor zero quando não houver viagens cadastradas")
    void buscarMaiorId_RetornaZero_QuandoNaoHouverViagemCadastrada(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        int maiorId = viagemRepository.buscarMaiorId();

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