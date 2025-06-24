package backend.main.exceptions;

/**
 * Exceção lançada quando um erro de validação ocorre.
 * Exemplo: usuário não digitou todos os campos necessários para salvar uma viagem ou a senha para login está incorreta.
 * Essa classe herda de RuntimeException por ser uma exceção customizada.
 * */
public class ValidacaoException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de exceção criada pelo sistema e lança ela quando a exceção for lançada
     * @param message Mensagem de erro que deve ser informada.
     * */
    public ValidacaoException(String message) {
        super(message);
    }
}
