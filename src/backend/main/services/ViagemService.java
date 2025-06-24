package backend.main.services;

import backend.main.entities.Viagem;
import backend.main.entities.Viajante;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViagemRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas regras de negócio ligadas as operações CRUD (Create, Read, Update and Delete) da entidade Viagem.
 * Essa classe se comunica com a camada repository {@link ViagemRepository} para buscar as informações dos arquivos e também
 * se comunica com a camada do frontend para exibir informações na tela.
 * */
public class ViagemService {

    //Injeção de dependências
    private final ViagemRepository viagemRepository;

    /**
     * Construtor que recebe a camada repository para fazer as operações e chamar os métodos que ligam ao arquivo.
     * Conceito de injeção de dependências foi utilizado.
     *
     * @param viagemRepository Classe repository que se comunicará com o service.
     * */
    public ViagemService(ViagemRepository viagemRepository) {
        this.viagemRepository = viagemRepository;
    }

    /**
     * Método responsável por validar os atributos de uma viagem a ser adicionada e enviar a viagem para a camada repository
     * onde será adicionada ao arquivo.
     *
     * @param viagem Viagem a ser validada e enviada para a camada repository
     * @param viajante Viajante que deseja registrar a viagem
     *
     * @exception ValidacaoException em caso de algum(ns) dos atributos considerados obrigatórios não for preenchido.
     * @exception ErroInternoException em caso de erro interno ao tentar adicionar a viagem, como falha no arquivo, por exemplo.
     * */
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

    /**
     * Método responsável por buscar da camada repository todas as viagens presentes no arquivo que pertençam ao
     * viajante passado como parâmetro.
     *
     * @param viajante Viajante que deve ter suas viagens buscadas.
     * @return Lista de Viagem {@link Viagem} pertencentes ao viajante.
     * */
    public List<Viagem> listarViagens(Viajante viajante){
        List<Viagem> viagensListadas = viagemRepository.buscarViagensPorEmailViajante(viajante.getEmail());
        viajante.setViagens(viagensListadas);
        return viagensListadas;
    }

    /**
     * Método responsável por buscar da camada repository todas as viagens presentes no arquivo de acordo com filtros
     * passados como parâmetro e que pertençam ao viajante com email também passado como parâmetro.
     *
     * @param emailViajante Email do viajante que deve ter suas viagens buscadas.
     * @param destino Destino que deve ser usado como filtro na busca das viagens
     * @param companhia Companhia que deve ser usada como filtro na busca das viagens
     * @param gasto Valor mínimo gasto que deve ser usado como filtro na busca das viagens
     *
     * @return Lista de viagens filtradas {@link Viagem}
     * */
    public List<Viagem> buscarViagensFiltradas(String emailViajante, String destino, String companhia, Double gasto){
        return viagemRepository.buscarViagensFiltradas(emailViajante, destino, companhia, gasto);
    }

    /**
     * Método responsável por buscar todas as viagens que pertençam ao viajante com email passado como parâmetro;
     *
     * @param emailViajante Email do viajante que deve ter suas viagens buscadas
     *
     * @return Lista de viagens buscadas de acordo com o email do viajante {@link Viagem}
     * */
    public List<Viagem> buscarTodasViagensPorEmailViajante(String emailViajante){
        return viagemRepository.buscarViagensPorEmailViajante(emailViajante);
    }

    /**
     * Método responsável por enviar uma viagem a ser removida para a camada repository e tratar um possível erro caso o id
     * da viagem não exista.
     *
     * @param id Id da viagem que deve ser removida
     * @param viajante Viajante que solicitou a remoção para confirmar se a viagem pertence a ele
     *
     * @exception EntidadeNaoEncontradaException em caso do id da viagem passado como parâmetro não pertencer a nenhuma viagem no arquivo
     * */
    public void removerViagem(int id, Viajante viajante){
        boolean isRemovido = viagemRepository.removerViagemPorId(id, viajante.getEmail());
        if (!isRemovido){
            throw new EntidadeNaoEncontradaException("Viagem com id: "+id+", não encontrada!");
        }
    }

    /**
     * Método responsável por enviar uma viagem a ser editada para a camada repository e tratar um possível erro caso o
     * id da viagem não exista
     *
     * @param id Id da viagem a ser edita
     * @param viagem Dados da viagem que substituirão os dados antigos
     * @param viajante Viajante que solicitou as mudanças para confirmar se a viagem pertence a ele
     *
     * @exception EntidadeNaoEncontradaException em caso do id da viagem passado como parâmetro não pertencer a nenhuma viagem no arquivo
     * */
    public void editarViagem(int id, Viagem viagem, Viajante viajante){
        boolean isEditada = viagemRepository.editarViagemPorId(id, viajante.getEmail(), viagem);
        if (!isEditada){
            throw new EntidadeNaoEncontradaException("Viagem com id: "+id+", não encontrada!");
        }
    }

    /**
     * Método responsável por chamar o repository e retornar o maior id de uma entidade viagem presente no arquivo até
     * o momento.
     *
     * @return Maior id de uma entidade viagem presente no arquivo
     * */
    public int buscarMaiorId(){
        return viagemRepository.buscarMaiorId();
    }
}
