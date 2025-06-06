package entities;

public class Hospedagem {
    private static int contador;
    private int id;
    private String nomeLocalHospedagem;
    private int numeroDeNoites;
    private double valorDiaria;

    public Hospedagem(){
        Hospedagem.contador++;
        this.id = Hospedagem.contador;
    }

    public Hospedagem(String nomeLocalHospedagem, int numeroDeNoites, double valorDiaria) {
        Hospedagem.contador++;
        this.id = Hospedagem.contador;
        this.nomeLocalHospedagem = nomeLocalHospedagem;
        this.numeroDeNoites = numeroDeNoites;
        this.valorDiaria = valorDiaria;
    }

    public String getNomeLocalHospedagem() {
        return nomeLocalHospedagem;
    }

    public void setNomeLocalHospedagem(String nomeLocalHospedagem) {
        this.nomeLocalHospedagem = nomeLocalHospedagem;
    }

    public int getNumeroDeNoites() {
        return numeroDeNoites;
    }

    public void setNumeroDeNoites(int numeroDeNoites) {
        this.numeroDeNoites = numeroDeNoites;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }
}
