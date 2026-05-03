package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.SolicitudHabilidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudHabilidadDAO {

    public boolean insertar(int idFreelancer, String nombre, String descripcion) {
        String sql = "INSERT INTO solicitudes_habilidad (id_freelancer, nombre, descripcion) VALUES (?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFreelancer);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en insertar solicitud habilidad: " + e);
        }
        return false;
    }

    public List<SolicitudHabilidad> findPendientes() {
        List<SolicitudHabilidad> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitudes_habilidad WHERE estado = 'PENDIENTE'";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findPendientes solicitud habilidad: " + e);
        }
        return lista;
    }

    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE solicitudes_habilidad SET estado = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en actualizarEstado solicitud habilidad: " + e);
        }
        return false;
    }

    private SolicitudHabilidad mapear(ResultSet rs) throws SQLException {
        SolicitudHabilidad s = new SolicitudHabilidad();
        s.setId(rs.getInt("id"));
        s.setIdFreelancer(rs.getInt("id_freelancer"));
        s.setNombre(rs.getString("nombre"));
        s.setDescripcion(rs.getString("descripcion"));
        s.setEstado(rs.getString("estado"));
        s.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        return s;
    }
}