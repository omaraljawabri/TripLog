package backend.main.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Evento extends Atividade implements Serializable {
    private int id;
    private static int contador;
    private String tema;

    public Evento() {
        Evento.contador++;
        this.id = Evento.contador;
    }

    public Evento(String nome, double gasto, LocalDateTime data, String tema) {
        super(nome, gasto, data);
        Evento.contador++;
        this.id = Evento.contador;
        this.tema = tema;
    }
}
