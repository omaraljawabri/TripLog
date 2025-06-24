package backend.test.unit;

import backend.main.utils.EmailUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EmailUtilTest {

    @Test
    @DisplayName("validarEmail deve retornar true quando email passado como parâmetro for válido")
    void validarEmail_RetornaTrue_QuandoEmailPassadoComoParametroEValido(){
        boolean resultado = EmailUtil.validarEmail("fulano@example.com");
        assertTrue(resultado);
    }

    @Test
    @DisplayName("validarEmail deve retornar false quando email passado como parâmetro não for válido")
    void validarEmail_RetornaFalse_QuandoEmailPassadoComoParametroNaoForValido(){
        boolean resultado = EmailUtil.validarEmail("fulano");
        assertFalse(resultado);
    }
}
