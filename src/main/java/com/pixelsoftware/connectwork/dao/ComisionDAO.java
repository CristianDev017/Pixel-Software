package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Comision;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComisionDAO {

    public Comision findVigente() {
        String sql = "SELECT * FROM comisiones WHERE fecha_fin IS NULL ORDER BY fecha_inicio DESC LIMIT 1";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findVigente: " + e);
        }
        return null;
    }

    public List<Comision> findAll() {
        List<Comision> lista = new ArrayList<>();
        String sql = "SELECT * FROM comisiones ORDER BY fecha_inicio DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error en findAll comisiones: " + e);
        }
        return lista;
    }

    public boolean cerrarVigente() {
        String sql = "UPDATE comisiones SET fecha_fin = NOW() WHERE fecha_fin IS NULL";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en cerrarVigente: " + e);
        }
        return false;
    }

    public int insertar(BigDecimal porcentaje) {
        String sql = "INSERT INTO comisiones (porcentaje) VALUES (?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBigDecimal(1, porcentaje);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en insertar comision: " + e);
        }
        return -1;
    }

    public Comision findById(int id) {
        String sql = "SELECT * FROM comisiones WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById comision: " + e);
        }
        return null;
    }

    private Comision mapear(ResultSet rs) throws SQLException {
        Comision c = new Comision();
        c.setId(rs.getInt("id"));
        c.setPorcentaje(rs.getBigDecimal("porcentaje"));
        c.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
        Timestamp fin = rs.getTimestamp("fecha_fin");
        if (fin != null) c.setFechaFin(fin.toLocalDateTime());
        return c;
    }
}