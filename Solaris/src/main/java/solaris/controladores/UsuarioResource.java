package solaris.controladores;

import solaris.entidades.Usuario;
import solaris.services.UsuarioService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private final UsuarioService usuarioService;

    public UsuarioResource() {
        this.usuarioService = new UsuarioService();
    }

    @GET
    public Response listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listar();
            return Response.ok(usuarios).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar usuários: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarUsuarioPorId(@PathParam("id") int id) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorId(id);
            return usuario.map(value -> Response.ok(value).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity("Usuário não encontrado: ID " + id).build());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar usuário: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/registro")
    public Response registrarUsuario(Usuario usuario) {
        try {
            usuarioService.registrar(usuario);
            return Response.status(Response.Status.CREATED)
                    .entity("Usuário registrado com sucesso.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao registrar usuário: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizarUsuario(@PathParam("id") int id, Usuario usuario) {
        try {
            usuarioService.atualizar(id, usuario);
            return Response.ok("Usuário atualizado com sucesso.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar usuário: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarUsuario(@PathParam("id") int id) {
        try {
            usuarioService.deletar(id);
            return Response.status(Response.Status.OK).entity("Usuário deletado com sucesso.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar usuário: " + e.getMessage()).build();
        }
    }


    @POST
    @Path("/login")
    public Response autenticarUsuario(Map<String, String> credenciais) {
        try {
            String email = credenciais.get("email");
            String senha = credenciais.get("senha");

            if (email == null || senha == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Email e senha são obrigatórios.").build();
            }

            boolean autenticado = usuarioService.autenticar(email, senha);

            if (autenticado) {
                return Response.ok("Login bem-sucedido.").build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Credenciais inválidas.").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao autenticar usuário: " + e.getMessage()).build();
        }
    }
}
