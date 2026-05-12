package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.FichaMedicaDTO;
import com.mycompany.veterinaria.grupo4.model.entity.*;
import com.mycompany.veterinaria.grupo4.view.historial.PnlHistorialMedico;
import com.mycompany.veterinaria.grupo4.view.historial.FormFichaMedica;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Controlador para la gestion del historial medico de mascotas.
 * <p>
 * Permite buscar mascotas, visualizar su historial completo de atenciones,
 * vacunas aplicadas, y gestionar la ficha medica con alergias y
 * enfermedades cronicas.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author CASTILLO MEREJILDO JOSHUA JAVIER – MODULO: MASCOTA
 * @version 1.0
 * @since 1.0
 */
public class CtrlHistorialMedico {
    
    private final PnlHistorialMedico view;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiBaseUrl = "http://localhost:8080/api";
    private final SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat sdfFechaCorta = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * Constructor del controlador de historial medico.
     * 
     * @param view panel de historial medico
     */
    public CtrlHistorialMedico(PnlHistorialMedico view) {
        this.view = view;
        initListeners();
    }
    
    /**
     * Inicializa los listeners del panel.
     */
    private void initListeners() {
        view.getBtnBuscarMascota().addActionListener(e -> buscarMascota());
        view.getBtnActualizarFicha().addActionListener(e -> actualizarFichaMedica());
    }
    
    /**
     * Busca una mascota por nombre o cedula del dueño.
     */
    private void buscarMascota() {
        String termino = view.getTxtBuscarMascota().getText().trim();
        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "Ingrese un termino de busqueda (nombre de mascota o cedula del dueño)",
                "Busqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            List<Mascota> mascotas = restTemplate.exchange(
                apiBaseUrl + "/mascota/buscar?termino=" + termino,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Mascota>>() {}
            ).getBody();
            
