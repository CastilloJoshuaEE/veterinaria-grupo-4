package com.mycompany.veterinaria.grupo4.view.swing.menu;

import com.mycompany.veterinaria.grupo4.util.SessionManager;
import com.mycompany.veterinaria.grupo4.view.swing.menu.mode.LightDarkMode;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import com.mycompany.veterinaria.grupo4.view.swing.menu.mode.ToolBarAccentColor;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Menú principal de la aplicación con filtro de opciones según el rol del usuario.
 * 
 * @author ROBLES MORALES JUAN ANDRES, CHILAN CHILAN DANNY ANDRES
 * @version 2.0
 * @since 1.0
 */
public class Menu extends JPanel {

    // ========== DEFINICIÓN DE MENÚS POR ROL ==========
    
    /**
     * Menú para ADMINISTRADOR - Acceso completo a todas las funcionalidades
     * ÍNDICES:
     * 0: Bienvenida (Dashboard)
     * 1: Citas Médicas
     * 2: Atención Médica
     * 3: Mascotas (subíndices: 0=Mascotas, 1=Ver todos, 2=Historial Médico)
     * 4: Clientes
     * 5: Inventario (subíndices: 0=Inventario, 3=Servicios)
     * 6: Facturación
     * 7: Personal Veterinario
     * 8: Configuración
     * 9: Ayuda (subíndices: 0=Ayuda, 1=Ver Documentación, 2=Acerca de)
     * 10: Cerrar Sesión
     */
    private final String[][] menuAdministrador = {
        {"~PRINCIPAL~"},
            {"Bienvenida"},                    // índice 0
            {"Citas Médicas"},                 // índice 1
        {"~CLÍNICA~"},
            {"Atención Médica"},               // índice 2
            {"Mascotas",
                "Ver todos",
                "Historial Médico"},           // índice 3 (sub: 0,1,2)
            {"Clientes"},                      // índice 4
        {"~ADMINISTRACIÓN~"},
            {"Inventario",
                "Servicios"},                  // índice 5 (sub: 0,3)
            {"Facturación"},                   // índice 6
            {"Personal Veterinario"},          // índice 7
        {"~SISTEMA~"},
            {"Configuración"},                 // índice 8
            {"Ayuda",           
                "Ver Documentación",
                "Acerca de"},                  // índice 9 (sub: 0,1,2)
            {"Cerrar Sesión"}                  // índice 10
    };

    /**
     * Menú para RECEPCIONISTA - Funcionalidades de atención al cliente
     * ÍNDICES:
     * 0: Bienvenida (Dashboard)
     * 1: Citas Médicas
     * 2: Mascotas (subíndices: 0=Mascotas, 1=Ver todos, 2=Historial Médico)
     * 3: Clientes
     * 4: Facturación
     * 5: Ayuda (subíndices: 0=Ayuda, 1=Ver Documentación, 2=Acerca de)
     * 6: Cerrar Sesión
     */
    private final String[][] menuRecepcionista = {
        {"~PRINCIPAL~"},
            {"Bienvenida"},                    // índice 0
            {"Citas Médicas"},                 // índice 1
        {"~CLÍNICA~"},
            {"Mascotas",
                "Ver todos",
                "Historial Médico"},           // índice 2 (sub: 0,1,2)
            {"Clientes"},                      // índice 3
        {"~ADMINISTRACIÓN~"},
            {"Facturación"},                   // índice 4
        {"~SISTEMA~"},
            {"Ayuda",           
                "Ver Documentación",
                "Acerca de"},                  // índice 5 (sub: 0,1,2)
            {"Cerrar Sesión"}                  // índice 6
    };

    /**
     * Menú para VETERINARIO - Funcionalidades médicas
     * ÍNDICES:
     * 0: Bienvenida (Dashboard)
     * 1: Atención Médica
     * 2: Mascotas (subíndices: 0=Mascotas, 1=Ver todos, 2=Historial Médico)
     * 3: Clientes
     * 4: Facturación
     * 5: Personal Veterinario
     * 6: Ayuda (subíndices: 0=Ayuda, 1=Ver Documentación, 2=Acerca de)
     * 7: Cerrar Sesión
     */
    private final String[][] menuVeterinario = {
        {"~PRINCIPAL~"},
            {"Bienvenida"},                    // índice 0
        {"~CLÍNICA~"},
            {"Atención Médica"},               // índice 1
            {"Mascotas",
                "Ver todos",
                "Historial Médico"},           // índice 2 (sub: 0,1,2)
            {"Clientes"},                      // índice 3
        {"~ADMINISTRACIÓN~"},
            {"Facturación"},                   // índice 4
            {"Personal Veterinario"},          // índice 5
        {"~SISTEMA~"},
            {"Ayuda",           
                "Ver Documentación",
                "Acerca de"},                  // índice 6 (sub: 0,1,2)
            {"Cerrar Sesión"}                  // índice 7
    };

    // ========== VARIABLES DE INSTANCIA ==========

    private String[][] menuItems;
    private boolean menuFull = true;
    private final String headerName = "PET TOWN";
    private final List<MenuEvent> events = new ArrayList<>();

    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 5;
    protected final int menuTitleVgap = 5;
    protected final int menuMaxWidth = 200;
    protected final int menuMinWidth = 60;
    protected final int headerFullHgap = 5;

    private JLabel header;
    private JScrollPane scroll;
    private JPanel panelMenu;
    private LightDarkMode lightDarkMode;
    private ToolBarAccentColor toolBarAccentColor;

    // ========== CONSTRUCTOR ==========

