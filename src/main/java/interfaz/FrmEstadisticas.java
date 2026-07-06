package interfaz;

import estilos.TemaFinca;
import estilos.EstiloFormularioBase;

import java.awt.*;
import javax.swing.*;

/**
 * Panel General de Estadísticas — HINORD
 * ════════════════════════════════════════════════════════════════ Ventana
 * independiente (JFrame), NO vive dentro del escritorio MDI. Centraliza el
 * panorama financiero, productivo y reproductivo de toda la finca en 3
 * secciones:
 *
 * 1. INGRESOS → venta_leche + venta_animal 2. GASTOS → compra_insumo (insumos)
 * 3. RESUMEN → todo combinado + utilidad neta + reproducción + censo del hato
 *
 * Cada sección tiene sus propios gráficos (JFreeChart), tablas, KPIs y botones
 * de exportación a PDF/Excel.
 * ════════════════════════════════════════════════════════════════
 */
public class FrmEstadisticas extends javax.swing.JFrame {

    private JPanel panelContenido;
    private CardLayout cardLayout;

    private JButton btnNavDashboard, btnNavIngresos, btnNavGastos, btnNavResumen, btnNavProduccion, btnNavVentas,btnNavReportes;

    // Nombres de las "cartas" del CardLayout
    private static final String VISTA_DASHBOARD = "DASHBOARD";
    private static final String VISTA_INGRESOS = "INGRESOS";
    private static final String VISTA_GASTOS = "GASTOS";
    private static final String VISTA_RESUMEN = "RESUMEN";
    private static final String VISTA_PRODUCCION = "PRODUCCION";
    private static final String VISTA_VENTAS = "VENTAS";
     private static final String VISTA_REPORTES = "REPORTES";
    

    // Paneles de cada sección (se construyen una sola vez, perezosamente)
    private PanelIngresos panelIngresos;
    private PanelGastos panelGastos;
    private PanelResumenGeneral panelResumen;
    private PanelProduccion panelProduccion;
    private PanelVentas panelVentas;
    private PanelDashboard panelDashboard;
    private PanelReportesAvanzados panelReportes;

    // Vista actualmente visible — se usa para no refrescar las 3 a la vez
    private String vistaActual = VISTA_RESUMEN;

    // Auto-refresco cada 5 minutos, solo de la vista visible en ese momento
    private static final int INTERVALO_AUTOREFRESCO_MS = 5 * 60 * 1000;
    private javax.swing.Timer timerAutoRefresco;

    public FrmEstadisticas() {
        setTitle("HINORD — Panel General de Estadísticas");
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // ← abre maximizada
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        construirUI();
        configurarAutoRefresco();
    }

    private void configurarAutoRefresco() {
        // 1) Timer: cada 5 minutos refresca la vista que esté visible
        timerAutoRefresco = new javax.swing.Timer(INTERVALO_AUTOREFRESCO_MS, e -> refrescarVistaActual());
        timerAutoRefresco.start();

        // Detiene el timer si la ventana se cierra, para no dejar hilos vivos
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                timerAutoRefresco.stop();
            }

