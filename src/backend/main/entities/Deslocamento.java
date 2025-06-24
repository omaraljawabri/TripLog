package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;

public class Deslocamento implements Serializable {
    @Serial
    private static final long serialVersionUID = -7505069781177437456L;

    private String meioDeTransporte;
    private double custo;

    public Deslocamento(String meioDeTransporte, double custo) {
        this.meioDeTransporte = meioDeTransporte;
        this.custo = custo;
    }

    public String getMeioDeTransporte() {
        return meioDeTransporte;
    }

    public double getCusto() {
        return custo;
    }
}
