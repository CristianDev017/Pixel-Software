package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.*;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.*;

import java.util.List;

public class SolicitudService {

    private final SolicitudHabilidadDAO  habilidadDAO  = new SolicitudHabilidadDAO();
    private final SolicitudCategoriaDAO  categoriaDAO  = new SolicitudCategoriaDAO();
    private final HabilidadDAO           habilidadCatDAO = new HabilidadDAO();
    private final CategoriaDAO           catDAO          = new CategoriaDAO();

    public void solicitarHabilidad(int idFreelancer, String nombre, String descripcion) throws AppException {
        if (nombre == null || nombre.isBlank())
            throw new AppException(400, "El nombre es requerido");
        habilidadDAO.insertar(idFreelancer, nombre, descripcion);
    }

    public void solicitarCategoria(int idCliente, String nombre, String descripcion) throws AppException {
        if (nombre == null || nombre.isBlank())
            throw new AppException(400, "El nombre es requerido");
        categoriaDAO.insertar(idCliente, nombre, descripcion);
    }

    public List<SolicitudHabilidad> pendientesHabilidad() {
        return habilidadDAO.findPendientes();
    }

    public List<SolicitudCategoria> pendientesCategoria() {
        return categoriaDAO.findPendientes();
    }

    public void resolverHabilidad(int id, boolean aceptada, int idCategoria) throws AppException {
        String estado = aceptada ? "ACEPTADA" : "RECHAZADA";
        habilidadDAO.actualizarEstado(id, estado);
        if (aceptada) {
            SolicitudHabilidad s = habilidadDAO.findPendientes().stream()
                    .filter(x -> x.getId() == id).findFirst().orElse(null);
            if (s != null) habilidadCatDAO.insertar(idCategoria, s.getNombre());
        }
    }

    public void resolverCategoria(int id, boolean aceptada) throws AppException {
        String estado = aceptada ? "ACEPTADA" : "RECHAZADA";
        categoriaDAO.actualizarEstado(id, estado);
        if (aceptada) {
            SolicitudCategoria s = categoriaDAO.findPendientes().stream()
                    .filter(x -> x.getId() == id).findFirst().orElse(null);
            if (s != null) catDAO.insertar(s.getNombre());
        }
    }
}