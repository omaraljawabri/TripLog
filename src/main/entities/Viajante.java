package main.entities;

import main.exceptions.EntityNotFoundException;
import main.exceptions.ValidationException;
import main.repositories.ViagemRepository;
import main.repositories.ViajanteRepository;
import main.utils.SenhaUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Viajante implements Serializable {
    private static int contador;
    private int id;
    private String nome;
    private String email;
    private String senha;
    private List<Viagem> viagens;

    private transient ViagemRepository viagemRepository;
    private transient ViajanteRepository viajanteRepository;

    public Viajante(ViagemRepository viagemRepository, ViajanteRepository viajanteRepository) {
        Viajante.contador++;
        this.id = Viajante.contador;
        this.viagemRepository = viagemRepository;
        this.viajanteRepository = viajanteRepository;
    }

    public Viajante(String nome, String senha, String email, ViagemRepository viagemRepository, ViajanteRepository viajanteRepository) {
        Viajante.contador++;
        this.id = Viajante.contador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.viagemRepository = viagemRepository;
        this.viajanteRepository = viajanteRepository;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getId() {
        return id;
    }

    public void adicionarViagem(Viagem v){
        if (v.getLugarDePartida() == null || v.getLugarDeChegada() == null || v.getDeslocamentos().isEmpty()
        || v.getHospedagens().isEmpty() || v.getAtividades().isEmpty()){
            throw new ValidationException("Atributos lugar de partida, lugar de chegada, deslocamentos, hospedagens e atividades devem ser preenchidos");
        }
        v.setIdViajante(this.id);
        boolean resultado = viagemRepository.salvarViagem(v);
        if (!resultado){
            throw new RuntimeException("Erro ao adicionar nova viagem");
        }

        if (this.getViagens() == null){
            this.setViagens(new ArrayList<>(List.of(v)));
            return;
        }

        this.getViagens().add(v);
    }

    public List<Viagem> listarViagens(){
        List<Viagem> viagensListadas = viagemRepository.buscarViagensPorIdViajante(this.id);
        this.setViagens(viagensListadas);
        return viagensListadas;
    }

    public Viagem buscarViagemPorId(int id){
        Viagem viagem = viagemRepository.buscarViagemPorId(id, this.id);
        if (viagem == null){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada!");
        }
        return viagem;
    }

    public void removerViagem(int id){
        boolean isRemovido = viagemRepository.removerViagemPorId(id, this.id);
        if (!isRemovido){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada!");
        }
    }

    public void editarViagem(int id, Viagem viagem){
        boolean isEditada = viagemRepository.editarViagemPorId(id, this.id, viagem);
        if (!isEditada){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada!");
        }
    }

    public void cadastrar(){
        if (this.email == null || this.nome == null || this.senha == null){
            throw new ValidationException("Email, nome e senha devem ser preenchidos");
        }
        String senhaCodificada = SenhaUtil.hashSenha(this.senha);
        this.setSenha(senhaCodificada);
        boolean resultado = viajanteRepository.salvarViajante(this);

        if (!resultado){
            throw new RuntimeException("Erro ao cadastrar viajante");
        }
    }

    public Viajante login(){
        Viajante viajante = viajanteRepository.buscarViajantePorEmail(this.email);
        if (viajante == null || !SenhaUtil.verificarSenha(this.senha, viajante.getSenha())){
            throw new ValidationException("Viajante com email ou senha incorreto(s)!");
        }
        return viajante;
    }
}
