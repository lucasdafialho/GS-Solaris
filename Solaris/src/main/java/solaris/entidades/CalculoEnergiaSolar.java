package solaris.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoEnergiaSolar {
    private Double areaDisponivel;
    private Integer numeroPainel;
    private Double horasSolDia;
    private Double eficiencia;
    private Double energiaProduzida;
    private Double reducaoCO2;

    public void calcularEnergiaProduzida() {
        if (eficiencia == null) {
            eficiencia = 0.8;
        }
        energiaProduzida = areaDisponivel * numeroPainel * horasSolDia * eficiencia;
    }

    public void calcularReducaoCO2(Double fatorEmissao) {
        if (energiaProduzida == null) {
            throw new IllegalStateException("Energia produzida precisa ser calculada antes de calcular a redução de CO₂.");
        }
        if (fatorEmissao == null) {
            fatorEmissao = 0.5;
        }
        reducaoCO2 = energiaProduzida * fatorEmissao;
    }
}
