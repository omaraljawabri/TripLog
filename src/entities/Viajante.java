package entities;

import java.time.LocalDate;
import java.util.List;

public class Viajante {
    private static int id;
    private String nome;
    private String senha;
    private LocalDate dataDeNascimento;
    private List<Viagem> viagens;

    public Viajante() {
        Viajante.id++;
    }

    public Viajante(String nome, String senha, LocalDate dataDeNascimento, List<Viagem> viagens) {
        Viajante.id++;
        this.nome = nome;
        this.senha = senha;
        this.dataDeNascimento = dataDeNascimento;
        this.viagens = viagens;
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

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public List<Viagem> getViagens() {
        return viagens;
    }

    public void setViagens(List<Viagem> viagens) {
        this.viagens = viagens;
    }
}
