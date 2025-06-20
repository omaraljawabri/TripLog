package backend.main.services;

import backend.main.entities.Viagem;
import backend.main.entities.Viajante;
import backend.main.exceptions.EntityNotFoundException;
import backend.main.exceptions.ValidationException;
import backend.main.repositories.ViagemRepository;

import java.util.ArrayList;
import java.util.List;

public class ViagemService {

    //Injeção de dependências
    private final ViagemRepository viagemRepository;

    public ViagemService(ViagemRepository viagemRepository) {
        this.viagemRepository = viagemRepository;
    }

    public void adicionarViagem(Viagem viagem, Viajante viajante){
        if (viagem.getLugarDePartida() == null || viagem.getLugarDeChegada() == null || viagem.getDeslocamentos().isEmpty()
                || viagem.getHospedagens().isEmpty() || viagem.getAtividades().isEmpty()){
            throw new ValidationException("Atributos lugar de partida, lugar de chegada, deslocamentos, hospedagens e atividades devem ser preenchidos");
        }
        viagem.setIdViajante(viajante.getId());
        boolean resultado = viagemRepository.salvarViagem(viagem);
        if (!resultado){
            throw new RuntimeException("Erro ao adicionar nova viagem");
        }

        if (viajante.getViagens() == null){
            viajante.setViagens(new ArrayList<>(List.of(viagem)));
            return;
        }

        viajante.getViagens().add(viagem);
    }

    public List<Viagem> listarViagens(Viajante viajante){
        List<Viagem> viagensListadas = viagemRepository.buscarViagensPorIdViajante(viajante.getId());
        viajante.setViagens(viagensListadas);
        return viagensListadas;
    }

    public Viagem buscarViagemPorId(int id, Viajante viajante){
        Viagem viagem = viagemRepository.buscarViagemPorId(id, viajante.getId());
        if (viagem == null){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada!");
        }
        return viagem;
    }

    public void removerViagem(int id, Viajante viajante){
        boolean isRemovido = viagemRepository.removerViagemPorId(id, viajante.getId());
        if (!isRemovido){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada!");
        }
    }

    public void editarViagem(int id, Viagem viagem, Viajante viajante){
        boolean isEditada = viagemRepository.editarViagemPorId(id, viajante.getId(), viagem);
        if (!isEditada){
            throw new EntityNotFoundException("Viagem com id: "+id+", não encontrada!");
        }
    }
}
