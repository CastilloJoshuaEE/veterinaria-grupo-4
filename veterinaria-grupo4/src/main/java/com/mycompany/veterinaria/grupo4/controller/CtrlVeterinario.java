package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.FormVeterinario;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.PnlVeterinario;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellRender;
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
 * Controlador para la gestion de veterinarios del sistema.
 * <p>
 * Gestiona la tabla de veterinarios, el formulario de registro/edicion,
 * y las operaciones REST sobre el recurso /api/veterinario.
 * Permite administrar el personal veterinario, sus especialidades
 * y los servicios que pueden realizar.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public class CtrlVeterinario {
    private final PnlVeterinario pnlVeterinario;
    private FormVeterinario form;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String api = "http://localhost:8080/api";
 
    /**
     * Mapa de servicios actualmente asignados al veterinario en el formulario.
     * Clave: idServicioVeterinario (FK de la tabla intermedia).
     * Valor: nombre del servicio para mostrarlo en la UI.
     * Se reconstruye cada vez que se abre el formulario de edicion.
     */
    private final Map<Integer, String> serviciosAsignados = new LinkedHashMap<>();
    
    /** Mapa auxiliar usado solo en modo alta para guardar los servicios seleccionados */
    private final Map<Integer, Integer> pendientesAlta = new LinkedHashMap<>();
 
    /**
     * Constructor del controlador de veterinarios.
     * 
     * @param pnlVeterinario panel principal de veterinarios
     */
    public CtrlVeterinario(PnlVeterinario pnlVeterinario) {
        this.pnlVeterinario = pnlVeterinario;
        initTabla();
        initBusqueda();
        cargarTabla();
        addListeners();
    }
 
    /**
     * Inicializa la estructura de la tabla de veterinarios.
     */
    private void initTabla() {
        pnlVeterinario.getTblVeterinario().setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Veterinario", "Cedula", "Especialidad", "Telefono", "Accion"}
        ) {
            @Override public boolean isCellEditable(int row, int col) { return col == 4; }
        });
 
        var col = pnlVeterinario.getTblVeterinario().getColumnModel();
        col.getColumn(0).setPreferredWidth(160);
        col.getColumn(1).setPreferredWidth(90);
        col.getColumn(2).setPreferredWidth(130);
        col.getColumn(3).setPreferredWidth(100);
        col.getColumn(4).setPreferredWidth(120);
        
        int colAccion = 4; // El índice de la columna "Accion"
        col.getColumn(colAccion).setCellRenderer(new TableCellRender());
        col.getColumn(colAccion).setCellEditor(new TableCellAction());
        
        pnlVeterinario.getTblVeterinario().fixTable(pnlVeterinario.getScrollPane());
    }
 
    /**
     * Configura el hint del campo de busqueda.
     */
    private void initBusqueda() {
        pnlVeterinario.getTxtBusqueda().setHint("Buscar por cedula o nombre...");
    }
 
    /**
     * Registra los listeners del panel principal.
     */
    private void addListeners() {
        pnlVeterinario.getBtnBuscar().addActionListener(e -> buscar());
        pnlVeterinario.getTxtBusqueda().addActionListener(e -> buscar());
        pnlVeterinario.getBtnNuevo().addActionListener(e -> nuevo());
    }
 
    /**
     * Carga todos los veterinarios desde la API.
     */
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
 
    /**
     * Llena la tabla con los datos de los veterinarios.
     * 
     * @param lista lista de veterinarios a mostrar
     */
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
                new ModelAction()
                    .add(ModelAction.Tipo.EDITAR,   () -> editar(v))
                    .add(ModelAction.Tipo.VER,      () -> ver(v))
                    .add(ModelAction.Tipo.ELIMINAR, () -> eliminar(v)
                )
            });
        }
    }
 
    /**
     * Busca veterinarios por termino de busqueda.
     */
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
 
    /**
     * Abre el formulario en modo alta con combos precargados.
     */
    private void nuevo() {
        serviciosAsignados.clear();
        form = new FormVeterinario(parentFrame());
        cargarCombos(form);
        conectarForm(form);
        form.setVisible(true);
    }
 
    /**
     * Abre el formulario en modo edicion con los datos del veterinario precargados,
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
        try {
            List<EspecialidadVeterinaria> especialidades = restTemplate.exchange(
                api + "/especialidad/listar", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<EspecialidadVeterinaria>>() {}
            ).getBody();
            if (especialidades != null) form.cargarEspecialidades(especialidades);
        } catch (Exception e) {
            System.err.println("Error al cargar especialidades: " + e.getMessage());
        }
 
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
     *
     * @param form instancia activa del formulario
     * @param idVeterinario ID del veterinario en edicion
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
                    "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (form.isModoEdicion()) actualizar(form);
            else                      registrar(form);
        });
    }
 
    /**
     * Asigna el servicio seleccionado en el combo al veterinario.
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
 
        if (serviciosAsignados.containsValue(seleccionado.getNombreServicio())) {
            JOptionPane.showMessageDialog(form,
                "El servicio ya esta asignado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        if (form.isModoEdicion()) {
            asignarServicioApi(form,
                seleccionado.getIdServicio(),
                form.getVeterinarioActual().getIdVeterinario(),
                seleccionado.getNombreServicio());
        } else {
            int claveTemp = -(serviciosAsignados.size() + 1);
            serviciosAsignados.put(claveTemp, seleccionado.getNombreServicio());
            pendientesAlta.put(claveTemp, seleccionado.getIdServicio());
            form.agregarServicioAsignado(seleccionado.getNombreServicio(),
                () -> eliminarLocal(form, claveTemp));
        }
 
        form.getCmbServiciosDisponibles().setSelectedIndex(-1);
    }
 
    /**
     * Llama a la API para asignar un servicio a un veterinario ya existente.
     */
    private void asignarServicioApi(FormVeterinario form, int idServicio,
                                    int idVeterinario, String nombreServicio) {
        try {
            boolean ok = Boolean.TRUE.equals(restTemplate.postForObject(
                api + "/servicio/asignar-veterinario?idServicio=" + idServicio
                    + "&idVeterinario=" + idVeterinario,
                null, Boolean.class));
 
            if (ok) {
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
     * Elimina una asignacion de la API y actualiza el panel.
     *
     * @param form instancia activa del formulario
     * @param idAsignacion ID de la fila en SERVICIO_VETERINARIO
     * @param idServicio ID del servicio (para refrescar la lista)
     */
    private void eliminarAsignacion(FormVeterinario form, int idAsignacion, int idServicio) {
        try {
            restTemplate.delete(api + "/servicio/eliminar-asignacion/" + idAsignacion);
            serviciosAsignados.remove(idAsignacion);
            cargarServiciosAsignados(form, form.getVeterinarioActual().getIdVeterinario());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al eliminar asignacion: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Elimina un servicio de la lista local en modo alta (sin persistir).
     */
    private void eliminarLocal(FormVeterinario form, int claveTemp) {
        serviciosAsignados.remove(claveTemp);
        pendientesAlta.remove(claveTemp);
        form.limpiarServiciosAsignados();
        for (Map.Entry<Integer, String> entry : serviciosAsignados.entrySet()) {
            int clave = entry.getKey();
            form.agregarServicioAsignado(entry.getValue(),
                () -> eliminarLocal(form, clave));
        }
    }
 
    /**
     * Valida los campos obligatorios del formulario.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error, o null si todo es valido
     */
    private String validarDatos(FormVeterinario form) {
        if (form.getTxtCedula().getText().trim().isEmpty())
            return "La cedula es obligatoria.";
        if (form.getTxtNombre().getText().trim().isEmpty())
            return "El nombre es obligatorio.";
        if (form.getTxtApellido().getText().trim().isEmpty())
            return "El apellido es obligatorio.";
        if (form.getTxtTelefono().getText().trim().isEmpty())
            return "El telefono es obligatorio.";
        if (form.getCmbEspecialidad().getSelectedItem() == null)
            return "Seleccione una especialidad.";
        return null;
    }
 
    /**
     * Construye el objeto Veterinario desde el estado actual del formulario.
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
 
    /**
     * Registra un nuevo veterinario en el sistema.
     * 
     * @param form formulario de veterinario
     */
    private void registrar(FormVeterinario form) {
        try {
            boolean ok = Boolean.TRUE.equals(restTemplate.postForObject(
                api + "/veterinario/crear", buildVeterinario(form), Boolean.class));
 
            if (ok) {
                Veterinario nuevo = restTemplate.getForObject(
                    api + "/veterinario/cedula/" + form.getTxtCedula().getText().trim(),
                    Veterinario.class);
 
                if (nuevo != null && !pendientesAlta.isEmpty()) {
                    pendientesAlta.forEach((clave, idServicio) ->
                        asignarServicioSilencioso(idServicio, nuevo.getIdVeterinario()));
                }
                pendientesAlta.clear();
 
                JOptionPane.showMessageDialog(pnlVeterinario,
                    "Veterinario registrado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                form.dispose();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(form,
                    "Ya existe un veterinario con esa cedula.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al registrar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Actualiza un veterinario existente.
     * 
     * @param form formulario de veterinario
     */
    private void actualizar(FormVeterinario form) {
        try {
            boolean ok = Boolean.TRUE.equals(restTemplate.postForObject(
                api + "/veterinario/actualizar", buildVeterinario(form), Boolean.class));
 
            if (ok) {
                JOptionPane.showMessageDialog(pnlVeterinario,
                    "Veterinario actualizado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                form.dispose();
                cargarTabla();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al actualizar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Asigna un servicio sin mostrar mensajes de error al usuario (usado en flujo de alta).
     */
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
 
    /**
     * Muestra los detalles de un veterinario.
     * 
     * @param v veterinario a visualizar
     */
    private void ver(Veterinario v) {
        // TODO: panel de detalle de veterinario
        System.out.println("Ver: " + v.getNombre());
    }
 
    /**
     * Elimina un veterinario del sistema.
     * 
     * @param v veterinario a eliminar
     */
    private void eliminar(Veterinario v) {
        int confirm = JOptionPane.showConfirmDialog(pnlVeterinario,
            "¿Eliminar al Dr. " + v.getNombre() + " " + v.getApellido() + "?\n"
            + "Se eliminaran tambien sus asignaciones de servicio.",
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
 
    /**
     * Devuelve el Frame padre del panel de veterinarios.
     * 
     * @return Frame contenedor
     */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlVeterinario);
    }
}