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

public class CtrlHistorialMedico {
    
    private final PnlHistorialMedico view;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiBaseUrl = "http://localhost:8080/api";
    private final SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat sdfFechaCorta = new SimpleDateFormat("dd/MM/yyyy");
    
    public CtrlHistorialMedico(PnlHistorialMedico view) {
        this.view = view;
        initListeners();
    }
    
    private void initListeners() {
        view.getBtnBuscarMascota().addActionListener(e -> buscarMascota());
        view.getBtnActualizarFicha().addActionListener(e -> actualizarFichaMedica());
    }
    
    private void buscarMascota() {
        String termino = view.getTxtBuscarMascota().getText().trim();
        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "Ingrese un término de búsqueda (nombre de mascota o cédula del dueño)",
                "Búsqueda", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Buscar mascotas
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
            
            // Si hay multiples, mostrar diálogo de selección
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
    
    private void cargarDatosCompletos(Mascota mascota) {
        view.setIdMascotaSeleccionada(mascota.getIdMascota());
        
        // 1. Cargar datos de la mascota
        cargarDatosMascota(mascota);
        
        // 2. Cargar datos del dueño
        cargarDatosDueno(mascota.getIdCliente());
        
        // 3. Cargar foto
        cargarFoto(mascota.getIdMascota());
        
        // 4. Cargar historial médico (atenciones)
        cargarHistorialMedico(mascota.getIdMascota());
        
        // 5. Cargar vacunas
        cargarVacunas(mascota.getIdMascota());
        
        // 6. Verificar si existe ficha médica y habilitar botón
        verificarFichaMedica(mascota.getIdMascota());
        
        // 7. Actualizar resumen
        actualizarResumen();
        
        view.getBtnActualizarFicha().setEnabled(true);
    }
    
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
                    
                    // Colorear fila según servicio (opcional)
                    model.addRow(new Object[]{
                        fecha, servicio, veterinario,
                        "-", // Instrumentos (puedes expandir)
                        "-", // Medicamentos (puedes expandir)
                        h.getDiagnostico() != null ? h.getDiagnostico() : "-",
                        h.getTratamiento() != null ? h.getTratamiento() : "-"
                    });
                }
            }
            
            view.getLblTotalAtenciones().setText("Total atenciones: " + (historial != null ? historial.size() : 0));
            view.getLblServiciosUnicos().setText("Servicios únicos: " + serviciosUnicos.size());
            
        } catch (Exception e) {
            System.err.println("Error al cargar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
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
                    
                    // Verificar si está próxima a vencer (30 días)
                    boolean proximaAVencer = false;
                    if (v.getFechaProxima() != null) {
                        long dias = (v.getFechaProxima().getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
                        proximaAVencer = dias <= 30 && dias >= 0;
                    }
                    
                    model.addRow(new Object[]{
                        v.getNombre() != null ? v.getNombre() : "-",
                        v.getDescripcion() != null ? v.getDescripcion() : "-",
                        fechaAplicacion,
                        fechaProxima
                    });
                    
                    // Resaltar filas próximas a vencer
                    if (proximaAVencer) {
                        int row = model.getRowCount() - 1;
                        // El resaltado se puede hacer después
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al cargar vacunas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void verificarFichaMedica(int idMascota) {
        try {
            FichaMedicaDTO ficha = restTemplate.getForObject(
                apiBaseUrl + "/mascota/ficha/" + idMascota, FichaMedicaDTO.class);
            
            // Solo habilitamos el botón, no mostramos mensaje de "sin ficha" automático
            // El usuario sabrá al abrir el formulario si está vacía o no
            
        } catch (Exception e) {
            System.err.println("Error al verificar ficha: " + e.getMessage());
        }
    }
    
    private void actualizarResumen() {
        // El resumen ya se actualiza al cargar el historial
    }
    
    private void actualizarFichaMedica() {
    int idMascota = view.getIdMascotaSeleccionada();
    if (idMascota <= 0) {
        JOptionPane.showMessageDialog(view,
            "Primero seleccione una mascota.",
            "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        // Obtener ficha actual
        FichaMedicaDTO fichaActual = restTemplate.getForObject(
            apiBaseUrl + "/mascota/ficha/" + idMascota, FichaMedicaDTO.class);
        
        String alergias = fichaActual != null ? fichaActual.getAlergias() : "";
        String enfermedades = fichaActual != null ? fichaActual.getEnfermedadesCronicas() : "";
        String observaciones = fichaActual != null ? fichaActual.getObservaciones() : "";
        
        // CORRECCIÓN: Obtener el Frame padre correctamente
        Frame parent = null;
        Component comp = view;
        while (comp != null && !(comp instanceof Frame)) {
            comp = comp.getParent();
        }
        if (comp instanceof Frame) {
            parent = (Frame) comp;
        }
        
        // Si no se encontró Frame, usar JOptionPane en su lugar
        if (parent == null) {
            // Mostrar un diálogo simple sin depender de Frame
            JDialog dialog = new JDialog();
            dialog.setTitle("Ficha Médica - " + view.getLblNombreMascota().getText());
            dialog.setModal(true);
            dialog.setSize(500, 450);
            dialog.setLocationRelativeTo(view);
            
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Alergias
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Alergias:"), gbc);
            gbc.gridx = 1;
            JTextArea txtAlergiasDialog = new JTextArea(4, 25);
            txtAlergiasDialog.setLineWrap(true);
            txtAlergiasDialog.setText(alergias);
            formPanel.add(new JScrollPane(txtAlergiasDialog), gbc);
            
            // Enfermedades Crónicas
            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(new JLabel("Enfermedades Crónicas:"), gbc);
            gbc.gridx = 1;
            JTextArea txtEnfermedadesDialog = new JTextArea(4, 25);
            txtEnfermedadesDialog.setLineWrap(true);
            txtEnfermedadesDialog.setText(enfermedades);
            formPanel.add(new JScrollPane(txtEnfermedadesDialog), gbc);
            
            // Observaciones
            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new JLabel("Observaciones:"), gbc);
            gbc.gridx = 1;
            JTextArea txtObservacionesDialog = new JTextArea(5, 25);
            txtObservacionesDialog.setLineWrap(true);
            txtObservacionesDialog.setText(observaciones);
            formPanel.add(new JScrollPane(txtObservacionesDialog), gbc);
            
            mainPanel.add(formPanel, BorderLayout.CENTER);
            
            // Botones
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
            
            final String finalAlergias = alergias;
            final String finalEnfermedades = enfermedades;
            final String finalObservaciones = observaciones;
            
            btnGuardarDialog.addActionListener(evt -> {
                // Guardar cambios
                FichaMedicaDTO dto = new FichaMedicaDTO();
                dto.setIdMascota(idMascota);
                dto.setAlergias(txtAlergiasDialog.getText());
                dto.setEnfermedadesCronicas(txtEnfermedadesDialog.getText());
                dto.setObservaciones(txtObservacionesDialog.getText());
                
                Boolean resultado = restTemplate.postForObject(
                    apiBaseUrl + "/mascota/ficha/guardar", dto, Boolean.class);
                
                if (Boolean.TRUE.equals(resultado)) {
                    JOptionPane.showMessageDialog(dialog,
                        "Ficha médica actualizada correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    // Recargar datos
                    cargarDatosCompletos(obtenerMascotaPorId(idMascota));
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Error al guardar la ficha médica.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            btnCancelarDialog.addActionListener(evt -> dialog.dispose());
            
            dialog.setVisible(true);
            return;
        }
        
        // Si se encontró Frame, usar FormFichaMedica normal
        FormFichaMedica dialog = new FormFichaMedica(parent,
            "Ficha Médica - " + view.getLblNombreMascota().getText(),
            alergias, enfermedades, observaciones);
        
        dialog.setVisible(true);
        
        if (dialog.isGuardado()) {
            // Guardar cambios
            FichaMedicaDTO dto = new FichaMedicaDTO();
            dto.setIdMascota(idMascota);
            dto.setAlergias(dialog.getAlergias());
            dto.setEnfermedadesCronicas(dialog.getEnfermedades());
            dto.setObservaciones(dialog.getObservaciones());
            
            Boolean resultado = restTemplate.postForObject(
                apiBaseUrl + "/mascota/ficha/guardar", dto, Boolean.class);
            
            if (Boolean.TRUE.equals(resultado)) {
                JOptionPane.showMessageDialog(view,
                    "Ficha médica actualizada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Recargar datos
                cargarDatosCompletos(obtenerMascotaPorId(idMascota));
            } else {
                JOptionPane.showMessageDialog(view,
                    "Error al guardar la ficha médica.",
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

// Método auxiliar para obtener mascota por ID
private Mascota obtenerMascotaPorId(int idMascota) {
    try {
        return restTemplate.getForObject(
            apiBaseUrl + "/mascota/" + idMascota, Mascota.class);
    } catch (Exception e) {
        return null;
    }
}
}