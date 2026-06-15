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
 * @version 2.0
 * @since 1.0
 */
public class CtrlCliente {

    // Constantes para expresiones regulares
    private static final String REGEX_SOLO_LETRAS = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]+$";
    private static final String REGEX_SOLO_NUMEROS = "^\\d+$";
    private static final int LONGITUD_CEDULA = 10;
    private static final int LONGITUD_TELEFONO = 10;
    private static final int LONGITUD_MAX_NOMBRE = 50;
    private static final int LONGITUD_MAX_APELLIDO = 50;
    private static final int LONGITUD_MAX_EMAIL = 100;
    private static final int LONGITUD_MAX_DIRECCION = 100;

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
        
        // Configurar validaciones en tiempo real para los campos de texto
        configurarValidacionesTiempoReal(form);
        
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
        
        // Configurar validaciones en tiempo real para los campos de texto
        configurarValidacionesTiempoReal(form);
        
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
     * Configura validaciones en tiempo real para los campos del formulario.
     * 
     * @param form formulario de registro de cliente
     */
    private void configurarValidacionesTiempoReal(FormRegistroCliente form) {
        // Validación para cédula: solo números y máximo 10 dígitos
        form.getTxtCedula().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                // Permitir solo dígitos, backspace y delete
                if (!Character.isDigit(c) && c != '\b' && c != '\u007f') {
                    e.consume();
                }
                // Limitar a 10 caracteres
                String texto = form.getTxtCedula().getText();
                if (texto.length() >= LONGITUD_CEDULA && c != '\b' && c != '\u007f') {
                    e.consume();
                }
            }
        });
        
        // Validación para teléfono: solo números y máximo 10 dígitos
        form.getTxtTelefono().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b' && c != '\u007f') {
                    e.consume();
                }
                String texto = form.getTxtTelefono().getText();
                if (texto.length() >= LONGITUD_TELEFONO && c != '\b' && c != '\u007f') {
                    e.consume();
                }
            }
        });
        
        // Validación para nombre: solo letras, espacios, tildes y ñ
        form.getTxtNombre().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                // Permitir letras (incluyendo tildes y ñ), espacios, backspace y delete
                boolean esLetra = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
                                  (c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú' ||
                                   c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú' ||
                                   c == 'ñ' || c == 'Ñ');
                if (!esLetra && c != ' ' && c != '\b' && c != '\u007f') {
                    e.consume();
                }
                // Limitar a 50 caracteres
                String texto = form.getTxtNombre().getText();
                if (texto.length() >= LONGITUD_MAX_NOMBRE && c != '\b' && c != '\u007f') {
                    e.consume();
                }
            }
        });
        
        // Validación para apellido: solo letras, espacios, tildes y ñ
        form.getTxtApellido().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                boolean esLetra = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
                                  (c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú' ||
                                   c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó' || c == 'Ú' ||
                                   c == 'ñ' || c == 'Ñ');
                if (!esLetra && c != ' ' && c != '\b' && c != '\u007f') {
                    e.consume();
                }
                String texto = form.getTxtApellido().getText();
                if (texto.length() >= LONGITUD_MAX_APELLIDO && c != '\b' && c != '\u007f') {
                    e.consume();
                }
            }
        });
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
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            String mensajeError;
            try {
                mensajeError = e.getResponseBodyAsString();
                if (mensajeError.startsWith("\"") && mensajeError.endsWith("\"")) {
                    mensajeError = mensajeError.substring(1, mensajeError.length() - 1);
                }
            } catch (Exception ex) {
                mensajeError = "Error al registrar el cliente";
            }
            JOptionPane.showMessageDialog(pnlCliente, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
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
        // Validación de CÉDULA (obligatorio, 10 dígitos numéricos)
        String cedula = form.getTxtCedula().getText().trim();
        if (cedula.isEmpty()) {
            return "La cédula es obligatoria.";
        }
        if (cedula.length() != LONGITUD_CEDULA) {
            return "La cédula debe tener exactamente " + LONGITUD_CEDULA + " dígitos.";
        }
        if (!cedula.matches(REGEX_SOLO_NUMEROS)) {
            return "La cédula debe contener solo números.";
        }
        
        // Validación de NOMBRE (obligatorio, solo letras y espacios, máximo 50 caracteres)
        String nombre = form.getTxtNombre().getText().trim();
        if (nombre.isEmpty()) {
            return "El nombre es obligatorio.";
        }
        if (nombre.length() > LONGITUD_MAX_NOMBRE) {
            return "El nombre no puede exceder los " + LONGITUD_MAX_NOMBRE + " caracteres.";
        }
        if (!nombre.matches(REGEX_SOLO_LETRAS)) {
            return "El nombre solo puede contener letras y espacios.";
        }
        
        // Validación de APELLIDO (obligatorio, solo letras y espacios, máximo 50 caracteres)
        String apellido = form.getTxtApellido().getText().trim();
        if (apellido.isEmpty()) {
            return "El apellido es obligatorio.";
        }
        if (apellido.length() > LONGITUD_MAX_APELLIDO) {
            return "El apellido no puede exceder los " + LONGITUD_MAX_APELLIDO + " caracteres.";
        }
        if (!apellido.matches(REGEX_SOLO_LETRAS)) {
            return "El apellido solo puede contener letras y espacios.";
        }
        
        // Validación de TELÉFONO (obligatorio, 10 dígitos numéricos)
        String telefono = form.getTxtTelefono().getText().trim();
        if (telefono.isEmpty()) {
            return "El teléfono es obligatorio.";
        }
        if (telefono.length() != LONGITUD_TELEFONO) {
            return "El teléfono debe tener exactamente " + LONGITUD_TELEFONO + " dígitos.";
        }
        if (!telefono.matches(REGEX_SOLO_NUMEROS)) {
            return "El teléfono debe contener solo números.";
        }
        
        // Validación de EMAIL (opcional, pero si se ingresa debe tener formato válido)
        String email = form.getTxtEmail().getText().trim();
        if (!email.isEmpty()) {
            if (email.length() > LONGITUD_MAX_EMAIL) {
                return "El correo electrónico no puede exceder los " + LONGITUD_MAX_EMAIL + " caracteres.";
            }
            // Expresión regular mejorada para email
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (!email.matches(emailRegex)) {
                return "Ingrese un correo electrónico válido (ejemplo: usuario@dominio.com).";
            }
        }
        
        // Validación de DIRECCIÓN (opcional, solo longitud máxima)
        String direccion = form.getTxtDireccion().getText().trim();
        if (!direccion.isEmpty() && direccion.length() > LONGITUD_MAX_DIRECCION) {
            return "La dirección no puede exceder los " + LONGITUD_MAX_DIRECCION + " caracteres.";
        }
        
        return null; // Todo válido
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