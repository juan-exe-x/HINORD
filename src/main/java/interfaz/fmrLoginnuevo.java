package interfaz;

import clases.PanelDegradado;
import clases.conexion;
import java.awt.*;
import java.net.URI;
import java.sql.*;
import javax.swing.*;

public class fmrLoginnuevo extends JFrame {

    Connection con = conexion.conectar();

    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    private JButton btnIngresar, btnCrear;

    private JLabel lblIcono, lblTitulo;

    public fmrLoginnuevo() {
        iniciarComponentes();
        diseñarLogin();
        eventos();
        CrearMenuBar();

        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setUndecorated(true);

        setVisible(true); // 👈
    }

    private void iniciarComponentes() {

        paneldefondo = new PanelDegradado();
        paneldefondo.setLayout(new GridBagLayout());

        PanelLogin = new JPanel();
        PanelLogin.setLayout(new BoxLayout(PanelLogin, BoxLayout.Y_AXIS));

        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();

        btnIngresar = new JButton("Ingresar");
        btnCrear = new JButton("Crear Cuenta");

        lblIcono = new JLabel();
        lblTitulo = new JLabel("Sistema Ganadero");

        lblUsuario = new JLabel("Usuario");
        lblPassword = new JLabel("Contraseña");

        setContentPane(paneldefondo);
    }

    private void diseñarLogin() {

        // 🔹 CENTRAR PANEL
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        paneldefondo.add(PanelLogin, gbc);

        // 🔹 ESTILO PANEL
        PanelLogin.setBackground(new Color(0, 0, 0, 150));
        PanelLogin.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        PanelLogin.setPreferredSize(new Dimension(350, 500));

        // 🔹 ICONO
        ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/user.png"));
        Image img = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        lblIcono.setIcon(new ImageIcon(img));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 🔹 TITULO
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 🔹 LABELS
        lblUsuario.setForeground(Color.WHITE);
        lblPassword.setForeground(Color.WHITE);

        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // 🔹 CONTENEDORES PARA ALINEAR IZQUIERDA
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelUsuario.setOpaque(false);
        panelUsuario.setMaximumSize(new Dimension(250, 20));
        panelUsuario.add(lblUsuario);

        JPanel panelPassword = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelPassword.setOpaque(false);
        panelPassword.setMaximumSize(new Dimension(250, 20));
        panelPassword.add(lblPassword);

        // 🔹 INPUTS
        txtUsuario.setMaximumSize(new Dimension(250, 35));
        txtPassword.setMaximumSize(new Dimension(250, 35));

        // 🔹 BOTONES
        btnIngresar.setMaximumSize(new Dimension(250, 40));
        btnCrear.setMaximumSize(new Dimension(250, 40));

        btnIngresar.setBackground(new Color(0, 153, 0));
        btnCrear.setBackground(new Color(0, 153, 0));

        btnIngresar.setFocusPainted(false);
        btnCrear.setFocusPainted(false);

        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 🔹 AGREGAR COMPONENTES
        PanelLogin.add(lblIcono);
        PanelLogin.add(Box.createRigidArea(new Dimension(0, 10)));

        PanelLogin.add(lblTitulo);
        PanelLogin.add(Box.createRigidArea(new Dimension(0, 20)));

        PanelLogin.add(panelUsuario);
        PanelLogin.add(Box.createRigidArea(new Dimension(0, 5)));

        PanelLogin.add(txtUsuario);
        PanelLogin.add(Box.createRigidArea(new Dimension(0, 15)));

        PanelLogin.add(panelPassword);
        PanelLogin.add(Box.createRigidArea(new Dimension(0, 5)));

        PanelLogin.add(txtPassword);
        PanelLogin.add(Box.createRigidArea(new Dimension(0, 20)));

        PanelLogin.add(btnIngresar);
        PanelLogin.add(Box.createRigidArea(new Dimension(0, 10)));

        PanelLogin.add(btnCrear);
    }

    private void eventos() {

        btnIngresar.addActionListener(e -> iniciarSesion());
        btnCrear.addActionListener(e -> crearCuenta());
    }

    private void iniciarSesion() {

        String user = txtUsuario.getText();
        String pass = new String(txtPassword.getPassword());

        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT idusuario FROM usuario WHERE user=? AND password=?");

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Bienvenido: " + user);

                this.dispose();

                frmnuevomenu m = new frmnuevomenu();
                m.setVisible(true);
                m.setLocationRelativeTo(null);

            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void crearCuenta() {

        String usuario = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa los campos");
            return;
        }

        try {

            String sql = "INSERT INTO usuario (user, password) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, password);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Cuenta creada");

            txtUsuario.setText("");
            txtPassword.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void CrearMenuBar() {
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
                Desktop.getDesktop().browse(new URI("http://127.0.0.1:5500/index.html")); // cambia la URL
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "No se pudo abrir el navegador.");
            }
        });

        barra.add(Box.createHorizontalGlue());

        barra.add(menuCuenta);
        barra.add(Box.createHorizontalStrut(5));
        barra.add(btnWeb);
        barra.add(Box.createHorizontalStrut(5));
        barra.add(btnCerrar);

        setJMenuBar(barra);

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paneldefondo = new clases.PanelDegradado();
        PanelLogin = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        paneldefondo.setForeground(new java.awt.Color(255, 255, 255));

        PanelLogin.setBackground(new java.awt.Color(153, 153, 153));
        PanelLogin.setForeground(new java.awt.Color(0, 0, 0));

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setForeground(new java.awt.Color(0, 51, 255));
        jTextField2.setText("Usuario");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jPasswordField1.setBackground(new java.awt.Color(255, 255, 255));
        jPasswordField1.setForeground(new java.awt.Color(0, 51, 255));
        jPasswordField1.setText("Contraseña");

        jButton1.setBackground(new java.awt.Color(0, 153, 0));
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Ingresar");

        jButton2.setBackground(new java.awt.Color(0, 153, 0));
        jButton2.setText("Crear Cuenta ");

        jLabel2.setText("jLabel1");

        lblUsuario.setText("jLabel1");

        lblPassword.setText("jLabel1");

        javax.swing.GroupLayout PanelLoginLayout = new javax.swing.GroupLayout(PanelLogin);
        PanelLogin.setLayout(PanelLoginLayout);
        PanelLoginLayout.setHorizontalGroup(
            PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
            .addGroup(PanelLoginLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelLoginLayout.createSequentialGroup()
                        .addGroup(PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(37, 37, 37))))
        );
        PanelLoginLayout.setVerticalGroup(
            PanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelLoginLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(lblUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );

        javax.swing.GroupLayout paneldefondoLayout = new javax.swing.GroupLayout(paneldefondo);
        paneldefondo.setLayout(paneldefondoLayout);
        paneldefondoLayout.setHorizontalGroup(
            paneldefondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldefondoLayout.createSequentialGroup()
                .addGap(294, 294, 294)
                .addComponent(PanelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(268, Short.MAX_VALUE))
        );
        paneldefondoLayout.setVerticalGroup(
            paneldefondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldefondoLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(PanelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(76, 76, 76))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneldefondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneldefondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

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
            java.util.logging.Logger.getLogger(fmrLoginnuevo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fmrLoginnuevo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fmrLoginnuevo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fmrLoginnuevo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fmrLoginnuevo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelLogin;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel paneldefondo;
    // End of variables declaration//GEN-END:variables
}
