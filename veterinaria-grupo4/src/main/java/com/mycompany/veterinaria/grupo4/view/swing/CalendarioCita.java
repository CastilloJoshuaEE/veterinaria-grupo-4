package com.mycompany.veterinaria.grupo4.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Calendario para selección de fecha en citas.
 * Incluye navegación prev/next de mes y marcadores de citas existentes.
 *
 * @author juan
 */
public class CalendarioCita extends JPanel {

    private static final int DAY_HEADER_H = 22; // altura fila de nombres de días

    private Calendar   calendar;
    private Date       fechaSeleccionada;
    private List<Date> fechasConCita = new ArrayList<>();

    // Header components
    private JLabel  lblMes;
    private JPanel  gridPanel;

    // ── Constructor ───────────────────────────────────────────────
    public CalendarioCita() {
        super(new BorderLayout(0, 0));
        setOpaque(false);

        calendar          = Calendar.getInstance();
        fechaSeleccionada = new Date(); // hoy por defecto

        buildHeader();
        buildGrid();
        actualizarLabelMes();

        setPreferredSize(new Dimension(280, 240));
    }

    // ── Header: ◀  Mes Año  ▶ ────────────────────────────────────
    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JButton btnPrev = navBtn("◀");
        JButton btnNext = navBtn("▶");

        lblMes = new JLabel("", JLabel.CENTER);
        lblMes.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblMes.setForeground(new Color(230, 140, 30));

        btnPrev.addActionListener(e -> cambiarMes(-1));
        btnNext.addActionListener(e -> cambiarMes(+1));

        header.add(btnPrev, BorderLayout.WEST);
        header.add(lblMes,  BorderLayout.CENTER);
        header.add(btnNext, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    // ── Grid: nombres de días + días del mes ─────────────────────
    private void buildGrid() {
        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintGrid((Graphics2D) g.create());
            }
        };
        gridPanel.setOpaque(false);
        gridPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onGridClick(e.getX(), e.getY());
            }
        });

        add(gridPanel, BorderLayout.CENTER);
    }

    // ── Pintar el grid ────────────────────────────────────────────
    private void paintGrid(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        boolean isDark       = UIManager.getBoolean("laf.dark");
        Color   clrTexto     = UIManager.getColor("Label.foreground");
        Color   clrHeader    = new Color(230, 140, 30);
        Color   clrSeleccion = new Color(230, 140, 30, 210);
        Color   clrCita      = isDark
            ? new Color(255, 100, 100, 180) : new Color(220, 60, 60, 180);
        Color   clrHoy       = isDark
            ? new Color(100, 180, 255, 120) : new Color(30, 120, 220, 120);

        int w  = gridPanel.getWidth();
        int h  = gridPanel.getHeight();
        int cw = w / 7;
        int ch = (h - DAY_HEADER_H) / 7;

        // ── Nombres de días ───────────────────────────────────────
        String[] dias = {"Do","Lu","Ma","Mi","Ju","Vi","Sá"};
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2.setColor(clrHeader);
        FontMetrics fmHdr = g2.getFontMetrics();
        for (int i = 0; i < 7; i++) {
            String d = dias[i];
            g2.drawString(d,
                i * cw + (cw - fmHdr.stringWidth(d)) / 2,
                DAY_HEADER_H - 4);
        }

        // ── Días del mes ──────────────────────────────────────────
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int offset  = temp.get(Calendar.DAY_OF_WEEK) - 1;
        int diasMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar hoy = Calendar.getInstance();

        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();

        for (int day = 1; day <= diasMes; day++) {
            int pos = offset + day - 1;
            int col = pos % 7;
            int row = pos / 7 + 1;      // fila 0 = encabezado días
            int x   = col * cw;
            int y   = DAY_HEADER_H + row * ch;   // borde inferior de la celda

            temp.set(Calendar.DAY_OF_MONTH, day);
            Date fechaDia = temp.getTime();

            boolean esHoy = hoy.get(Calendar.DAY_OF_YEAR) == temp.get(Calendar.DAY_OF_YEAR)
                && hoy.get(Calendar.YEAR) == temp.get(Calendar.YEAR);
            boolean esSel = fechaSeleccionada != null && mismodia(fechaSeleccionada, fechaDia);
            boolean tieneC = fechasConCita.stream().anyMatch(d -> mismodia(d, fechaDia));

            // Fondo celda
            if (esSel) {
                g2.setColor(clrSeleccion);
                g2.fillOval(x + 2, y - ch + 4, cw - 4, ch - 4);
            } else if (esHoy) {
                g2.setColor(clrHoy);
                g2.fillOval(x + 2, y - ch + 4, cw - 4, ch - 4);
            }

            // Número día
            g2.setColor(esSel ? Color.WHITE : clrTexto);
            String s = String.valueOf(day);
            g2.drawString(s, x + (cw - fm.stringWidth(s)) / 2, y - 4);

            // Punto rojo si hay cita
            if (tieneC) {
                g2.setColor(clrCita);
                g2.fillOval(x + cw / 2 - 3, y - 1, 6, 6);
            }
        }

        g2.dispose();
    }

    // ── Click en el grid → seleccionar día ───────────────────────
    private void onGridClick(int mx, int my) {
        if (my < DAY_HEADER_H) return;          // clic en encabezado días → ignorar

        int cw  = gridPanel.getWidth() / 7;
        int ch  = (gridPanel.getHeight() - DAY_HEADER_H) / 7;
        int col = mx / cw;
        int row = (my - DAY_HEADER_H) / ch;    // fila 0 = primera fila de días

        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int offset = temp.get(Calendar.DAY_OF_WEEK) - 1;
        int day    = row * 7 + col - offset + 1;

        if (day >= 1 && day <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Date anterior = fechaSeleccionada;
            fechaSeleccionada = calendar.getTime();
            gridPanel.repaint();
            firePropertyChange("fechaSeleccionada", anterior, fechaSeleccionada);
        }
    }

    // ── Navegación mes ────────────────────────────────────────────
    private void cambiarMes(int delta) {
        calendar.add(Calendar.MONTH, delta);
        actualizarLabelMes();
        gridPanel.repaint();
    }

    private void actualizarLabelMes() {
        String[] meses = {
            "Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
        };
        lblMes.setText(meses[calendar.get(Calendar.MONTH)]
            + "  " + calendar.get(Calendar.YEAR));
    }

    // ── Helper botón de nav ───────────────────────────────────────
    private JButton navBtn(String txt) {
        JButton b = new JButton(txt);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setForeground(new Color(230, 140, 30));
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ── Utilitario ────────────────────────────────────────────────
    private boolean mismodia(Date a, Date b) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(a);
        c2.setTime(b);
        return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
            && c1.get(Calendar.YEAR)         == c2.get(Calendar.YEAR);
    }

    // ── API pública ───────────────────────────────────────────────
    public Date getFechaSeleccionada() { return fechaSeleccionada; }

    public void setFechaSeleccionada(Date d) {
        this.fechaSeleccionada = d;
        calendar.setTime(d);
        actualizarLabelMes();
        gridPanel.repaint();
    }

    public void setFechasConCita(List<Date> fechas) {
        this.fechasConCita = fechas != null ? fechas : new ArrayList<>();
        gridPanel.repaint();
    }
}