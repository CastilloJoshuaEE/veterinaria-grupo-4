package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.view.cliente.FormRegistroCliente;
import com.mycompany.veterinaria.grupo4.view.cliente.PnlCliente;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellRender;
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
        initTabla();
        cargarTabla();
        addListeners();
    }

    /**
     * Registra los listeners de los botones del panel.
     */
    private void addListeners() {
        if (pnlCliente.getBtnBuscar() != null) {
            pnlCliente.getBtnBuscar().addActionListener(e -> buscar());
        }
        if (pnlCliente.getBtnNuevo() != null) {
            pnlCliente.getBtnNuevo().addActionListener(e -> nuevo());
        }
    }
    
    /**
     * Inicializa la estructura de la tabla de clientes.
     */
    private void initTabla() {
        Table tblCliente = pnlCliente.getTblCliente();
        if (tblCliente == null) {
            System.err.println("ERROR: La tabla de clientes es null");
            return;
        }
        
        tblCliente.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nombre", "Cedula", "Telefono", "Email", "Estado", "Accion"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        });
        
        var col = tblCliente.getColumnModel();
        if (col.getColumnCount() > 0) {
            col.getColumn(0).setMaxWidth(50);
            if (col.getColumnCount() > 5) {
                col.getColumn(5).setPreferredWidth(90);
            }
            if (col.getColumnCount() > 6) {
                col.getColumn(6).setPreferredWidth(120);
                
                int colAccion = 6;
                col.getColumn(colAccion).setCellRenderer(new TableCellRender());
                col.getColumn(colAccion).setCellEditor(new TableCellAction());
            }
        }
        
        // Ajustar la tabla en el scroll pane
        if (pnlCliente.getScrollPane() != null) {
            tblCliente.fixTable(pnlCliente.getScrollPane());
        }
    }

    /**
     * Carga todos los clientes en la tabla.
     */
    private void cargarTabla() {
        Table tblCliente = pnlCliente.getTblCliente();
        if (tblCliente == null) {
            System.err.println("ERROR: No se puede cargar la tabla porque es null");
            return;
        }
        
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
   
            if (clientes != null) {
                for (Cliente c : clientes) {
                    model.addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre() + " " + c.getApellido(),
                        c.getCedula(),
                        c.getTelefono(),
                        c.getCorreoElectronico() != null ? c.getCorreoElectronico() : "",
                        Estado.ACTIVO,
                        new ModelAction()
                            .add(ModelAction.Tipo.EDITAR,   () -> editar(c))
                            .add(ModelAction.Tipo.VER,      () -> ver(c))
                            .add(ModelAction.Tipo.ELIMINAR, () -> eliminar(c))
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al cargar clientes: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Realiza la busqueda de clientes por nombre.
     */
    private void buscar() {
        Table tblCliente = pnlCliente.getTblCliente();
        if (tblCliente == null) return;
        
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
            DefaultTableModel model = (DefaultTableModel) tblCliente.getModel();
            model.setRowCount(0);
            if (clientes != null) {
                for (Cliente c : clientes) {
                    model.addRow(new Object[]{
                        c.getIdCliente(),
                        c.getNombre() + " " + c.getApellido(),
                        c.getCedula(),
                        c.getTelefono(),
                        c.getCorreoElectronico() != null ? c.getCorreoElectronico() : "",
                        Estado.ACTIVO,
                        new ModelAction()
                            .add(ModelAction.Tipo.EDITAR,   () -> editar(c))
                            .add(ModelAction.Tipo.VER,      () -> ver(c))
                            .add(ModelAction.Tipo.ELIMINAR, () -> eliminar(c))
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al buscar clientes: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre el formulario para registrar un nuevo cliente.
     */
    private void nuevo() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(pnlCliente);
        if (parentFrame == null) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error: No se puede determinar la ventana padre",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        FormRegistroCliente form = new FormRegistroCliente(parentFrame);
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
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(pnlCliente);
        if (parentFrame == null) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error: No se puede determinar la ventana padre",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        FormRegistroCliente form = new FormRegistroCliente(parentFrame, c);
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
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("ID: ").append(c.getIdCliente()).append("\n");
        mensaje.append("Nombre: ").append(c.getNombre()).append(" ").append(c.getApellido()).append("\n");
        mensaje.append("Cedula: ").append(c.getCedula()).append("\n");
        mensaje.append("Telefono: ").append(c.getTelefono()).append("\n");
        mensaje.append("Email: ").append(c.getCorreoElectronico()).append("\n");
        mensaje.append("Direccion: ").append(c.getDireccion() != null ? c.getDireccion() : "No registrada").append("\n");
        
        JOptionPane.showMessageDialog(pnlCliente, mensaje.toString(),
            "Detalle del Cliente", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Registra un nuevo cliente en la API.
     * 
     * @param c cliente a registrar
     */
    private void registrar(Cliente c) {
        try {
            Boolean resultado = restTemplate.postForObject(apiBaseUrl + "/crear", c, Boolean.class);
            if (Boolean.TRUE.equals(resultado)) {
                JOptionPane.showMessageDialog(pnlCliente,
                    "Cliente registrado correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(pnlCliente,
                    "Error al registrar el cliente",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al registrar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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
            JOptionPane.showMessageDialog(pnlCliente,
                "Cliente actualizado correctamente",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCliente,
                "Error al actualizar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Elimina un cliente del sistema.
     * 
     * @param c cliente a eliminar
     */
    private void eliminar(Cliente c) {
        int confirm = JOptionPane.showConfirmDialog(pnlCliente,
            "¿Eliminar a " + c.getNombre() + " " + c.getApellido() + "?\n" +
            "Se eliminaran tambien sus mascotas asociadas.",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                restTemplate.delete(apiBaseUrl + "/eliminar/" + c.getIdCliente());
                JOptionPane.showMessageDialog(pnlCliente,
                    "Cliente eliminado correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(pnlCliente,
                    "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
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
        String direccion = form.getTxtDireccion().getText().trim();
        c.setDireccion(direccion.isEmpty() ? null : direccion);
        return c;
    }
}