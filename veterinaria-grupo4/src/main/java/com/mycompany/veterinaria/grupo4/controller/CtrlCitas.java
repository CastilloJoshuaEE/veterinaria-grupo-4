package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.controller.CitaController.CitaRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.Cliente;
import com.mycompany.veterinaria.grupo4.model.entity.EstadoCita;
import com.mycompany.veterinaria.grupo4.model.entity.Mascota;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.view.cita.FormRegistroCita;
import com.mycompany.veterinaria.grupo4.view.cita.PnlCita;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador de la vista de Citas.
 * <p>
 * Coordina la carga de datos, el formulario de registro/edición
 * y las operaciones REST sobre el recurso {@code /api/cita}.
 *
 * @author juan
 */
public class CtrlCitas {
 
    private final PnlCita pnlCita;
    private FormRegistroCita form;
    private final RestTemplate restTemplate = new RestTemplate();
 
    private final String apiCita        = "http://localhost:8080/api/cita";
    private final String apiCliente     = "http://localhost:8080/api/cliente";
    private final String apiMascota     = "http://localhost:8080/api/mascota";
    private final String apiServicio    = "http://localhost:8080/api/servicio";
    private final String apiVeterinario = "http://localhost:8080/api/veterinario";
 
    /** Caché de servicios reutilizado en cada instancia del formulario. */
    private List<Servicio> servicios = new ArrayList<>();
 
    // ─── Constructor ──────────────────────────────────────────────────────────
 
    public CtrlCitas(PnlCita pnlCita) {
        this.pnlCita = pnlCita;
        initTabla();
        cargarServicios();
        cargarTabla();
        addListeners();
    }
 
    // ─── Inicialización ───────────────────────────────────────────────────────
 
