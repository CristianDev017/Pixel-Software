package com.pixelsoftware.connectwork.servlets.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.services.ClienteService;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/api/client/*")
public class ClienteServlet extends HttpServlet {

    private final ClienteService clienteService = new ClienteService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        int idUsuario = (int) req.getAttribute("userId");
        try {
            switch (path == null ? "/" : path) {
                case "/perfil"   -> ResponseUtil.sendSuccess(resp, clienteService.getPerfil(idUsuario));
                case "/recargas" -> ResponseUtil.sendSuccess(resp, clienteService.historialRecargas(idUsuario));
                default          -> ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
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
        int idUsuario = (int) req.getAttribute("userId");
        try {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            switch (path == null ? "/" : path) {
                case "/perfil" -> {
                    String descripcion = body.get("descripcion").getAsString();
                    String sector      = body.get("sector").getAsString();
                    String sitioWeb    = body.has("sitioWeb") ? body.get("sitioWeb").getAsString() : null;
                    clienteService.completarPerfil(idUsuario, descripcion, sector, sitioWeb);
                    ResponseUtil.sendSuccess(resp, "Perfil completado");
                }
                case "/recargar" -> {
                    BigDecimal monto = body.get("monto").getAsBigDecimal();
                    clienteService.recargarSaldo(idUsuario, monto);
                    ResponseUtil.sendSuccess(resp, "Recarga exitosa");
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