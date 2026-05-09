package com.mycompany.veterinaria.grupo4.view;

import com.mycompany.veterinaria.grupo4.model.entity.Cita;
import com.mycompany.veterinaria.grupo4.model.entity.Recordatorio;
import com.mycompany.veterinaria.grupo4.service.CitaService;
import com.mycompany.veterinaria.grupo4.service.RecordatorioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class frmCalendarioCitasMedicas extends JFrame {
    private CitaService citaService = new CitaService();
    private RecordatorioService recordatorioService = new RecordatorioService();
    
    private JMonthCalendar monthCalendar;
    private JTable tblCitas;
    private DefaultTableModel modelCitas;
    private JButton btnHoy, btnAnterior, btnSiguiente;
    private JLabel lblTitulo;
    private String nombreUsuario;
    private int idUsuario;
    private Timer timerRecordatorios;
    
    public frmCalendarioCitasMedicas(String nombreUsuario, int idUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        initComponents();
        cargarCitasDelMes();
        iniciarTimerRecordatorios();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Calendario de Citas Médicas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 102, 204));
        
        lblTitulo = new JLabel("Calendario de Citas Médicas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        topPanel.add(lblTitulo, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Calendario
        monthCalendar = new JMonthCalendar();
        monthCalendar.addPropertyChangeListener("selectedDate", evt -> cargarCitasDelDia());
        
        // Panel de botones del calendario
        JPanel calendarButtons = new JPanel(new FlowLayout());
        btnAnterior = new JButton("< Mes");
        btnHoy = new JButton("Hoy");
        btnSiguiente = new JButton("Mes >");
        calendarButtons.add(btnAnterior);
        calendarButtons.add(btnHoy);
        calendarButtons.add(btnSiguiente);
        
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.add(monthCalendar, BorderLayout.CENTER);
        calendarPanel.add(calendarButtons, BorderLayout.SOUTH);
        calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendario"));
        
        mainPanel.add(calendarPanel, BorderLayout.WEST);
        
        // Tabla de citas
        JPanel citasPanel = new JPanel(new BorderLayout());
        citasPanel.setBorder(BorderFactory.createTitledBorder("Citas del día"));
        
        modelCitas = new DefaultTableModel(new String[]{"Hora", "Cliente", "Mascota", "Servicio", "Veterinario", "Estado"}, 0);
        tblCitas = new JTable(modelCitas);
        tblCitas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblCitas.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblCitas.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblCitas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblCitas.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        citasPanel.add(new JScrollPane(tblCitas), BorderLayout.CENTER);
        
        mainPanel.add(citasPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Eventos
        btnAnterior.addActionListener(e -> mesAnterior());
        btnSiguiente.addActionListener(e -> mesSiguiente());
        btnHoy.addActionListener(e -> irHoy());
        
        cargarCitasDelDia();
    }
    
    private void cargarCitasDelMes() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(monthCalendar.getSelectedDate());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date inicio = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date fin = cal.getTime();
        
        List<Cita> citas = citaService.listarPorRangoFechas(inicio, fin);
        if (citas != null) {
            monthCalendar.clearBoldedDates();
            for (Cita c : citas) {
                monthCalendar.addBoldedDate(c.getFechaHora());
            }
            monthCalendar.repaint();
        }
    }
    
    private void cargarCitasDelDia() {
        modelCitas.setRowCount(0);
        Date fecha = monthCalendar.getSelectedDate();
        List<Cita> citas = citaService.listarPorFecha(fecha);
        
        if (citas != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            for (Cita c : citas) {
                modelCitas.addRow(new Object[]{
                    sdf.format(c.getFechaHora()),
                    "Cliente ID: " + c.getCliente().getIdCliente(),
                    "-",
                    "Servicio ID: " + c.getServicio().getIdServicio(),
                    "-",
                    c.getEstado()
                });
            }
        }
        
        if (modelCitas.getRowCount() == 0) {
            modelCitas.addRow(new Object[]{"-", "No hay citas para esta fecha", "-", "-", "-", "-"});
        }
    }
    
    private void mesAnterior() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(monthCalendar.getSelectedDate());
        cal.add(Calendar.MONTH, -1);
        monthCalendar.setSelectedDate(cal.getTime());
        cargarCitasDelMes();
        cargarCitasDelDia();
    }
    
    private void mesSiguiente() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(monthCalendar.getSelectedDate());
        cal.add(Calendar.MONTH, 1);
        monthCalendar.setSelectedDate(cal.getTime());
        cargarCitasDelMes();
        cargarCitasDelDia();
    }
    
    private void irHoy() {
        monthCalendar.setSelectedDate(new Date());
        cargarCitasDelMes();
        cargarCitasDelDia();
    }
    
    private void iniciarTimerRecordatorios() {
        timerRecordatorios = new Timer(30000, e -> verificarRecordatorios());
        timerRecordatorios.start();
    }
    
    private void verificarRecordatorios() {
        recordatorioService.generarRecordatorios(idUsuario);
        List<Recordatorio> pendientes = recordatorioService.obtenerPendientes(idUsuario);
        
        for (Recordatorio r : pendientes) {
            mostrarNotificacion(r);
            recordatorioService.marcarComoLeido(r.getIdRecordatorio());
        }
    }
    
    private void mostrarNotificacion(Recordatorio r) {
        frmNotificacion notificacion = new frmNotificacion(r);
        notificacion.setVisible(true);
    }
    
    // Clase interna para el calendario
    private class JMonthCalendar extends JPanel {
        private java.util.Calendar calendar;
        private Date selectedDate;
        private java.util.List<Date> boldedDates;
        
        public JMonthCalendar() {
            calendar = java.util.Calendar.getInstance();
            selectedDate = new Date();
            boldedDates = new java.util.ArrayList<>();
            setPreferredSize(new Dimension(400, 300));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int cellWidth = getWidth() / 7;
                    int cellHeight = getHeight() / 7;
                    int col = e.getX() / cellWidth;
                    int row = e.getY() / cellHeight;
                    if (row >= 1 && row <= 6) {
                        int day = (row - 1) * 7 + col + 1;
                        if (day <= getDaysInMonth()) {
                            calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
                            selectedDate = calendar.getTime();
                            repaint();
                            firePropertyChange("selectedDate", null, selectedDate);
                        }
                    }
                }
            });
        }
        
        private int getDaysInMonth() {
            return calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        }
        
        public Date getSelectedDate() { return selectedDate; }
        public void setSelectedDate(Date date) {
            this.selectedDate = date;
            calendar.setTime(date);
            repaint();
        }
        
        public void addBoldedDate(Date date) { boldedDates.add(date); }
        public void clearBoldedDates() { boldedDates.clear(); }
        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();
            int cellW = w / 7;
            int cellH = h / 7;
            
            // Dibujar días de la semana
            String[] dias = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
            for (int i = 0; i < 7; i++) {
                g.drawString(dias[i], i * cellW + 10, 20);
            }
            
            // Dibujar días del mes
            java.util.Calendar tempCal = (java.util.Calendar) calendar.clone();
            tempCal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            int primerDiaSemana = tempCal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
            int diasEnMes = getDaysInMonth();
            java.util.Calendar hoy = java.util.Calendar.getInstance();
            
            for (int day = 1; day <= diasEnMes; day++) {
                int col = (primerDiaSemana + day - 1) % 7;
                int row = (primerDiaSemana + day - 1) / 7 + 1;
                int x = col * cellW;
                int y = row * cellH;
                
                tempCal.set(java.util.Calendar.DAY_OF_MONTH, day);
                Date fechaDia = tempCal.getTime();
                
                if (boldedDates.stream().anyMatch(d -> {
                    java.util.Calendar cal1 = java.util.Calendar.getInstance();
                    java.util.Calendar cal2 = java.util.Calendar.getInstance();
                    cal1.setTime(d);
                    cal2.setTime(fechaDia);
                    return cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR);
                })) {
                    g.setColor(Color.RED);
                    g.fillOval(x + cellW - 15, y + 5, 10, 10);
                    g.setColor(Color.BLACK);
                }
                
                g.drawString(String.valueOf(day), x + 10, y + 20);
            }
        }
    }
}