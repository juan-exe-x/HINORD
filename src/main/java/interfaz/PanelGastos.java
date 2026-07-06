package interfaz;

import estilos.TemaFinca;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class PanelGastos extends javax.swing.JPanel {

    private static final Color C_ROJO = new Color(0xe3, 0x49, 0x48);
    private static final Color C_NARANJA = new Color(0xeb, 0x68, 0x34);
    private static final Color C_AZUL = new Color(0x2a, 0x78, 0xd6);
    private static final Color C_AQUA = new Color(0x1b, 0xaf, 0x7a);
    private static final Color C_AMARILLO = new Color(0xed, 0xa1, 0x00);
    private static final Color C_VERDE = new Color(0x00, 0x83, 0x00);
    private static final Color C_VIOLETA = new Color(0x4a, 0x3a, 0xa7);
    private static final Color C_GRIS_MUTED = new Color(137, 135, 129);
    private static final Color C_GRID = new Color(225, 224, 217);
    private static final Color C_AXIS_LINE = new Color(195, 194, 183);
    private static final Color C_GRIS_VACIO = new Color(210, 210, 210);
    private static final Color[] PALETA_CATEGORIAS = {C_ROJO, C_NARANJA, C_AMARILLO, C_VIOLETA, C_AZUL, C_GRIS_MUTED};

    private JLabel lblKpiHoy, lblKpiSemana, lblKpiMes, lblKpiAnio;
    private JTable tblDetalle;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscador;
    private JPanel gridGraficos;

    public PanelGastos() {
        setLayout(new BorderLayout());
        setBackground(TemaFinca.VERDE_CLARO);
        construirUI();
        refrescar();
    }
// =========================================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    // =========================================================================

    private void construirUI() {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(TemaFinca.VERDE_CLARO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel titulo = new JLabel("GASTOS - Compra de Insumos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(140, 30, 30));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(titulo);
        contenedor.add(Box.createVerticalStrut(16));

        // --- TARJETAS DE MÉTRICAS (KPIs) ---
        JPanel panelKpis = new JPanel(new GridLayout(1, 4, 16, 0));
        panelKpis.setOpaque(false);
        panelKpis.setMaximumSize(new Dimension(1500, 90));
        panelKpis.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardHoy = crearCardKpi("GASTADO HOY");
        lblKpiHoy = (JLabel) cardHoy.getComponent(1);
        JPanel cardSemana = crearCardKpi("ESTA SEMANA");
        lblKpiSemana = (JLabel) cardSemana.getComponent(1);
        JPanel cardMes = crearCardKpi("ESTE MES");
        lblKpiMes = (JLabel) cardMes.getComponent(1);
        JPanel cardAnio = crearCardKpi("ESTE AÑO");
        lblKpiAnio = (JLabel) cardAnio.getComponent(1);

        panelKpis.add(cardHoy);
        panelKpis.add(cardSemana);
        panelKpis.add(cardMes);
        panelKpis.add(cardAnio);
        contenedor.add(panelKpis);
        contenedor.add(Box.createVerticalStrut(24));

        // --- GRID DE GRÁFICOS ---
        gridGraficos = new JPanel(new GridLayout(2, 2, 18, 18));
        gridGraficos.setOpaque(false);
        gridGraficos.setMaximumSize(new Dimension(1500, 760));
        gridGraficos.setPreferredSize(new Dimension(1500, 760));
        gridGraficos.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(gridGraficos);
        contenedor.add(Box.createVerticalStrut(24));

        // --- ENCABEZADO DE TABLA CON FILTRO INTERACTIVO ---
        JPanel panelEncabezadoTabla = new JPanel(new BorderLayout());
        panelEncabezadoTabla.setOpaque(false);
        panelEncabezadoTabla.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEncabezadoTabla.setMaximumSize(new Dimension(1500, 30));

        JLabel lblDetalle = new JLabel("Detalle de compras recientes");
        lblDetalle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDetalle.setForeground(TemaFinca.GRIS_TEXTO);
        panelEncabezadoTabla.add(lblDetalle, BorderLayout.WEST);

        // Buscador interactivo en tiempo real
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelFiltro.setOpaque(false);
        panelFiltro.add(new JLabel("Buscar: "));
        txtBuscador = new JTextField(20);
        txtBuscador.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscador.addCaretListener(e -> {
            String texto = txtBuscador.getText();
            if (texto.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            }
        });
        panelFiltro.add(txtBuscador);
        panelEncabezadoTabla.add(panelFiltro, BorderLayout.EAST);
        contenedor.add(panelEncabezadoTabla);
        contenedor.add(Box.createVerticalStrut(8));

        // Configuración de Tabla
        modeloTabla = new DefaultTableModel(new Object[][]{}, new String[]{"Insumo", "Proveedor", "Cantidad", "Fecha", "Valor"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetalle = new JTable(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tblDetalle.setRowSorter(sorter);

        tblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblDetalle.setRowHeight(26);
        tblDetalle.setShowVerticalLines(false);
        tblDetalle.setGridColor(new Color(240, 240, 240));
        tblDetalle.getTableHeader().setBackground(new Color(140, 30, 30));
        tblDetalle.getTableHeader().setForeground(Color.WHITE);
        tblDetalle.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblDetalle.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollTabla = new JScrollPane(tblDetalle);
        scrollTabla.setPreferredSize(new Dimension(1500, 240));
        scrollTabla.setMaximumSize(new Dimension(1500, 240));
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(225, 224, 217)));
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

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(C_GRIS_MUTED);

        JLabel lblValor = new JLabel("$ 0");
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(new Color(140, 30, 30));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    private JPanel envolverGrafico(String titulo, JComponent grafico, JPanel leyendaOpcional) {
        JPanel contenedor = new JPanel(new BorderLayout(0, 8));
        contenedor.setBackground(Color.WHITE);
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 224, 217)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TemaFinca.GRIS_TEXTO);
        contenedor.add(lbl, BorderLayout.NORTH);
        contenedor.add(grafico, BorderLayout.CENTER);
        if (leyendaOpcional != null) {
            contenedor.add(leyendaOpcional, BorderLayout.SOUTH);
        }
        return contenedor;
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
    // REFRESCAR ASÍNCRONO (SWINGWORKER)
    // =========================================================================
    public void refrescar() {
        lblKpiHoy.setText("Cargando...");
        lblKpiSemana.setText("Cargando...");
        lblKpiMes.setText("Cargando...");
        lblKpiAnio.setText("Cargando...");

        SwingWorker<DatosGastos, Void> worker = new SwingWorker<>() {
            @Override
            protected DatosGastos doInBackground() throws Exception {
                DatosGastos datos = new DatosGastos();

                try (Connection cn = clases.conexion.conectar()) {
                    // 1. Carga de KPIs Financieros
                    datos.kpiHoy = sumarGastosBackend(cn, "fecha_compra = CURDATE()");
                    datos.kpiSemana = sumarGastosBackend(cn, "YEARWEEK(fecha_compra,1) = YEARWEEK(CURDATE(),1)");
                    datos.kpiMes = sumarGastosBackend(cn, "MONTH(fecha_compra)=MONTH(CURDATE()) AND YEAR(fecha_compra)=YEAR(CURDATE())");
                    datos.kpiAnio = sumarGastosBackend(cn, "YEAR(fecha_compra)=YEAR(CURDATE())");

                    // 2. Carga del Historial de la Tabla
                    datos.filasTabla = procesarDetalleBackend(cn);

                    // 3. Dataset Gráfico 1: Línea de Tendencia (Últimos 30 días)
                    datos.datasetTendencia = procesarTendenciaBackend(cn);

                    // 4. Dataset Gráfico 2: Top 5 Insumos individuales
                    procesarTopInsumosBackend(cn, datos);

                    // 5. Dataset Gráfico 3: Distribución por Categoría de Insumo
                    procesarCategoriasDonutBackend(cn, datos);

                    // 6. Dataset Gráfico 4: Comparativo Mensual (Mes Actual vs Mes Anterior)
                    procesarComparativoMensualBackend(cn, datos);
                }
                return datos;
            }

            @Override
            protected void done() {
                try {
                    DatosGastos d = get();

                    // Asignar los textos formateados a los KPIs
                    lblKpiHoy.setText(formatoMoneda(d.kpiHoy));
                    lblKpiSemana.setText(formatoMoneda(d.kpiSemana));
                    lblKpiMes.setText(formatoMoneda(d.kpiMes));
                    lblKpiAnio.setText(formatoMoneda(d.kpiAnio));

                    // Rellenar JTable
                    modeloTabla.setRowCount(0);
                    for (Object[] fila : d.filasTabla) {
                        modeloTabla.addRow(fila);
                    }

                    // Renderizar los componentes de JFreeChart en el EDT
                    renderizarPanelGraficos(d);

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PanelGastos.this,
                            "Error al actualizar panel de gastos: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // =========================================================================
    // CONSULTAS EN SEGUNDO PLANO (HILO BACKEND)
    // =========================================================================
    private double sumarGastosBackend(Connection cn, String condicion) throws SQLException {
        String sql = "SELECT COALESCE(SUM(valor_total),0) AS total FROM compra_insumo WHERE " + condicion;
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble("total") : 0;
        }
    }

    private List<Object[]> procesarDetalleBackend(Connection cn) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT i.nombre, c.proveedor, c.cantidad, c.fecha_compra, c.valor_total "
                + "FROM compra_insumo c JOIN insumo i ON c.id_insumo = i.id_insumo "
                + "ORDER BY c.fecha_compra DESC LIMIT 50";
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("proveedor"),
                    rs.getDouble("cantidad"),
                    rs.getDate("fecha_compra"),
                    formatoMoneda(rs.getDouble("valor_total"))
                });
            }
        }
        return lista;
    }

    private TimeSeriesCollection procesarTendenciaBackend(Connection cn) throws SQLException {
        Map<java.time.LocalDate, Double> porDia = new LinkedHashMap<>();
        java.time.LocalDate hoy = java.time.LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            porDia.put(hoy.minusDays(i), 0.0);
        }

        String sql = "SELECT fecha_compra, valor_total FROM compra_insumo WHERE fecha_compra >= DATE_SUB(CURDATE(), INTERVAL 29 DAY)";
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Date fSql = rs.getDate("fecha_compra");
                if (fSql != null) {
                    java.time.LocalDate f = fSql.toLocalDate();
                    porDia.merge(f, rs.getDouble("valor_total"), Double::sum);
                }
            }
        }

        TimeSeries serie = new TimeSeries("Gastos diarios");
        for (Map.Entry<java.time.LocalDate, Double> e : porDia.entrySet()) {
            java.time.LocalDate f = e.getKey();
            serie.add(new Day(f.getDayOfMonth(), f.getMonthValue(), f.getYear()), e.getValue());
        }
        return new TimeSeriesCollection(serie);
    }

    private void procesarTopInsumosBackend(Connection cn, DatosGastos datos) throws SQLException {
        String sql = "SELECT i.nombre, SUM(c.valor_total) AS total "
                + "FROM compra_insumo c JOIN insumo i ON c.id_insumo = i.id_insumo "
                + "WHERE c.fecha_compra >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) "
                + "GROUP BY i.nombre ORDER BY total DESC LIMIT 5";
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                if (nombre != null && nombre.length() > 18) {
                    nombre = nombre.substring(0, 16) + "...";
                }
                datos.topInsumosNombres.add(nombre);
                datos.topInsumosValores.add(rs.getDouble("total"));
            }
        }
    }

    private void procesarCategoriasDonutBackend(Connection cn, DatosGastos datos) throws SQLException {
        String sql = "SELECT cat.nombre AS categoria, SUM(c.valor_total) AS total "
                + "FROM compra_insumo c "
                + "JOIN insumo i ON c.id_insumo = i.id_insumo "
                + "JOIN categoria_insumo cat ON i.id_categoria = cat.id_categoria "
                + "GROUP BY cat.nombre ORDER BY total DESC LIMIT 6";
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                datos.catDonutNombres.add(rs.getString("categoria"));
                datos.catDonutValores.add(rs.getDouble("total"));
            }
        }
    }

    private void procesarComparativoMensualBackend(Connection cn, DatosGastos datos) throws SQLException {
        datos.gastoMesActual = sumarGastosBackend(cn, "MONTH(fecha_compra)=MONTH(CURDATE()) AND YEAR(fecha_compra)=YEAR(CURDATE())");
        datos.gastoMesAnterior = sumarGastosBackend(cn, "MONTH(fecha_compra)=MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) AND YEAR(fecha_compra)=YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))");
    }

    // =========================================================================
    // DIBUJO Y ENSAMBLE VISUAL (HILO EDT)
    // =========================================================================
    private void renderizarPanelGraficos(DatosGastos d) {
        gridGraficos.removeAll();

        // 1. Gráfico de Línea - Tendencia
        JFreeChart chartLinea = ChartFactory.createTimeSeriesChart(null, null, null, d.datasetTendencia, false, false, false);
        chartLinea.setBackgroundPaint(Color.WHITE);
        XYPlot plotLinea = chartLinea.getXYPlot();
        plotLinea.setBackgroundPaint(Color.WHITE);
        plotLinea.setDomainGridlinesVisible(false);
        plotLinea.setRangeGridlinePaint(C_GRID);
        plotLinea.setOutlineVisible(false);

        XYLineAndShapeRenderer rendererLinea = new XYLineAndShapeRenderer(true, false);
        rendererLinea.setSeriesPaint(0, C_ROJO);
        rendererLinea.setSeriesStroke(0, new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        plotLinea.setRenderer(rendererLinea);

        DateAxis ejeX = (DateAxis) plotLinea.getDomainAxis();
        ejeX.setDateFormatOverride(new SimpleDateFormat("dd/MM"));
        ejeX.setTickLabelPaint(C_GRIS_MUTED);
        ejeX.setAxisLinePaint(C_AXIS_LINE);
        ejeX.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));

        NumberAxis ejeY = (NumberAxis) plotLinea.getRangeAxis();
        ejeY.setTickLabelPaint(C_GRIS_MUTED);
        ejeY.setAxisLinePaint(C_AXIS_LINE);
        ejeY.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        ejeY.setNumberFormatOverride(new java.text.DecimalFormat("$#,##0"));

        gridGraficos.add(envolverGrafico("Tendencia de gastos - últimos 30 días", crearChartPanelSinBorde(chartLinea), null));

        // 2. Gráfico de Barras - Top 5 Insumos
        DefaultCategoryDataset dsBarras = new DefaultCategoryDataset();
        for (int i = 0; i < d.topInsumosNombres.size(); i++) {
            dsBarras.addValue(d.topInsumosValores.get(i), "Gasto", d.topInsumosNombres.get(i));
        }
        JFreeChart chartBarras = ChartFactory.createBarChart(null, null, null, dsBarras, PlotOrientation.HORIZONTAL, false, false, false);
        chartBarras.setBackgroundPaint(Color.WHITE);
        CategoryPlot plotBarras = chartBarras.getCategoryPlot();
        plotBarras.setBackgroundPaint(Color.WHITE);
        plotBarras.setRangeGridlinePaint(C_GRID);
        plotBarras.setDomainGridlinesVisible(false);
        plotBarras.setOutlineVisible(false);

        BarRenderer rendererBarras = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                return PALETA_CATEGORIAS[column % PALETA_CATEGORIAS.length];
            }
        };
        rendererBarras.setBarPainter(new StandardBarPainter());
        rendererBarras.setShadowVisible(false);
        rendererBarras.setMaximumBarWidth(0.14);
        plotBarras.setRenderer(rendererBarras);
        estilizarEjesCategoria(plotBarras, "$#,##0");

        gridGraficos.add(envolverGrafico("Top 5 insumos con mayor gasto - últimos 30 días", crearChartPanelSinBorde(chartBarras), null));

        // 3. Gráfico Donut - Distribución de Categorías
        gridGraficos.add(crearDonutCategoriasUI(d));

        // 4. Gráfico Donut - Comparativo Mensual
        gridGraficos.add(crearDonutComparativoUI(d));

        gridGraficos.revalidate();
        gridGraficos.repaint();
    }

    private JPanel crearDonutCategoriasUI(DatosGastos d) {
        double total = d.catDonutValores.stream().mapToDouble(Double::doubleValue).sum();
        boolean sinDatos = d.catDonutNombres.isEmpty() || total <= 0;

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        if (sinDatos) {
            dataset.setValue("Sin compras registradas", 1);
        } else {
            for (int i = 0; i < d.catDonutNombres.size(); i++) {
                dataset.setValue(d.catDonutNombres.get(i), d.catDonutValores.get(i));
            }
        }

        JFreeChart chart = construirDonutBase(dataset);
        RingPlot plot = (RingPlot) chart.getPlot();
        if (sinDatos) {
            plot.setSectionPaint("Sin compras registradas", C_GRIS_VACIO);
        } else {
            for (int i = 0; i < d.catDonutNombres.size(); i++) {
                plot.setSectionPaint(d.catDonutNombres.get(i), PALETA_CATEGORIAS[i % PALETA_CATEGORIAS.length]);
            }
        }

        String pctTexto = "0%";
        String etiquetaTop = "Sin datos";
        if (!sinDatos) {
            double pctTop = (d.catDonutValores.get(0) / total) * 100;
            pctTexto = Math.round(pctTop) + "%";
            etiquetaTop = d.catDonutNombres.get(0);
            if (etiquetaTop.length() > 14) {
                etiquetaTop = etiquetaTop.substring(0, 12) + "...";
            }
        }

        JPanel centro = superponerTextoCentral(crearChartPanelSinBorde(chart), pctTexto, etiquetaTop, new Color(140, 30, 30));

        JPanel leyenda;
        if (sinDatos) {
            leyenda = crearLeyenda(new String[]{"Sin compras registradas"}, new Color[]{C_GRIS_VACIO});
        } else {
            String[] etiquetas = new String[d.catDonutNombres.size()];
            Color[] colores = new Color[d.catDonutNombres.size()];
            for (int i = 0; i < d.catDonutNombres.size(); i++) {
                double pct = (d.catDonutValores.get(i) / total) * 100;
                etiquetas[i] = d.catDonutNombres.get(i) + " - " + Math.round(pct) + "%";
                colores[i] = PALETA_CATEGORIAS[i % PALETA_CATEGORIAS.length];
            }
            leyenda = crearLeyenda(etiquetas, colores);
        }

        return volverAEnvolver("Distribución del gasto por categoría", centro, leyenda);
    }

    private JPanel crearDonutComparativoUI(DatosGastos d) {
        double total = d.gastoMesActual + d.gastoMesAnterior;
        boolean sinDatos = total <= 0;

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        if (sinDatos) {
            dataset.setValue("Sin gastos registrados", 1);
        } else {
            dataset.setValue("Mes actual", d.gastoMesActual);
            dataset.setValue("Mes anterior", d.gastoMesAnterior);
        }

        JFreeChart chart = construirDonutBase(dataset);
        RingPlot plot = (RingPlot) chart.getPlot();
        if (sinDatos) {
            plot.setSectionPaint("Sin gastos registrados", C_GRIS_VACIO);
        } else {
            plot.setSectionPaint("Mes actual", C_ROJO);
            plot.setSectionPaint("Mes anterior", C_NARANJA);
        }

        String textoCentral;
        String subTexto;
        Color colorTexto;
        if (sinDatos) {
            textoCentral = "-";
            subTexto = "Sin datos";
            colorTexto = C_GRIS_MUTED;
        } else if (d.gastoMesAnterior <= 0) {
            textoCentral = formatoMonedaCorta(d.gastoMesActual);
            subTexto = "Mes actual";
            colorTexto = new Color(140, 30, 30);
        } else {
            double variacion = ((d.gastoMesActual - d.gastoMesAnterior) / d.gastoMesAnterior) * 100;
            String signo = variacion >= 0 ? "+" : "";
            textoCentral = signo + Math.round(variacion) + "%";
            subTexto = "vs mes anterior";
            colorTexto = variacion >= 0 ? C_ROJO : C_VERDE;
        }

        JPanel centro = superponerTextoCentral(crearChartPanelSinBorde(chart), textoCentral, subTexto, colorTexto);

        JPanel leyenda = sinDatos
                ? crearLeyenda(new String[]{"Sin gastos registrados"}, new Color[]{C_GRIS_VACIO})
                : crearLeyenda(
                        new String[]{
                            "Mes actual - " + formatoMoneda(d.gastoMesActual),
                            "Mes anterior - " + formatoMoneda(d.gastoMesAnterior)
                        },
                        new Color[]{C_ROJO, C_NARANJA});

        return volverAEnvolver("Gasto de este mes vs mes anterior", centro, leyenda);
    }

    private JPanel volverAEnvolver(String t, JComponent g, JPanel l) {
        return envolverGrafico(t, g, l);
    }

    private JFreeChart construirDonutBase(DefaultPieDataset<String> dataset) {
        JFreeChart chart = ChartFactory.createRingChart(null, dataset, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        RingPlot plot = (RingPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelGenerator(null);
        plot.setSectionOutlinesVisible(false);
        plot.setShadowPaint(null);
        plot.setInteriorGap(0.04);
        plot.setSectionDepth(0.38);
        return chart;
    }

    private JPanel superponerTextoCentral(ChartPanel chartPanel, String textoPrincipal, String textoSecundario, Color colorTexto) {
        JLabel lblCentro = new JLabel(
                "<html><center>" + textoPrincipal + "<br>"
                + "<span style='font-size:9px;color:#898781;font-weight:normal;'>" + textoSecundario + "</span></center></html>",
                SwingConstants.CENTER);
        lblCentro.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCentro.setForeground(colorTexto);

        JLayeredPane capas = new JLayeredPane();
        capas.setOpaque(false);
        capas.setPreferredSize(new Dimension(260, 220));

        chartPanel.setBounds(0, 0, 260, 220);
        lblCentro.setBounds(0, 0, 260, 220);

        capas.add(chartPanel, Integer.valueOf(0));
        capas.add(lblCentro, Integer.valueOf(1));

        capas.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                Dimension d = capas.getSize();
                chartPanel.setBounds(0, 0, d.width, d.height);
                lblCentro.setBounds(0, 0, d.width, d.height);
            }
        });

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(capas, BorderLayout.CENTER);
        return wrapper;
    }

    private void estilizarEjesCategoria(CategoryPlot plot, String formatoNumero) {
        NumberAxis ejeNumerico = (NumberAxis) plot.getRangeAxis();
        ejeNumerico.setTickLabelPaint(C_GRIS_MUTED);
        ejeNumerico.setAxisLinePaint(C_AXIS_LINE);
        ejeNumerico.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        ejeNumerico.setNumberFormatOverride(new java.text.DecimalFormat(formatoNumero));

        CategoryAxis ejeCategoria = plot.getDomainAxis();
        ejeCategoria.setTickLabelPaint(C_GRIS_MUTED);
        ejeCategoria.setAxisLinePaint(C_AXIS_LINE);
        ejeCategoria.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
    }

    private ChartPanel crearChartPanelSinBorde(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createEmptyBorder());
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(260, 220));
        return chartPanel;
    }

    private String formatoMoneda(double valor) {
        return "$ " + String.format("%,.0f", valor).replace(",", ".");
    }

    private String formatoMonedaCorta(double valor) {
        if (valor >= 1_000_000) {
            return "$" + String.format("%.1f", valor / 1_000_000) + "M";
        } else if (valor >= 1_000) {
            return "$" + String.format("%.0f", valor / 1_000) + "k";
        }
        return "$" + String.format("%.0f", valor);
    }

    // =========================================================================
    // ESTRUCTURA INTERNA DE TRANSPORTE DE DATOS ASÍNCRONOS
    // =========================================================================
    private static class DatosGastos {

        double kpiHoy, kpiSemana, kpiMes, kpiAnio;
        List<Object[]> filasTabla = new ArrayList<>();
        TimeSeriesCollection datasetTendencia;

        List<String> topInsumosNombres = new ArrayList<>();
        List<Double> topInsumosValores = new ArrayList<>();

        List<String> catDonutNombres = new ArrayList<>();
        List<Double> catDonutValores = new ArrayList<>();

        double gastoMesActual;
        double gastoMesAnterior;
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
