package backend.test.unit;

import backend.main.entities.Viajante;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViajanteRepository;
import backend.main.services.ViajanteService;
import backend.main.utils.SenhaUtil;
import org.junit.jupiter.api.AfterEach;
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
    void editarViajante_EditaViajantePeloEmailENaoLancaException_QuandoBemSucedido() {
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", SenhaUtil.hashSenha("fulano123"), "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        assertDoesNotThrow(() -> viajanteService.editarViajante("fulano@example.com", "Ciclano", "fulano123", "ciclano123"));
    }

    @Test
    void editarViajante_LancaEntidadeNaoEncontradaException_QuandoNaoExistirViajanteComOEmailInformado(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        EntidadeNaoEncontradaException exception = assertThrows(EntidadeNaoEncontradaException.class, () -> viajanteService.editarViajante("fulano@example.com", "Ciclano", "fulano123", "ciclano123"));
        assertEquals("Não há viajante salvo com o email: fulano@example.com", exception.getMessage());
    }

    @Test
    void editarViajante_LancaValidaoException_QuandoSenhaAntigaInformadaNaoForAMesmaQueASenhaSalva(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(viajanteRepository);

        Viajante viajante = new Viajante("Fulano", SenhaUtil.hashSenha("fulano123"), "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> viajanteService.editarViajante("fulano@example.com", "Ciclano", "ciclano99", "ciclano123"));
        assertEquals("Senha atual digitada não é igual a senha salva", exception.getMessage());
    }

    //Esse erro só conseguiria ser simulado utilizando mocks via Mockito ou algo do tipo, a situação para que ele ocorra não consegue ser feita apenas com JUnit 5
    /*@Test
    void editarViajante_LancaErroInternoException_QuandoAlgumErroOcorrerAoEditarViajante(){
        ViajanteRepository viajanteRepository = new ViajanteRepository(NOME_ARQUIVO_VIAJANTE);
        ViajanteService viajanteService = new ViajanteService(new ViajanteRepository("erro/"+NOME_ARQUIVO_VIAJANTE));

        Viajante viajante = new Viajante("Fulano", SenhaUtil.hashSenha("fulano123"), "fulano@example.com");

        viajanteRepository.salvarViajante(viajante);

        ErroInternoException exception = assertThrows(ErroInternoException.class, () -> viajanteService.editarViajante("fulano@example.com", "Ciclano", "fulano123", "ciclano123"));
        assertEquals("Erro ao editar viajante", exception.getMessage());
    }*/
}