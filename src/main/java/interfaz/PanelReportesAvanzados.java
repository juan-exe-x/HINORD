package interfaz;

import clases.conexion;
import estilos.TemaFinca;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PanelReportesAvanzados extends javax.swing.JPanel {

   private JTable tblDatosReporte;
    private JPanel panelGraficoContenedor;
    private JTextArea txtSugerencias;
    private JButton btnExportarPDF, btnExportarExcel;
    private JComboBox<String> cbTipoReporte;

    public PanelReportesAvanzados() {
       setLayout(new BorderLayout(15, 15));
        setBackground(TemaFinca.VERDE_CLARO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        construirFiltrosSuperior();
        construirCuerpoCentral();
        construirPanelSugerenciasInferior();
        
        // Listener para cambiar el reporte dinámicamente al seleccionar otra opción
        cbTipoReporte.addActionListener(e -> refrescar());
        
        // Carga inicial de datos
        refrescar();
    }
    
    private void construirFiltrosSuperior() {
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelFiltros.setOpaque(false);

        JLabel lblTipo = new JLabel("Seleccione Reporte:");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        cbTipoReporte = new JComboBox<>(new String[]{"Balance Financiero", "Rendimiento del Hato"});
        
        btnExportarPDF = new JButton("📕 Exportar PDF");
        btnExportarExcel = new JButton("🍏 Exportar Excel");

        btnExportarPDF.addActionListener(e -> accionExportarPDF());
        btnExportarExcel.addActionListener(e -> accionExportarExcel());

        panelFiltros.add(lblTipo);
        panelFiltros.add(cbTipoReporte);
        panelFiltros.add(btnExportarPDF);
        panelFiltros.add(btnExportarExcel);

        add(panelFiltros, BorderLayout.NORTH);
    }

    private void construirCuerpoCentral() {
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 20, 0));
        panelCentro.setOpaque(false);

        // Lado Izquierdo: Tabla
        tblDatosReporte = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Concepto", "Valor / Indicador"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JScrollPane scrollTabla = new JScrollPane(tblDatosReporte);
        panelCentro.add(scrollTabla);

        // Lado Derecho: Contenedor del Gráfico
        panelGraficoContenedor = new JPanel(new BorderLayout());
        panelGraficoContenedor.setBackground(Color.WHITE);
        panelGraficoContenedor.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel lblInfo = new JLabel("Gráfico estadístico aquí", SwingConstants.CENTER);
        panelGraficoContenedor.add(lblInfo, BorderLayout.CENTER);

        panelCentro.add(panelGraficoContenedor);

        add(panelCentro, BorderLayout.CENTER);
    }

    private void construirPanelSugerenciasInferior() {
        JPanel panelSugerencias = new JPanel(new BorderLayout(0, 8));
        panelSugerencias.setOpaque(false);
        panelSugerencias.setPreferredSize(new Dimension(100, 150));

        JLabel lblTitulo = new JLabel("💡 RECOMENDACIONES Y SUGERENCIAS DEL SISTEMA ASISTIDO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(TemaFinca.VERDE_OSCURO);

        txtSugerencias = new JTextArea();
        txtSugerencias.setEditable(false);
        txtSugerencias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSugerencias.setMargin(new Insets(10, 10, 10, 10));
        txtSugerencias.setLineWrap(true);
        txtSugerencias.setWrapStyleWord(true);

        JScrollPane scrollSugerencias = new JScrollPane(txtSugerencias);
        panelSugerencias.add(lblTitulo, BorderLayout.NORTH);
        panelSugerencias.add(scrollSugerencias, BorderLayout.CENTER);

        add(panelSugerencias, BorderLayout.SOUTH);
    }

    public void refrescar() {
        String seleccion = (String) cbTipoReporte.getSelectedItem();
        if (seleccion == null) {
            return;
        }

        DefaultTableModel modeloTabla = (DefaultTableModel) tblDatosReporte.getModel();
        modeloTabla.setRowCount(0); 
        txtSugerencias.setText(""); 

        try (Connection cn = conexion.conectar()) {
            if (cn == null) {
                txtSugerencias.setText("❌ Error: No se pudo establecer conexión con la base de datos.");
                return;
            }

            if ("Balance Financiero".equals(seleccion)) {
                cargarDatosFinancieros(cn, modeloTabla);
            } else if ("Rendimiento del Hato".equals(seleccion)) {
                cargarDatosHato(cn, modeloTabla);
            }

            // Obligamos a la UI a redibujar el nuevo gráfico asignado
            panelGraficoContenedor.revalidate();
            panelGraficoContenedor.repaint();

        } catch (SQLException e) {
            txtSugerencias.setText("❌ Error crítico al procesar las estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatosFinancieros(Connection cn, DefaultTableModel modelo) throws SQLException {
        double ingresosLeche = 0;
        double ingresosAnimales = 0;
        double gastosInsumos = 0;

        String qLeche = "SELECT COALESCE(SUM(valor_total),0) AS total FROM venta_leche WHERE MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())";
        try (PreparedStatement ps = cn.prepareStatement(qLeche); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                ingresosLeche = rs.getDouble("total");
            }
        }

        String qAnimales = "SELECT COALESCE(SUM(precio_total),0) AS total FROM venta_animal WHERE MONTH(fecha_venta)=MONTH(CURDATE()) AND YEAR(fecha_venta)=YEAR(CURDATE())";
        try (PreparedStatement ps = cn.prepareStatement(qAnimales); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                ingresosAnimales = rs.getDouble("total");
            }
        }

        String qGastos = "SELECT COALESCE(SUM(valor_total),0) AS total FROM compra_insumo WHERE MONTH(fecha_compra)=MONTH(CURDATE()) AND YEAR(fecha_compra)=YEAR(CURDATE())";
        try (PreparedStatement ps = cn.prepareStatement(qGastos); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                gastosInsumos = rs.getDouble("total");
            }
        }

        double totalIngresos = ingresosLeche + ingresosAnimales;
        double utilidadNeta = totalIngresos - gastosInsumos;

        modelo.addRow(new Object[]{"(+) Ventas de Leche", formatoMoneda(ingresosLeche)});
        modelo.addRow(new Object[]{"(+) Ventas de Animales", formatoMoneda(ingresosAnimales)});
        modelo.addRow(new Object[]{" Total Ingresos Brutos", formatoMoneda(totalIngresos)});
        modelo.addRow(new Object[]{"(-) Compras de Insumos / Gastos", formatoMoneda(gastosInsumos)});
        modelo.addRow(new Object[]{"(=) Utilidad Neta del Mes", formatoMoneda(utilidadNeta)});

        List<String> sugerencias = new ArrayList<>();
        if (gastosInsumos > totalIngresos) {
            sugerencias.add("⚠️ ALERTA FINANCIERA: Los gastos operativos de este mes superan los ingresos generados. Se recomienda pausar compras no esenciales de insumos de inmediato.");
        } else if (gastosInsumos > (totalIngresos * 0.75)) {
            sugerencias.add("🔸 SUGERENCIA: El margen operativo es muy estrecho (menor al 25%). Evalúa si los proveedores de insumos o concentrados han subido precios o si la producción de leche disminuyó.");
        } else {
            sugerencias.add("✅ RENDIMIENTO ECONÓMICO ÓPTIMO: El flujo de caja es saludable y mantiene un margen de ganancia superior al promedio requerido.");
        }

        mostrarSugerenciasEnTexto(sugerencias);
        
        // 🔴 INVOCACIÓN CORREGIDA: Renderiza las barras
        actualizarGraficoFinanciero(totalIngresos, gastosInsumos);
    }

    private void cargarDatosHato(Connection cn, DefaultTableModel modelo) throws SQLException {
        int totalActivos = 0;
        int gestacionesActivas = 0;

        String qCenso = "SELECT COUNT(*) AS total FROM registro WHERE estado_animal = 'Activo'";
        try (PreparedStatement ps = cn.prepareStatement(qCenso); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                totalActivos = rs.getInt("total");
            }
        }

        String qGest = "SELECT COUNT(*) AS total FROM gestacion WHERE estado = 'pendiente_confirmacion'";
        try (PreparedStatement ps = cn.prepareStatement(qGest); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                gestacionesActivas = rs.getInt("total");
            }
        }

        modelo.addRow(new Object[]{"Animales Activos Totales", totalActivos});
        modelo.addRow(new Object[]{"Gestaciones en Curso (Pendientes)", gestacionesActivas});

        List<String> sugerencias = new ArrayList<>();
        if (totalActivos > 0) {
            double tasaGestacion = (double) gestacionesActivas / totalActivos;
            modelo.addRow(new Object[]{"Tasa Reproductiva Estimada", String.format("%.1f%%", tasaGestacion * 100)});

            if (tasaGestacion < 0.25) {
                sugerencias.add("❌ ALERTA REPRODUCTIVA: Menos del 25% de tu hato se encuentra gestando. Esto provocará baches futuros de producción láctea. Solicita una revisión veterinaria de días abiertos.");
            } else {
                sugerencias.add("✅ ESTABILIDAD BIOLÓGICA: Los ciclos de inseminación/montas y preñeces del hato se mantienen estables.");
            }
        } else {
            sugerencias.add("🔸 No hay suficientes animales registrados activos para calcular tasas reproductivas.");
        }

        mostrarSugerenciasEnTexto(sugerencias);
        
        // 🔴 INVOCACIÓN NUEVA: Renderiza el pastel para el hato
        actualizarGraficoHato(totalActivos - gestacionesActivas, gestacionesActivas);
    }

    private void mostrarSugerenciasEnTexto(List<String> sugerencias) {
        StringBuilder sb = new StringBuilder();
        for (String sug : sugerencias) {
            sb.append("• ").append(sug).append("\n\n");
        }
        txtSugerencias.setText(sb.toString().trim());
    }

    private String formatoMoneda(double valor) {
        return "$ " + String.format("%,.0f", valor).replace(",", ".");
    }

  private void accionExportarPDF() {
    // 1. Preguntar al usuario dónde guardar el archivo
    JFileChooser selectorArchivo = new JFileChooser();
    selectorArchivo.setDialogTitle("Guardar Reporte en PDF");
    selectorArchivo.setSelectedFile(new java.io.File("Reporte_" + cbTipoReporte.getSelectedItem().toString().replace(" ", "_") + ".pdf"));

    int seleccion = selectorArchivo.showSaveDialog(this);
    if (seleccion == JFileChooser.APPROVE_OPTION) {
        java.io.File archivoElegido = selectorArchivo.getSelectedFile();

        // 2. Crear el PDF utilizando la librería OpenPDF
        com.lowagie.text.Document documento = new com.lowagie.text.Document();
        try {
            com.lowagie.text.pdf.PdfWriter.getInstance(documento, new java.io.FileOutputStream(archivoElegido));
            documento.open();

            // Título principal del reporte
            com.lowagie.text.Paragraph titulo = new com.lowagie.text.Paragraph(
                    "SOFTWARE PROGANADERÍA - REPORTE ASISTIDO\n", 
                    com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18)
            );
            titulo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            documento.add(titulo);

            // Subtítulo con el tipo de reporte
            com.lowagie.text.Paragraph subtitulo = new com.lowagie.text.Paragraph(
                    "Categoría: " + cbTipoReporte.getSelectedItem().toString() + "\n\n",
                    com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12)
            );
            documento.add(subtitulo);

            // Agregar la tabla de datos
            DefaultTableModel modelo = (DefaultTableModel) tblDatosReporte.getModel();
            com.lowagie.text.pdf.PdfPTable tablaPDF = new com.lowagie.text.pdf.PdfPTable(2); // 2 columnas
            tablaPDF.setWidthPercentage(100);

            // Encabezados de la tabla
            tablaPDF.addCell(new com.lowagie.text.Phrase("Concepto / Indicador", com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD)));
            tablaPDF.addCell(new com.lowagie.text.Phrase("Valor", com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD)));

            // Pasar las filas de la JTable de la pantalla al PDF
            for (int i = 0; i < modelo.getRowCount(); i++) {
                tablaPDF.addCell(modelo.getValueAt(i, 0).toString());
                tablaPDF.addCell(modelo.getValueAt(i, 1).toString());
            }
            documento.add(tablaPDF);

            // Sección de sugerencias al final
            documento.add(new com.lowagie.text.Paragraph("\n💡 RECOMENDACIONES DEL SISTEMA:\n", com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 11)));
            documento.add(new com.lowagie.text.Paragraph(txtSugerencias.getText(), com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 10)));

            JOptionPane.showMessageDialog(this, "¡PDF generado y guardado con éxito!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (documento.isOpen()) {
                documento.close(); // Siempre cerrar el flujo del archivo
            }
        }
    }
}

    private void accionExportarExcel() {
    // 1. Preguntar al usuario dónde guardar el archivo .xlsx
    JFileChooser selectorArchivo = new JFileChooser();
    selectorArchivo.setDialogTitle("Guardar Reporte en Excel");
    selectorArchivo.setSelectedFile(new java.io.File("Reporte_" + cbTipoReporte.getSelectedItem().toString().replace(" ", "_") + ".xlsx"));

    int seleccion = selectorArchivo.showSaveDialog(this);
    if (seleccion == JFileChooser.APPROVE_OPTION) {
        java.io.File archivoElegido = selectorArchivo.getSelectedFile();

        // 2. Crear el libro de trabajo de Excel (.xlsx) usando Apache POI
        try (org.apache.poi.ss.usermodel.Workbook libro = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet hoja = libro.createSheet("Estadísticas Hato");

            // Crear fila de encabezados
            org.apache.poi.ss.usermodel.Row filaHeaders = hoja.createRow(0);
            filaHeaders.createCell(0).setCellValue("Concepto / Indicador");
            filaHeaders.createCell(1).setCellValue("Valor");

            // Recorrer las filas del JTable e insertarlas en las celdas de Excel
            DefaultTableModel modelo = (DefaultTableModel) tblDatosReporte.getModel();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                org.apache.poi.ss.usermodel.Row filaDatos = hoja.createRow(i + 1);
                filaDatos.createCell(0).setCellValue(modelo.getValueAt(i, 0).toString());
                filaDatos.createCell(1).setCellValue(modelo.getValueAt(i, 1).toString());
            }

            // Autoajustar las columnas al contenido
            hoja.autoSizeColumn(0);
            hoja.autoSizeColumn(1);

            // Escribir el archivo en el disco duro
            try (java.io.FileOutputStream archivoSalida = new java.io.FileOutputStream(archivoElegido)) {
                libro.write(archivoSalida);
            }

            JOptionPane.showMessageDialog(this, "¡Matriz de Excel exportada correctamente!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el archivo de Excel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
    
    private void actualizarGraficoFinanciero(double ingresos, double gastos) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(ingresos, "Dinero", "Ingresos Totales");
        dataset.addValue(gastos, "Dinero", "Gastos / Insumos");

        JFreeChart chart = ChartFactory.createBarChart(
                "Flujo de Caja Mensual",      
                "Concepto",                   
                "Valor ($)",                  
                dataset,                      
                PlotOrientation.VERTICAL,     
                false,                        
                true,                         
                false                         
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        panelGraficoContenedor.removeAll(); 
        panelGraficoContenedor.add(chartPanel, BorderLayout.CENTER); 
    }

    private void actualizarGraficoHato(int normales, int gestando) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Vacas Gestando", gestando);
        dataset.setValue("Otros / Vacas Vacías", normales);

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución Reproductiva del Hato", 
                dataset, 
                true, 
                true, 
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        panelGraficoContenedor.removeAll();
        panelGraficoContenedor.add(chartPanel, BorderLayout.CENTER);
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
