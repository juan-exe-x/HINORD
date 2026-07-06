package interfaz;

import estilos.TemaFinca;

import estilos.TemaFinca;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class PanelDashboard extends javax.swing.JPanel {

    // ── Paleta de Colores ────────────────────────────────────────────────────
    private static final Color C_AZUL       = new Color(0x2a, 0x78, 0xd6);
    private static final Color C_AQUA       = new Color(0x1b, 0xaf, 0x7a);
    private static final Color C_ROJO       = new Color(0xe3, 0x49, 0x48);
    private static final Color C_AMARILLO   = new Color(0xed, 0xa1, 0x00);
    private static final Color C_VIOLETA    = new Color(0x4a, 0x3a, 0xa7);
    private static final Color C_ROSA       = new Color(173, 58, 119);
    private static final Color C_GRIS_MUTED = new Color(137, 135, 129);
    private static final Color C_GRID       = new Color(225, 224, 217);
    private static final Color C_AXIS_LINE  = new Color(195, 194, 183);
    private static final Color C_SURFACE    = new Color(255, 255, 255);
    private static final Color C_BORDER     = new Color(225, 224, 217);

    // ── Componentes de UI ────────────────────────────────────────────────────
    private JLabel lblIngresos, lblGastos, lblUtilidad, lblActivos;
    private JPanel contenedorGraficoLeche;
    private JPanel contenedorGraficoBalance;
    private JPanel contenedorGraficoHato; // Nuevo: Para el gráfico de distribución del hato
    private JTable tblPartos, tblVacunas, tblAlertas;

    public PanelDashboard() {
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

        // Título del Dashboard
        JLabel titulo = new JLabel("DASHBOARD — Vista General de la Finca");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(TemaFinca.VERDE_OSCURO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(titulo);
        contenedor.add(Box.createVerticalStrut(20));

        // ── FILA 1: Balance | Gráfico de Producción de Leche ─────────────────
        JPanel fila1 = new JPanel(new GridLayout(1, 2, 16, 0));
        fila1.setOpaque(false);
        fila1.setMaximumSize(new Dimension(1500, 300));
        fila1.setPreferredSize(new Dimension(1500, 300));
        fila1.setAlignmentX(Component.LEFT_ALIGNMENT);

        fila1.add(crearTarjetaBalance());
        fila1.add(crearTarjetaGraficoLeche());
        contenedor.add(fila1);
        contenedor.add(Box.createVerticalStrut(16));

        // ── FILA 2: Estado del Hato Visual | Próximos Eventos ────────────────
        JPanel fila2 = new JPanel(new GridLayout(1, 2, 16, 0));
        fila2.setOpaque(false);
        fila2.setMaximumSize(new Dimension(1500, 280));
        fila2.setPreferredSize(new Dimension(1500, 280));
        fila2.setAlignmentX(Component.LEFT_ALIGNMENT);

        fila2.add(crearTarjetaHato());
        fila2.add(crearTarjetaEventos());
        contenedor.add(fila2);
        contenedor.add(Box.createVerticalStrut(16));

        // ── FILA 3: Gráfico de Ingresos vs Gastos Histórico ──────────────────
        contenedorGraficoBalance = crearTarjetaConTitulo("Ingresos vs Gastos — últimos 6 meses");
        contenedorGraficoBalance.setMaximumSize(new Dimension(1500, 320));
        contenedorGraficoBalance.setPreferredSize(new Dimension(1500, 320));
        contenedorGraficoBalance.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenedor.add(contenedorGraficoBalance);
        contenedor.add(Box.createVerticalStrut(20));

        JScrollPane scrollGeneral = new JScrollPane(contenedor);
        scrollGeneral.setBorder(BorderFactory.createEmptyBorder());
        scrollGeneral.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollGeneral, BorderLayout.CENTER);
    }

    private JPanel crearTarjetaBalance() {
        JPanel tarjeta = crearTarjetaConTitulo("Balance Financiero — Este mes");

        JPanel filaKpis = new JPanel(new GridLayout(3, 1, 0, 10));
        filaKpis.setOpaque(false);
        filaKpis.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel cardIng = crearKpiHorizontal("Ingresos", "$ 0", C_AQUA);
        lblIngresos = (JLabel) cardIng.getComponent(1);

        JPanel cardGas = crearKpiHorizontal("Gastos", "$ 0", C_ROJO);
        lblGastos = (JLabel) cardGas.getComponent(1);

        JPanel cardUtil = crearKpiHorizontal("Utilidad Neta", "$ 0", C_AZUL);
        lblUtilidad = (JLabel) cardUtil.getComponent(1);

        filaKpis.add(cardIng);
        filaKpis.add(cardGas);
        filaKpis.add(cardUtil);

        tarjeta.add(filaKpis, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearTarjetaGraficoLeche() {
        JPanel tarjeta = crearTarjetaConTitulo("Producción de Leche — últimos 30 días");
        contenedorGraficoLeche = new JPanel(new BorderLayout());
        contenedorGraficoLeche.setBackground(C_SURFACE);
        tarjeta.add(contenedorGraficoLeche, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearTarjetaHato() {
        JPanel tarjeta = crearTarjetaConTitulo("Estado e Inventario del Hato");

        JPanel contenido = new JPanel(new BorderLayout(14, 0));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // KPI general arriba o al lado del gráfico
        JPanel cardTot = crearKpiBloque("Animales Activos en Finca", "0", TemaFinca.VERDE_OSCURO);
        lblActivos = (JLabel) cardTot.getComponent(1);
        cardTot.setPreferredSize(new Dimension(160, 0));
        contenido.add(cardTot, BorderLayout.WEST);

        // Contenedor dinámico del gráfico de pastel para dar impacto visual instantáneo
        contenedorGraficoHato = new JPanel(new BorderLayout());
        contenedorGraficoHato.setBackground(C_SURFACE);
        contenido.add(contenedorGraficoHato, BorderLayout.CENTER);

        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearTarjetaEventos() {
        JPanel tarjeta = crearTarjetaConTitulo("Próximos Eventos y Monitoreo");

        JPanel contenidoEventos = new JPanel(new GridLayout(1, 3, 10, 0));
        contenidoEventos.setOpaque(false);
        contenidoEventos.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        tblPartos = crearTablaEvento(new String[]{"Animal", "Días"});
        tblVacunas = crearTablaEvento(new String[]{"Animal", "Vacuna"});
        tblAlertas = crearTablaEvento(new String[]{"Animal", "Estado"});

        contenidoEventos.add(crearSubSeccion("Partos próximos (30d)", tblPartos, C_VIOLETA));
        contenidoEventos.add(crearSubSeccion("Vacunas programadas", tblVacunas, C_AMARILLO));
        contenidoEventos.add(crearSubSeccion("Alertas sanitarias", tblAlertas, C_ROJO));

        tarjeta.add(contenidoEventos, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearSubSeccion(String titulo, JTable tabla, Color colorHeader) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(C_GRIS_MUTED);
        p.add(lbl, BorderLayout.NORTH);

        tabla.getTableHeader().setBackground(colorHeader);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    // =========================================================================
    // COMPONENTES UI HELPERS
    // =========================================================================
    private JPanel crearTarjetaConTitulo(String titulo) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 8));
        tarjeta.setBackground(C_SURFACE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TemaFinca.GRIS_TEXTO);
        tarjeta.add(lbl, BorderLayout.NORTH);
        return tarjeta;
    }

    private JPanel crearKpiHorizontal(String etiqueta, String valorInicial, Color accentColor) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(new Color(248, 248, 248));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JPanel barra = new JPanel();
        barra.setPreferredSize(new Dimension(4, 0));
        barra.setBackground(accentColor);
        row.add(barra, BorderLayout.WEST);

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEtiqueta.setForeground(C_GRIS_MUTED);

        JLabel lblValor = new JLabel(valorInicial);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValor.setForeground(new Color(30, 30, 30));
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(lblEtiqueta, BorderLayout.CENTER);
        row.add(lblValor, BorderLayout.EAST);
        return row;
    }

    private JPanel crearKpiBloque(String etiqueta, String valorInicial, Color colorFondo) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        JLabel lblEt = new JLabel(etiqueta, SwingConstants.CENTER);
        lblEt.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEt.setForeground(new Color(255, 255, 255, 200));

        JLabel lblVal = new JLabel(valorInicial, SwingConstants.CENTER);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblVal.setForeground(Color.WHITE);

        card.add(lblEt, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);
        return card;
    }

    private JTable crearTablaEvento(String[] columnas) {
        JTable tabla = new JTable(new DefaultTableModel(new Object[][]{}, columnas));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabla.setRowHeight(22);
        tabla.setGridColor(C_GRID);
        tabla.setShowGrid(true);
        return tabla;
    }

    private JPanel itemLeyenda(String texto, Color color) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);
        JPanel cuadro = new JPanel();
        cuadro.setBackground(color);
        cuadro.setPreferredSize(new Dimension(10, 10));
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(C_GRIS_MUTED);
        item.add(cuadro);
        item.add(lbl);
        return item;
    }

    // =========================================================================
    // ARQUITECTURA DE DATOS ASÍNCRONA (SWINGWORKER)
    // =========================================================================
    public void refrescar() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection cn = clases.conexion.conectar()) {
                    // Cargar KPIs Financieros del mes actual
                    cargarBalanceData(cn);
                    
                    // Generar Gráfico Temporal de Leche (Últimos 30 días)
                    ChartPanel cpLeche = procesoGraficoLeche(cn);
                    
                    // Generar Datos Demográficos y Gráfico de Tarta del Hato
                    Object[] hatoData = procesoHatoData(cn);
                    
                    // Cargar Tablas Compactas de Monitoreo Operativo
                    Object[][] eventosTablas = procesoEventosData(cn);
                    
                    // Generar Historial Multimes de Ingresos vs Gastos (6 Meses)
                    ChartPanel cpBalance = procesoGraficoBalance(cn);

                    // Despachar actualizaciones visuales de vuelta al hilo de Swing (EDT)
                    SwingUtilities.invokeLater(() -> {
                        // Renderizar Gráfico Leche
                        contenedorGraficoLeche.removeAll();
                        if (cpLeche != null) contenedorGraficoLeche.add(cpLeche, BorderLayout.CENTER);
                        contenedorGraficoLeche.revalidate(); contenedorGraficoLeche.repaint();

                        // Renderizar Demografía e Inventario Hato
                        lblActivos.setText(String.valueOf(hatoData[0]));
                        contenedorGraficoHato.removeAll();
                        if (hatoData[1] != null) contenedorGraficoHato.add((ChartPanel) hatoData[1], BorderLayout.CENTER);
                        contenedorGraficoHato.revalidate(); contenedorGraficoHato.repaint();

                        // Renderizar Tablas de Notificaciones Operativas
                        actualizarModelosTablas(eventosTablas);

                        // Renderizar Gráfico Histórico
                        contenedorGraficoBalance.removeAll();
                        if (cpBalance != null) {
                            contenedorGraficoBalance.add(cpBalance, BorderLayout.CENTER);
                            JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 4));
                            leyenda.setBackground(C_SURFACE);
                            leyenda.add(itemLeyenda("Ingresos", C_AQUA));
                            leyenda.add(itemLeyenda("Gastos", C_ROJO));
                            contenedorGraficoBalance.add(leyenda, BorderLayout.SOUTH);
                        }
                        contenedorGraficoBalance.revalidate(); contenedorGraficoBalance.repaint();
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private void cargarBalanceData(Connection cn) throws SQLException {
        double ingresos = 0, gastos = 0;

        ResultSet rs1 = cn.prepareStatement(
                "SELECT COALESCE(SUM(valor_total),0) t FROM venta_leche WHERE MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())"
        ).executeQuery();
        if (rs1.next()) ingresos += rs1.getDouble("t");

        ResultSet rs2 = cn.prepareStatement(
                "SELECT COALESCE(SUM(precio_total),0) t FROM venta_animal WHERE MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())"
        ).executeQuery();
        if (rs2.next()) ingresos += rs2.getDouble("t");

        ResultSet rs3 = cn.prepareStatement(
                "SELECT COALESCE(SUM(valor_total),0) t FROM compra_insumo WHERE MONTH(fecha_compra)=MONTH(CURDATE()) AND YEAR(fecha_compra)=YEAR(CURDATE())"
        ).executeQuery();
        if (rs3.next()) gastos = rs3.getDouble("t");

        final double finalIng = ingresos;
        final double finalGas = gastos;
        final double utilidad = ingresos - gastos;

        SwingUtilities.invokeLater(() -> {
            lblIngresos.setText(formatoMoneda(finalIng));
            lblGastos.setText(formatoMoneda(finalGas));
            lblUtilidad.setText(formatoMoneda(utilidad));
            lblUtilidad.setForeground(utilidad < 0 ? C_ROJO : TemaFinca.VERDE_OSCURO);
        });
    }

    private ChartPanel procesoGraficoLeche(Connection cn) throws SQLException {
        java.util.Map<java.time.LocalDate, Double> porDia = new java.util.LinkedHashMap<>();
        java.time.LocalDate hoy = java.time.LocalDate.now();
        for (int i = 29; i >= 0; i--) porDia.put(hoy.minusDays(i), 0.0);

        ResultSet rs = cn.prepareStatement(
                "SELECT fecha, SUM(litros) AS total FROM produccion_leche WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 29 DAY) GROUP BY fecha"
        ).executeQuery();
        while (rs.next()) {
            java.time.LocalDate f = rs.getDate("fecha").toLocalDate();
            porDia.merge(f, rs.getDouble("total"), Double::sum);
        }

        TimeSeries serie = new TimeSeries("Litros");
        for (java.util.Map.Entry<java.time.LocalDate, Double> e : porDia.entrySet()) {
            // Corrección de bug de índice de meses en series temporales de JFreeChart
            serie.add(new Day(java.sql.Date.valueOf(e.getKey())), e.getValue());
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, null, new TimeSeriesCollection(serie), false, false, false);
        chart.setBackgroundPaint(C_SURFACE);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(C_SURFACE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(C_GRID);
        plot.setOutlineVisible(false);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, C_AQUA);
        renderer.setSeriesStroke(0, new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        plot.setRenderer(renderer);

        DateAxis ejeX = (DateAxis) plot.getDomainAxis();
        ejeX.setDateFormatOverride(new java.text.SimpleDateFormat("dd/MM"));
        ejeX.setTickLabelPaint(C_GRIS_MUTED);
        ejeX.setAxisLinePaint(C_AXIS_LINE);
        ejeX.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        NumberAxis ejeY = (NumberAxis) plot.getRangeAxis();
        ejeY.setTickLabelPaint(C_GRIS_MUTED);
        ejeY.setAxisLinePaint(C_AXIS_LINE);
        ejeY.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        ejeY.setNumberFormatOverride(new java.text.DecimalFormat("#,##0.0' L'"));

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(C_SURFACE);
        cp.setBorder(BorderFactory.createEmptyBorder());
        return cp;
    }

    /** Procesa y genera de forma visual la segmentación del ganado (Pie Chart) */
    private Object[] procesoHatoData(Connection cn) throws SQLException {
        int total = 0, hembras = 0, machos = 0, gestaciones = 0;

        ResultSet rs = cn.prepareStatement(
                "SELECT COUNT(*) total, SUM(CASE WHEN sexo='HEMBRA' THEN 1 ELSE 0 END) hembras, SUM(CASE WHEN sexo='MACHO' THEN 1 ELSE 0 END) machos FROM registro WHERE estado_animal='Activo'"
        ).executeQuery();
        if (rs.next()) {
            total = rs.getInt("total");
            hembras = rs.getInt("hembras");
            machos = rs.getInt("machos");
        }

        ResultSet rsGest = cn.prepareStatement(
                "SELECT COUNT(*) total FROM gestacion WHERE estado IN ('confirmada','en_curso')"
        ).executeQuery();
        if (rsGest.next()) gestaciones = rsGest.getInt("total");

        // Crear set de datos para el gráfico circular
        DefaultPieDataset dataset = new DefaultPieDataset();
        if (total > 0) {
            // Descontamos las hembras gestantes del grupo general para pintar el sector exacto
            int hembrasVacias = Math.max(0, hembras - gestaciones);
            dataset.setValue("Vacas/Novillas (" + hembrasVacias + ")", hembrasVacias);
            dataset.setValue("Machos/Toros (" + machos + ")", machos);
            dataset.setValue("Gestantes (" + gestaciones + ")", gestaciones);
        }

        JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, false, false);
        chart.setBackgroundPaint(C_SURFACE);
        chart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);
        chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 11));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(C_SURFACE);
        plot.setOutlineVisible(false);
        plot.setLabelGenerator(null); // Sin etiquetas colgando, la leyenda limpia basta
        
        // Colores consistentes con los KPIs de tu app
        plot.setSectionPaint(dataset.getKey(0), C_ROSA);
        plot.setSectionPaint(dataset.getKey(1), C_AZUL);
        if (gestaciones > 0) plot.setSectionPaint(dataset.getKey(2), C_AMARILLO);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(C_SURFACE);
        cp.setBorder(BorderFactory.createEmptyBorder());

        return new Object[]{total, cp};
    }

    private Object[][] procesoEventosData(Connection cn) throws SQLException {
        ArrayList<Object[]> partos = new ArrayList<>();
        ArrayList<Object[]> vacunas = new ArrayList<>();
        ArrayList<Object[]> alertas = new ArrayList<>();

        // 1. Partos
        ResultSet rs1 = cn.prepareStatement(
                "SELECT r.nombre, DATEDIFF(g.fecha_parto_estimada, CURDATE()) AS dias FROM gestacion g JOIN registro r ON g.id_vaca = r.idregistro WHERE g.estado IN ('confirmada','en_curso') AND g.fecha_parto_estimada BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) ORDER BY g.fecha_parto_estimada ASC LIMIT 8"
        ).executeQuery();
        while (rs1.next()) {
            int d = rs1.getInt("dias");
            partos.add(new Object[]{rs1.getString("nombre"), d == 0 ? "Hoy" : d + "d"});
        }

        // 2. Vacunas
        ResultSet rs2 = cn.prepareStatement(
                "SELECT p.nombre, p.enfermedadpreviene FROM prevenciones p WHERE p.fechaproxima BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) ORDER BY p.fechaproxima ASC LIMIT 8"
        ).executeQuery();
        while (rs2.next()) {
            vacunas.add(new Object[]{rs2.getString("nombre"), rs2.getString("enfermedadpreviene")});
        }

        // 3. Alertas
        ResultSet rs3 = cn.prepareStatement(
                "SELECT nombre, estado_animal FROM registro WHERE estado_animal IN ('Enfermo','Muerto') ORDER BY estado_animal ASC LIMIT 8"
        ).executeQuery();
        while (rs3.next()) {
            alertas.add(new Object[]{rs3.getString("nombre"), rs3.getString("estado_animal")});
        }

        return new Object[][]{partos.toArray(), vacunas.toArray(), alertas.toArray()};
    }

    /** Optimización: Consultas parametrizadas seguras para evitar compilaciones repetitivas */
    private ChartPanel procesoGraficoBalance(Connection cn) throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        java.time.YearMonth actual = java.time.YearMonth.now();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM yy");

        PreparedStatement psLeche = cn.prepareStatement("SELECT COALESCE(SUM(valor_total),0) t FROM venta_leche WHERE YEAR(fecha_venta)=? AND MONTH(fecha_venta)=?");
        PreparedStatement psAnimal = cn.prepareStatement("SELECT COALESCE(SUM(precio_total),0) t FROM venta_animal WHERE YEAR(fecha_venta)=? AND MONTH(fecha_venta)=?");
        PreparedStatement psInsumo = cn.prepareStatement("SELECT COALESCE(SUM(valor_total),0) t FROM compra_insumo WHERE YEAR(fecha_compra)=? AND MONTH(fecha_compra)=?");

        for (int i = 5; i >= 0; i--) {
            java.time.YearMonth ym = actual.minusMonths(i);
            String etiqueta = capitalizar(sdf.format(java.sql.Date.valueOf(ym.atDay(1))));
            int anio = ym.getYear();
            int mes = ym.getMonthValue();

            double ing = 0;
            
            psLeche.setInt(1, anio); psLeche.setInt(2, mes);
            ResultSet ri1 = psLeche.executeQuery();
            if (ri1.next()) ing += ri1.getDouble("t");

            psAnimal.setInt(1, anio); psAnimal.setInt(2, mes);
            ResultSet ri2 = psAnimal.executeQuery();
            if (ri2.next()) ing += ri2.getDouble("t");

            psInsumo.setInt(1, anio); psInsumo.setInt(2, mes);
            ResultSet rg = psInsumo.executeQuery();
            double gas = rg.next() ? rg.getDouble("t") : 0;

            dataset.addValue(ing, "Ingresos", etiqueta);
            dataset.addValue(gas, "Gastos", etiqueta);
        }

        JFreeChart chart = ChartFactory.createBarChart(null, null, null, dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(C_SURFACE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(C_SURFACE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinePaint(C_GRID);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, C_AQUA);
        renderer.setSeriesPaint(1, C_ROJO);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setMaximumBarWidth(0.08);
        renderer.setItemMargin(0.04);

        NumberAxis ejeY = (NumberAxis) plot.getRangeAxis();
        ejeY.setTickLabelPaint(C_GRIS_MUTED);
        ejeY.setAxisLinePaint(C_AXIS_LINE);
        ejeY.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        ejeY.setNumberFormatOverride(new java.text.DecimalFormat("$#,##0"));

        CategoryAxis ejeX = plot.getDomainAxis();
        ejeX.setTickLabelPaint(C_GRIS_MUTED);
        ejeX.setAxisLinePaint(C_AXIS_LINE);
        ejeX.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(C_SURFACE);
        cp.setBorder(BorderFactory.createEmptyBorder());
        return cp;
    }

    private void actualizarModelosTablas(Object[][] datos) {
        Object[] partos = (Object[]) datos[0];
        Object[] vacunas = (Object[]) datos[1];
        Object[] alertas = (Object[]) datos[2];

        DefaultTableModel mPartos = (DefaultTableModel) tblPartos.getModel();
        mPartos.setRowCount(0);
        for (Object obj : partos) mPartos.addRow((Object[]) obj);

        DefaultTableModel mVacunas = (DefaultTableModel) tblVacunas.getModel();
        mVacunas.setRowCount(0);
        for (Object obj : vacunas) mVacunas.addRow((Object[]) obj);

        DefaultTableModel mAlertas = (DefaultTableModel) tblAlertas.getModel();
        mAlertas.setRowCount(0);
        for (Object obj : alertas) mAlertas.addRow((Object[]) obj);
    }

    // =========================================================================
    // UTILIDADES FORMATO
    // =========================================================================
    private String capitalizar(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
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
