package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.util.SessionManager;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
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
 * @version 1.0
 * @since 1.0
 */
public class AuthController implements ActionListener{
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
        if (obj == pnl.getPnlLogin().getCmdLogin()){
            login();
        }
    }
    
    /**
     * Ejecuta el proceso de login validando credenciales.
     */
    private void login() {
        String usuario = pnl.getPnlLogin().getTxtEmail().getText().trim();
        String password = new String(pnl.getPnlLogin().getTxtPass().getPassword()).trim();
        
        if (usuario.equals("Ingrese su usuario") || usuario.isEmpty() ||
            password.equals("Ingrese su contrasena") || password.isEmpty()) {
            System.out.println(usuario + password);
            JOptionPane.showMessageDialog(pnl, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            LoginRequest request = new LoginRequest();
            request.setUsuario(usuario);
            request.setPassword(password);
            
            Usuario user = restTemplate.postForObject(apiBaseUrl + "/login", request, Usuario.class);
            
            if (user != null && user.getIdUsuario() > 0) {
                SessionManager.getInstance().login(user);
                JOptionPane.showMessageDialog(pnl, "Login exitoso", "Exito", JOptionPane.INFORMATION_MESSAGE);
                app.cargarPnlDefault();
            } else {
                JOptionPane.showMessageDialog(pnl, "Usuario o contrasena incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pnl, "Error de conexion. Asegurese que el servidor este corriendo.\n" + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Registra los listeners para los componentes del panel de login.
     */
    private void addListeners() {
        this.pnl.getPnlLogin().getTxtUser().addActionListener(this);
        this.pnl.getPnlLogin().getTxtEmail().addActionListener(this);
        this.pnl.getPnlLogin().getTxtPass().addActionListener(this);
        this.pnl.getPnlLogin().getCmdForget().addActionListener(this);
        this.pnl.getPnlLogin().getCmdLogin().addActionListener(this);
    }
}