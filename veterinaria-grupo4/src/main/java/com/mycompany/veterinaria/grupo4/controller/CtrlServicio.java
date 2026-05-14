package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.model.entity.Servicio;
import com.mycompany.veterinaria.grupo4.model.entity.Veterinario;
import com.mycompany.veterinaria.grupo4.view.servicio.FormServicio;
import com.mycompany.veterinaria.grupo4.view.servicio.PnlServicio;
import com.mycompany.veterinaria.grupo4.view.swing.table.ModelAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellAction;
import com.mycompany.veterinaria.grupo4.view.swing.table.TableCellRender;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador para la gestion de servicios veterinarios.
 * <p>
 * Gestiona la tabla de servicios, el formulario de registro/edicion,
 * y las operaciones REST sobre el recurso /api/servicio.
 * Permite administrar los servicios ofrecidos por la clinica,
 * asi como asignar veterinarios a cada servicio.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author BESILLA TOMALA ANGEL KALED – MODULO: VETERINARIO
 * @version 1.0
 * @since 1.0
 */
public class CtrlServicio {
    private final PnlServicio pnlServicio;
    private FormServicio form;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String api = "http://localhost:8080/api";

    /**
     * Constructor del controlador de servicios.
     * 
     * @param pnlServicio panel principal de servicios
     */
    public CtrlServicio(PnlServicio pnlServicio) {
        this.pnlServicio = pnlServicio;
        initTabla();
        initBusqueda();
        cargarTabla();
        addListeners();
    }

