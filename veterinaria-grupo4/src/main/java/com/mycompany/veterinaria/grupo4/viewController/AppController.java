
package com.mycompany.veterinaria.grupo4.viewController;

import com.mycompany.veterinaria.grupo4.view.auth.FrmPrincipal;
import com.mycompany.veterinaria.grupo4.view.auth.PnlBgLogin;
import javax.swing.JPanel;

/**
 *
 * @author juan
 */
public class AppController {
    //Vistas
    private FrmPrincipal frm;
    private PnlBgLogin bgLogin;
    private JPanel bgPnlPrincipal;
    
    //Controladores
    private AuthController authcontroller;

    public AppController(FrmPrincipal frm) {
        this.frm = frm;
        bgPnlPrincipal = frm.getBgPrincipal().getPnlVentana();
        cargarLogin();
    }
    
    private void cargarLogin(){
        if(bgLogin == null){
            bgLogin = new PnlBgLogin();
            authcontroller = new AuthController(bgPnlPrincipal,bgLogin);
        }
        bgPnlPrincipal.removeAll();
        bgPnlPrincipal.add(bgLogin);
        bgPnlPrincipal.revalidate();      // ← Refresca el layout
        bgPnlPrincipal.repaint(); 
    } 
}
