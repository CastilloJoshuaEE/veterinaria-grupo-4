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
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellRender;
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
 * Coordina la carga de datos, el formulario de registro/edicion
 * y las operaciones REST sobre el recurso /api/cita.
 * Gestiona la programacion de citas, asignacion de veterinarios,
 * y el flujo completo de agenda de mascotas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CHILAN CHILAN DANNY ANDRES – MODULO: AGENDAMIENTO DE CITA
 * @version 1.0
 * @since 1.0
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
 
    /** Cache de servicios reutilizado en cada instancia del formulario. */
    private List<Servicio> servicios = new ArrayList<>();
 
    /**
     * Constructor del controlador de citas.
     * 
     * @param pnlCita panel principal de citas
     */
    public CtrlCitas(PnlCita pnlCita) {
        this.pnlCita = pnlCita;
        initTabla();
        cargarServicios();
        cargarTabla();
        addListeners();
    }
 
    /**
     * Inicializa la estructura de la tabla de citas.
     */
    private void initTabla() {
        pnlCita.getTblCita().setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Cliente", "Mascota", "Servicio", "Fecha", "Estado", "Accion"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; }
        });
        var col = pnlCita.getTblCita().getColumnModel();
        col.getColumn(0).setMaxWidth(40);
        col.getColumn(1).setPreferredWidth(130);
        col.getColumn(2).setPreferredWidth(120);
        col.getColumn(3).setPreferredWidth(120);
        col.getColumn(4).setPreferredWidth(120);
        col.getColumn(5).setPreferredWidth(90);
        col.getColumn(6).setPreferredWidth(110);
        
        int colAccion = 6; // El índice de la columna "Accion"
        col.getColumn(colAccion).setCellRenderer(new TableCellRender());
        col.getColumn(colAccion).setCellEditor(new TableCellAction());
        
        pnlCita.getTblCita().fixTable(pnlCita.getScrollPane());
    }
 
    /**
     * Registra los listeners del panel principal.
     */
    private void addListeners() {
        pnlCita.getBtnBuscar().addActionListener(e -> cargarTabla());
        pnlCita.getBtnNuevo().addActionListener(e -> nuevo());
    }
 
    /**
     * Obtiene los servicios disponibles y los almacena en cache.
     */
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
 
    /**
     * Carga todas las citas desde la API y las muestra en la tabla.
     */
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
 
    /**
     * Llena la tabla con los datos de las citas.
     * 
     * @param citas lista de citas a mostrar
     */
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
                new ModelAction()
                    .add(ModelAction.Tipo.EDITAR,   () -> editar(c))
                    .add(ModelAction.Tipo.VER,      () -> ver(c))
                    .add(ModelAction.Tipo.ELIMINAR, () -> cancelar(c)
                )
            });
        }
    }
 
    /**
     * Abre el formulario en modo alta con todos los campos vacios.
     */
    private void nuevo() {
        form = new FormRegistroCita(parentFrame(), servicios);
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Abre el formulario en modo edicion precargado con los datos de la cita.
     * 
     * @param cita cita a editar
     */
    private void editar(Cita cita) {
        Cliente cliente = cita.getCliente();
        form = new FormRegistroCita(parentFrame(), cita, cliente, servicios);
        // Deshabilitar busqueda       
        form.getBtnBuscarCliente().setVisible(false);
        form.getTxtCedula().setText(cliente.getCedula());
        form.getTxtCedula().setEnabled(false);
        // Cargar Mascotas
        List<Mascota> mascotas = obtenerMascotasPorCliente(
            cliente != null ? cliente.getIdCliente() : -1);
        form.cargarMascotasDisponibles(mascotas);
        if (cita.getMascota() != null) {
            form.preseleccionarMascota(cita.getMascota().getIdMascota());
        }
        filtrarVeterinariosPorServicio(form);
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Registra los listeners del formulario dado.
     * Debe llamarse cada vez que se crea una nueva instancia del formulario,
     * tanto en modo alta como en modo edicion.
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
                JOptionPane.showMessageDialog(form, err, "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (form.isModoEdicion()) actualizar(form);
            else                      registrar(form);
        });
    }
 
    /**
     * Busca un cliente por cedula y carga sus mascotas en el combo correspondiente.
     * 
     * @param cedula numero de cedula del cliente
     */
    private void buscarClientePorCedula(String cedula) {
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(form,
                "Ingrese la cedula del cliente.", "Busqueda", JOptionPane.WARNING_MESSAGE);
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
 
    /**
     * Actualiza el combo de veterinarios en funcion del servicio seleccionado.
     * En modo edicion, reintenta preseleccionar al veterinario original.
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
                form.getCmbVeterinario().removeAllItems();
                Veterinario sinVeterinarios = new Veterinario();
                sinVeterinarios.setIdVeterinario(-1);
                sinVeterinarios.setNombre(" No hay veterinarios disponibles");
                sinVeterinarios.setApellido("para este servicio");
                form.getCmbVeterinario().addItem(sinVeterinarios);
                form.getCmbVeterinario().setEnabled(false);

                JOptionPane.showMessageDialog(form,
                    "No hay veterinarios disponibles para el servicio seleccionado.\n" +
                    "Por favor, seleccione otro servicio o contacte al administrador.",
                    "Sin veterinarios", JOptionPane.WARNING_MESSAGE);
            }

            if (form.isModoEdicion() && form.getCitaActual().getVeterinario() != null) {
                int idServicioOriginal = form.getCitaActual().getServicio().getIdServicio();
                if (idServicioOriginal == seleccionado.getIdServicio()) {
                    preseleccionarVeterinario(form.getCitaActual().getVeterinario().getIdVeterinario());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al filtrar veterinarios: " + e.getMessage());
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
        form.getCmbVeterinario().setEnabled(true);
        for (Veterinario v : lista) {
            form.getCmbVeterinario().addItem(v);
        }
        if (lista.isEmpty()) {
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
     * el elemento cuyo ID coincida con el parametro.
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
 
    /**
     * Valida los datos del formulario antes de enviarlos a la API.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error, o null si todo es correcto
     */
    private String validarDatos(FormRegistroCita form) {
        if (form.getClienteSeleccionado() == null)
            return "Ingrese la cedula y busque al cliente.";
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
 
    /**
     * Combina la fecha del calendario y la hora del spinner en un unico Date.
     * 
     * @param form instancia activa del formulario
     * @return fecha y hora combinadas
     */
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
     * Construye (o actualiza) el objeto Cita a partir del estado actual del formulario.
     * Cuando la cita es nueva, inicializa las entidades anidadas.
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
 
    /**
     * Registra una nueva cita en el sistema.
     * 
     * @param form instancia activa del formulario
     */
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
            System.out.println(ex);
        }
    }
 
    /**
     * Actualiza una cita existente en el sistema.
     * 
     * @param form instancia activa del formulario
     */
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
 
    /**
     * Muestra los detalles de una cita.
     * 
     * @param c cita a visualizar
     */
    private void ver(Cita c) {
        // TODO: panel de detalle de cita
        System.out.println("Ver cita: " + c.getIdCita());
    }
 
    /**
     * Cancela una cita existente.
     * 
     * @param c cita a cancelar
     */
    private void cancelar(Cita c) {
        String motivo = JOptionPane.showInputDialog(pnlCita, "Motivo de cancelacion:");
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
 
    /**
     * Obtiene las mascotas registradas para un cliente.
     *
     * @param idCliente identificador del cliente
     * @return lista de mascotas, o lista vacia si falla la llamada
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
 
    /**
     * Devuelve el Frame padre del panel de citas.
     * 
     * @return Frame contenedor del panel
     */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlCita);
    }
}