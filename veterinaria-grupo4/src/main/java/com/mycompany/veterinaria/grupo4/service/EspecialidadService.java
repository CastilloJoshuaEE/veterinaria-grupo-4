package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IEspecialidadDAO;
import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.impl.EspecialidadDAOImpl;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestion de especialidades veterinarias.
 * <p>
 * Proporciona la capa de servicios para las operaciones de negocio
 * relacionadas con las especialidades de los veterinarios, permitiendo
 * listar todas las especialidades y obtener una por su identificador.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
@Service
public class EspecialidadService {
    private IEspecialidadDAO especialidadDAO = new EspecialidadDAOImpl();

    /**
     * Lista todas las especialidades veterinarias registradas.
     *
     * @return lista de especialidades o null si hay error
     */
    public List<EspecialidadVeterinaria> listarTodas() {
        try {
            return especialidadDAO.obtenerTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una especialidad por su identificador.
     *
     * @param id identificador de la especialidad
     * @return objeto EspecialidadVeterinaria o null si no existe
     */
    public EspecialidadVeterinaria obtenerPorId(int id) {
        try {
            return especialidadDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}