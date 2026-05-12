package com.mycompany.veterinaria.grupo4.api.dto;

/**
 * Data Transfer Object para la transferencia de datos de clientes entre capas.
 * <p>
 * Este DTO encapsula la informacion basica de un cliente para ser utilizada
 * en operaciones de consulta y transferencia a traves de la API REST.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
 * @version 1.0
 * @since 1.0
 */
public class ClienteDTO {
    private int idCliente;
    private String cedula;
    private String nombreCompleto;
    private String telefono;
    private String correoElectronico;

    /**
     * Constructor por defecto.
     */
    public ClienteDTO() {}

    /**
     * Constructor con todos los campos del cliente.
     * 
     * @param idCliente identificador unico del cliente
     * @param cedula numero de cedula del cliente
     * @param nombreCompleto nombre completo del cliente
     * @param telefono numero de telefono del cliente
     * @param correoElectronico correo electronico del cliente
     */
    public ClienteDTO(int idCliente, String cedula, String nombreCompleto, String telefono, String correoElectronico) {
        this.idCliente = idCliente;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
}