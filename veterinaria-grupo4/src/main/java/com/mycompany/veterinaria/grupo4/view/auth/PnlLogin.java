package com.mycompany.veterinaria.grupo4.view.auth;

import com.mycompany.veterinaria.grupo4.view.swing.Button;
import com.mycompany.veterinaria.grupo4.view.swing.MyPasswordField;
import com.mycompany.veterinaria.grupo4.view.swing.MyTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

/**
 * Panel de login con campos de usuario, email, contraseña y botones.
 * 
 * @author juan
 */
public class PnlLogin extends javax.swing.JLayeredPane {
    private MigLayout layout;
    
    // Campos para el registro de recepcionista
    private MyTextField txtCedula;
    private MyTextField txtNombre;
    private MyTextField txtApellido;
    private MyTextField txtTelefono;
    private MyTextField txtDireccion;
    private MyTextField txtEmailRegistro;
    private MyPasswordField txtPassRegistro;
    private JButton btnTogglePasswordRegistro;
    private Button cmdRegistrar;
    
    // Campos para el login (manteniendo compatibilidad)
    private MyTextField txtEmailLogin;
    private MyPasswordField txtPassLogin;
    private JButton btnTogglePasswordLogin;
    private JButton cmdForget;
    private Button cmdLogin;
    
    // Iconos
    private ImageIcon iconUser;
    private ImageIcon iconMail;
    private ImageIcon iconPass;
    private ImageIcon iconPhone;
    private ImageIcon iconHome;
    private ImageIcon iconEye;
    private ImageIcon iconEyeOff;
    
    /**
     * Creates new form PnlLogin
     */
    public PnlLogin() {
        initComponents();
        cargarIconos();
        initRegister();
        initLogin();
        login.setVisible(false);
        register.setVisible(true);
        
        setBorder(new EmptyBorder(10,10,10,10));
        setOpaque(false);
        this.setBounds(0, 0, 375, 400);
        setPreferredSize(new Dimension(375, 400));
    }
    
    private void cargarIconos() {
        try {
            iconUser = new ImageIcon(getClass().getResource("/icon/user.png"));
            iconMail = new ImageIcon(getClass().getResource("/icon/mail.png"));
            iconPass = new ImageIcon(getClass().getResource("/icon/pass.png"));
            iconPhone = new ImageIcon(getClass().getResource("/icon/phone.png"));
            iconHome = new ImageIcon(getClass().getResource("/icon/home.png"));
            iconEye = new ImageIcon(getClass().getResource("/icon/eye.png"));
            iconEyeOff = new ImageIcon(getClass().getResource("/icon/eye-off.png"));
        } catch (Exception e) {
            // Si falla la carga de iconos, usar null (se manejará con try-catch en cada uso)
            //System.err.println("Error cargando iconos: " + e.getMessage());
        }
    }
    
    private ImageIcon getIconSafe(String nombre) {
        try {
            return new ImageIcon(getClass().getResource("/icon/" + nombre));
        } catch (Exception e) {
            return null;
        }
    }
    
    private void initRegister() {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]5[]5[]5[]5[]5[]5[]5[]push"));
        
        JLabel label = new JLabel("Crea tu cuenta");
        label.setFont(new Font("sansserif", 1, 24));
        label.setForeground(new Color(255,178,39));
        register.add(label, "gapbottom 10");
        
        // Cédula
        txtCedula = new MyTextField();
        ImageIcon iconUserSafe = getIconSafe("user.png");
        if (iconUserSafe != null) txtCedula.setPrefixIcon(iconUserSafe);
        txtCedula.setHint("Cédula");
        register.add(txtCedula, "w 70%");
        
        // Nombre
        txtNombre = new MyTextField();
        if (iconUserSafe != null) txtNombre.setPrefixIcon(iconUserSafe);
        txtNombre.setHint("Nombre");
        register.add(txtNombre, "w 70%");
        
        // Apellido
        txtApellido = new MyTextField();
        if (iconUserSafe != null) txtApellido.setPrefixIcon(iconUserSafe);
        txtApellido.setHint("Apellido");
        register.add(txtApellido, "w 70%");
        
