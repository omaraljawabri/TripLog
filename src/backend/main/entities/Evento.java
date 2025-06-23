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

    public Evento(String nome, double gasto, LocalDateTime horario, String tema) {
        super(nome, gasto, horario);
        Evento.contador++;
        this.id = Evento.contador;
        this.tema = tema;
    }

    public int getId() {
        return id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", tema='" + tema + '\'' +
                '}';
    }
}
