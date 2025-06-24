package backend.main.services;

import backend.main.entities.Viajante;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViajanteRepository;
import backend.main.utils.SenhaUtil;

/**
 * Classe responsável pelas regras de negócio ligadas as operações de edição e busca de id da entidade Viajante.
 * Essa classe se comunica com a camada repository {@link ViajanteRepository} para buscar as informações dos arquivos e também
 * se comunica com a camada do frontend para exibir informações na tela.
 * */
public class ViajanteService {

    // Injeção de dependências
    private final ViajanteRepository viajanteRepository;

    /**
     * Construtor que recebe a camada repository para fazer as operações e chamar os métodos que ligam ao arquivo.
     * Conceito de injeção de dependências foi utilizado.
     *
     * @param viajanteRepository Classe repository que se comunicará com o service.
     * */
    public ViajanteService(ViajanteRepository viajanteRepository) {
        this.viajanteRepository = viajanteRepository;
    }

    /**
     * Método responsável por editar um viajante, inicialmente validando as informações do viajante e posteriormente
     * chamando a camada repository para concluir a edição.
     *
     * @param email Email do viajante que será editado
     * @param nome Nome do viajante a ser editado
     * @param senhaAntiga Senha salva anteriormente do viajante para caráter de validação
     * @param senhaNova Nova senha do viajante que será salva
     *
     * @exception EntidadeNaoEncontradaException em caso de não haver viajante com email passado como parâmetro salvo no arquivo
     * @exception ValidacaoException em caso da senha antiga passada como parâmetro não ser igual a senha salva no arquivo
     * @exception ErroInternoException em caso de algum erro na edição do viajante como, por exemplo, erro no acesso ao arquivo
     * */
    public void editarViajante(String email, String nome, String senhaAntiga, String senhaNova){
        Viajante viajante = viajanteRepository.buscarViajantePorEmail(email);
        if (viajante == null){
            throw new EntidadeNaoEncontradaException("Não há viajante salvo com o email: "+email);
        }
        boolean resultado = SenhaUtil.verificarSenha(senhaAntiga, viajante.getSenha());
        if (!resultado){
            throw new ValidacaoException("Senha atual digitada não é igual a senha salva");
        }
        String senhaHash = SenhaUtil.hashSenha(senhaNova);

        boolean resultadoEdicao = viajanteRepository.editarViajantePorEmail(email, nome, senhaHash);

        if (!resultadoEdicao){
            throw new ErroInternoException("Erro ao editar viajante");
        }
    }

    /**
     * Método responsável por chamar a camada repository e retornar o maior id de um viajante salvo em arquivo no momento
     *
     * @return Maior id de viajante salvo no arquivo até o momento
     * */
    public int buscarMaiorId(){
        return viajanteRepository.buscarMaiorId();
    }
}
