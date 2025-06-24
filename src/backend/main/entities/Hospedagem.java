package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;

public class Hospedagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 5990586390555358684L;

    private String nomeLocalHospedagem;
    private int numeroDeNoites;
    private double valorDiaria;

    public Hospedagem(){
    }

    public Hospedagem(String nomeLocalHospedagem, int numeroDeNoites, double valorDiaria) {
        this.nomeLocalHospedagem = nomeLocalHospedagem;
        this.numeroDeNoites = numeroDeNoites;
        this.valorDiaria = valorDiaria;
    }

    public double calculaTotal(){
        return this.numeroDeNoites*this.valorDiaria;
    }

    public String getNomeLocalHospedagem() {
        return nomeLocalHospedagem;
    }

    public int getNumeroDeNoites() {
        return numeroDeNoites;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }
}
