package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.ICitaDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.impl.CitaDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class CitaService {
    private ICitaDAO citaDAO = new CitaDAOImpl();

    public List<Cita> listarPorFecha(Date fecha) {
        try {
            return citaDAO.obtenerPorFecha(fecha);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cita> listarPorCliente(int idCliente) {
        try {
            return citaDAO.obtenerPorCliente(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cita obtenerPorId(int idCita) {
        try {
            return citaDAO.obtenerPorId(idCita);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int agendar(Cita cita, String idsMascotas) {
        try {
            return citaDAO.agendar(cita, idsMascotas);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean actualizar(Cita cita, String idsMascotas) {
        try {
            return citaDAO.actualizar(cita, idsMascotas);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelar(int idCita, String motivo) {
        try {
            return citaDAO.cancelar(idCita, motivo);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cita> listarTodas() {
        try {
            return citaDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cita> listarPorRangoFechas(Date fechaInicio, Date fechaFin) {
        try {
            return citaDAO.obtenerPorRangoFechas(fechaInicio, fechaFin);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cita> listarPorServicioYVeterinario(int idServicio, int idVeterinario, String estado) {
        try {
            return citaDAO.obtenerPorServicioYVeterinario(idServicio, idVeterinario, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizarEstado(int idCita, String estado) {
        try {
            return citaDAO.actualizarEstado(idCita, estado);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idCita) {
        try {
            return citaDAO.eliminar(idCita);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}