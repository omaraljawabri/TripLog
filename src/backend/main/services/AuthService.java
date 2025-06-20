package backend.main.services;

import backend.main.entities.Viajante;
import backend.main.exceptions.ValidationException;
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
            throw new ValidationException("Email, nome e senha devem ser preenchidos");
        }
        String senhaCodificada = SenhaUtil.hashSenha(viajante.getSenha());
        viajante.setSenha(senhaCodificada);
        boolean resultado = viajanteRepository.salvarViajante(viajante);

        if (!resultado){
            throw new RuntimeException("Erro ao fazer cadastro");
        }
    }

    public Viajante login(String senha, String email){
        Viajante viajanteBuscado = viajanteRepository.buscarViajantePorEmail(email);
        if (viajanteBuscado == null || !SenhaUtil.verificarSenha(senha, viajanteBuscado.getSenha())){
            throw new ValidationException("Viajante com email ou senha incorreto(s)!");
        }
        return viajanteBuscado;
    }
}
