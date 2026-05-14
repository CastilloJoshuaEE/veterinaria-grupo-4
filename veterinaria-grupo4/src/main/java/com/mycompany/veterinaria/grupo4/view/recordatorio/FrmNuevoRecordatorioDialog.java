package com.mycompany.veterinaria.grupo4.view.recordatorio;

import com.mycompany.veterinaria.grupo4.controller.CtrlRecordatorio;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FrmNuevoRecordatorioDialog extends JDialog {
    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbCorreoUsuario;
    private JComboBox<Map<String, Object>> cmbCita;
    private JComboBox<Map<String, Object>> cmbVacuna;
    private JTextArea txtMensaje;
    private JComboBox<String> cmbAnticipacion;
    private CtrlRecordatorio controller;
    private RestTemplate restTemplate;
    private Recordatorio recordatorio;
    private String anticipacion;
    private boolean ok = false;
    
    private static final String BASE_URL = "http://localhost:8080/api";

    public FrmNuevoRecordatorioDialog(Window parent) {
        super(parent, "Nuevo Recordatorio Manual", ModalityType.APPLICATION_MODAL);
        controller = new CtrlRecordatorio();
        restTemplate = new RestTemplate();
        setSize(550, 500);
        setLocationRelativeTo(parent);
        initUI();
        cargarDatosReales();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cmbTipo = new JComboBox<>(new String[]{"CITA", "VACUNA"});
        cmbTipo.addActionListener(e -> cambiarTipo());
        add(cmbTipo, gbc);
        
        // Usuario (Correo)
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Usuario (Email):"), gbc);
        gbc.gridx = 1;
        cmbCorreoUsuario = new JComboBox<>();
        cmbCorreoUsuario.setPreferredSize(new Dimension(280, 25));
        add(cmbCorreoUsuario, gbc);
        
        // Cita
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Cita:"), gbc);
        gbc.gridx = 1;
        cmbCita = new JComboBox<>();
        cmbCita.setPreferredSize(new Dimension(280, 25));
        cmbCita.addActionListener(e -> generarMensaje());
        add(cmbCita, gbc);
        
        // Vacuna
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Vacuna:"), gbc);
        gbc.gridx = 1;
        cmbVacuna = new JComboBox<>();
        cmbVacuna.setPreferredSize(new Dimension(280, 25));
        cmbVacuna.addActionListener(e -> generarMensaje());
        add(cmbVacuna, gbc);
        
        // Anticipación
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Anticipación:"), gbc);
        gbc.gridx = 1;
        cmbAnticipacion = new JComboBox<>(new String[]{"15_DIAS", "7_DIAS", "1_MES", "12_HORAS", "5_MINUTOS", "30_SEGUNDOS"});
        cmbAnticipacion.addActionListener(e -> generarMensaje());
        add(cmbAnticipacion, gbc);
        
        // Mensaje
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Mensaje:"), gbc);
        gbc.gridx = 1;
        txtMensaje = new JTextArea(5, 30);
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        JScrollPane spMensaje = new JScrollPane(txtMensaje);
        spMensaje.setPreferredSize(new Dimension(300, 100));
        add(spMensaje, gbc);
        
        // Botones
        JPanel pnlBotones = new JPanel();
        JButton btnOk = new JButton("Guardar");
        JButton btnCancel = new JButton("Cancelar");
        JButton btnGenerar = new JButton("Generar Mensaje Automático");
        pnlBotones.add(btnOk);
        pnlBotones.add(btnCancel);
        pnlBotones.add(btnGenerar);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(pnlBotones, gbc);
        
        btnOk.addActionListener(e -> guardar());
        btnCancel.addActionListener(e -> dispose());
        btnGenerar.addActionListener(e -> generarMensaje());
        
        cambiarTipo();
    }
    
    private void cargarDatosReales() {
        try {
            // 1. Cargar correos de usuarios desde API (endpoint CORRECTO)
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> usuarios = restTemplate.getForObject(
                BASE_URL + "/auth/correos", List.class);
            
            if (usuarios != null) {
                cmbCorreoUsuario.removeAllItems();
                for (Map<String, Object> user : usuarios) {
                    String email = (String) user.get("CORREO_ELECTRONICO");
                    if (email != null && !email.isEmpty()) {
                        cmbCorreoUsuario.addItem(email);
                    }
                }
            }
            
            // 2. Cargar citas pendientes desde API (endpoint CORRECTO: /cita/pendientes existe en tu API?)
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> citas = restTemplate.getForObject(
                    BASE_URL + "/cita/pendientes", List.class);
                
                if (citas != null && !citas.isEmpty()) {
                    cmbCita.removeAllItems();
                    for (Map<String, Object> cita : citas) {
                        cmbCita.addItem(cita);
                    }
                }
            } catch (Exception e) {
                System.err.println("No se pudieron cargar citas pendientes: " + e.getMessage());
                // Dejar combo vacío
            }
            
            // 3. Cargar vacunas próximas desde API (endpoint CORRECTO: /vacuna/proximas-a-vencer)
            try {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> vacunas = restTemplate.getForObject(
                    BASE_URL + "/vacuna/proximas-a-vencer?dias=30", List.class);
                
                if (vacunas != null && !vacunas.isEmpty()) {
                    cmbVacuna.removeAllItems();
                    for (Map<String, Object> vacuna : vacunas) {
                        cmbVacuna.addItem(vacuna);
                    }
                }
            } catch (Exception e) {
                System.err.println("No se pudieron cargar vacunas próximas: " + e.getMessage());
                // Dejar combo vacío
            }
            
            // Configurar renderers personalizados
            configurarRenderers();
            
        } catch (Exception ex) {
            System.err.println("Error cargando datos reales: " + ex.getMessage());

        }
    }
    
    private void configurarRenderers() {
    // Renderer para citas
    cmbCita.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> cita = (Map<String, Object>) value;
                
                // Obtener valores correctamente según la estructura JSON
                Object idCita = cita.get("idCita");
                
                // Servicio y Mascota están anidados
                Map<String, Object> servicio = (Map<String, Object>) cita.get("servicio");
                Map<String, Object> mascota = (Map<String, Object>) cita.get("mascota");
                
                String nombreServicio = servicio != null && servicio.get("nombreServicio") != null 
                    ? servicio.get("nombreServicio").toString() : "Sin servicio";
                String nombreMascota = mascota != null && mascota.get("nombre") != null 
                    ? mascota.get("nombre").toString() : "Sin mascota";
                String fechaHora = cita.get("fechaHora") != null 
                    ? cita.get("fechaHora").toString() : "Fecha no disponible";
                
                // Formatear fecha
                String fechaFormateada = fechaHora;
                try {
                    if (fechaHora.contains("T")) {
                        fechaFormateada = fechaHora.replace("T", " ").substring(0, 16);
                    }
                } catch (Exception e) {}
                
                String display = String.format("CITA #%d - %s - %s - Mascota: %s",
                    idCita != null ? Integer.parseInt(idCita.toString()) : 0,
                    fechaFormateada,
                    nombreServicio,
                    nombreMascota);
                return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    });
    
    // Renderer para vacunas
    cmbVacuna.setRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> vacuna = (Map<String, Object>) value;
            
            Object idVacuna = vacuna.get("idVacuna");
            Object nombre = vacuna.get("nombre");
            Object fechaProxima = vacuna.get("fechaProxima");
            Object nombreMascota = vacuna.get("nombreMascota");
            
            String display = String.format("VACUNA #%d - %s - Próxima: %s - Mascota: %s",
                idVacuna != null ? Integer.parseInt(idVacuna.toString()) : 0,
                nombre != null ? nombre.toString() : "Sin nombre",
                fechaProxima != null ? fechaProxima.toString() : "Fecha no disponible",
                nombreMascota != null ? nombreMascota.toString() : "Mascota no disponible");
            return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
});
}

   
    
    private void cambiarTipo() {
        String tipo = (String) cmbTipo.getSelectedItem();
        boolean esCita = "CITA".equals(tipo);
        cmbCita.setEnabled(esCita);
        cmbVacuna.setEnabled(!esCita);
        generarMensaje();
    }
    private void generarMensaje() {
    String tipo = (String) cmbTipo.getSelectedItem();
    String anticipacion = (String) cmbAnticipacion.getSelectedItem();
    StringBuilder msg = new StringBuilder();
    
    if ("CITA".equals(tipo) && cmbCita.getSelectedItem() != null) {
        @SuppressWarnings("unchecked")
        Map<String, Object> cita = (Map<String, Object>) cmbCita.getSelectedItem();
        msg.append("Recordatorio de CITA\n");
        msg.append("Tiene una cita pendiente dentro de ").append(anticipacion).append(".\n");
        
        // Obtener valores anidados correctamente
        Map<String, Object> servicio = (Map<String, Object>) cita.get("servicio");
        Map<String, Object> mascota = (Map<String, Object>) cita.get("mascota");
        
        String nombreServicio = servicio != null && servicio.get("nombreServicio") != null 
            ? servicio.get("nombreServicio").toString() : "Servicio no disponible";
        String nombreMascota = mascota != null && mascota.get("nombre") != null 
            ? mascota.get("nombre").toString() : "Mascota no disponible";
        
        msg.append("Detalles: ").append(nombreServicio)
           .append(" - Mascota: ").append(nombreMascota);
    } else if ("VACUNA".equals(tipo) && cmbVacuna.getSelectedItem() != null) {
        @SuppressWarnings("unchecked")
        Map<String, Object> vacuna = (Map<String, Object>) cmbVacuna.getSelectedItem();
        msg.append("Recordatorio de VACUNA\n");
        msg.append("Vacunación próxima dentro de ").append(anticipacion).append(".\n");
        
        Object nombre = vacuna.get("nombre");
        Object nombreMascota = vacuna.get("nombreMascota");
        
        msg.append("Detalles: ").append(nombre != null ? nombre : "Vacuna no disponible")
           .append(" - Mascota: ").append(nombreMascota != null ? nombreMascota : "Mascota no disponible");
    }
    
    if (msg.length() > 0) {
        txtMensaje.setText(msg.toString());
    }
}

    
    
    @SuppressWarnings("unchecked")
