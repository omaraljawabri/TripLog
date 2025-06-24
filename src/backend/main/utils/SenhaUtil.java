package backend.main.utils;

import backend.main.exceptions.ErroInternoException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Classe responsável por armazenar utilitários relacionados a validação e criptografia do atributo senha
 * */
public class SenhaUtil {

    /**
     * Método responsável por receber uma senha sem estar criptografada e retornar uma senha criptografada
     * para ser salva no arquivo. O algoritmo SHA-256 foi utilizado para isso.
     *
     * @param senha Senha a ser criptografada
     * @return Senha criptografada
     * ou
     * @exception ErroInternoException erro interno na geração da senha, como falha nos bytes
     * */
    //O mais correto seria utilizar BCrypt, contudo, para evitar dependências externas demais optamos pelo SHA-256
    public static String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new ErroInternoException("Erro ao gerar hash da senha");
        }
    }

    /**
     * Método responsável por receber uma senha sem estar criptografada e outra criptografada e compará-las para ver se
     * possuem o mesmo valor e são a mesma.
     *
     * @param senhaDigitada Senha sem estar criptografada
     * @param hashSalvo Senha criptografada
     *
     * @return Valor booleano informando se a senha é a mesma (true) ou não (false)
     * */
    public static boolean verificarSenha(String senhaDigitada, String hashSalvo) {
        String hashDigitado = hashSenha(senhaDigitada);
        return hashDigitado.equals(hashSalvo);
    }
}