    public Menu() {
        // Seleccionar el menú según el rol del usuario
        String rol = SessionManager.getInstance().getCurrentUserRole();
        if (rol == null) {
            rol = "ADMINISTRADOR";
        }
        
        System.out.println("Rol del usuario: " + rol); // Debug
        
        switch (rol.toUpperCase()) {
            case "RECEPCIONISTA":
                this.menuItems = menuRecepcionista;
                break;
            case "VETERINARIO":
                this.menuItems = menuVeterinario;
                break;
            case "ADMINISTRADOR":
            default:
                this.menuItems = menuAdministrador;
                break;
        }
        
        init();
    }

    // ========== INICIALIZACIÓN ==========

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");
        
        header = new JLabel(headerName);
        header.setIcon(new ImageIcon(getClass().getResource("/image/LOGO-ICON.png")));
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.header.font;"
                + "foreground:$Menu.foreground");

        // Menú
        scroll = new JScrollPane();
        panelMenu = new JPanel(new MenuItemLayout(this));
        panelMenu.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5;"
                + "background:$Menu.background");

        scroll.setViewportView(panelMenu);
        scroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:null");
        
        JScrollBar vscroll = scroll.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);
        vscroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "width:$Menu.scroll.width;"
                + "trackInsets:$Menu.scroll.trackInsets;"
                + "thumbInsets:$Menu.scroll.thumbInsets;"
                + "background:$Menu.ScrollBar.background;"
                + "thumb:$Menu.ScrollBar.thumb");
        
        createMenu();
        
        lightDarkMode = new LightDarkMode();
        toolBarAccentColor = new ToolBarAccentColor(this);
        toolBarAccentColor.setVisible(FlatUIUtils.getUIBoolean("AccentControl.show", false));
        
        add(header);
        add(scroll);
        add(lightDarkMode);
        add(toolBarAccentColor);
    }

    // ========== CREACIÓN DEL MENÚ ==========

    private void createMenu() {
        int index = 0;
        for (int i = 0; i < menuItems.length; i++) {
            String menuName = menuItems[i][0];
            if (menuName.startsWith("~") && menuName.endsWith("~")) {
                panelMenu.add(createTitle(menuName));
            } else {
                MenuItem menuItem = new MenuItem(this, menuItems[i], index++, events);
                panelMenu.add(menuItem);
            }
        }
    }

    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.label.font;"
                + "foreground:$Menu.title.foreground");
        return lbTitle;
    }

    // ========== MÉTODOS PÚBLICOS ==========

    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        if (menuFull) {
            header.setText(headerName);
            header.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JLabel.LEFT : JLabel.RIGHT);
        } else {
            header.setText("");
            header.setHorizontalAlignment(JLabel.CENTER);
        }
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setFull(menuFull);
            }
        }
        lightDarkMode.setMenuFull(menuFull);
        toolBarAccentColor.setMenuFull(menuFull);
    }

    public void setSelectedMenu(int index, int subIndex) {
        runEvent(index, subIndex);
    }

    protected void setSelected(int index, int subIndex) {
        int size = panelMenu.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component com = panelMenu.getComponent(i);
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.getMenuIndex() == index) {
                    item.setSelectedIndex(subIndex);
                } else {
                    item.setSelectedIndex(-1);
                }
            }
        }
    }

    protected void runEvent(int index, int subIndex) {
        MenuAction menuAction = new MenuAction();
        for (MenuEvent event : events) {
            event.menuSelected(index, subIndex, menuAction);
        }
        if (!menuAction.isCancel()) {
            setSelected(index, subIndex);
        }
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }

    public int getMenuTitleLeftInset() {
        return menuTitleLeftInset;
    }

    public int getMenuTitleVgap() {
        return menuTitleVgap;
    }

    public int getMenuMaxWidth() {
        return menuMaxWidth;
    }

    public int getMenuMinWidth() {
        return menuMinWidth;
    }

    // ========== CLASE INTERNA PARA LAYOUT ==========

    private class MenuLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = UIScale.scale(5);
                int sheaderFullHgap = UIScale.scale(headerFullHgap);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int iconWidth = width;
                int iconHeight = header.getPreferredSize().height;
                int hgap = menuFull ? sheaderFullHgap : 0;
                int accentColorHeight = 0;
                if (toolBarAccentColor.isVisible()) {
                    accentColorHeight = toolBarAccentColor.getPreferredSize().height + gap;
                }

                header.setBounds(x + hgap, y, iconWidth - (hgap * 2), iconHeight);
                int ldgap = UIScale.scale(10);
                int ldWidth = width - ldgap * 2;
                int ldHeight = lightDarkMode.getPreferredSize().height;
                int ldx = x + ldgap;
                int ldy = y + height - ldHeight - ldgap - accentColorHeight;

                int menux = x;
                int menuy = y + iconHeight + gap;
                int menuWidth = width;
                int menuHeight = height - (iconHeight + gap) - (ldHeight + ldgap * 2) - (accentColorHeight);
                scroll.setBounds(menux, menuy, menuWidth, menuHeight);

                lightDarkMode.setBounds(ldx, ldy, ldWidth, ldHeight);

                if (toolBarAccentColor.isVisible()) {
                    int tbheight = toolBarAccentColor.getPreferredSize().height;
                    int tbwidth = Math.min(toolBarAccentColor.getPreferredSize().width, ldWidth);
                    int tby = y + height - tbheight - ldgap;
                    int tbx = ldx + ((ldWidth - tbwidth) / 2);
                    toolBarAccentColor.setBounds(tbx, tby, tbwidth, tbheight);
                }
            }
        }
    }
}