package backend.main.services;

import backend.main.entities.Viajante;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViajanteRepository;
import backend.main.utils.SenhaUtil;

public class AuthService {

    //Injeção de dependências
    private final ViajanteRepository viajanteRepository;

    public AuthService(ViajanteRepository viajanteRepository) {
        this.viajanteRepository = viajanteRepository;
    }

    public void cadastrar(Viajante viajante){
        if (viajante.getEmail() == null || viajante.getNome() == null || viajante.getSenha() == null){
            throw new ValidacaoException("Email, nome e senha devem ser preenchidos");
        }

        Viajante viajantePorEmail = viajanteRepository.buscarViajantePorEmail(viajante.getEmail());
        if (viajantePorEmail != null){
            throw new ValidacaoException("Email informado já está em uso");
        }

        String senhaCodificada = SenhaUtil.hashSenha(viajante.getSenha());
        viajante.setSenha(senhaCodificada);
        boolean resultado = viajanteRepository.salvarViajante(viajante);

        if (!resultado){
            throw new ErroInternoException("Erro ao fazer cadastro");
        }
    }

    public Viajante login(String senha, String email){
        Viajante viajanteBuscado = viajanteRepository.buscarViajantePorEmail(email);
        if (viajanteBuscado == null || !SenhaUtil.verificarSenha(senha, viajanteBuscado.getSenha())){
            throw new ValidacaoException("Viajante com email ou senha incorreto(s)!");
        }
        return viajanteBuscado;
    }
}
