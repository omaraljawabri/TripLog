package backend.main.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public abstract class Atividade implements Serializable {
    private String nome;
    private List<Gasto> gastos;
    private LocalDateTime data;

    public Atividade() {
    }

    public Atividade(String nome, List<Gasto> gastos, LocalDateTime data) {
        this.nome = nome;
        this.gastos = gastos;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public LocalDateTime getData() {
        return data;
    }
}
