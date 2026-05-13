package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.view.cliente.FormRegistroCliente;
import com.mycompany.veterinaria.grupo4.view.cliente.PnlCliente;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import java.awt.Frame;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador para la gestion de clientes del sistema veterinario.
 * <p>
 * Gestiona la tabla de clientes, el formulario de registro/edicion,
 * y las operaciones REST sobre el recurso /api/cliente.
 * Permite buscar, crear, actualizar y eliminar clientes.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTRO AVILA JONATHAN XAVIER – MODULO: CLIENTE
 * @version 1.0
 * @since 1.0
 */
public class CtrlCliente {

    private PnlCliente pnlCliente;
    private Table tblCliente;
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api/cliente";
    private Cliente clienteSeleccionado;
    
    /**
     * Constructor del controlador de clientes.
     * 
     * @param pnlCliente panel principal de clientes
     */
    public CtrlCliente(PnlCliente pnlCliente) {
        this.pnlCliente = pnlCliente;
        this.tblCliente = pnlCliente.getTblCliente();
        cargarTabla();
        addListeners();
    }

    /**
     * Registra los listeners de los botones del panel.
     */
    private void addListeners() {
        pnlCliente.getBtnBuscar().addActionListener(e -> buscar());
        pnlCliente.getBtnNuevo().addActionListener(e -> nuevo());
    }