        // Teléfono
        txtTelefono = new MyTextField();
        ImageIcon iconPhoneSafe = getIconSafe("phone.png");
        if (iconPhoneSafe != null) txtTelefono.setPrefixIcon(iconPhoneSafe);
        txtTelefono.setHint("Teléfono");
        register.add(txtTelefono, "w 70%");
        
        // Dirección
        txtDireccion = new MyTextField();
        ImageIcon iconHomeSafe = getIconSafe("home.png");
        if (iconHomeSafe != null) txtDireccion.setPrefixIcon(iconHomeSafe);
        txtDireccion.setHint("Dirección (opcional)");
        register.add(txtDireccion, "w 70%");
        
        // Email
        txtEmailRegistro = new MyTextField();
        ImageIcon iconMailSafe = getIconSafe("mail.png");
        if (iconMailSafe != null) txtEmailRegistro.setPrefixIcon(iconMailSafe);
        txtEmailRegistro.setHint("Correo electrónico");
        register.add(txtEmailRegistro, "w 70%");
        
        // Contraseña con botón mostrar/ocultar
        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passPanel.setOpaque(false);
        passPanel.setPreferredSize(new Dimension(0, 38));
        
        txtPassRegistro = new MyPasswordField();
        ImageIcon iconPassSafe = getIconSafe("pass.png");
        if (iconPassSafe != null) txtPassRegistro.setPrefixIcon(iconPassSafe);
        txtPassRegistro.setHint("Contraseña");
        passPanel.add(txtPassRegistro, BorderLayout.CENTER);
        
        btnTogglePasswordRegistro = new JButton();
        ImageIcon iconEyeOffSafe = getIconSafe("eye-off.png");
        if (iconEyeOffSafe != null) btnTogglePasswordRegistro.setIcon(iconEyeOffSafe);
        btnTogglePasswordRegistro.setBorderPainted(false);
        btnTogglePasswordRegistro.setContentAreaFilled(false);
        btnTogglePasswordRegistro.setFocusPainted(false);
        btnTogglePasswordRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTogglePasswordRegistro.setPreferredSize(new Dimension(35, 35));
        btnTogglePasswordRegistro.addActionListener(e -> {
            if (txtPassRegistro.getEchoChar() == '\u2022' || txtPassRegistro.getEchoChar() == '*') {
                txtPassRegistro.setEchoChar((char) 0);
                ImageIcon iconEyeSafe = getIconSafe("eye.png");
                if (iconEyeSafe != null) btnTogglePasswordRegistro.setIcon(iconEyeSafe);
            } else {
                txtPassRegistro.setEchoChar('*');
                ImageIcon iconEyeOffSafe2 = getIconSafe("eye-off.png");
                if (iconEyeOffSafe2 != null) btnTogglePasswordRegistro.setIcon(iconEyeOffSafe2);
            }
        });
        passPanel.add(btnTogglePasswordRegistro, BorderLayout.EAST);
        register.add(passPanel, "w 70%");
        
