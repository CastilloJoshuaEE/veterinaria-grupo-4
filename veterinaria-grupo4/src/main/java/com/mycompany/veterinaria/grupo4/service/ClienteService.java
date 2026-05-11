package com.mycompany.veterinaria.grupo4.service;

import com.mycompany.veterinaria.grupo4.model.dao.IClienteDAO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.impl.ClienteDAOImpl;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ClienteService {
    private IClienteDAO clienteDAO = new ClienteDAOImpl();
    
    public List<Cliente> listarTodos() {
        try {
            return clienteDAO.obtenerTodos();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Cliente obtenerPorCedula(String cedula) {
        try {
            return clienteDAO.obtenerPorCedula(cedula);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Cliente obtenerPorId(int id) {
        try {
            return clienteDAO.obtenerPorId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public List<Cliente> buscarPorNombre(String nombre) {
        try {
            return clienteDAO.buscarPorNombre(nombre);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean crear(Cliente cliente) {
        try {
            return clienteDAO.insertar(cliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizar(Cliente cliente) {
        try {
            return clienteDAO.actualizar(cliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminar(int idCliente) {
        try {
            return clienteDAO.eliminar(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> obtenerCedulas() {
        try {
            return clienteDAO.obtenerCedulas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}