    /**
     * Carga todos los clientes en la tabla.
     */
    private void cargarTabla() {
        try {
            ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                apiBaseUrl + "/listar",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cliente>>() {}
            );
            List<Cliente> clientes = response.getBody();
            DefaultTableModel model = (DefaultTableModel) tblCliente.getModel();
            model.setRowCount(0);
            
            tblCliente.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Cedula", "Telefono", "Email", "Estado", "Accion"}
            ) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return col == 6;
                }
            });

            tblCliente.getColumnModel().getColumn(0).setMaxWidth(50);
            tblCliente.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblCliente.getColumnModel().getColumn(6).setPreferredWidth(120);

            if (clientes != null) {
                for (Cliente c : clientes) {
                    tblCliente.addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre() + " " + c.getApellido(),
                        c.getCedula(),
                        c.getTelefono(),
                        c.getCorreoElectronico(),
                        Estado.ACTIVO,
                        new ModelAction()
                            .add(ModelAction.Tipo.EDITAR,   () -> editar(c))
                            .add(ModelAction.Tipo.VER,      () -> ver(c))
                            .add(ModelAction.Tipo.ELIMINAR, () -> eliminar(c)
                        )
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al cargar clientes: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Realiza la busqueda de clientes por nombre.
     */
    private void buscar() {
        String texto = pnlCliente.getTxtBusqueda().getText().trim();
        if (texto.isEmpty()) {
            cargarTabla();
            return;
        }
        try {
            ResponseEntity<List<Cliente>> response = restTemplate.exchange(
                apiBaseUrl + "/buscar/nombre?nombre=" + texto,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cliente>>() {}
            );
            List<Cliente> clientes = response.getBody();
            DefaultTableModel model = (DefaultTableModel) pnlCliente.getTblCliente().getModel();
            model.setRowCount(0);
            if (clientes != null) {
                for (Cliente c : clientes) {
                    pnlCliente.getTblCliente().addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre() + " " + c.getApellido(),
                        c.getCedula(),
                        c.getTelefono(),
                        c.getCorreoElectronico(),
                        Estado.ACTIVO,
                        new ModelAction()
                            .add(ModelAction.Tipo.EDITAR,   () -> editar(c))
                            .add(ModelAction.Tipo.VER,      () -> ver(c))
                            .add(ModelAction.Tipo.ELIMINAR, () -> eliminar(c)
                        )
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre el formulario para registrar un nuevo cliente.
     */
    private void nuevo() {
        FormRegistroCliente form = new FormRegistroCliente(
            (Frame) SwingUtilities.getWindowAncestor(pnlCliente)
        );
        form.getBtnAccion().addActionListener(e -> {
            String error = validarDatos(form);
            if (error != null) {
                JOptionPane.showMessageDialog(form, error, "Validacion",
                    JOptionPane.WARNING_MESSAGE);
                return;      
            }
            registrar(getCliente(form));
            form.dispose();
        });
        form.setVisible(true);
    }

    /**
     * Abre el formulario para editar un cliente existente.
     * 
     * @param c cliente a editar
     */
    private void editar(Cliente c) {
        FormRegistroCliente form = new FormRegistroCliente(
            (Frame) SwingUtilities.getWindowAncestor(pnlCliente), c
        );
        form.getBtnAccion().addActionListener(e -> {
            String error = validarDatos(form);
            if (error != null) {
                JOptionPane.showMessageDialog(form, error, "Validacion",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            actualizar(getCliente(form));
            form.dispose();
        });
        form.setVisible(true);
    }

    /**
     * Muestra los detalles de un cliente.
     * 
     * @param c cliente a visualizar
     */
    private void ver(Cliente c) {
        // TODO: abrir panel de detalle
        System.out.println("Ver: " + c.getNombre());
    }
    
    /**
     * Registra un nuevo cliente en la API.
     * 
     * @param c cliente a registrar
     */
    private void registrar(Cliente c) {
        try {
            restTemplate.postForObject(apiBaseUrl + "/crear", c, Boolean.class);
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al registrar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza un cliente existente en la API.
     * 
     * @param c cliente con datos actualizados
     */
    private void actualizar(Cliente c) {
        try {
            restTemplate.put(apiBaseUrl + "/actualizar", c);
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al actualizar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina un cliente del sistema.
     * 
     * @param c cliente a eliminar
     */
    private void eliminar(Cliente c) {
        int confirm = JOptionPane.showConfirmDialog(pnlCliente,
            "¿Eliminar a " + c.getNombre() + " " + c.getApellido() + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                restTemplate.delete(apiBaseUrl + "/eliminar/" + c.getIdCliente());
                cargarTabla();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(pnlCliente,
                    "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Valida los datos del formulario de cliente.
     * 
     * @param form formulario de registro de cliente
     * @return mensaje de error o null si los datos son validos
     */
    public String validarDatos(FormRegistroCliente form) {
        if (form.getTxtCedula().getText().trim().isEmpty())
            return "Ingrese la cedula";
        if (form.getTxtCedula().getText().trim().length() != 10)
            return "La cedula debe tener 10 digitos";
        if (!form.getTxtCedula().getText().trim().matches("\\d+"))
            return "La cedula debe contener solo numeros";
        if (form.getTxtNombre().getText().trim().isEmpty())
            return "Ingrese el nombre";
        if (form.getTxtApellido().getText().trim().isEmpty())
            return "Ingrese el apellido";
        if (form.getTxtTelefono().getText().trim().isEmpty())
            return "Ingrese el telefono";
        if (form.getTxtTelefono().getText().trim().length() != 10)
            return "El telefono debe tener 10 digitos";
        if (!form.getTxtTelefono().getText().trim().matches("\\d+"))
            return "El telefono debe contener solo numeros";
        if (form.getTxtEmail().getText().trim().isEmpty())
            return "Ingrese el email";
        if (!form.getTxtEmail().getText().trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            return "Ingrese un email valido";
        return null;
    }
    
    /**
     * Obtiene un objeto Cliente a partir de los datos del formulario.
     * 
     * @param form formulario de registro de cliente
     * @return cliente con los datos del formulario
     */
    public Cliente getCliente(FormRegistroCliente form) {
        Cliente c = form.getClienteActual() != null ? form.getClienteActual() : new Cliente();
        c.setCedula(form.getTxtCedula().getText().trim());
        c.setNombre(form.getTxtNombre().getText().trim());
        c.setApellido(form.getTxtApellido().getText().trim());
        c.setTelefono(form.getTxtTelefono().getText().trim());
        c.setCorreoElectronico(form.getTxtEmail().getText().trim());
        c.setDireccion(form.getTxtDireccion().getText().trim());
        return c;
    }
}