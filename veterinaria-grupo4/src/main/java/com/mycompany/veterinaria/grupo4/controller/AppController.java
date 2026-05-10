package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.view.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import com.mycompany.veterinaria.grupo4.view.PnlMain;
import com.mycompany.veterinaria.grupo4.view.Dashboard;
import com.mycompany.veterinaria.grupo4.view.cita.PnlCita;
import com.mycompany.veterinaria.grupo4.view.cliente.PnlCliente;
import com.mycompany.veterinaria.grupo4.view.mascota.PnlMascota;
import com.mycompany.veterinaria.grupo4.view.personalVeterinario.PnlVeterinario;
import com.mycompany.veterinaria.grupo4.view.swing.menu.MenuAction;
import com.mycompany.veterinaria.grupo4.view.historial.PnlHistorialMedico;
import com.mycompany.veterinaria.grupo4.view.servicio.PnlServicio;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 *
 * @author juan
 */
public class AppController {
    //Vistas
    private FrmPrincipal frm;
    private PnlBgLogin bgLogin;
    private PnlMain main;
    private PnlCliente pnlCliente;
    private PnlCita pnlCita;
    private PnlMascota pnlMascota;
    private PnlVeterinario pnlVeterinario;
    private PnlHistorialMedico pnlHistorialMedico;
    private PnlServicio pnlServicio;
    
    //Controladores
    private AuthController authcontroller;

    public AppController(FrmPrincipal frm) {
        this.frm = frm;
        cargarLogin();
    }
    
    private void cargarLogin(){
        // Crear nuevo login
        bgLogin = new PnlBgLogin();
        authcontroller = new AuthController(this, bgLogin);
        
        // Obtener el panel principal y limpiarlo completamente
        JPanel bgPrincipal = frm.getBgPrincipal();
        bgPrincipal.removeAll();
        bgPrincipal.setLayout(new BorderLayout());
        
        // Crear un nuevo Background con su pnlVentana para contener el login
        com.mycompany.veterinaria.grupo4.view.auth.Background bgLoginContainer = frm.getBgLogin();
        JPanel pnlVentana = bgLoginContainer.getPnlVentana();
        pnlVentana.removeAll();
        pnlVentana.setLayout(new BorderLayout());
        pnlVentana.add(bgLogin, BorderLayout.CENTER);
        pnlVentana.revalidate();
        pnlVentana.repaint();
        
        // Agregar el contenedor al panel principal
        bgPrincipal.add(bgLoginContainer, BorderLayout.CENTER);
        bgPrincipal.revalidate();      
        bgPrincipal.repaint();
        
        // Asegurar que el frame principal se actualice
        frm.revalidate();
        frm.repaint();
    } 
    
    public void cargarPnlDefault(){
        if(main == null){
            main = new PnlMain();
            initMenuEvent();
        }
        JPanel bg = frm.getBgPrincipal();
        bg.removeAll();
        bg.setLayout(new BorderLayout());
        bg.add(main, BorderLayout.CENTER);
        bg.revalidate();      
        bg.repaint(); 
    }
    
    // ── Aquí vive la lógica del menú ────────────────────────────
    private void initMenuEvent() {
        main.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            System.out.println("index: "+index + "subIndex: "+subIndex);
            switch (index) {
                case 0 -> mostrarDashboard();
                case 1 -> {
                    switch (subIndex) {
                        case 1 -> mostrarCitas();
                        default -> action.cancel();
                    }
                }
                case 3 -> {
                    switch (subIndex) {
                        case 1 -> mostrarMascotas(); 
                        case 2 -> mostrarHistorialMedico();
                        default -> action.cancel();
                    }
                }
                case 4 -> {
                    switch (subIndex) {
                        case 1 -> mostrarClientes(); 
                        default -> action.cancel();
                    }
                }
                case 5 -> {
                    switch (subIndex) {
                        case 3 -> mostrarServicios();  // Servicios en índice 5, subIndex 3
                        default -> action.cancel();
                    }
                }
                case 7 -> mostrarPersonalVeterinario();
                case 9 -> cerrarSesion();
                default -> action.cancel();
            }
        });
    }
    
    // ── Métodos de navegación ────────────────────────────────────
    private void mostrarDashboard() {
        main.showForm(new Dashboard());
    }

    private void mostrarMascotas() {
        if (pnlMascota == null) pnlMascota = new PnlMascota();
        CtrlMascotas ctrl = new CtrlMascotas(pnlMascota);
        main.showForm(pnlMascota);
    }

    private void mostrarCitas() {
        if (pnlCita == null) pnlCita = new PnlCita();
        CtrlCitas ctrl = new CtrlCitas(pnlCita);
        main.showForm(pnlCita);
    }

    private void mostrarClientes() {
        if (pnlCliente == null) pnlCliente = new PnlCliente();
        CtrlCliente ctrlCliente = new CtrlCliente(pnlCliente);
        main.showForm(pnlCliente);
    }
    
    private void mostrarPersonalVeterinario() {
        if (pnlVeterinario == null) pnlVeterinario = new PnlVeterinario();
        CtrlVeterinario ctrlVeterinario = new CtrlVeterinario(pnlVeterinario);
        main.showForm(pnlVeterinario);
    }
    private void mostrarHistorialMedico() {
        if (pnlHistorialMedico == null) {
            pnlHistorialMedico = new PnlHistorialMedico();
        }
        // Crear el controlador del historial
        CtrlHistorialMedico ctrlHistorial = new CtrlHistorialMedico(pnlHistorialMedico);
        main.showForm(pnlHistorialMedico);
    }
    private void mostrarServicios() {
        if (pnlServicio == null) pnlServicio = new PnlServicio();
        CtrlServicio ctrl = new CtrlServicio(pnlServicio);
        main.showForm(pnlServicio);
    }
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(frm,
            "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Limpiar referencias de los paneles
            main = null;
            pnlCliente = null;
            pnlCita = null;
            pnlMascota = null;
            bgLogin = null;
            authcontroller = null;
            
            // Recargar el login
            cargarLogin();
        }
    }
}