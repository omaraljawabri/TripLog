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

    public Passeio(String nome, double gasto, LocalDateTime data, String nomeLocal) {
        super(nome, gasto, data);
        Passeio.contador++;
        this.id = Passeio.contador;
        this.nomeLocal = nomeLocal;
    }
}
