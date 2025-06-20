package backend.main.entities;

import java.io.Serializable;

public class Deslocamento implements Serializable {
    private String meioDeTransporte;
    private double custo;

    public Deslocamento(String meioDeTransporte, double custo) {
        this.meioDeTransporte = meioDeTransporte;
        this.custo = custo;
    }


    public String getMeioDeTransporte() {
        return meioDeTransporte;
    }

    public void setMeioDeTransporte(String meioDeTransporte) {
        this.meioDeTransporte = meioDeTransporte;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    @Override
    public String toString() {
        return "Deslocamento{" +
                "meioDeTransporte='" + meioDeTransporte + '\'' +
                ", custo=" + custo +
                '}';
    }
}
