package solaris.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergiaSolar {
    private Long idCalculo;
    private Double areaDisponivel;
    private Integer numeroPainel;
    private Double horasSolDia;
    private Double eficiencia;
    private Double energiaProduzida;
    private Double fatorEmissao;
    private Double reducaoCO2;
}
