package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe que representa um Evento que ocorreu em uma viagem.
 * Essa classe é uma extensão da classe abstrata {@link Atividade} herdando os seus atributos e métodos.
 * Cada evento possui os atributos nome, gasto, data e tema. Sendo nome, gasto e data herdados de Atividade.
 * */
public class Evento extends Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = 1822957277203428237L;

    private String tema;

    /**
     * Construtor vazio que instancia um Evento
     * Utilizado para facilitar os testes unitários
     * */
    public Evento() {

    }

    /**
     * Construtor utilizado para instanciar um Evento
     * @param nome  Nome da atividade que foi feita na viagem
     * @param gasto Valor gasto na atividade
     * @param data Data em que a atividade foi feita (dia, mês, ano, hora, minutos)
     * @param tema Tema do evento que ocorreu na Viagem
     * */
    public Evento(String nome, double gasto, LocalDateTime data, String tema) {
        super(nome, gasto, data);
        this.tema = tema;
    }
}
