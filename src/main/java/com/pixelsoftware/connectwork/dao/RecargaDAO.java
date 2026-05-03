package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Recarga;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecargaDAO {

    public boolean insertar(int idCliente, BigDecimal monto) {
        String sql = "INSERT INTO recargas (id_cliente, monto) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setBigDecimal(2, monto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en insertar recarga: " + e);
        }
        return false;
    }

    public List<Recarga> findByCliente(int idCliente) {
        List<Recarga> lista = new ArrayList<>();
        String sql = "SELECT * FROM recargas WHERE id_cliente = ? ORDER BY fecha DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findByCliente recarga: " + e);
        }
        return lista;
    }

    private Recarga mapear(ResultSet rs) throws SQLException {
        Recarga r = new Recarga();
        r.setId(rs.getInt("id"));
        r.setIdCliente(rs.getInt("id_cliente"));
        r.setMonto(rs.getBigDecimal("monto"));
        r.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        return r;
    }
}