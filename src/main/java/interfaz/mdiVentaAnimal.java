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

public class mdiVentaAnimal extends javax.swing.JInternalFrame {

    // ── Estado ────────────────────────────────────────────────────────────────
    private int idAnimalSeleccionado = 0;

    // ── Componentes ───────────────────────────────────────────────────────────
    private JPanel panel;
    private JScrollPane jScrollPane1;
    private JScrollPane scrollObs;

    private JComboBox<String> cmbAnimal;
    private JComboBox<String> cmbTipoVenta;
    private JComboBox<String> cmbDestino;
    private JComboBox<String> cmbCompradorTipo;
    private JComboBox<String> cmbModalidadCarne;

    private JTextField txtFechaVenta;
    private JTextField txtCompradorNombre;
    private JTextField txtCompradorTel;
    private JTextField txtCompradorNit;
    private JTextField txtPesoVivo;
    private JTextField txtPesoCanal;
    private JTextField txtPrecioPorKg;
    private JTextField txtPrecioTotal;
    private JTextField txtRendimiento;

    private JCheckBox chkTeniaCria;
    private JCheckBox chkCriaVendida;

    private JTextArea txtObservaciones;

    private JLabel lblInfoAnimal;
    private JLabel lblNumPartos;
    private JLabel lblRendimientoInfo;

    // Paneles secciones
    private JPanel panelCarne;
    private JPanel panelPie;

    private JTable tblVentas;
    private JButton btnNuevo, btnGuardar, btnEliminar;