            // 2) Al volver a esta ventana desde otra (otro módulo, otro frame),
            //    refresca inmediatamente sin esperar al timer.
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
                refrescarVistaActual();
            }
        });
    }

    private void refrescarVistaActual() {
        switch (vistaActual) {
            case VISTA_DASHBOARD:
                if (panelDashboard != null) {
                    panelDashboard.refrescar();
                }
                break;
            case VISTA_INGRESOS:
                if (panelIngresos != null) {
                    panelIngresos.refrescar();
                }
                break;
            case VISTA_GASTOS:
                if (panelGastos != null) {
                    panelGastos.refrescar();
                }
                break;
            case VISTA_RESUMEN:
                if (panelResumen != null) {
                    panelResumen.refrescar();
                }
                break;
            case VISTA_PRODUCCION:
                if (panelProduccion != null) {
                    panelProduccion.refrescar();
                }
                break;
            case VISTA_VENTAS:
                if (panelVentas != null) {
                    panelVentas.refrescar();

                }
                break;
            case VISTA_REPORTES:
                if (panelReportes != null) {
                    panelReportes.refrescar();

                }
                break;

        }
    }

    private void construirUI() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(TemaFinca.VERDE_CLARO);

        // Aumentamos a 125 para que quepan dos filas de botones de ser necesario en pantallas pequeñas
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(TemaFinca.VERDE_OSCURO);
        barraSuperior.setPreferredSize(new Dimension(0, 125));

        JLabel titulo = new JLabel("📊  PANEL GENERAL DE ESTADÍSTICAS — HINORD", SwingConstants.LEFT);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 24, 4, 0));
        barraSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelNav.setOpaque(false);
        panelNav.setBorder(BorderFactory.createEmptyBorder(0, 18, 5, 0));

        // Bajamos el ancho a 160 para que entren perfectamente los 7 en una sola fila horizontal
        btnNavDashboard = crearBotonNav("DASHBOARD", Color.MAGENTA);
        btnNavResumen = crearBotonNav("RESUMEN GRAL", TemaFinca.DORADO);
        btnNavIngresos = crearBotonNav("INGRESOS", TemaFinca.BTN_GUARDAR);
        btnNavGastos = crearBotonNav("GASTOS", new Color(180, 50, 50));
        btnNavProduccion = crearBotonNav("PRODUCCION", Color.BLUE);
        btnNavVentas = crearBotonNav("VENTAS", Color.ORANGE);
        btnNavReportes = crearBotonNav("REPORTES", Color.LIGHT_GRAY);

        // CONECTAR TODOS LOS LISTENERS (Faltaba el de reportes)
        btnNavDashboard.addActionListener(e -> mostrarVista(VISTA_DASHBOARD));
        btnNavIngresos.addActionListener(e -> mostrarVista(VISTA_INGRESOS));
        btnNavGastos.addActionListener(e -> mostrarVista(VISTA_GASTOS));
        btnNavProduccion.addActionListener(e -> mostrarVista(VISTA_PRODUCCION));
        btnNavVentas.addActionListener(e -> mostrarVista(VISTA_VENTAS));
        btnNavResumen.addActionListener(e -> mostrarVista(VISTA_RESUMEN));
        btnNavReportes.addActionListener(e -> mostrarVista(VISTA_REPORTES)); 

        panelNav.add(btnNavDashboard);
        panelNav.add(btnNavIngresos);
        panelNav.add(btnNavGastos);
        panelNav.add(btnNavProduccion);
        panelNav.add(btnNavVentas);
        panelNav.add(btnNavResumen);
        panelNav.add(btnNavReportes);

        barraSuperior.add(panelNav, BorderLayout.SOUTH);
        getContentPane().add(barraSuperior, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(TemaFinca.VERDE_CLARO);

        JPanel placeholderCargando = new JPanel(new GridBagLayout());
        placeholderCargando.setBackground(TemaFinca.VERDE_CLARO);
        JLabel lblCargando = new JLabel("Cargando estadísticas...");
        lblCargando.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblCargando.setForeground(TemaFinca.GRIS_TEXTO);
        placeholderCargando.add(lblCargando);
        panelContenido.add(placeholderCargando, "LOADING");

        getContentPane().add(panelContenido, BorderLayout.CENTER);

        // Mostrar Resumen General por defecto al abrir
        mostrarVista(VISTA_RESUMEN);
        setJMenuBar(null);
    }

    private JButton crearBotonNav(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Fuente un punto más pequeña para legibilidad
        EstiloFormularioBase.estilizarBoton(btn, color, null);
        btn.setPreferredSize(new Dimension(160, 38)); // Ancho optimizado a 160
        return btn;
    }

    /**
     * Cambia de sección, construyendo el panel correspondiente la primera vez.
     */
    private void mostrarVista(String vista) {
        vistaActual = vista;
        switch (vista) {
            case VISTA_DASHBOARD:
                if (panelDashboard == null) {
                    panelDashboard = new PanelDashboard();
                    panelContenido.add(panelDashboard, VISTA_DASHBOARD);
                } else {
                    panelDashboard.refrescar();
                }
                break;
            case VISTA_INGRESOS:
                if (panelIngresos == null) {
                    panelIngresos = new PanelIngresos();
                    panelContenido.add(panelIngresos, VISTA_INGRESOS);
                } else {
                    panelIngresos.refrescar();
                }
                break;
            case VISTA_GASTOS:
                if (panelGastos == null) {
                    panelGastos = new PanelGastos();
                    panelContenido.add(panelGastos, VISTA_GASTOS);
                } else {
                    panelGastos.refrescar();
                }
                break;

            case VISTA_PRODUCCION:
                if (panelProduccion == null) {
                    panelProduccion = new PanelProduccion();
                    panelContenido.add(panelProduccion, VISTA_PRODUCCION);
                } else {
                    panelProduccion.refrescar();
                }
                break;
            case VISTA_VENTAS:
                if (panelVentas == null) {
                    panelVentas = new PanelVentas();
                    panelContenido.add(panelVentas, VISTA_VENTAS);
                } else {
                    panelVentas.refrescar();
                }
                break;
            case VISTA_RESUMEN:
                if (panelResumen == null) {
                    panelResumen = new PanelResumenGeneral();
                    panelContenido.add(panelResumen, VISTA_RESUMEN);
                } else {
                    panelResumen.refrescar();
                }
                break;
            case VISTA_REPORTES:
                if (panelReportes == null) {
                    panelReportes = new PanelReportesAvanzados();
                    panelContenido.add(panelReportes, VISTA_REPORTES);
                } else {
                    panelReportes.refrescar();
                }
                break;
        }
        cardLayout.show(panelContenido, vista);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmEstadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmEstadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmEstadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmEstadisticas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmEstadisticas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
