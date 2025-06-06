package entities;

public class Deslocamento {
    private static int id;
    private String meioDeTransporte;
    private double custo;

    public Deslocamento(){
        Deslocamento.id++;
    }

    public Deslocamento(String meioDeTransporte, double custo) {
        Deslocamento.id++;
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
}
