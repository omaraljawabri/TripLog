package backend.main.utils;

import java.util.regex.Pattern;

/**
 * Classe responsável por armazenar utilitários de validação relacionados ao atributo email.
 * */
public class EmailUtil {

    /**
     * Método responsável por validar um email passado como parâmetro a partir de um regex pré-estabelecido de um formato
     * aceitável de email.
     *
     * @param email Email a ser validado
     * @return Valor booleano informando se o email é válido (true) ou não (false)
     * */
    public static boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return Pattern.matches(regex, email);
    }
}
