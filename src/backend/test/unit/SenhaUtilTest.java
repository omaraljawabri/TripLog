package backend.test.unit;

import backend.main.utils.SenhaUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SenhaUtilTest {

    @Test
    @DisplayName("hashSenha deve retornar uma senha criptografada com algoritmo SHA-256 quando bem sucedido")
    void hashSenha_RetornaSenhaCriptografadaComAlgoritmoSHA256_QuandoBemSucedido() {

        String hashGerado = SenhaUtil.hashSenha("senha123");

        assertNotEquals("senha123", hashGerado);
        assertTrue(hashGerado.length() > 50);
    }

    @Test
    @DisplayName("verificarSenha deve retornar true quando as senhas enviadas são iguais")
    void verificarSenha_RetornaTrue_QuandoSenhasSaoIguais() {
        String senha123 = SenhaUtil.hashSenha("senha123");

        boolean resultado = SenhaUtil.verificarSenha("senha123", senha123);
        assertTrue(resultado);
    }

    @Test
    @DisplayName("verificarSenha deve retornar false quando as senhas enviadas são dfierentes")
    void verificarSenha_RetornaFalse_QuandoSenhasEnviadasSaoDiferentes(){
        String teste123 = SenhaUtil.hashSenha("teste123");

        boolean resultado = SenhaUtil.verificarSenha("senha123", teste123);
        assertFalse(resultado);
    }
}