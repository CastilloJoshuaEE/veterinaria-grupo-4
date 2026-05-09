
package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.view.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import com.mycompany.veterinaria.grupo4.view.PnlMain;
import com.mycompany.veterinaria.grupo4.view.Dashboard;
import com.mycompany.veterinaria.grupo4.view.cita.PnlCita;
import com.mycompany.veterinaria.grupo4.view.cliente.PnlCliente;
import com.mycompany.veterinaria.grupo4.view.mascota.PnlMascota;
import com.mycompany.veterinaria.grupo4.view.swing.menu.MenuAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author juan
 */
public class AppController {
    //Vistas
    private FrmPrincipal frm;
    private PnlBgLogin bgLogin;
    private JPanel bgPnlLogin;
    private PnlMain main;
    private PnlCliente pnlCliente;
    private PnlCita pnlCita;
    private PnlMascota pnlMascota;
    
    //Controladores
    private AuthController authcontroller;

    public AppController(FrmPrincipal frm) {
        this.frm = frm;
        bgPnlLogin = frm.getBgLogin().getPnlVentana();
        cargarLogin();
    }
    
    private void cargarLogin(){
        if(bgLogin == null){
            bgLogin = new PnlBgLogin();
            authcontroller = new AuthController(this,bgLogin);
        }
        bgPnlLogin.removeAll();
        bgPnlLogin.add(bgLogin);
        bgPnlLogin.revalidate();      
        bgPnlLogin.repaint(); 
    } 
    public void cargarPnlDefault(){
        if(main == null){
            main = new PnlMain();
            initMenuEvent();
        }
        JPanel bg = frm.getBgPrincipal();
        bg.removeAll();
        bg.add(main, java.awt.BorderLayout.CENTER);
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
                        //case 2 -> mostrarMascotas();
                        default -> action.cancel();
                    }
                }
                case 4 -> {
                    switch (subIndex) {
                        case 1 -> mostrarClientes(); 
                        //case 2 -> mostrarClientes();
                        default -> action.cancel();
                    }
                }
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
        CtrlMascotas ctrl = new CtrlMascotas(pnlMascota); // el controller llena la tabla
        main.showForm(pnlMascota);
    }

    private void mostrarCitas() {
        if (pnlCita == null)pnlCita = new PnlCita();
        CtrlCitas ctrl = new CtrlCitas(pnlCita);
        main.showForm(pnlCita);
    }

    private void mostrarClientes() {
        if (pnlCliente == null) pnlCliente = new PnlCliente();
        CtrlCliente ctrlCliente = new CtrlCliente(pnlCliente);
        main.showForm(pnlCliente);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(frm,
            "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            main = null;
            cargarLogin();
        }
    }
}
