package interfaz;

import estilos.TemaFinca;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.*;

public class PanelProduccion extends javax.swing.JPanel {

    private static final Color C_AQUA = new Color(0x1b, 0xaf, 0x7a);
    private static final Color C_AZUL = new Color(0x2a, 0x78, 0xd6);
    private static final Color C_AMARILLO = new Color(0xed, 0xa1, 0x00);
    private static final Color C_VERDE = new Color(0x00, 0x83, 0x00);
    private static final Color C_VIOLETA = new Color(0x4a, 0x3a, 0xa7);
    private static final Color C_GRIS_MUTED = new Color(137, 135, 129);
    private static final Color C_GRID = new Color(225, 224, 217);
    private static final Color C_AXIS_LINE = new Color(195, 194, 183);

    private JLabel lblKpiHoy, lblKpiSemana, lblKpiMes, lblKpiProm;
    private JPanel gridGraficos;
    private JTable tblDetalle;

    public PanelProduccion() {
        setLayout(new BorderLayout());
        setBackground(TemaFinca.VERDE_CLARO);
        construirUI();
        refrescar(); // Solución al bloqueo de UI
    }

    private void construirUI() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(TemaFinca.VERDE_CLARO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel titulo = new JLabel("PRODUCCIÓN DE LECHE");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(TemaFinca.VERDE_OSCURO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(titulo);
        contenedor.add(Box.createVerticalStrut(16));

        // ── KPIs ─────────────────────────────────────────────────────────────
        JPanel panelKpis = new JPanel(new GridLayout(1, 4, 16, 0));
        panelKpis.setOpaque(false);
        panelKpis.setMaximumSize(new Dimension(1500, 90));
        panelKpis.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardHoy = crearCardKpi("LITROS HOY");
        lblKpiHoy = (JLabel) cardHoy.getComponent(1);
        JPanel cardSemana = crearCardKpi("ESTA SEMANA");
        lblKpiSemana = (JLabel) cardSemana.getComponent(1);
        JPanel cardMes = crearCardKpi("ESTE MES");
        lblKpiMes = (JLabel) cardMes.getComponent(1);
        JPanel cardProm = crearCardKpi("PROMEDIO DIARIO");
        lblKpiProm = (JLabel) cardProm.getComponent(1);

        panelKpis.add(cardHoy);
        panelKpis.add(cardSemana);
        panelKpis.add(cardMes);
        panelKpis.add(cardProm);
        contenedor.add(panelKpis);
        contenedor.add(Box.createVerticalStrut(24));

        // ── Grid 2x2 de gráficos ─────────────────────────────────────────────
        gridGraficos = new JPanel(new GridLayout(2, 2, 18, 18));
        gridGraficos.setOpaque(false);
        gridGraficos.setMaximumSize(new Dimension(1500, 760));
        gridGraficos.setPreferredSize(new Dimension(1500, 760));
        gridGraficos.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(gridGraficos);
        contenedor.add(Box.createVerticalStrut(24));

        // ── Tabla detalle ─────────────────────────────────────────────────────
        JLabel lblDetalleTitulo = new JLabel("Producción por animal — mes actual");
        lblDetalleTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDetalleTitulo.setForeground(TemaFinca.GRIS_TEXTO);
        lblDetalleTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(lblDetalleTitulo);
        contenedor.add(Box.createVerticalStrut(8));

        tblDetalle = new JTable(new DefaultTableModel(new Object[][]{},
                new String[]{"Animal", "Total Litros", "Promedio Diario", "Registros"}));
        tblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblDetalle.setRowHeight(26);
        tblDetalle.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblDetalle.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollTabla = new JScrollPane(tblDetalle);
        scrollTabla.setPreferredSize(new Dimension(1500, 240));
        scrollTabla.setMaximumSize(new Dimension(1500, 240));
        scrollTabla.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(scrollTabla);

        JScrollPane scrollGeneral = new JScrollPane(contenedor);
        scrollGeneral.setBorder(BorderFactory.createEmptyBorder());
        scrollGeneral.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollGeneral, BorderLayout.CENTER);
    }

    private JPanel crearCardKpi(String titulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 224, 217)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblT.setForeground(C_GRIS_MUTED);

        JLabel lblV = new JLabel("Cargando...");
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Un poco más pequeño temporalmente
        lblV.setForeground(TemaFinca.VERDE_OSCURO);

