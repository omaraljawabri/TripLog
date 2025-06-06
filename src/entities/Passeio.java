package entities;

import java.time.LocalDateTime;
import java.util.List;

public class Passeio extends Atividade{
    private static int id;
    private String nomeLocal;

    public Passeio() {
    }

    public Passeio(String nome, List<Gasto> gastos, LocalDateTime horario, String nomeLocal) {
        super(nome, gastos, horario);
        Passeio.id++;
        this.nomeLocal = nomeLocal;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }
}
