package backend.main.exceptions;

/**
 * Exceção lançada quando algum erro interno ocorrer na aplicação.
 * Exemplo: falha na leitura de dados de um arquivo.
 * Essa classe herda de RuntimeException por ser uma exceção customizada.
 * */
public class ErroInternoException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de exceção criada pelo sistema e lança ela quando a exceção for lançada
     * @param message Mensagem de erro que deve ser informada.
     * */
    public ErroInternoException(String message) {
        super(message);
    }
}
