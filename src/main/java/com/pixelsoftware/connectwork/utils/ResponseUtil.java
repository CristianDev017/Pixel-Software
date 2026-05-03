package com.pixelsoftware.connectwork.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    private static final Gson GSON = new Gson();

    private ResponseUtil() {}

    public static void sendSuccess(HttpServletResponse response, Object data) throws IOException {
        send(response, HttpServletResponse.SC_OK, true, data, null);
    }

    public static void sendCreated(HttpServletResponse response, Object data) throws IOException {
        send(response, HttpServletResponse.SC_CREATED, true, data, null);
    }

    public static void sendError(HttpServletResponse response, int status, String message) throws IOException {
        send(response, status, false, null, message);
    }

    public static void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        sendError(response, HttpServletResponse.SC_BAD_REQUEST, message);
    }

    public static void sendUnauthorized(HttpServletResponse response) throws IOException {
        sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
    }

    public static void sendForbidden(HttpServletResponse response) throws IOException {
        sendError(response, HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
    }

    public static void sendNotFound(HttpServletResponse response, String message) throws IOException {
        sendError(response, HttpServletResponse.SC_NOT_FOUND, message);
    }

    private static void send(HttpServletResponse response, int status,
                             boolean success, Object data, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> body = new HashMap<>();
        body.put("success", success);
        if (data    != null) body.put("data", data);
        if (message != null) body.put("message", message);
        response.getWriter().write(GSON.toJson(body));
    }
}