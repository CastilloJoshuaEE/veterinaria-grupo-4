package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.util.SessionManager;
import com.mycompany.veterinaria.grupo4.view.auth.FormRestablecerContrasena;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador de autenticacion del sistema.
 * <p>
 * Gestiona el proceso de inicio de sesion, validando las credenciales
 * del usuario mediante la API REST y manejando la sesion activa.
 * </p>
 * 
 * <p><b>Fecha de inicio del proyecto:</b> 15/04/2026</p>
 * 
 * @author ROBLES MORALES JUAN ANDRES – MODULO: ATENCION VETERINARIA
 * @version 2.0
 * @since 1.0
 */
public class AuthController implements ActionListener {
    private AppController app;
    private PnlBgLogin pnl;
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api/auth";

    /**
     * Constructor del controlador de autenticacion.
     * 
     * @param app controlador principal de la aplicacion
     * @param pnl panel de login que contiene los componentes de la UI
     */
    public AuthController(AppController app, PnlBgLogin pnl) {
        this.app = app;
        this.pnl = pnl;
        addListeners();
    }  
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == pnl.getPnlLogin().getCmdLogin()) {
            login();
        } else if (obj == pnl.getPnlLogin().getCmdForget()) {
            mostrarDialogoRestablecer();
        }
    }
    
    /**
     * Muestra el diálogo para restablecer contraseña.
     */
    private void mostrarDialogoRestablecer() {
        Frame parent = (Frame) javax.swing.SwingUtilities.getWindowAncestor(pnl);
        if (parent == null) {
            parent = new javax.swing.JFrame();
        }
        
        FormRestablecerContrasena dialog = new FormRestablecerContrasena(parent);
        dialog.setVisible(true);
        
        if (dialog.isConfirmado()) {
            String email = dialog.getEmail();
            String nuevaContrasena = dialog.getNuevaContrasena();
            String confirmarContrasena = dialog.getConfirmarContrasena();
            
            // Validar que las contraseñas coincidan (ya se validó en el diálogo)
            if (nuevaContrasena.equals(confirmarContrasena)) {
                restablecerContrasena(email, nuevaContrasena);
            }
        }
    }
    
    /**
     * Ejecuta el proceso de restablecimiento de contraseña.
     * 
     * @param email correo del usuario
     * @param nuevaContrasena nueva contraseña
     */
    private void restablecerContrasena(String email, String nuevaContrasena) {
        try {
            // Construir la solicitud
            java.util.Map<String, String> request = new java.util.HashMap<>();
            request.put("email", email);
            request.put("nuevaContrasena", nuevaContrasena);
            
            // Llamar a la API para restablecer la contraseña
            Boolean resultado = restTemplate.postForObject(
                apiBaseUrl + "/restablecer-contrasena",
                request,
                Boolean.class
            );
            
            if (Boolean.TRUE.equals(resultado)) {
                JOptionPane.showMessageDialog(pnl,
                    "Contraseña restablecida exitosamente.\n" +
                    "Puede iniciar sesión con su nueva contraseña.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(pnl,
                    "No se encontró una cuenta asociada a este correo electrónico.",
                    "Usuario no encontrado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // Error del servidor (400, 404, etc.)
            String mensaje = "Error al restablecer la contraseña";
            try {
                String body = e.getResponseBodyAsString();
                if (body != null && !body.isEmpty()) {
                    if (body.startsWith("\"") && body.endsWith("\"")) {
                        mensaje = body.substring(1, body.length() - 1);
                    } else {
                        mensaje = body;
                    }
                }
            } catch (Exception ex) {
                mensaje = e.getMessage();
            }
            JOptionPane.showMessageDialog(pnl,
                mensaje,
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnl,
                "Error de conexión. Asegúrese que el servidor esté corriendo.\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Ejecuta el proceso de login validando credenciales.
     */
    private void login() {
        String email = pnl.getPnlLogin().getTxtEmail().getText().trim();
        String password = new String(pnl.getPnlLogin().getTxtPass().getPassword()).trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(pnl, 
                "Complete todos los campos", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            LoginRequest request = new LoginRequest();
            request.setEmail(email);
            request.setPassword(password);
            
            Usuario user = restTemplate.postForObject(apiBaseUrl + "/login", request, Usuario.class);
            
            if (user != null && user.getIdUsuario() > 0) {
                SessionManager.getInstance().login(user);
                JOptionPane.showMessageDialog(pnl, 
                    "Bienvenido " + (user.getNombreCompleto() != null ? user.getNombreCompleto() : email),
                    "Login exitoso", JOptionPane.INFORMATION_MESSAGE);
                app.cargarPnlDefault();
            } else {
                JOptionPane.showMessageDialog(pnl, 
                    "Email o contraseña incorrectos", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnl, 
                "Error de conexion. Asegurese que el servidor este corriendo.\n" + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Registra los listeners para los componentes del panel de login.
     */
    private void addListeners() {
        this.pnl.getPnlLogin().getTxtEmail().addActionListener(this);
        this.pnl.getPnlLogin().getTxtPass().addActionListener(this);
        this.pnl.getPnlLogin().getCmdForget().addActionListener(this);
        this.pnl.getPnlLogin().getCmdLogin().addActionListener(this);
    }
}