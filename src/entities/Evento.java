package entities;

import java.time.LocalDateTime;
import java.util.List;

public class Evento extends Atividade{
    private static int id;
    private String tema;

    public Evento() {
    }

    public Evento(String nome, List<Gasto> gastos, LocalDateTime horario, String tema) {
        super(nome, gastos, horario);
        Evento.id++;
        this.tema = tema;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}
