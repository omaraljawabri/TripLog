package entities;

public class Gasto {
    private static int id;
    private double valor;
    private String tipo;

    public Gasto(){
        Gasto.id++;
    }

    public Gasto(double valor, String tipo) {
        Gasto.id++;
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
}
