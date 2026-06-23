package interfaz;

import clases.*;
import estilos.EstiloFormularioBase;
import estilos.TemaFinca;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class mdiInsumos extends javax.swing.JInternalFrame {
    // ── Catálogo ─────────────────────────────────────────────────────────────

    private JTextField txtNombreInsumo, txtUnidad, txtStockMinimo, txtStockActual, txtPrecioUnitario;
    private JComboBox<String> cmbCategoriaInsumo;
    private JTextArea txtDescripcionInsumo;
    private JScrollPane scrollDescripcion;
    private JTable tblCatalogo;
    private JScrollPane scrollCatalogo;
    private JButton btnNuevoCat, btnGuardarCat, btnActualizarCat, btnEliminarCat;

    // ── Compras ──────────────────────────────────────────────────────────────
    private JComboBox<String> cmbInsumoCompra;
    private JTextField txtCantidadCompra, txtPrecioCompra, txtProveedorCompra,
            txtFacturaCompra, txtFechaCompra,
            txtFiltroFechaDesde, txtFiltroFechaHasta;
    private JTextArea txtObsCompra;
    private JScrollPane scrollObsCompra;
    private JTable tblCompras;
    private JScrollPane scrollCompras;
    private JButton btnGuardarCompra, btnEliminarCompra, btnFiltrarCompras;

    // ── Usos ─────────────────────────────────────────────────────────────────
    private JComboBox<String> cmbInsumoUso;
    private JTextField txtCantidadUso, txtPrecioUso, txtFechaUso,
            txtLoteUso, txtMotivoUso,
            txtFiltroFechaUso, txtFiltroInsumoUso;
    private JTable tblUsos;
    private JScrollPane scrollUsos;
    private JButton btnGuardarUso, btnEliminarUso, btnFiltrarUsos;

    // ── Shared ───────────────────────────────────────────────────────────────
    private JTabbedPane tabbedPane;

    public mdiInsumos() {
        
        setTitle("Gestión de Insumos");
        setSize(980, 800);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        aplicarEstilo();
        cargarComboCategorias();
        cargarCombosInsumos();
        cargarCatalogo();
        cargarCompras(null, null);
        cargarUsos(0, null);
    }

    // =========================================================================
    // APLICAR ESTILO
    // =========================================================================
    private void aplicarEstilo() {
        getContentPane().removeAll();
        getContentPane().setBackground(TemaFinca.VERDE_CLARO);
        getContentPane().setLayout(null);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon(
                            getClass().getResource("/imagenes/inventario.png")).getImage();
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

        JLabel titulo = new JLabel("GESTIÓN DE INSUMOS", SwingConstants.CENTER);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBounds(0, 18, 980, 44);
        header.add(titulo);
        getContentPane().add(header);

        // ── TabbedPane ────────────────────────────────────────────────────────
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(TemaFinca.VERDE_CLARO);
        tabbedPane.setForeground(TemaFinca.VERDE_OSCURO);

        tabbedPane.addTab("  📦  Catálogo  ", buildPanelCatalogo());
        tabbedPane.addTab("  🛒  Compras  ", buildPanelCompras());
        tabbedPane.addTab("  🔧  Usos / Consumos  ", buildPanelUsos());

        tabbedPane.setBounds(20, 90, 935, 660);
        getContentPane().add(tabbedPane);

        revalidate();
        repaint();
    }

    // =========================================================================
    // PANEL — CATÁLOGO  (tabla: insumo  +  categoria_insumo)
    // =========================================================================
    private JScrollPane buildPanelCatalogo() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);

        int x1 = 30, x2 = 470, y = 18, gap = 72;

        // Fila 1
        lbl(p, "NOMBRE DEL INSUMO", x1, y);
        txtNombreInsumo = campo(x1, y + 20, 350);
        p.add(txtNombreInsumo);

        lbl(p, "CATEGORÍA", x2, y);
        cmbCategoriaInsumo = combo(new String[]{});
        setBounds(cmbCategoriaInsumo, p, x2, y + 20, 350);
        y += gap;

        // Fila 2
        lbl(p, "UNIDAD DE MEDIDA", x1, y);
        txtUnidad = campo(x1, y + 20, 200);
        p.add(txtUnidad);

        lbl(p, "PRECIO UNITARIO ($)", x1 + 220, y);
        txtPrecioUnitario = campo(x1 + 220, y + 20, 150);
        p.add(txtPrecioUnitario);

        lbl(p, "STOCK MÍNIMO", x2, y);
        txtStockMinimo = campo(x2, y + 20, 150);
        p.add(txtStockMinimo);

        lbl(p, "STOCK ACTUAL", x2 + 170, y);
        txtStockActual = campo(x2 + 170, y + 20, 180);
        p.add(txtStockActual);
        y += gap;

        // Fila 3 — descripción
        lbl(p, "DESCRIPCIÓN / OBSERVACIONES", x1, y);
        txtDescripcionInsumo = new JTextArea(3, 10);
        txtDescripcionInsumo.setFont(TemaFinca.FUENTE_INPUT);
        txtDescripcionInsumo.setLineWrap(true);
        txtDescripcionInsumo.setWrapStyleWord(true);
        scrollDescripcion = new JScrollPane(txtDescripcionInsumo);
        scrollDescripcion.setBounds(x1, y + 20, 830, 55);
        scrollDescripcion.setBorder(TemaFinca.bordeCampo());
        p.add(scrollDescripcion);
        y += 82;

        sep(p, x1, y);
        y += 15;

        // Tabla
        lbl(p, "CATÁLOGO DE INSUMOS", x1, y);
        y += 20;

        tblCatalogo = tabla(new String[]{
            "ID", "Nombre", "Categoría", "Unidad",
            "Precio Unit.", "Stock Mín.", "Stock Act.", "Activo"});
        scrollCatalogo = new JScrollPane(tblCatalogo);
        scrollCatalogo.setBounds(x1, y, 830, 130);
        scrollCatalogo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        p.add(scrollCatalogo);
        y += 148;

        // Botones
        btnNuevoCat = new JButton("NUEVO");
        btnGuardarCat = new JButton("GUARDAR");
        btnActualizarCat = new JButton("ACTUALIZAR");
        btnEliminarCat = new JButton("ELIMINAR");

        setIconos(btnNuevoCat, btnGuardarCat, btnActualizarCat, btnEliminarCat);

        int[] pos = botonRow(x1, y, 830, 4);
        btnNuevoCat.setBounds(pos[0], y, pos[4], 42);
        btnGuardarCat.setBounds(pos[1], y, pos[4], 42);
        btnActualizarCat.setBounds(pos[2], y, pos[4], 42);
        btnEliminarCat.setBounds(pos[3], y, pos[4], 42);

        EstiloFormularioBase.estilizarBoton(btnNuevoCat, TemaFinca.VERDE_OSCURO, null);
        EstiloFormularioBase.estilizarBoton(btnGuardarCat, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnActualizarCat, TemaFinca.BTN_ACTUALIZAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminarCat, new Color(180, 50, 50), null);

        btnNuevoCat.addActionListener(e -> limpiarCatalogo());
        btnGuardarCat.addActionListener(e -> guardarInsumo());
        btnActualizarCat.addActionListener(e -> actualizarInsumo());
        btnEliminarCat.addActionListener(e -> eliminarInsumo());

        tblCatalogo.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                cargarFilaCatalogo();
            }
        });

        p.add(btnNuevoCat);
        p.add(btnGuardarCat);
        p.add(btnActualizarCat);
        p.add(btnEliminarCat);

        y += 42 + 20;
        p.setPreferredSize(new Dimension(870, y));

        return scrollPanel(p);
    }

    // =========================================================================
    // PANEL — COMPRAS  (tabla: compra_insumo)
    // =========================================================================
    private JScrollPane buildPanelCompras() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);

        int x1 = 30, x2 = 470, y = 18, gap = 72;

        // Filtros
        lbl(p, "FILTRAR DESDE (AAAA-MM-DD)", x1, y);
        txtFiltroFechaDesde = campo(x1, y + 20, 230);
        p.add(txtFiltroFechaDesde);

        lbl(p, "HASTA (AAAA-MM-DD)", x1 + 250, y);
        txtFiltroFechaHasta = campo(x1 + 250, y + 20, 230);
        p.add(txtFiltroFechaHasta);

        btnFiltrarCompras = new JButton("FILTRAR");
        EstiloFormularioBase.estilizarBoton(btnFiltrarCompras, TemaFinca.VERDE_OSCURO, null);
        btnFiltrarCompras.setBounds(x1 + 500, y + 18, 120, 34);
        btnFiltrarCompras.addActionListener(e
                -> cargarCompras(txtFiltroFechaDesde.getText().trim(),
                        txtFiltroFechaHasta.getText().trim()));
        p.add(btnFiltrarCompras);

        sep(p, x1, y + 62);
        y += 80;

        // Fila 1
        lbl(p, "INSUMO", x1, y);
        cmbInsumoCompra = combo(new String[]{});
        setBounds(cmbInsumoCompra, p, x1, y + 20, 350);

        lbl(p, "PROVEEDOR", x2, y);
        txtProveedorCompra = campo(x2, y + 20, 350);
        p.add(txtProveedorCompra);
        y += gap;

        // Fila 2
        lbl(p, "CANTIDAD", x1, y);
        txtCantidadCompra = campo(x1, y + 20, 200);
        p.add(txtCantidadCompra);

        lbl(p, "PRECIO UNITARIO ($)", x1 + 220, y);
        txtPrecioCompra = campo(x1 + 220, y + 20, 220);
        p.add(txtPrecioCompra);

        lbl(p, "FECHA COMPRA (AAAA-MM-DD)", x2, y);
        txtFechaCompra = campo(x2, y + 20, 350);
        p.add(txtFechaCompra);
        y += gap;

        // Fila 3
        lbl(p, "N° FACTURA / REMISIÓN", x1, y);
        txtFacturaCompra = campo(x1, y + 20, 350);
        p.add(txtFacturaCompra);
        y += gap;

        // Observaciones
        lbl(p, "OBSERVACIONES", x1, y);
        txtObsCompra = new JTextArea(3, 10);
        txtObsCompra.setFont(TemaFinca.FUENTE_INPUT);
        txtObsCompra.setLineWrap(true);
        txtObsCompra.setWrapStyleWord(true);
        scrollObsCompra = new JScrollPane(txtObsCompra);
        scrollObsCompra.setBounds(x1, y + 20, 830, 55);
        scrollObsCompra.setBorder(TemaFinca.bordeCampo());
        p.add(scrollObsCompra);
        y += 82;

        sep(p, x1, y);
        y += 15;

        // Tabla
        lbl(p, "HISTORIAL DE COMPRAS", x1, y);
        y += 20;

        tblCompras = tabla(new String[]{
            "ID", "Insumo", "Proveedor", "Cantidad",
            "Precio Unit.", "Valor Total", "Fecha Compra", "Factura"});
        scrollCompras = new JScrollPane(tblCompras);
        scrollCompras.setBounds(x1, y, 830, 130);
        scrollCompras.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        p.add(scrollCompras);
        y += 148;

        // Botones
        btnGuardarCompra = new JButton("REGISTRAR COMPRA");
        btnEliminarCompra = new JButton("ELIMINAR");

        setIconos(null, btnGuardarCompra, null, btnEliminarCompra);
        EstiloFormularioBase.estilizarBoton(btnGuardarCompra, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminarCompra, new Color(180, 50, 50), null);

        int bW = 230, bH = 42, bGap = 20;
        btnGuardarCompra.setBounds(x1, y, bW, bH);
        btnEliminarCompra.setBounds(x1 + bW + bGap, y, 185, bH);

        btnGuardarCompra.addActionListener(e -> guardarCompra());
        btnEliminarCompra.addActionListener(e -> eliminarCompra());

        tblCompras.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                cargarFilaCompra();
            }
        });

        p.add(btnGuardarCompra);
        p.add(btnEliminarCompra);

        y += bH + 20;
        p.setPreferredSize(new Dimension(870, y));
        return scrollPanel(p);
    }

    // =========================================================================
    // PANEL — USOS / CONSUMOS  (tabla: uso_insumo)
    // =========================================================================
    private JScrollPane buildPanelUsos() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);

        int x1 = 30, x2 = 470, y = 18, gap = 72;

        // Filtros
        lbl(p, "FILTRAR POR INSUMO (nombre parcial)", x1, y);
        txtFiltroInsumoUso = campo(x1, y + 20, 280);
        p.add(txtFiltroInsumoUso);

        lbl(p, "FILTRAR POR FECHA (AAAA-MM-DD)", x1 + 300, y);
        txtFiltroFechaUso = campo(x1 + 300, y + 20, 230);
        p.add(txtFiltroFechaUso);

        btnFiltrarUsos = new JButton("FILTRAR");
        EstiloFormularioBase.estilizarBoton(btnFiltrarUsos, TemaFinca.VERDE_OSCURO, null);
        btnFiltrarUsos.setBounds(x1 + 550, y + 18, 120, 34);
        btnFiltrarUsos.addActionListener(e -> {
            // id_insumo 0 = sin filtro por ID; usamos nombre parcial en la query
            cargarUsos(0, txtFiltroFechaUso.getText().trim());
        });
        p.add(btnFiltrarUsos);

        sep(p, x1, y + 62);
        y += 80;

        // Fila 1
        lbl(p, "INSUMO", x1, y);
        cmbInsumoUso = combo(new String[]{});
        setBounds(cmbInsumoUso, p, x1, y + 20, 350);

        lbl(p, "FECHA DE USO (AAAA-MM-DD)", x2, y);
        txtFechaUso = campo(x2, y + 20, 350);
        p.add(txtFechaUso);
        y += gap;

        // Fila 2
        lbl(p, "CANTIDAD USADA", x1, y);
        txtCantidadUso = campo(x1, y + 20, 200);
        p.add(txtCantidadUso);

        lbl(p, "PRECIO UNITARIO ($)", x1 + 220, y);
        txtPrecioUso = campo(x1 + 220, y + 20, 220);
        p.add(txtPrecioUso);

        lbl(p, "LOTE / ANIMAL", x2, y);
        txtLoteUso = campo(x2, y + 20, 350);
        p.add(txtLoteUso);
        y += gap;

        // Fila 3
        lbl(p, "MOTIVO / DESCRIPCIÓN DEL USO", x1, y);
        txtMotivoUso = campo(x1, y + 20, 830);
        p.add(txtMotivoUso);
        y += gap;

        sep(p, x1, y);
        y += 15;

        // Tabla
        lbl(p, "HISTORIAL DE USOS / CONSUMOS", x1, y);
        y += 20;

        tblUsos = tabla(new String[]{
            "ID", "Insumo", "Cantidad", "Precio Unit.",
            "Valor Total", "Fecha Uso", "Lote", "Motivo"});
        scrollUsos = new JScrollPane(tblUsos);
        scrollUsos.setBounds(x1, y, 830, 130);
        scrollUsos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        p.add(scrollUsos);
        y += 148;

        // Botones
        btnGuardarUso = new JButton("REGISTRAR USO");
        btnEliminarUso = new JButton("ELIMINAR");

        setIconos(null, btnGuardarUso, null, btnEliminarUso);
        EstiloFormularioBase.estilizarBoton(btnGuardarUso, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminarUso, new Color(180, 50, 50), null);

        int bW = 210, bH = 42, bGap = 20;
        btnGuardarUso.setBounds(x1, y, bW, bH);
        btnEliminarUso.setBounds(x1 + bW + bGap, y, 185, bH);

        btnGuardarUso.addActionListener(e -> guardarUso());
        btnEliminarUso.addActionListener(e -> eliminarUso());

        tblUsos.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                cargarFilaUso();
            }
        });

        p.add(btnGuardarUso);
        p.add(btnEliminarUso);

        y += bH + 20;
        p.setPreferredSize(new Dimension(870, y));
        return scrollPanel(p);
    }

    // =========================================================================
    // HELPERS UI
    // =========================================================================
    private void lbl(JPanel p, String texto, int x, int y) {
        JLabel l = new JLabel(texto);
        l.setFont(TemaFinca.FUENTE_LABEL);
        l.setForeground(TemaFinca.GRIS_TEXTO);
        l.setBounds(x, y, 430, 20);
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

    private void setBounds(JComboBox<String> c, JPanel p, int x, int y, int w) {
        c.setBounds(x, y, w, 30);
        p.add(c);
    }

    private void sep(JPanel p, int x, int y) {
        JSeparator s = new JSeparator();
        s.setBounds(x, y, 830, 2);
        s.setForeground(new Color(220, 220, 220));
        p.add(s);
    }

    private JTable tabla(String[] cols) {
        JTable t = new JTable(new DefaultTableModel(new Object[][]{}, cols));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(26);
        t.setGridColor(new Color(240, 240, 240));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setReorderingAllowed(false);
        t.setSelectionBackground(new Color(200, 230, 201));
        t.setSelectionForeground(Color.BLACK);
        return t;
    }

    private JScrollPane scrollPanel(JPanel p) {
        JScrollPane sp = new JScrollPane(p);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private int[] botonRow(int x0, int y, int totalW, int n) {
        int bGap = 15;
        int bW = (totalW - bGap * (n - 1)) / n;
        int[] res = new int[n + 1];
        for (int i = 0; i < n; i++) {
            res[i] = x0 + i * (bW + bGap);
        }
        res[n] = bW;
        return res;
    }

    private void setIconos(JButton nuevo, JButton guardar, JButton actualizar, JButton eliminar) {
        try {
            if (nuevo != null) {
                nuevo.setIcon(new ImageIcon(getClass().getResource("/imagenes/buscar.png")));
            }
            if (guardar != null) {
                guardar.setIcon(new ImageIcon(getClass().getResource("/imagenes/expediente.png")));
            }
            if (actualizar != null) {
                actualizar.setIcon(new ImageIcon(getClass().getResource("/imagenes/actualizarr.png")));
            }
            if (eliminar != null) {
                eliminar.setIcon(new ImageIcon(getClass().getResource("/imagenes/papelera.png")));
            }
        } catch (Exception ignored) {
        }
    }

    // =========================================================================
    // LÓGICA — cargar combos
    // =========================================================================
    private void cargarComboCategorias() {
        try (Connection cn = conexion.conectar()) {
            ResultSet rs = cn.prepareStatement(
                    "SELECT id_categoria, nombre FROM categoria_insumo WHERE activo=1 ORDER BY nombre"
            ).executeQuery();
            DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
            m.addElement("-- Seleccione --");
            while (rs.next()) {
                m.addElement(rs.getInt("id_categoria") + " - " + rs.getString("nombre"));
            }
            cmbCategoriaInsumo.setModel(m);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarCombosInsumos() {
        try (Connection cn = conexion.conectar()) {
            ResultSet rs = cn.prepareStatement(
                    "SELECT id_insumo, nombre FROM insumo WHERE activo=1 ORDER BY nombre"
            ).executeQuery();
            DefaultComboBoxModel<String> mC = new DefaultComboBoxModel<>();
            DefaultComboBoxModel<String> mU = new DefaultComboBoxModel<>();
            mC.addElement("-- Seleccione --");
            mU.addElement("-- Seleccione --");
            while (rs.next()) {
                String item = rs.getInt("id_insumo") + " - " + rs.getString("nombre");
                mC.addElement(item);
                mU.addElement(item);
            }
            cmbInsumoCompra.setModel(mC);
            cmbInsumoUso.setModel(mU);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // LÓGICA — Catálogo
    // =========================================================================
    private void cargarCatalogo() {
        DefaultTableModel m = (DefaultTableModel) tblCatalogo.getModel();
        m.setRowCount(0);
        try (Connection cn = conexion.conectar()) {
            String sql
                    = "SELECT i.id_insumo, i.nombre, c.nombre AS categoria, i.unidad_medida, "
                    + "i.precio_unitario, i.stock_minimo, i.stock_actual, i.activo "
                    + "FROM insumo i "
                    + "JOIN categoria_insumo c ON i.id_categoria = c.id_categoria "
                    + "ORDER BY i.nombre";
            ResultSet rs = cn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getInt("id_insumo"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getString("unidad_medida"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("stock_minimo"),
                    rs.getDouble("stock_actual"),
                    rs.getInt("activo") == 1 ? "Sí" : "No"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarInsumo() {
        String nombre = txtNombreInsumo.getText().trim();
        String selCat = (String) cmbCategoriaInsumo.getSelectedItem();
        if (nombre.isEmpty() || selCat == null || selCat.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Nombre y categoría son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idCat = Integer.parseInt(selCat.split(" - ")[0]);
        try (Connection cn = conexion.conectar()) {
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO insumo (id_categoria, nombre, descripcion, unidad_medida, "
                    + "precio_unitario, stock_actual, stock_minimo) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, idCat);
            ps.setString(2, nombre);
            ps.setString(3, txtDescripcionInsumo.getText().trim());
            ps.setString(4, txtUnidad.getText().trim());
            ps.setDouble(5, parseDbl(txtPrecioUnitario.getText()));
            ps.setDouble(6, parseDbl(txtStockActual.getText()));
            ps.setDouble(7, parseDbl(txtStockMinimo.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Insumo guardado correctamente.");
            limpiarCatalogo();
            cargarCatalogo();
            cargarCombosInsumos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarInsumo() {
        int row = tblCatalogo.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo en la tabla.");
            return;
        }
        int id = (int) tblCatalogo.getValueAt(row, 0);
        String selCat = (String) cmbCategoriaInsumo.getSelectedItem();
        if (selCat == null || selCat.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría.");
            return;
        }
        int idCat = Integer.parseInt(selCat.split(" - ")[0]);
        try (Connection cn = conexion.conectar()) {
            PreparedStatement ps = cn.prepareStatement(
                    "UPDATE insumo SET id_categoria=?, nombre=?, descripcion=?, unidad_medida=?, "
                    + "precio_unitario=?, stock_actual=?, stock_minimo=? WHERE id_insumo=?");
            ps.setInt(1, idCat);
            ps.setString(2, txtNombreInsumo.getText().trim());
            ps.setString(3, txtDescripcionInsumo.getText().trim());
            ps.setString(4, txtUnidad.getText().trim());
            ps.setDouble(5, parseDbl(txtPrecioUnitario.getText()));
            ps.setDouble(6, parseDbl(txtStockActual.getText()));
            ps.setDouble(7, parseDbl(txtStockMinimo.getText()));
            ps.setInt(8, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Insumo actualizado.");
            limpiarCatalogo();
            cargarCatalogo();
            cargarCombosInsumos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarInsumo() {
        int row = tblCatalogo.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo.");
            return;
        }
        int id = (int) tblCatalogo.getValueAt(row, 0);
        int ok = JOptionPane.showConfirmDialog(this,
                "¿Desactivar este insumo? (no se eliminará físicamente)", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection cn = conexion.conectar()) {
            // Desactivar en vez de borrar para preservar historial
            cn.prepareStatement("UPDATE insumo SET activo=0 WHERE id_insumo=" + id).executeUpdate();
            JOptionPane.showMessageDialog(this, "Insumo desactivado.");
            limpiarCatalogo();
            cargarCatalogo();
            cargarCombosInsumos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFilaCatalogo() {
        int row = tblCatalogo.getSelectedRow();
        if (row < 0) {
            return;
        }
        txtNombreInsumo.setText(str(tblCatalogo, row, 1));
        setComboByNombre(cmbCategoriaInsumo, str(tblCatalogo, row, 2));
        txtUnidad.setText(str(tblCatalogo, row, 3));
        txtPrecioUnitario.setText(str(tblCatalogo, row, 4));
        txtStockMinimo.setText(str(tblCatalogo, row, 5));
        txtStockActual.setText(str(tblCatalogo, row, 6));
    }

    private void limpiarCatalogo() {
        txtNombreInsumo.setText("");
        txtUnidad.setText("");
        txtPrecioUnitario.setText("");
        txtStockMinimo.setText("");
        txtStockActual.setText("");
        txtDescripcionInsumo.setText("");
        cmbCategoriaInsumo.setSelectedIndex(0);
        tblCatalogo.clearSelection();
    }

    // =========================================================================
    // LÓGICA — Compras  (tabla: compra_insumo)
    // valor_total es STORED GENERATED → NO se inserta
    // =========================================================================
    private void cargarCompras(String desde, String hasta) {
        DefaultTableModel m = (DefaultTableModel) tblCompras.getModel();
        m.setRowCount(0);
        try (Connection cn = conexion.conectar()) {
            String sql
                    = "SELECT c.id_compra, i.nombre, c.proveedor, c.cantidad, "
                    + "c.precio_unitario, c.valor_total, c.fecha_compra, c.factura "
                    + "FROM compra_insumo c "
                    + "JOIN insumo i ON c.id_insumo = i.id_insumo "
                    + "WHERE 1=1 "
                    + (noVacio(desde) ? " AND c.fecha_compra >= '" + desde + "'" : "")
                    + (noVacio(hasta) ? " AND c.fecha_compra <= '" + hasta + "'" : "")
                    + " ORDER BY c.fecha_compra DESC";
            ResultSet rs = cn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getInt("id_compra"),
                    rs.getString("nombre"),
                    rs.getString("proveedor"),
                    rs.getDouble("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("valor_total"),
                    rs.getString("fecha_compra"),
                    rs.getString("factura")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarCompra() {
        String selInsumo = (String) cmbInsumoCompra.getSelectedItem();
        if (selInsumo == null || selInsumo.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo.");
            return;
        }
        int idInsumo = Integer.parseInt(selInsumo.split(" - ")[0]);
        double cantidad = parseDbl(txtCantidadCompra.getText());
        double precioUnit = parseDbl(txtPrecioCompra.getText());
        if (cantidad <= 0 || precioUnit <= 0) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser mayores a 0.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection cn = conexion.conectar()) {
            // valor_total es GENERATED → no se incluye en el INSERT
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO compra_insumo "
                    + "(id_insumo, fecha_compra, cantidad, precio_unitario, proveedor, factura, observaciones) "
                    + "VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, idInsumo);
            ps.setString(2, noVacio(txtFechaCompra.getText())
                    ? txtFechaCompra.getText().trim() : LocalDate.now().toString());
            ps.setDouble(3, cantidad);
            ps.setDouble(4, precioUnit);
            ps.setString(5, txtProveedorCompra.getText().trim());
            ps.setString(6, txtFacturaCompra.getText().trim());
            ps.setString(7, txtObsCompra.getText().trim());
            ps.executeUpdate();

            // Actualizar stock_actual en insumo
            cn.prepareStatement(
                    "UPDATE insumo SET stock_actual = stock_actual + " + cantidad
                    + " WHERE id_insumo = " + idInsumo).executeUpdate();

            JOptionPane.showMessageDialog(this, "Compra registrada y stock actualizado.");
            limpiarCompra();
            cargarCompras(null, null);
            cargarCatalogo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCompra() {
        int row = tblCompras.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una compra.");
            return;
        }
        int id = (int) tblCompras.getValueAt(row, 0);
        double cant = (double) tblCompras.getValueAt(row, 3);
        int ok = JOptionPane.showConfirmDialog(this,
                "¿Eliminar esta compra? El stock se reducirá en " + cant + " unidades.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection cn = conexion.conectar()) {
            ResultSet rs = cn.prepareStatement(
                    "SELECT id_insumo FROM compra_insumo WHERE id_compra=" + id).executeQuery();
            if (rs.next()) {
                int idIns = rs.getInt("id_insumo");
                cn.prepareStatement(
                        "UPDATE insumo SET stock_actual = stock_actual - " + cant
                        + " WHERE id_insumo=" + idIns).executeUpdate();
            }
            cn.prepareStatement("DELETE FROM compra_insumo WHERE id_compra=" + id).executeUpdate();
            JOptionPane.showMessageDialog(this, "Compra eliminada.");
            limpiarCompra();
            cargarCompras(null, null);
            cargarCatalogo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFilaCompra() {
        int row = tblCompras.getSelectedRow();
        if (row < 0) {
            return;
        }
        setComboByNombre(cmbInsumoCompra, str(tblCompras, row, 1));
        txtProveedorCompra.setText(str(tblCompras, row, 2));
        txtCantidadCompra.setText(str(tblCompras, row, 3));
        txtPrecioCompra.setText(str(tblCompras, row, 4));
        txtFechaCompra.setText(str(tblCompras, row, 6));
        txtFacturaCompra.setText(str(tblCompras, row, 7));
    }

    private void limpiarCompra() {
        cmbInsumoCompra.setSelectedIndex(0);
        txtProveedorCompra.setText("");
        txtCantidadCompra.setText("");
        txtPrecioCompra.setText("");
        txtFechaCompra.setText("");
        txtFacturaCompra.setText("");
        txtObsCompra.setText("");
        tblCompras.clearSelection();
    }

    // =========================================================================
    // LÓGICA — Usos  (tabla: uso_insumo)
    // valor_total es STORED GENERATED → NO se inserta
    // =========================================================================
    private void cargarUsos(int idInsumo, String fecha) {
        DefaultTableModel m = (DefaultTableModel) tblUsos.getModel();
        m.setRowCount(0);
        try (Connection cn = conexion.conectar()) {
            String sql
                    = "SELECT u.id_uso, i.nombre, u.cantidad, u.precio_unitario, "
                    + "u.valor_total, u.fecha_uso, u.lote, u.motivo "
                    + "FROM uso_insumo u "
                    + "JOIN insumo i ON u.id_insumo = i.id_insumo "
                    + "WHERE 1=1 "
                    + (idInsumo > 0 ? " AND u.id_insumo = " + idInsumo : "")
                    + (noVacio(fecha) ? " AND u.fecha_uso = '" + fecha + "'" : "")
                    + " ORDER BY u.fecha_uso DESC";
            ResultSet rs = cn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getInt("id_uso"),
                    rs.getString("nombre"),
                    rs.getDouble("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("valor_total"),
                    rs.getString("fecha_uso"),
                    rs.getString("lote"),
                    rs.getString("motivo")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarUso() {
        String selInsumo = (String) cmbInsumoUso.getSelectedItem();
        if (selInsumo == null || selInsumo.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo.");
            return;
        }
        int idInsumo = Integer.parseInt(selInsumo.split(" - ")[0]);
        double cantidad = parseDbl(txtCantidadUso.getText());
        double precioUnit = parseDbl(txtPrecioUso.getText());
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection cn = conexion.conectar()) {
            // Verificar stock
            ResultSet rs = cn.prepareStatement(
                    "SELECT stock_actual FROM insumo WHERE id_insumo=" + idInsumo).executeQuery();
            if (rs.next() && rs.getDouble("stock_actual") < cantidad) {
                JOptionPane.showMessageDialog(this,
                        "Stock insuficiente. Stock actual: " + rs.getDouble("stock_actual"),
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // valor_total es GENERATED → no se incluye en el INSERT
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO uso_insumo (id_insumo, fecha_uso, cantidad, precio_unitario, lote, motivo) "
                    + "VALUES (?,?,?,?,?,?)");
            ps.setInt(1, idInsumo);
            ps.setString(2, noVacio(txtFechaUso.getText())
                    ? txtFechaUso.getText().trim() : LocalDate.now().toString());
            ps.setDouble(3, cantidad);
            ps.setDouble(4, precioUnit);
            ps.setString(5, txtLoteUso.getText().trim());
            ps.setString(6, txtMotivoUso.getText().trim());
            ps.executeUpdate();

            // Descontar stock
            cn.prepareStatement(
                    "UPDATE insumo SET stock_actual = stock_actual - " + cantidad
                    + " WHERE id_insumo=" + idInsumo).executeUpdate();

            JOptionPane.showMessageDialog(this, "Uso registrado y stock descontado.");
            limpiarUso();
            cargarUsos(0, null);
            cargarCatalogo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUso() {
        int row = tblUsos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro.");
            return;
        }
        int id = (int) tblUsos.getValueAt(row, 0);
        double cant = (double) tblUsos.getValueAt(row, 2);
        int ok = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este uso? El stock se restaurará en " + cant + " unidades.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection cn = conexion.conectar()) {
            ResultSet rs = cn.prepareStatement(
                    "SELECT id_insumo FROM uso_insumo WHERE id_uso=" + id).executeQuery();
            if (rs.next()) {
                int idIns = rs.getInt("id_insumo");
                cn.prepareStatement(
                        "UPDATE insumo SET stock_actual = stock_actual + " + cant
                        + " WHERE id_insumo=" + idIns).executeUpdate();
            }
            cn.prepareStatement("DELETE FROM uso_insumo WHERE id_uso=" + id).executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro eliminado y stock restaurado.");
            limpiarUso();
            cargarUsos(0, null);
            cargarCatalogo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFilaUso() {
        int row = tblUsos.getSelectedRow();
        if (row < 0) {
            return;
        }
        setComboByNombre(cmbInsumoUso, str(tblUsos, row, 1));
        txtCantidadUso.setText(str(tblUsos, row, 2));
        txtPrecioUso.setText(str(tblUsos, row, 3));
        txtFechaUso.setText(str(tblUsos, row, 5));
        txtLoteUso.setText(str(tblUsos, row, 6));
        txtMotivoUso.setText(str(tblUsos, row, 7));
    }

    private void limpiarUso() {
        cmbInsumoUso.setSelectedIndex(0);
        txtCantidadUso.setText("");
        txtPrecioUso.setText("");
        txtFechaUso.setText("");
        txtLoteUso.setText("");
        txtMotivoUso.setText("");
        tblUsos.clearSelection();
    }

    // =========================================================================
    // UTILIDADES
    // =========================================================================
    private double parseDbl(String s) {
        try {
            return Double.parseDouble(s.trim().replace(",", "."));
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean noVacio(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String str(JTable t, int row, int col) {
        Object v = t.getValueAt(row, col);
        return v != null ? v.toString() : "";
    }

    /**
     * Busca en el combo un ítem que CONTENGA el texto dado
     */
    private void setComboByNombre(JComboBox<String> c, String texto) {
        for (int i = 0; i < c.getItemCount(); i++) {
            if (c.getItemAt(i).contains(texto)) {
                c.setSelectedIndex(i);
                return;
            }
        }
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
