package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.view.mascota.FormRegistroMascota;
import com.mycompany.veterinaria.grupo4.view.mascota.PnlMascota;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelProfile;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellRender;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador de la vista de Mascotas.
 * <p>
 * Gestiona la tabla principal, el formulario de registro/edicion
 * y las operaciones REST sobre los recursos /api/mascota
 * y /api/cliente. Permite registrar, editar, eliminar mascotas,
 * asi como gestionar su ficha medica y ver el historial clinico.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class CtrlMascotas {
 
    private final PnlMascota pnlMascota;
    private final Table      tblMascota;
    private FormRegistroMascota form;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiBaseUrl = "http://localhost:8080/api";
 
    /**
     * Constructor del controlador de mascotas.
     * 
     * @param pnlMascota panel principal de mascotas
     */
    public CtrlMascotas(PnlMascota pnlMascota) {
        this.pnlMascota = pnlMascota;
        this.tblMascota = pnlMascota.getTblMascota();
        initTabla();
        cargarClientes();
        cargarTabla();
        addListeners();
    }
 
    /**
     * Inicializa la estructura de la tabla de mascotas.
     */
    private void initTabla() {
        tblMascota.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Mascota", "Especie", "Raza", "Sexo", "Peso", "Estado", "Accion"}
        ) {
            @Override public boolean isCellEditable(int row, int col) { return col == 6; }
        });
        var col = pnlMascota.getTblMascota().getColumnModel();
        col.getColumn(0).setPreferredWidth(140);
        col.getColumn(1).setPreferredWidth(80);
        col.getColumn(2).setPreferredWidth(80);
        col.getColumn(3).setPreferredWidth(60);
        col.getColumn(4).setPreferredWidth(60);
        col.getColumn(5).setPreferredWidth(90);
        col.getColumn(6).setPreferredWidth(220);
        
        int colAccion = 6; // El índice de la columna "Accion"
        col.getColumn(colAccion).setCellRenderer(new TableCellRender());
        col.getColumn(colAccion).setCellEditor(new TableCellAction());
        
        tblMascota.fixTable(pnlMascota.getScrollPane());
    }
 
    /**
     * Registra los listeners del panel principal.
     */
    private void addListeners() {
        pnlMascota.getBtnBuscar().addActionListener(e -> buscar());
        pnlMascota.getBtnNuevo().addActionListener(e -> nuevo());
        pnlMascota.getCmbClientes().addActionListener(e -> cargarMascotasPorCliente());
    }
    
    /**
     * Carga la lista de clientes en el combo box.
     */
    private void cargarClientes() {
        try {
            List<Cliente> clientes = restTemplate.exchange(
                apiBaseUrl + "/cliente/listar",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Cliente>>() {}
            ).getBody();
            if (clientes != null) {
                pnlMascota.getCmbClientes().removeAllItems();
                pnlMascota.getCmbClientes().addItem(new Cliente(0, "", "TODOS", "LOS CLIENTES", ""));
                for (Cliente c : clientes) {
                    pnlMascota.getCmbClientes().addItem(c);
                }

                pnlMascota.getCmbClientes().setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, 
                            int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof Cliente) {
                            Cliente c = (Cliente) value;
                            if (c.getIdCliente() == 0) {
                                setText("--- TODOS LOS CLIENTES ---");
                            } else {
                                setText(c.getCedula() + " - " + c.getNombre() + " " + c.getApellido());
                            }
                        }
                        return this;
                    }
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota, "Error al cargar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carga las mascotas filtradas por cliente seleccionado.
     */
    private void cargarMascotasPorCliente() {
        Cliente clienteSeleccionado = (Cliente) pnlMascota.getCmbClientes().getSelectedItem();
        if (clienteSeleccionado != null && clienteSeleccionado.getIdCliente() > 0) {
            try {
                List<Mascota> lista = restTemplate.exchange(
                    apiBaseUrl + "/mascota/listar/" + clienteSeleccionado.getIdCliente(),
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Mascota>>() {}
                ).getBody();
                llenarTabla(lista);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(pnlMascota, "Error al cargar mascotas del cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            cargarTabla();
        }
    }
    
    /**
     * Carga todas las mascotas desde la API.
     */
    private void cargarTabla() {
        try {
            List<Mascota> lista = restTemplate.exchange(
                apiBaseUrl + "/mascota/listar",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Mascota>>() {}
            ).getBody();
            llenarTabla(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al cargar mascotas: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Llena la tabla con los datos de las mascotas.
     * 
     * @param mascotas lista de mascotas a mostrar
     */
    private void llenarTabla(List<Mascota> mascotas) {
        DefaultTableModel model = (DefaultTableModel) tblMascota.getModel();
        model.setRowCount(0);
        if (mascotas == null) return;

        for (Mascota m : mascotas) {
            byte[]    imgData = m.getFoto();
            ImageIcon foto    = (imgData != null && imgData.length > 0)
                ? new ImageIcon(imgData)
                : new ImageIcon(getClass().getResource("/icon/pet-paw.png"));

            tblMascota.addRow(new Object[]{
                new ModelProfile(foto, m.getNombre()),
                m.getEspecie(),
                m.getRaza() != null ? m.getRaza() : "-",
                m.getSexo() == 'M' ? "Macho" : "Hembra",
                m.getPeso() != null ? m.getPeso() + " kg" : "-",
                Estado.ACTIVO,
                new ModelAction()
                    .add(ModelAction.Tipo.EDITAR,    () -> editar(m))
                    .add(ModelAction.Tipo.VER,       () -> ver(m))
                    //.add(ModelAction.Tipo.ATENCION,  () -> mostrarFichaMedica(m.getIdMascota(), m.getNombre()))
                    .add(ModelAction.Tipo.HISTORIAL, () -> mostrarHistorialMedico(m.getIdMascota(), m.getNombre()))
                    .add(ModelAction.Tipo.ELIMINAR,  () -> eliminar(m)
                )
            });
        }
    }
    
    /**
     * Muestra el historial medico de una mascota.
     * 
     * @param idMascota identificador de la mascota
     * @param nombreMascota nombre de la mascota
     */
    private void mostrarHistorialMedico(int idMascota, String nombreMascota) {
        com.mycompany.veterinaria.grupo4.view.historial.PnlHistorialMedico pnlHistorial = 
            new com.mycompany.veterinaria.grupo4.view.historial.PnlHistorialMedico();

        com.mycompany.veterinaria.grupo4.controller.CtrlHistorialMedico ctrlHistorial = 
            new com.mycompany.veterinaria.grupo4.controller.CtrlHistorialMedico(pnlHistorial);

        JDialog dialog = new JDialog(parentFrame(), "Historial Medico - " + nombreMascota, true);
        dialog.setSize(1100, 700);
        dialog.setLocationRelativeTo(parentFrame());
        dialog.setContentPane(pnlHistorial);

        try {
            Mascota mascota = restTemplate.getForObject(
                apiBaseUrl + "/mascota/" + idMascota, Mascota.class);
            if (mascota != null) {
                pnlHistorial.getTxtBuscarMascota().setText(mascota.getNombre());
                SwingUtilities.invokeLater(() -> {
                    pnlHistorial.getBtnBuscarMascota().doClick();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.setVisible(true);
    }
    
    /**
     * Busca mascotas por termino de busqueda.
     */
    private void buscar() {
        String texto = pnlMascota.getTxtBusqueda().getText().trim();
        if (texto.isEmpty()) { cargarTabla(); return; }
        try {
            List<Mascota> lista = restTemplate.exchange(
                apiBaseUrl + "/mascota/buscar?termino=" + texto,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Mascota>>() {}
            ).getBody();
            llenarTabla(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al buscar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Abre el formulario en modo alta.
     */
    private void nuevo() {
        form = new FormRegistroMascota(parentFrame());
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Abre el formulario en modo edicion precargado con los datos de la mascota.
     *
     * @param m mascota a editar
     */
    private void editar(Mascota m) {
        Cliente cliente = obtenerClientePorId(m.getIdCliente());
        form = new FormRegistroMascota(parentFrame(), m, cliente);
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Registra los listeners del formulario activo.
     *
     * @param form instancia activa del formulario
     */
    private void conectarForm(FormRegistroMascota form) {
        form.getBtnBuscarCliente().addActionListener(e -> {
            String cedula = form.getTxtCedula().getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(form, "Ingrese la cedula del cliente.", "Busqueda", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Cliente cliente = restTemplate.getForObject(apiBaseUrl + "/cliente/cedula/" + cedula, Cliente.class);
                form.setClienteSeleccionado(cliente);
            } catch (Exception ex) {
                form.setClienteSeleccionado(null);
                JOptionPane.showMessageDialog(form, "Cliente no encontrado o error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.getTxtCedula().addActionListener(e -> {
            String cedula = form.getTxtCedula().getText().trim();
            if (!cedula.isEmpty()) {
                try {
                    Cliente cliente = restTemplate.getForObject(apiBaseUrl + "/cliente/cedula/" + cedula, Cliente.class);
                    form.setClienteSeleccionado(cliente);
                } catch (Exception ex) {
                    form.setClienteSeleccionado(null);
                    JOptionPane.showMessageDialog(form, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
 
        form.getBtnAccion().addActionListener(e -> {
            String err = validarDatos(form);
            if (err != null) {
                form.setValidando(true);
                try {
                    JOptionPane.showMessageDialog(form, err, "Validacion", JOptionPane.WARNING_MESSAGE);
                } finally {
                    form.setValidando(false);
                }
                return;
            }
            if (form.isModoEdicion()) actualizar(form);
            else registrar(form);
        });
    }
 
    /**
     * Valida los campos obligatorios del formulario.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error, o null si todo es valido
     */
    private String validarDatos(FormRegistroMascota form) {

        // ─── Cliente ──────────────────────────────────────────────────────────────
        if (form.getClienteSeleccionado() == null)
           return "Busque y seleccione un cliente antes de continuar.";

        // ─── Nombre ───────────────────────────────────────────────────────────────
        String nombre = form.getTxtNombre().getText().trim();
        if (nombre.isEmpty())
            return "El nombre de la mascota es obligatorio.";
        if (nombre.length() < 2)
            return "El nombre debe tener al menos 2 caracteres.";
        if (nombre.length() > 50)
            return "El nombre no puede superar los 50 caracteres.";
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+"))
            return "El nombre solo puede contener letras y espacios.";

        // ─── Especie ──────────────────────────────────────────────────────────────
        String especie = form.getTxtEspecie().getText().trim();
        if (especie.isEmpty())
            return "La especie es obligatoria.";
        if (especie.length() < 2)
            return "La especie debe tener al menos 2 caracteres.";
        if (especie.length() > 50)
            return "La especie no puede superar los 50 caracteres.";
        if (!especie.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+"))
            return "La especie solo puede contener letras y espacios.";

        // ─── Raza (opcional) ──────────────────────────────────────────────────────
        String raza = form.getTxtRaza().getText().trim();
        if (!raza.isEmpty()) {
            if (raza.length() < 2)
                return "La raza debe tener al menos 2 caracteres.";
            if (raza.length() > 50)
                return "La raza no puede superar los 50 caracteres.";
            if (!raza.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+"))
                return "La raza solo puede contener letras y espacios.";
        }

        // ─── Sexo ─────────────────────────────────────────────────────────────────
        if (form.getCmbSexo().getSelectedIndex() < 0)
            return "Seleccione el sexo de la mascota.";

        // ─── Fecha de nacimiento (opcional) ───────────────────────────────────────
        Date fechaNac = form.getFechaNacimiento();
        if (fechaNac != null) {
            if (fechaNac.after(new Date()))
                return "La fecha de nacimiento no puede ser futura.";
            // No más de 50 años atrás (límite razonable para cualquier mascota)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -50);
            if (fechaNac.before(cal.getTime()))
                return "La fecha de nacimiento no es válida.";
        }

        // ─── Peso (opcional) ──────────────────────────────────────────────────────
        String pesoTxt = form.getTxtPeso().getText().trim();
        if (!pesoTxt.isEmpty()) {
            try {
                double peso = Double.parseDouble(pesoTxt);
                if (peso <= 0)
                    return "El peso debe ser un valor positivo.";
                if (peso > 999)
                    return "El peso ingresado no es válido.";
            } catch (NumberFormatException e) {
                return "El peso debe ser un valor numérico válido. Ej: 4.5";
            }
        }

        // ─── Color (opcional) ─────────────────────────────────────────────────────
        String color = form.getTxtColor().getText().trim();
        if (!color.isEmpty()) {
            if (color.length() < 2)
                return "El color debe tener al menos 2 caracteres.";
            if (color.length() > 50)
                return "El color no puede superar los 50 caracteres.";
            if (!color.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+"))
                return "El color solo puede contener letras y espacios.";
        }
    return null;
}
 
    /**
     * Construye un objeto Mascota con los datos actuales del formulario.
     *
     * @param form instancia activa del formulario
     * @return mascota lista para enviar a la API
     */
    private Mascota buildMascota(FormRegistroMascota form) {
        Mascota m = form.isModoEdicion() ? form.getMascotaActual() : new Mascota();
 
        m.setIdCliente(form.getClienteSeleccionado().getIdCliente());
        m.setNombre(form.getTxtNombre().getText().trim());
        m.setEspecie(form.getTxtEspecie().getText().trim());
        m.setRaza(form.getTxtRaza().getText().trim());
        m.setSexo("Macho".equals(form.getCmbSexo().getSelectedItem()) ? 'M' : 'H');
        m.setFechaNacimiento(form.getFechaNacimiento());
 
        String pesoTxt = form.getTxtPeso().getText().trim();
        if (!pesoTxt.isEmpty()) {
            try { m.setPeso(new BigDecimal(pesoTxt).doubleValue()); } catch (NumberFormatException ignored) {}
        }
 
        String colorTxt = form.getTxtColor().getText().trim();
        if (!colorTxt.isEmpty()) m.setColor(colorTxt);
 
        return m;
    }
 
    /**
     * Registra una nueva mascota.
     * 
     * @param form formulario con los datos
     */
    private void registrar(FormRegistroMascota form) {
        guardar(buildMascota(form), form.getFotoBytes(), form.getFotoNombreArchivo());
        form.dispose();
    }
 
    /**
     * Actualiza una mascota existente.
     * 
     * @param form formulario con los datos actualizados
     */
    private void actualizar(FormRegistroMascota form) {
        guardar(buildMascota(form), form.getFotoBytes(), form.getFotoNombreArchivo());
        form.dispose();
    }
 
    /**
     * Envia la mascota a la API como multipart/form-data.
     * Maneja tanto el alta como la actualizacion.
     *
     * @param m              mascota a persistir
     * @param fotoBytes      bytes de la imagen, o null si no se cambia
     * @param nombreArchivo  nombre del archivo de imagen
     */
    public void guardar(Mascota m, byte[] fotoBytes, String nombreArchivo) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("idCliente", m.getIdCliente());
            body.add("nombre",    m.getNombre());
            body.add("especie",   m.getEspecie());
            body.add("raza",      m.getRaza());
            body.add("sexo",      String.valueOf(m.getSexo()));
            body.add("peso",      m.getPeso());
            body.add("color",     m.getColor());
            if (m.getFechaNacimiento() != null)
                body.add("fechaNacimiento",
                    new SimpleDateFormat("yyyy-MM-dd").format(m.getFechaNacimiento()));
 
            if (fotoBytes != null) {
                body.add("foto", new org.springframework.core.io.ByteArrayResource(fotoBytes) {
                    @Override public String getFilename() { return nombreArchivo; }
                });
            }
 
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
 
            if (m.getIdMascota() == 0) {
                restTemplate.postForObject(apiBaseUrl + "/mascota/crear", request, Integer.class);
                JOptionPane.showMessageDialog(pnlMascota,
                    "Mascota registrada correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                body.add("idMascota", m.getIdMascota());
                restTemplate.put(apiBaseUrl + "/mascota/actualizar", request);
                JOptionPane.showMessageDialog(pnlMascota,
                    "Mascota actualizada correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Guarda o actualiza la ficha medica de una mascota.
     */
    public void guardarFichaMedica(int idMascota, String alergias,
            String enfermedades, String observaciones) {
        try {
            FichaMedicaDTO dto = new FichaMedicaDTO();
            dto.setIdMascota(idMascota);
            dto.setAlergias(alergias);
            dto.setEnfermedadesCronicas(enfermedades);
            dto.setObservaciones(observaciones);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<FichaMedicaDTO> request = new HttpEntity<>(dto, headers);

            Boolean resultado = restTemplate.postForObject(
                apiBaseUrl + "/mascota/ficha/guardar", request, Boolean.class);
            
            if (resultado != null && resultado) {
                JOptionPane.showMessageDialog(pnlMascota,
                    "Ficha medica guardada correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(pnlMascota,
                    "Error al guardar la ficha medica.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al guardar ficha: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Muestra los detalles completos de una mascota.
     * 
     * @param m mascota a visualizar
     */
    private void ver(Mascota m) {
        JDialog dialog = new JDialog(parentFrame(), "Detalles de " + m.getNombre(), true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(parentFrame());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout(10, 5));
        headerPanel.setOpaque(false);

        JLabel fotoLabel = new JLabel();
        fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (m.getFoto() != null && m.getFoto().length > 0) {
            ImageIcon foto = new ImageIcon(m.getFoto());
            Image img = foto.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            fotoLabel.setIcon(new ImageIcon(img));
        } else {
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/icon/pet-paw.png"));
            Image img = defaultIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            fotoLabel.setIcon(new ImageIcon(img));
        }

        JPanel fotoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fotoPanel.setOpaque(false);
        fotoPanel.add(fotoLabel);
        headerPanel.add(fotoPanel, BorderLayout.WEST);

        JLabel nombreLabel = new JLabel(m.getNombre());
        nombreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nombreLabel.setForeground(new Color(230, 140, 30));
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nombrePanel.setOpaque(false);
        nombrePanel.add(nombreLabel);
        headerPanel.add(nombrePanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Cliente cliente = obtenerClientePorId(m.getIdCliente());
        String nombreCliente = cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : "No encontrado";
        String cedulaCliente = cliente != null ? cliente.getCedula() : "No disponible";
        String telefonoCliente = cliente != null ? cliente.getTelefono() : "No disponible";

        addInfoRow(infoPanel, gbc, "ID:", String.valueOf(m.getIdMascota()), 0);
        addInfoRow(infoPanel, gbc, "Especie:", m.getEspecie(), 1);
        addInfoRow(infoPanel, gbc, "Raza:", m.getRaza() != null ? m.getRaza() : "-", 2);
        addInfoRow(infoPanel, gbc, "Sexo:", m.getSexo() == 'M' ? "Macho" : "Hembra", 3);
        addInfoRow(infoPanel, gbc, "Peso:", m.getPeso() != null ? m.getPeso() + " kg" : "-", 4);
        addInfoRow(infoPanel, gbc, "Color:", m.getColor() != null ? m.getColor() : "-", 5);
        addInfoRow(infoPanel, gbc, "Fecha Nac.:", m.getFechaNacimiento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(m.getFechaNacimiento()) : "-", 6);
        addInfoRow(infoPanel, gbc, "Propietario:", nombreCliente, 7);
        addInfoRow(infoPanel, gbc, "Cedula:", cedulaCliente, 8);
        addInfoRow(infoPanel, gbc, "Telefono:", telefonoCliente, 9);
        addInfoRow(infoPanel, gbc, "Fecha Registro:", m.getFechaRegistro() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(m.getFechaRegistro()) : "-", 10);

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(230, 140, 30));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnCerrar);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }

    /**
     * Agrega una fila de informacion al panel de detalles.
     */
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, String label, String value, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(100, 100, 100));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(val, gbc);
    }
 
/**
 * Elimina una mascota del sistema.
 * 
 * @param m mascota a eliminar
 */
private void eliminar(Mascota m) {
    int confirm = JOptionPane.showConfirmDialog(pnlMascota,
        "¿Esta seguro de eliminar a " + m.getNombre() + "?\n" +
        "Esta accion no se puede deshacer.",
        "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

    if (confirm != JOptionPane.YES_OPTION) return;

    try {
        // Usar exchange para obtener la respuesta completa
        ResponseEntity<String> response = restTemplate.exchange(
            apiBaseUrl + "/mascota/eliminar/" + m.getIdMascota(),
            HttpMethod.DELETE,
            null,
            String.class
        );
        
        if (response.getStatusCode().is2xxSuccessful()) {
            JOptionPane.showMessageDialog(pnlMascota, 
                "Mascota eliminada correctamente.", 
                "Exito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            
            // Refrescar filtro por cliente si está activo
            if (pnlMascota.getCmbClientes().getSelectedItem() != null) {
                cargarMascotasPorCliente();
            }
        }
        
    } catch (org.springframework.web.client.HttpClientErrorException e) {
        // Error 4xx (incluye CONFLICT 409)
        String mensajeError;
        try {
            // Intentar obtener el mensaje del cuerpo de la respuesta
            mensajeError = e.getResponseBodyAsString();
            // Limpiar comillas si vienen en el JSON
            if (mensajeError.startsWith("\"") && mensajeError.endsWith("\"")) {
                mensajeError = mensajeError.substring(1, mensajeError.length() - 1);
            }
        } catch (Exception ex) {
            mensajeError = e.getMessage();
        }
        
        JOptionPane.showMessageDialog(pnlMascota, 
            mensajeError, 
            "No se puede eliminar", 
            JOptionPane.ERROR_MESSAGE);
            
    } catch (Exception e) {
        JOptionPane.showMessageDialog(pnlMascota,
            "Error al eliminar: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    /**
     * Obtiene el cliente propietario de una mascota dado su ID.
     *
     * @param idCliente identificador del cliente
     * @return cliente encontrado, o null si falla la llamada
     */
    private Cliente obtenerClientePorId(int idCliente) {
        try {
            return restTemplate.getForObject(
                apiBaseUrl + "/cliente/" + idCliente, Cliente.class);
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     * Devuelve el Frame padre del panel de mascotas.
     */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlMascota);
    }
    
    /**
     * Muestra el formulario de ficha medica de la mascota.
     * 
     * @param idMascota identificador de la mascota
     * @param nombreMascota nombre de la mascota
     */
    private void mostrarFichaMedica(int idMascota, String nombreMascota) {
        JDialog dialog = new JDialog(parentFrame(), "Ficha Medica - " + nombreMascota, true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(parentFrame());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Alergias:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextArea txtAlergias = new JTextArea(3, 20);
        txtAlergias.setLineWrap(true);
        JScrollPane scrollAlergias = new JScrollPane(txtAlergias);
        formPanel.add(scrollAlergias, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Enfermedades Cronicas:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextArea txtEnfermedades = new JTextArea(3, 20);
        txtEnfermedades.setLineWrap(true);
        JScrollPane scrollEnfermedades = new JScrollPane(txtEnfermedades);
        formPanel.add(scrollEnfermedades, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextArea txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        formPanel.add(scrollObservaciones, gbc);

        try {
            FichaMedicaDTO ficha = restTemplate.getForObject(
                apiBaseUrl + "/mascota/ficha/" + idMascota, 
                FichaMedicaDTO.class);
            if (ficha != null) {
                txtAlergias.setText(ficha.getAlergias() != null ? ficha.getAlergias() : "");
                txtEnfermedades.setText(ficha.getEnfermedadesCronicas() != null ? ficha.getEnfermedadesCronicas() : "");
                txtObservaciones.setText(ficha.getObservaciones() != null ? ficha.getObservaciones() : "");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar ficha medica: " + e.getMessage());
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(230, 140, 30));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(150, 150, 150));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        btnGuardar.addActionListener(e -> {
            guardarFichaMedica(idMascota, txtAlergias.getText(), txtEnfermedades.getText(), txtObservaciones.getText());
            dialog.dispose();
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);
    }
}