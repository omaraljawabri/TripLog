package backend.main.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Restaurante extends Atividade implements Serializable {
    private static int contador;
    private int id;
    private String nomeRestaurante;
    private String culinaria;
    private String prato;

    public Restaurante(String nome, double gasto, LocalDateTime data, String nomeRestaurante, String culinaria, String prato) {
        super(nome, gasto, data);
        Restaurante.contador++;
        this.id = Restaurante.contador;
        this.nomeRestaurante = nomeRestaurante;
        this.culinaria = culinaria;
        this.prato = prato;
    }

    public Restaurante() {
        Restaurante.contador++;
        this.id = Restaurante.contador;
    }

    public int getId() {
        return id;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public void setNomeRestaurante(String nomeRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
    }

    public String getCulinaria() {
        return culinaria;
    }

    public void setCulinaria(String culinaria) {
        this.culinaria = culinaria;
    }

    public String getPrato() {
        return prato;
    }

    public void setPrato(String prato) {
        this.prato = prato;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nomeRestaurante='" + nomeRestaurante + '\'' +
                ", culinaria='" + culinaria + '\'' +
                ", prato='" + prato + '\'' +
                '}';
    }
}
