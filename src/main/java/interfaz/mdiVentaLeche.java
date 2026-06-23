package interfaz;

import clases.VentaLeche;
import clases.VentaLecheDAO;
import estilos.EstiloFormularioBase;
import estilos.TemaFinca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class mdiVentaLeche extends javax.swing.JInternalFrame {

    // ── DAO ───────────────────────────────────────────────────────────────────
    private final VentaLecheDAO dao = new VentaLecheDAO();

    // ── Formato moneda colombiana ─────────────────────────────────────────────
    private final NumberFormat fmt
            = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    // ── Componentes ───────────────────────────────────────────────────────────
    private JPanel panel;
    private JScrollPane scrollPrincipal;

    private JTextField txtDesde, txtHasta, txtMes, txtAnio;
    private JTextField txtEntidad, txtPrecio, txtObservaciones;
    private JRadioButton rbRango, rbMes;
    private ButtonGroup bgFiltro;

    private JLabel lblTotalLitros, lblTotalValor;

    private JTable tblVacas, tblHistorial;
    private JButton btnCalcular, btnGuardar, btnEliminar, btnNuevo;

    // ── Estado ────────────────────────────────────────────────────────────────
    private double litrosTotalesActual = 0;
    private double valorTotalActual = 0;

    public mdiVentaLeche() {
        setTitle("Venta de Leche");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        construirUI();
        cargarHistorial();
    }

    // =========================================================================
    // CALCULAR — llama al DAO y llena la tabla + resumen
    // =========================================================================
    private void calcular() {
        try {
            Date desde, hasta;

            if (rbRango.isSelected()) {
                desde = Date.valueOf(LocalDate.parse(txtDesde.getText().trim()));
                hasta = Date.valueOf(LocalDate.parse(txtHasta.getText().trim()));
            } else {
                int mes = Integer.parseInt(txtMes.getText().trim());
                int anio = Integer.parseInt(txtAnio.getText().trim());
                desde = Date.valueOf(LocalDate.of(anio, mes, 1));
                hasta = Date.valueOf(LocalDate.of(anio, mes, 1)
                        .withDayOfMonth(
                                LocalDate.of(anio, mes, 1)
                                        .lengthOfMonth()));
            }

            if (txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese el precio por litro.");
                txtPrecio.requestFocus();
                return;
            }
            double precio = Double.parseDouble(
                    txtPrecio.getText().trim().replace(",", "."));

            List<Object[]> filas = dao.getProduccionPorVaca(desde, hasta, precio);
            double[] totales = dao.getTotales(desde, hasta, precio);

            litrosTotalesActual = totales[0];
            valorTotalActual = totales[1];

            // Llenar tabla vacas
            DefaultTableModel m = (DefaultTableModel) tblVacas.getModel();
            m.setRowCount(0);
            for (Object[] f : filas) {
                m.addRow(new Object[]{
                    f[1], // Nombre
                    String.format("%.2f", f[2]), // Litros
                    fmt.format(f[3]), // Valor
                    String.format("%.1f%%", f[4]) // %
                });
            }

            // Totales
            lblTotalLitros.setText(String.format("%.2f L", litrosTotalesActual));
            lblTotalValor.setText(fmt.format(valorTotalActual));

            btnGuardar.setEnabled(litrosTotalesActual > 0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // GUARDAR VENTA
    // =========================================================================
    private void guardarVenta() {
        if (txtEntidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese el nombre de la entidad compradora.");
            txtEntidad.requestFocus();
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Registrar esta venta?\n"
                + "  Entidad : " + txtEntidad.getText().trim() + "\n"
                + "  Litros  : " + String.format("%.2f", litrosTotalesActual) + "\n"
                + "  Total   : " + fmt.format(valorTotalActual),
                "Confirmar venta", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Date desde, hasta;
            if (rbRango.isSelected()) {
                desde = Date.valueOf(LocalDate.parse(txtDesde.getText().trim()));
                hasta = Date.valueOf(LocalDate.parse(txtHasta.getText().trim()));
            } else {
                int mes = Integer.parseInt(txtMes.getText().trim());
                int anio = Integer.parseInt(txtAnio.getText().trim());
                desde = Date.valueOf(LocalDate.of(anio, mes, 1));
                hasta = Date.valueOf(LocalDate.of(anio, mes, 1)
                        .withDayOfMonth(
                                LocalDate.of(anio, mes, 1)
                                        .lengthOfMonth()));
            }

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim().replace(",", "."));

            VentaLeche vl = new VentaLeche(
                    Date.valueOf(LocalDate.now()),
                    desde, hasta,
                    txtEntidad.getText().trim(),
                    precio,
                    litrosTotalesActual,
                    valorTotalActual,
                    txtObservaciones.getText().trim()
            );

            if (dao.guardarVenta(vl)) {
                JOptionPane.showMessageDialog(this,
                        "✔ Venta registrada correctamente.");
                cargarHistorial();
                limpiar();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // HISTORIAL
    // =========================================================================
    private void cargarHistorial() {
        try {
            DefaultTableModel m = (DefaultTableModel) tblHistorial.getModel();
            m.setRowCount(0);
            for (VentaLeche vl : dao.getHistorial()) {
                m.addRow(new Object[]{
                    vl.getIdVenta(),
                    vl.getFechaVenta(),
                    vl.getEntidad(),
                    vl.getFechaDesde() + " → " + vl.getFechaHasta(),
                    String.format("%.2f", vl.getLitrosTotales()),
                    fmt.format(vl.getPrecioLitro()),
                    fmt.format(vl.getValorTotal())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar historial: " + e.getMessage());
        }
    }

    // =========================================================================
    // ELIMINAR del historial
    // =========================================================================
    private void eliminarVenta() {
        int fila = tblHistorial.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una venta del historial para eliminar.");
            return;
        }
        int id = (int) tblHistorial.getValueAt(fila, 0);
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la venta ID " + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (dao.eliminarVenta(id)) {
                JOptionPane.showMessageDialog(this, "Venta eliminada.");
                cargarHistorial();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // LIMPIAR
    // =========================================================================
    private void limpiar() {
        txtEntidad.setText("");
        txtPrecio.setText("");
        txtObservaciones.setText("");
        txtDesde.setText(LocalDate.now().withDayOfMonth(1).toString());
        txtHasta.setText(LocalDate.now().toString());
        txtMes.setText(String.valueOf(LocalDate.now().getMonthValue()));
        txtAnio.setText(String.valueOf(LocalDate.now().getYear()));
        lblTotalLitros.setText("—");
        lblTotalValor.setText("—");
        ((DefaultTableModel) tblVacas.getModel()).setRowCount(0);
        litrosTotalesActual = 0;
        valorTotalActual = 0;
        btnGuardar.setEnabled(false);
    }

    // =========================================================================
    // UI
    // =========================================================================
    private void construirUI() {
        getContentPane().removeAll();
        getContentPane().setBackground(TemaFinca.VERDE_CLARO);
        getContentPane().setLayout(null);
        setSize(1000, 860);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(null);
        header.setBackground(TemaFinca.VERDE_OSCURO);
        header.setBounds(0, 0, 1000, 80);
        JLabel titulo = new JLabel("VENTA DE LECHE", SwingConstants.CENTER);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBounds(0, 18, 1000, 44);
        header.add(titulo);
        add(header);

        // ── Panel interior ────────────────────────────────────────────────────
        panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        int x = 40, y = 18;

        // ── BLOQUE 1: Filtro de período ───────────────────────────────────────
        lbl(panel, "PERÍODO DE PRODUCCIÓN", x, y);
        y += 22;

        bgFiltro = new ButtonGroup();
        rbRango = new JRadioButton("Rango de fechas", true);
        rbMes = new JRadioButton("Por mes/año");
        rbRango.setFont(TemaFinca.FUENTE_LABEL);
        rbMes.setFont(TemaFinca.FUENTE_LABEL);
        rbRango.setBackground(Color.WHITE);
        rbMes.setBackground(Color.WHITE);
        rbRango.setBounds(x, y, 160, 24);
        rbMes.setBounds(x + 170, y, 140, 24);
        bgFiltro.add(rbRango);
        bgFiltro.add(rbMes);
        panel.add(rbRango);
        panel.add(rbMes);
        y += 28;

        // Fila rango
        lbl(panel, "DESDE", x, y);
        lbl(panel, "HASTA", x + 220, y);
        txtDesde = campo(x, y + 20, 200);
        txtHasta = campo(x + 220, y + 20, 200);
        txtDesde.setText(LocalDate.now().withDayOfMonth(1).toString());
        txtHasta.setText(LocalDate.now().toString());
        panel.add(txtDesde);
        panel.add(txtHasta);

        // Fila mes/año (inicialmente oculta)
        lbl(panel, "MES (1-12)", x + 460, y);
        lbl(panel, "AÑO", x + 590, y);
        txtMes = campo(x + 460, y + 20, 110);
        txtAnio = campo(x + 590, y + 20, 110);
        txtMes.setText(String.valueOf(LocalDate.now().getMonthValue()));
        txtAnio.setText(String.valueOf(LocalDate.now().getYear()));
        txtMes.setEnabled(false);
        txtAnio.setEnabled(false);
        panel.add(txtMes);
        panel.add(txtAnio);

        // Listener radios
        rbRango.addActionListener(e -> {
            txtDesde.setEnabled(true);
            txtHasta.setEnabled(true);
            txtMes.setEnabled(false);
            txtAnio.setEnabled(false);
        });
        rbMes.addActionListener(e -> {
            txtDesde.setEnabled(false);
            txtHasta.setEnabled(false);
            txtMes.setEnabled(true);
            txtAnio.setEnabled(true);
        });
        y += 56;

        sep(panel, x, y);
        y += 14;

        // ── BLOQUE 2: Datos de la venta ───────────────────────────────────────
        lbl(panel, "DATOS DE VENTA", x, y);
        y += 22;

        lbl(panel, "ENTIDAD / COMPRADOR", x, y);
        lbl(panel, "PRECIO POR LITRO ($)", x + 340, y);
        txtEntidad = campo(x, y + 20, 300);
        txtPrecio = campo(x + 340, y + 20, 180);
        panel.add(txtEntidad);
        panel.add(txtPrecio);
        y += 62;

        lbl(panel, "OBSERVACIONES", x, y);
        txtObservaciones = campo(x, y + 20, 540);
        panel.add(txtObservaciones);
        y += 56;

        sep(panel, x, y);
        y += 14;

        // ── BLOQUE 3: Resumen totales ─────────────────────────────────────────
        JPanel panelTotales = new JPanel(null);
        panelTotales.setBackground(new Color(245, 250, 245));
        panelTotales.setBorder(BorderFactory.createLineBorder(
                new Color(180, 210, 180), 1));
        panelTotales.setBounds(x, y, 830, 60);
        panel.add(panelTotales);

        JLabel lTL = new JLabel("TOTAL LITROS:");
        lTL.setFont(TemaFinca.FUENTE_LABEL);
        lTL.setForeground(TemaFinca.GRIS_TEXTO);
        lTL.setBounds(20, 10, 120, 20);

        lblTotalLitros = new JLabel("—");
        lblTotalLitros.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalLitros.setForeground(TemaFinca.VERDE_OSCURO);
        lblTotalLitros.setBounds(20, 30, 200, 24);

        JLabel lTV = new JLabel("VALOR TOTAL:");
        lTV.setFont(TemaFinca.FUENTE_LABEL);
        lTV.setForeground(TemaFinca.GRIS_TEXTO);
        lTV.setBounds(300, 10, 120, 20);

        lblTotalValor = new JLabel("—");
        lblTotalValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalValor.setForeground(new Color(30, 120, 60));
        lblTotalValor.setBounds(300, 30, 280, 24);

        panelTotales.add(lTL);
        panelTotales.add(lblTotalLitros);
        panelTotales.add(lTV);
        panelTotales.add(lblTotalValor);
        y += 76;

        // ── BLOQUE 4: Tabla por vaca ──────────────────────────────────────────
        lbl(panel, "PRODUCCIÓN POR ANIMAL", x, y);
        y += 20;

        tblVacas = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Animal", "Litros", "Valor ($)", "% del total"}));
        estilizarTabla(tblVacas);
        tblVacas.getColumnModel().getColumn(0).setPreferredWidth(220);
        tblVacas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblVacas.getColumnModel().getColumn(2).setPreferredWidth(160);
        tblVacas.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane spVacas = new JScrollPane(tblVacas);
        spVacas.setBounds(x, y, 830, 160);
        spVacas.setBorder(BorderFactory.createLineBorder(
                new Color(220, 220, 220)));
        panel.add(spVacas);
        y += 176;

        sep(panel, x, y);
        y += 14;

        // ── BLOQUE 5: Historial de ventas ─────────────────────────────────────
        lbl(panel, "HISTORIAL DE VENTAS REGISTRADAS", x, y);
        y += 20;

        tblHistorial = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Fecha venta", "Entidad",
                    "Período", "Litros", "Precio/L", "Total"}));
        estilizarTabla(tblHistorial);
        tblHistorial.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblHistorial.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblHistorial.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblHistorial.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblHistorial.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblHistorial.getColumnModel().getColumn(5).setPreferredWidth(90);
        tblHistorial.getColumnModel().getColumn(6).setPreferredWidth(110);

        JScrollPane spHist = new JScrollPane(tblHistorial);
        spHist.setBounds(x, y, 830, 140);
        spHist.setBorder(BorderFactory.createLineBorder(
                new Color(220, 220, 220)));
        panel.add(spHist);
        y += 156;

        // ── Botones ───────────────────────────────────────────────────────────
        btnCalcular = new JButton("CALCULAR");
        btnGuardar = new JButton("REGISTRAR VENTA");
        btnEliminar = new JButton("ELIMINAR");
        btnNuevo = new JButton("NUEVO");

        btnGuardar.setEnabled(false);

        int bW = 185, bH = 42, bGap = 16;
        int totalW = 4 * bW + 3 * bGap;
        int sx = (910 - totalW) / 2;

        btnCalcular.setBounds(sx, y, bW, bH);
        btnGuardar.setBounds(sx + bW + bGap, y, bW, bH);
        btnEliminar.setBounds(sx + 2 * (bW + bGap), y, bW, bH);
        btnNuevo.setBounds(sx + 3 * (bW + bGap), y, bW, bH);

        EstiloFormularioBase.estilizarBoton(btnCalcular, TemaFinca.VERDE_OSCURO, null);
        EstiloFormularioBase.estilizarBoton(btnGuardar, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminar, new Color(180, 50, 50), null);
        EstiloFormularioBase.estilizarBoton(btnNuevo, new Color(80, 80, 80), null);

        btnCalcular.addActionListener(e -> calcular());
        btnGuardar.addActionListener(e -> guardarVenta());
        btnEliminar.addActionListener(e -> eliminarVenta());
        btnNuevo.addActionListener(e -> limpiar());

        panel.add(btnCalcular);
        panel.add(btnGuardar);
        panel.add(btnEliminar);
        panel.add(btnNuevo);

        y += bH + 20;

        // ── Scroll ────────────────────────────────────────────────────────────
        panel.setPreferredSize(new Dimension(910, y));
        scrollPrincipal = new JScrollPane(panel);
        scrollPrincipal.setBounds(35, 90, 920, 680);
        scrollPrincipal.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPrincipal);

        revalidate();
        repaint();
    }

    // =========================================================================
    // HELPERS UI
    // =========================================================================
    private void lbl(JPanel p, String texto, int x, int y) {
        JLabel l = new JLabel(texto);
        l.setFont(TemaFinca.FUENTE_LABEL);
        l.setForeground(TemaFinca.GRIS_TEXTO);
        l.setBounds(x, y, 500, 20);
        p.add(l);
    }

    private JTextField campo(int x, int y, int w) {
        JTextField t = new JTextField();
        t.setFont(TemaFinca.FUENTE_INPUT);
        t.setBorder(TemaFinca.bordeCampo());
        t.setBounds(x, y, w, 28);
        return t;
    }

    private void sep(JPanel p, int x, int y) {
        JSeparator s = new JSeparator();
        s.setBounds(x, y, 830, 2);
        s.setForeground(new Color(220, 220, 220));
        p.add(s);
    }

    private void estilizarTabla(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(26);
        t.setGridColor(new Color(240, 240, 240));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setReorderingAllowed(false);
        t.setSelectionBackground(new Color(210, 235, 210));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
