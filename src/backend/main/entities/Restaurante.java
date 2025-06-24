package backend.main.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Restaurante extends Atividade implements Serializable {
    @Serial
    private static final long serialVersionUID = 8528758322962441714L;

    private String nomeRestaurante;
    private String culinaria;
    private String prato;

    public Restaurante(String nome, double gasto, LocalDateTime data, String nomeRestaurante, String culinaria, String prato) {
        super(nome, gasto, data);
        this.nomeRestaurante = nomeRestaurante;
        this.culinaria = culinaria;
        this.prato = prato;
    }

    public Restaurante() {
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public String getCulinaria() {
        return culinaria;
    }

    public String getPrato() {
        return prato;
    }
}
