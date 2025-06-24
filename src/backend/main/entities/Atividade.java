package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe que representa uma Atividade presente em uma Viagem, cada Viagem pode possuir várias atividades.
 * Atividade é uma classe abstrata que serve como base para Evento, Restaurante e Passeio.
 * Cada Atividade possui os atributos nome, gasto e data.
 * */
public abstract class Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = -2063544862544769327L;

    private String nome;
    private double gasto;
    private LocalDateTime data;

    /**
     * Construtor vazio para criação de uma Atividade.
     * Utilizado para facilitação nos testes unitários.
     * */
    public Atividade() {
    }

    /**
     * Construtor utilizado para instanciar uma Atividade
     * @param nome  Nome da atividade que foi feita na viagem
     * @param gasto Valor gasto na atividade
     * @param data Data em que a atividade foi feita (dia, mês, ano, hora, minutos)
     * */
    public Atividade(String nome, double gasto, LocalDateTime data) {
        this.nome = nome;
        this.gasto = gasto;
        this.data = data;
    }

    /**
     * Busca e retorna o nome da atividade
     * @return Nome da atividade
     * */
    public String getNome() {
        return nome;
    }

    /**
     * Busca e retorna o valor gasto na atividade
     * @return Valor gasto na atividade
     * */
    public double getGasto() {
        return gasto;
    }

    /**
     * Busca e retorna a data que a atividade foi realizada
     * @return Data da atividade
     * */
    public LocalDateTime getData() {
        return data;
    }
}
