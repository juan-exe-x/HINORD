package interfaz;

import clases.conexion;
import clases.panelmenudegradado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Desktop;
import java.beans.PropertyVetoException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class frmnuevomenu extends javax.swing.JFrame {

    public frmnuevomenu() {

        escritorio = new clases.panelmenudegradado();

        // ── NUEVO: wrap del escritorio en JScrollPane ─────────────────────
        javax.swing.JScrollPane scrollEscritorio = new javax.swing.JScrollPane(escritorio);
        scrollEscritorio.setHorizontalScrollBarPolicy(
                javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollEscritorio.setVerticalScrollBarPolicy(
                javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollEscritorio.setBorder(null); // sin borde para que no se note
        scrollEscritorio.getViewport().setOpaque(false); // mantiene el fondo degradado
        scrollEscritorio.setOpaque(false);

        setContentPane(scrollEscritorio); // 👈 el scroll, no el escritorio directo

        crearMenuBar();

        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        this.setUndecorated(true);

        setVisible(true);

        cargarDashboard();
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (panelCards != null) {
                    int x = (getWidth() - panelCards.getWidth()) / 2;
                    panelCards.setLocation(x, 80); // 👈 mismo valor que en cargarDashboard
                }
            }
        });

    }

    /**
     * Abre un JInternalFrame centrado horizontalmente y siempre por DEBAJO del
     * JMenuBar, para que la barra de título sea visible.
     */
    private void abrirMDI(javax.swing.JInternalFrame ventana) {
        escritorio.add(ventana);
        try {
            ventana.setSelected(true);
        } catch (java.beans.PropertyVetoException ex) {
        }

        ventana.setVisible(true); // 👈 visible ANTES de calcular, para que tenga tamaño real

        int menuBarH = getJMenuBar() != null ? getJMenuBar().getHeight() : 0;

        // Área disponible real = ventana principal menos el menu bar
        int areaW = getWidth();
        int areaH = getHeight() - menuBarH;

        int frameW = ventana.getWidth();
        int frameH = ventana.getHeight();

        // Centrado; si el frame es más grande que el área, lo pone en (0, menuBarH)
        int x = Math.max(0, (areaW - frameW) / 2);
        int y = menuBarH + Math.max(0, (areaH - frameH) / 2);

        ventana.setLocation(x, y);
        escritorio.revalidate();
        escritorio.repaint();
    }
    private clases.panelmenudegradado escritorio;
    private JPanel panelCards;

    private void crearMenuBar() {

        // 🎨 FUENTES
        Font fuenteMenu = new Font("Segoe UI", Font.BOLD, 15);
        Font fuenteItem = new Font("Segoe UI", Font.PLAIN, 14);

        // 🎯 COLORES
        Color colorBarra = new Color(27, 67, 50);
        Color colorHover = new Color(46, 125, 50);
        Color colorActivo = new Color(56, 142, 60);
        Color colorItemHover = new Color(200, 230, 201);

        // 🔹 BARRA
        JMenuBar barra = new JMenuBar();
        barra.setBackground(colorBarra);
        barra.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // ============================
        // 🔹 MENÚ REGISTRO
        // ============================
        JMenu menuRegistro = new JMenu("Gestion Animal ");
        menuRegistro.setForeground(Color.WHITE);
        menuRegistro.setFont(fuenteMenu);
        menuRegistro.setIcon(new ImageIcon(getClass().getResource("/imagenes/registro.png")));
        menuRegistro.setIconTextGap(10);
        menuRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenu bovinos = new JMenu("Bovinos");
        JMenu mascotas = new JMenu("Mascotas");

        bovinos.setFont(fuenteItem);
        mascotas.setFont(fuenteItem);

        bovinos.setIcon(new ImageIcon(getClass().getResource("/imagenes/bovino.png")));
        mascotas.setIcon(new ImageIcon(getClass().getResource("/imagenes/mascotas.png")));

        // Hover items
        agregarHoverItem(bovinos, colorItemHover);
        agregarHoverItem(mascotas, colorItemHover);

        menuRegistro.add(bovinos);
        menuRegistro.add(mascotas);

        // Hover menú
        agregarHoverMenu(menuRegistro, colorBarra, colorHover);

        // ============================
        // 🔹 MENÚ AÑADIR BOVINO
        // ============================
        JMenuItem AÑADIR = new JMenuItem("Añadir Bovino");
        JMenuItem Vacunas = new JMenuItem("ingresar vacunacion");

        //registro bovino
        AÑADIR.addActionListener(e -> abrirMDI(new mdiRegistro()));

        //registro de vacunas 
        Vacunas.addActionListener(e -> abrirMDI(new mdiPrevenciones()));

        AÑADIR.setFont(fuenteItem);
        Vacunas.setFont(fuenteItem);

        AÑADIR.setIcon(new ImageIcon(getClass().getResource("/imagenes/vaquita.png")));
        Vacunas.setIcon(new ImageIcon(getClass().getResource("/imagenes/medico.png")));

        agregarHoverItem(AÑADIR, colorItemHover);
        agregarHoverItem(Vacunas, colorItemHover);

        bovinos.add(AÑADIR);
        bovinos.add(Vacunas);

        // ============================
        // 🔹 MODULO REPRODUCTIVO
        // ============================
        JMenu menuReproductivo = new JMenu("Reproductivo");
        menuReproductivo.setForeground(Color.WHITE);
        menuReproductivo.setFont(fuenteMenu);
        menuReproductivo.setIcon(new ImageIcon(getClass().getResource("/imagenes/cria.png")));
        menuReproductivo.setIconTextGap(10);
        menuReproductivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        agregarHoverMenu(menuReproductivo, colorBarra, colorHover);

        JMenuItem bancogenético = new JMenuItem("Banco Genético");
        JMenuItem gestacion = new JMenuItem("Gestacion");
        JMenuItem historialreproductivo = new JMenuItem("Historial Reproductivo");
        JMenuItem registrarparto = new JMenuItem("Registrar Parto");
        JMenuItem registrarcria = new JMenuItem("Registrar Cria");

        bancogenético.setFont(fuenteItem);
        registrarparto.setFont(fuenteItem);
        registrarcria.setFont(fuenteItem);
        gestacion.setFont(fuenteItem);
        historialreproductivo.setFont(fuenteItem);

        bancogenético.setIcon(new ImageIcon(getClass().getResource("/imagenes/genetica.png")));
        agregarHoverItem(bancogenético, colorHover);

        registrarparto.setIcon(new ImageIcon(getClass().getResource("/imagenes/vacas.png")));
        agregarHoverItem(registrarparto, colorHover);

        registrarcria.setIcon(new ImageIcon(getClass().getResource("/imagenes/vacabb.png")));
        agregarHoverItem(registrarcria, colorHover);

        gestacion.setIcon(new ImageIcon(getClass().getResource("/imagenes/adn.png")));
        agregarHoverItem(gestacion, colorHover);

        historialreproductivo.setIcon(new ImageIcon(getClass().getResource("/imagenes/adn.png")));
        agregarHoverItem(historialreproductivo, colorHover);

        menuReproductivo.add(bancogenético);
        menuReproductivo.add(gestacion);
        menuReproductivo.add(historialreproductivo);
        menuReproductivo.addSeparator();
        menuReproductivo.add(registrarparto);
        menuReproductivo.add(registrarcria);

        //registro de banco genetico 
        bancogenético.addActionListener(e -> abrirMDI(new mdiBancoGenetico()));

        //registro de partos
        registrarparto.addActionListener(e -> abrirMDI(new mdiRegistroParto()));

        //formularios reproductivos 
        gestacion.addActionListener(e -> abrirMDI(new mdiGestacion()));

        //formularios reproductivos 
        registrarcria.addActionListener(e -> abrirMDI(new mdiRegistrarCria()));

        // ============================
        // 🔹 MENÚ PRODUCCIÓN
        // ============================
        JMenu menuProduccion = new JMenu("Producción");
        menuProduccion.setForeground(Color.WHITE);
        menuProduccion.setFont(fuenteMenu);
        menuProduccion.setIcon(new ImageIcon(getClass().getResource("/imagenes/produccion.png")));
        menuProduccion.setIconTextGap(10);
        menuProduccion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenuItem registrar_leche = new JMenuItem("Leche Diaria ");

        registrar_leche.setFont(fuenteItem);

        registrar_leche.setIcon(new ImageIcon(getClass().getResource("/imagenes/lechediaria.png")));

        agregarHoverItem(registrar_leche, colorItemHover);

        menuProduccion.add(registrar_leche);

        //registro de leche diaria 
        registrar_leche.addActionListener(e -> abrirMDI(new mdilechediaria()));

        JMenuItem leche = new JMenuItem("Asistente Virtual");

        leche.setFont(fuenteItem);

        leche.setIcon(new ImageIcon(getClass().getResource("/imagenes/ia.png")));

        agregarHoverItem(leche, colorItemHover);

        menuProduccion.add(leche);

        agregarHoverMenu(menuProduccion, colorBarra, colorHover);

        //modulo con IA
        leche.addActionListener(e -> {
            try {
                abrirMDI(new MDIRecomendaciones());
            } catch (java.beans.PropertyVetoException ex) {
                ex.printStackTrace();
            }
        });

        JMenuItem VentaAnimal = new JMenuItem("Venta de Ganado");

        VentaAnimal.setFont(fuenteItem);

        VentaAnimal.setIcon(new ImageIcon(getClass().getResource("/imagenes/mano.png")));

        agregarHoverItem(VentaAnimal, colorItemHover);

        menuProduccion.add(VentaAnimal);

        agregarHoverMenu(menuProduccion, colorBarra, colorHover);

        //registro de venta de animales 
        VentaAnimal.addActionListener(e -> abrirMDI(new mdiVentaAnimal()));

        JMenuItem VentaLeche = new JMenuItem("Venta de Leche");
        VentaLeche.setFont(fuenteItem);

        VentaLeche.setIcon(new ImageIcon(getClass().getResource("/imagenes/mano.png")));

        agregarHoverItem(VentaLeche, colorItemHover);

        menuProduccion.add(VentaLeche);

        agregarHoverMenu(menuProduccion, colorBarra, colorHover);

        //registro de venta de leche semanal y mensual
        VentaLeche.addActionListener(e -> abrirMDI(new mdiVentaLeche()));

        // ============================
        // 🔹 MENÚ ESTADÍSTICAS
        // ============================
        JMenu menuEstadisticas = new JMenu("Estadísticas");
        menuEstadisticas.setForeground(Color.WHITE);
        menuEstadisticas.setFont(fuenteMenu);
        menuEstadisticas.setIcon(new ImageIcon(getClass().getResource("/imagenes/stats.png")));
        menuEstadisticas.setIconTextGap(10);
        menuEstadisticas.setCursor(new Cursor(Cursor.HAND_CURSOR));

        agregarHoverMenu(menuEstadisticas, colorBarra, colorHover);

        // ============================
        // 🔹 MENÚ INVENTARIO
        // ============================
        JMenu menuInventario = new JMenu("Insumos");
        menuInventario.setForeground(Color.WHITE);
        menuInventario.setFont(fuenteMenu);
        menuInventario.setIcon(new ImageIcon(getClass().getResource("/imagenes/inventario.png")));
        menuInventario.setIconTextGap(10);
        menuInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JMenuItem insumos = new JMenuItem("Registrar inventario");

        insumos.setFont(fuenteItem);

        insumos.setIcon(new ImageIcon(getClass().getResource("/imagenes/lista.png")));

        agregarHoverItem(insumos, colorItemHover);

        menuInventario.add(insumos);
        insumos.addActionListener(e -> abrirMDI(new mdiInsumos()));

        // ============================
        // 🔹 AGREGAR MENÚS IZQUIERDA
        // ============================
        barra.add(menuRegistro);
        barra.add(Box.createHorizontalStrut(20));
        barra.add(menuReproductivo);
        barra.add(Box.createHorizontalStrut(20));
        barra.add(menuProduccion);
        barra.add(Box.createHorizontalStrut(20));
        barra.add(menuEstadisticas);
        barra.add(menuInventario);
        barra.add(Box.createHorizontalStrut(20));

        // 🔥 EMPUJA TODO LO SIGUIENTE A LA DERECHA
        barra.add(Box.createHorizontalGlue());

        // ============================
        // 👤 MENÚ CUENTA (derecha)
        // ============================
        JMenu menuCuenta = new JMenu("");
        menuCuenta.setIcon(new ImageIcon(getClass().getResource("/imagenes/usuario.png")));
        menuCuenta.setForeground(Color.WHITE);
        menuCuenta.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuCuenta.setToolTipText("Cuenta");
        menuCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuCuenta.setToolTipText("Cuenta");

        JMenuItem itemCambiarUsuario = new JMenuItem("Cambiar usuario");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemCambiarUsuario.setIcon(new ImageIcon(getClass().getResource("/imagenes/cambiar-usuario.png")));
        itemSalir.setIcon(new ImageIcon(getClass().getResource("/imagenes/salir.png")));

        itemCambiarUsuario.setFont(fuenteItem);
        itemSalir.setFont(fuenteItem);

        itemSalir.setForeground(new Color(183, 28, 28)); // Rojo para salir

        agregarHoverItem(itemCambiarUsuario, colorItemHover);
        agregarHoverItem(itemSalir, new Color(255, 205, 210)); // Hover rojizo para salir

        itemCambiarUsuario.addActionListener(e -> {
            // Reemplaza "Login" con el nombre de tu ventana de inicio de sesión
            new fmrLoginnuevo().setVisible(true);
            this.dispose();
        });

        itemSalir.addActionListener(e -> {
            // Reemplaza "Login" con el nombre de tu ventana de inicio de sesión
            new fmrLoginnuevo().setVisible(true);
            this.dispose();
        });

        menuCuenta.add(itemCambiarUsuario);
        menuCuenta.addSeparator();
        menuCuenta.add(itemSalir);

        agregarHoverMenu(menuCuenta, colorBarra, colorHover);

// ============================
// ✖ BOTÓN CERRAR (X rojo)
// ============================
        JButton btnCerrar = new JButton("");
        btnCerrar.setIcon(new ImageIcon(getClass().getResource("/imagenes/cerrar-sesion.png")));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(colorBarra);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setOpaque(true);
        btnCerrar.setToolTipText("Cerrar aplicación");

        btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCerrar.setBackground(new Color(183, 28, 28));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCerrar.setBackground(colorBarra);
            }
        });

        btnCerrar.addActionListener(e -> System.exit(0));

