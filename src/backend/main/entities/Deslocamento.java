package backend.main.entities;

import java.io.Serializable;

public class Deslocamento implements Serializable {
    private String meioDeTransporte;
    private double custo;

    public Deslocamento(String meioDeTransporte, double custo) {
        this.meioDeTransporte = meioDeTransporte;
        this.custo = custo;
    }

    public double getCusto() {
        return custo;
    }
}
