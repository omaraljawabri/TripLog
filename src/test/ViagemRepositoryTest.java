package test;

import main.entities.*;
import main.repositories.ViagemRepository;
import org.junit.jupiter.api.AfterEach;
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
    void salvarViagem_RetornaTrue_QuandoViagemESalvaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagem(criarViagem1());
        assertTrue(resultado);
    }

    @Test
    void salvarViagem_RetornaFalse_QuandoAlgumErroOcorrerAoSalvarViagem(){
        ViagemRepository viagemRepository = new ViagemRepository("/erro/"+NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagem(criarViagem1());
        assertFalse(resultado);
    }

    @Test
    void salvarViagens_RetornaTrue_QuandoListaDeViagemESalvaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagens(List.of(criarViagem1(), criarViagem2(), criarViagem3()));
        assertTrue(resultado);
    }

    @Test
    void salvarViagens_RetornaFalse_QuandoAlgumErroOcorrerAoSalvarViagens(){
        ViagemRepository viagemRepository = new ViagemRepository("/erro/" + NOME_ARQUIVO);

        boolean resultado = viagemRepository.salvarViagens(List.of(criarViagem2(), criarViagem1()));
        assertFalse(resultado);
    }

    @Test
    void buscarTodasViagens_RetornaListaDeViagem_QuandoHouveremViagensCadastradas() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagens(List.of(criarViagem1(), criarViagem2()));

        List<Viagem> viagens = viagemRepository.buscarTodasViagens();

        assertEquals(2, viagens.size());
        assertEquals(criarViagem1().getLugarDeChegada(), viagens.getFirst().getLugarDeChegada());
    }

    @Test
    void buscarTodasViagens_RetornaListaVazia_QuandoNaoHouveremViagensCadastradas(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        List<Viagem> viagens = viagemRepository.buscarTodasViagens();

        assertTrue(viagens.isEmpty());
    }

    @Test
    void buscarViagemPorId_RetornaViagemComIdPassado_QuandoIdPassadoExistir() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);
        Viagem viagem1 = criarViagem1();

        viagemRepository.salvarViagem(viagem1);

        Viagem viagem = viagemRepository.buscarViagemPorId(viagem1.getId(), viagem1.getIdViajante());

        assertNotNull(viagem);
        assertEquals(viagem.getId(), viagem1.getId());
    }

    @Test
    void buscarViagemPorId_RetornaNull_QuandoIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        Viagem viagem = viagemRepository.buscarViagemPorId(1, 1);

        assertNull(viagem);
    }

    @Test
    void removerViagemPorId_RetornaTrue_QuandoViagemERemovidaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);
        Viagem viagem = criarViagem1();

        viagemRepository.salvarViagem(viagem);

        boolean resultado = viagemRepository.removerViagemPorId(viagem.getId(), viagem.getIdViajante());

        assertTrue(resultado);
    }

    @Test
    void removerViagemPorId_RetornaFalse_QuandoViagemComIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.removerViagemPorId(1, 1);

        assertFalse(resultado);
    }

    @Test
    void editarViagemPorId_RetornaTrue_QuandoViagemEEditadaComSucesso() {
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);
        Viagem viagem = criarViagem1();

        viagemRepository.salvarViagem(viagem);

        boolean resultado = viagemRepository.editarViagemPorId(viagem.getId(), viagem.getIdViajante(), criarViagem2());

        assertTrue(resultado);
    }

    @Test
    void editarViagemPorId_RetornaFalse_QuandoViagemComIdPassadoNaoExistir(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        boolean resultado = viagemRepository.editarViagemPorId(1, 1, criarViagem1());

        assertFalse(resultado);
    }

    @Test
    void buscarViagensPorIdViajante_RetornaListaDeViagem_QUandoExistirViagemComIdViajantePassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        viagemRepository.salvarViagem(criarViagem3());
        List<Viagem> viagens = viagemRepository.buscarViagensPorIdViajante(1);

        assertEquals(1, viagens.size());
    }

    @Test
    void buscarViagensPorIdViajante_RetornaListaVazia_QuandoNaoExistirViagemComIdViajantePassado(){
        ViagemRepository viagemRepository = new ViagemRepository(NOME_ARQUIVO);

        List<Viagem> viagens = viagemRepository.buscarViagensPorIdViajante(5);

        assertTrue(viagens.isEmpty());
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