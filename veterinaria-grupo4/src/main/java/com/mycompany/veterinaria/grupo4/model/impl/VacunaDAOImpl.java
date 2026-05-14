package com.mycompany.veterinaria.grupo4.model.impl;

import com.mycompany.veterinaria.grupo4.model.dao.IVacunaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.VacunaAplicada;
import com.mycompany.veterinaria.grupo4.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del DAO para la gestion de vacunas aplicadas.
 * <p>
 * Esta clase implementa la interfaz IVacunaDAO y proporciona la logica
 * de acceso a datos para la entidad VacunaAplicada utilizando procedimientos
 * almacenados de SQL Server. Permite obtener vacunas por mascota,
 * registrar nuevas vacunas, actualizar existentes, verificar duplicados
 * y obtener vacunas proximas a vencer.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class VacunaDAOImpl implements IVacunaDAO {

    /**
     * Obtiene todas las vacunas aplicadas a una mascota.
     *
     * @param idMascota identificador de la mascota
     * @return lista de vacunas aplicadas
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<VacunaAplicada> obtenerPorMascota(int idMascota) throws SQLException {
        List<VacunaAplicada> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VACUNAS_APLICADAS_POR_MASCOTA(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                VacunaAplicada v = new VacunaAplicada();
                v.setIdVacuna(rs.getInt("ID_VACUNA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setDescripcion(rs.getString("DESCRIPCION"));
                v.setFechaAplicacion(rs.getDate("FECHA_APLICACION"));
                v.setFechaProxima(rs.getDate("FECHA_PROXIMA"));
                v.setIdMascota(idMascota);
                lista.add(v);
            }
        }
        return lista;
    }

    /**
     * Registra una nueva vacuna aplicada.
     *
     * @param vacuna objeto VacunaAplicada a registrar
     * @return ID de la vacuna registrada
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public int registrar(VacunaAplicada vacuna) throws SQLException {
        String sql = "{call SP_REGISTRAR_VACUNA_APLICADA(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, vacuna.getIdMascota());
            stmt.setString(2, vacuna.getNombre());
            stmt.setString(3, vacuna.getDescripcion());
            stmt.setInt(4, vacuna.getPeriodoMeses());
            stmt.setDate(5, new java.sql.Date(vacuna.getFechaAplicacion().getTime()));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID_VACUNA");
            }
            return -1;
        }
    }

    /**
     * Actualiza los datos de una vacuna aplicada.
     *
     * @param vacuna objeto VacunaAplicada con datos actualizados
     * @return true si la actualizacion fue exitosa
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public boolean actualizar(VacunaAplicada vacuna) throws SQLException {
        String sql = "{call SP_ACTUALIZAR_VACUNA_APLICADA(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, vacuna.getIdVacuna());
            stmt.setString(2, vacuna.getNombre());
            stmt.setString(3, vacuna.getDescripcion());
            stmt.setInt(4, vacuna.getPeriodoMeses());
            stmt.setDate(5, new java.sql.Date(vacuna.getFechaAplicacion().getTime()));
            stmt.setDate(6, new java.sql.Date(vacuna.getFechaProxima().getTime()));
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt("FILAS_AFECTADAS") > 0;
        }
    }

    /**
     * Verifica si ya existe una vacuna con el mismo nombre para una mascota.
     *
     * @param idMascota identificador de la mascota
     * @param nombreVacuna nombre de la vacuna
     * @return objeto VacunaAplicada si existe, null en caso contrario
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public VacunaAplicada verificarExistente(int idMascota, String nombreVacuna) throws SQLException {
        String sql = "{call SP_VERIFICAR_VACUNA_APLICADA_POR_MASCOTA(?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, idMascota);
            stmt.setString(2, nombreVacuna);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                VacunaAplicada v = new VacunaAplicada();
                v.setIdVacuna(rs.getInt("ID_VACUNA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setFechaAplicacion(rs.getDate("FECHA_APLICACION"));
                v.setFechaProxima(rs.getDate("FECHA_PROXIMA"));
                return v;
            }
            return null;
        }
    }

    /**
     * Obtiene las vacunas proximas a vencer en los proximos dias.
     *
     * @param diasAnticipacion numero de dias de anticipacion
     * @return lista de vacunas proximas a vencer
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public List<VacunaAplicada> obtenerProximasAVencer(int diasAnticipacion) throws SQLException {
        List<VacunaAplicada> lista = new ArrayList<>();
        String sql = "{call SP_OBTENER_VACUNAS_PROXIMAS_A_VENCER(?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, diasAnticipacion);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                VacunaAplicada v = new VacunaAplicada();
                v.setIdVacuna(rs.getInt("ID_VACUNA"));
                v.setNombre(rs.getString("NOMBRE"));
                v.setFechaAplicacion(rs.getDate("FECHA_APLICACION"));
                v.setFechaProxima(rs.getDate("FECHA_PROXIMA"));
                v.setIdMascota(rs.getInt("ID_MASCOTA"));
                lista.add(v);
            }
        }
        return lista;
    }
}