package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.AtencionMedica;
import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.InstrumentoMedico;
import com.mycompany.veterinaria.grupo4.model.entity.Medicamento;
import com.mycompany.veterinaria.grupo4.view.atencionMedica.FormAtencionMedica;
import com.mycompany.veterinaria.grupo4.view.atencionMedica.PnlAtencionMedica;
import com.mycompany.veterinaria.grupo4.view.factura.frmDetalleFactura;
import com.mycompany.veterinaria.grupo4.view.factura.frmMetodoPago;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellRender;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador para la gestion de atenciones medicas.
 * <p>
 * Maneja el flujo completo de una atencion medica: desde la lista de citas
 * pendientes hasta el registro de la atencion, prescripcion de medicamentos,
 * uso de instrumentos y generacion de factura.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 1.0
 * @since 1.0
 */
public class CtrlAtencionMedica {
    private final PnlAtencionMedica pnlAtencion;
    private FormAtencionMedica form;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String api = "http://localhost:8080/api";
    private static final SimpleDateFormat FMT_HORA = new SimpleDateFormat("HH:mm");
 
    /** Medicamento a recetar al crear la atencion. */
    private record MedReceta(int idMedicamento, String nombre,
                              String dosis, String frecuencia, String duracion) {}
 
    /** Instrumento a registrar como usado al crear la atencion. */
    private record InstrUsado(int idInstrumento, String nombre) {}
 
    private final List<MedReceta>  medicamentosReceta  = new ArrayList<>();
    private final List<InstrUsado> instrumentosUsados  = new ArrayList<>();
 
    /**
     * Constructor del controlador de atencion medica.
     * 
     * @param pnlAtencion panel que contiene la tabla de citas pendientes
     */
    public CtrlAtencionMedica(PnlAtencionMedica pnlAtencion) {
        this.pnlAtencion = pnlAtencion;
        initTabla();
        initPanel();
        cargarTabla();
    }
 
    /**
     * Inicializa la estructura de la tabla de atenciones.
     */
    private void initTabla() {
        pnlAtencion.getTblAtencionMedica().setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Hora", "Mascota", "Dueño", "Veterinario", "Servicio", "Accion"}
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
        
        int colAccion = 5; // El índice de la columna "Accion"
        col.getColumn(colAccion).setCellRenderer(new TableCellRender());
        col.getColumn(colAccion).setCellEditor(new TableCellAction());
        
        pnlAtencion.getTblAtencionMedica().fixTable(pnlAtencion.getScrollPane());
    }
 
    /**
     * Configura el boton "Nuevo" como "Actualizar" para refrescar la lista.
     */
    private void initPanel() {
        pnlAtencion.getBtnNuevo().setText("Actualizar");
        pnlAtencion.getBtnNuevo().addActionListener(e -> cargarTabla());
    }
 
    /**
     * Carga las citas pendientes desde la API y las muestra en la tabla.
     */
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
 
    /**
     * Llena la tabla con los datos de las citas pendientes.
     * 
     * @param citas lista de citas a mostrar
     */
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
                new ModelAction().add(ModelAction.Tipo.ATENCION,()-> atender(c))
            });
        }
    }
 
    /**
     * Abre el formulario de atencion medica para la cita seleccionada.
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
 
    /**
     * Carga los combos de medicamentos e instrumentos del formulario.
     * 
     * @param form instancia del formulario de atencion medica
     */
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
     * Conecta los listeners del formulario de atencion.
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
                    "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            guardar(form);
        });
    }
 
    /**
     * Agrega el medicamento seleccionado a la lista local de recetas.
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
                "Complete dosis, frecuencia y duracion.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        MedReceta receta = new MedReceta(
            med.getIdMedicamento(), med.getNombre(), dosis, frecuencia, duracion);
        medicamentosReceta.add(receta);
 
        String resumen = med.getNombre() + " · " + dosis + " c/" + frecuencia + " · " + duracion;
        int idx = medicamentosReceta.size() - 1;
        form.agregarMedicamentoRecetado(resumen, () -> medicamentosReceta.remove(idx));
 
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
        InstrumentoMedico inst = (InstrumentoMedico) form.getCmbInstrumentos().getSelectedItem();
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
 
    /**
     * Valida los campos obligatorios antes de guardar.
     *
     * @param form instancia activa del formulario
     * @return mensaje de error o null si todo es valido
     */
