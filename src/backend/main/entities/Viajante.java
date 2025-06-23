package backend.main.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Viajante implements Serializable {
    private static int contador;
    private int id;
    private String nome;
    private String email;
    private String senha;
    private List<Viagem> viagens = new ArrayList<>();

    public Viajante() {
        Viajante.contador++;
        this.id = Viajante.contador;
    }

    public Viajante(String nome, String senha, String email) {
        Viajante.contador++;
        this.id = Viajante.contador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Viagem> getViagens() {
        return viagens;
    }

    public void setViagens(List<Viagem> viagens) {
        this.viagens = viagens;
    }

}