    private void initTabla() {
        pnlCita.getTblCita().setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Cliente", "Mascota", "Servicio", "Fecha", "Estado", "Acción"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; }
        });
        pnlCita.getTblCita().getColumnModel().getColumn(0).setMaxWidth(40);
        pnlCita.getTblCita().getColumnModel().getColumn(1).setPreferredWidth(130);
        pnlCita.getTblCita().getColumnModel().getColumn(2).setPreferredWidth(120);
        pnlCita.getTblCita().getColumnModel().getColumn(3).setPreferredWidth(120);
        pnlCita.getTblCita().getColumnModel().getColumn(4).setPreferredWidth(120);
        pnlCita.getTblCita().getColumnModel().getColumn(5).setPreferredWidth(90);
        pnlCita.getTblCita().getColumnModel().getColumn(6).setPreferredWidth(110);
        pnlCita.getTblCita().fixTable(pnlCita.getScrollPane());
    }
 
    private void addListeners() {
        pnlCita.getBtnBuscar().addActionListener(e -> cargarTabla());
        pnlCita.getBtnNuevo().addActionListener(e -> nuevo());
    }
 
    /** Obtiene los servicios disponibles y los almacena en caché. */
    private void cargarServicios() {
        try {
            List<Servicio> res = restTemplate.exchange(
                apiServicio + "/listar", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Servicio>>() {}
            ).getBody();
            if (res != null) servicios = res;
        } catch (Exception e) {
            servicios = new ArrayList<>();
        }
    }
 
    // ─── Tabla ────────────────────────────────────────────────────────────────
 
    private void cargarTabla() {
        try {
            List<Cita> citas = restTemplate.exchange(
                apiCita + "/listar", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Cita>>() {}
            ).getBody();
            llenarTabla(citas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlCita,
                "Error al cargar citas: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void llenarTabla(List<Cita> citas) {
        DefaultTableModel model = (DefaultTableModel) pnlCita.getTblCita().getModel();
        model.setRowCount(0);
        if (citas == null) return;
 
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
 
        for (Cita c : citas) {
            String nombreCliente  = c.getCliente()  != null ? c.getCliente().getNombre()          : "—";
            String nombreMascota  = c.getMascota()  != null ? c.getMascota().getNombre()           : "—";
            String nombreServicio = c.getServicio() != null ? c.getServicio().getNombreServicio()  : "—";
 
            EstadoCita estado = switch (c.getEstado()) {
                case "REALIZADA" -> EstadoCita.REALIZADA;
                case "CANCELADA" -> EstadoCita.CANCELADA;
                default          -> EstadoCita.PENDIENTE;
            };
 
            pnlCita.getTblCita().addRow(new Object[]{
                c.getIdCita(),
                nombreCliente,
                nombreMascota,
                nombreServicio,
                sdf.format(c.getFechaHora()),
                estado,
                new ModelAction(
                    () -> editar(c),
                    () -> ver(c),
                    () -> cancelar(c)
                )
            });
        }
    }
 
    // ─── Formulario ───────────────────────────────────────────────────────────
 
    /** Abre el formulario en modo alta con todos los campos vacíos. */
    private void nuevo() {
        form = new FormRegistroCita(parentFrame(), servicios);
        conectarForm(form);
        form.setVisible(true);
    }
 
    /** Abre el formulario en modo edición precargado con los datos de {@code cita}. */
    private void editar(Cita cita) {
        Cliente cliente = cita.getCliente();
        form = new FormRegistroCita(parentFrame(), cita, cliente, servicios);
 
        List<Mascota> mascotas = obtenerMascotasPorCliente(
            cliente != null ? cliente.getIdCliente() : -1);
        form.cargarMascotasDisponibles(mascotas);
 
        if (cita.getMascota() != null) {
            form.preseleccionarMascota(cita.getMascota().getIdMascota());
        }
 
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Registra los listeners del formulario dado.
     * <p>
     * Debe llamarse cada vez que se crea una nueva instancia del formulario,
     * tanto en modo alta como en modo edición.
     *
     * @param form instancia activa del formulario
     */
    private void conectarForm(FormRegistroCita form) {
        form.getBtnBuscarCliente().addActionListener(e -> buscarClientePorCedula(form.getTxtCedula().getText().trim()));
        form.getTxtCedula().addActionListener(e -> buscarClientePorCedula(form.getTxtCedula().getText().trim()));
 
        form.getCmbServicio().addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                filtrarVeterinariosPorServicio(form);
            }
        });
 
        form.getBtnAccion().addActionListener(e -> {
            String err = validarDatos(form);
            if (err != null) {
                JOptionPane.showMessageDialog(form, err, "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (form.isModoEdicion()) actualizar(form);
            else                      registrar(form);
        });
    }
 
    /**
     * Restablece todos los campos del formulario activo a su estado inicial.
     * Útil si se desea reutilizar la misma instancia entre operaciones.
     */
    private void limpiarForm() {
        form.getTxtCedula().setText("");
        form.setClienteSeleccionado(null);
        form.cargarMascotasDisponibles(new ArrayList<>());
        form.getCmbServicio().setSelectedIndex(-1);
        form.getCmbVeterinario().removeAllItems();
        form.getSpnHora().setValue(new Date());
        form.getTxtObservaciones().setText("");
    }
 
    /** Busca un cliente por cédula y carga sus mascotas en el combo correspondiente. */
    private void buscarClientePorCedula(String cedula) {
        
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(form,
                "Ingrese la cédula del cliente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Cliente cliente = restTemplate.getForObject(
                apiCliente + "/cedula/" + cedula, Cliente.class);
            form.setClienteSeleccionado(cliente);
            if (cliente != null) {
                List<Mascota> mascotas = obtenerMascotasPorCliente(cliente.getIdCliente());
                form.cargarMascotasDisponibles(mascotas);
            }
        } catch (Exception ex) {
            form.setClienteSeleccionado(null);
            JOptionPane.showMessageDialog(form,
                "Error al buscar cliente: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // ─── Veterinario ──────────────────────────────────────────────────────────
 
    /**
     * Actualiza el combo de veterinarios en función del servicio seleccionado.
     * En modo edición, reintenta preseleccionar al veterinario original.
     *
     * @param form instancia activa del formulario
     */
private void filtrarVeterinariosPorServicio(FormRegistroCita form) {
    Servicio seleccionado = (Servicio) form.getCmbServicio().getSelectedItem();
    if (seleccionado == null) {
        cargarVeterinarios(new ArrayList<>());
        return;
    }
    try {
        List<Veterinario> filtrados = restTemplate.exchange(
            apiVeterinario + "/servicio/" + seleccionado.getIdServicio(),
            HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Veterinario>>() {}
        ).getBody();

        if (filtrados != null && !filtrados.isEmpty()) {
            cargarVeterinarios(filtrados);
        } else {
            // NO HAY VETERINARIOS DISPONIBLES - Mostrar mensaje
            form.getCmbVeterinario().removeAllItems();
            // Agregar un ítem deshabilitado que indique que no hay disponibles
            Veterinario sinVeterinarios = new Veterinario();
            sinVeterinarios.setIdVeterinario(-1);
            sinVeterinarios.setNombre("️ No hay veterinarios disponibles");
            sinVeterinarios.setApellido("para este servicio");
            form.getCmbVeterinario().addItem(sinVeterinarios);
            form.getCmbVeterinario().setEnabled(false);
            
            // Mostrar mensaje al usuario
            JOptionPane.showMessageDialog(form,
                "No hay veterinarios disponibles para el servicio seleccionado.\n" +
                "Por favor, seleccione otro servicio o contacte al administrador.",
                "Sin veterinarios", JOptionPane.WARNING_MESSAGE);
        }

        if (form.isModoEdicion() && form.getCitaActual().getVeterinario() != null) {
            preseleccionarVeterinario(form.getCitaActual().getVeterinario().getIdVeterinario());
        }
    } catch (Exception e) {
        System.err.println("Error al filtrar veterinarios: " + e.getMessage());
        // En caso de error, mostrar mensaje también
        JOptionPane.showMessageDialog(form,
            "Error al cargar veterinarios: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
 
    /**
     * Puebla el combo de veterinarios del formulario activo.
     *
     * @param lista veterinarios a mostrar
     */
private void cargarVeterinarios(List<Veterinario> lista) {
    form.getCmbVeterinario().removeAllItems();
    form.getCmbVeterinario().setEnabled(true);  // Habilitar siempre que se carga
    for (Veterinario v : lista) {
        form.getCmbVeterinario().addItem(v);
    }
    if (lista.isEmpty()) {
        // Si la lista está vacía, agregar mensaje
        Veterinario sinVeterinarios = new Veterinario();
        sinVeterinarios.setIdVeterinario(-1);
        sinVeterinarios.setNombre("No hay veterinarios");
        sinVeterinarios.setApellido("disponibles");
        form.getCmbVeterinario().addItem(sinVeterinarios);
        form.getCmbVeterinario().setEnabled(false);
    }
    form.getCmbVeterinario().setSelectedIndex(-1);
}
 
    /**
     * Preselecciona en el combo de veterinarios del formulario activo
     * el elemento cuyo ID coincida con {@code idVeterinario}.
     *
     * @param idVeterinario identificador del veterinario a seleccionar
     */
    private void preseleccionarVeterinario(int idVeterinario) {
        JComboBox<Veterinario> cmb = form.getCmbVeterinario();
        for (int i = 0; i < cmb.getItemCount(); i++) {
            if (cmb.getItemAt(i).getIdVeterinario() == idVeterinario) {
                cmb.setSelectedIndex(i);
                return;
            }
        }
    }
 
    // ─── Validación y construcción ────────────────────────────────────────────
 
    /**
     * Valida los datos del formulario antes de enviarlos a la API.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error, o {@code null} si todo es correcto
     */
    private String validarDatos(FormRegistroCita form) {
        if (form.getClienteSeleccionado() == null)
            return "Ingrese la cédula y busque al cliente.";
        if (form.getCmbServicio().getSelectedItem() == null)
            return "Seleccione un servicio.";
        if (form.getCmbMascota().getSelectedItem() == null)
            return "Seleccione una mascota.";
        if (form.getFechaSeleccionada() == null)
            return "Seleccione una fecha.";
        if (getFechaHoraCombinada(form).before(new Date()))
            return "La fecha y hora no pueden ser en el pasado.";
        return null;
    }
 
    /** Combina la fecha del calendario y la hora del spinner en un único {@link Date}. */
    private Date getFechaHoraCombinada(FormRegistroCita form) {
        Calendar cf = Calendar.getInstance();
        Calendar ch = Calendar.getInstance();
        cf.setTime(form.getFechaSeleccionada());
        ch.setTime((Date) form.getSpnHora().getValue());
        cf.set(Calendar.HOUR_OF_DAY, ch.get(Calendar.HOUR_OF_DAY));
        cf.set(Calendar.MINUTE,      ch.get(Calendar.MINUTE));
        cf.set(Calendar.SECOND, 0);
        return cf.getTime();
    }
 
    /**
     * Construye (o actualiza) el objeto {@link Cita} a partir del estado actual del formulario.
     * <p>
     * Cuando la cita es nueva ({@code getCitaActual() == null}), inicializa las entidades
     * anidadas para evitar {@link NullPointerException}.
     *
     * @param form instancia activa del formulario
     * @return cita lista para enviar a la API
     */
    private Cita buildCita(FormRegistroCita form) {
        Cita c = form.getCitaActual() != null ? form.getCitaActual() : new Cita();
 
        if (c.getCliente()   == null) c.setCliente(new Cliente());
        if (c.getServicio()  == null) c.setServicio(new Servicio());
        if (c.getMascota()   == null) c.setMascota(new Mascota());
 
        c.getCliente() .setIdCliente (form.getClienteSeleccionado().getIdCliente());
        c.getServicio().setIdServicio(((Servicio) form.getCmbServicio().getSelectedItem()).getIdServicio());
        c.getMascota() .setIdMascota (((Mascota)  form.getCmbMascota() .getSelectedItem()).getIdMascota());
 
        if (form.getCmbVeterinario().getSelectedItem() != null) {
            if (c.getVeterinario() == null) c.setVeterinario(new Veterinario());
            c.getVeterinario().setIdVeterinario(
                ((Veterinario) form.getCmbVeterinario().getSelectedItem()).getIdVeterinario());
        }
 
        c.setFechaHora    (getFechaHoraCombinada(form));
        c.setEstado       ("PENDIENTE");
        c.setObservaciones(form.getTxtObservaciones().getText().trim());
        return c;
    }
 
    // ─── API ──────────────────────────────────────────────────────────────────
    
    private void registrar(FormRegistroCita form) {
        try {
            CitaRequest req = new CitaRequest();
            req.setCita(buildCita(form));
            restTemplate.postForObject(apiCita + "/agendar", req, Integer.class);
            form.dispose();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCita,
                "Error al registrar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex); //botrar despues
        }
    }
 
    private void actualizar(FormRegistroCita form) {
        try {
            CitaRequest req = new CitaRequest();
            req.setCita(buildCita(form));
            restTemplate.put(apiCita + "/actualizar", req);
            form.dispose();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCita,
                "Error al actualizar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void ver(Cita c) {
        // TODO: panel de detalle de cita
        System.out.println("Ver cita: " + c.getIdCita());
    }
 
    private void cancelar(Cita c) {
        String motivo = JOptionPane.showInputDialog(pnlCita, "Motivo de cancelación:");
        if (motivo == null || motivo.trim().isEmpty()) return;
        try {
            restTemplate.put(apiCita + "/cancelar/" + c.getIdCita()
                + "?motivo=" + motivo, null);
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnlCita,
                "Error al cancelar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // ─── Utilidades ───────────────────────────────────────────────────────────
 
    /**
     * Obtiene las mascotas registradas para un cliente.
     *
     * @param idCliente identificador del cliente
     * @return lista de mascotas, o lista vacía si falla la llamada
     */
    private List<Mascota> obtenerMascotasPorCliente(int idCliente) {
        if (idCliente < 0) return new ArrayList<>();
        try {
            List<Mascota> res = restTemplate.exchange(
                apiMascota + "/listar/" + idCliente,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Mascota>>() {}
            ).getBody();
            return res != null ? res : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
 
    /** Devuelve el {@link Frame} padre del panel de citas. */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlCita);
    }
}