package com.mycompany.veterinaria.grupo4.model.dao;

import com.mycompany.veterinaria.grupo4.model.entity.FichaMedica;
import java.sql.SQLException;

public interface IFichaMedicaDAO {
    FichaMedica obtenerPorMascota(int idMascota) throws SQLException;
    boolean actualizar(int idMascota, String alergias, String enfermedadesCronicas, String observaciones) throws SQLException;
}