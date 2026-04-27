package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.api.dto.LoginRequest;
import com.mycompany.veterinaria.grupo4.model.entity.Usuario;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import com.mycompany.veterinaria.grupo4.view.frmSistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author juan
 */
public class AuthController implements ActionListener{
    private AppController app;
    private PnlBgLogin pnl;
    //API
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
        if (obj == pnl.getPnlLogin().getCmdLogin()){
            login();
        }
    }
    
    private void login() {
        String usuario = pnl.getPnlLogin().getTxtEmail().getText().trim();
        String password = new String(pnl.getPnlLogin().getTxtPass().getPassword()).trim();
        
        if (usuario.equals("Ingrese su usuario") || usuario.isEmpty() ||
            password.equals("Ingrese su contraseña") || password.isEmpty()) {
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
                JOptionPane.showMessageDialog(pnl, "Login exitoso", "Éxito", JOptionPane.INFORMATION_MESSAGE);
//                frmSistema sistema = new frmSistema(user.getNombreUsuario(), user.getIdUsuario());
//                sistema.setVisible(true);
                app.cargarPnlDefault();
            } else {
                JOptionPane.showMessageDialog(pnl, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // Si el servidor no está corriendo, mostrar mensaje
            JOptionPane.showMessageDialog(pnl, "Error de conexión. Asegúrese que el servidor esté corriendo.\n" + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void addListeners() {
        this.pnl.getPnlLogin().getTxtUser().addActionListener(this);
        this.pnl.getPnlLogin().getTxtEmail().addActionListener(this);
        this.pnl.getPnlLogin().getTxtPass().addActionListener(this);
        this.pnl.getPnlLogin().getCmdForget().addActionListener(this);
        this.pnl.getPnlLogin().getCmdLogin().addActionListener(this);
        
        
    }
}
