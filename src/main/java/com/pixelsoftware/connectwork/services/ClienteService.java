package com.pixelsoftware.connectwork.services;

import com.pixelsoftware.connectwork.dao.ClienteDAO;
import com.pixelsoftware.connectwork.dao.RecargaDAO;
import com.pixelsoftware.connectwork.dao.UsuarioDAO;
import com.pixelsoftware.connectwork.exceptions.AppException;
import com.pixelsoftware.connectwork.models.Cliente;
import com.pixelsoftware.connectwork.models.Recarga;

import java.math.BigDecimal;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RecargaDAO recargaDAO = new RecargaDAO();

    public Cliente getPerfil(int idUsuario) throws AppException {
        Cliente c = clienteDAO.findById(idUsuario);
        if (c == null) throw new AppException(404, "Cliente no encontrado");
        return c;
    }

    public void completarPerfil(int idUsuario, String descripcion, String sector, String sitioWeb) throws AppException {
        if (descripcion == null || descripcion.isBlank())
            throw new AppException(400, "La descripción es requerida");
        if (sector == null || sector.isBlank())
            throw new AppException(400, "El sector es requerido");
        clienteDAO.completarPerfil(idUsuario, descripcion, sector, sitioWeb);
        usuarioDAO.actualizarPerfilCompleto(idUsuario);
    }

    public void recargarSaldo(int idUsuario, BigDecimal monto) throws AppException {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new AppException(400, "El monto debe ser mayor a 0");
        Cliente c = clienteDAO.findById(idUsuario);
        if (c == null) throw new AppException(404, "Cliente no encontrado");
        BigDecimal nuevoSaldo = c.getSaldo().add(monto);
        clienteDAO.actualizarSaldo(idUsuario, nuevoSaldo);
        recargaDAO.insertar(idUsuario, monto);
    }

    public List<Recarga> historialRecargas(int idUsuario) {
        return recargaDAO.findByCliente(idUsuario);
    }
}