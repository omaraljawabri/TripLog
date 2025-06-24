package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = -2063544862544769327L;

    private String nome;
    private double gasto;
    private LocalDateTime data;

    public Atividade() {
    }

    public Atividade(String nome, double gasto, LocalDateTime data) {
        this.nome = nome;
        this.gasto = gasto;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public double getGasto() {
        return gasto;
    }

    public LocalDateTime getData() {
        return data;
    }
}
