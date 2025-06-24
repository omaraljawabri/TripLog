package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Evento extends Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = 1822957277203428237L;

    private String tema;

    public Evento() {

    }

    public Evento(String nome, double gasto, LocalDateTime data, String tema) {
        super(nome, gasto, data);
        this.tema = tema;
    }
}