    public mdiVentaAnimal() {
        setTitle("Venta de Animales");
        aplicarEstilo();
        cargarComboAnimales();
        cargarTablaVentas();

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
    // CARGAR COMBO ANIMALES
    // =========================================================================
    private void cargarComboAnimales() {
        for (java.awt.event.ActionListener al : cmbAnimal.getActionListeners()) {
            cmbAnimal.removeActionListener(al);
        }
        cmbAnimal.removeAllItems();
        cmbAnimal.addItem("-- Seleccione un animal --");

        String sql = "SELECT idregistro, nombre, IDICA, raza, clasificacion "
                + "FROM registro "
                + "WHERE estado_animal = 'Activo' "
                + "ORDER BY nombre";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cmbAnimal.addItem(
                        rs.getInt("idregistro")
                        + " | " + rs.getString("nombre")
                        + " | IDICA: " + rs.getString("IDICA")
                        + " | " + rs.getString("raza")
                        + " | " + rs.getString("clasificacion"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar animales: " + e.getMessage());
        }
        cmbAnimal.addActionListener(e -> seleccionarAnimal());
    }

    // =========================================================================
    // SELECCIONAR ANIMAL
    // =========================================================================
    private void seleccionarAnimal() {
        if (lblInfoAnimal == null) {
            return;
        }
        int idx = cmbAnimal.getSelectedIndex();
        if (idx <= 0) {
            lblInfoAnimal.setText("Animal: —");
            lblNumPartos.setText("Partos en la finca: —");
            idAnimalSeleccionado = 0;
            return;
        }
        String sel = cmbAnimal.getSelectedItem().toString();
        try {
            idAnimalSeleccionado = Integer.parseInt(sel.split("\\|")[0].trim());
        } catch (NumberFormatException ex) {
            return;
        }

        // Datos del animal
        String sql = "SELECT r.nombre, r.raza, r.edad, r.clasificacion, r.IDICA, "
                + "       COUNT(p.id_parto) AS num_partos "
                + "FROM registro r "
                + "LEFT JOIN gestacion g ON g.id_vaca = r.idregistro "
                + "LEFT JOIN partos_reproductivo p ON p.id_gestacion = g.id_gestacion "
                + "WHERE r.idregistro = ? "
                + "GROUP BY r.nombre, r.raza, r.edad, r.clasificacion, r.IDICA";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAnimalSeleccionado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblInfoAnimal.setText(
                        "Animal: " + rs.getString("nombre")
                        + "   IDICA: " + rs.getString("IDICA")
                        + "   Raza: " + rs.getString("raza")
                        + "   Edad: " + rs.getInt("edad") + " meses"
                        + "   Clasificación: " + rs.getString("clasificacion"));
                lblNumPartos.setText(
                        "Partos registrados en la finca: " + rs.getInt("num_partos"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar animal: " + e.getMessage());
        }
    }

    // =========================================================================
    // MOSTRAR/OCULTAR SECCIONES SEGÚN TIPO DE VENTA
    // =========================================================================
    private void actualizarSeccionTipoVenta() {
        String tipo = cmbTipoVenta.getSelectedItem().toString();
        boolean esCarne = tipo.equals("carne");
        panelCarne.setVisible(esCarne);
        panelPie.setVisible(!esCarne);
        if (esCarne) {
            cmbDestino.setSelectedItem("carne");
        }
        panel.revalidate();
        panel.repaint();
    }

    // =========================================================================
    // CALCULAR RENDIMIENTO CANAL AUTOMÁTICO
    // =========================================================================
    private void calcularRendimiento() {
        try {
            double vivo = txtPesoVivo.getText().trim().isEmpty()
                    ? 0 : Double.parseDouble(txtPesoVivo.getText().trim());
            double canal = txtPesoCanal.getText().trim().isEmpty()
                    ? 0 : Double.parseDouble(txtPesoCanal.getText().trim());
            if (vivo > 0 && canal > 0) {
                double rend = (canal / vivo) * 100;
                txtRendimiento.setText(String.format("%.2f", rend));
                lblRendimientoInfo.setText("Rendimiento canal: " + String.format("%.2f", rend) + "%");
            }
            // Calcular precio total si tiene precio por kg
            double precioKg = txtPrecioPorKg.getText().trim().isEmpty()
                    ? 0 : Double.parseDouble(txtPrecioPorKg.getText().trim());
            String modalidad = cmbModalidadCarne.getSelectedItem().toString();
            if (modalidad.equals("por_peso") && precioKg > 0) {
                double base = canal > 0 ? canal : vivo;
                txtPrecioTotal.setText(String.format("%.2f", base * precioKg));
            }
        } catch (NumberFormatException ignored) {
        }
    }

    // =========================================================================
    // GUARDAR VENTA
    // =========================================================================
    private void guardarVenta() {
        if (idAnimalSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un animal.");
            return;
        }
        if (txtCompradorNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del comprador.");
            txtCompradorNombre.requestFocus();
            return;
        }
        if (txtPrecioTotal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el precio total de venta.");
            txtPrecioTotal.requestFocus();
            return;
        }

        try {
            java.sql.Date fechaVenta = java.sql.Date.valueOf(
                    LocalDate.parse(txtFechaVenta.getText().trim()));

            String tipoVenta = cmbTipoVenta.getSelectedItem().toString();
            String destino = cmbDestino.getSelectedItem().toString();
            String compradorTipo = cmbCompradorTipo.getSelectedItem().toString();
            String compradorNom = txtCompradorNombre.getText().trim();
            String compradorTel = txtCompradorTel.getText().trim();
            String compradorNit = txtCompradorNit.getText().trim();
            double precioTotal = Double.parseDouble(txtPrecioTotal.getText().trim());
            boolean teniaCria = chkTeniaCria.isSelected();
            boolean criaVendida = chkCriaVendida.isSelected();
            String obs = txtObservaciones.getText().trim();

            // Campos de carne
            String modalidadCarne = null;
            Double pesoVivo = null, pesoCanal = null,
                    precioPorKg = null, rendimiento = null;

            if (tipoVenta.equals("carne")) {
                modalidadCarne = cmbModalidadCarne.getSelectedItem().toString();
                pesoVivo = txtPesoVivo.getText().trim().isEmpty()
                        ? null : Double.parseDouble(txtPesoVivo.getText().trim());
                pesoCanal = txtPesoCanal.getText().trim().isEmpty()
                        ? null : Double.parseDouble(txtPesoCanal.getText().trim());
                precioPorKg = txtPrecioPorKg.getText().trim().isEmpty()
                        ? null : Double.parseDouble(txtPrecioPorKg.getText().trim());
                rendimiento = txtRendimiento.getText().trim().isEmpty()
                        ? null : Double.parseDouble(txtRendimiento.getText().trim());
            }

            // Contar partos en finca
            int numPartos = 0;
            String sqlPartos = "SELECT COUNT(p.id_parto) AS total "
                    + "FROM partos_reproductivo p "
                    + "JOIN gestacion g ON p.id_gestacion = g.id_gestacion "
                    + "WHERE g.id_vaca = ?";
            try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sqlPartos)) {
                ps.setInt(1, idAnimalSeleccionado);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    numPartos = rs.getInt("total");
                }
            }

            // Confirmar
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Confirmar venta?\n\n"
                    + "  Animal ID: " + idAnimalSeleccionado + "\n"
                    + "  Tipo:      " + tipoVenta + "\n"
                    + "  Destino:   " + destino + "\n"
                    + "  Comprador: " + compradorNom + "\n"
                    + "  Precio:  $ " + String.format("%,.2f", precioTotal) + "\n"
                    + "  Partos en finca: " + numPartos + "\n\n"
                    + "El animal cambiará a estado 'Vendido'.",
                    "Confirmar venta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // INSERT venta
            String sql = "INSERT INTO venta_animal ("
                    + "id_animal, fecha_venta, tipo_venta, destino, "
                    + "comprador_tipo, comprador_nombre, comprador_tel, comprador_nit, "
                    + "modalidad_carne, peso_vivo_kg, peso_canal_kg, precio_por_kg, "
                    + "precio_total, rendimiento_canal, "
                    + "tenia_cria, cria_vendida, num_partos_finca, observaciones) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAnimalSeleccionado);
                ps.setDate(2, fechaVenta);
                ps.setString(3, tipoVenta);
                ps.setString(4, destino);
                ps.setString(5, compradorTipo);
                ps.setString(6, compradorNom);
                ps.setString(7, compradorTel);
                ps.setString(8, compradorNit.isEmpty() ? null : compradorNit);
                ps.setString(9, modalidadCarne);
                if (pesoVivo != null) {
                    ps.setDouble(10, pesoVivo);
                } else {
                    ps.setNull(10, java.sql.Types.DECIMAL);
                }
                if (pesoCanal != null) {
                    ps.setDouble(11, pesoCanal);
                } else {
                    ps.setNull(11, java.sql.Types.DECIMAL);
                }
                if (precioPorKg != null) {
                    ps.setDouble(12, precioPorKg);
                } else {
                    ps.setNull(12, java.sql.Types.DECIMAL);
                }
                ps.setDouble(13, precioTotal);
                if (rendimiento != null) {
                    ps.setDouble(14, rendimiento);
                } else {
                    ps.setNull(14, java.sql.Types.DECIMAL);
                }
                ps.setBoolean(15, teniaCria);
                ps.setBoolean(16, criaVendida);
                ps.setInt(17, numPartos);
                ps.setString(18, obs);
                ps.executeUpdate();
            }

            // Cambiar estado a Vendido en registro
            String sqlUpd = "UPDATE registro SET estado_animal = 'Vendido' "
                    + "WHERE idregistro = ?";
            try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sqlUpd)) {
                ps.setInt(1, idAnimalSeleccionado);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                    "✔ Venta registrada correctamente.\n"
                    + "  El animal ha sido marcado como Vendido.",
                    "Venta exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            cargarComboAnimales();
            cargarTablaVentas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // ELIMINAR VENTA
    // =========================================================================
    private void eliminarVenta() {
        int fila = tblVentas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una venta de la tabla para eliminar.");
            return;
        }
        int idVenta = Integer.parseInt(tblVentas.getValueAt(fila, 0).toString());
        int idAnimal = Integer.parseInt(tblVentas.getValueAt(fila, 1).toString());

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la venta ID " + idVenta + "?\n"
                + "El animal volverá a estado 'Activo'.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection con = conexion.conectar()) {
            // Eliminar venta
            PreparedStatement ps1 = con.prepareStatement(
                    "DELETE FROM venta_animal WHERE id_venta = ?");
            ps1.setInt(1, idVenta);
            ps1.executeUpdate();

            // Reactivar animal
            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE registro SET estado_animal = 'Activo' WHERE idregistro = ?");
            ps2.setInt(1, idAnimal);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Venta eliminada. Animal reactivado.");
            cargarComboAnimales();
            cargarTablaVentas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    // =========================================================================
    // CARGAR TABLA
    // =========================================================================
    private void cargarTablaVentas() {
        DefaultTableModel modelo = (DefaultTableModel) tblVentas.getModel();
        modelo.setRowCount(0);

        String sql = "SELECT v.id_venta, v.id_animal, r.nombre, r.IDICA, "
                + "       v.fecha_venta, v.tipo_venta, v.destino, "
                + "       v.comprador_nombre, v.comprador_tipo, "
                + "       v.precio_total, v.num_partos_finca, "
                + "       v.peso_vivo_kg, v.peso_canal_kg, v.rendimiento_canal "
                + "FROM venta_animal v "
                + "JOIN registro r ON v.id_animal = r.idregistro "
                + "ORDER BY v.fecha_venta DESC";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_venta"),
                    rs.getInt("id_animal"),
                    rs.getString("nombre"),
                    rs.getString("IDICA"),
                    rs.getDate("fecha_venta"),
                    rs.getString("tipo_venta"),
                    rs.getString("destino"),
                    rs.getString("comprador_nombre"),
                    rs.getString("comprador_tipo"),
                    rs.getDouble("precio_total"),
                    rs.getInt("num_partos_finca"),
                    rs.getObject("peso_vivo_kg"),
                    rs.getObject("peso_canal_kg"),
                    rs.getObject("rendimiento_canal")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar ventas: " + e.getMessage());
        }
    }

    // =========================================================================
    // LIMPIAR
    // =========================================================================
    private void limpiar() {
        cmbAnimal.setSelectedIndex(0);
        cmbTipoVenta.setSelectedIndex(0);
        cmbDestino.setSelectedIndex(0);
        cmbCompradorTipo.setSelectedIndex(0);
        cmbModalidadCarne.setSelectedIndex(0);
        txtFechaVenta.setText(LocalDate.now().toString());
        txtCompradorNombre.setText("");
        txtCompradorTel.setText("");
        txtCompradorNit.setText("");
        txtPesoVivo.setText("");
        txtPesoCanal.setText("");
        txtPrecioPorKg.setText("");
        txtPrecioTotal.setText("");
        txtRendimiento.setText("");
        txtObservaciones.setText("");
        chkTeniaCria.setSelected(false);
        chkCriaVendida.setSelected(false);
        lblInfoAnimal.setText("Animal: —");
        lblNumPartos.setText("Partos en la finca: —");
        lblRendimientoInfo.setText("");
        idAnimalSeleccionado = 0;
        tblVentas.clearSelection();
        actualizarSeccionTipoVenta();
    }

    // =========================================================================
    // APLICAR ESTILO
    // =========================================================================
    private void aplicarEstilo() {
        getContentPane().removeAll();
        getContentPane().setBackground(TemaFinca.VERDE_CLARO);
        getContentPane().setLayout(null);
        setSize(980, 850);

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
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                    g2.drawImage(img, (getWidth() - nw) / 2, (getHeight() - nh) / 2, nw, nh, this);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } catch (Exception ignored) {
                }
            }
        };
        header.setBackground(TemaFinca.VERDE_OSCURO);
        header.setBounds(0, 0, 980, 80);
        JLabel titulo = new JLabel("VENTA DE ANIMALES", SwingConstants.CENTER);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBounds(0, 18, 980, 44);
        header.add(titulo);
        add(header);

        panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        int x1 = 40, x2 = 470;
        int y = 18, gap = 72;

        // ── Animal ────────────────────────────────────────────────────────────
        lbl(panel, "SELECCIONAR ANIMAL", x1, y);
        cmbAnimal = combo(new String[]{});
        cmbAnimal.setBounds(x1, y + 20, 830, 30);
        panel.add(cmbAnimal);
        y += 55;

        lblInfoAnimal = new JLabel("Animal: —");
        lblInfoAnimal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfoAnimal.setForeground(TemaFinca.VERDE_OSCURO);
        lblInfoAnimal.setBounds(x1, y, 830, 20);
        panel.add(lblInfoAnimal);
        y += 22;

        lblNumPartos = new JLabel("Partos en la finca: —");
        lblNumPartos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNumPartos.setForeground(TemaFinca.GRIS_TEXTO);
        lblNumPartos.setBounds(x1, y, 830, 18);
        panel.add(lblNumPartos);
        y += 28;

        sep(panel, x1, y);
        y += 15;

        // ── Fecha + Tipo venta ────────────────────────────────────────────────
        lbl(panel, "FECHA DE VENTA", x1, y);
        txtFechaVenta = campo(x1, y + 20, 350);
        txtFechaVenta.setText(LocalDate.now().toString());
        panel.add(txtFechaVenta);

        lbl(panel, "TIPO DE VENTA", x2, y);
        cmbTipoVenta = combo(new String[]{"en_pie", "carne"});
        setBounds(cmbTipoVenta, x2, y + 20, 350);
        cmbTipoVenta.addActionListener(e -> actualizarSeccionTipoVenta());
        y += gap;

        // ── Destino + Comprador tipo ──────────────────────────────────────────
        lbl(panel, "DESTINO", x1, y);
        cmbDestino = combo(new String[]{
            "levante", "lecheria", "pie_de_cria", "carne", "otro"});
        setBounds(cmbDestino, x1, y + 20, 350);

        lbl(panel, "TIPO DE COMPRADOR", x2, y);
        cmbCompradorTipo = combo(new String[]{"persona", "empresa"});
        setBounds(cmbCompradorTipo, x2, y + 20, 350);
        y += gap;

        // ── Comprador nombre + tel ────────────────────────────────────────────
        lbl(panel, "NOMBRE COMPRADOR / EMPRESA", x1, y);
        txtCompradorNombre = campo(x1, y + 20, 350);
        panel.add(txtCompradorNombre);

        lbl(panel, "TELÉFONO", x2, y);
        txtCompradorTel = campo(x2, y + 20, 350);
        panel.add(txtCompradorTel);
        y += gap;

        // ── NIT + Precio total ────────────────────────────────────────────────
        lbl(panel, "NIT / CÉDULA (opcional)", x1, y);
        txtCompradorNit = campo(x1, y + 20, 350);
        panel.add(txtCompradorNit);

        lbl(panel, "PRECIO TOTAL DE VENTA ($)", x2, y);
        txtPrecioTotal = campo(x2, y + 20, 350);
        panel.add(txtPrecioTotal);
        y += gap;

        // ── Tenía cría ────────────────────────────────────────────────────────
        chkTeniaCria = new JCheckBox("¿El animal tenía cría al momento de la venta?");
        chkTeniaCria.setFont(TemaFinca.FUENTE_INPUT);
        chkTeniaCria.setBackground(Color.WHITE);
        chkTeniaCria.setBounds(x1, y, 450, 28);
        panel.add(chkTeniaCria);

        chkCriaVendida = new JCheckBox("¿La cría también fue vendida?");
        chkCriaVendida.setFont(TemaFinca.FUENTE_INPUT);
        chkCriaVendida.setBackground(Color.WHITE);
        chkCriaVendida.setBounds(x2, y, 350, 28);
        panel.add(chkCriaVendida);
        y += 40;

        sep(panel, x1, y);
        y += 10;

        // ── SECCIÓN CARNE ─────────────────────────────────────────────────────
        panelCarne = new JPanel(null);
        panelCarne.setBackground(new Color(255, 248, 230));
        panelCarne.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TemaFinca.DORADO, 1),
                "🥩 DATOS PARA VENTA EN CARNE",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                TemaFinca.VERDE_OSCURO));
        panelCarne.setBounds(x1, y, 830, 200);
        panel.add(panelCarne);

        int px = 20, py = 25, pgap = 60;

        // Modalidad
        JLabel lm = new JLabel("MODALIDAD");
        lm.setFont(TemaFinca.FUENTE_LABEL);
        lm.setForeground(TemaFinca.GRIS_TEXTO);
        lm.setBounds(px, py, 200, 20);
        panelCarne.add(lm);
        cmbModalidadCarne = combo(new String[]{"por_peso", "precio_fijo"});
        cmbModalidadCarne.setBounds(px, py + 20, 350, 30);
        cmbModalidadCarne.addActionListener(e -> calcularRendimiento());
        panelCarne.add(cmbModalidadCarne);

        // Peso vivo
        JLabel lpv = new JLabel("PESO VIVO (kg)");
        lpv.setFont(TemaFinca.FUENTE_LABEL);
        lpv.setForeground(TemaFinca.GRIS_TEXTO);
        lpv.setBounds(px, py + pgap, 200, 20);
        panelCarne.add(lpv);
        txtPesoVivo = campo(px, py + pgap + 20, 180);
        txtPesoVivo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }
        });
        panelCarne.add(txtPesoVivo);

        // Peso canal
        JLabel lpc = new JLabel("PESO CANAL (kg)");
        lpc.setFont(TemaFinca.FUENTE_LABEL);
        lpc.setForeground(TemaFinca.GRIS_TEXTO);
        lpc.setBounds(px + 200, py + pgap, 200, 20);
        panelCarne.add(lpc);
        txtPesoCanal = campo(px + 200, py + pgap + 20, 180);
        txtPesoCanal.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }
        });
        panelCarne.add(txtPesoCanal);

        // Precio por kg
        JLabel lpk = new JLabel("PRECIO POR KG ($)");
        lpk.setFont(TemaFinca.FUENTE_LABEL);
        lpk.setForeground(TemaFinca.GRIS_TEXTO);
        lpk.setBounds(px + 410, py + pgap, 200, 20);
        panelCarne.add(lpk);
        txtPrecioPorKg = campo(px + 410, py + pgap + 20, 180);
        txtPrecioPorKg.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularRendimiento();
            }
        });
        panelCarne.add(txtPrecioPorKg);

        // Rendimiento
        JLabel lr = new JLabel("RENDIMIENTO CANAL (%)");
        lr.setFont(TemaFinca.FUENTE_LABEL);
        lr.setForeground(TemaFinca.GRIS_TEXTO);
        lr.setBounds(px, py + pgap * 2, 220, 20);
        panelCarne.add(lr);
        txtRendimiento = campo(px, py + pgap * 2 + 20, 180);
        txtRendimiento.setEditable(false);
        txtRendimiento.setBackground(new Color(245, 245, 245));
        panelCarne.add(txtRendimiento);

        lblRendimientoInfo = new JLabel("");
        lblRendimientoInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRendimientoInfo.setForeground(TemaFinca.VERDE_OSCURO);
        lblRendimientoInfo.setBounds(px + 200, py + pgap * 2 + 20, 400, 28);
        panelCarne.add(lblRendimientoInfo);

        panelCarne.setVisible(false);
        y += 210;

        // ── SECCIÓN EN PIE (placeholder) ──────────────────────────────────────
        panelPie = new JPanel(null);
        panelPie.setBackground(new Color(240, 248, 240));
        panelPie.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TemaFinca.VERDE_OSCURO, 1),
                "🐄 VENTA EN PIE",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                TemaFinca.VERDE_OSCURO));
        panelPie.setBounds(x1, y - 210, 830, 50);
        JLabel lblPie = new JLabel(
                "Complete los datos de comprador y precio total arriba.");
        lblPie.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblPie.setForeground(TemaFinca.GRIS_TEXTO);
        lblPie.setBounds(20, 20, 780, 20);
        panelPie.add(lblPie);
        panel.add(panelPie);

        // ── Observaciones ─────────────────────────────────────────────────────
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

        // ── Tabla ─────────────────────────────────────────────────────────────
        lbl(panel, "HISTORIAL DE VENTAS", x1, y);
        y += 20;

        tblVentas = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "ID Animal", "Animal", "IDICA", "Fecha",
                    "Tipo", "Destino", "Comprador", "Tipo Comp.",
                    "Precio $", "Partos", "Peso Vivo", "Peso Canal", "Rendimiento"}));
        tblVentas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblVentas.setRowHeight(26);
        tblVentas.setGridColor(new Color(240, 240, 240));
        tblVentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblVentas.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblVentas.getTableHeader().setForeground(Color.WHITE);
        tblVentas.getTableHeader().setReorderingAllowed(false);

        jScrollPane1 = new JScrollPane(tblVentas);
        jScrollPane1.setBounds(x1, y, 830, 150);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(jScrollPane1);
        y += 168;

        // ── Botones ───────────────────────────────────────────────────────────
        btnNuevo = new JButton("NUEVO");
        btnGuardar = new JButton("GUARDAR");
        btnEliminar = new JButton("ELIMINAR");

        try {
            btnNuevo.setIcon(new ImageIcon(getClass().getResource("/imagenes/buscar.png")));
            btnGuardar.setIcon(new ImageIcon(getClass().getResource("/imagenes/expediente.png")));
            btnEliminar.setIcon(new ImageIcon(getClass().getResource("/imagenes/papelera.png")));
        } catch (Exception ignored) {
        }

        int bW = 185, bH = 42, bGap = 18;
        int sx = (910 - (3 * bW + 2 * bGap)) / 2;

        btnNuevo.setBounds(sx, y, bW, bH);
        btnGuardar.setBounds(sx + bW + bGap, y, bW, bH);
        btnEliminar.setBounds(sx + 2 * (bW + bGap), y, bW, bH);

        EstiloFormularioBase.estilizarBoton(btnNuevo, TemaFinca.VERDE_OSCURO, null);
        EstiloFormularioBase.estilizarBoton(btnGuardar, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminar, new Color(180, 50, 50), null);

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardarVenta());
        btnEliminar.addActionListener(e -> eliminarVenta());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnEliminar);
        y += bH + 20;

        panel.setPreferredSize(new Dimension(910, y));
        JScrollPane scrollPrincipal = new JScrollPane(panel);
        scrollPrincipal.setBounds(35, 90, 910, 660);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPrincipal);

        revalidate();
        repaint();
    }

    // ── Helpers UI ────────────────────────────────────────────────────────────
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
