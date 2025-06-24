package backend.test.unit;

import backend.main.entities.Viajante;
import backend.main.repositories.ViajanteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ViajanteRepositoryTest {

    private static final String NOME_ARQUIVO = "test_viajante_repository.ser";

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(NOME_ARQUIVO));
    }

    @Test
    @DisplayName("salvarViajante deve retornar true quando um viajante for salvo no arquivo com sucesso")
    void salvarViajante_RetornaTrue_QuandoViajanteESalvoComSucesso() {
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        boolean resultado = viajanteRepository.salvarViajante(viajante);
        assertTrue(resultado);
    }

    @Test
    @DisplayName("salvarViajante deve retornar false quando algum erro interno ocorrer ao tentar salvar um viajante")
    void salvarViajante_RetornaFalse_QuandoErroOcorreAoSalvarViajante(){
        ViajanteRepository viajanteRepository = new ViajanteRepository("/erro/" + NOME_ARQUIVO);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        boolean resultado = viajanteRepository.salvarViajante(viajante);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("buscarViajantePorEmail deve retornar um Viajante quando existir viajante com o email passado como parâmetro")
    void buscarViajantePorEmail_RetornaViajante_QuandoExistirViajanteComOEmailPassado() {
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        Viajante viajanteRecuperado = viajanteRepository.buscarViajantePorEmail("fulano@example.com");
        assertNotNull(viajanteRecuperado);
        assertEquals("fulano@example.com", viajanteRecuperado.getEmail());
    }

    @Test
    @DisplayName("buscarViajantePorEmail deve retornar null quando não existir viajante com o email passado como parâmetro")
    void buscarViajantePorEmail_RetornaNull_QuandoNaoExistirViajanteComOEmailPassado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        Viajante viajanteRecuperado = viajanteRepository.buscarViajantePorEmail("fulano@example.com");

        assertNull(viajanteRecuperado);
    }

    @Test
    @DisplayName("editarViajantePorEmail deve retornar true quando um viajante for atualizado com sucesso")
    void editarViajantePorEmail_RetornaTrue_QuandoViajanteEAtualizadoComSucesso(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        boolean resultado = viajanteRepository.editarViajantePorEmail(viajante.getEmail(), "Ciclano", "ciclano123");

        assertTrue(resultado);
    }

    @Test
    @DisplayName("editarViajantePorEmail deve retornar false quando algum erro interno ocorrer ao atualizar um viajante")
    void editarViajantePorEmail_RetornaFalse_QuandoOcorrerErroAoEditarViajante(){
        ViajanteRepository viajanteRepository = new ViajanteRepository("erro/" + NOME_ARQUIVO);


        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        boolean resultado = viajanteRepository.editarViajantePorEmail(viajante.getEmail(), "Ciclano", "ciclano123");

        assertFalse(resultado);
    }

    @Test
    @DisplayName("buscarMaiorId deve retornar o maior id de viajante cadastrado quando houver viajantes cadastrados")
    void buscarMaiorId_RetornaMaiorIdDeViajante_QuandoHouverViajanteCadastrado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        Viajante.resetarContador();

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");
        Viajante viajante2 = new Viajante("Fulano", "fulano123", "fulano@example.com");
        Viajante viajante3 = new Viajante("Fulano", "fulano123", "fulano@example.com");
        viajanteRepository.salvarViajante(viajante);
        viajanteRepository.salvarViajante(viajante2);
        viajanteRepository.salvarViajante(viajante3);

        int maiorId = viajanteRepository.buscarMaiorId();
        assertEquals(3, maiorId);
    }

    @Test
    @DisplayName("buscarMaiorId deve retornar o valor zero quando não houver viajantes cadastrados")
    void buscarMaiorId_RetornaZero_QuandoNaoHouverViajanteCadastrado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        int maiorId = viajanteRepository.buscarMaiorId();

        assertEquals(0, maiorId);
    }
}