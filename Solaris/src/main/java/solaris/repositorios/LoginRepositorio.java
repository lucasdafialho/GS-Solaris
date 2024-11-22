package solaris.repositorios;

import solaris.entidades.Login;
import solaris.infraestrutura.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginRepositorio implements _RepositorioBase<Login> {
    private final Connection connection;

    public LoginRepositorio(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void cadastrar(Login login) {
        String sql = "INSERT INTO T_LOGIN_SOLARIS (SENHA, EMAIL, ID_USUARIO) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login.getSenha());
            stmt.setString(2, login.getEmail());
            stmt.setLong(3, login.getIdUsuario());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Falha ao cadastrar login.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar login: " + e.getMessage(), e);
        }
    }


    @Override
    public void atualizar(Login login, int id) {
        String sql = "UPDATE T_LOGIN_SOLARIS SET SENHA = ?, EMAIL = ?, ID_USUARIO = ? WHERE ID_LOGIN = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login.getSenha());
            stmt.setString(2, login.getEmail());
            stmt.setLong(3, login.getIdUsuario());
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar login: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id) {
        String sql = "DELETE FROM T_LOGIN_SOLARIS WHERE ID_LOGIN = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover login: " + e.getMessage());
        }
    }

    @Override
    public Optional<Login> buscarPorId(int id) {
        String sql = "SELECT * FROM T_LOGIN_SOLARIS WHERE ID_LOGIN = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Login login = new Login(
                            rs.getLong("ID_LOGIN"),
                            rs.getString("SENHA"),
                            rs.getString("EMAIL"),
                            rs.getLong("ID_USUARIO")
                    );
                    return Optional.of(login);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar login por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Login> listar() {
        String sql = "SELECT * FROM T_LOGIN_SOLARIS";
        List<Login> logins = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logins.add(new Login(
                        rs.getLong("ID_LOGIN"),
                        rs.getString("SENHA"),
                        rs.getString("EMAIL"),
                        rs.getLong("ID_USUARIO")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar logins: " + e.getMessage());
        }
        return logins;
    }
}
