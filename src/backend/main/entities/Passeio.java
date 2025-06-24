package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Passeio extends Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = 3326632644976495366L;

    private String nomeLocal;

    public Passeio() {
    }

    public Passeio(String nome, double gasto, LocalDateTime data, String nomeLocal) {
        super(nome, gasto, data);
        this.nomeLocal = nomeLocal;
    }
}
