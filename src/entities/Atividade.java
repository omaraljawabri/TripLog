package entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Atividade implements Serializable {
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

    public String getNome() {
        return nome;
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public LocalDateTime getHorario() {
        return horario;
    }
}
