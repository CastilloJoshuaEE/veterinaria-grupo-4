package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.view.atencionMedica.FormAtencionMedica;
import com.mycompany.veterinaria.grupo4.view.atencionMedica.PnlAtencionMedica;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;


public class CtrlAtencionMedica {
    private final PnlAtencionMedica pnlAtencion;
    private FormAtencionMedica form;
 
    private final RestTemplate restTemplate = new RestTemplate();
    private final String api = "http://localhost:8080/api";
 
    private static final SimpleDateFormat FMT_HORA = new SimpleDateFormat("HH:mm");
 
    // ─── Listas locales de prescripciones pendientes de guardar ───────────────
 
    /** Medicamento a recetar al crear la atención. */
    private record MedReceta(int idMedicamento, String nombre,
                              String dosis, String frecuencia, String duracion) {}
 
    /** Instrumento a registrar como usado al crear la atención. */
    private record InstrUsado(int idInstrumento, String nombre) {}
 
    private final List<MedReceta>  medicamentosReceta  = new ArrayList<>();
    private final List<InstrUsado> instrumentosUsados  = new ArrayList<>();
 
    // ─── Constructor ──────────────────────────────────────────────────────────
 
    public CtrlAtencionMedica(PnlAtencionMedica pnlAtencion) {
        this.pnlAtencion = pnlAtencion;
        initTabla();
        initPanel();
        cargarTabla();
    }
 
    // ─── Inicialización ───────────────────────────────────────────────────────
 