    /**
     * Inicializa la estructura de la tabla de servicios.
     */
    private void initTabla() {
        pnlServicio.getTblServicio().setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nombre", "Precio", "Duracion", "Estado", "Accion"}
        ) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5; }
        });

        var col = pnlServicio.getTblServicio().getColumnModel();
        col.getColumn(0).setMinWidth(0);
        col.getColumn(0).setMaxWidth(0);
        col.getColumn(1).setPreferredWidth(180);
        col.getColumn(2).setPreferredWidth(80);
        col.getColumn(3).setPreferredWidth(80);
        col.getColumn(4).setPreferredWidth(80);
        col.getColumn(5).setPreferredWidth(100);
        
        int colAccion = 5; // El índice de la columna "Accion"
        col.getColumn(colAccion).setCellRenderer(new TableCellRender());
        col.getColumn(colAccion).setCellEditor(new TableCellAction());
        
        pnlServicio.getTblServicio().fixTable(pnlServicio.getScrollPane());
    }

    /**
     * Configura el hint del campo de busqueda.
     */
    private void initBusqueda() {
        pnlServicio.getTxtBusqueda().setHint("Buscar por nombre...");
    }

    /**
     * Registra los listeners del panel principal.
     */
    private void addListeners() {
        pnlServicio.getBtnBuscar().addActionListener(e -> buscar());
        pnlServicio.getTxtBusqueda().addActionListener(e -> buscar());
        pnlServicio.getBtnNuevo().addActionListener(e -> nuevo());
    }

    /**
     * Carga todos los servicios desde la API.
     */
    private void cargarTabla() {
        try {
            List<Servicio> lista = restTemplate.exchange(
                api + "/servicio/listar", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Servicio>>() {}
            ).getBody();
            llenarTabla(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlServicio,
                "Error al cargar servicios: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llena la tabla con los datos de los servicios.
     * 
     * @param lista lista de servicios a mostrar
     */
    private void llenarTabla(List<Servicio> lista) {
        DefaultTableModel model = (DefaultTableModel)
            pnlServicio.getTblServicio().getModel();
        model.setRowCount(0);
        if (lista == null) return;

        for (Servicio s : lista) {
            model.addRow(new Object[]{
                s.getIdServicio(),
                s.getNombreServicio(),
                s.getPrecio(),
                s.getDuracionEstimada(),
                s.isEstado() ? "Activo" : "Inactivo",
                new ModelAction()
                    .add(ModelAction.Tipo.EDITAR,   () -> editar(s))
                    .add(ModelAction.Tipo.VER,      () -> ver(s))
                    .add(ModelAction.Tipo.ELIMINAR, () -> eliminar(s)
                )
            });
        }
    }

    /**
     * Busca servicios por termino de busqueda.
     */
    private void buscar() {
        String termino = pnlServicio.getTxtBusqueda().getText().trim();
        if (termino.isEmpty()) { cargarTabla(); return; }
        try {
            List<Servicio> lista = restTemplate.exchange(
                api + "/servicio/buscar?termino=" + termino,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Servicio>>() {}
            ).getBody();
            llenarTabla(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlServicio,
                "Error al buscar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre el formulario para registrar un nuevo servicio.
     */
    private void nuevo() {
        form = new FormServicio(parentFrame());
        conectarEventosForm(form);
        form.ocultarSeccionVeterinarios();
        form.setVisible(true);
    }
    
    /**
     * Carga todos los veterinarios disponibles (metodo auxiliar).
     * 
     * @param form formulario de servicio
     */
    private void cargarTodosLosVeterinariosDisponibles(FormServicio form) {
        try {
            List<Veterinario> lista = restTemplate.exchange(
                api + "/veterinario/listar", 
                HttpMethod.GET, 
                null,
                new ParameterizedTypeReference<List<Veterinario>>() {}
            ).getBody();
            form.cargarVeterinariosDisponibles(lista);
            form.cargarVeterinariosAsignados(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error al cargar veterinarios: " + e.getMessage());
        }
    }
    
    /**
     * Abre el formulario para editar un servicio existente.
     * 
     * @param s servicio a editar
     */
    private void editar(Servicio s) {
        form = new FormServicio(parentFrame(), s);
        conectarEventosForm(form);
        cargarVeterinariosAsignados(form, s.getIdServicio());
        cargarVeterinariosDisponibles(form, s.getIdServicio());
        form.setVisible(true);
    }

    /**
     * Muestra los detalles de un servicio.
     * 
     * @param s servicio a visualizar
     */
    private void ver(Servicio s) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("ID: ").append(s.getIdServicio()).append("\n");
        mensaje.append("Nombre: ").append(s.getNombreServicio()).append("\n");
        mensaje.append("Descripcion: ").append(s.getDescripcion()).append("\n");
        mensaje.append("Precio: $").append(s.getPrecio()).append("\n");
        mensaje.append("Duracion: ").append(s.getDuracionEstimada()).append(" min\n");
        mensaje.append("Estado: ").append(s.isEstado() ? "Activo" : "Inactivo");
        
        JOptionPane.showMessageDialog(pnlServicio, mensaje.toString(),
            "Detalle del Servicio", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Elimina un servicio del sistema.
     * 
     * @param s servicio a eliminar
     */
    private void eliminar(Servicio s) {
        int confirm = JOptionPane.showConfirmDialog(pnlServicio,
            "¿Eliminar el servicio \"" + s.getNombreServicio() + "\"?\n" +
            "Se eliminaran tambien sus asignaciones de veterinarios.",
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try {
            restTemplate.delete(api + "/servicio/eliminar/" + s.getIdServicio());
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnlServicio,
                "Error al eliminar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga los veterinarios asignados a un servicio.
     * 
     * @param form formulario de servicio
     * @param idServicio identificador del servicio
     */
    private void cargarVeterinariosAsignados(FormServicio form, int idServicio) {
        try {
            List<Veterinario> lista = restTemplate.exchange(
                api + "/servicio/veterinarios/asignados/" + idServicio,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Veterinario>>() {}
            ).getBody();
            form.cargarVeterinariosAsignados(lista);
        } catch (Exception e) {
            System.err.println("Error al cargar veterinarios asignados: " + e.getMessage());
        }
    }

    /**
     * Carga los veterinarios disponibles (no asignados) para un servicio.
     * 
     * @param form formulario de servicio
     * @param idServicio identificador del servicio
     */
    private void cargarVeterinariosDisponibles(FormServicio form, int idServicio) {
        try {
            List<Veterinario> lista = restTemplate.exchange(
                api + "/servicio/veterinarios/no-asignados/" + idServicio,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Veterinario>>() {}
            ).getBody();
            form.cargarVeterinariosDisponibles(lista);
        } catch (Exception e) {
            System.err.println("Error al cargar veterinarios disponibles: " + e.getMessage());
        }
    }

    /**
     * Conecta los eventos del formulario de servicio.
     * 
     * @param form formulario de servicio
     */
    private void conectarEventosForm(FormServicio form) {
        form.getBtnAccion().addActionListener(e -> {
            String error = validarDatos(form);
            if (error != null) {
                JOptionPane.showMessageDialog(form, error,
                    "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (form.isModoEdicion()) actualizar(form);
            else registrar(form);
        });

        form.getBtnAsignar().addActionListener(e -> asignarVeterinario(form));
        form.getBtnQuitar().addActionListener(e -> quitarVeterinario(form));
        form.getBtnAbrirVeterinario().addActionListener(e -> abrirVentanaVeterinario());
    }

    /**
     * Asigna un veterinario al servicio actual.
     * 
     * @param form formulario de servicio
     */
    private void asignarVeterinario(FormServicio form) {
        int row = form.getTblVeterinariosDisponibles().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(form,
                "Seleccione un veterinario de la lista de disponibles.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idVeterinario = (int) form.getTblVeterinariosDisponibles().getValueAt(row, 0);
        String nombreVet = (String) form.getTblVeterinariosDisponibles().getValueAt(row, 1);
        int idServicio = Integer.parseInt(form.getTxtId().getText());

        try {
            Boolean ok = restTemplate.postForObject(
                api + "/servicio/asignar-veterinario?idServicio=" + idServicio +
                    "&idVeterinario=" + idVeterinario,
                null, Boolean.class);

            if (Boolean.TRUE.equals(ok)) {
                JOptionPane.showMessageDialog(form,
                    "Veterinario \"" + nombreVet + "\" asignado correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
                cargarVeterinariosAsignados(form, idServicio);
                cargarVeterinariosDisponibles(form, idServicio);
            } else {
                JOptionPane.showMessageDialog(form,
                    "El veterinario ya esta asignado a este servicio.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al asignar veterinario: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina la asignacion de un veterinario del servicio actual.
     * 
     * @param form formulario de servicio
     */
    private void quitarVeterinario(FormServicio form) {
        int row = form.getTblVeterinariosAsignados().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(form,
                "Seleccione un veterinario de la lista de asignados.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idVeterinario = (int) form.getTblVeterinariosAsignados().getValueAt(row, 0);
        int idServicio = Integer.parseInt(form.getTxtId().getText());
        String nombreVet = (String) form.getTblVeterinariosAsignados().getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(
            null,
            "¿Esta seguro de quitar a \"" + nombreVet + "\" del servicio?",
            "Confirmar desasignacion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            restTemplate.delete(api + "/servicio/eliminar-asignacion/" + idVeterinario + "/" + idServicio);
            JOptionPane.showMessageDialog(null,
                "Veterinario desasignado correctamente.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
            cargarVeterinariosAsignados(form, idServicio);
            cargarVeterinariosDisponibles(form, idServicio);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al quitar veterinario: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Abre la ventana de gestion de veterinarios.
     */
    private void abrirVentanaVeterinario() {
        java.awt.Frame parentFrame = parentFrame();
        com.mycompany.veterinaria.grupo4.view.personalVeterinario.PnlVeterinario pnlVeterinario = 
            new com.mycompany.veterinaria.grupo4.view.personalVeterinario.PnlVeterinario();
        CtrlVeterinario ctrlVeterinario = new CtrlVeterinario(pnlVeterinario);

        javax.swing.JDialog dialog = new javax.swing.JDialog(parentFrame, "Gestion de Veterinarios", true);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.add(pnlVeterinario);
        dialog.setVisible(true);
    }

    /**
     * Valida los datos del formulario de servicio.
     * 
     * @param form formulario de servicio
     * @return mensaje de error o null si los datos son validos
     */
    private String validarDatos(FormServicio form) {
        String nombre = form.getTxtNombre().getText().trim();
        if (nombre.isEmpty()) return "El nombre del servicio es obligatorio.";

        String precioStr = form.getTxtPrecio().getText().trim();
        if (precioStr.isEmpty()) return "El precio es obligatorio.";
        try {
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) return "El precio debe ser mayor a 0.";
        } catch (NumberFormatException e) {
            return "Ingrese un precio valido (numero).";
        }

        String duracionStr = form.getTxtDuracion().getText().trim();
        if (duracionStr.isEmpty()) return "La duracion es obligatoria.";
        try {
            int duracion = Integer.parseInt(duracionStr);
            if (duracion <= 0) return "La duracion debe ser mayor a 0.";
        } catch (NumberFormatException e) {
            return "Ingrese una duracion valida (numero entero).";
        }

        return null;
    }

    /**
     * Construye un objeto Servicio a partir de los datos del formulario.
     * 
     * @param form formulario de servicio
     * @return servicio construido
     */
    private Servicio buildServicio(FormServicio form) {
        Servicio s = form.isModoEdicion() ? form.getServicioActual() : new Servicio();
        
        if (!form.getTxtId().getText().isEmpty()) {
            s.setIdServicio(Integer.parseInt(form.getTxtId().getText()));
        }
        s.setNombreServicio(form.getTxtNombre().getText().trim());
        s.setDescripcion(form.getTxtDescripcion().getText().trim());
        s.setPrecio(Double.parseDouble(form.getTxtPrecio().getText().trim()));
        s.setDuracionEstimada(Integer.parseInt(form.getTxtDuracion().getText().trim()));
        s.setEstado(form.getChkEstado().isSelected());
        
        return s;
    }

    /**
     * Registra un nuevo servicio en el sistema.
     * 
     * @param form formulario de servicio
     */
    private void registrar(FormServicio form) {
        try {
            Servicio s = buildServicio(form);
            Integer resultado = restTemplate.postForObject(
                api + "/servicio/crear", s, Integer.class);

            if (resultado != null && resultado > 0) {
                JOptionPane.showMessageDialog(form,
                    "Servicio registrado correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
                form.dispose();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(form,
                    "Error al registrar el servicio.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(form,
                "Error al registrar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza un servicio existente.
     * 
     * @param form formulario de servicio
     */
    private void actualizar(FormServicio form) {
        try {
            Servicio s = buildServicio(form);
            Boolean ok = restTemplate.exchange(
                api + "/servicio/actualizar",
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(s),
                Boolean.class
            ).getBody();

            if (Boolean.TRUE.equals(ok)) {
                JOptionPane.showMessageDialog(form,
                    "Servicio actualizado correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
                form.dispose();
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(form,
                    "Error al actualizar el servicio.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(form,
                "Error al actualizar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Devuelve el Frame padre del panel de servicios.
     * 
     * @return Frame contenedor
     */
    private Frame parentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(pnlServicio);
    }
}