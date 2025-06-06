package entities;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Atividade {
    private String nome;
    private List<Gasto> gastos;
    private LocalDateTime horario;

    public Atividade() {
    }

    public Atividade(String nome, List<Gasto> gastos, LocalDateTime horario) {
        this.nome = nome;
        this.gastos = gastos;
        this.horario = horario;
    }
}
