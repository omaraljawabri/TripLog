package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe que representa a hospedagem que ocorreu na Viagem, cada Viagem pode possuir várias hospedagens.
 * Cada hospedagem possui o nome do local onde ocorreu a hospedagem, o numero de noites e o valor da diária pago.
 * */
public class Hospedagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 5990586390555358684L;

    private String nomeLocalHospedagem;
    private int numeroDeNoites;
    private double valorDiaria;

    /**
     * Construtor vazio para criação de uma Hospedagem.
     * Utilizado para facilitação nos testes unitários.
     * */
    public Hospedagem(){
    }

    /**
     * Construtor utilizado para instanciar uma Hospedagem
     * @param nomeLocalHospedagem Nome do local onde ocorreu a hospedagem
     * @param numeroDeNoites Número de noites em que o usuário se hospedou no local
     * @param valorDiaria Valor pago por dia no local da hospedagem
     * */
    public Hospedagem(String nomeLocalHospedagem, int numeroDeNoites, double valorDiaria) {
        this.nomeLocalHospedagem = nomeLocalHospedagem;
        this.numeroDeNoites = numeroDeNoites;
        this.valorDiaria = valorDiaria;
    }

    /**
     * Método que calcula o valor total gasto na hospedagem, multiplicando o número de noites pelo valor da diária.
     * @return Valor total gasto na hospedagem
     * */
    public double calculaTotal(){
        return this.numeroDeNoites*this.valorDiaria;
    }

    /**
     * Busca e retorna o nome do local da hospedagem
     * @return Nome do local da hospedagem
     * */
    public String getNomeLocalHospedagem() {
        return nomeLocalHospedagem;
    }

    /**
     * Busca e retorna o número de noites que o usuário se hospedou no local
     * @return Número de noites da hospedagem
     * */
    public int getNumeroDeNoites() {
        return numeroDeNoites;
    }

    /**
     * Busca e retorna o valor da diária gasto na hospedagem
     * @return Valor da diária
     * */
    public double getValorDiaria() {
        return valorDiaria;
    }
}
