package interfaz;

import clases.conexion;

import clases.conexion;
import estilos.TemaFinca;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelResumenGeneral extends javax.swing.JPanel {

    private JLabel lblIngresoMes, lblGastoMes, lblUtilidadMes;
    private JLabel lblTotalAnimales, lblHembras, lblMachos, lblGestacionesActivas;
    private JTable tblProximosPartos;
    private JTable tblCensoEstado;

    public PanelResumenGeneral() {
        setLayout(new BorderLayout());
        setBackground(TemaFinca.VERDE_CLARO);
        construirUI();
        refrescar();
    }
    
  private void construirUI() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(TemaFinca.VERDE_CLARO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        // ── BLOQUE 1: Utilidad del mes ───────────────────────────────────────
        JLabel tituloFinanciero = new JLabel("📈 BALANCE FINANCIERO DEL MES");
        tituloFinanciero.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloFinanciero.setForeground(TemaFinca.VERDE_OSCURO);
        tituloFinanciero.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(tituloFinanciero);
        contenedor.add(Box.createVerticalStrut(14));

        JPanel panelBalance = new JPanel(new GridLayout(1, 3, 20, 0));
        panelBalance.setOpaque(false);
        panelBalance.setMaximumSize(new Dimension(1200, 110));
        panelBalance.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardIngreso = crearCard("INGRESOS DEL MES", TemaFinca.BTN_GUARDAR);
        lblIngresoMes = (JLabel) cardIngreso.getComponent(1);
        JPanel cardGasto = crearCard("GASTOS DEL MES", new Color(180, 50, 50));
        lblGastoMes = (JLabel) cardGasto.getComponent(1);
        JPanel cardUtilidad = crearCard("UTILIDAD NETA", TemaFinca.VERDE_OSCURO);
        lblUtilidadMes = (JLabel) cardUtilidad.getComponent(1);

        panelBalance.add(cardIngreso);
        panelBalance.add(cardGasto);
        panelBalance.add(cardUtilidad);
        contenedor.add(panelBalance);
        contenedor.add(Box.createVerticalStrut(28));

        // ── BLOQUE 2: Censo del Hato ──────────────────────────────────────────
        JLabel tituloCenso = new JLabel("🐄 CENSO DEL HATO");
        tituloCenso.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloCenso.setForeground(TemaFinca.VERDE_OSCURO);
        tituloCenso.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(tituloCenso);
        contenedor.add(Box.createVerticalStrut(14));

        JPanel panelCenso = new JPanel(new GridLayout(1, 4, 20, 0));
        panelCenso.setOpaque(false);
        panelCenso.setMaximumSize(new Dimension(1200, 110));
        panelCenso.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardTotal = crearCard("TOTAL ANIMALES ACTIVOS", TemaFinca.VERDE_OSCURO);
        lblTotalAnimales = (JLabel) cardTotal.getComponent(1);
        JPanel cardHembras = crearCard("HEMBRAS", new Color(173, 58, 119));
        lblHembras = (JLabel) cardHembras.getComponent(1);
        JPanel cardMachos = crearCard("MACHOS", new Color(50, 100, 160));
        lblMachos = (JLabel) cardMachos.getComponent(1);
        JPanel cardGestacion = crearCard("GESTACIONES ACTIVAS", TemaFinca.DORADO);
        lblGestacionesActivas = (JLabel) cardGestacion.getComponent(1);

        panelCenso.add(cardTotal);
        panelCenso.add(cardHembras);
        panelCenso.add(cardMachos);
        panelCenso.add(cardGestacion);
        contenedor.add(panelCenso);
        contenedor.add(Box.createVerticalStrut(28));

        // ── BLOQUE 3: Próximos partos + Estado del hato ───────────────────────
        JLabel tituloReprod = new JLabel("🍼 SALUD REPRODUCTIVA Y ESTADO DEL HATO");
        tituloReprod.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloReprod.setForeground(TemaFinca.VERDE_OSCURO);
        tituloReprod.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(tituloReprod);
        contenedor.add(Box.createVerticalStrut(14));

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 20, 0));
        panelTablas.setOpaque(false);
        panelTablas.setMaximumSize(new Dimension(1200, 260));
        panelTablas.setPreferredSize(new Dimension(1200, 260));
        panelTablas.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sub-panel: próximos partos
        JPanel subPanelPartos = new JPanel(new BorderLayout(0, 6));
        subPanelPartos.setOpaque(false);
        JLabel lblPartos = new JLabel("Próximos partos (siguientes 30 días)");
        lblPartos.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPartos.setForeground(TemaFinca.GRIS_TEXTO);
        subPanelPartos.add(lblPartos, BorderLayout.NORTH);

        tblProximosPartos = new JTable(new DefaultTableModel(new Object[][]{},
                new String[]{"ID Vaca", "Fecha Estimada", "Días Restantes"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
        tblProximosPartos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblProximosPartos.setRowHeight(24);
        tblProximosPartos.getTableHeader().setBackground(TemaFinca.DORADO);
        tblProximosPartos.getTableHeader().setForeground(Color.BLACK);
        subPanelPartos.add(new JScrollPane(tblProximosPartos), BorderLayout.CENTER);
        panelTablas.add(subPanelPartos);

        // Sub-panel: estado del hato
        JPanel subPanelEstado = new JPanel(new BorderLayout(0, 6));
        subPanelEstado.setOpaque(false);
        JLabel lblEstado = new JLabel("Distribución por estado");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstado.setForeground(TemaFinca.GRIS_TEXTO);
        subPanelEstado.add(lblEstado, BorderLayout.NORTH);

        tblCensoEstado = new JTable(new DefaultTableModel(new Object[][]{},
                new String[]{"Estado", "Cantidad"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        });
        tblCensoEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblCensoEstado.setRowHeight(24);
        tblCensoEstado.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblCensoEstado.getTableHeader().setForeground(Color.WHITE);
        subPanelEstado.add(new JScrollPane(tblCensoEstado), BorderLayout.CENTER);
        panelTablas.add(subPanelEstado);

        contenedor.add(panelTablas);

        JScrollPane scrollGeneral = new JScrollPane(contenedor);
        scrollGeneral.setBorder(BorderFactory.createEmptyBorder());
        scrollGeneral.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollGeneral, BorderLayout.CENTER);
    }

    private JPanel crearCard(String titulo, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTitulo.setForeground(new Color(255, 255, 255, 230));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 4, 0, 4));
        JLabel lblValor = new JLabel("0", SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValor.setForeground(Color.WHITE);
        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    public void refrescar() {
        try (Connection cn = conexion.conectar()) {
            if (cn == null) return;

            double ingresoMes = sumarIngresosMes(cn);
            double gastoMes = sumarGastosMes(cn);
            double utilidad = ingresoMes - gastoMes;

            lblIngresoMes.setText(formatoMoneda(ingresoMes));
            lblGastoMes.setText(formatoMoneda(gastoMes));
            lblUtilidadMes.setText(formatoMoneda(utilidad));
            lblUtilidadMes.setForeground(utilidad < 0 ? new Color(255, 210, 210) : Color.WHITE);

            cargarCensoHato(cn);
            cargarProximosPartos(cn);
            cargarDistribucionEstado(cn);

        } catch (SQLException e) {
            System.err.println("Error general al refrescar el panel de resumen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double sumarIngresosMes(Connection cn) throws SQLException {
        double total = 0;
        String queryLeche = "SELECT COALESCE(SUM(valor_total),0) AS total FROM venta_leche WHERE MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())";
        String queryAnimal = "SELECT COALESCE(SUM(precio_total),0) AS total FROM venta_animal WHERE MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())";

        try (PreparedStatement ps1 = cn.prepareStatement(queryLeche);
             ResultSet rs1 = ps1.executeQuery()) {
            if (rs1.next()) total += rs1.getDouble("total");
        }

        try (PreparedStatement ps2 = cn.prepareStatement(queryAnimal);
             ResultSet rs2 = ps2.executeQuery()) {
            if (rs2.next()) total += rs2.getDouble("total");
        }

        return total;
    }

    private double sumarGastosMes(Connection cn) throws SQLException {
        String queryGasto = "SELECT COALESCE(SUM(valor_total),0) AS total FROM compra_insumo WHERE MONTH(fecha_compra)=MONTH(CURDATE()) AND YEAR(fecha_compra)=YEAR(CURDATE())";
        try (PreparedStatement ps = cn.prepareStatement(queryGasto);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble("total") : 0;
        }
    }

    private void cargarCensoHato(Connection cn) throws SQLException {
        String queryCenso = "SELECT COUNT(*) AS total, SUM(CASE WHEN sexo='HEMBRA' THEN 1 ELSE 0 END) AS hembras, SUM(CASE WHEN sexo='MACHO' THEN 1 ELSE 0 END) AS machos FROM registro WHERE estado_animal = 'Activo'";
        try (PreparedStatement ps = cn.prepareStatement(queryCenso);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                lblTotalAnimales.setText(String.valueOf(rs.getInt("total")));
                lblHembras.setText(String.valueOf(rs.getInt("hembras")));
                lblMachos.setText(String.valueOf(rs.getInt("machos")));
            }
        }

        String queryGest = "SELECT COUNT(*) AS total FROM gestacion WHERE estado = 'pendiente_confirmacion'";
        try (PreparedStatement psGest = cn.prepareStatement(queryGest);
             ResultSet rsGest = psGest.executeQuery()) {
            if (rsGest.next()) {
                lblGestacionesActivas.setText(String.valueOf(rsGest.getInt("total")));
            }
        }
    }

    private void cargarProximosPartos(Connection cn) throws SQLException {
        DefaultTableModel m = (DefaultTableModel) tblProximosPartos.getModel();
        m.setRowCount(0);

        String sql = "SELECT id_vaca, fecha_parto_estimada, DATEDIFF(fecha_parto_estimada, CURDATE()) AS dias_restantes FROM gestacion WHERE estado = 'pendiente_confirmacion' AND fecha_parto_estimada BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) ORDER BY fecha_parto_estimada ASC";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int dias = rs.getInt("dias_restantes");
                Date fecha = rs.getDate("fecha_parto_estimada");
                m.addRow(new Object[]{
                    "Vaca ID: " + rs.getInt("id_vaca"),
                    fecha != null ? fecha.toString() : "Sin fecha",
                    dias == 0 ? "¡Hoy!" : dias + " días"
                });
            }
        }
    }

    private void cargarDistribucionEstado(Connection cn) throws SQLException {
        DefaultTableModel m = (DefaultTableModel) tblCensoEstado.getModel();
        m.setRowCount(0);

        String sql = "SELECT estado_animal, COUNT(*) AS total FROM registro GROUP BY estado_animal ORDER BY total DESC";
        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String estado = rs.getString("estado_animal");
                m.addRow(new Object[]{
                    estado != null ? estado : "Desconocido", 
                    rs.getInt("total")
                });
            }
        }
    }

    private String formatoMoneda(double valor) {
        return "$ " + String.format("%,.0f", valor).replace(",", ".");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
