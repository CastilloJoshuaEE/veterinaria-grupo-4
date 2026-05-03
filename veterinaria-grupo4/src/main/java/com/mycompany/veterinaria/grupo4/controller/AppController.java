
package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.view.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import com.mycompany.veterinaria.grupo4.view.PnlMain;
import com.mycompany.veterinaria.grupo4.view.Dashboard;
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
                        default -> action.cancel();
                    }
                }
                case 3 -> {
                    switch (subIndex) {
                        //case 1 -> mostrarMascotas(); cambiar a nueva mascota
                        case 2 -> mostrarMascotas();
                        default -> action.cancel();
                    }
                }
                case 4 -> {
                    switch (subIndex) {
                        //case 1 -> mostrarClientes(); Cambiar a nuevo cliente
                        case 2 -> mostrarClientes();
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
        PnlMascota pnl = new PnlMascota();
        CtrlMascotas ctrl = new CtrlMascotas(pnl); // el controller llena la tabla
        main.showForm(pnl);
    }

    private void mostrarCitas() {
//        PnlCita pnl = new PnlCita();
//        new CitaController(pnl);
//        main.showForm(pnl);
    }

    private void mostrarClientes() {
        PnlCliente pnl = new PnlCliente();
        CtrlCliente ctrlCliente = new CtrlCliente(pnl);
        main.showForm(pnl);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(frm,
            "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            main = null; // fuerza recrear el panel si vuelve a entrar
            cargarLogin();
        }
    }
}
