package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe que representa um Restaurante que foi visitado durante a Viagem.
 * Essa classe é uma extensão da classe abstrata {@link Atividade} herdando os seus atributos e métodos.
 * Cada restaurante possui atributos como nome, gasto, data, nome do restaurante, culinária e prato. Sendo nome, gasto e data herdados de Atividade.
 * */
public class Restaurante extends Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = 8528758322962441714L;

    private String nomeRestaurante;
    private String culinaria;
    private String prato;

    /**
     * Construtor utilizado para instanciar um Restaurante
     * @param nome  Nome da atividade que foi feita na viagem
     * @param gasto Valor gasto na atividade
     * @param data Data em que a atividade foi feita (dia, mês, ano, hora, minutos)
     * @param nomeRestaurante Nome do restaurante que foi visitado
     * @param culinaria Culinária típica do restaurante que foi visitado
     * @param prato Prato consumido no restaurante visitado
     * */
    public Restaurante(String nome, double gasto, LocalDateTime data, String nomeRestaurante, String culinaria, String prato) {
        super(nome, gasto, data);
        this.nomeRestaurante = nomeRestaurante;
        this.culinaria = culinaria;
        this.prato = prato;
    }

    /**
     * Construtor vazio para criação de um Restaurante
     * Utilizado para facilitar os testes unitários
     * */
    public Restaurante() {
    }

    /**
     * Busca e retorna o nome do restaurante visitado
     * @return Nome do restaurante visitado
     * */
    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    /**
     * Busca e retorna a culinária do restaurante visitado
     * @return Culinária do restaurante visitado
     * */
    public String getCulinaria() {
        return culinaria;
    }

    /**
     * Busca e retorna o prato consumido no restaurante visitado
     * @return Prato consumido no restaurante visitado
     * */
    public String getPrato() {
        return prato;
    }
}
