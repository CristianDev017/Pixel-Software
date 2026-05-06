package com.pixelsoftware.connectwork.servlets.admin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.services.*;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/api/admin/*")
public class AdminServlet extends HttpServlet {

    private final CategoriaService categoriaService = new CategoriaService();
    private final ComisionService  comisionService  = new ComisionService();
    private final SolicitudService solicitudService = new SolicitudService();
    private final AuthService      authService      = new AuthService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        try {
            switch (path == null ? "/" : path) {
                case "/categorias"             -> ResponseUtil.sendSuccess(resp, categoriaService.listarTodas());
                case "/comision"               -> ResponseUtil.sendSuccess(resp, comisionService.getVigente());
                case "/comision/historial"     -> ResponseUtil.sendSuccess(resp, comisionService.historial());
                case "/solicitudes/habilidad"  -> ResponseUtil.sendSuccess(resp, solicitudService.pendientesHabilidad());
                case "/solicitudes/categoria"  -> ResponseUtil.sendSuccess(resp, solicitudService.pendientesCategoria());
                default                        -> ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
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
        try {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            switch (path == null ? "/" : path) {
                case "/categorias" -> {
                    String nombre = body.get("nombre").getAsString();
                    ResponseUtil.sendCreated(resp, categoriaService.crear(nombre));
                }
                case "/categorias/habilidad" -> {
                    int idCategoria = body.get("idCategoria").getAsInt();
                    String nombre   = body.get("nombre").getAsString();
                    ResponseUtil.sendCreated(resp, categoriaService.crearHabilidad(idCategoria, nombre));
                }
                case "/comision" -> {
                    BigDecimal porcentaje = body.get("porcentaje").getAsBigDecimal();
                    ResponseUtil.sendSuccess(resp, comisionService.cambiar(porcentaje));
                }
                case "/solicitudes/habilidad/resolver" -> {
                    int id          = body.get("id").getAsInt();
                    boolean aceptada = body.get("aceptada").getAsBoolean();
                    int idCategoria = body.get("idCategoria").getAsInt();
                    solicitudService.resolverHabilidad(id, aceptada, idCategoria);
                    ResponseUtil.sendSuccess(resp, "Solicitud resuelta");
                }
                case "/solicitudes/categoria/resolver" -> {
                    int id           = body.get("id").getAsInt();
                    boolean aceptada = body.get("aceptada").getAsBoolean();
                    solicitudService.resolverCategoria(id, aceptada);
                    ResponseUtil.sendSuccess(resp, "Solicitud resuelta");
                }
                default -> ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
            }
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        try {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            if (path != null && path.startsWith("/categorias/")) {
                int id      = Integer.parseInt(path.split("/")[2]);
                String nombre = body.get("nombre").getAsString();
                categoriaService.editar(id, nombre);
                ResponseUtil.sendSuccess(resp, "Categoría actualizada");
            } else {
                ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
            }
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        try {
            if (path != null && path.startsWith("/categorias/")) {
                int id = Integer.parseInt(path.split("/")[2]);
                categoriaService.cambiarEstado(id, false);
                ResponseUtil.sendSuccess(resp, "Categoría desactivada");
            } else {
                ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
            }
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }
}