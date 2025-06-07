package entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Evento extends Atividade implements Serializable {
    private int id;
    private static int contador;
    private String tema;

    public Evento() {
        Evento.contador++;
        this.id = Evento.contador;
    }

    public Evento(String nome, List<Gasto> gastos, LocalDateTime horario, String tema) {
        super(nome, gastos, horario);
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