        cmdRegistrar = new Button();
        cmdRegistrar.setBackground(new Color(255,178,39));
        cmdRegistrar.setForeground(new Color(250, 250, 250));
        cmdRegistrar.setText("Registrarse");
        register.add(cmdRegistrar, "w 50%, h 40, gapbottom 5");
    }
    
    private void initLogin() {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Iniciar Sesión");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(255,178,39));
        login.add(label);
        
        txtEmailLogin = new MyTextField();
        ImageIcon iconUserSafe = getIconSafe("user.png");
        if (iconUserSafe != null) txtEmailLogin.setPrefixIcon(iconUserSafe);
        txtEmailLogin.setHint("Email");
        login.add(txtEmailLogin, "w 60%");
        
        // Campo de contraseña con botón de mostrar/ocultar
        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passPanel.setOpaque(false);
        passPanel.setPreferredSize(new Dimension(0, 38));
        
        txtPassLogin = new MyPasswordField();
        ImageIcon iconPassSafe = getIconSafe("pass.png");
        if (iconPassSafe != null) txtPassLogin.setPrefixIcon(iconPassSafe);
        txtPassLogin.setHint("Contraseña");
        passPanel.add(txtPassLogin, BorderLayout.CENTER);
        
        // Botón para mostrar/ocultar contraseña
        btnTogglePasswordLogin = new JButton();
        ImageIcon iconEyeOffSafe = getIconSafe("eye-off.png");
        if (iconEyeOffSafe != null) btnTogglePasswordLogin.setIcon(iconEyeOffSafe);
        btnTogglePasswordLogin.setBorderPainted(false);
        btnTogglePasswordLogin.setContentAreaFilled(false);
        btnTogglePasswordLogin.setFocusPainted(false);
        btnTogglePasswordLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTogglePasswordLogin.setPreferredSize(new Dimension(35, 35));
        btnTogglePasswordLogin.addActionListener(e -> {
            if (txtPassLogin.getEchoChar() == '\u2022' || txtPassLogin.getEchoChar() == '*') {
                txtPassLogin.setEchoChar((char) 0);
                ImageIcon iconEyeSafe = getIconSafe("eye.png");
                if (iconEyeSafe != null) btnTogglePasswordLogin.setIcon(iconEyeSafe);
            } else {
                txtPassLogin.setEchoChar('*');
                ImageIcon iconEyeOffSafe2 = getIconSafe("eye-off.png");
                if (iconEyeOffSafe2 != null) btnTogglePasswordLogin.setIcon(iconEyeOffSafe2);
            }
        });
        passPanel.add(btnTogglePasswordLogin, BorderLayout.EAST);
        login.add(passPanel, "w 60%");
        
        cmdForget = new JButton("Olvidaste tu contraseña?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);
        
        cmdLogin = new Button();
        cmdLogin.setBackground(new Color(255,178,39));
        cmdLogin.setForeground(Color.white);
        cmdLogin.setText("Iniciar Sesión");
        login.add(cmdLogin, "w 40%, h 40");
    }
    
    public void showRegister(boolean show) {
        java.awt.CardLayout cl = (java.awt.CardLayout) this.getLayout();
        if (show) {
            cl.show(this, "register");
        } else {
            cl.show(this, "login");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setBackground(java.awt.Color.white);
        setMaximumSize(new java.awt.Dimension(375, 400));
        setPreferredSize(new java.awt.Dimension(375, 400));
        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));
        login.setPreferredSize(new java.awt.Dimension(375, 400));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        add(login, "login");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        add(register, "register");
    }// </editor-fold>

    // ========== GETTERS PARA REGISTRO ==========
    public MyTextField getTxtCedula() {
        return txtCedula;
    }

    public MyTextField getTxtNombre() {
        return txtNombre;
    }

    public MyTextField getTxtApellido() {
        return txtApellido;
    }

    public MyTextField getTxtTelefono() {
        return txtTelefono;
    }

    public MyTextField getTxtDireccion() {
        return txtDireccion;
    }

    public MyTextField getTxtEmailRegistro() {
        return txtEmailRegistro;
    }

    public MyPasswordField getTxtPassRegistro() {
        return txtPassRegistro;
    }

    public Button getCmdRegistrar() {
        return cmdRegistrar;
    }

    // ========== GETTERS PARA LOGIN ==========
    public MyTextField getTxtEmailLogin() {
        return txtEmailLogin;
    }

    public MyPasswordField getTxtPassLogin() {
        return txtPassLogin;
    }

    public JButton getCmdForget() {
        return cmdForget;
    }

    public Button getCmdLogin() {
        return cmdLogin;
    }

    // ========== GETTERS GENERICOS (para compatibilidad con AuthController original) ==========
    public MyTextField getTxtUser() {
        return txtNombre;
    }

    public MyTextField getTxtEmail() {
        return txtEmailLogin;
    }

    public MyPasswordField getTxtPass() {
        return txtPassLogin;
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration
}