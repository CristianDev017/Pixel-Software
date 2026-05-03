package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public int insertar(Entrega e) {
        String sql = "INSERT INTO entregas (id_contrato, descripcion, archivos_url) VALUES (?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getIdContrato());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getArchivosUrl());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println("Error en insertar entrega: " + ex);
        }
        return -1;
    }

    public List<Entrega> findByContrato(int idContrato) {
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT * FROM entregas WHERE id_contrato = ? ORDER BY fecha_entrega DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idContrato);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findByContrato entrega: " + e);
        }
        return lista;
    }

    public boolean aprobar(int id) {
        String sql = "UPDATE entregas SET estado = 'APROBADA' WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en aprobar entrega: " + e);
        }
        return false;
    }

    public boolean rechazar(int id, String motivo) {
        String sql = "UPDATE entregas SET estado = 'RECHAZADA', motivo_rechazo = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, motivo);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en rechazar entrega: " + e);
        }
        return false;
    }

    private Entrega mapear(ResultSet rs) throws SQLException {
        Entrega e = new Entrega();
        e.setId(rs.getInt("id"));
        e.setIdContrato(rs.getInt("id_contrato"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setArchivosUrl(rs.getString("archivos_url"));
        e.setEstado(rs.getString("estado"));
        e.setMotivoRechazo(rs.getString("motivo_rechazo"));
        e.setFechaEntrega(rs.getTimestamp("fecha_entrega").toLocalDateTime());
        return e;
    }
}