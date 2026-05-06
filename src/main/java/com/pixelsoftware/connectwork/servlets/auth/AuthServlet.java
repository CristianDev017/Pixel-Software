package com.pixelsoftware.connectwork.servlets.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.services.AuthService;
import com.pixelsoftware.connectwork.utils.ResponseUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        try {
            JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);
            switch (path == null ? "/" : path) {
                case "/login" -> {
                    String username = body.get("username").getAsString();
                    String password = body.get("password").getAsString();
                    ResponseUtil.sendSuccess(resp, authService.login(username, password));
                }
                case "/register" -> {
                    String nombre    = body.get("nombre").getAsString();
                    String username  = body.get("username").getAsString();
                    String password  = body.get("password").getAsString();
                    String email     = body.get("email").getAsString();
                    String telefono  = body.get("telefono").getAsString();
                    String direccion = body.get("direccion").getAsString();
                    String cui       = body.get("cui").getAsString();
                    String birthDate = body.get("birthDate").getAsString();
                    String rol       = body.get("rol").getAsString();
                    ResponseUtil.sendCreated(resp, authService.register(
                            nombre, username, password, email,
                            telefono, direccion, cui, birthDate, rol));
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