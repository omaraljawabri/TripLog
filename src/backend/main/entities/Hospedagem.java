package backend.main.entities;

import java.io.Serializable;

public class Hospedagem implements Serializable {
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

    public double calculaTotal(){
        return this.numeroDeNoites*this.valorDiaria;
    }
}
