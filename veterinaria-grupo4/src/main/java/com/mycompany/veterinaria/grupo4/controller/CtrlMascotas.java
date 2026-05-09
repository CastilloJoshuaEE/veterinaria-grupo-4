package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.view.mascota.FormRegistroMascota;
import com.mycompany.veterinaria.grupo4.view.mascota.PnlMascota;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelProfile;
import com.mycompany.veterinaria.grupo4.view.swing.table.Table;
import java.awt.Frame;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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
        tblMascota.getColumnModel().getColumn(6).setPreferredWidth(120);
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
                pnlMascota.getCmbClientes().addItem(new Cliente(0, "", "", "", ""));
                for (Cliente c : clientes) {
                    pnlMascota.getCmbClientes().addItem(c);
                }
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
                    () -> editar(m),
                    () -> ver(m),
                    () -> eliminar(m)
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
    public void guardarFichaMedica(int idMascota, String alergias,
            String enfermedades, String observaciones) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("idMascota",            idMascota);
            body.add("alergias",             alergias);
            body.add("enfermedadesCronicas", enfermedades);
            body.add("observaciones",        observaciones);
 
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
 
            restTemplate.postForObject(
                apiBaseUrl + "/mascota/ficha/actualizar", request, Boolean.class);
            JOptionPane.showMessageDialog(pnlMascota,
                "Ficha médica guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlMascota,
                "Error al guardar ficha: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void ver(Mascota m) {
        // TODO: panel de detalle de mascota
        System.out.println("Ver: " + m.getNombre());
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
            cargarTabla(); // Recargar la tabla
            // Si hay un cliente seleccionado, recargar sus mascotas
            if (pnlMascota.getCmbClientes().getSelectedItem() != null) {
                cargarMascotasPorCliente();
            }
        } catch (Exception e) {
            // Manejo del error si la mascota tiene antecedentes
            if (e.getMessage().contains("la mascota ya posee una cita médica")) {
                JOptionPane.showMessageDialog(pnlMascota,
                    "No se puede eliminar la mascota porque ya posee un antecedente clínico en esta veterinaria.\n" +
                    "Se recomienda mantener su registro para conservar la integridad de los datos.",
                    "Eliminación no permitida", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(pnlMascota,
                    "Error al eliminar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
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
}