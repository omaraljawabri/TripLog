package backend.test.unit;

import backend.main.entities.Viajante;
import backend.main.exceptions.ValidationException;
import backend.main.repositories.ViajanteRepository;
import backend.main.services.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private static final String NOME_ARQUIVO_VIAJANTE = "viajante_test.ser";

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(NOME_ARQUIVO_VIAJANTE));
    }

    @Test
    void cadastrar_CadastraViajante_QuandoDadosDoViajanteEstiveremCorretos(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        assertDoesNotThrow(() -> authService.cadastrar(viajante));
    }

    @Test
    void cadastrar_LancaValidationException_QuandoNomeEmailOuSenhaForemNull(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante(null, "fulano123", "fulano@example.com");

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.cadastrar(viajante));
        assertEquals("Email, nome e senha devem ser preenchidos", exception.getMessage());
    }

    @Test
    void cadastrar_RetornaFalse_QuandoAlgumErroOcorreAoSalvarViajante(){
        ViajanteRepository viajanteRepository = new ViajanteRepository("/erro/"+NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.cadastrar(viajante));
        assertEquals("Erro ao fazer cadastro", exception.getMessage());
    }

    @Test
    void login_RetornaTrue_QuandoUsuarioEAutenticadoComSucesso(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        authService.cadastrar(viajante);

        viajante.setSenha("fulano123");

        assertDoesNotThrow(() -> authService.login(viajante.getSenha(), viajante.getEmail()));
    }

    @Test
    void login_LancaValidationException_QuandoEmailDoViajanteNaoExisteNoSistema(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.login(viajante.getSenha(), viajante.getEmail()));
        assertEquals("Viajante com email ou senha incorreto(s)!", exception.getMessage());
    }

    @Test
    void login_LancaValidationException_QuandoSenhaDoViajanteEstaIncorreta(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        authService.cadastrar(viajante);

        viajante.setSenha("senhaincorreta123");

        ValidationException exception = assertThrows(ValidationException.class, () -> authService.login(viajante.getSenha(), viajante.getEmail()));
        assertEquals("Viajante com email ou senha incorreto(s)!", exception.getMessage());
    }
}