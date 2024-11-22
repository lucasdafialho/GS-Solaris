package solaris.entidades;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Usuario extends _EntidadeBase {
    private String cpf;
    private String nome;
    private String email;
    private String senha;

    public Usuario(String cpf, String nome, String email, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(int id, String cpf, String nome, String email, String senha) {
        super(id);
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
