package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.ProyectoDAO;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.Proyecto;

import java.math.BigDecimal;
import java.util.List;

public class ProyectoService {

    private final ProyectoDAO proyectoDAO = new ProyectoDAO();

    public Proyecto publicar(Proyecto p, List<Integer> habilidades) throws AppException {
        if (p.getTitulo() == null || p.getTitulo().isBlank())
            throw new AppException(400, "El título es requerido");
        if (p.getPresupuestoMax() == null || p.getPresupuestoMax().compareTo(BigDecimal.ZERO) <= 0)
            throw new AppException(400, "El presupuesto debe ser mayor a 0");
        if (habilidades == null || habilidades.isEmpty())
            throw new AppException(400, "Debe seleccionar al menos una habilidad");

        int id = proyectoDAO.insertar(p);
        if (id == -1) throw new AppException(500, "Error al publicar proyecto");

        for (int idHabilidad : habilidades) {
            proyectoDAO.agregarHabilidad(id, idHabilidad);
        }
        return proyectoDAO.findById(id);
    }

    public Proyecto findById(int id) throws AppException {
        Proyecto p = proyectoDAO.findById(id);
        if (p == null) throw new AppException(404, "Proyecto no encontrado");
        return p;
    }

    public List<Proyecto> listarAbiertos() {
        return proyectoDAO.findAbiertos();
    }

    public List<Proyecto> listarPorCliente(int idCliente) {
        return proyectoDAO.findByCliente(idCliente);
    }

    public void actualizar(Proyecto p) throws AppException {
        Proyecto existente = proyectoDAO.findById(p.getId());
        if (existente == null) throw new AppException(404, "Proyecto no encontrado");
        if (!"ABIERTO".equals(existente.getEstado()))
            throw new AppException(400, "Solo se pueden editar proyectos en estado ABIERTO");
        proyectoDAO.actualizar(p);
    }

    public void cancelar(int id) throws AppException {
        Proyecto p = proyectoDAO.findById(id);
        if (p == null) throw new AppException(404, "Proyecto no encontrado");
        if (!"ABIERTO".equals(p.getEstado()))
            throw new AppException(400, "Solo se pueden cancelar proyectos en estado ABIERTO");
        proyectoDAO.actualizarEstado(id, "CANCELADO");
    }
}