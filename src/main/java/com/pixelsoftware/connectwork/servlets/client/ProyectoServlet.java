package com.pixelsoftware.connectwork.servlets.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.Proyecto;
import com.pixelsoftware.connectwork.services.ProyectoService;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/client/proyectos/*")
public class ProyectoServlet extends HttpServlet {

    private final ProyectoService proyectoService = new ProyectoService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int idCliente = (int) req.getAttribute("userId");
        try {
            ResponseUtil.sendSuccess(resp, proyectoService.listarPorCliente(idCliente));
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int idCliente = (int) req.getAttribute("userId");
        try {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            Proyecto p = new Proyecto();
            p.setIdCliente(idCliente);
            p.setIdCategoria(body.get("idCategoria").getAsInt());
            p.setTitulo(body.get("titulo").getAsString());
            p.setDescripcion(body.get("descripcion").getAsString());
            p.setPresupuestoMax(body.get("presupuestoMax").getAsBigDecimal());
            p.setFechaLimite(LocalDate.parse(body.get("fechaLimite").getAsString()));

            List<Integer> habilidades = new ArrayList<>();
            JsonArray arr = body.getAsJsonArray("habilidades");
            for (int i = 0; i < arr.size(); i++) habilidades.add(arr.get(i).getAsInt());

            ResponseUtil.sendCreated(resp, proyectoService.publicar(p, habilidades));
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            int id = Integer.parseInt(path.substring(1));
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            Proyecto p = new Proyecto();
            p.setId(id);
            p.setTitulo(body.get("titulo").getAsString());
            p.setDescripcion(body.get("descripcion").getAsString());
            p.setIdCategoria(body.get("idCategoria").getAsInt());
            p.setPresupuestoMax(body.get("presupuestoMax").getAsBigDecimal());
            p.setFechaLimite(LocalDate.parse(body.get("fechaLimite").getAsString()));
            proyectoService.actualizar(p);
            ResponseUtil.sendSuccess(resp, "Proyecto actualizado");
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = req.getPathInfo();
            int id = Integer.parseInt(path.substring(1));
            proyectoService.cancelar(id);
            ResponseUtil.sendSuccess(resp, "Proyecto cancelado");
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }
}