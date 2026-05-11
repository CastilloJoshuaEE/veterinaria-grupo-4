package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.config.RecordatorioConfig;
import com.mycompany.veterinaria.grupo4.model.dao.IRecordatorioDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.model.impl.RecordatorioDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class RecordatorioService {
    private IRecordatorioDAO recordatorioDAO = new RecordatorioDAOImpl();

    public List<Recordatorio> obtenerPendientes(int idUsuario) {
        try {
            return recordatorioDAO.obtenerPendientes(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean marcarComoLeido(int idRecordatorio) {
        try {
            return recordatorioDAO.marcarComoLeido(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void generarRecordatorios(int idUsuario) {
        try {
            recordatorioDAO.generarRecordatorios(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recordatorio> listarTodos(Date fechaInicio, Date fechaFin) {
        try {
            return recordatorioDAO.obtenerTodos(fechaInicio, fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void incrementarContador(int idRecordatorio) {
        try {
            recordatorioDAO.incrementarContador(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int obtenerContador(int idRecordatorio) {
        try {
            return recordatorioDAO.obtenerContador(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int registrar(Recordatorio recordatorio, String anticipacion) {
        try {
            return recordatorioDAO.registrar(recordatorio, anticipacion);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean actualizar(Recordatorio recordatorio) {
        try {
            return recordatorioDAO.actualizar(recordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idRecordatorio) {
        try {
            return recordatorioDAO.eliminar(idRecordatorio);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<RecordatorioConfig> obtenerTodasConfiguraciones() {
        try {
            return recordatorioDAO.obtenerTodasConfiguraciones();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizarConfiguracion(RecordatorioConfig config) {
        try {
            return recordatorioDAO.actualizarConfiguracion(config);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int crearConfiguracion(RecordatorioConfig config) {
        try {
            return recordatorioDAO.crearConfiguracion(config);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}