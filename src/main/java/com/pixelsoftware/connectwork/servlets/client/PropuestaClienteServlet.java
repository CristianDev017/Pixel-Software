package com.pixelsoftware.connectwork.servlets.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.services.CalificacionService;
import com.pixelsoftware.connectwork.services.ContratoService;
import com.pixelsoftware.connectwork.services.PropuestaService;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/client/propuestas/*")
public class PropuestaClienteServlet extends HttpServlet {

    private final PropuestaService    propuestaService    = new PropuestaService();
    private final ContratoService     contratoService     = new ContratoService();
    private final CalificacionService calificacionService = new CalificacionService();
    private final Gson gson = new Gson();

    // GET /api/client/propuestas/{idProyecto} → listar propuestas de un proyecto
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            int idProyecto = Integer.parseInt(path.substring(1));
            ResponseUtil.sendSuccess(resp, propuestaService.listarPorProyecto(idProyecto));
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }

    // POST /api/client/propuestas/aceptar     → aceptar propuesta
    // POST /api/client/propuestas/rechazar    → rechazar propuesta
    // POST /api/client/propuestas/entrega/aprobar  → aprobar entrega
    // POST /api/client/propuestas/entrega/rechazar → rechazar entrega
    // POST /api/client/propuestas/cancelar    → cancelar contrato
    // POST /api/client/propuestas/calificar   → calificar freelancer
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        int idCliente = (int) req.getAttribute("userId");
        try {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            switch (path == null ? "/" : path) {
                case "/aceptar" -> {
                    int idPropuesta = body.get("idPropuesta").getAsInt();
                    ResponseUtil.sendSuccess(resp, propuestaService.aceptar(idPropuesta, idCliente));
                }
                case "/rechazar" -> {
                    int idPropuesta = body.get("idPropuesta").getAsInt();
                    propuestaService.rechazar(idPropuesta);
                    ResponseUtil.sendSuccess(resp, "Propuesta rechazada");
                }
                case "/entrega/aprobar" -> {
                    int idEntrega  = body.get("idEntrega").getAsInt();
                    int idContrato = body.get("idContrato").getAsInt();
                    contratoService.aprobarEntrega(idEntrega, idContrato, idCliente);
                    ResponseUtil.sendSuccess(resp, "Entrega aprobada");
                }
                case "/entrega/rechazar" -> {
                    int idEntrega  = body.get("idEntrega").getAsInt();
                    int idContrato = body.get("idContrato").getAsInt();
                    String motivo  = body.get("motivo").getAsString();
                    contratoService.rechazarEntrega(idEntrega, idContrato, idCliente, motivo);
                    ResponseUtil.sendSuccess(resp, "Entrega rechazada");
                }
                case "/cancelar" -> {
                    int idContrato = body.get("idContrato").getAsInt();
                    String motivo  = body.get("motivo").getAsString();
                    contratoService.cancelarContrato(idContrato, idCliente, motivo);
                    ResponseUtil.sendSuccess(resp, "Contrato cancelado");
                }
                case "/calificar" -> {
                    int idContrato = body.get("idContrato").getAsInt();
                    int estrellas  = body.get("estrellas").getAsInt();
                    String comentario = body.has("comentario") ? body.get("comentario").getAsString() : null;
                    calificacionService.calificar(idContrato, idCliente, estrellas, comentario);
                    ResponseUtil.sendSuccess(resp, "Calificación registrada");
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