    private void initTabla() {
        pnlAtencion.getTblAtencionMedica().setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Hora", "Mascota", "Dueño", "Veterinario", "Servicio", "Acción"}
        ) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5; }
        });
 
        var col = pnlAtencion.getTblAtencionMedica().getColumnModel();
        col.getColumn(0).setPreferredWidth(55);
        col.getColumn(1).setPreferredWidth(110);
        col.getColumn(2).setPreferredWidth(140);
        col.getColumn(3).setPreferredWidth(140);
        col.getColumn(4).setPreferredWidth(120);
        col.getColumn(5).setPreferredWidth(110);
        pnlAtencion.getTblAtencionMedica().fixTable(pnlAtencion.getScrollPane());
    }
 
    /** Configura el botón "Nuevo" como "Actualizar" para refrescar la lista. */
    private void initPanel() {
        pnlAtencion.getBtnNuevo().setText("Actualizar");
        pnlAtencion.getBtnNuevo().addActionListener(e -> cargarTabla());
    }
 
    // ─── Tabla ────────────────────────────────────────────────────────────────
 
    private void cargarTabla() {
        try {
            List<Cita> pendientes = restTemplate.exchange(
                api + "/cita/pendientes", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Cita>>() {}
            ).getBody();
            llenarTabla(pendientes);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlAtencion,
                "Error al cargar citas pendientes: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void llenarTabla(List<Cita> citas) {
        DefaultTableModel model = (DefaultTableModel)
            pnlAtencion.getTblAtencionMedica().getModel();
        model.setRowCount(0);
        if (citas == null) return;
 
        for (Cita c : citas) {
            String hora = c.getFechaHora() != null
                ? FMT_HORA.format(c.getFechaHora()) : "—";
            String mascota = c.getMascota()    != null ? c.getMascota().getNombre()    : "—";
            String dueno   = c.getCliente()    != null
                ? c.getCliente().getNombre() + " " + c.getCliente().getApellido()     : "—";
            String vet     = c.getVeterinario() != null
                ? "Dr. " + c.getVeterinario().getNombre() + " " + c.getVeterinario().getApellido() : "—";
            String servicio = c.getServicio() != null ? c.getServicio().getNombreServicio() : "—";
 
            model.addRow(new Object[]{
                hora, mascota, dueno, vet, servicio,
                new ModelAction(
                    () -> atender(c),   // botón principal → Atender
                    null,
                    null
                )
            });
        }
    }
 
    // ─── Formulario de atención ───────────────────────────────────────────────
 
    /**
     * Abre el formulario de atención médica para la cita seleccionada.
     * Precarga medicamentos e instrumentos disponibles.
     *
     * @param cita cita pendiente a atender
     */
    private void atender(Cita cita) {
        medicamentosReceta.clear();
        instrumentosUsados.clear();
 
        form = new FormAtencionMedica(parentFrame(), cita);
        cargarCombosForm(form);
        conectarForm(form);
        form.setVisible(true);
    }
 
    /** Carga los combos de medicamentos e instrumentos del formulario. */
    private void cargarCombosForm(FormAtencionMedica form) {
        try {
            List<Medicamento> meds = restTemplate.exchange(
                api + "/medicamento/disponibles", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Medicamento>>() {}
            ).getBody();
            if (meds != null) form.cargarMedicamentos(meds);
        } catch (Exception e) {
            System.err.println("Error al cargar medicamentos: " + e.getMessage());
        }
 
        try {
            List<InstrumentoMedico> inst = restTemplate.exchange(
                api + "/instrumento/disponibles", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<InstrumentoMedico>>() {}
            ).getBody();
            if (inst != null) form.cargarInstrumentos(inst);
        } catch (Exception e) {
            System.err.println("Error al cargar instrumentos: " + e.getMessage());
        }
    }
 
    /**
     * Conecta los listeners del formulario de atención.
     *
     * @param form instancia activa del formulario
     */
    private void conectarForm(FormAtencionMedica form) {
        form.getBtnRecetar().addActionListener(e -> agregarMedicamento(form));
        form.getBtnUsarInstrumento().addActionListener(e -> agregarInstrumento(form));
        form.getBtnGuardar().addActionListener(e -> {
            String err = validarDatos(form);
            if (err != null) {
                JOptionPane.showMessageDialog(form, err,
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            guardar(form);
        });
    }
 
    // ─── Prescripciones ───────────────────────────────────────────────────────
 
    /**
     * Agrega el medicamento seleccionado a la lista local de recetas.
     * Valida que los campos de dosis, frecuencia y duración estén completos.
     *
     * @param form instancia activa del formulario
     */
    private void agregarMedicamento(FormAtencionMedica form) {
        Medicamento med = (Medicamento) form.getCmbMedicamentos().getSelectedItem();
        if (med == null) {
            JOptionPane.showMessageDialog(form,
                "Seleccione un medicamento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String dosis      = form.getTxtDosis().getText().trim();
        String frecuencia = form.getTxtFrecuencia().getText().trim();
        String duracion   = form.getTxtDuracion().getText().trim();
        if (dosis.isEmpty() || frecuencia.isEmpty() || duracion.isEmpty()) {
            JOptionPane.showMessageDialog(form,
                "Complete dosis, frecuencia y duración.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        MedReceta receta = new MedReceta(
            med.getIdMedicamento(), med.getNombre(), dosis, frecuencia, duracion);
        medicamentosReceta.add(receta);
 
        String resumen = med.getNombre() + " · " + dosis + " c/" + frecuencia + " · " + duracion;
        int idx = medicamentosReceta.size() - 1;
        form.agregarMedicamentoRecetado(resumen, () -> medicamentosReceta.remove(idx));
 
        // Limpiar campos de receta
        form.getCmbMedicamentos().setSelectedIndex(-1);
        form.getTxtDosis().setText("");
        form.getTxtFrecuencia().setText("");
        form.getTxtDuracion().setText("");
    }
 
    /**
     * Agrega el instrumento seleccionado a la lista local.
     *
     * @param form instancia activa del formulario
     */
    private void agregarInstrumento(FormAtencionMedica form) {
        InstrumentoMedico inst =
            (InstrumentoMedico) form.getCmbInstrumentos().getSelectedItem();
        if (inst == null) {
            JOptionPane.showMessageDialog(form,
                "Seleccione un instrumento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        InstrUsado usado = new InstrUsado(inst.getIdInstrumento(), inst.getNombre());
        instrumentosUsados.add(usado);
 
        int idx = instrumentosUsados.size() - 1;
        form.agregarInstrumentoUsado(inst.getNombre(), () -> instrumentosUsados.remove(idx));
        form.getCmbInstrumentos().setSelectedIndex(-1);
    }
 
    // ─── Validación ───────────────────────────────────────────────────────────
 
    /**
     * Valida los campos obligatorios antes de guardar.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error o {@code null} si todo es válido
     */
    private String validarDatos(FormAtencionMedica form) {
        if (form.getTxtDiagnostico().getText().trim().isEmpty())
            return "El diagnóstico es obligatorio.";
        if (form.getTxtTratamiento().getText().trim().isEmpty())
            return "El tratamiento es obligatorio.";
        return null;
    }
 
    // ─── Guardar — flujo completo ─────────────────────────────────────────────
 
    /**
     * Ejecuta el flujo completo de cierre de atención:
     * <ol>
     *   <li>Crea el registro de {@code ATENCION_MEDICA}.</li>
     *   <li>Persiste cada medicamento recetado.</li>
     *   <li>Persiste cada instrumento usado.</li>
     *   <li>El trigger cambia el estado de la cita a {@code REALIZADA} automáticamente.</li>
     *   <li>Solicita el método de pago y genera la factura.</li>
     * </ol>
     *
     * @param form instancia activa del formulario
     */
    private void guardar(FormAtencionMedica form) {
        try {
            // 1. Crear registro de atención médica
            AtencionMedica atencion = buildAtencion(form);
            int idAtencion = Optional.ofNullable(
                restTemplate.postForObject(
                    api + "/atencion-medica/crear", atencion, Integer.class)
            ).orElse(-1);
 
            if (idAtencion <= 0) {
                JOptionPane.showMessageDialog(form,
                    "No se pudo crear la atención médica.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
 
            // 2. Prescribir medicamentos
            for (MedReceta r : medicamentosReceta) {
                restTemplate.postForObject(
                    api + "/medicamento/recetar"
                        + "?idAtencionMedica=" + idAtencion
                        + "&idMedicamento="    + r.idMedicamento()
                        + "&dosis="            + r.dosis()
                        + "&frecuencia="       + r.frecuencia()
                        + "&duracion="         + r.duracion(),
                    null, Boolean.class);
            }
 
            // 3. Registrar instrumentos
            for (InstrUsado i : instrumentosUsados) {
                restTemplate.postForObject(
                    api + "/instrumento/usar"
                        + "?idAtencionMedica=" + idAtencion
                        + "&idInstrumento="    + i.idInstrumento(),
                    null, Boolean.class);
            }
 
            // 4. El trigger TR_ACTUALIZAR_ESTADO_CITA cambia la cita a REALIZADA automáticamente.
 
            // 5. Seleccionar método de pago y generar factura
            int idFactura = solicitarPagoYFacturar(form, idAtencion);
 
            if (idFactura > 0) {
                JOptionPane.showMessageDialog(pnlAtencion,
                    "Atención registrada correctamente.\nFactura Nº " + idFactura + " generada.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(pnlAtencion,
                    "Atención registrada. No se generó factura.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            }
 
            form.dispose();
            cargarTabla();
 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al guardar la atención: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Muestra un diálogo para seleccionar el método de pago y llama al endpoint
     * de generación de factura.
     *
     * @param form        formulario activo (para posicionamiento del diálogo)
     * @param idAtencion  ID de la atención recién creada
     * @return ID de la factura generada, o {@code -1} si se canceló
     */
    private int solicitarPagoYFacturar(FormAtencionMedica form, int idAtencion) {
        String[] metodos = {"EFECTIVO", "TRANSFERENCIA BANCARIA"};
        JComboBox<String> cmbMetodo = new JComboBox<>(metodos);
 
        int opcion = JOptionPane.showConfirmDialog(form,
            new Object[]{"Método de pago:", cmbMetodo},
            "Generar Factura", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
 
        if (opcion != JOptionPane.OK_OPTION) return -1;
 
        String metodo        = (String) cmbMetodo.getSelectedItem();
        String cuentaOrigen  = null;
        String cuentaDestino = null;
 
        if ("TRANSFERENCIA BANCARIA".equals(metodo)) {
            cuentaOrigen  = JOptionPane.showInputDialog(form, "Cuenta origen:");
            cuentaDestino = JOptionPane.showInputDialog(form, "Cuenta destino:");
            if (cuentaOrigen == null || cuentaDestino == null) return -1;
        }
 
        try {
            String url = api + "/factura/generar"
                + "?idAtencionMedica=" + idAtencion
                + "&metodoPago="       + metodo;
            if (cuentaOrigen  != null) url += "&cuentaOrigen="  + cuentaOrigen;
            if (cuentaDestino != null) url += "&cuentaDestino=" + cuentaDestino;
 
            return Optional.ofNullable(
                restTemplate.postForObject(url, null, Integer.class)
            ).orElse(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al generar factura: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
 
    // ─── Construcción de entidad ──────────────────────────────────────────────
 
    /**
     * Construye el objeto {@link AtencionMedica} desde el formulario y la cita activa.
     *
     * @param form instancia activa del formulario
     * @return atención lista para enviar a la API
     */
    private AtencionMedica buildAtencion(FormAtencionMedica form) {
        Cita cita = form.getCita();
        AtencionMedica a = new AtencionMedica();
        a.setIdCita(cita.getIdCita());
        a.setDiagnostico(form.getTxtDiagnostico().getText().trim());
        a.setTratamiento(form.getTxtTratamiento().getText().trim());
        a.setObservaciones(form.getTxtObservaciones().getText().trim());
        return a;
    }
 
    // ─── Utilidades ───────────────────────────────────────────────────────────
 
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlAtencion);
    }
}
