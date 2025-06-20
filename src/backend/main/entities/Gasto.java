package backend.main.entities;

import java.io.Serializable;

public class Gasto implements Serializable {
    private double valor;
    private String tipo;

    public Gasto(double valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Gasto{" +
                "valor=" + valor +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
