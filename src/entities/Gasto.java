package entities;

public class Gasto {
    private int id;
    private static int contador;
    private double valor;
    private String tipo;

    public Gasto(){
        Gasto.contador++;
        this.id = Gasto.contador;
    }

    public Gasto(double valor, String tipo) {
        Gasto.contador++;
        this.id = Gasto.contador;
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
