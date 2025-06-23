package backend.main.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Passeio extends Atividade implements Serializable {
    private static int contador;
    private int id;
    private String nomeLocal;

    public Passeio() {
        Passeio.contador++;
        this.id = Passeio.contador;
    }

    public Passeio(String nome, double gasto, LocalDateTime horario, String nomeLocal) {
        super(nome, gasto, horario);
        Passeio.contador++;
        this.id = Passeio.contador;
        this.nomeLocal = nomeLocal;
    }

    public int getId() {
        return id;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    @Override
    public String toString() {
        return "Passeio{" +
                "id=" + id +
                ", nomeLocal='" + nomeLocal + '\'' +
                '}';
    }
}
