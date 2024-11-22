package solaris.repositorios;

import solaris.entidades.Usuario;
import solaris.infraestrutura.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositorio {

    public void cadastrar(Usuario usuario) {
        if (cpfOuEmailJaExistem(usuario.getCpf(), usuario.getEmail())) {
            throw new RuntimeException("CPF ou e-mail já estão cadastrados.");
        }

        String query = "INSERT INTO T_USUARIO_SOLARIS (CPF, NM_USUARIO, EMAIL, SENHA) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage(), e);
        }
    }

    private boolean cpfOuEmailJaExistem(String cpf, String email) {
        String query = "SELECT COUNT(*) FROM T_USUARIO_SOLARIS WHERE CPF = ? OR EMAIL = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cpf);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar CPF ou e-mail: " + e.getMessage(), e);
        }
        return false;
    }

    public void atualizar(Usuario usuario, int id) {
        String query = "UPDATE T_USUARIO_SOLARIS SET CPF = ?, NM_USUARIO = ?, EMAIL = ?, SENHA = ? WHERE ID_USUARIO = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setInt(5, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Usuário não encontrado para atualização: ID " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }

    public void remover(int id) {
        String query = "DELETE FROM T_USUARIO_SOLARIS WHERE ID_USUARIO = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Usuário não encontrado para remoção: ID " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover usuário: " + e.getMessage(), e);
        }
    }

    public Optional<Usuario> buscarPorId(int id) {
        String query = "SELECT * FROM T_USUARIO_SOLARIS WHERE ID_USUARIO = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getInt("ID_USUARIO"),
                            rs.getString("CPF"),
                            rs.getString("NM_USUARIO"),
                            rs.getString("EMAIL"),
                            rs.getString("SENHA")
                    );
                    return Optional.of(usuario);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        String query = "SELECT * FROM T_USUARIO_SOLARIS WHERE EMAIL = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getInt("ID_USUARIO"),
                            rs.getString("CPF"),
                            rs.getString("NM_USUARIO"),
                            rs.getString("EMAIL"),
                            rs.getString("SENHA")
                    );
                    return Optional.of(usuario);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por e-mail: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Usuario> listar() {
        String query = "SELECT * FROM T_USUARIO_SOLARIS";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("ID_USUARIO"),
                        rs.getString("CPF"),
                        rs.getString("NM_USUARIO"),
                        rs.getString("EMAIL"),
                        rs.getString("SENHA")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
        return usuarios;
    }
}
