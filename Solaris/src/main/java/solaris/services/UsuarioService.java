package solaris.services;

import solaris.entidades.Usuario;
import solaris.infraestrutura.Log4jLogger;
import solaris.repositorios.UsuarioRepositorio;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UsuarioService {
    private final UsuarioRepositorio usuarioRepositorio;
    private final Log4jLogger logger = new Log4jLogger();

    private static final String CPF_REGEX = "^\\d{11}$";
    private static final String SENHA_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\W).{8,}$";

    public UsuarioService() {
        this.usuarioRepositorio = new UsuarioRepositorio();
    }

    public void registrar(Usuario usuario) {
        try {
            validarUsuario(usuario);
            usuarioRepositorio.cadastrar(usuario);
            logger.info("Usuário registrado com sucesso: " + usuario.getEmail());
        } catch (Exception e) {
            logger.error("Erro ao registrar usuário: " + e.getMessage());
            throw e;
        }
    }

    public void atualizar(int id, Usuario usuario) {
        try {
            validarUsuario(usuario);
            usuarioRepositorio.atualizar(usuario, id);
            logger.info("Usuário atualizado com sucesso: " + usuario.getEmail());
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: " + e.getMessage());
            throw e;
        }
    }

    public void deletar(int id) {
        try {
            usuarioRepositorio.remover(id);
            logger.info("Usuário removido com sucesso: ID " + id);
        } catch (Exception e) {
            logger.error("Erro ao remover usuário: " + e.getMessage());
            throw e;
        }
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarioRepositorio.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepositorio.buscarPorEmail(email);
    }

    public List<Usuario> listar() {
        return usuarioRepositorio.listar();
    }

    public boolean autenticar(String email, String senha) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepositorio.buscarPorEmail(email);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                boolean senhaValida = usuario.getSenha().equals(senha);

                if (senhaValida) {
                    logger.info("Login bem-sucedido para o email: " + email);
                    return true;
                }
            }

            logger.warn("Tentativa de login falhou para o email: " + email);
            return false;
        } catch (Exception e) {
            logger.error("Erro ao autenticar usuário: " + e.getMessage());
            throw e;
        }
    }

    private void validarUsuario(Usuario usuario) {
        if (!Pattern.matches(CPF_REGEX, usuario.getCpf())) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos numéricos.");
        }
        if (!Pattern.matches(SENHA_REGEX, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha inválida. Deve conter ao menos 8 caracteres, incluindo maiúscula, minúscula e caractere especial.");
        }
    }
}
