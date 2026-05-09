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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
 * Gestiona la tabla principal, el formulario de registro/edición
 * y las operaciones REST sobre los recursos {@code /api/mascota}
 * y {@code /api/cliente}.
 *
 * @author juan
 */
public class CtrlMascotas {
 
    private final PnlMascota pnlMascota;
    private final Table      tblMascota;
    private FormRegistroMascota form;
 
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiBaseUrl = "http://localhost:8080/api";
 
    // ─── Constructor ──────────────────────────────────────────────────────────
 
    public CtrlMascotas(PnlMascota pnlMascota) {
        this.pnlMascota = pnlMascota;
        this.tblMascota = pnlMascota.getTblMascota();
        initTabla();
        cargarClientes();
        cargarTabla();
        addListeners();
    }
 
    // ─── Inicialización ───────────────────────────────────────────────────────
 
    private void initTabla() {
    tblMascota.setModel(new DefaultTableModel(
        new Object[][]{},
        new String[]{"Mascota", "Especie", "Raza", "Sexo", "Peso", "Estado", "Acción"}
    ) {
        @Override public boolean isCellEditable(int row, int col) { return col == 6; }
    });

    tblMascota.getColumnModel().getColumn(0).setPreferredWidth(140);
    tblMascota.getColumnModel().getColumn(1).setPreferredWidth(80);
    tblMascota.getColumnModel().getColumn(2).setPreferredWidth(80);
    tblMascota.getColumnModel().getColumn(3).setPreferredWidth(60);
    tblMascota.getColumnModel().getColumn(4).setPreferredWidth(60);
    tblMascota.getColumnModel().getColumn(5).setPreferredWidth(90);
    
    // AUMENTAR el ancho de la columna de acción (columna 6)
    // De 120 a 160 para dar espacio a los 4 botones
    tblMascota.getColumnModel().getColumn(6).setPreferredWidth(160);
    tblMascota.getColumnModel().getColumn(6).setMinWidth(150);   // Ancho mínimo
    tblMascota.getColumnModel().getColumn(6).setMaxWidth(200);   // Ancho máximo
    
    tblMascota.fixTable(pnlMascota.getScrollPane());
}
 
