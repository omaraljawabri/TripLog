package backend.main.services;

import backend.main.entities.Viajante;
import backend.main.exceptions.ErroInternoException;
import backend.main.exceptions.ValidacaoException;
import backend.main.repositories.ViajanteRepository;
import backend.main.utils.SenhaUtil;

/**
 * Classe responsável pelas regras de negócio ligadas com o cadastro e login de usuários viajantes no sistema.
 * Essa classe se comunica com a camada repository {@link ViajanteRepository} para buscar as informações dos arquivos e também
 * se comunica com a camada do frontend para exibir informações na tela.
 * */
public class AuthService {

    //Injeção de dependências
    private final ViajanteRepository viajanteRepository;

    /**
     * Construtor que recebe a camada repository para fazer as operações e chamar os métodos que ligam ao arquivo.
     * Conceito de injeção de dependências foi utilizado.
     *
     * @param viajanteRepository Classe repository que se comunicará com o service.
     * */
    public AuthService(ViajanteRepository viajanteRepository) {
        this.viajanteRepository = viajanteRepository;
    }

    /**
     * Método responsável por cadastrar um usuário no sistema, verificações de atributos nulos e email em uso são feitas
     *
     * @param viajante Viajante a ser cadastrado no sistema
     *
     * @exception ValidacaoException Lançada em caso de email, nome ou senha não serem preenchidos.
     * @exception ValidacaoException Lançada em caso de email do viajante a ser cadastrado já existir no sistema
     * @exception ErroInternoException Lançada em caso de erros internos como arquivo inacessível.
     * */
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

    /**
     * Método responsável por logar um usuário viajante no sistema. Atributos email e senha são recebidos e verifica-se
     * se existe um usuário no sistema com esses atributos. Se sim, é retornado um viajante, se não é retornada uma exceção.
     *
     * @param senha Senha do usuário que deseja fazer login
     * @param email Email do usuário que deseja fazer login
     *
     * @return Viajante com suas informações após ser logado
     * ou
     * @exception ValidacaoException Quando informações do usuário (email e/ou senha) estão incorretas
     * */
    public Viajante login(String senha, String email){
        Viajante viajanteBuscado = viajanteRepository.buscarViajantePorEmail(email);
        if (viajanteBuscado == null || !SenhaUtil.verificarSenha(senha, viajanteBuscado.getSenha())){
            throw new ValidacaoException("Viajante com email ou senha incorreto(s)!");
        }
        return viajanteBuscado;
    }
}
