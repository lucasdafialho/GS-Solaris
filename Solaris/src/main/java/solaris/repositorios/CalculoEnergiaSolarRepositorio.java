package solaris.repositorios;

import solaris.entidades.EnergiaSolar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CalculoEnergiaSolarRepositorio implements _RepositorioBase<EnergiaSolar> {
    private final Connection connection;

    public CalculoEnergiaSolarRepositorio(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void cadastrar(EnergiaSolar energiaSolar) {
        String sql = "INSERT INTO T_ENERGIA_SOLAR_SOLARIS (AREA_DISPONIVEL, NUMERO_PAINEL, HORAS_SOL_DIA, EFICIENCIA, ENERGIA_PRODUZIDA, FATOR_EMISSAO, REDUCAO_CO2) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, energiaSolar.getAreaDisponivel());
            stmt.setInt(2, energiaSolar.getNumeroPainel());
            stmt.setDouble(3, energiaSolar.getHorasSolDia());
            double eficiencia = (energiaSolar.getEficiencia() != null) ? energiaSolar.getEficiencia() : 0.8;
            stmt.setDouble(4, eficiencia);
            stmt.setDouble(5, energiaSolar.getEnergiaProduzida());
            stmt.setDouble(6, energiaSolar.getFatorEmissao());
            stmt.setDouble(7, energiaSolar.getReducaoCO2());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar cálculo de energia solar: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(EnergiaSolar energiaSolar, int id) {
        String sql = "UPDATE T_ENERGIA_SOLAR_SOLARIS SET AREA_DISPONIVEL = ?, NUMERO_PAINEL = ?, HORAS_SOL_DIA = ?, EFICIENCIA = ?, ENERGIA_PRODUZIDA = ?, FATOR_EMISSAO = ?, REDUCAO_CO2 = ? WHERE ID_CALCULO = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, energiaSolar.getAreaDisponivel());
            stmt.setInt(2, energiaSolar.getNumeroPainel());
            stmt.setDouble(3, energiaSolar.getHorasSolDia());
            double eficiencia = (energiaSolar.getEficiencia() != null) ? energiaSolar.getEficiencia() : 0.8;
            stmt.setDouble(4, eficiencia);
            stmt.setDouble(5, energiaSolar.getEnergiaProduzida());
            stmt.setDouble(6, energiaSolar.getFatorEmissao());
            stmt.setDouble(7, energiaSolar.getReducaoCO2());
            stmt.setInt(8, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cálculo de energia solar: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id) {
        String sql = "DELETE FROM T_ENERGIA_SOLAR_SOLARIS WHERE ID_CALCULO = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover cálculo de energia solar: " + e.getMessage());
        }
    }

    @Override
    public Optional<EnergiaSolar> buscarPorId(int id) {
        String sql = "SELECT * FROM T_ENERGIA_SOLAR_SOLARIS WHERE ID_CALCULO = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EnergiaSolar energiaSolar = new EnergiaSolar(
                            rs.getLong("ID_CALCULO"),
                            rs.getDouble("AREA_DISPONIVEL"),
                            rs.getInt("NUMERO_PAINEL"),
                            rs.getDouble("HORAS_SOL_DIA"),
                            rs.getDouble("EFICIENCIA"),
                            rs.getDouble("ENERGIA_PRODUZIDA"),
                            rs.getDouble("FATOR_EMISSAO"),
                            rs.getDouble("REDUCAO_CO2")
                    );
                    return Optional.of(energiaSolar);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cálculo de energia solar por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<EnergiaSolar> listar() {
        String sql = "SELECT * FROM T_ENERGIA_SOLAR_SOLARIS";
        List<EnergiaSolar> calculos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                calculos.add(new EnergiaSolar(
                        rs.getLong("ID_CALCULO"),
                        rs.getDouble("AREA_DISPONIVEL"),
                        rs.getInt("NUMERO_PAINEL"),
                        rs.getDouble("HORAS_SOL_DIA"),
                        rs.getDouble("EFICIENCIA"),
                        rs.getDouble("ENERGIA_PRODUZIDA"),
                        rs.getDouble("FATOR_EMISSAO"),
                        rs.getDouble("REDUCAO_CO2")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cálculos de energia solar: " + e.getMessage());
        }
        return calculos;
    }
}