// Hover rojo al pasar el mouse
        btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCerrar.setBackground(new Color(183, 28, 28));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCerrar.setBackground(colorBarra);
            }
        });

// ============================
// 🌐 BOTÓN SITIO WEB
// ============================
        JButton btnWeb = new JButton("");
        btnWeb.setIcon(new ImageIcon(getClass().getResource("/imagenes/www.png")));
        btnWeb.setForeground(Color.WHITE);
        btnWeb.setBackground(colorBarra);
        btnWeb.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnWeb.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        btnWeb.setFocusPainted(false);
        btnWeb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnWeb.setOpaque(true);
        btnWeb.setToolTipText("Visitar sitio web");
        btnWeb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnWeb.setBackground(colorHover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btnWeb.setBackground(colorBarra);
            }
        });
        btnWeb.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://pagina-hinord.vercel.app/")); // cambia la URL
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No se pudo abrir el navegador.");
            }
        });

        btnCerrar.addActionListener(e -> System.exit(0));

        barra.add(menuCuenta);
        barra.add(Box.createHorizontalStrut(5));
        barra.add(btnWeb);
        barra.add(Box.createHorizontalStrut(5));
        barra.add(btnCerrar);

// 🔥 MENÚ ACTIVO
        menuRegistro.setBackground(colorActivo);
        menuRegistro.setOpaque(true);

        setJMenuBar(barra);

