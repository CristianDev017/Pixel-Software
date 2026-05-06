package com.pixelsoftware.connectwork.servlets.shared;

import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.services.CategoriaService;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/public/*")
public class CategoriaPublicaServlet extends HttpServlet {

    private final CategoriaService categoriaService = new CategoriaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        try {
            switch (path == null ? "/" : path) {
                case "/categorias" -> ResponseUtil.sendSuccess(resp, categoriaService.listarActivas());
                default -> {
                    if (path != null && path.startsWith("/categorias/")) {
                        int idCategoria = Integer.parseInt(path.split("/")[2]);
                        ResponseUtil.sendSuccess(resp, categoriaService.listarHabilidades(idCategoria));
                    } else {
                        ResponseUtil.sendNotFound(resp, "Endpoint no encontrado");
                    }
                }
            }
        } catch (AppException e) {
            ResponseUtil.sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendError(resp, 500, "Error interno del servidor");
        }
    }
}