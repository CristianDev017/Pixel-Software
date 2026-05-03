package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.SolicitudCategoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudCategoriaDAO {

    public boolean insertar(int idCliente, String nombre, String descripcion) {
        String sql = "INSERT INTO solicitudes_categoria (id_cliente, nombre, descripcion) VALUES (?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en insertar solicitud categoria: " + e);
        }
        return false;
    }

    public List<SolicitudCategoria> findPendientes() {
        List<SolicitudCategoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitudes_categoria WHERE estado = 'PENDIENTE'";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findPendientes solicitud categoria: " + e);
        }
        return lista;
    }

    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE solicitudes_categoria SET estado = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarEstado solicitud categoria: " + e);
        }
        return false;
    }

    private SolicitudCategoria mapear(ResultSet rs) throws SQLException {
        SolicitudCategoria s = new SolicitudCategoria();
        s.setId(rs.getInt("id"));
        s.setIdCliente(rs.getInt("id_cliente"));
        s.setNombre(rs.getString("nombre"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setEstado(rs.getString("estado"));
        s.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        return s;
    }
}