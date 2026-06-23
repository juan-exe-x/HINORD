package interfaz;

import clases.conexion;
import estilos.EstiloFormularioBase;
import estilos.TemaFinca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class mdilechediaria extends javax.swing.JInternalFrame {

    // ── Estado ────────────────────────────────────────────────────────────────
    private int idRegistroSeleccionado = 0;

    // ── Componentes ───────────────────────────────────────────────────────────
    private JPanel panel;
    private JScrollPane jScrollPane1;
    private JScrollPane scrollPrincipal;

    private JTextField txtIdRegistro;
    private JTextField txtNombre;
    private JTextField txtRaza;
    private JTextField txtEdad;
    private JTextField txtFecha;
    private JTextField txtLitros;
    private JTextArea txtObservaciones;
    private JScrollPane scrollObs;
    private JComboBox<String> cmbAnimal;
    private JComboBox<String> cmbTurno;

    private JLabel lblInfoAnimal;

    private JTable tblProduccion;

    private JButton btnNuevo, btnGuardar, btnEliminar;

    public mdilechediaria() {
        setTitle("Producción de Leche Diaria");
        aplicarEstilo();
        cargarComboAnimales();
        cargarTablaProduccion();

        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    // =========================================================================
    // BUSCAR ANIMAL
    // =========================================================================
    private void cargarComboAnimales() {
        // Primero quitar listeners anteriores para evitar duplicados
        for (java.awt.event.ActionListener al : cmbAnimal.getActionListeners()) {
            cmbAnimal.removeActionListener(al);
        }

        cmbAnimal.removeAllItems();
        cmbAnimal.addItem("-- Seleccione un animal --");

        String sql = "SELECT idregistro, nombre, IDICA, raza, edad "
                + "FROM registro "
                + "WHERE estado_animal = 'Activo' "
                + "ORDER BY nombre";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cmbAnimal.addItem(
                        rs.getInt("idregistro")
                        + " | " + rs.getString("nombre")
                        + " | IDICA: " + rs.getString("IDICA")
                        + " | Raza: " + rs.getString("raza")
                        + " | Edad: " + rs.getInt("edad") + " meses");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar animales: " + e.getMessage());
        }

        // ── Agregar listener AL FINAL, después de cargar los items ───────────
        cmbAnimal.addActionListener(e -> seleccionarAnimal());
    }

    private void seleccionarAnimal() {

        // Guard: si el label aún no fue creado, ignorar
        if (lblInfoAnimal == null) {
            return;
        }

        int idx = cmbAnimal.getSelectedIndex();
        if (idx <= 0) {
            lblInfoAnimal.setText("Animal: —");
            txtIdRegistro.setText("");
            txtNombre.setText("");
            txtRaza.setText("");
            txtEdad.setText("");
            idRegistroSeleccionado = 0;
            return;
        }
        String sel = cmbAnimal.getSelectedItem().toString();
        try {
            idRegistroSeleccionado = Integer.parseInt(sel.split("\\|")[0].trim());
        } catch (NumberFormatException ex) {
            return;
        }

        String sql = "SELECT idregistro, nombre, raza, edad "
                + "FROM registro WHERE idregistro = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRegistroSeleccionado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtIdRegistro.setText(String.valueOf(rs.getInt("idregistro")));
                txtNombre.setText(rs.getString("nombre"));
                txtRaza.setText(rs.getString("raza"));
                txtEdad.setText(rs.getInt("edad") + " meses");
                lblInfoAnimal.setText("Animal: " + rs.getString("nombre")
                        + "   |   Raza: " + rs.getString("raza"));
                cargarTablaProduccion();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar animal: " + e.getMessage());
        }
    }

    // =========================================================================
    // CARGAR TABLA HISTORIAL
    // =========================================================================
    private void cargarTablaProduccion() {
        DefaultTableModel modelo = (DefaultTableModel) tblProduccion.getModel();
        modelo.setRowCount(0);

        String sql = "SELECT pl.id_produccion, r.nombre, pl.fecha, pl.turno, "
                + "       pl.litros, pl.observaciones, "
                + "       SUM(pl2.litros) AS total_dia "
                + "FROM produccion_leche pl "
                + "JOIN registro r ON pl.idregistro = r.idregistro "
                + "JOIN produccion_leche pl2 "
                + "     ON pl2.idregistro = pl.idregistro "
                + "     AND pl2.fecha = pl.fecha "
                + "WHERE 1=1 "
                + (idRegistroSeleccionado > 0
                        ? "AND pl.idregistro = " + idRegistroSeleccionado + " " : "")
                + "GROUP BY pl.id_produccion, r.nombre, pl.fecha, pl.turno, "
                + "         pl.litros, pl.observaciones "
                + "ORDER BY pl.fecha DESC, pl.turno";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_produccion"),
                    rs.getString("nombre"),
                    rs.getDate("fecha"),
                    rs.getString("turno"),
                    rs.getDouble("litros"),
                    rs.getDouble("total_dia"),
                    rs.getString("observaciones")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar producción: " + e.getMessage());
        }
    }

    // =========================================================================
    // GUARDAR REGISTRO DE LECHE
    // =========================================================================
    private void guardarProduccion() {
        if (idRegistroSeleccionado == 0) {
            JOptionPane.showMessageDialog(this,
                    "Primero busque y seleccione un animal.");
            return;
        }
        if (txtLitros.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese los litros producidos.");
            txtLitros.requestFocus();
            return;
        }

        try {
            double litros = Double.parseDouble(txtLitros.getText().trim());
            if (litros <= 0) {
                JOptionPane.showMessageDialog(this, "Los litros deben ser mayor a 0.");
                return;
            }

            java.sql.Date fecha = java.sql.Date.valueOf(
                    LocalDate.parse(txtFecha.getText().trim()));

            String turnoNuevo = cmbTurno.getSelectedItem().toString();

            // ── Buscar si ya existe registro para esta vaca en esta fecha ─────────
            String sqlCheck = "SELECT id_produccion, litros, turno "
                    + "FROM produccion_leche "
                    + "WHERE idregistro = ? AND fecha = ?";

            try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sqlCheck)) {

                ps.setInt(1, idRegistroSeleccionado);
                ps.setDate(2, fecha);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // ── Ya existe registro ese día → ACUMULAR ───────────────────
                    int idExistente = rs.getInt("id_produccion");
                    double litrosActual = rs.getDouble("litros");
                    String turnoActual = rs.getString("turno");

                    // Agregar turno solo si no está ya registrado
                    String turnoFinal;
                    if (turnoActual.contains(turnoNuevo)) {
                        turnoFinal = turnoActual;   // ya estaba, no duplicar
                    } else {
                        turnoFinal = turnoActual + ", " + turnoNuevo;
                    }

                    double litrosFinal = litrosActual + litros;

                    String sqlUpd = "UPDATE produccion_leche "
                            + "SET litros = ?, turno = ?, observaciones = ? "
                            + "WHERE id_produccion = ?";

                    try (Connection con2 = conexion.conectar(); PreparedStatement ps2 = con2.prepareStatement(sqlUpd)) {

                        ps2.setDouble(1, litrosFinal);
                        ps2.setString(2, turnoFinal);
                        ps2.setString(3, txtObservaciones.getText().trim());
                        ps2.setInt(4, idExistente);
                        ps2.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(this,
                            "✔ Ordeño acumulado al registro del día.\n"
                            + "  Animal : " + txtNombre.getText() + "\n"
                            + "  Fecha  : " + fecha + "\n"
                            + "  Turnos : " + turnoFinal + "\n"
                            + "  Total  : " + litrosFinal + " litros");

                } else {
                    // ── No existe → INSERTAR nuevo registro ─────────────────────
                    String sqlIns = "INSERT INTO produccion_leche "
                            + "(idregistro, fecha, litros, turno, observaciones) "
                            + "VALUES (?, ?, ?, ?, ?)";

                    try (Connection con2 = conexion.conectar(); PreparedStatement ps2 = con2.prepareStatement(sqlIns)) {

                        ps2.setInt(1, idRegistroSeleccionado);
                        ps2.setDate(2, fecha);
                        ps2.setDouble(3, litros);
                        ps2.setString(4, turnoNuevo);
                        ps2.setString(5, txtObservaciones.getText().trim());
                        ps2.executeUpdate();
                    }

                    JOptionPane.showMessageDialog(this,
                            "✔ Producción registrada.\n"
                            + "  Animal : " + txtNombre.getText() + "\n"
                            + "  Fecha  : " + fecha + "\n"
                            + "  Turno  : " + turnoNuevo + "\n"
                            + "  Litros : " + litros);
                }
            }

            cargarTablaProduccion();
            limpiarCamposProduccion();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // ELIMINAR REGISTRO SELECCIONADO EN TABLA
    // =========================================================================
    private void eliminarProduccion() {
        int fila = tblProduccion.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un registro de la tabla para eliminar.");
            return;
        }
        int idProd = Integer.parseInt(tblProduccion.getValueAt(fila, 0).toString());
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el registro ID " + idProd + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM produccion_leche WHERE id_produccion = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProd);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Registro eliminado.");
                cargarTablaProduccion();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    // =========================================================================
    // LIMPIAR SOLO CAMPOS DE PRODUCCIÓN (mantiene el animal seleccionado)
    // =========================================================================
    private void limpiarCamposProduccion() {
        txtFecha.setText(LocalDate.now().toString());
        txtLitros.setText("");
        txtObservaciones.setText("");
        cmbTurno.setSelectedIndex(0);
    }

    // =========================================================================
    // LIMPIAR TODO
    // =========================================================================
    private void limpiarTodo() {
        idRegistroSeleccionado = 0;

        txtIdRegistro.setText("");
        txtNombre.setText("");
        txtRaza.setText("");
        txtEdad.setText("");
        lblInfoAnimal.setText("Animal: —");
        limpiarCamposProduccion();
        tblProduccion.clearSelection();
        cargarTablaProduccion();
    }

    // =========================================================================
    // APLICAR ESTILO — construye TODO el UI
    // =========================================================================
    private void aplicarEstilo() {
        getContentPane().removeAll();
        getContentPane().setBackground(TemaFinca.VERDE_CLARO);
        getContentPane().setLayout(null);
        setSize(980, 800);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon(
                            getClass().getResource("/Imagenes/vaca (4).png")).getImage();
                    Graphics2D g2 = (Graphics2D) g;
                    int iw = img.getWidth(this), ih = img.getHeight(this);
                    double e = Math.min((double) getWidth() / iw, (double) getHeight() / ih);
                    int nw = (int) (iw * e), nh = (int) (ih * e);
                    g2.setComposite(AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER, 0.12f));
                    g2.drawImage(img, (getWidth() - nw) / 2, (getHeight() - nh) / 2, nw, nh, this);
                    g2.setComposite(AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER, 1f));
                } catch (Exception ignored) {
                }
            }
        };
        header.setBackground(TemaFinca.VERDE_OSCURO);
        header.setBounds(0, 0, 980, 80);
        JLabel titulo = new JLabel("PRODUCCIÓN DE LECHE DIARIA", SwingConstants.CENTER);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBounds(0, 18, 980, 44);
        header.add(titulo);
        add(header);

        // ── Panel contenido ───────────────────────────────────────────────────
        panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        int x1 = 40, x2 = 470;
        int y = 18, gap = 72;

        // ── BLOQUE 1: Buscador ────────────────────────────────────────────────
        // ── BLOQUE 1: Seleccionar animal ──────────────────────────────────────────
        lbl(panel, "SELECCIONAR ANIMAL", x1, y);
        cmbAnimal = combo(new String[]{});
        cmbAnimal.setBounds(x1, y + 20, 830, 30);
        panel.add(cmbAnimal);
        y += 58;

        // ── AGREGAR ESTA LÍNEA QUE FALTA ─────────────────────────────────────────
        lblInfoAnimal = new JLabel("Animal: —");
        lblInfoAnimal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfoAnimal.setForeground(TemaFinca.VERDE_OSCURO);
        lblInfoAnimal.setBounds(x1, y, 830, 20);
        panel.add(lblInfoAnimal);
        y += 28;

        sep(panel, x1, y);
        y += 15;

        // ── BLOQUE 2: Datos del animal (solo lectura) ─────────────────────────
        lbl(panel, "ID REGISTRO", x1, y);
        txtIdRegistro = campo(x1, y + 20, 160);
        txtIdRegistro.setEditable(false);
        txtIdRegistro.setBackground(new Color(245, 245, 245));
        panel.add(txtIdRegistro);

        lbl(panel, "NOMBRE", x1 + 180, y);
        txtNombre = campo(x1 + 180, y + 20, 250);
        txtNombre.setEditable(false);
        txtNombre.setBackground(new Color(245, 245, 245));
        panel.add(txtNombre);

        lbl(panel, "RAZA", x1 + 450, y);
        txtRaza = campo(x1 + 450, y + 20, 200);
        txtRaza.setEditable(false);
        txtRaza.setBackground(new Color(245, 245, 245));
        panel.add(txtRaza);

        lbl(panel, "EDAD", x1 + 670, y);
        txtEdad = campo(x1 + 670, y + 20, 160);
        txtEdad.setEditable(false);
        txtEdad.setBackground(new Color(245, 245, 245));
        panel.add(txtEdad);
        y += gap;

        sep(panel, x1, y);
        y += 15;

        // ── BLOQUE 3: Registro de producción ─────────────────────────────────
        // DESPUÉS — fecha ocupa columna izquierda, turno columna derecha, botón Hoy debajo de fecha
        lbl(panel, "FECHA (AAAA-MM-DD)", x1, y);
        txtFecha = campo(x1, y + 20, 350);
        txtFecha.setText(LocalDate.now().toString());
        panel.add(txtFecha);

        lbl(panel, "TURNO", x2, y);
        cmbTurno = combo(new String[]{"mañana", "tarde", "noche"});
        setBounds(cmbTurno, x2, y + 20, 350);
        y += 56;

        // Botón Hoy en su propia fila debajo de la fecha
        JButton btnHoy = new JButton("📅 Restaurar fecha de hoy");
        btnHoy.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnHoy.setBounds(x1, y, 200, 26);
        EstiloFormularioBase.estilizarBoton(btnHoy, TemaFinca.VERDE_OSCURO, null);
        btnHoy.addActionListener(e -> txtFecha.setText(LocalDate.now().toString()));
        panel.add(btnHoy);
        y += 40;

        lbl(panel, "LITROS PRODUCIDOS", x1, y);
        txtLitros = campo(x1, y + 20, 350);
        panel.add(txtLitros);
        y += gap;

        lbl(panel, "OBSERVACIONES", x1, y);
        txtObservaciones = new JTextArea(3, 10);
        txtObservaciones.setFont(TemaFinca.FUENTE_INPUT);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setBounds(x1, y + 20, 830, 55);
        scrollObs.setBorder(TemaFinca.bordeCampo());
        panel.add(scrollObs);
        y += 82;

        sep(panel, x1, y);
        y += 15;

        // ── BLOQUE 4: Tabla historial ─────────────────────────────────────────
        lbl(panel, "HISTORIAL DE PRODUCCIÓN", x1, y);
        y += 20;

        tblProduccion = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Animal", "Fecha", "Turno",
                    "Litros", "Total Día", "Observaciones"}));
        tblProduccion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblProduccion.setRowHeight(26);
        tblProduccion.setGridColor(new Color(240, 240, 240));
        tblProduccion.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblProduccion.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblProduccion.getTableHeader().setForeground(Color.WHITE);
        tblProduccion.getTableHeader().setReorderingAllowed(false);

        // Ajustar ancho columnas
        tblProduccion.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblProduccion.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblProduccion.getColumnModel().getColumn(2).setPreferredWidth(90);
        tblProduccion.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblProduccion.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblProduccion.getColumnModel().getColumn(5).setPreferredWidth(80);
        tblProduccion.getColumnModel().getColumn(6).setPreferredWidth(200);

        jScrollPane1 = new JScrollPane(tblProduccion);
        jScrollPane1.setBounds(x1, y, 830, 150);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(jScrollPane1);
        y += 168;

        // ── Botones ───────────────────────────────────────────────────────────
        btnNuevo = new JButton("NUEVO");
        btnGuardar = new JButton("GUARDAR");
        btnEliminar = new JButton("ELIMINAR");

        try {
            btnNuevo.setIcon(new ImageIcon(
                    getClass().getResource("/imagenes/buscar.png")));
            btnGuardar.setIcon(new ImageIcon(
                    getClass().getResource("/imagenes/expediente.png")));
            btnEliminar.setIcon(new ImageIcon(
                    getClass().getResource("/imagenes/papelera.png")));
        } catch (Exception ignored) {
        }

        int bW = 185, bH = 42, bGap = 18;
        int totalW = 3 * bW + 2 * bGap;
        int sx = (910 - totalW) / 2;

        btnNuevo.setBounds(sx, y, bW, bH);
        btnGuardar.setBounds(sx + bW + bGap, y, bW, bH);
        btnEliminar.setBounds(sx + 2 * (bW + bGap), y, bW, bH);

        EstiloFormularioBase.estilizarBoton(btnNuevo, TemaFinca.VERDE_OSCURO, null);
        EstiloFormularioBase.estilizarBoton(btnGuardar, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminar, new Color(180, 50, 50), null);

        btnNuevo.addActionListener(e -> limpiarTodo());
        btnGuardar.addActionListener(e -> guardarProduccion());
        btnEliminar.addActionListener(e -> eliminarProduccion());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnEliminar);

        y += bH + 20;

        // ── Scroll principal ──────────────────────────────────────────────────
        panel.setPreferredSize(new Dimension(910, y));
        scrollPrincipal = new JScrollPane(panel);
        scrollPrincipal.setBounds(35, 90, 910, 620);
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
        l.setBounds(x, y, 420, 20);
        p.add(l);
    }

    private JTextField campo(int x, int y, int w) {
        JTextField t = new JTextField();
        t.setFont(TemaFinca.FUENTE_INPUT);
        t.setBorder(TemaFinca.bordeCampo());
        t.setBounds(x, y, w, 28);
        return t;
    }

    private JComboBox<String> combo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(TemaFinca.FUENTE_INPUT);
        c.setBackground(Color.WHITE);
        c.setForeground(new Color(50, 50, 50));
        return c;
    }

    private void setBounds(JComboBox<String> c, int x, int y, int w) {
        c.setBounds(x, y, w, 30);
        panel.add(c);
    }

    private void sep(JPanel p, int x, int y) {
        JSeparator s = new JSeparator();
        s.setBounds(x, y, 830, 2);
        s.setForeground(new Color(220, 220, 220));
        p.add(s);
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
