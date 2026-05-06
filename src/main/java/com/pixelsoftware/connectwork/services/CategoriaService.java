package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.CategoriaDAO;
import com.pixelsoftware.connectwork.dao.HabilidadDAO;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.Categoria;
import com.pixelsoftware.connectwork.models.Habilidad;

import java.util.List;

public class CategoriaService {

    private final CategoriaDAO  categoriaDAO  = new CategoriaDAO();
    private final HabilidadDAO  habilidadDAO  = new HabilidadDAO();

    public List<Categoria> listarActivas() {
        return categoriaDAO.findActivas();
    }

    public List<Categoria> listarTodas() {
        return categoriaDAO.findAll();
    }

    public Categoria crear(String nombre) throws AppException {
        if (nombre == null || nombre.isBlank())
            throw new AppException(400, "El nombre es requerido");
        int id = categoriaDAO.insertar(nombre);
        if (id == -1) throw new AppException(500, "Error al crear categoría");
        return categoriaDAO.findById(id);
    }

    public void editar(int id, String nombre) throws AppException {
        if (nombre == null || nombre.isBlank())
            throw new AppException(400, "El nombre es requerido");
        categoriaDAO.actualizar(id, nombre);
    }

    public void cambiarEstado(int id, boolean activa) {
        categoriaDAO.cambiarEstado(id, activa);
    }

    public List<Habilidad> listarHabilidades(int idCategoria) {
        return habilidadDAO.findByCategoria(idCategoria);
    }

    public Habilidad crearHabilidad(int idCategoria, String nombre) throws AppException {
        if (nombre == null || nombre.isBlank())
            throw new AppException(400, "El nombre es requerido");
        int id = habilidadDAO.insertar(idCategoria, nombre);
        if (id == -1) throw new AppException(500, "Error al crear habilidad");
        return habilidadDAO.findById(id);
    }

    public void cambiarEstadoHabilidad(int id, boolean activa) {
        habilidadDAO.cambiarEstado(id, activa);
    }
}