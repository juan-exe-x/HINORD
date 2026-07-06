package interfaz;

import estilos.TemaFinca;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class PanelIngresos extends javax.swing.JPanel {

    private static final Color C_AZUL       = new Color(0x2a, 0x78, 0xd6);
    private static final Color C_AQUA       = new Color(0x1b, 0xaf, 0x7a);
    private static final Color C_AMARILLO   = new Color(0xed, 0xa1, 0x00);
    private static final Color C_VERDE      = new Color(0x00, 0x83, 0x00);
    private static final Color C_GRIS_MUTED = new Color(137, 135, 129);
    private static final Color C_GRID       = new Color(225, 224, 217);
    private static final Color C_AXIS_LINE  = new Color(195, 194, 183);
    private static final Color C_GRIS_VACIO = new Color(210, 210, 210);

    private JLabel lblKpiHoy, lblKpiSemana, lblKpiMes, lblKpiAnio;
    private JTable tblDetalle;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JPanel gridGraficos;
    private JTextField txtBuscador;

    public PanelIngresos() {
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

        JLabel titulo = new JLabel("INGRESOS - Venta de Leche y Venta de Ganado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(TemaFinca.VERDE_OSCURO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(titulo);
        contenedor.add(Box.createVerticalStrut(16));

        // --- TARJETAS DE MÉTRICAS (KPIs) ---
        JPanel panelKpis = new JPanel(new GridLayout(1, 4, 16, 0));
        panelKpis.setOpaque(false);
        panelKpis.setMaximumSize(new Dimension(1500, 90));
        panelKpis.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardHoy = crearCardKpi("INGRESOS HOY");
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

        // --- SECCIÓN DE TABLA CON FILTRO INTERACTIVO ---
        JPanel panelEncabezadoTabla = new JPanel(new BorderLayout());
        panelEncabezadoTabla.setOpaque(false);
        panelEncabezadoTabla.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEncabezadoTabla.setMaximumSize(new Dimension(1500, 30));

        JLabel lblDetalleLabel = new JLabel("Detalle de ventas recientes");
        lblDetalleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDetalleLabel.setForeground(TemaFinca.GRIS_TEXTO);
        panelEncabezadoTabla.add(lblDetalleLabel, BorderLayout.WEST);

        // Buscador Estilizado
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

        // Configuración Avanzada de Tabla
        modeloTabla = new DefaultTableModel(new Object[][]{}, new String[]{"Tipo", "Fecha", "Detalle", "Valor"}) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblDetalle = new JTable(modeloTabla);
        sorter = new TableRowSorter<>(modeloTabla);
        tblDetalle.setRowSorter(sorter);
        
        tblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblDetalle.setRowHeight(30);
        tblDetalle.setShowVerticalLines(false);
        tblDetalle.setGridColor(new Color(240, 240, 240));
        tblDetalle.setSelectionBackground(new Color(230, 245, 235));
        tblDetalle.setSelectionForeground(TemaFinca.VERDE_OSCURO);
        
        tblDetalle.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
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
        lblValor.setForeground(TemaFinca.VERDE_OSCURO);

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
    // REFRESCAR ASÍNCRONO (SWINGWORKER) -> EVITA QUE LA APP SE CONGELE
    // =========================================================================
    public void refrescar() {
        lblKpiHoy.setText("Cargando...");
        lblKpiSemana.setText("Cargando...");
        lblKpiMes.setText("Cargando...");
        lblKpiAnio.setText("Cargando...");

        SwingWorker<DatosDashboard, Void> worker = new SwingWorker<>() {
            @Override
            protected DatosDashboard doInBackground() throws Exception {
                DatosDashboard datos = new DatosDashboard();

                try (Connection cn = clases.conexion.conectar()) {
                    // Carga de KPIs
                    datos.kpiHoy = sumarIngresos(cn, "fecha_venta = CURDATE()", "fecha_venta = CURDATE()");
                    datos.kpiSemana = sumarIngresos(cn, "YEARWEEK(fecha_venta,1) = YEARWEEK(CURDATE(),1)", "YEARWEEK(fecha_venta,1) = YEARWEEK(CURDATE(),1)");
                    datos.kpiMes = sumarIngresos(cn, "MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())", "MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())");
                    datos.kpiAnio = sumarIngresos(cn, "YEAR(fecha_venta)=YEAR(CURDATE())", "YEAR(fecha_venta)=YEAR(CURDATE())");

                    // Carga de Filas de la Tabla
                    datos.filasTabla = procesarFilasTabla(cn);

                    // Generación de Datasets para Gráficos
                    datos.datasetTurnos = obtenerDatasetTurnos(cn);
                    datos.datasetPrecioLitro = obtenerDatasetPrecioLitro(cn);
                    procesarDonutsHistoricoYMes(cn, datos);
                }
                return datos;
            }

            @Override
            protected void done() {
                try {
                    DatosDashboard d = get();

                    // Asignar KPIs
                    lblKpiHoy.setText(formatoMoneda(d.kpiHoy));
                    lblKpiSemana.setText(formatoMoneda(d.kpiSemana));
                    lblKpiMes.setText(formatoMoneda(d.kpiMes));
                    lblKpiAnio.setText(formatoMoneda(d.kpiAnio));

                    // Actualizar Tabla de datos
                    modeloTabla.setRowCount(0);
                    for (Object[] fila : d.filasTabla) {
                        modeloTabla.addRow(fila);
                    }

                    // Renderizar los 4 Gráficos de forma limpia
                    renderizarPanelGraficos(d);

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PanelIngresos.this, "Error al actualizar panel de ingresos: " + e.getMessage(), "Error de Datos", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    // --- MÉTODOS AUXILIARES DE EXTRACCIÓN (HILO DE FONDO) ---
    private double sumarIngresos(Connection cn, String condicionLeche, String condicionAnimal) throws SQLException {
        double total = 0;
        try (PreparedStatement ps1 = cn.prepareStatement("SELECT COALESCE(SUM(valor_total),0) AS total FROM venta_leche WHERE " + condicionLeche);
             ResultSet rs1 = ps1.executeQuery()) {
            if (rs1.next()) total += rs1.getDouble("total");
        }
        try (PreparedStatement ps2 = cn.prepareStatement("SELECT COALESCE(SUM(precio_total),0) AS total FROM venta_animal WHERE " + condicionAnimal);
             ResultSet rs2 = ps2.executeQuery()) {
            if (rs2.next()) total += rs2.getDouble("total");
        }
        return total;
    }

    private List<Object[]> procesarFilasTabla(Connection cn) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        try (PreparedStatement ps = cn.prepareStatement("SELECT fecha_venta, entidad, litros_totales, valor_total FROM venta_leche ORDER BY fecha_venta DESC LIMIT 30");
             ResultSet rsLeche = ps.executeQuery()) {
            while (rsLeche.next()) {
                lista.add(new Object[]{
                    "Venta de Leche",
                    rsLeche.getDate("fecha_venta"),
                    rsLeche.getString("entidad") + " - " + rsLeche.getDouble("litros_totales") + " L",
                    formatoMoneda(rsLeche.getDouble("valor_total"))
                });
            }
        }
        try (PreparedStatement ps = cn.prepareStatement("SELECT va.fecha_venta, va.tipo_venta, va.comprador_nombre, va.precio_total, r.nombre FROM venta_animal va LEFT JOIN registro r ON va.id_animal = r.idregistro ORDER BY va.fecha_venta DESC LIMIT 30");
             ResultSet rsAnimal = ps.executeQuery()) {
            while (rsAnimal.next()) {
                lista.add(new Object[]{
                    "Venta de Animal",
                    rsAnimal.getDate("fecha_venta"),
                    (rsAnimal.getString("nombre") != null ? rsAnimal.getString("nombre") : "Animal") + " - " + rsAnimal.getString("comprador_nombre"),
                    formatoMoneda(rsAnimal.getDouble("precio_total"))
                });
            }
        }
        return lista;
    }

    private DefaultCategoryDataset obtenerDatasetTurnos(Connection cn) throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] turnos = {"mañana", "tarde", "noche"};
        String[] etiquetas = {"Mañana", "Tarde", "Noche"};

        for (int i = 0; i < turnos.length; i++) {
            try (PreparedStatement ps = cn.prepareStatement("SELECT MAX(litros) AS maximo, MIN(litros) AS minimo, AVG(litros) AS promedio FROM produccion_leche WHERE turno = ? AND fecha >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)")) {
                ps.setString(1, turnos[i]);
                try (ResultSet rs = ps.executeQuery()) {
                    double maximo = 0, minimo = 0, promedio = 0;
                    if (rs.next()) {
                        maximo = rs.getDouble("maximo");
                        minimo = rs.getDouble("minimo");
                        promedio = rs.getDouble("promedio");
                    }
                    dataset.addValue(maximo, "Máximo", etiquetas[i]);
                    dataset.addValue(promedio, "Promedio", etiquetas[i]);
                    dataset.addValue(minimo, "Mínimo", etiquetas[i]);
                }
            }
        }
        return dataset;
    }

    private DefaultCategoryDataset obtenerDatasetPrecioLitro(Connection cn) throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (PreparedStatement ps = cn.prepareStatement("SELECT fecha_venta, precio_litro FROM venta_leche ORDER BY fecha_venta DESC LIMIT 10");
             ResultSet rs = ps.executeQuery()) {
            List<Object[]> filas = new ArrayList<>();
            while (rs.next()) {
                filas.add(new Object[]{rs.getDate("fecha_venta"), rs.getDouble("precio_litro")});
            }
            Collections.reverse(filas);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            for (Object[] fila : filas) {
                dataset.addValue((Double) fila[1], "Precio por litro", sdf.format((java.sql.Date) fila[0]));
            }
        }
        return dataset;
    }

    private void procesarDonutsHistoricoYMes(Connection cn, DatosDashboard datos) throws SQLException {
        // Histórico
        try (PreparedStatement ps1 = cn.prepareStatement("SELECT COALESCE(SUM(valor_total),0) AS total FROM venta_leche");
             ResultSet rsL = ps1.executeQuery()) { if (rsL.next()) datos.histLeche = rsL.getDouble("total"); }
        try (PreparedStatement ps2 = cn.prepareStatement("SELECT COALESCE(SUM(precio_total),0) AS total FROM venta_animal");
             ResultSet rsA = ps2.executeQuery()) { if (rsA.next()) datos.histAnimal = rsA.getDouble("total"); }

        // Mensual
        String conMes = "WHERE MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())";
        try (PreparedStatement ps3 = cn.prepareStatement("SELECT COALESCE(SUM(valor_total),0) AS total FROM venta_leche " + conMes);
             ResultSet rsLM = ps3.executeQuery()) { if (rsLM.next()) datos.mesLeche = rsLM.getDouble("total"); }
        try (PreparedStatement ps4 = cn.prepareStatement("SELECT COALESCE(SUM(precio_total),0) AS total FROM venta_animal " + conMes);
             ResultSet rsAM = ps4.executeQuery()) { if (rsAM.next()) datos.mesAnimal = rsAM.getDouble("total"); }
    }

    // --- RENDERIZADO VISUAL DEL GRID EN EL EDT ---
    private void renderizarPanelGraficos(DatosDashboard d) {
        gridGraficos.removeAll();

        // 1. Gráfico de Producción por turno
        JFreeChart chart1 = ChartFactory.createBarChart(null, null, null, d.datasetTurnos, PlotOrientation.VERTICAL, false, false, false);
        configurarEstiloBarras(chart1, new Color[]{C_AZUL, C_AQUA, C_AMARILLO}, "#,##0.0' L'");
        gridGraficos.add(envolverGrafico("Producción de leche por turno - máx / prom / mín", crearChartPanelSinBorde(chart1),
                crearLeyenda(new String[]{"Máximo", "Promedio", "Mínimo"}, new Color[]{C_AZUL, C_AQUA, C_AMARILLO})));

        // 2. Gráfico Precio por litro
        JFreeChart chart2 = ChartFactory.createBarChart(null, null, null, d.datasetPrecioLitro, PlotOrientation.VERTICAL, false, false, false);
        configurarEstiloBarras(chart2, new Color[]{C_AQUA}, "$#,##0");
        gridGraficos.add(envolverGrafico("Precio por litro pagado - últimas 10 ventas", crearChartPanelSinBorde(chart2), null));

        // 3. Donut Histórico
        gridGraficos.add(crearDonutParticipacionUI(d.histLeche, d.histAnimal));

        // 4. Donut Mensual
        gridGraficos.add(crearDonutMesUI(d.mesLeche, d.mesAnimal));

        gridGraficos.revalidate();
        gridGraficos.repaint();
    }

    private void configurarEstiloBarras(JFreeChart chart, Color[] colores, String formato) {
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(C_GRID);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        for (int i = 0; i < colores.length; i++) {
            renderer.setSeriesPaint(i, colores[i]);
        }
        renderer.setMaximumBarWidth(0.10);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setItemMargin(0.06);
        estilizarEjesCategoria(plot, formato);
    }

    private JPanel crearDonutParticipacionUI(double leche, double animal) {
        double total = leche + animal;
        boolean sinDatos = total <= 0;
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        
        if (sinDatos) dataset.setValue("Sin datos", 1);
        else {
            dataset.setValue("Venta de Leche", leche);
            dataset.setValue("Venta de Animales", animal);
        }

        JFreeChart chart = construirDonutBase(dataset);
        RingPlot plot = (RingPlot) chart.getPlot();
        if (sinDatos) plot.setSectionPaint("Sin datos", C_GRIS_VACIO);
        else {
            plot.setSectionPaint("Venta de Leche", C_AZUL);
            plot.setSectionPaint("Venta de Animales", C_AQUA);
        }

        double pctLeche = sinDatos ? 0 : (leche / total) * 100;
        double pctAnimal = sinDatos ? 0 : (animal / total) * 100;

        JPanel centro = superponerTextoCentral(crearChartPanelSinBorde(chart), sinDatos ? "—" : Math.round(pctLeche) + "%", "Leche", TemaFinca.VERDE_OSCURO);
        JPanel leyenda = sinDatos ? crearLeyenda(new String[]{"Sin registros"}, new Color[]{C_GRIS_VACIO})
                : crearLeyenda(new String[]{"Leche - " + Math.round(pctLeche) + "%", "Animales - " + Math.round(pctAnimal) + "%"}, new Color[]{C_AZUL, C_AQUA});

        return volverPanelContenedorGr(centro, leyenda, "Participación de ingresos - histórico");
    }

    private JPanel crearDonutMesUI(double leche, double animal) {
        double total = leche + animal;
        boolean sinDatos = total <= 0;
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        if (sinDatos) dataset.setValue("Sin ventas", 1);
        else {
            dataset.setValue("Venta de Leche", leche);
            dataset.setValue("Venta de Animales", animal);
        }

        JFreeChart chart = construirDonutBase(dataset);
        RingPlot plot = (RingPlot) chart.getPlot();
        if (sinDatos) plot.setSectionPaint("Sin ventas", C_GRIS_VACIO);
        else {
            plot.setSectionPaint("Venta de Leche", C_VERDE);
            plot.setSectionPaint("Venta de Animales", C_AMARILLO);
        }

        double pctLeche = sinDatos ? 0 : (leche / total) * 100;
        double pctAnimal = sinDatos ? 0 : (animal / total) * 100;

        JPanel centro = superponerTextoCentral(crearChartPanelSinBorde(chart), sinDatos ? "—" : formatoMonedaCorta(total), "Total Mes", new Color(90, 70, 10));
        JPanel leyenda = sinDatos ? crearLeyenda(new String[]{"Sin ventas este mes"}, new Color[]{C_GRIS_VACIO})
                : crearLeyenda(new String[]{"Leche - " + Math.round(pctLeche) + "%", "Animales - " + Math.round(pctAnimal) + "%"}, new Color[]{C_VERDE, C_AMARILLO});

        return volverPanelContenedorGr(centro, leyenda, "Ingresos de este mes por fuente");
    }

    private JPanel volverPanelContenedorGr(JPanel centro, JPanel leyenda, String titulo) {
        return envolverGrafico(titulo, centro, leyenda);
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
        lblCentro.setFont(new Font("Segoe UI", Font.BOLD, 17));
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
    
    private static class DatosDashboard {
        double kpiHoy, kpiSemana, kpiMes, kpiAnio;
        List<Object[]> filasTabla;
        DefaultCategoryDataset datasetTurnos;
        DefaultCategoryDataset datasetPrecioLitro;
        double histLeche, histAnimal;
        double mesLeche, mesAnimal;
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