    private void addListeners() {
        pnlMascota.getBtnBuscar().addActionListener(e -> buscar());
        pnlMascota.getBtnNuevo().addActionListener(e -> nuevo());
        pnlMascota.getCmbClientes().addActionListener(e -> cargarMascotasPorCliente());
    }
    private void cargarClientes() {
      try {
          List<Cliente> clientes = restTemplate.exchange(
              apiBaseUrl + "/cliente/listar",
              HttpMethod.GET, null,
              new ParameterizedTypeReference<List<Cliente>>() {}
          ).getBody();
          if (clientes != null) {
              pnlMascota.getCmbClientes().removeAllItems();
              // Usar un renderer personalizado para mostrar la cédula
              // O crear un objeto con toString() sobrescrito
              pnlMascota.getCmbClientes().addItem(new Cliente(0, "", "TODOS", "LOS CLIENTES", ""));
              for (Cliente c : clientes) {
                  // Crear un objeto anónimo con toString() que muestre la cédula
                  pnlMascota.getCmbClientes().addItem(c);
              }

              // IMPORTANTE: Establecer un renderer para el ComboBox
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
            cargarTabla(); // Si no hay cliente seleccionado, carga todas
        }
    }
    // ─── Tabla ────────────────────────────────────────────────────────────────
 
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
            new ModelAction(
                () -> editar(m),                          // Editar
                () -> ver(m),                             // Ver detalles
                () -> eliminar(m),                        // Eliminar
                () -> mostrarFichaMedica(m.getIdMascota(), m.getNombre())  // Ficha Médica
            )
        });
    }
}
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
 
    // ─── Formulario ───────────────────────────────────────────────────────────
 
    /** Abre el formulario en modo alta. */
    private void nuevo() {
        form = new FormRegistroMascota(parentFrame());
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Abre el formulario en modo edición precargado con los datos de {@code m}.
     * Obtiene el cliente propietario desde la API antes de mostrar el formulario.
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
     * Se llama cada vez que se crea una nueva instancia del formulario.
     *
     * @param form instancia activa del formulario
     */
    private void conectarForm(FormRegistroMascota form) {
        form.getBtnBuscarCliente().addActionListener(e -> {
            String cedula = form.getTxtCedula().getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(form, "Ingrese la cédula del cliente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                // URL: /cliente/cedula/{cedula}
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
                JOptionPane.showMessageDialog(form, err, "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (form.isModoEdicion()) actualizar(form);
            else registrar(form);
        });
    }
 
    /** Busca un cliente por cédula y actualiza el campo de nombre en el formulario. */
    private void buscarClientePorCedula(FormRegistroMascota form) {
        String cedula = form.getTxtCedula().getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(form,
                "Ingrese la cédula del cliente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Cliente cliente = restTemplate.getForObject(
                apiBaseUrl + "/cliente/buscar?cedula=" + cedula, Cliente.class);
            form.setClienteSeleccionado(cliente);
        } catch (Exception ex) {
            form.setClienteSeleccionado(null);
            JOptionPane.showMessageDialog(form,
                "Error al buscar cliente: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // ─── Validación y construcción ────────────────────────────────────────────
 
    /**
     * Valida los campos obligatorios del formulario.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error, o {@code null} si todo es válido
     */
    private String validarDatos(FormRegistroMascota form) {
        if (form.getClienteSeleccionado() == null)
            return "Busque y seleccione un cliente antes de continuar.";
        if (form.getTxtNombre().getText().trim().isEmpty())
            return "El nombre de la mascota es obligatorio.";
        if (form.getTxtEspecie().getText().trim().isEmpty())
            return "La especie es obligatoria.";
        if (form.getCmbSexo().getSelectedIndex() < 0)
            return "Seleccione el sexo de la mascota.";
        return null;
    }
 
    /**
     * Construye un objeto {@link Mascota} con los datos actuales del formulario.
     * Si es modo edición, toma la instancia existente para conservar el ID.
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
 
    // ─── API ──────────────────────────────────────────────────────────────────
 
    private void registrar(FormRegistroMascota form) {
        guardar(buildMascota(form), form.getFotoBytes(), form.getFotoNombreArchivo());
        form.dispose();
    }
 
    private void actualizar(FormRegistroMascota form) {
        guardar(buildMascota(form), form.getFotoBytes(), form.getFotoNombreArchivo());
        form.dispose();
    }
 
    /**
     * Envía la mascota a la API como {@code multipart/form-data}.
     * Maneja tanto el alta ({@code idMascota == 0}) como la actualización.
     *
     * @param m              mascota a persistir
     * @param fotoBytes      bytes de la imagen, o {@code null} si no se cambió
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
                    "Mascota registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                body.add("idMascota", m.getIdMascota());
                restTemplate.put(apiBaseUrl + "/mascota/actualizar", request);
                JOptionPane.showMessageDialog(pnlMascota,
                    "Mascota actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al guardar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
/** Guarda o actualiza la ficha médica de una mascota. */
/** Guarda o actualiza la ficha médica de una mascota. */
public void guardarFichaMedica(int idMascota, String alergias,
        String enfermedades, String observaciones) {
    try {
        // Crear el DTO con los datos
        FichaMedicaDTO dto = new FichaMedicaDTO();
        dto.setIdMascota(idMascota);
        dto.setAlergias(alergias);
        dto.setEnfermedadesCronicas(enfermedades);
        dto.setObservaciones(observaciones);

        // Configurar headers para JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FichaMedicaDTO> request = new HttpEntity<>(dto, headers);

        // Enviar POST
        Boolean resultado = restTemplate.postForObject(
            apiBaseUrl + "/mascota/ficha/guardar", request, Boolean.class);
        
        if (resultado != null && resultado) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Ficha médica guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al guardar la ficha médica.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(pnlMascota,
            "Error al guardar ficha: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void ver(Mascota m) {
       // Crear un JDialog personalizado para mostrar los detalles
       JDialog dialog = new JDialog(parentFrame(), "Detalles de " + m.getNombre(), true);
       dialog.setSize(500, 550);
       dialog.setLocationRelativeTo(parentFrame());

       JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
       mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
       mainPanel.setBackground(Color.WHITE);

       // Panel superior con foto y nombre
       JPanel headerPanel = new JPanel(new BorderLayout(10, 5));
       headerPanel.setOpaque(false);

       // Foto
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

       // Nombre de la mascota
       JLabel nombreLabel = new JLabel(m.getNombre());
       nombreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
       nombreLabel.setForeground(new Color(230, 140, 30));
       JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       nombrePanel.setOpaque(false);
       nombrePanel.add(nombreLabel);
       headerPanel.add(nombrePanel, BorderLayout.CENTER);

       mainPanel.add(headerPanel, BorderLayout.NORTH);

       // Panel central con información en tabla
       JPanel infoPanel = new JPanel(new GridBagLayout());
       infoPanel.setOpaque(false);
       GridBagConstraints gbc = new GridBagConstraints();
       gbc.insets = new Insets(5, 10, 5, 10);
       gbc.anchor = GridBagConstraints.WEST;

       // Obtener cliente
       Cliente cliente = obtenerClientePorId(m.getIdCliente());
       String nombreCliente = cliente != null ? cliente.getNombre() + " " + cliente.getApellido() : "No encontrado";
       String cedulaCliente = cliente != null ? cliente.getCedula() : "No disponible";
       String telefonoCliente = cliente != null ? cliente.getTelefono() : "No disponible";

       // Agregar campos
       addInfoRow(infoPanel, gbc, "ID:", String.valueOf(m.getIdMascota()), 0);
       addInfoRow(infoPanel, gbc, "Especie:", m.getEspecie(), 1);
       addInfoRow(infoPanel, gbc, "Raza:", m.getRaza() != null ? m.getRaza() : "-", 2);
       addInfoRow(infoPanel, gbc, "Sexo:", m.getSexo() == 'M' ? "Macho" : "Hembra", 3);
       addInfoRow(infoPanel, gbc, "Peso:", m.getPeso() != null ? m.getPeso() + " kg" : "-", 4);
       addInfoRow(infoPanel, gbc, "Color:", m.getColor() != null ? m.getColor() : "-", 5);
       addInfoRow(infoPanel, gbc, "Fecha Nac.:", m.getFechaNacimiento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(m.getFechaNacimiento()) : "-", 6);
       addInfoRow(infoPanel, gbc, "Propietario:", nombreCliente, 7);
       addInfoRow(infoPanel, gbc, "Cédula:", cedulaCliente, 8);
       addInfoRow(infoPanel, gbc, "Teléfono:", telefonoCliente, 9);
       addInfoRow(infoPanel, gbc, "Fecha Registro:", m.getFechaRegistro() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(m.getFechaRegistro()) : "-", 10);

       JScrollPane scrollPane = new JScrollPane(infoPanel);
       scrollPane.setBorder(BorderFactory.createEmptyBorder());
       scrollPane.setOpaque(false);
       scrollPane.getViewport().setOpaque(false);
       mainPanel.add(scrollPane, BorderLayout.CENTER);

       // Panel inferior con botón cerrar
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
 
    private void eliminar(Mascota m) {
        int confirm = JOptionPane.showConfirmDialog(pnlMascota,
            "¿Está seguro de eliminar a " + m.getNombre() + "? Se eliminará también su ficha médica.\n" +
            "Si la mascota ya tiene antecedentes clínicos (citas), no podrá ser eliminada.",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            restTemplate.delete(apiBaseUrl + "/mascota/eliminar/" + m.getIdMascota());
            JOptionPane.showMessageDialog(pnlMascota, "Mascota eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            if (pnlMascota.getCmbClientes().getSelectedItem() != null) {
                cargarMascotasPorCliente();
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // Error HTTP 4xx - el backend lanza excepción con mensaje
            String mensajeError = "No se puede eliminar la mascota.";
            try {
                // Intentar extraer el mensaje del cuerpo de la respuesta
                String responseBody = e.getResponseBodyAsString();
                if (responseBody != null && responseBody.contains("cita médica")) {
                    mensajeError = "No se puede eliminar la mascota porque ya posee un antecedente clínico (cita médica realizada) en esta veterinaria.\n" +
                                   "Se recomienda mantener su registro para conservar la integridad de los datos.";
                } else if (responseBody != null && responseBody.contains("pendiente")) {
                    mensajeError = "No se puede eliminar la mascota porque tiene citas pendientes.\n" +
                                   "Primero cancele o complete las citas pendientes.";
                }
            } catch (Exception ex) {
                // Si no se puede extraer, usar mensaje por defecto
                mensajeError = e.getMessage();
            }
            JOptionPane.showMessageDialog(pnlMascota, mensajeError, "Eliminación no permitida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // ─── Utilidades ───────────────────────────────────────────────────────────
 
    /**
     * Obtiene el cliente propietario de una mascota dado su ID.
     *
     * @param idCliente identificador del cliente
     * @return cliente encontrado, o {@code null} si falla la llamada
     */
    private Cliente obtenerClientePorId(int idCliente) {
        try {
            return restTemplate.getForObject(
                apiBaseUrl + "/cliente/" + idCliente, Cliente.class);
        } catch (Exception e) {
            return null;
        }
    }
 
    /** Devuelve el {@link Frame} padre del panel de mascotas. */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlMascota);
    }
    private void mostrarFichaMedica(int idMascota, String nombreMascota) {
        JDialog dialog = new JDialog(parentFrame(), "Ficha Médica - " + nombreMascota, true);
        dialog.setSize(550, 450);
        dialog.setLocationRelativeTo(parentFrame());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Alergias
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Alergias:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextArea txtAlergias = new JTextArea(3, 20);
        txtAlergias.setLineWrap(true);
        JScrollPane scrollAlergias = new JScrollPane(txtAlergias);
        formPanel.add(scrollAlergias, gbc);

        // Enfermedades Crónicas
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Enfermedades Crónicas:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextArea txtEnfermedades = new JTextArea(3, 20);
        txtEnfermedades.setLineWrap(true);
        JScrollPane scrollEnfermedades = new JScrollPane(txtEnfermedades);
        formPanel.add(scrollEnfermedades, gbc);

        // Observaciones
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextArea txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setLineWrap(true);
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        formPanel.add(scrollObservaciones, gbc);

        // Cargar datos existentes
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
            System.err.println("Error al cargar ficha médica: " + e.getMessage());
        }

        // Panel de botones
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