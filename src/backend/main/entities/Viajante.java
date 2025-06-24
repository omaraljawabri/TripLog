package backend.main.entities;

import backend.main.repositories.ViajanteRepository;
import backend.main.services.ViajanteService;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Viajante implements Serializable {
    @Serial
    private static final long serialVersionUID = -6772402613425968371L;

    private static int contador;
    private int id;
    private String nome;
    private String email;
    private String senha;
    private List<Viagem> viagens = new ArrayList<>();

    public Viajante() {
        if (Viajante.contador == 0){
            ViajanteRepository viajanteRepository = new ViajanteRepository("viajante.ser");
            ViajanteService viajanteService = new ViajanteService(viajanteRepository);
            Viajante.contador = viajanteService.buscarMaiorId();
        }
        Viajante.contador++;
        this.id = Viajante.contador;
    }

    public Viajante(String nome, String senha, String email) {
        if (Viajante.contador == 0){
            ViajanteRepository viajanteRepository = new ViajanteRepository("viajante.ser");
            ViajanteService viajanteService = new ViajanteService(viajanteRepository);
            Viajante.contador = viajanteService.buscarMaiorId();
        }
        Viajante.contador++;
        this.id = Viajante.contador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
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

    public static void resetarContador(){
        Viajante.contador = 0;
    }
}
