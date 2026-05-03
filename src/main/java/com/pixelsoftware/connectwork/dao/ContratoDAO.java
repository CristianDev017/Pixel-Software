package com.pixelsoftware.connectwork.dao;

import com.pixelsoftware.connectwork.config.DatabaseConnection;
import com.pixelsoftware.connectwork.models.Contrato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO {

    public int insertar(Contrato c) {
        String sql = "INSERT INTO contratos (id_propuesta, id_comision, monto_contrato, monto_bloqueado) VALUES (?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getIdPropuesta());
            ps.setInt(2, c.getIdComision());
            ps.setBigDecimal(3, c.getMontoContrato());
            ps.setBigDecimal(4, c.getMontoBloqueado());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error en insertar contrato: " + e);
        }
        return -1;
    }

    public Contrato findById(int id) {
        String sql = "SELECT * FROM contratos WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findById contrato: " + e);
        }
        return null;
    }

    public Contrato findByPropuesta(int idPropuesta) {
        String sql = "SELECT * FROM contratos WHERE id_propuesta = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropuesta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error en findByPropuesta: " + e);
        }
        return null;
    }

    public boolean cancelar(int id, String motivo) {
        String sql = "UPDATE contratos SET motivo_cancel = ?, fecha_fin = NOW() WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, motivo);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en cancelar contrato: " + e);
        }
        return false;
    }

    public boolean completar(int id) {
        String sql = "UPDATE contratos SET fecha_fin = NOW() WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en completar contrato: " + e);
        }
        return false;
    }

    private Contrato mapear(ResultSet rs) throws SQLException {
        Contrato c = new Contrato();
        c.setId(rs.getInt("id"));
        c.setIdPropuesta(rs.getInt("id_propuesta"));
        c.setIdComision(rs.getInt("id_comision"));
        c.setMontoContrato(rs.getBigDecimal("monto_contrato"));
        c.setMontoBloqueado(rs.getBigDecimal("monto_bloqueado"));
        c.setMotivoCancel(rs.getString("motivo_cancel"));
        c.setFechaInicio(rs.getTimestamp("fecha_inicio").toLocalDateTime());
        Timestamp fin = rs.getTimestamp("fecha_fin");
        if (fin != null) c.setFechaFin(fin.toLocalDateTime());
        return c;
    }
}