private void guardar() {
    if (txtMensaje.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "El mensaje es requerido", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    recordatorio = new Recordatorio();
    recordatorio.setTipo((String) cmbTipo.getSelectedItem());
    recordatorio.setMensaje(txtMensaje.getText());
    recordatorio.setFechaEnvio(new Date());
    recordatorio.setLeido(false);
    recordatorio.setCorreoUsuario((String) cmbCorreoUsuario.getSelectedItem());
    
    String tipo = (String) cmbTipo.getSelectedItem();
    
    if ("CITA".equals(tipo) && cmbCita.getSelectedItem() != null) {
        Map<String, Object> cita = (Map<String, Object>) cmbCita.getSelectedItem();
        Object idCitaObj = cita.get("idCita");
        if (idCitaObj != null) {
            recordatorio.setIdCita(Integer.parseInt(idCitaObj.toString()));
        }
        recordatorio.setIdVacuna(null);
    } else if ("VACUNA".equals(tipo) && cmbVacuna.getSelectedItem() != null) {
        Map<String, Object> vacuna = (Map<String, Object>) cmbVacuna.getSelectedItem();
        Object idVacunaObj = vacuna.get("idVacuna");
        if (idVacunaObj != null) {
            recordatorio.setIdVacuna(Integer.parseInt(idVacunaObj.toString()));
        }
        recordatorio.setIdCita(null);
    } else {
        JOptionPane.showMessageDialog(this, 
            "Debe seleccionar una Cita o una Vacuna según el tipo elegido.", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    anticipacion = (String) cmbAnticipacion.getSelectedItem();
    ok = true;
    dispose();
}
    public boolean showDialog() {
        setVisible(true);
        return ok;
    }
    
    public Recordatorio getRecordatorio() { return recordatorio; }
    public String getAnticipacion() { return anticipacion; }
}