package backend.main.exceptions;

/**
 * Exceção lançada quando uma busca por algum atributo de uma classe não retorna resultados.
 * Exemplo: Busca por id de uma viagem, quando a busca nas viagens salvas em arquivos não retornar nenhuma viagem com o
 * id passado, essa exceção será lançada.
 * Essa classe herda de RuntimeException por ser uma exceção customizada.
 * */
public class EntidadeNaoEncontradaException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de exceção criada pelo sistema e lança ela quando a exceção for lançada
     * @param message Mensagem de erro que deve ser informada.
     * */
    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }
}
