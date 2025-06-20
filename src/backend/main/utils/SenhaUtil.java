package backend.main.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SenhaUtil {

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
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

    public static boolean verificarSenha(String senhaDigitada, String hashSalvo) {
        String hashDigitado = hashSenha(senhaDigitada);
        return hashDigitado.equals(hashSalvo);
    }
}
