package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Recepcionista;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.util.SessionManager;
import com.mycompany.veterinaria.grupo4.view.auth.FormRestablecerContrasena;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador de autenticacion del sistema.
 */
public class AuthController implements ActionListener {
    private AppController app;
    private PnlBgLogin pnl;
    private RestTemplate restTemplate = new RestTemplate();
    private String apiBaseUrl = "http://localhost:8080/api/auth";

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
        } else if (obj == pnl.getPnlLogin().getCmdRegistrar()) {
            registrarRecepcionista();
        }
    }
    
    /**
     * Registra un nuevo recepcionista usando el SP_REGISTRAR_RECEPCIONISTA.
     */
        private void registrarRecepcionista() {
        var pnlLogin = pnl.getPnlLogin();
        
        // Obtener datos
        String cedula = pnlLogin.getTxtCedula().getText().trim();
        String nombre = pnlLogin.getTxtNombre().getText().trim();
        String apellido = pnlLogin.getTxtApellido().getText().trim();
        String telefono = pnlLogin.getTxtTelefono().getText().trim();
        String direccion = pnlLogin.getTxtDireccion().getText().trim();
        String email = pnlLogin.getTxtEmailRegistro().getText().trim();
        String password = new String(pnlLogin.getTxtPassRegistro().getPassword()).trim();
        
        // Validaciones básicas
        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || 
            telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(pnl,
                "Todos los campos son obligatorios excepto Dirección",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(pnl,
                "La contraseña debe tener al menos 6 caracteres",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Recepcionista registro = new Recepcionista();
            registro.setCedula(cedula);
            registro.setNombre(nombre);
            registro.setApellido(apellido);
            registro.setTelefono(telefono);
            registro.setDireccion(direccion.isEmpty() ? null : direccion);
            registro.setEmail(email);
            registro.setContrasena(password);
            
            // Llamar a la API
            Boolean resultado = restTemplate.postForObject(
                apiBaseUrl + "/registrar-recepcionista",
                registro,
                Boolean.class
            );
            
            if (Boolean.TRUE.equals(resultado)) {
                JOptionPane.showMessageDialog(pnl,
                    "Recepcionista registrado exitosamente.\n" +
                    "Ahora puede iniciar sesión con sus credenciales.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar campos
                limpiarCamposRegistro();
                
                // Cambiar a la vista de Login
                pnl.getCover().login(false);
                pnlLogin.showRegister(false);
                
            } else {
                JOptionPane.showMessageDialog(pnl,
                    "Error al registrar el recepcionista. Verifique que la cédula y email no estén registrados.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // Errores HTTP 4xx (incluye validaciones del backend)
            String mensaje = "Error al registrar";
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnl,
                "Error de conexión. Asegúrese que el servidor esté corriendo.\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
        /**
     * Limpia todos los campos del formulario de registro.
     */
    private void limpiarCamposRegistro() {
        var pnlLogin = pnl.getPnlLogin();
        pnlLogin.getTxtCedula().setText("");
        pnlLogin.getTxtNombre().setText("");
        pnlLogin.getTxtApellido().setText("");
        pnlLogin.getTxtTelefono().setText("");
        pnlLogin.getTxtDireccion().setText("");
        pnlLogin.getTxtEmailRegistro().setText("");
        pnlLogin.getTxtPassRegistro().setText("");
        // Desactivar el botón de registro si se desea (se habilitará con validación)
    }
    /**
     * Extrae el mensaje de error de la excepción HTTP.
     */
    private String extraerMensajeError(HttpClientErrorException e) {
        try {
            String body = e.getResponseBodyAsString();
            if (body != null && !body.isEmpty()) {
                // Intentar extraer el mensaje del JSON
                if (body.contains("mensaje") || body.contains("message")) {
                    // Buscar entre comillas
                    int start = body.indexOf("\"message\"");
                    if (start == -1) start = body.indexOf("\"mensaje\"");
                    if (start != -1) {
                        int startQuote = body.indexOf("\"", start + 10);
                        if (startQuote != -1) {
                            int endQuote = body.indexOf("\"", startQuote + 1);
                            if (endQuote != -1) {
                                return body.substring(startQuote + 1, endQuote);
                            }
                        }
                    }
                }
                // Si no se pudo extraer, limpiar comillas
                if (body.startsWith("\"") && body.endsWith("\"")) {
                    return body.substring(1, body.length() - 1);
                }
                return body;
            }
        } catch (Exception ex) {
            // Ignorar errores al parsear
        }
        return e.getMessage();
    }
    
    /**
     * Limpia todos los campos del formulario de registro.
     */
    private void limpiarFormularioRegistro() {
        var pnlLogin = pnl.getPnlLogin();
        pnlLogin.getTxtCedula().setText("");
        pnlLogin.getTxtNombre().setText("");
        pnlLogin.getTxtApellido().setText("");
        pnlLogin.getTxtTelefono().setText("");
        pnlLogin.getTxtDireccion().setText("");
        pnlLogin.getTxtEmailRegistro().setText("");
        pnlLogin.getTxtPassRegistro().setText("");
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
            
            if (nuevaContrasena.equals(confirmarContrasena)) {
                restablecerContrasena(email, nuevaContrasena);
            }
        }
    }
    
    /**
     * Ejecuta el proceso de restablecimiento de contraseña.
     */
    private void restablecerContrasena(String email, String nuevaContrasena) {
        try {
            java.util.Map<String, String> request = new java.util.HashMap<>();
            request.put("email", email);
            request.put("nuevaContrasena", nuevaContrasena);
            
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnl,
                "Error al restablecer la contraseña: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Ejecuta el proceso de login validando credenciales.
     */
    private void login() {
        String email = pnl.getPnlLogin().getTxtEmailLogin().getText().trim();
        String password = new String(pnl.getPnlLogin().getTxtPassLogin().getPassword()).trim();
        
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
                "Error de conexión. Asegúrese que el servidor esté corriendo.\n" + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Registra los listeners para los componentes del panel de login.
     */
    private void addListeners() {
        var pnlLogin = pnl.getPnlLogin();
        
        // Login
        pnlLogin.getTxtEmailLogin().addActionListener(this);
        pnlLogin.getTxtPassLogin().addActionListener(this);
        pnlLogin.getCmdForget().addActionListener(this);
        pnlLogin.getCmdLogin().addActionListener(this);
        
        // Registro
        pnlLogin.getCmdRegistrar().addActionListener(this);
        pnlLogin.getTxtCedula().addActionListener(this);
        pnlLogin.getTxtNombre().addActionListener(this);
        pnlLogin.getTxtApellido().addActionListener(this);
        pnlLogin.getTxtTelefono().addActionListener(this);
        pnlLogin.getTxtEmailRegistro().addActionListener(this);
        pnlLogin.getTxtPassRegistro().addActionListener(this);
    }
}