            if (mascotas == null || mascotas.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "No se encontraron mascotas con ese criterio.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            Mascota seleccionada = null;
            if (mascotas.size() == 1) {
                seleccionada = mascotas.get(0);
            } else {
                seleccionada = mostrarSelectorMascotas(mascotas);
            }
            
            if (seleccionada != null) {
                cargarDatosCompletos(seleccionada);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Error al buscar: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra un selector de mascotas cuando hay multiples resultados.
     * 
     * @param mascotas lista de mascotas encontradas
     * @return mascota seleccionada o null
     */
    private Mascota mostrarSelectorMascotas(List<Mascota> mascotas) {
        String[] opciones = mascotas.stream()
            .map(m -> m.getNombre() + " (" + m.getEspecie() + ") - ID: " + m.getIdMascota())
            .toArray(String[]::new);
        
        int seleccion = JOptionPane.showOptionDialog(view,
            "Se encontraron varias mascotas. Seleccione una:",
            "Seleccionar Mascota",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (seleccion >= 0 && seleccion < mascotas.size()) {
            return mascotas.get(seleccion);
        }
        return null;
    }
    
    /**
     * Carga todos los datos completos de una mascota seleccionada.
     * 
     * @param mascota mascota seleccionada
     */
    private void cargarDatosCompletos(Mascota mascota) {
        view.setIdMascotaSeleccionada(mascota.getIdMascota());
        
        cargarDatosMascota(mascota);
        cargarDatosDueno(mascota.getIdCliente());
        cargarFoto(mascota.getIdMascota());
        cargarHistorialMedico(mascota.getIdMascota());
        cargarVacunas(mascota.getIdMascota());
        verificarFichaMedica(mascota.getIdMascota());
        actualizarResumen();
        
        view.getBtnActualizarFicha().setEnabled(true);
    }
    
    /**
     * Carga los datos basicos de la mascota en la interfaz.
     * 
     * @param mascota mascota cuyos datos cargar
     */
    private void cargarDatosMascota(Mascota mascota) {
        view.getLblNombreMascota().setText(mascota.getNombre());
        
        String especieRaza = mascota.getEspecie();
        if (mascota.getRaza() != null && !mascota.getRaza().isEmpty()) {
            especieRaza += " - " + mascota.getRaza();
        }
        view.getLblEspecie().setText(especieRaza);
        view.getLblSexo().setText(mascota.getSexo() == 'M' ? "Macho" : "Hembra");
        
        if (mascota.getFechaNacimiento() != null) {
            view.getLblFechaNacimiento().setText(sdfFechaCorta.format(mascota.getFechaNacimiento()));
        }
    }
    
    /**
     * Carga los datos del dueño de la mascota.
     * 
     * @param idCliente identificador del cliente
     */
    private void cargarDatosDueno(int idCliente) {
        try {
            Cliente cliente = restTemplate.getForObject(
                apiBaseUrl + "/cliente/" + idCliente, Cliente.class);
            
            if (cliente != null) {
                view.getLblNombreCliente().setText(cliente.getNombre() + " " + cliente.getApellido());
                view.getLblCedulaCliente().setText(cliente.getCedula());
                view.getLblTelefonoCliente().setText(cliente.getTelefono());
            }
        } catch (Exception e) {
            System.err.println("Error al cargar dueño: " + e.getMessage());
        }
    }
    
    /**
     * Carga la foto de la mascota.
     * 
     * @param idMascota identificador de la mascota
     */
    private void cargarFoto(int idMascota) {
        try {
            byte[] foto = restTemplate.getForObject(
                apiBaseUrl + "/mascota/foto/" + idMascota, byte[].class);
            
            if (foto != null && foto.length > 0) {
                ImageIcon icon = new ImageIcon(foto);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                view.getLblFotoMascota().setIcon(new ImageIcon(img));
                view.getLblSinFoto().setVisible(false);
            } else {
                view.getLblFotoMascota().setIcon(null);
                view.getLblSinFoto().setVisible(true);
            }
        } catch (Exception e) {
            view.getLblFotoMascota().setIcon(null);
            view.getLblSinFoto().setVisible(true);
        }
    }
    
    /**
     * Carga el historial de atenciones medicas de la mascota.
     * 
     * @param idMascota identificador de la mascota
     */
    private void cargarHistorialMedico(int idMascota) {
        try {
            List<HistorialMedico> historial = restTemplate.exchange(
                apiBaseUrl + "/historial/mascota/" + idMascota,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<HistorialMedico>>() {}
            ).getBody();
            
            javax.swing.table.DefaultTableModel model = 
                (javax.swing.table.DefaultTableModel) view.getTblAtenciones().getModel();
            model.setRowCount(0);
            
            Set<String> serviciosUnicos = new HashSet<>();
            
            if (historial != null) {
                for (HistorialMedico h : historial) {
                    String fecha = h.getFecha() != null ? sdfFecha.format(h.getFecha()) : "-";
                    String servicio = h.getNombreServicio() != null ? h.getNombreServicio() : "-";
                    String veterinario = h.getNombreVeterinario() != null ? h.getNombreVeterinario() : "-";
                    
                    serviciosUnicos.add(servicio);
                    
                    model.addRow(new Object[]{
                        fecha, servicio, veterinario,
                        "-", "-",
                        h.getDiagnostico() != null ? h.getDiagnostico() : "-",
                        h.getTratamiento() != null ? h.getTratamiento() : "-"
                    });
                }
            }
            
            view.getLblTotalAtenciones().setText("Total atenciones: " + (historial != null ? historial.size() : 0));
            view.getLblServiciosUnicos().setText("Servicios unicos: " + serviciosUnicos.size());
            
        } catch (Exception e) {
            System.err.println("Error al cargar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Carga las vacunas aplicadas a la mascota.
     * 
     * @param idMascota identificador de la mascota
     */
    private void cargarVacunas(int idMascota) {
        try {
            List<VacunaAplicada> vacunas = restTemplate.exchange(
                apiBaseUrl + "/vacuna/mascota/" + idMascota,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<VacunaAplicada>>() {}
            ).getBody();
            
            javax.swing.table.DefaultTableModel model = 
                (javax.swing.table.DefaultTableModel) view.getTblVacunas().getModel();
            model.setRowCount(0);
            
            if (vacunas != null) {
                for (VacunaAplicada v : vacunas) {
                    String fechaAplicacion = v.getFechaAplicacion() != null ? 
                        sdfFechaCorta.format(v.getFechaAplicacion()) : "-";
                    String fechaProxima = v.getFechaProxima() != null ? 
                        sdfFechaCorta.format(v.getFechaProxima()) : "-";
                    
                    model.addRow(new Object[]{
                        v.getNombre() != null ? v.getNombre() : "-",
                        v.getDescripcion() != null ? v.getDescripcion() : "-",
                        fechaAplicacion,
                        fechaProxima
                    });
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al cargar vacunas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si existe ficha medica para la mascota.
     * 
     * @param idMascota identificador de la mascota
     */
    private void verificarFichaMedica(int idMascota) {
        try {
            FichaMedicaDTO ficha = restTemplate.getForObject(
                apiBaseUrl + "/mascota/ficha/" + idMascota, FichaMedicaDTO.class);
        } catch (Exception e) {
            System.err.println("Error al verificar ficha: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza el resumen del historial.
     */
    private void actualizarResumen() {
        // El resumen ya se actualiza al cargar el historial
    }
    
    /**
     * Abre el formulario para actualizar la ficha medica de la mascota.
     */
    private void actualizarFichaMedica() {
        int idMascota = view.getIdMascotaSeleccionada();
        if (idMascota <= 0) {
            JOptionPane.showMessageDialog(view,
                "Primero seleccione una mascota.",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            FichaMedicaDTO fichaActual = restTemplate.getForObject(
                apiBaseUrl + "/mascota/ficha/" + idMascota, FichaMedicaDTO.class);
            
            String alergias = fichaActual != null ? fichaActual.getAlergias() : "";
            String enfermedades = fichaActual != null ? fichaActual.getEnfermedadesCronicas() : "";
            String observaciones = fichaActual != null ? fichaActual.getObservaciones() : "";
            
            Frame parent = null;
            Component comp = view;
            while (comp != null && !(comp instanceof Frame)) {
                comp = comp.getParent();
            }
            if (comp instanceof Frame) {
                parent = (Frame) comp;
            }
            
            if (parent == null) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Ficha Medica - " + view.getLblNombreMascota().getText());
                dialog.setModal(true);
                dialog.setSize(500, 450);
                dialog.setLocationRelativeTo(view);
                
                JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                
                JPanel formPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(8, 8, 8, 8);
                gbc.fill = GridBagConstraints.HORIZONTAL;
                
                gbc.gridx = 0; gbc.gridy = 0;
                formPanel.add(new JLabel("Alergias:"), gbc);
                gbc.gridx = 1;
                JTextArea txtAlergiasDialog = new JTextArea(4, 25);
                txtAlergiasDialog.setLineWrap(true);
                txtAlergiasDialog.setText(alergias);
                formPanel.add(new JScrollPane(txtAlergiasDialog), gbc);
                
                gbc.gridx = 0; gbc.gridy = 1;
                formPanel.add(new JLabel("Enfermedades Cronicas:"), gbc);
                gbc.gridx = 1;
                JTextArea txtEnfermedadesDialog = new JTextArea(4, 25);
                txtEnfermedadesDialog.setLineWrap(true);
                txtEnfermedadesDialog.setText(enfermedades);
                formPanel.add(new JScrollPane(txtEnfermedadesDialog), gbc);
                
                gbc.gridx = 0; gbc.gridy = 2;
                formPanel.add(new JLabel("Observaciones:"), gbc);
                gbc.gridx = 1;
                JTextArea txtObservacionesDialog = new JTextArea(5, 25);
                txtObservacionesDialog.setLineWrap(true);
                txtObservacionesDialog.setText(observaciones);
                formPanel.add(new JScrollPane(txtObservacionesDialog), gbc);
                
                mainPanel.add(formPanel, BorderLayout.CENTER);
                
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
                JButton btnGuardarDialog = new JButton("Guardar");
                btnGuardarDialog.setBackground(new Color(76, 175, 80));
                btnGuardarDialog.setForeground(Color.WHITE);
                JButton btnCancelarDialog = new JButton("Cancelar");
                btnCancelarDialog.setBackground(new Color(150, 150, 150));
                btnCancelarDialog.setForeground(Color.WHITE);
                
                buttonPanel.add(btnGuardarDialog);
                buttonPanel.add(btnCancelarDialog);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                dialog.setContentPane(mainPanel);
                
                btnGuardarDialog.addActionListener(evt -> {
                    FichaMedicaDTO dto = new FichaMedicaDTO();
                    dto.setIdMascota(idMascota);
                    dto.setAlergias(txtAlergiasDialog.getText());
                    dto.setEnfermedadesCronicas(txtEnfermedadesDialog.getText());
                    dto.setObservaciones(txtObservacionesDialog.getText());
                    
                    Boolean resultado = restTemplate.postForObject(
                        apiBaseUrl + "/mascota/ficha/guardar", dto, Boolean.class);
                    
                    if (Boolean.TRUE.equals(resultado)) {
                        JOptionPane.showMessageDialog(dialog,
                            "Ficha medica actualizada correctamente.",
                            "Exito", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        cargarDatosCompletos(obtenerMascotaPorId(idMascota));
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                            "Error al guardar la ficha medica.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                
                btnCancelarDialog.addActionListener(evt -> dialog.dispose());
                dialog.setVisible(true);
                return;
            }
            
            FormFichaMedica dialog = new FormFichaMedica(parent,
                "Ficha Medica - " + view.getLblNombreMascota().getText(),
                alergias, enfermedades, observaciones);
            
            dialog.setVisible(true);
            
            if (dialog.isGuardado()) {
                FichaMedicaDTO dto = new FichaMedicaDTO();
                dto.setIdMascota(idMascota);
                dto.setAlergias(dialog.getAlergias());
                dto.setEnfermedadesCronicas(dialog.getEnfermedades());
                dto.setObservaciones(dialog.getObservaciones());
                
                Boolean resultado = restTemplate.postForObject(
                    apiBaseUrl + "/mascota/ficha/guardar", dto, Boolean.class);
                
                if (Boolean.TRUE.equals(resultado)) {
                    JOptionPane.showMessageDialog(view,
                        "Ficha medica actualizada correctamente.",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosCompletos(obtenerMascotaPorId(idMascota));
                } else {
                    JOptionPane.showMessageDialog(view,
                        "Error al guardar la ficha medica.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Error al actualizar ficha: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una mascota por su identificador.
     * 
     * @param idMascota identificador de la mascota
     * @return mascota encontrada o null
     */
    private Mascota obtenerMascotaPorId(int idMascota) {
        try {
            return restTemplate.getForObject(
                apiBaseUrl + "/mascota/" + idMascota, Mascota.class);
        } catch (Exception e) {
            return null;
        }
    }
}