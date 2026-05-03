package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Calificacion;

import java.sql.*;

public class CalificacionDAO {

    public boolean insertar(Calificacion c) {
        String sql = "INSERT INTO calificaciones (id_contrato, id_freelancer, id_cliente, estrellas, comentario) VALUES (?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdContrato());
            ps.setInt(2, c.getIdFreelancer());
            ps.setInt(3, c.getIdCliente());
            ps.setInt(4, c.getEstrellas());
            ps.setString(5, c.getComentario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en insertar calificacion: " + e);
        }
        return false;
    }

    public Calificacion findByContrato(int idContrato) {
        String sql = "SELECT * FROM calificaciones WHERE id_contrato = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idContrato);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findByContrato calificacion: " + e);
        }
        return null;
    }

    private Calificacion mapear(ResultSet rs) throws SQLException {
        Calificacion c = new Calificacion();
        c.setId(rs.getInt("id"));
        c.setIdContrato(rs.getInt("id_contrato"));
        c.setIdFreelancer(rs.getInt("id_freelancer"));
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setEstrellas(rs.getInt("estrellas"));
        c.setComentario(rs.getString("comentario"));
        c.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        return c;
    }
}