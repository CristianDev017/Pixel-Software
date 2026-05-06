package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.ComisionDAO;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.Comision;

import java.math.BigDecimal;
import java.util.List;

public class ComisionService {

    private final ComisionDAO comisionDAO = new ComisionDAO();

    public Comision getVigente() throws AppException {
        Comision c = comisionDAO.findVigente();
        if (c == null) throw new AppException(404, "No hay comisión vigente");
        return c;
    }

    public List<Comision> historial() {
        return comisionDAO.findAll();
    }

    public Comision cambiar(BigDecimal nuevoPorcentaje) throws AppException {
        if (nuevoPorcentaje == null || nuevoPorcentaje.compareTo(BigDecimal.ZERO) <= 0
                || nuevoPorcentaje.compareTo(BigDecimal.valueOf(100)) >= 0)
            throw new AppException(400, "El porcentaje debe estar entre 0 y 100");
        comisionDAO.cerrarVigente();
        int id = comisionDAO.insertar(nuevoPorcentaje);
        if (id == -1) throw new AppException(500, "Error al cambiar comisión");
        return comisionDAO.findVigente();
    }
}