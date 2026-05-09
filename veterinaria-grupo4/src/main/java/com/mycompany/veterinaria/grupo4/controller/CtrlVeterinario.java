package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.FormVeterinario;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.PnlVeterinario;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import java.awt.Frame;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author juan
 */
public class CtrlVeterinario {
    private final PnlVeterinario pnlVeterinario;
    private FormVeterinario form;
 
    private final RestTemplate restTemplate = new RestTemplate();
    private final String api = "http://localhost:8080/api";
 
    /**
     * Mapa de servicios actualmente asignados al veterinario en el formulario.
     * Clave: {@code idServicioVeterinario} (FK de la tabla intermedia).
     * Valor: nombre del servicio para mostrarlo en la UI.
     * Se reconstruye cada vez que se abre el formulario de edición.
     */
    private final Map<Integer, String> serviciosAsignados = new LinkedHashMap<>();
 
    // ─── Constructor ──────────────────────────────────────────────────────────
 
    public CtrlVeterinario(PnlVeterinario pnlVeterinario) {
        this.pnlVeterinario = pnlVeterinario;
        initTabla();
        initBusqueda();
        cargarTabla();
        addListeners();
    }
 
    // ─── Inicialización ───────────────────────────────────────────────────────
 
    private void initTabla() {
        pnlVeterinario.getTblVeterinario().setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Veterinario", "Cédula", "Especialidad", "Teléfono", "Acción"}
        ) {
            @Override public boolean isCellEditable(int row, int col) { return col == 4; }
        });
 
        var col = pnlVeterinario.getTblVeterinario().getColumnModel();
        col.getColumn(0).setPreferredWidth(160);
        col.getColumn(1).setPreferredWidth(90);
        col.getColumn(2).setPreferredWidth(130);
        col.getColumn(3).setPreferredWidth(100);
        col.getColumn(4).setPreferredWidth(120);
        pnlVeterinario.getTblVeterinario().fixTable(pnlVeterinario.getScrollPane());
    }
 
    /** Configura el hint del campo de búsqueda. */
    private void initBusqueda() {
        pnlVeterinario.getTxtBusqueda().setHint("Buscar por cédula o nombre...");
    }
 
    private void addListeners() {
        pnlVeterinario.getBtnBuscar().addActionListener(e -> buscar());
        pnlVeterinario.getTxtBusqueda().addActionListener(e -> buscar());
        pnlVeterinario.getBtnNuevo().addActionListener(e -> nuevo());
    }
 
    // ─── Tabla ────────────────────────────────────────────────────────────────
 
    private void cargarTabla() {
        try {
            List<Veterinario> lista = restTemplate.exchange(
                api + "/veterinario/listar", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Veterinario>>() {}
            ).getBody();
            llenarTabla(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlVeterinario,
                "Error al cargar veterinarios: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void llenarTabla(List<Veterinario> lista) {
        DefaultTableModel model = (DefaultTableModel)
            pnlVeterinario.getTblVeterinario().getModel();
        model.setRowCount(0);
        if (lista == null) return;
 
        for (Veterinario v : lista) {
            String especialidad = v.getEspecialidad() != null
                ? v.getEspecialidad().getNombreEspecialidad() : "—";
 
            model.addRow(new Object[]{
                v.getNombre() + " " + v.getApellido(),
                v.getCedula(),
                especialidad,
                v.getTelefono(),
                new ModelAction(
                    () -> editar(v),
                    () -> ver(v),
                    () -> eliminar(v)
                )
            });
        }
    }
 
    private void buscar() {
        String termino = pnlVeterinario.getTxtBusqueda().getText().trim();
        if (termino.isEmpty()) { cargarTabla(); return; }
        try {
            List<Veterinario> lista = restTemplate.exchange(
                api + "/veterinario/buscar?termino=" + termino,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Veterinario>>() {}
            ).getBody();
            llenarTabla(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlVeterinario,
                "Error al buscar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // ─── Formulario ───────────────────────────────────────────────────────────
 
    /** Abre el formulario en modo alta con combos precargados. */
    private void nuevo() {
        serviciosAsignados.clear();
        form = new FormVeterinario(parentFrame());
        cargarCombos(form);
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Abre el formulario en modo edición con los datos del veterinario precargados,
     * incluyendo los servicios que ya tiene asignados.
     *
     * @param v veterinario a editar
     */
    private void editar(Veterinario v) {
        serviciosAsignados.clear();
        form = new FormVeterinario(parentFrame(), v);
        cargarCombos(form);
        form.rellenarCampos(v);
        if (v.getEspecialidad() != null)
            form.preseleccionarEspecialidad(v.getEspecialidad().getIdEspecialidad());
        cargarServiciosAsignados(form, v.getIdVeterinario());
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Carga las especialidades y los servicios activos en los combos del formulario.
     *
     * @param form instancia activa del formulario
     */
    private void cargarCombos(FormVeterinario form) {
        // Especialidades
        try {
            List<EspecialidadVeterinaria> especialidades = restTemplate.exchange(
                api + "/especialidad/listar", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<EspecialidadVeterinaria>>() {}
            ).getBody();
            if (especialidades != null) form.cargarEspecialidades(especialidades);
        } catch (Exception e) {
            System.err.println("Error al cargar especialidades: " + e.getMessage());
        }
 
        // Servicios disponibles
        try {
            List<Servicio> servicios = restTemplate.exchange(
                api + "/servicio/activos", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Servicio>>() {}
            ).getBody();
            if (servicios != null) form.cargarServiciosDisponibles(servicios);
        } catch (Exception e) {
            System.err.println("Error al cargar servicios: " + e.getMessage());
        }
    }
 
    /**
     * Obtiene los servicios ya asignados al veterinario y los muestra en el panel.
     * <p>
     * Nota: el endpoint {@code GET /api/servicio/veterinario/{id}} debe devolver
     * la lista de servicios con el campo {@code idServicioVeterinario} poblado
     * (columna de la tabla intermedia), necesario para poder eliminar la asignación.
     *
     * @param form          instancia activa del formulario
     * @param idVeterinario ID del veterinario en edición
     */
    private void cargarServiciosAsignados(FormVeterinario form, int idVeterinario) {
        try {
            List<Servicio> asignados = restTemplate.exchange(
                api + "/servicio/veterinario/" + idVeterinario,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Servicio>>() {}
            ).getBody();
 
            if (asignados == null) return;
 
            form.limpiarServiciosAsignados();
            serviciosAsignados.clear();
 
            for (Servicio s : asignados) {
                // idServicioVeterinario debe venir mapeado en el entity/DTO
                int idAsignacion = s.getIdServicio();
                serviciosAsignados.put(idAsignacion, s.getNombreServicio());
                form.agregarServicioAsignado(s.getNombreServicio(),
                    () -> eliminarAsignacion(form, idAsignacion, s.getIdServicio()));
            }
        } catch (Exception e) {
            System.err.println("Error al cargar servicios asignados: " + e.getMessage());
        }
    }
 
    /**
     * Conecta los listeners del formulario activo.
     *
     * @param form instancia activa del formulario
     */
    private void conectarForm(FormVeterinario form) {
        form.getBtnAgregarServicio().addActionListener(e -> agregarServicio(form));
 
        form.getBtnAccion().addActionListener(e -> {
            String err = validarDatos(form);
            if (err != null) {
                JOptionPane.showMessageDialog(form, err,
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (form.isModoEdicion()) actualizar(form);
            else                      registrar(form);
        });
    }
 
    // ─── Asignación de servicios ──────────────────────────────────────────────
 
    /**
     * Asigna el servicio seleccionado en el combo al veterinario.
     * En modo alta la asignación se encola y se persiste al guardar;
     * en modo edición se persiste de inmediato si el veterinario ya tiene ID.
     *
     * @param form instancia activa del formulario
     */
    private void agregarServicio(FormVeterinario form) {
        Servicio seleccionado = (Servicio) form.getCmbServiciosDisponibles().getSelectedItem();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(form,
                "Seleccione un servicio de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        // Verificar duplicado en la lista local
        if (serviciosAsignados.containsValue(seleccionado.getNombreServicio())) {
            JOptionPane.showMessageDialog(form,
                "El servicio ya está asignado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        if (form.isModoEdicion()) {
            // Persistir de inmediato
            asignarServicioApi(form,
                seleccionado.getIdServicio(),
                form.getVeterinarioActual().getIdVeterinario(),
                seleccionado.getNombreServicio());
        } else {
            // En alta aún no hay ID de veterinario; se guarda localmente con clave negativa temporal
            int claveTemp = -(serviciosAsignados.size() + 1);
            serviciosAsignados.put(claveTemp, seleccionado.getNombreServicio());
            // Guardamos el idServicio real en la clave positiva para usarlo al registrar
            pendientesAlta.put(claveTemp, seleccionado.getIdServicio());
            form.agregarServicioAsignado(seleccionado.getNombreServicio(),
                () -> eliminarLocal(form, claveTemp));
        }
 
        form.getCmbServiciosDisponibles().setSelectedIndex(-1);
    }
 
    /**
     * Mapa auxiliar usado solo en modo alta para guardar los servicios seleccionados
     * antes de que exista el {@code idVeterinario}.
     * Clave: clave temporal negativa. Valor: {@code idServicio} real.
     */
    private final Map<Integer, Integer> pendientesAlta = new LinkedHashMap<>();
 
    /** Llama a la API para asignar un servicio a un veterinario ya existente. */
    private void asignarServicioApi(FormVeterinario form, int idServicio,
                                    int idVeterinario, String nombreServicio) {
        try {
            boolean ok = Boolean.TRUE.equals(restTemplate.postForObject(
                api + "/servicio/asignar-veterinario?idServicio=" + idServicio
                    + "&idVeterinario=" + idVeterinario,
                null, Boolean.class));
 
            if (ok) {
                // Recargar para obtener el idServicioVeterinario real
                cargarServiciosAsignados(form, idVeterinario);
            } else {
                JOptionPane.showMessageDialog(form,
                    "El servicio ya estaba asignado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al asignar servicio: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Elimina una asignación de la API y actualiza el panel.
     *
     * @param form          instancia activa del formulario
     * @param idAsignacion  ID de la fila en SERVICIO_VETERINARIO
     * @param idServicio    ID del servicio (para refrescar la lista)
     */
    private void eliminarAsignacion(FormVeterinario form, int idAsignacion, int idServicio) {
        try {
            restTemplate.delete(api + "/servicio/eliminar-asignacion/" + idAsignacion);
            serviciosAsignados.remove(idAsignacion);
            cargarServiciosAsignados(form, form.getVeterinarioActual().getIdVeterinario());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al eliminar asignación: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /** Elimina un servicio de la lista local en modo alta (sin persistir). */
    private void eliminarLocal(FormVeterinario form, int claveTemp) {
        serviciosAsignados.remove(claveTemp);
        pendientesAlta.remove(claveTemp);
        // Reconstruir el panel desde el mapa local actualizado
        form.limpiarServiciosAsignados();
        for (Map.Entry<Integer, String> entry : serviciosAsignados.entrySet()) {
            int clave = entry.getKey();
            form.agregarServicioAsignado(entry.getValue(),
                () -> eliminarLocal(form, clave));
        }
    }
 
    // ─── Validación y construcción ────────────────────────────────────────────
 
    /**
     * Valida los campos obligatorios del formulario.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error, o {@code null} si todo es válido
     */
    private String validarDatos(FormVeterinario form) {
        if (form.getTxtCedula().getText().trim().isEmpty())
            return "La cédula es obligatoria.";
        if (form.getTxtNombre().getText().trim().isEmpty())
            return "El nombre es obligatorio.";
        if (form.getTxtApellido().getText().trim().isEmpty())
            return "El apellido es obligatorio.";
        if (form.getTxtTelefono().getText().trim().isEmpty())
            return "El teléfono es obligatorio.";
        if (form.getCmbEspecialidad().getSelectedItem() == null)
            return "Seleccione una especialidad.";
        return null;
    }
 
    /**
     * Construye el objeto {@link Veterinario} desde el estado actual del formulario.
     *
     * @param form instancia activa del formulario
     * @return veterinario listo para enviar a la API
     */
    private Veterinario buildVeterinario(FormVeterinario form) {
        Veterinario v = form.isModoEdicion() ? form.getVeterinarioActual() : new Veterinario();
 
        v.setCedula(form.getTxtCedula().getText().trim());
        v.setNombre(form.getTxtNombre().getText().trim());
        v.setApellido(form.getTxtApellido().getText().trim());
        v.setTelefono(form.getTxtTelefono().getText().trim());
        v.setCorreoElectronico(form.getTxtCorreo().getText().trim());
        v.setDireccion(form.getTxtDireccion().getText().trim());
 
        String pago = form.getTxtPagoMensual().getText().trim();
        if (!pago.isEmpty()) {
            try { v.setPagoMensual(Double.parseDouble(pago)); }
            catch (NumberFormatException ignored) {}
        }
 
        EspecialidadVeterinaria esp =
            (EspecialidadVeterinaria) form.getCmbEspecialidad().getSelectedItem();
        if (esp != null) v.setEspecialidad(esp);
 
        return v;
    }
 
    // ─── API ──────────────────────────────────────────────────────────────────
 
    private void registrar(FormVeterinario form) {
        try {
            boolean ok = Boolean.TRUE.equals(restTemplate.postForObject(
                api + "/veterinario/crear", buildVeterinario(form), Boolean.class));
 
            if (ok) {
                // Asignar servicios pendientes: necesitamos el nuevo ID
                // Obtenemos el veterinario recién creado por cédula para recuperar su ID
                Veterinario nuevo = restTemplate.getForObject(
                    api + "/veterinario/cedula/" + form.getTxtCedula().getText().trim(),
                    Veterinario.class);
 
                if (nuevo != null && !pendientesAlta.isEmpty()) {
                    pendientesAlta.forEach((clave, idServicio) ->
                        asignarServicioSilencioso(idServicio, nuevo.getIdVeterinario()));
                }
                pendientesAlta.clear();
 
                JOptionPane.showMessageDialog(pnlVeterinario,
                    "Veterinario registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                form.dispose();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(form,
                    "Ya existe un veterinario con esa cédula.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al registrar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void actualizar(FormVeterinario form) {
        try {
            boolean ok = Boolean.TRUE.equals(restTemplate.postForObject(
                api + "/veterinario/actualizar", buildVeterinario(form), Boolean.class));
 
            if (ok) {
                JOptionPane.showMessageDialog(pnlVeterinario,
                    "Veterinario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                form.dispose();
                cargarTabla();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al actualizar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /** Asigna un servicio sin mostrar mensajes de error al usuario (usado en flujo de alta). */
    private void asignarServicioSilencioso(int idServicio, int idVeterinario) {
        try {
            restTemplate.postForObject(
                api + "/servicio/asignar-veterinario?idServicio=" + idServicio
                    + "&idVeterinario=" + idVeterinario,
                null, Boolean.class);
        } catch (Exception e) {
            System.err.println("Error al asignar servicio " + idServicio + ": " + e.getMessage());
        }
    }
 
    private void ver(Veterinario v) {
        // TODO: panel de detalle de veterinario
        System.out.println("Ver: " + v.getNombre());
    }
 
    private void eliminar(Veterinario v) {
        int confirm = JOptionPane.showConfirmDialog(pnlVeterinario,
            "¿Eliminar al Dr. " + v.getNombre() + " " + v.getApellido() + "?\n"
            + "Se eliminarán también sus asignaciones de servicio.",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            restTemplate.delete(api + "/veterinario/eliminar/" + v.getIdVeterinario());
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlVeterinario,
                "Error al eliminar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // ─── Utilidades ───────────────────────────────────────────────────────────
 
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlVeterinario);
    }
}
