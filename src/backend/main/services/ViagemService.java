package backend.main.services;

import backend.main.entities.Viagem;
import backend.main.entities.Viajante;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.SemResultadoException;
import backend.main.exceptions.ValidacaoException;
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
            throw new ValidacaoException("Atributos lugar de partida, lugar de chegada, deslocamentos, hospedagens e atividades devem ser preenchidos");
        }
        viagem.setEmailViajante(viajante.getEmail());
        boolean resultado = viagemRepository.salvarViagem(viagem);
        if (!resultado){
            throw new ErroInternoException("Erro ao adicionar nova viagem");
        }

        if (viajante.getViagens() == null){
            viajante.setViagens(new ArrayList<>(List.of(viagem)));
            return;
        }

        viajante.getViagens().add(viagem);
    }

    public List<Viagem> listarViagens(Viajante viajante){
        List<Viagem> viagensListadas = viagemRepository.buscarViagensPorEmailViajante(viajante.getEmail());
        viajante.setViagens(viagensListadas);
        return viagensListadas;
    }

    public List<Viagem> buscarViagensFiltradas(String emailViajante, String destino, String companhia, Double gasto){
        return viagemRepository.buscarViagensFiltradas(emailViajante, destino, companhia, gasto);
    }

    public List<Viagem> buscarTodasViagensPorEmailViajante(String emailViajante){
        return viagemRepository.buscarViagensPorEmailViajante(emailViajante);
    }

    public void removerViagem(int id, Viajante viajante){
        boolean isRemovido = viagemRepository.removerViagemPorId(id, viajante.getEmail());
        if (!isRemovido){
            throw new EntidadeNaoEncontradaException("Viagem com id: "+id+", não encontrada!");
        }
    }

    public void editarViagem(int id, Viagem viagem, Viajante viajante){
        boolean isEditada = viagemRepository.editarViagemPorId(id, viajante.getEmail(), viagem);
        if (!isEditada){
            throw new EntidadeNaoEncontradaException("Viagem com id: "+id+", não encontrada!");
        }
    }

    public int buscarMaiorIdPorEmailViajante(String emailViajante){
        return viagemRepository.buscarMaiorIdPorEmailViajante(emailViajante);
    }
}