        card.add(lblT, BorderLayout.NORTH);
        card.add(lblV, BorderLayout.CENTER);
        return card;
    }

    private JPanel verificarYEnvolver(String titulo, ChartPanel panelChart, JPanel leyenda) {
        if (panelChart == null) {
            JPanel errorPanel = new JPanel(new GridBagLayout());
            errorPanel.setBackground(Color.WHITE);
            errorPanel.add(new JLabel("Sin datos disponibles"));
            return envolverGrafico(titulo, errorPanel, null);
        }
        return envolverGrafico(titulo, panelChart, leyenda);
    }

    private JPanel envolverGrafico(String titulo, JComponent grafico, JPanel leyenda) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 224, 217)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TemaFinca.GRIS_TEXTO);

        p.add(lbl, BorderLayout.NORTH);
        p.add(grafico, BorderLayout.CENTER);
        if (leyenda != null) {
            p.add(leyenda, BorderLayout.SOUTH);
        }
        return p;
    }

    private JPanel crearLeyenda(String[] etiquetas, Color[] colores) {
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 4));
        leyenda.setOpaque(false);
        for (int i = 0; i < etiquetas.length; i++) {
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            item.setOpaque(false);
            JPanel cuadro = new JPanel();
            cuadro.setPreferredSize(new Dimension(10, 10));
            cuadro.setBackground(colores[i]);
            JLabel lbl = new JLabel(etiquetas[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(C_GRIS_MUTED);
            item.add(cuadro);
            item.add(lbl);
            leyenda.add(item);
        }
        return leyenda;
    }

    // =========================================================================
    // CARGA DE DATOS ASÍNCRONA
    // =========================================================================
    public void refrescar() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection cn = clases.conexion.conectar()) {
                    cargarKpis(cn);

                    // Generamos los paneles de gráficos en el hilo secundario
                    ChartPanel cpLinea = crearLineaLitrosDiarios(cn);
                    ChartPanel cpTurnos = crearBarrasTurnos(cn);
                    ChartPanel cpRanking = crearBarrasRanking(cn);
                    ChartPanel cpHistorico = crearBarrasHistorico(cn);

                    // Modificaciones de interfaz se envían al hilo de UI de Swing
                    SwingUtilities.invokeLater(() -> {
                        gridGraficos.removeAll();
                        gridGraficos.add(verificarYEnvolver("Litros producidos por día — últimos 30 días", cpLinea, null));
                        gridGraficos.add(verificarYEnvolver("Producción por turno — máx / prom / mín", cpTurnos,
                                crearLeyenda(new String[]{"Máximo", "Promedio", "Mínimo"}, new Color[]{C_AZUL, C_AQUA, C_AMARILLO})));
                        gridGraficos.add(verificarYEnvolver("Ranking top 10 animales — litros este mes", cpRanking, null));
                        gridGraficos.add(verificarYEnvolver("Histórico mensual — últimos 6 meses", cpHistorico, null));
                        gridGraficos.revalidate();
                        gridGraficos.repaint();
                    });

                    cargarTablaDetalle(cn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void cargarKpis(Connection cn) throws SQLException {
        ResultSet rsHoy = cn.prepareStatement(
                "SELECT COALESCE(SUM(litros),0) t FROM produccion_leche WHERE fecha=CURDATE()"
        ).executeQuery();
        if (rsHoy.next()) {
            double v = rsHoy.getDouble("t");
            SwingUtilities.invokeLater(() -> {
                lblKpiHoy.setFont(new Font("Segoe UI", Font.BOLD, 24));
                lblKpiHoy.setText(String.format("%.1f L", v));
            });
        }

        ResultSet rsSem = cn.prepareStatement(
                "SELECT COALESCE(SUM(litros),0) t FROM produccion_leche WHERE YEARWEEK(fecha,1)=YEARWEEK(CURDATE(),1)"
        ).executeQuery();
        if (rsSem.next()) {
            double v = rsSem.getDouble("t");
            SwingUtilities.invokeLater(() -> lblKpiSemana.setText(String.format("%.1f L", v)));
        }

        ResultSet rsMes = cn.prepareStatement(
                "SELECT COALESCE(SUM(litros),0) t FROM produccion_leche WHERE MONTH(fecha)=MONTH(CURDATE()) AND YEAR(fecha)=YEAR(CURDATE())"
        ).executeQuery();
        if (rsMes.next()) {
            double v = rsMes.getDouble("t");
            SwingUtilities.invokeLater(() -> lblKpiMes.setText(String.format("%.1f L", v)));
        }

        ResultSet rsProm = cn.prepareStatement(
                "SELECT COALESCE(AVG(total_dia),0) t FROM (SELECT fecha, SUM(litros) total_dia FROM produccion_leche WHERE MONTH(fecha)=MONTH(CURDATE()) AND YEAR(fecha)=YEAR(CURDATE()) GROUP BY fecha) d"
        ).executeQuery();
        if (rsProm.next()) {
            double v = rsProm.getDouble("t");
            SwingUtilities.invokeLater(() -> lblKpiProm.setText(String.format("%.1f L", v)));
        }
    }

    /**
     * 1. Línea: corregido error de casteo del constructor Day
     */
    private ChartPanel crearLineaLitrosDiarios(Connection cn) throws SQLException {
        Map<java.time.LocalDate, Double> porDia = new LinkedHashMap<>();
        java.time.LocalDate hoy = java.time.LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            porDia.put(hoy.minusDays(i), 0.0);
        }

        ResultSet rs = cn.prepareStatement(
                "SELECT fecha, SUM(litros) total FROM produccion_leche WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 29 DAY) GROUP BY fecha"
        ).executeQuery();
        while (rs.next()) {
            java.time.LocalDate f = rs.getDate("fecha").toLocalDate();
            porDia.merge(f, rs.getDouble("total"), Double::sum);
        }

        TimeSeries serie = new TimeSeries("Litros");
        for (Map.Entry<java.time.LocalDate, Double> e : porDia.entrySet()) {
            java.time.LocalDate f = e.getKey();
            // Corrección: Usar java.sql.Date previene desajustes de mes en JFreeChart
            serie.add(new Day(java.sql.Date.valueOf(f)), e.getValue());
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, null, new TimeSeriesCollection(serie), false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(C_GRID);
        plot.setOutlineVisible(false);

        XYLineAndShapeRenderer r = new XYLineAndShapeRenderer(true, false);
        r.setSeriesPaint(0, C_AQUA);
        r.setSeriesStroke(0, new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        plot.setRenderer(r);

        DateAxis ejeX = (DateAxis) plot.getDomainAxis();
        ejeX.setDateFormatOverride(new SimpleDateFormat("dd/MM"));
        ejeX.setTickLabelPaint(C_GRIS_MUTED);
        ejeX.setAxisLinePaint(C_AXIS_LINE); // <-- CORREGIDO: Se agregó el 'set'
        ejeX.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));

        NumberAxis ejeY = (NumberAxis) plot.getRangeAxis();
        ejeY.setTickLabelPaint(C_GRIS_MUTED);
        ejeY.setAxisLinePaint(C_AXIS_LINE); // <-- CORREGIDO: Se agregó el 'set'
        ejeY.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        ejeY.setNumberFormatOverride(new java.text.DecimalFormat("#,##0.0' L'"));

        return chartPanelLimpio(chart);
    }

    /**
     * 2. Barras por turno: corrección de SQL concatenado
     */
    private ChartPanel crearBarrasTurnos(Connection cn) throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] turnos = {"mañana", "tarde", "noche"};
        String[] etiq = {"Mañana", "Tarde", "Noche"};

        PreparedStatement ps = cn.prepareStatement(
                "SELECT MAX(litros) maximo, MIN(litros) minimo, AVG(litros) promedio FROM produccion_leche WHERE turno=? AND fecha >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)"
        );

        for (int i = 0; i < turnos.length; i++) {
            ps.setString(1, turnos[i]);
            ResultSet rs = ps.executeQuery();
            double max = 0, min = 0, prom = 0;
            if (rs.next()) {
                max = rs.getDouble("maximo");
                min = rs.getDouble("minimo");
                prom = rs.getDouble("promedio");
            }
            dataset.addValue(max, "Máximo", etiq[i]);
            dataset.addValue(prom, "Promedio", etiq[i]);
            dataset.addValue(min, "Mínimo", etiq[i]);
        }

        JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(C_GRID);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, C_AZUL);
        renderer.setSeriesPaint(1, C_AQUA);
        renderer.setSeriesPaint(2, C_AMARILLO);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.10);
        renderer.setItemMargin(0.06);

        estilizarEjes(plot, "#,##0.0' L'");
        return chartPanelLimpio(chart);
    }

    /**
     * 3. Ranking Top 10: corrección del orden inverso en gráficos horizontales
     */
    private ChartPanel crearBarrasRanking(Connection cn) throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Corrección: ORDER BY total ASC para que el mayor quede arriba en barras horizontales
        ResultSet rs = cn.prepareStatement(
                "SELECT r.nombre, SUM(p.litros) total FROM produccion_leche p JOIN registro r ON p.idregistro = r.idregistro WHERE MONTH(p.fecha)=MONTH(CURDATE()) AND YEAR(p.fecha)=YEAR(CURDATE()) GROUP BY r.nombre ORDER BY total ASC LIMIT 10"
        ).executeQuery();

        boolean tieneDatos = false;
        while (rs.next()) {
            tieneDatos = true;
            String nombre = rs.getString("nombre");
            if (nombre.length() > 16) {
                nombre = nombre.substring(0, 14) + "…";
            }
            dataset.addValue(rs.getDouble("total"), "Litros", nombre);
        }

        if (!tieneDatos) {
            return null;
        }

        JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.HORIZONTAL, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(C_GRID);
        plot.setDomainGridlinesVisible(false);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, C_VERDE);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.12);

        estilizarEjes(plot, "#,##0.0' L'");
        return chartPanelLimpio(chart);
    }

    /**
     * 4. Histórico 6 meses
     */
    private ChartPanel crearBarrasHistorico(Connection cn) throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        java.time.YearMonth actual = java.time.YearMonth.now();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yy");

        for (int i = 5; i >= 0; i--) {
            java.time.YearMonth ym = actual.minusMonths(i);
            String etiqueta = capitalizar(sdf.format(java.sql.Date.valueOf(ym.atDay(1))));

            ResultSet rs = cn.prepareStatement(
                    "SELECT COALESCE(SUM(litros),0) t FROM produccion_leche WHERE YEAR(fecha)=" + ym.getYear() + " AND MONTH(fecha)=" + ym.getMonthValue()
            ).executeQuery();
            double total = rs.next() ? rs.getDouble("t") : 0;
            dataset.addValue(total, "Litros producidos", etiqueta);
        }

        JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(C_GRID);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, C_VIOLETA);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.10);

        estilizarEjes(plot, "#,##0.0' L'");
        return chartPanelLimpio(chart);
    }

    private void cargarTablaDetalle(Connection cn) throws SQLException {
        ResultSet rs = cn.prepareStatement(
                "SELECT r.nombre, SUM(p.litros) total, AVG(p.litros) promedio, COUNT(*) registros FROM produccion_leche p JOIN registro r ON p.idregistro = r.idregistro WHERE MONTH(p.fecha)=MONTH(CURDATE()) AND YEAR(p.fecha)=YEAR(CURDATE()) GROUP BY r.nombre ORDER BY total DESC"
        ).executeQuery();

        ArrayList<Object[]> filas = new ArrayList<>();
        while (rs.next()) {
            filas.add(new Object[]{
                rs.getString("nombre"),
                String.format("%.1f L", rs.getDouble("total")),
                String.format("%.1f L", rs.getDouble("promedio")),
                rs.getInt("registros")
            });
        }

        SwingUtilities.invokeLater(() -> {
            DefaultTableModel m = (DefaultTableModel) tblDetalle.getModel();
            m.setRowCount(0);
            for (Object[] fila : filas) {
                m.addRow(fila);
            }
        });
    }

    private void estilizarEjes(CategoryPlot plot, String formato) {
        NumberAxis ejeN = (NumberAxis) plot.getRangeAxis();
        ejeN.setTickLabelPaint(C_GRIS_MUTED);
        ejeN.setAxisLinePaint(C_AXIS_LINE);
        ejeN.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        ejeN.setNumberFormatOverride(new java.text.DecimalFormat(formato));

        plot.getDomainAxis().setTickLabelPaint(C_GRIS_MUTED);
        plot.getDomainAxis().setAxisLinePaint(C_AXIS_LINE);
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
    }

    private ChartPanel chartPanelLimpio(JFreeChart chart) {
        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(Color.WHITE);
        cp.setBorder(BorderFactory.createEmptyBorder());
        cp.setMouseWheelEnabled(true);
        cp.setPreferredSize(new Dimension(260, 220));
        return cp;
    }

    private String capitalizar(String s) {
        return s == null || s.isEmpty() ? s : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
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
