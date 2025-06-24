package backend.main.services;

import backend.main.entities.Viajante;
import backend.main.exceptions.EntidadeNaoEncontradaException;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViajanteRepository;
import backend.main.utils.SenhaUtil;

public class ViajanteService {

    // Injeção de dependências
    private final ViajanteRepository viajanteRepository;

    public ViajanteService(ViajanteRepository viajanteRepository) {
        this.viajanteRepository = viajanteRepository;
    }

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

    public int buscarMaiorId(){
        return viajanteRepository.buscarMaiorId();
    }
}
