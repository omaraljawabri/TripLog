package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe que representa um deslocamento que ocorreu durante a viagem, cada Viagem pode possuir vários deslocamentos.
 * Cada deslocamento possui o meio de transporte que foi utilizado e o custo.
 * */
public class Deslocamento implements Serializable {
    @Serial
    private static final long serialVersionUID = -7505069781177437456L;

    private String meioDeTransporte;
    private double custo;

    /**
     * Construtor utilizado para instanciar um Deslocamento
     * @param meioDeTransporte Meio de transporte que o deslocamento ocorreu
     * @param custo Custo do deslocamento
     * */
    public Deslocamento(String meioDeTransporte, double custo) {
        this.meioDeTransporte = meioDeTransporte;
        this.custo = custo;
    }

    /**
     * Busca e retorna o meio de transporte de um deslocamento
     * @return Meio de transporte do deslocamento
     * */
    public String getMeioDeTransporte() {
        return meioDeTransporte;
    }

    /**
     * Busca e retorna o custo de um deslocamento
     * @return Custo do deslocamento
     * */
    public double getCusto() {
        return custo;
    }
}
