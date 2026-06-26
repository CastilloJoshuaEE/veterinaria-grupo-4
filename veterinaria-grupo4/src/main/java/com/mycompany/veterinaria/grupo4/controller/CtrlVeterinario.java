package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.EspecialidadVeterinaria;
import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.FormVeterinario;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.PnlVeterinario;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellRender;
import java.awt.Frame;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
/**
 * Controlador para la gestion de veterinarios del sistema.
 * <p>
 * Gestiona la tabla de veterinarios, el formulario de registro/edicion,
 * y las operaciones REST sobre el recurso /api/veterinario.
 * Permite administrar el personal veterinario, sus especialidades
 * y los servicios que pueden realizar.
 * </p>
 * * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public class CtrlVeterinario {
    private final PnlVeterinario pnlVeterinario;
    private FormVeterinario form;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String api = "http://localhost:8080/api";
    
    // Constantes para las columnas
    private static final int COL_NOMBRE = 0;
    private static final int COL_CEDULA = 1;
    private static final int COL_ESPECIALIDAD = 2;
    private static final int COL_TELEFONO = 3;
    private static final int COL_ACCION = 4;
    
    // Nombres de las columnas
    private static final String[] COLUMN_NAMES = {"Veterinario", "Cedula", "Especialidad", "Telefono", "Accion"};
    
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
     * * @param pnlVeterinario panel principal de veterinarios
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
        var tblVeterinario = pnlVeterinario.getTblVeterinario();
        if (tblVeterinario == null) {
            System.err.println("ERROR: La tabla de veterinarios es null");
            return;
        }
        
        tblVeterinario.setModel(new DefaultTableModel(new Object[][]{}, COLUMN_NAMES) {
            @Override 
            public boolean isCellEditable(int row, int col) { 
                return col == COL_ACCION; 
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == COL_ACCION) {
                    return ModelAction.class;
                }
                return String.class;
            }
        });

        var col = tblVeterinario.getColumnModel();
        if (col.getColumnCount() > COL_NOMBRE) col.getColumn(COL_NOMBRE).setPreferredWidth(160);
        if (col.getColumnCount() > COL_CEDULA) col.getColumn(COL_CEDULA).setPreferredWidth(90);
        if (col.getColumnCount() > COL_ESPECIALIDAD) col.getColumn(COL_ESPECIALIDAD).setPreferredWidth(130);
        if (col.getColumnCount() > COL_TELEFONO) col.getColumn(COL_TELEFONO).setPreferredWidth(100);
        if (col.getColumnCount() > COL_ACCION) {
            col.getColumn(COL_ACCION).setPreferredWidth(120);
            col.getColumn(COL_ACCION).setCellRenderer(new TableCellRender());
            col.getColumn(COL_ACCION).setCellEditor(new TableCellAction());
        }
        
        if (pnlVeterinario.getScrollPane() != null) {
            tblVeterinario.fixTable(pnlVeterinario.getScrollPane());
        }
    }

    /**
     * Configura el hint del campo de busqueda.
     */
    private void initBusqueda() {
        if (pnlVeterinario.getTxtBusqueda() != null) {
            pnlVeterinario.getTxtBusqueda().setHint("Buscar por cedula o nombre...");
        }
    }

    /**
     * Registra los listeners del panel principal.
     */
    private void addListeners() {
        if (pnlVeterinario.getBtnBuscar() != null) {
            pnlVeterinario.getBtnBuscar().addActionListener(e -> buscar());
        }
        if (pnlVeterinario.getTxtBusqueda() != null) {
            pnlVeterinario.getTxtBusqueda().addActionListener(e -> buscar());
        }
        if (pnlVeterinario.getBtnNuevo() != null) {
            pnlVeterinario.getBtnNuevo().addActionListener(e -> nuevo());
        }
    }

    /**
     * Obtiene la lista de veterinarios desde el API y llena la tabla.
     */
    private void cargarTabla() {
        try {
            var lista = restTemplate.exchange(
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
     * * @param lista lista de veterinarios a mostrar
     */
    private void llenarTabla(List<Veterinario> lista) {
        var tblVeterinario = pnlVeterinario.getTblVeterinario();
        if (tblVeterinario == null) { return; }
        
        DefaultTableModel model = (DefaultTableModel) tblVeterinario.getModel();
        model.setRowCount(0);
        if (lista == null || lista.isEmpty()) return;

        for (Veterinario v : lista) {
            String especialidad = (v.getEspecialidad() != null && v.getEspecialidad().getNombreEspecialidad() != null)
                ? v.getEspecialidad().getNombreEspecialidad() : "—";

            ModelAction acciones = new ModelAction()
                .add(ModelAction.Tipo.EDITAR,   () -> editar(v))
                .add(ModelAction.Tipo.VER,      () -> ver(v))
                .add(ModelAction.Tipo.ELIMINAR, () -> eliminar(v));
            
            model.addRow(new Object[]{
                v.getNombre() + " " + v.getApellido(),
                v.getCedula(),
                especialidad,
                v.getTelefono(),
                acciones
            });
        }
    }

   /**
     * Busca veterinarios por termino de busqueda.
     */
    private void buscar() {
        String termino = pnlVeterinario.getTxtBusqueda().getText().trim();
        if (termino.isEmpty()) {
            cargarTabla();
            return;
        }
        try {
            var lista = restTemplate.exchange(
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
        pendientesAlta.clear();
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
        pendientesAlta.clear();
        form = new FormVeterinario(parentFrame(), v);
        cargarCombos(form);
        form.rellenarCampos(v);
        if (v.getEspecialidad() != null && v.getEspecialidad().getIdEspecialidad() > 0) {
            form.preseleccionarEspecialidad(v.getEspecialidad().getIdEspecialidad());
        }
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
        if (form == null) return;
        
        try {
            List<EspecialidadVeterinaria> especialidades = restTemplate.exchange(
                api + "/especialidad/listar", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<EspecialidadVeterinaria>>() {}
            ).getBody();
            if (especialidades != null && !especialidades.isEmpty()) {
                form.cargarEspecialidades(especialidades);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar especialidades: " + e.getMessage());
        }

        try {
            List<Servicio> servicios = restTemplate.exchange(
                api + "/servicio/activos", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Servicio>>() {}
            ).getBody();
            if (servicios != null && !servicios.isEmpty()) {
                form.cargarServiciosDisponibles(servicios);
            }
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
        if (form == null) return;
        
        try {
            List<Servicio> asignados = restTemplate.exchange(
                api + "/servicio/veterinario/" + idVeterinario,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Servicio>>() {}
            ).getBody();

            if (asignados == null || asignados.isEmpty()) return;

            form.limpiarServiciosAsignados();
            serviciosAsignados.clear();

            for (Servicio s : asignados) {
                int idAsignacion = s.getIdServicio();
                serviciosAsignados.put(idAsignacion, s.getNombreServicio());
                int finalIdAsignacion = idAsignacion;
                int finalIdServicio = s.getIdServicio();
                form.agregarServicioAsignado(s.getNombreServicio(),
                    () -> eliminarAsignacion(form, finalIdAsignacion, finalIdServicio));
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
        if (form == null) return;
        
        if (form.getBtnAgregarServicio() != null) {
            form.getBtnAgregarServicio().addActionListener(e -> agregarServicio(form));
        }

        if (form.getBtnAccion() != null) {
            form.getBtnAccion().addActionListener(e -> {
                String err = validarDatos(form);
                if (err != null) {
                    JOptionPane.showMessageDialog(form, err,
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (form.isModoEdicion()) actualizar(form);
                else registrar(form);
            });
        }
    }

    /**
     * Asigna el servicio seleccionado en el combo al veterinario.
     *
     * @param form instancia activa del formulario
     */
    private void agregarServicio(FormVeterinario form) {
        if (form == null) return;
        
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
            int finalClaveTemp = claveTemp;
            form.agregarServicioAsignado(seleccionado.getNombreServicio(),
                () -> eliminarLocal(form, finalClaveTemp));
        }

        form.getCmbServiciosDisponibles().setSelectedIndex(-1);
    }

    /**
     * Llama a la API para asignar un servicio a un veterinario ya existente.
     */
    private void asignarServicioApi(FormVeterinario form, int idServicio,
                                    int idVeterinario, String nombreServicio) {
        if (form == null) return;
        
        try {
            Boolean ok = restTemplate.postForObject(
                api + "/servicio/asignar-veterinario?idServicio=" + idServicio
                    + "&idVeterinario=" + idVeterinario,
                null, Boolean.class);

            if (Boolean.TRUE.equals(ok)) {
                cargarServiciosAsignados(form, idVeterinario);
                JOptionPane.showMessageDialog(form,
                    "Servicio '" + nombreServicio + "' asignado correctamente.",
                    "Exito", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(form,
                    "El servicio ya estaba asignado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al asignar servicio: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
        if (form == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(form,
            "¿Desea eliminar este servicio del veterinario?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try {
            restTemplate.delete(api + "/servicio/eliminar-asignacion/" + idAsignacion);
            serviciosAsignados.remove(idAsignacion);
            cargarServiciosAsignados(form, form.getVeterinarioActual().getIdVeterinario());
            JOptionPane.showMessageDialog(form,
                "Servicio eliminado correctamente.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al eliminar asignacion: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Elimina un servicio de la lista local en modo alta (sin persistir).
     */
    private void eliminarLocal(FormVeterinario form, int claveTemp) {
        if (form == null) return;
        
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
        if (form == null) return "Formulario invalido";
        
        if (form.getTxtCedula().getText().trim().isEmpty())
            return "La cedula es obligatoria.";
        if (form.getTxtCedula().getText().trim().length() != 10)
            return "La cedula debe tener 10 digitos";
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
            try { 
                double pagoValue = Double.parseDouble(pago);
                if (pagoValue >= 0) {
                    v.setPagoMensual(pagoValue);
                }
            } catch (NumberFormatException ignored) {}
        }

        EspecialidadVeterinaria esp =
            (EspecialidadVeterinaria) form.getCmbEspecialidad().getSelectedItem();
        if (esp != null && esp.getIdEspecialidad() > 0) {
            v.setEspecialidad(esp);
        }

        return v;
    }

    /**
     * Registra un nuevo veterinario en el sistema.
     * * @param form formulario de veterinario
     */
   private void registrar(FormVeterinario form) {
    try {
        Boolean ok = restTemplate.postForObject(
            api + "/veterinario/crear",
            buildVeterinario(form),
            Boolean.class
        );

        if (Boolean.TRUE.equals(ok)) {

            // Buscar el veterinario recién creado por su cédula
            Veterinario nuevo = null;

            try {
                nuevo = restTemplate.getForObject(
                    api + "/veterinario/cedula/" + form.getTxtCedula().getText().trim(),
                    Veterinario.class
                );
            } catch (Exception ex) {
                System.err.println("Error al obtener veterinario creado: " + ex.getMessage());
            }

            if (nuevo != null && !pendientesAlta.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : pendientesAlta.entrySet()) {
                    asignarServicioSilencioso(
                        entry.getValue(),
                        nuevo.getIdVeterinario()
                    );
                }
            }

            pendientesAlta.clear();

            JOptionPane.showMessageDialog(
                pnlVeterinario,
                "Veterinario registrado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );

            form.dispose();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                form,
                "Ya existe un veterinario con esa cédula.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE
            );
        }

    } catch (HttpClientErrorException e) {

    JOptionPane.showMessageDialog(
        form,
        e.getResponseBodyAsString(),
        "Error",
        JOptionPane.ERROR_MESSAGE
    );

    e.printStackTrace();

} catch (HttpServerErrorException e) {

    JOptionPane.showMessageDialog(
        form,
        e.getResponseBodyAsString(),
        "Error del Servidor",
        JOptionPane.ERROR_MESSAGE
    );

    e.printStackTrace();


    } catch (Exception e) {

    e.printStackTrace();

    JOptionPane.showConfirmDialog(
        null,
        e.toString(),
        "ERROR DETECTADO",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.ERROR_MESSAGE
    );

    System.out.println("PRESIONA ENTER PARA CONTINUAR...");
    new java.util.Scanner(System.in).nextLine();
    }
}

    /**
     * Actualiza un veterinario existente usando PUT.
     * * @param form formulario de veterinario
     */
    private void actualizar(FormVeterinario form) {
    try {
        Veterinario veterinario = buildVeterinario(form);
        
        restTemplate.exchange(
            api + "/veterinario/actualizar",
            HttpMethod.PUT,
            new HttpEntity<>(veterinario),
            Boolean.class
        );

        JOptionPane.showMessageDialog(pnlVeterinario,
            "Veterinario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        form.dispose();
        cargarTabla();
        
    } catch (HttpClientErrorException e) {
        // Error 4xx - Mostrar mensaje al usuario, NO imprimir en consola
        String mensajeError;
        try {
            mensajeError = e.getResponseBodyAsString();
            if (mensajeError == null || mensajeError.isEmpty()) {
                mensajeError = e.getMessage();
            }
            if (mensajeError.startsWith("\"") && mensajeError.endsWith("\"")) {
                mensajeError = mensajeError.substring(1, mensajeError.length() - 1);
            }
        } catch (Exception ex) {
            mensajeError = "Error al actualizar el veterinario";
        }
        
        JOptionPane.showMessageDialog(form,
            "Error al actualizar: " + mensajeError,
            "Error", JOptionPane.ERROR_MESSAGE);
            
    } catch (HttpServerErrorException e) {
        // Error 5xx - Mostrar mensaje al usuario, NO imprimir en consola
        String mensajeError;
        try {
            mensajeError = e.getResponseBodyAsString();
            if (mensajeError == null || mensajeError.isEmpty()) {
                mensajeError = "Error interno del servidor";
            }
            if (mensajeError.startsWith("\"") && mensajeError.endsWith("\"")) {
                mensajeError = mensajeError.substring(1, mensajeError.length() - 1);
            }
        } catch (Exception ex) {
            mensajeError = "Error interno del servidor";
        }
        
        JOptionPane.showMessageDialog(form,
            "Error del servidor: " + mensajeError,
            "Error", JOptionPane.ERROR_MESSAGE);
            
    } catch (Exception e) {
        // Error genérico - Mostrar mensaje al usuario, NO imprimir en consola
        String mensaje = e.getMessage();
        JOptionPane.showMessageDialog(form,
            "Error al actualizar: " + (mensaje != null ? mensaje : "Error desconocido"),
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
     * * @param v veterinario a visualizar
     */
    private void ver(Veterinario v) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("ID: ").append(v.getIdVeterinario()).append("\n");
        mensaje.append("Nombre: ").append(v.getNombre()).append(" ").append(v.getApellido()).append("\n");
        mensaje.append("Cedula: ").append(v.getCedula()).append("\n");
        mensaje.append("Telefono: ").append(v.getTelefono()).append("\n");
        mensaje.append("Especialidad: ");
        if (v.getEspecialidad() != null) {
            mensaje.append(v.getEspecialidad().getNombreEspecialidad());
        } else {
            mensaje.append("No especificada");
        }
        mensaje.append("\n");
        mensaje.append("Pago Mensual: $").append(v.getPagoMensual()).append("\n");
        mensaje.append("Email: ").append(v.getCorreoElectronico() != null ? v.getCorreoElectronico() : "No registrado").append("\n");
        mensaje.append("Direccion: ").append(v.getDireccion() != null ? v.getDireccion() : "No registrada");
        
        JOptionPane.showMessageDialog(pnlVeterinario, mensaje.toString(),
            "Detalle del Veterinario", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Elimina un veterinario del sistema.
     * * @param v veterinario a eliminar
     */
    private void eliminar(Veterinario v) {
        int confirm = JOptionPane.showConfirmDialog(pnlVeterinario,
            "¿Eliminar al Dr. " + v.getNombre() + " " + v.getApellido() + "?\n"
            + "Se eliminaran tambien sus asignaciones de servicio.",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            restTemplate.delete(api + "/veterinario/eliminar/" + v.getIdVeterinario());
            JOptionPane.showMessageDialog(pnlVeterinario,
                "Veterinario eliminado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlVeterinario,
                "Error al eliminar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Devuelve el Frame padre del panel de veterinarios.
     * * @return Frame contenedor
     */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlVeterinario);
    }
}