// ============================
// 🖱️ MOVER VENTANA CON EL MOUSE
// (ya que no hay barra de título)
// ============================
        final int[] mousePos = {0, 0};

        barra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                mousePos[0] = e.getX();
                mousePos[1] = e.getY();
            }
        });

        barra.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                setLocation(getX() + e.getX() - mousePos[0],
                        getY() + e.getY() - mousePos[1]);
            }
        });
    }

    private void agregarHoverMenu(JMenu menu, Color normal, Color hover) {
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu.setBackground(hover);
                menu.setOpaque(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu.setBackground(normal);
            }
        });
    }

    private void agregarHoverItem(JMenuItem item, Color hover) {
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(hover);
                item.setOpaque(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Color.WHITE);
            }
        });
    }

    private JPanel crearCard(String titulo, String valor) {

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                // 🔥 FONDO NEGRO TRANSPARENTE REAL
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            }
        };

        card.setOpaque(false); // 🔥 CLAVE
        card.setPreferredSize(new Dimension(180, 120));
        card.setLayout(new BorderLayout());

        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        JLabel v = new JLabel(valor, SwingConstants.CENTER);

        t.setForeground(Color.LIGHT_GRAY);
        v.setForeground(Color.WHITE);

        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        v.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);

        return card;

    }

    private int cargarKPIs() throws SQLException {

        int total = 0;

        try (Connection cn = conexion.conectar()) {
            String sql = "SELECT COUNT(*) AS total FROM registro";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        }

        return total;

    }

    private void cargarDashboard() {
        try {
            int totalRegistros = cargarKPIs();
            int totalVacunas = cargarvacunas();

            // Panel transparente que flotará encima del JDesktopPane
            JPanel panelCards = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            panelCards.setOpaque(false);

            panelCards.add(crearCard("Ganado Registrado", String.valueOf(totalRegistros)));
            panelCards.add(crearCard("Ganado Vacunado", String.valueOf(totalVacunas)));

            // 📐 Calcula el centro dinámicamente
            int anchoPanel = 440;  // ajusta si agregas más cards
            int altoPanel = 140;
            int frameAncho = getWidth();
            int xCentrado = (frameAncho - anchoPanel) / 2;

            panelCards.setBounds(xCentrado, 80, anchoPanel, altoPanel);

            // 🔑 Agréga al LayeredPane del JFrame, por ENCIMA del JDesktopPane
            escritorio.add(panelCards, JLayeredPane.DEFAULT_LAYER);
            escritorio.revalidate();
            escritorio.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int cargarvacunas() throws SQLException {

        int total = 0;

        try (Connection cn = conexion.conectar()) {
            String sql = "SELECT COUNT(*) AS total FROM prevenciones";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        }

        return total;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName(""); // NOI18N

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
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(frmnuevomenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmnuevomenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmnuevomenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmnuevomenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmnuevomenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
