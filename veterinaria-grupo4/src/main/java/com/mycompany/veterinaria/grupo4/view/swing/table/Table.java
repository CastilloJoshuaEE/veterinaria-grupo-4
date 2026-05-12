package com.mycompany.veterinaria.grupo4.view.swing.table;


import com.mycompany.veterinaria.grupo4.model.entity.Estado;
import com.mycompany.veterinaria.grupo4.model.entity.EstadoCita;
import com.mycompany.veterinaria.grupo4.view.swing.scrollbar.ScrollBarCustom;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Raven
 */
public class Table extends JTable {
    public Table() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);

        // ── Header ──────────────────────────────────────────────
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o,
                    boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");
                // centra el header de la última columna (acciones)
                if (i1 == getColumnCount() - 1) {
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });

        // ── Renderer de celdas ──────────────────────────────────
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o,
                    boolean selected, boolean focus, int i, int i1) {

                Color bg = selected
                        ? UIManager.getColor("Table.selectionBackground")
                        : UIManager.getColor("Table.background");

                Color fg = selected
                        ? UIManager.getColor("Table.selectionForeground")
                        : UIManager.getColor("Table.foreground");
                
                // Celda de Avatar de mascotas
                if (o instanceof ModelProfile data) {
                    Profile cell = new Profile(data);
                    cell.setOpaque(true);
                    cell.setBackground(bg);
                    return cell;
                }
                
                // Celda de Estado de cita
                if (o instanceof EstadoCita type) {
                    CellCitaEstado cell = new CellCitaEstado(type);
                    cell.setOpaque(true);
                    cell.setBackground(bg);
                    return cell;
                }
                // Celda de Estado Activo Inactivo
                if (o instanceof Estado type) {
                    CellEstado cell = new CellEstado(type);
                    cell.setOpaque(true);
                    cell.setBackground(bg);
                    return cell;
                }

                // Celda de ACCIÓN (botones)
                if (o instanceof ModelAction) {
                    Action cell = new Action();
                    cell.setOpaque(true);
                    cell.setBackground(bg);
                    return cell;
                }

                // Celda de texto normal
                Component com = super.getTableCellRendererComponent(
                        jtable, o, selected, focus, i, i1);
                setBorder(noFocusBorder);
                com.setBackground(bg);
                com.setForeground(fg);  
                return com;
            }
        });
    }

    // ── Editor: activa el click en botones ──────────────────────
    @Override
    public TableCellEditor getCellEditor(int row, int col) {
        Object value = getValueAt(row, col);
        if (value instanceof ModelAction) {
            return new TableCellAction();
        }
        return super.getCellEditor(row, col);
    }

    // ── Helper para agregar filas ───────────────────────────────
    public void addRow(Object[] row) {
        ((DefaultTableModel) getModel()).addRow(row);
    }

    // ── Arregla el JScrollPane que contiene la tabla ────────────
    public void fixTable(JScrollPane scroll) {
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        JPanel corner = new JPanel();
        corner.setBackground(Color.WHITE);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, corner);
        scroll.setBorder(new EmptyBorder(5, 10, 5, 10));
    }
}

