package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe que representa um Passeio que ocorreu durante a Viagem.
 * Essa classe é uma extensão da classe abstrata {@link Atividade} herdando os seus atributos e métodos.
 * Cada passeio possui atributos como nome, gasto, data e nome do local do passeio. Sendo nome, gasto e data herdados de Atividade.
 * */
public class Passeio extends Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = 3326632644976495366L;

    private String nomeLocal;

    /**
     * Construtor vazio para criação de um Passeio.
     * Utilizado para facilitação nos testes unitários.
     * * */
    public Passeio() {
    }

    /**
     * Construtor utilizado para instanciar um Passeio
     * @param nome  Nome da atividade que foi feita na viagem
     * @param gasto Valor gasto na atividade
     * @param data Data em que a atividade foi feita (dia, mês, ano, hora, minutos)
     * @param nomeLocal Nome do local em que ocorreu o passeio
     * */
    public Passeio(String nome, double gasto, LocalDateTime data, String nomeLocal) {
        super(nome, gasto, data);
        this.nomeLocal = nomeLocal;
    }
}
