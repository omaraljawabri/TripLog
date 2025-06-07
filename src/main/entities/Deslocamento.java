package main.entities;

import java.io.Serializable;

public class Deslocamento implements Serializable {
    private int id;
    private static int contador = 0;
    private String meioDeTransporte;
    private double custo;

    public Deslocamento(){
        Deslocamento.contador++;
        this.id = Deslocamento.contador;
    }

    public Deslocamento(String meioDeTransporte, double custo) {
        Deslocamento.contador++;
        this.id = Deslocamento.contador;
        this.meioDeTransporte = meioDeTransporte;
        this.custo = custo;
    }

    public int getId() {
        return id;
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
                "id=" + id +
                ", meioDeTransporte='" + meioDeTransporte + '\'' +
                ", custo=" + custo +
                '}';
    }
}
