package com.pixelsoftware.connectwork.servlets.freelancer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.services.FreelancerService;
import com.pixelsoftware.connectwork.services.ProyectoService;
import com.pixelsoftware.connectwork.services.PropuestaService;
import com.pixelsoftware.connectwork.services.ContratoService;
import com.pixelsoftware.connectwork.services.SolicitudService;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/freelancer/*")
public class FreelancerServlet extends HttpServlet {

    private final FreelancerService freelancerService = new FreelancerService();
    private final ProyectoService   proyectoService   = new ProyectoService();
    private final PropuestaService  propuestaService  = new PropuestaService();
    private final ContratoService   contratoService   = new ContratoService();
    private final SolicitudService  solicitudService  = new SolicitudService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        int idFreelancer = (int) req.getAttribute("userId");
        try {
            switch (path == null ? "/" : path) {
                case "/perfil"      -> ResponseUtil.sendSuccess(resp, freelancerService.getPerfil(idFreelancer));
                case "/proyectos"   -> ResponseUtil.sendSuccess(resp, proyectoService.listarAbiertos());
                case "/propuestas"  -> ResponseUtil.sendSuccess(resp, propuestaService.listarPorFreelancer(idFreelancer));
                default             -> ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
            }
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        int idFreelancer = (int) req.getAttribute("userId");
        try {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            switch (path == null ? "/" : path) {
                case "/perfil" -> {
                    String biografia   = body.get("biografia").getAsString();
                    String experiencia = body.get("experiencia").getAsString();
                    BigDecimal tarifa  = body.get("tarifaHora").getAsBigDecimal();
                    List<Integer> habilidades = new ArrayList<>();
                    JsonArray arr = body.getAsJsonArray("habilidades");
                    for (int i = 0; i < arr.size(); i++) habilidades.add(arr.get(i).getAsInt());
                    freelancerService.completarPerfil(idFreelancer, biografia, experiencia, tarifa, habilidades);
                    ResponseUtil.sendSuccess(resp, "Perfil completado");
                }
                case "/propuestas" -> {
                    int idProyecto = body.get("idProyecto").getAsInt();
                    BigDecimal monto = body.get("monto").getAsBigDecimal();
                    int plazoDias  = body.get("plazoDias").getAsInt();
                    String carta   = body.get("carta").getAsString();
                    ResponseUtil.sendCreated(resp,
                            propuestaService.enviar(idFreelancer, idProyecto, monto, plazoDias, carta));
                }
                case "/propuestas/retirar" -> {
                    int idPropuesta = body.get("idPropuesta").getAsInt();
                    propuestaService.retirar(idPropuesta, idFreelancer);
                    ResponseUtil.sendSuccess(resp, "Propuesta retirada");
                }
                case "/entregas" -> {
                    int idContrato    = body.get("idContrato").getAsInt();
                    String descripcion = body.get("descripcion").getAsString();
                    String archivos   = body.get("archivosUrl").getAsString();
                    ResponseUtil.sendCreated(resp,
                            contratoService.subirEntrega(idContrato, idFreelancer, descripcion, archivos));
                }
                case "/solicitudes/habilidad" -> {
                    String nombre      = body.get("nombre").getAsString();
                    String descripcion = body.get("descripcion").getAsString();
                    solicitudService.solicitarHabilidad(idFreelancer, nombre, descripcion);
                    ResponseUtil.sendSuccess(resp, "Solicitud enviada");
                }
                default -> ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
            }
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }
}