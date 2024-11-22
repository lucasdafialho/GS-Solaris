package solaris.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private Long idLogin;
    private String senha;
    private String email;
    private Long idUsuario;
}