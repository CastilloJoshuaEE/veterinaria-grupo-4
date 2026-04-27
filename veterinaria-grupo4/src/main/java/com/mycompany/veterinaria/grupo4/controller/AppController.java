
package com.mycompany.veterinaria.grupo4.controller;

import com.mycompany.veterinaria.grupo4.view.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import com.mycompany.veterinaria.grupo4.view.pnlMain;
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
    private pnlMain main;
    
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
            main = new pnlMain();
        }
        JPanel bg = frm.getBgPrincipal();
        bg.removeAll();
        bg.add(main, java.awt.BorderLayout.CENTER);
        bg.revalidate();      
        bg.repaint(); 
    }
}
