package entities;

import exceptions.EntityNotFoundException;
import exceptions.ValidationException;
import repositories.ViagemRepository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Viajante implements Serializable {
    private static int contador;
    private int id;
    private String nome;
    private String senha;
    private LocalDate dataDeNascimento;
    private List<Viagem> viagens;

    public Viajante() {
        Viajante.contador++;
        this.id = Viajante.contador;
    }

    public Viajante(String nome, String senha, LocalDate dataDeNascimento, List<Viagem> viagens) {
        Viajante.contador++;
        this.id = Viajante.contador;
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

    public void adicionarViagem(Viagem v){
        if (v.getLugarDePartida() == null || v.getLugarDeChegada() == null || v.getDeslocamentos().isEmpty()
        || v.getHospedagens().isEmpty() || v.getAtividades().isEmpty()){
            throw new ValidationException("Atributos lugar de partida, lugar de chegada, deslocamentos, hospedagens e atividades devem ser preenchidos");
        }
        boolean resultado = ViagemRepository.salvarViagem(v);
        if (!resultado){
            throw new RuntimeException("Erro ao adicionar nova viagem");
        }
    }

    public List<Viagem> listarViagens(){
        return ViagemRepository.buscarTodasViagens();
    }

    public Viagem buscarViagemPorId(int id){
        Viagem viagem = ViagemRepository.buscarViagemPorId(id);
        if (viagem == null){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada!");
        }
        System.out.println(viagem);
        return viagem;
    }

    public void removerViagem(int id){
        boolean isRemovido = ViagemRepository.removerViagemPorId(id);
        if (!isRemovido){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada");
        }
    }

    public void editarViagem(int id, Viagem viagem){
        boolean isEditada = ViagemRepository.editarViagemPorId(id, viagem);
        if (!isEditada){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada");
        }
    }
}
