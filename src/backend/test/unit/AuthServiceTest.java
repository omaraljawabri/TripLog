package backend.test.unit;

import backend.main.entities.Viajante;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViajanteRepository;
import backend.main.services.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("cadastrar deve cadastrar um Viajante quando os dados do Viajante estiverem corretos")
    void cadastrar_CadastraViajante_QuandoDadosDoViajanteEstiveremCorretos(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        assertDoesNotThrow(() -> authService.cadastrar(viajante));
    }

    @Test
    @DisplayName("cadastrar deve lançar uma ValidacaoException quando o nome, email ou senha forem null")
    void cadastrar_LancaValidacaoException_QuandoNomeEmailOuSenhaForemNull(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante(null, "fulano123", "fulano@example.com");

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> authService.cadastrar(viajante));
        assertEquals("Email, nome e senha devem ser preenchidos", exception.getMessage());
    }

    @Test
    @DisplayName("cadastrar deve lançar uma ErroInternoException quando algum erro interno ocorrer ao salvar um Viajante")
    void cadastrar_LancaErroInternoException_QuandoAlgumErroOcorreAoSalvarViajante(){
        ViajanteRepository viajanteRepository = new ViajanteRepository("/erro/"+NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        ErroInternoException exception = assertThrows(ErroInternoException.class, () -> authService.cadastrar(viajante));
        assertEquals("Erro ao fazer cadastro", exception.getMessage());
    }

    @Test
    @DisplayName("cadastrar deve lançar uma ValidacaoException quando o email informado para cadastro do viajante já existir no sistema")
    void cadastrar_LancaValidacaoException_QuandoOEmailInformadoParaCadastroJaExistirNoSistema(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        authService.cadastrar(viajante);

        Viajante viajante2 = new Viajante("Fulano", "fulano123", "fulano@example.com");
        ValidacaoException validacaoException = assertThrows(ValidacaoException.class, () -> authService.cadastrar(viajante2));
        assertEquals("Email informado já está em uso", validacaoException.getMessage());
    }

    @Test
    @DisplayName("login deve retornar true quando o usuário tiver suas credenciais autenticadas com sucesso")
    void login_RetornaTrue_QuandoUsuarioEAutenticadoComSucesso(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        authService.cadastrar(viajante);

        viajante.setSenha("fulano123");

        assertDoesNotThrow(() -> authService.login(viajante.getSenha(), viajante.getEmail()));
    }

    @Test
    @DisplayName("login deve lançar uma ValidacaoException quando o email do viajante não existir no sistema")
    void login_LancaValidacaoException_QuandoEmailDoViajanteNaoExisteNoSistema(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> authService.login(viajante.getSenha(), viajante.getEmail()));
        assertEquals("Viajante com email ou senha incorreto(s)!", exception.getMessage());
    }

    @Test
    @DisplayName("login deve lançar uma ValidacaoException quando a senha do viajante estiver incorreta")
    void login_LancaValidacaoException_QuandoSenhaDoViajanteEstaIncorreta(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        AuthService authService = new AuthService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");

        authService.cadastrar(viajante);

        viajante.setSenha("senhaincorreta123");

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> authService.login(viajante.getSenha(), viajante.getEmail()));
        assertEquals("Viajante com email ou senha incorreto(s)!", exception.getMessage());
    }
}