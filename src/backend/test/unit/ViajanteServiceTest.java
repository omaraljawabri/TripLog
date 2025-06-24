package backend.test.unit;

import backend.main.entities.Viajante;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViajanteRepository;
import backend.main.services.ViajanteService;
import backend.main.utils.SenhaUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ViajanteServiceTest {

    private static final String NOME_ARQUIVO_VIAJANTE = "viajante_test.ser";

    @AfterEach
    void teardown() throws IOException {
        Files.deleteIfExists(Paths.get(NOME_ARQUIVO_VIAJANTE));
    }

    @Test
    @DisplayName("editarViajante deve editar um viajante pelo email e não lançar nenhuma exception quando a edição for bem sucedida")
    void editarViajante_EditaViajantePeloEmailENaoLancaException_QuandoBemSucedido() {
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", SenhaUtil.hashSenha("fulano123"), "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        assertDoesNotThrow(() -> viajanteService.editarViajante("fulano@example.com", "Ciclano", "fulano123", "ciclano123"));
    }

    @Test
    @DisplayName("editarViajante deve lançar uma EntidadeNaoEncontradaException quando não existir um viajante com o email passado como parâmetro")
    void editarViajante_LancaEntidadeNaoEncontradaException_QuandoNaoExistirViajanteComOEmailInformado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> viajanteService.editarViajante("fulano@example.com", "Ciclano", "fulano123", "ciclano123"));
        assertEquals("Não há viajante salvo com o email: fulano@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("editarViajante deve lançar uma ValidacaoException quando a senha antiga passada como parâmetro não for a mesma da senha salva no arquivo")
    void editarViajante_LancaValidacaoException_QuandoSenhaAntigaInformadaNaoForAMesmaQueASenhaSalva(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", SenhaUtil.hashSenha("fulano123"), "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> viajanteService.editarViajante("fulano@example.com", "Ciclano", "ciclano99", "ciclano123"));
        assertEquals("Senha atual digitada não é igual a senha salva", exception.getMessage());
    }

    @Test
    @DisplayName("buscarMaiorId deve retornar o maior id de viajante quando houver viajantes cadastrados")
    void buscarMaiorId_RetornaMaiorIdDeViajante_QuandoHouverViajanteCadastrado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        Viajante.resetarContador();

        Viajante viajante = new Viajante("Fulano", "fulano123", "fulano@example.com");
        Viajante viajante2 = new Viajante("Fulano", "fulano123", "fulano@example.com");
        Viajante viajante3 = new Viajante("Fulano", "fulano123", "fulano@example.com");
        viajanteRepository.salvarViajante(viajante);
        viajanteRepository.salvarViajante(viajante2);
        viajanteRepository.salvarViajante(viajante3);

        int maiorId = viajanteService.buscarMaiorId();
        assertEquals(3, maiorId);
    }

    @Test
    @DisplayName("buscarMaiorId deve retornar o valor zero quando não houver viajantes cadastrados")
    void buscarMaiorId_RetornaZero_QuandoNaoHouverViajanteCadastrado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        int maiorId = viajanteService.buscarMaiorId();

        assertEquals(0, maiorId);
    }
}