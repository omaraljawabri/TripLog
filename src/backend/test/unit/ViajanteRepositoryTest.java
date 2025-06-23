package backend.test.unit;

import backend.main.entities.Viajante;
import backend.main.repositories.ViajanteRepository;
import org.junit.jupiter.api.AfterEach;
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
    void salvarViajante_RetornaTrue_QuandoViajanteESalvoComSucesso() {
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);
        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        boolean resultado = viajanteRepository.salvarViajante(viajante);
        assertTrue(resultado);
    }

    @Test
    void salvarViajante_RetornaFalse_QuandoErroOcorreAoSalvarViajante(){
        ViajanteRepository viajanteRepository = new ViajanteRepository("/erro/" + NOME_ARQUIVO);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        boolean resultado = viajanteRepository.salvarViajante(viajante);

        assertFalse(resultado);
    }

    @Test
    void buscarViajantePorEmail_RetornaViajante_QuandoExistirViajanteComOEmailPassado() {
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        Viajante viajanteRecuperado = viajanteRepository.buscarViajantePorEmail("fulano@example.com");
        assertNotNull(viajanteRecuperado);
        assertEquals("fulano@example.com", viajanteRecuperado.getEmail());
    }

    @Test
    void buscarViajantePorEmail_RetornaNull_QuandoNaoExistirViajanteComOEmailPassado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        Viajante viajanteRecuperado = viajanteRepository.buscarViajantePorEmail("fulano@example.com");

        assertNull(viajanteRecuperado);
    }

    @Test
    void editarViajantePorEmail_RetornaTrue_QuandoViajanteEAtualizadoComSucesso(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        boolean resultado = viajanteRepository.editarViajantePorEmail(viajante.getEmail(), "Ciclano", "ciclano123");

        assertTrue(resultado);
    }

    @Test
    void editarViajantePorEmail_RetornaFalse_QuandoOcorrerErroAoEditarViajante(){
        ViajanteRepository viajanteRepository = new ViajanteRepository("erro/" + NOME_ARQUIVO);


        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        boolean resultado = viajanteRepository.editarViajantePorEmail(viajante.getEmail(), "Ciclano", "ciclano123");

        assertFalse(resultado);
    }
}