package solaris.controladores;

import solaris.entidades.EnergiaSolar;
import solaris.infraestrutura.DatabaseConfig;
import solaris.repositorios.CalculoEnergiaSolarRepositorio;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/calculo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnergiaSolarResource {

    private final CalculoEnergiaSolarRepositorio calculoRepositorio;

    public EnergiaSolarResource() {
        try {
            this.calculoRepositorio = new CalculoEnergiaSolarRepositorio(DatabaseConfig.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao configurar o repositório: " + e.getMessage());
        }
    }

    @POST
    public Response cadastrarCalculo(EnergiaSolar calculo) {
        try {
            if (calculo.getEnergiaProduzida() == null) {
                double eficiencia = (calculo.getEficiencia() != null) ? calculo.getEficiencia() : 0.8;
                calculo.setEnergiaProduzida(calculo.getAreaDisponivel() * calculo.getNumeroPainel() * calculo.getHorasSolDia() * eficiencia);
            }

            if (calculo.getReducaoCO2() == null) {
                double fatorEmissao = (calculo.getFatorEmissao() != null) ? calculo.getFatorEmissao() : 0.5;
                calculo.setReducaoCO2(calculo.getEnergiaProduzida() * fatorEmissao);
            }

            calculoRepositorio.cadastrar(calculo);

            return Response.status(Response.Status.CREATED)
                    .entity(calculo)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao cadastrar cálculo: " + e.getMessage())
                    .build();
        }
    }



    @GET
    @Path("/{id}")
    public Response buscarCalculoPorId(@PathParam("id") int id) {
        try {
            Optional<EnergiaSolar> calculo = calculoRepositorio.buscarPorId(id);
            if (calculo.isPresent()) {
                return Response.ok(calculo.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cálculo não encontrado.").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar cálculo: " + e.getMessage()).build();
        }
    }

    @GET
    public Response listarCalculos() {
        try {
            return Response.ok(calculoRepositorio.listar()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar cálculos: " + e.getMessage()).build();
        }
    }
}