private String validarDatos(FormAtencionMedica form) {
    String diagnostico = form.getTxtDiagnostico().getText().trim();
    if (diagnostico.isEmpty()) {
        return "El diagnostico es obligatorio.";
    }
    if (diagnostico.length() < 3) {
        return "El diagnostico debe tener al menos 3 caracteres.";
    }
    
    String tratamiento = form.getTxtTratamiento().getText().trim();
    if (tratamiento.isEmpty()) {
        return "El tratamiento es obligatorio.";
    }
    if (tratamiento.length() < 3) {
        return "El tratamiento debe tener al menos 3 caracteres.";
    }
    return null;
}
    /**
     * Ejecuta el flujo completo de cierre de atencion:
     * <ol>
     *   <li>Crea el registro de ATENCION_MEDICA.</li>
     *   <li>Persiste cada medicamento recetado.</li>
     *   <li>Persiste cada instrumento usado.</li>
     *   <li>El trigger cambia el estado de la cita a REALIZADA.</li>
     *   <li>Solicita el metodo de pago y genera la factura.</li>
     * </ol>
     *
     * @param form instancia activa del formulario
     */
    private void guardar(FormAtencionMedica form) {
        try {
            AtencionMedica atencion = buildAtencion(form);
            int idAtencion = Optional.ofNullable(
                restTemplate.postForObject(api + "/atencion-medica/crear", atencion, Integer.class)
            ).orElse(-1);
 
            if (idAtencion <= 0) {
                JOptionPane.showMessageDialog(form,
                    "No se pudo crear la atencion medica.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
 
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
 
            for (InstrUsado i : instrumentosUsados) {
                restTemplate.postForObject(
                    api + "/instrumento/usar"
                        + "?idAtencionMedica=" + idAtencion
                        + "&idInstrumento="    + i.idInstrumento(),
                    null, Boolean.class);
            }
 
            int idFactura = solicitarPagoYFacturarConDialogo(form, idAtencion);
 
            if (idFactura > 0) {
                JOptionPane.showMessageDialog(pnlAtencion,
                    "Atencion registrada correctamente.\nFactura Nº " + idFactura + " generada.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(pnlAtencion,
                    "Atencion registrada. No se genero factura.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            }
 
            form.dispose();
            cargarTabla();
 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al guardar la atencion: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    /**
     * Muestra el dialogo de metodo de pago y genera la factura.
     *
     * @param form        formulario activo (para posicionamiento del dialogo)
     * @param idAtencion  ID de la atencion recien creada
     * @return ID de la factura generada, o -1 si se cancelo
     */
    private int solicitarPagoYFacturarConDialogo(FormAtencionMedica form, int idAtencion) {
    // Calcular total a pagar
    double totalAPagar = calcularTotalAtencion(idAtencion);
    
    // Mostrar el diálogo de método de pago personalizado
    frmMetodoPago dialogPago = new frmMetodoPago(SwingUtilities.getWindowAncestor(form), totalAPagar);
    dialogPago.setVisible(true);
    
    if (!dialogPago.isConfirmed()) {
        eliminarAtencionMedica(idAtencion);
        return -1;
    }
    
    String metodoPago = dialogPago.getMetodoPago();
    String cuentaOrigen = dialogPago.getCuentaOrigen();
    String cuentaDestino = dialogPago.getCuentaDestino();
    
    try {
        String url = api + "/factura/generar?"
            + "idAtencionMedica=" + idAtencion
            + "&metodoPago=" + java.net.URLEncoder.encode(metodoPago, "UTF-8");
        if (cuentaOrigen != null && !cuentaOrigen.isEmpty()) {
            url += "&cuentaOrigen=" + java.net.URLEncoder.encode(cuentaOrigen, "UTF-8");
        }
        if (cuentaDestino != null && !cuentaDestino.isEmpty()) {
            url += "&cuentaDestino=" + java.net.URLEncoder.encode(cuentaDestino, "UTF-8");
        }
        
        System.out.println("URL: " + url); // DEBUG
        
        int idFactura = Optional.ofNullable(restTemplate.postForObject(url, null, Integer.class)).orElse(-1);
        
        if (idFactura > 0) {
            frmDetalleFactura detalleFactura = new frmDetalleFactura(
                SwingUtilities.getWindowAncestor(form), 
                idFactura
            );
            detalleFactura.setVisible(true);
        } else {
            // Mostrar error más específico
            JOptionPane.showMessageDialog(form,
                "No se pudo generar la factura. El procedimiento almacenado retornó -1.\n" +
                "Verifique que la atención médica tenga datos válidos.",
                "Error de facturación", JOptionPane.ERROR_MESSAGE);
        }
        
        return idFactura;
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(form,
            "Error al generar factura: " + e.getMessage() + "\n\n" +
            "StackTrace: " + getStackTraceAsString(e),
            "Error", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
}

private String getStackTraceAsString(Exception e) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
        sb.append(element.toString()).append("\n");
        if (sb.length() > 500) break;
    }
    return sb.toString();
}
 
    /**
     * Calcula el total a pagar de una atencion medica.
     *
     * @param idAtencion ID de la atencion medica
     * @return total a pagar (servicio + medicamentos + instrumentos + IVA)
     */
    private double calcularTotalAtencion(int idAtencion) {
        double total = 0;
        
        try {
            // Obtener precio del servicio desde la cita
            AtencionMedica atencion = restTemplate.getForObject(
                api + "/atencion-medica/" + idAtencion, AtencionMedica.class);
            
            if (atencion != null && atencion.getIdCita() > 0) {
                Cita cita = restTemplate.getForObject(
                    api + "/cita/" + atencion.getIdCita(), Cita.class);
                if (cita != null && cita.getServicio() != null) {
                    total += cita.getServicio().getPrecio();
                }
            }
            
            // Sumar precio de medicamentos recetados
            List<Medicamento> medicamentos = restTemplate.exchange(
                api + "/medicamento/atencion/" + idAtencion, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Medicamento>>() {}
            ).getBody();
            if (medicamentos != null) {
                for (Medicamento m : medicamentos) {
                    total += m.getPrecio();
                }
            }
            
            // Sumar costo de instrumentos usados
            List<InstrumentoMedico> instrumentos = restTemplate.exchange(
                api + "/instrumento/atencion/" + idAtencion, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<InstrumentoMedico>>() {}
            ).getBody();
            if (instrumentos != null) {
                for (InstrumentoMedico i : instrumentos) {
                    total += i.getCostoUso();
                }
            }
            
            // Agregar IVA (12%)
            double subtotal = total;
            total = subtotal * 1.12;
            
        } catch (Exception e) {
            System.err.println("Error al calcular total: " + e.getMessage());
        }
        
        return total;
    }
 
    /**
     * Elimina una atencion medica si se cancela el pago.
     *
     * @param idAtencion ID de la atencion a eliminar
     */
    private void eliminarAtencionMedica(int idAtencion) {
        try {
            restTemplate.delete(api + "/atencion-medica/eliminar/" + idAtencion);
            JOptionPane.showMessageDialog(form,
                "Operacion cancelada. No se registro la atencion medica.",
                "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error al eliminar atencion medica: " + e.getMessage());
        }
    }
 
    /**
     * Construye el objeto AtencionMedica desde el formulario y la cita activa.
     *
     * @param form instancia activa del formulario
     * @return atencion lista para enviar a la API
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
 
    /**
     * Devuelve el Frame padre del panel de atenciones.
     * 
     * @return Frame contenedor del panel
     */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlAtencion);
    }
}