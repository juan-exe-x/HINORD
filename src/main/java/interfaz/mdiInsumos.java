package interfaz;

import clases.conexion;
import estilos.TemaFinca;
import estilos.EstiloFormularioBase;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

public class mdiInsumos extends javax.swing.JInternalFrame {

    // ── Catálogo + Compra (tabla seleccionable de predefinidos) ─────────────
    private JTable tblCatalogoPredef;
    private DefaultTableModel modeloCatalogoPredef;
    private JLabel lblTotalCompra;
    private JTextField txtProveedorCompra, txtFacturaCompra, txtFechaCompra;
    private JTextArea txtObsCompra;
    private JButton btnRegistrarCompra, btnLimpiarSeleccion;

    // Índices de columnas de la tabla de catálogo predefinido
    private static final int COL_SEL = 0, COL_NOMBRE = 1, COL_CATEGORIA = 2,
            COL_UNIDAD = 3, COL_PRECIO_SUG = 4, COL_CANTIDAD = 5,
            COL_PRECIO_REAL = 6, COL_SUBTOTAL = 7;

    // ── Usos / Consumos ──────────────────────────────────────────────────────
    private JComboBox<String> cmbInsumoUso;
    private JTextField txtCantidadUso, txtPrecioUso, txtFechaUso,
            txtLoteUso, txtMotivoUso;
    private JCheckBox chkDescontarHerramienta;
    private JTable tblUsos;
    private JScrollPane scrollUsos;
    private JButton btnGuardarUso, btnEliminarUso;
    private JLabel lblUnidadUso, lblPrecioSugeridoUso;

    // ── Inventario Actual ────────────────────────────────────────────────────
    private JTable tblInventarioActual;
    private JScrollPane scrollInventarioActual;

    // ── Estadísticas ─────────────────────────────────────────────────────────
    private JLabel lblKpiHoy, lblKpiSemana, lblKpiMes;
    private JTable tblEstadisticas;

    // ── Shared ───────────────────────────────────────────────────────────────
    private JTabbedPane tabbedPane;

    public mdiInsumos() {

        setTitle("Inventario de Insumos Ganaderos");
        setSize(980, 800);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);

        aplicarEstilo();
        cargarCatalogoPredefinidoEnTabla();  // ← se muestra de una vez, sin botón
        cargarCombosInsumos();
        cargarUsos();
        cargarInventarioActual();
        cargarEstadisticas();

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

    private void aplicarEstilo() {
        getContentPane().removeAll();
        getContentPane().setBackground(TemaFinca.VERDE_CLARO);
        getContentPane().setLayout(null);

        JPanel header = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon(getClass().getResource("/imagenes/inventario.png")).getImage();
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

        JLabel titulo = new JLabel("INVENTARIO DE INSUMOS GANADEROS", SwingConstants.CENTER);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBounds(0, 18, 980, 44);
        header.add(titulo);
        getContentPane().add(header);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(TemaFinca.VERDE_CLARO);
        tabbedPane.setForeground(TemaFinca.VERDE_OSCURO);

        tabbedPane.addTab("  📦  1. Catálogo y Compra  ", buildPanelCatalogoCompra());
        tabbedPane.addTab("  🔧  2. Aplicación y Consumos  ", buildPanelUsos());
        tabbedPane.addTab("  📋  3. Inventario Actual  ", buildPanelInventarioActual());
        tabbedPane.addTab("  📊  4. Estadísticas  ", buildPanelEstadisticas());

        tabbedPane.setBounds(20, 90, 935, 660);
        getContentPane().add(tabbedPane);

        revalidate();
        repaint();
    }

    // =========================================================================
    // PESTAÑA 1: CATÁLOGO + COMPRA — tabla de productos predefinidos,
    // selección con checkbox, cantidad y precio editables, suma automática.
    // =========================================================================
    private JScrollPane buildPanelCatalogoCompra() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);

        int x1 = 30, y = 18;

        lbl(p, "MARQUE LOS PRODUCTOS QUE COMPRÓ Y AJUSTE CANTIDAD / PRECIO SI FUE DIFERENTE", x1, y);
        y += 26;

        // ── Tabla de catálogo predefinido (la base de todo) ─────────────────
        String[] columnas = {"✓", "Producto", "Categoría", "Unidad", "Precio Sugerido", "Cantidad Comprada", "Precio Real Pagado", "Subtotal"};
        modeloCatalogoPredef = new DefaultTableModel(new Object[][]{}, columnas) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == COL_SEL) {
                    return Boolean.class;
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                // Solo editable si está marcado: cantidad y precio.
                // El checkbox siempre es editable.
                if (col == COL_SEL) {
                    return true;
                }
                boolean marcado = (Boolean) getValueAt(row, COL_SEL);
                return marcado && (col == COL_CANTIDAD || col == COL_PRECIO_REAL);
            }
        };

        tblCatalogoPredef = new JTable(modeloCatalogoPredef);
        tblCatalogoPredef.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblCatalogoPredef.setRowHeight(28);
        tblCatalogoPredef.setGridColor(new Color(235, 235, 235));
        tblCatalogoPredef.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblCatalogoPredef.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblCatalogoPredef.getTableHeader().setForeground(Color.WHITE);
        tblCatalogoPredef.getTableHeader().setReorderingAllowed(false);
        tblCatalogoPredef.setSelectionBackground(new Color(220, 240, 221));

        tblCatalogoPredef.getColumnModel().getColumn(COL_SEL).setMaxWidth(36);
        tblCatalogoPredef.getColumnModel().getColumn(COL_NOMBRE).setPreferredWidth(220);
        tblCatalogoPredef.getColumnModel().getColumn(COL_CATEGORIA).setPreferredWidth(110);

        // Resalta visualmente las filas marcadas (fondo verde clarito)
        tblCatalogoPredef.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                boolean marcado = (Boolean) table.getModel().getValueAt(row, COL_SEL);
                c.setBackground(marcado ? new Color(232, 248, 233) : Color.WHITE);
                if (column == COL_SUBTOTAL) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    ((JLabel) c).setFont(c.getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        });

        // Al cambiar checkbox, cantidad o precio → recalcula subtotal y total general
        modeloCatalogoPredef.addTableModelListener(ev -> {
            int row = ev.getFirstRow();
            if (row < 0) {
                return;
            }
            int col = ev.getColumn();
            if (col == COL_SEL || col == COL_CANTIDAD || col == COL_PRECIO_REAL) {
                recalcularSubtotalFila(row);
                recalcularTotalGeneral();
                tblCatalogoPredef.repaint();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tblCatalogoPredef);
        scrollTabla.setBounds(x1, y, 870, 380);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        p.add(scrollTabla);
        y += 396;

        sep(p, x1, y);
        y += 16;

        // ── Datos generales de la compra (aplica a todo el lote marcado) ────
        lbl(p, "VETERINARIA / TIENDA DE COMPRA", x1, y);
        txtProveedorCompra = campo(x1, y + 20, 280);
        p.add(txtProveedorCompra);

        lbl(p, "N° FACTURA / REMISIÓN", x1 + 300, y);
        txtFacturaCompra = campo(x1 + 300, y + 20, 250);
        p.add(txtFacturaCompra);

        lbl(p, "FECHA DE COMPRA (AAAA-MM-DD)", x1 + 570, y);
        txtFechaCompra = campo(x1 + 570, y + 20, 270);
        txtFechaCompra.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        p.add(txtFechaCompra);
        y += 58;

        lbl(p, "OBSERVACIONES (opcional)", x1, y);
        txtObsCompra = new JTextArea(2, 10);
        txtObsCompra.setFont(TemaFinca.FUENTE_INPUT);
        txtObsCompra.setLineWrap(true);
        JScrollPane scrollObs = new JScrollPane(txtObsCompra);
        scrollObs.setBounds(x1, y + 20, 870, 40);
        scrollObs.setBorder(TemaFinca.bordeCampo());
        p.add(scrollObs);
        y += 70;

        // ── Total general + botones ──────────────────────────────────────────
        lblTotalCompra = new JLabel("TOTAL A PAGAR: $ 0");
        lblTotalCompra.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalCompra.setForeground(TemaFinca.VERDE_OSCURO);
        lblTotalCompra.setBounds(x1, y, 400, 32);
        p.add(lblTotalCompra);

        btnLimpiarSeleccion = new JButton("LIMPIAR SELECCIÓN");
        EstiloFormularioBase.estilizarBoton(btnLimpiarSeleccion, new Color(150, 150, 150), null);
        btnLimpiarSeleccion.setBounds(x1 + 420, y - 2, 200, 38);
        btnLimpiarSeleccion.addActionListener(e -> limpiarSeleccionCatalogo());
        p.add(btnLimpiarSeleccion);

        btnRegistrarCompra = new JButton("✔ REGISTRAR COMPRA Y SUMAR AL INVENTARIO");
        EstiloFormularioBase.estilizarBoton(btnRegistrarCompra, TemaFinca.BTN_GUARDAR, null);
        btnRegistrarCompra.setBounds(x1 + 630, y - 2, 260, 38);
        btnRegistrarCompra.addActionListener(e -> registrarCompraDesdeSeleccion());
        p.add(btnRegistrarCompra);

        y += 60;
        p.setPreferredSize(new Dimension(900, y));
        return scrollPanel(p);
    }

    /**
     * Carga los 25 (o más) productos predefinidos en la tabla, sin tocar el
     * inventario.
     */
    private void cargarCatalogoPredefinidoEnTabla() {
        modeloCatalogoPredef.setRowCount(0);
        try (Connection cn = conexion.conectar()) {
            ResultSet rs = cn.prepareStatement(
                    "SELECT nombre, unidad_medida, precio_sugerido, categoria_sugerida "
                    + "FROM catalogo_insumos_predefinidos WHERE activo = 1 ORDER BY categoria_sugerida, nombre"
            ).executeQuery();
            while (rs.next()) {
                modeloCatalogoPredef.addRow(new Object[]{
                    Boolean.FALSE,
                    rs.getString("nombre"),
                    rs.getString("categoria_sugerida"),
                    rs.getString("unidad_medida"),
                    formatoMoneda(rs.getDouble("precio_sugerido")),
                    "", // cantidad — la llena el usuario
                    "", // precio real — opcional, si no se llena se usa el sugerido
                    "$ 0" // subtotal
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar catálogo predefinido: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recalcularSubtotalFila(int row) {
        boolean marcado = (Boolean) modeloCatalogoPredef.getValueAt(row, COL_SEL);
        if (!marcado) {
            modeloCatalogoPredef.setValueAt("$ 0", row, COL_SUBTOTAL);
            return;
        }
        double cantidad = parseDbl(String.valueOf(modeloCatalogoPredef.getValueAt(row, COL_CANTIDAD)));
        double precio = obtenerPrecioEfectivoFila(row);
        modeloCatalogoPredef.setValueAt(formatoMoneda(cantidad * precio), row, COL_SUBTOTAL);
    }

    /**
     * Si el usuario no escribió precio real, usa el precio sugerido de esa
     * fila.
     */
    private double obtenerPrecioEfectivoFila(int row) {
        String precioRealTxt = String.valueOf(modeloCatalogoPredef.getValueAt(row, COL_PRECIO_REAL));
        double precioReal = parseDbl(precioRealTxt);
        if (precioReal > 0) {
            return precioReal;
        }
        String precioSugTxt = String.valueOf(modeloCatalogoPredef.getValueAt(row, COL_PRECIO_SUG));
        return parseMoneda(precioSugTxt);
    }

    private void recalcularTotalGeneral() {
        double total = 0;
        for (int r = 0; r < modeloCatalogoPredef.getRowCount(); r++) {
            boolean marcado = (Boolean) modeloCatalogoPredef.getValueAt(r, COL_SEL);
            if (!marcado) {
                continue;
            }
            double cantidad = parseDbl(String.valueOf(modeloCatalogoPredef.getValueAt(r, COL_CANTIDAD)));
            double precio = obtenerPrecioEfectivoFila(r);
            total += cantidad * precio;
        }
        lblTotalCompra.setText("TOTAL A PAGAR: " + formatoMoneda(total));
    }

    private void limpiarSeleccionCatalogo() {
        for (int r = 0; r < modeloCatalogoPredef.getRowCount(); r++) {
            modeloCatalogoPredef.setValueAt(Boolean.FALSE, r, COL_SEL);
            modeloCatalogoPredef.setValueAt("", r, COL_CANTIDAD);
            modeloCatalogoPredef.setValueAt("", r, COL_PRECIO_REAL);
            modeloCatalogoPredef.setValueAt("$ 0", r, COL_SUBTOTAL);
        }
        txtProveedorCompra.setText("");
        txtFacturaCompra.setText("");
        txtObsCompra.setText("");
        txtFechaCompra.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        recalcularTotalGeneral();
        tblCatalogoPredef.repaint();
    }

    /**
     * Para cada fila marcada: si el insumo no existe en la tabla "insumo" (por
     * nombre), lo crea usando los datos del predefinido. Luego inserta la
     * compra en compra_insumo y suma el stock. Todo en una transacción.
     */
    private void registrarCompraDesdeSeleccion() {
        // Termina cualquier edición de celda pendiente antes de leer valores
        if (tblCatalogoPredef.isEditing()) {
            TableCellEditor editor = tblCatalogoPredef.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }

        java.util.List<Integer> filasMarcadas = new java.util.ArrayList<>();
        for (int r = 0; r < modeloCatalogoPredef.getRowCount(); r++) {
            if ((Boolean) modeloCatalogoPredef.getValueAt(r, COL_SEL)) {
                filasMarcadas.add(r);
            }
        }

        if (filasMarcadas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Marque al menos un producto que haya comprado.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que cada fila marcada tenga cantidad > 0
        for (int r : filasMarcadas) {
            double cantidad = parseDbl(String.valueOf(modeloCatalogoPredef.getValueAt(r, COL_CANTIDAD)));
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Indique la cantidad comprada de \"" + modeloCatalogoPredef.getValueAt(r, COL_NOMBRE) + "\".",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        String fecha = !txtFechaCompra.getText().trim().isEmpty()
                ? txtFechaCompra.getText().trim()
                : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String proveedor = txtProveedorCompra.getText().trim();
        String factura = txtFacturaCompra.getText().trim();
        String observaciones = txtObsCompra.getText().trim();

        try (Connection cn = conexion.conectar()) {
            cn.setAutoCommit(false);

            for (int r : filasMarcadas) {
                String nombre = String.valueOf(modeloCatalogoPredef.getValueAt(r, COL_NOMBRE));
                String categoriaSugerida = String.valueOf(modeloCatalogoPredef.getValueAt(r, COL_CATEGORIA));
                String unidad = String.valueOf(modeloCatalogoPredef.getValueAt(r, COL_UNIDAD));
                double cantidad = parseDbl(String.valueOf(modeloCatalogoPredef.getValueAt(r, COL_CANTIDAD)));
                double precioEfectivo = obtenerPrecioEfectivoFila(r);

                int idInsumo = obtenerOcrearInsumo(cn, nombre, categoriaSugerida, unidad, precioEfectivo);

                // valor_total es STORED GENERATED → no se inserta manualmente
                PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO compra_insumo (id_insumo, fecha_compra, cantidad, precio_unitario, proveedor, factura, observaciones) "
                        + "VALUES (?,?,?,?,?,?,?)");
                ps.setInt(1, idInsumo);
                ps.setString(2, fecha);
                ps.setDouble(3, cantidad);
                ps.setDouble(4, precioEfectivo);
                ps.setString(5, proveedor);
                ps.setString(6, factura);
                ps.setString(7, observaciones);
                ps.executeUpdate();

                PreparedStatement psUp = cn.prepareStatement(
                        "UPDATE insumo SET stock_actual = stock_actual + ? WHERE id_insumo = ?");
                psUp.setDouble(1, cantidad);
                psUp.setInt(2, idInsumo);
                psUp.executeUpdate();
            }

            cn.commit();
            JOptionPane.showMessageDialog(this,
                    "Compra registrada. " + filasMarcadas.size() + " producto(s) sumado(s) al inventario.");

            limpiarSeleccionCatalogo();
            cargarCombosInsumos();
            cargarInventarioActual();
            cargarEstadisticas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la compra: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca el insumo por nombre exacto en la tabla "insumo". Si no existe, lo
     * crea usando la categoría sugerida (mapeada o creada si falta) y los datos
     * del catálogo predefinido. Devuelve el id_insumo resultante.
     */
    private int obtenerOcrearInsumo(Connection cn, String nombre, String categoriaSugerida,
            String unidad, double precioReferencia) throws SQLException {

        PreparedStatement psBuscar = cn.prepareStatement("SELECT id_insumo FROM insumo WHERE nombre = ?");
        psBuscar.setString(1, nombre);
        ResultSet rs = psBuscar.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_insumo");
        }

        int idCategoria = obtenerOcrearCategoria(cn, categoriaSugerida);

        PreparedStatement psInsert = cn.prepareStatement(
                "INSERT INTO insumo (nombre, id_categoria, unidad_medida, precio_unitario, stock_minimo, stock_actual, activo) "
                + "VALUES (?, ?, ?, ?, ?, 0, 1)", Statement.RETURN_GENERATED_KEYS);
        psInsert.setString(1, nombre);
        psInsert.setInt(2, idCategoria);
        psInsert.setString(3, unidad);
        psInsert.setDouble(4, precioReferencia);
        psInsert.setDouble(5, 1); // stock mínimo por defecto, el usuario lo puede ajustar luego
        psInsert.executeUpdate();

        ResultSet keys = psInsert.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }
        throw new SQLException("No se pudo crear el insumo \"" + nombre + "\".");
    }

    private int obtenerOcrearCategoria(Connection cn, String nombreCategoria) throws SQLException {
        PreparedStatement psBuscar = cn.prepareStatement("SELECT id_categoria FROM categoria_insumo WHERE nombre = ?");
        psBuscar.setString(1, nombreCategoria);
        ResultSet rs = psBuscar.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_categoria");
        }

        PreparedStatement psInsert = cn.prepareStatement(
                "INSERT INTO categoria_insumo (nombre, descripcion, activo) VALUES (?, ?, 1)",
                Statement.RETURN_GENERATED_KEYS);
        psInsert.setString(1, nombreCategoria);
        psInsert.setString(2, "Creada automáticamente desde el catálogo predefinido");
        psInsert.executeUpdate();

        ResultSet keys = psInsert.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }
        throw new SQLException("No se pudo crear la categoría \"" + nombreCategoria + "\".");
    }

    // =========================================================================
    // PESTAÑA 2: CONSUMOS / USOS (igual que antes, columnas reales de la BD)
    // =========================================================================
    private JScrollPane buildPanelUsos() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);

        int x1 = 30, x2 = 470, y = 18, gap = 72;

        lbl(p, "REGISTRA AQUÍ CADA APLICACIÓN O USO DE UN PRODUCTO DEL INVENTARIO", x1, y);
        y += 30;

        lbl(p, "PRODUCTO A UTILIZAR", x1, y);
        cmbInsumoUso = combo(new String[]{});
        setBounds(cmbInsumoUso, p, x1, y + 20, 350);

        lblUnidadUso = new JLabel("Unidad: -");
        lblUnidadUso.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUnidadUso.setBounds(x1, y + 52, 160, 20);
        p.add(lblUnidadUso);

        lblPrecioSugeridoUso = new JLabel("Precio Ref: -");
        lblPrecioSugeridoUso.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrecioSugeridoUso.setBounds(x1 + 180, y + 52, 200, 20);
        p.add(lblPrecioSugeridoUso);

        cmbInsumoUso.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                actualizarLabelsUso();
            }
        });

        lbl(p, "FECHA DE APLICACIÓN / CONSUMO", x2, y);
        txtFechaUso = campo(x2, y + 20, 350);
        txtFechaUso.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        p.add(txtFechaUso);
        y += gap + 15;

        lbl(p, "CANTIDAD(Dosis/ml/Bultos)", x1, y);
        txtCantidadUso = campo(x1, y + 20, 160);
        p.add(txtCantidadUso);

        lbl(p, "PRECIO REFERENCIA UNITARIO ($)", x1 + 190, y);
        txtPrecioUso = campo(x1 + 190, y + 25, 175);
        p.add(txtPrecioUso);

        lbl(p, "IDENTIFICACIÓN DEL ANIMAL / LOTE TRATADO", x2, y);
        txtLoteUso = campo(x2, y + 20, 350);
        p.add(txtLoteUso);
        y += gap;

        lbl(p, "MOTIVO / DESCRIPCIÓN DEL USO", x1, y);
        txtMotivoUso = campo(x1, y + 20, 350);
        p.add(txtMotivoUso);

        chkDescontarHerramienta = new JCheckBox("Descontar automáticamente 1 Aguja/Jeringa desechable del stock");
        chkDescontarHerramienta.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkDescontarHerramienta.setBackground(Color.WHITE);
        chkDescontarHerramienta.setBounds(x2, y + 20, 400, 28);
        p.add(chkDescontarHerramienta);
        y += gap;

        sep(p, x1, y);
        y += 15;

        lbl(p, "HISTORIAL DE APLICACIONES (más reciente primero)", x1, y);
        y += 22;

        tblUsos = tabla(new String[]{"ID", "Insumo", "Cantidad", "Precio Ref.", "Total", "Cuándo", "Animal/Lote", "Motivo"});
        scrollUsos = new JScrollPane(tblUsos);
        scrollUsos.setBounds(x1, y, 830, 180);
        scrollUsos.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        p.add(scrollUsos);
        y += 195;

        btnGuardarUso = new JButton("REGISTRAR APLICACIÓN");
        btnEliminarUso = new JButton("ELIMINAR");
        setIconos(null, btnGuardarUso, null, btnEliminarUso);
        EstiloFormularioBase.estilizarBoton(btnGuardarUso, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminarUso, new Color(180, 50, 50), null);

        btnGuardarUso.setBounds(x1, y, 230, 42);
        btnEliminarUso.setBounds(x1 + 250, y, 150, 42);

        btnGuardarUso.addActionListener(e -> guardarUso());
        btnEliminarUso.addActionListener(e -> eliminarUso());

        tblUsos.getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                cargarFilaUso();
            }
        });

        p.add(btnGuardarUso);
        p.add(btnEliminarUso);
        y += 60;
        p.setPreferredSize(new Dimension(870, y));
        return scrollPanel(p);
    }

    private void actualizarLabelsUso() {
        String seleccionado = (String) cmbInsumoUso.getSelectedItem();
        if (seleccionado == null || seleccionado.startsWith("--")) {
            lblUnidadUso.setText("Unidad: -");
            lblPrecioSugeridoUso.setText("Precio Ref: -");
            return;
        }
        int idInsumo = Integer.parseInt(seleccionado.split(" - ")[0]);
        try (Connection cn = conexion.conectar()) {
            PreparedStatement ps = cn.prepareStatement("SELECT unidad_medida, precio_unitario, stock_actual FROM insumo WHERE id_insumo = ?");
            ps.setInt(1, idInsumo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblUnidadUso.setText("Unidad: " + rs.getString("unidad_medida"));
                lblPrecioSugeridoUso.setText("Stock: " + rs.getDouble("stock_actual"));
                txtPrecioUso.setText(String.valueOf((int) rs.getDouble("precio_unitario")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // PESTAÑA 3: INVENTARIO ACTUAL (SEMAFORIZACIÓN)
    // =========================================================================
    private JScrollPane buildPanelInventarioActual() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);

        int x1 = 30, y = 18;
        lbl(p, "ESTADO GENERAL DE LA BODEGA — Qué tienes, qué se está agotando", x1, y);
        y += 25;

        tblInventarioActual = tabla(new String[]{"ID", "Nombre Insumo", "Stock Disponible", "Stock Mínimo Alerta", "Estado"});

        tblInventarioActual.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String val = value.toString();
                    if (val.contains("🔴") || val.contains("❌")) {
                        c.setForeground(new Color(180, 40, 40));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(new Color(30, 120, 30));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                }
                return c;
            }
        });

        scrollInventarioActual = new JScrollPane(tblInventarioActual);
        scrollInventarioActual.setBounds(x1, y, 830, 430);
        scrollInventarioActual.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        p.add(scrollInventarioActual);
        y += 445;

        JButton btnRecargarInv = new JButton("🔄 REFRESCAR BODEGA");
        EstiloFormularioBase.estilizarBoton(btnRecargarInv, TemaFinca.VERDE_OSCURO, null);
        btnRecargarInv.setBounds(x1, y, 220, 38);
        btnRecargarInv.addActionListener(e -> cargarInventarioActual());
        p.add(btnRecargarInv);

        y += 50;
        p.setPreferredSize(new Dimension(870, y));
        return scrollPanel(p);
    }

    // =========================================================================
    // PESTAÑA 4: ESTADÍSTICAS — Hoy / Semana / Mes
    // =========================================================================
    private JScrollPane buildPanelEstadisticas() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);

        int x1 = 30, y = 18;
        lbl(p, "CUÁNTO HAS GASTADO EN INSUMOS — lo que compraste, no lo que usaste", x1, y);
        y += 30;

        int cardW = 260, cardH = 100, cardGap = 25;

        JPanel cardHoy = crearCardEstadistica("GASTADO HOY", TemaFinca.VERDE_OSCURO);
        cardHoy.setBounds(x1, y, cardW, cardH);
        lblKpiHoy = (JLabel) cardHoy.getComponent(1);
        p.add(cardHoy);

        JPanel cardSemana = crearCardEstadistica("ESTA SEMANA", TemaFinca.BTN_GUARDAR);
        cardSemana.setBounds(x1 + cardW + cardGap, y, cardW, cardH);
        lblKpiSemana = (JLabel) cardSemana.getComponent(1);
        p.add(cardSemana);

        JPanel cardMes = crearCardEstadistica("ESTE MES", TemaFinca.BTN_ACTUALIZAR);
        cardMes.setBounds(x1 + 2 * (cardW + cardGap), y, cardW, cardH);
        lblKpiMes = (JLabel) cardMes.getComponent(1);
        p.add(cardMes);

        y += cardH + 30;
        sep(p, x1, y);
        y += 20;

        lbl(p, "HISTÓRICO ACUMULADO POR PRODUCTO (todas las compras)", x1, y);
        y += 30;

        tblEstadisticas = tabla(new String[]{"Insumo/Producto", "Cantidad Comprada", "Total Invertido (COP)", "N° Compras"});
        JScrollPane scrollEst = new JScrollPane(tblEstadisticas);
        scrollEst.setBounds(x1, y, 830, 300);
        scrollEst.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        p.add(scrollEst);
        y += 315;

        JButton btnRefrescar = new JButton("🔄 ACTUALIZAR ESTADÍSTICAS");
        EstiloFormularioBase.estilizarBoton(btnRefrescar, TemaFinca.VERDE_OSCURO, null);
        btnRefrescar.setBounds(x1, y, 260, 38);
        btnRefrescar.addActionListener(e -> cargarEstadisticas());
        p.add(btnRefrescar);

        y += 50;
        p.setPreferredSize(new Dimension(870, y));
        return scrollPanel(p);
    }

    /**
     * KPIs HOY / SEMANA / MES — usando las columnas REALES: fecha_compra,
     * valor_total
     */
    private void cargarEstadisticas() {
        try (Connection cn = conexion.conectar()) {
            PreparedStatement psHoy = cn.prepareStatement(
                    "SELECT COALESCE(SUM(valor_total),0) AS total FROM compra_insumo WHERE fecha_compra = CURDATE()");
            ResultSet rsHoy = psHoy.executeQuery();
            if (rsHoy.next()) {
                lblKpiHoy.setText(formatoMoneda(rsHoy.getDouble("total")));
            }

            PreparedStatement psSemana = cn.prepareStatement(
                    "SELECT COALESCE(SUM(valor_total),0) AS total FROM compra_insumo "
                    + "WHERE YEARWEEK(fecha_compra, 1) = YEARWEEK(CURDATE(), 1)");
            ResultSet rsSemana = psSemana.executeQuery();
            if (rsSemana.next()) {
                lblKpiSemana.setText(formatoMoneda(rsSemana.getDouble("total")));
            }

            PreparedStatement psMes = cn.prepareStatement(
                    "SELECT COALESCE(SUM(valor_total),0) AS total FROM compra_insumo "
                    + "WHERE MONTH(fecha_compra) = MONTH(CURDATE()) AND YEAR(fecha_compra) = YEAR(CURDATE())");
            ResultSet rsMes = psMes.executeQuery();
            if (rsMes.next()) {
                lblKpiMes.setText(formatoMoneda(rsMes.getDouble("total")));
            }

            DefaultTableModel m = (DefaultTableModel) tblEstadisticas.getModel();
            m.setRowCount(0);
            PreparedStatement psMatriz = cn.prepareStatement(
                    "SELECT i.nombre, SUM(c.cantidad) AS cant, SUM(c.valor_total) AS inversion, COUNT(c.id_compra) AS conteo "
                    + "FROM compra_insumo c INNER JOIN insumo i ON c.id_insumo = i.id_insumo "
                    + "GROUP BY i.nombre ORDER BY inversion DESC");
            ResultSet rsMatriz = psMatriz.executeQuery();
            while (rsMatriz.next()) {
                m.addRow(new Object[]{
                    rsMatriz.getString("nombre"),
                    rsMatriz.getDouble("cant"),
                    formatoMoneda(rsMatriz.getDouble("inversion")),
                    rsMatriz.getInt("conteo")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarInventarioActual() {
        if (tblInventarioActual == null) {
            return;
        }
        DefaultTableModel m = (DefaultTableModel) tblInventarioActual.getModel();
        m.setRowCount(0);
        try (Connection cn = conexion.conectar()) {
            String sql = "SELECT id_insumo, nombre, stock_minimo, stock_actual, unidad_medida FROM insumo WHERE activo=1 ORDER BY nombre";
            ResultSet rs = cn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                double sActual = rs.getDouble("stock_actual");
                double sMinimo = rs.getDouble("stock_minimo");
                String unidad = rs.getString("unidad_medida");
                String estado = (sActual <= sMinimo) ? "🔴 Bajo Stock / Comprar" : "🟢 Stock Correcto";
                if (sActual <= 0) {
                    estado = "❌ AGOTADO EN BODEGA";
                }

                m.addRow(new Object[]{
                    rs.getInt("id_insumo"), rs.getString("nombre"),
                    sActual + " " + unidad, sMinimo + " " + unidad, estado
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // CRUD — USOS  (columnas reales: cantidad, precio_unitario, fecha_uso, lote, motivo)
    // =========================================================================
    private void guardarUso() {
        if (cmbInsumoUso.getSelectedIndex() <= 0 || txtCantidadUso.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione el producto y la cantidad.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idInsumo = Integer.parseInt(cmbInsumoUso.getSelectedItem().toString().split(" - ")[0]);
        double cantidad = parseDbl(txtCantidadUso.getText());
        double precio = parseDbl(txtPrecioUso.getText());

        try (Connection cn = conexion.conectar()) {
            cn.setAutoCommit(false);

            PreparedStatement psCheck = cn.prepareStatement("SELECT stock_actual FROM insumo WHERE id_insumo = ?");
            psCheck.setInt(1, idInsumo);
            ResultSet rsC = psCheck.executeQuery();
            if (rsC.next() && rsC.getDouble("stock_actual") < cantidad) {
                JOptionPane.showMessageDialog(this,
                        "No hay suficiente cantidad en el inventario.\nStock disponible: " + rsC.getDouble("stock_actual"),
                        "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                cn.rollback();
                return;
            }

            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO uso_insumo (id_insumo, fecha_uso, cantidad, precio_unitario, lote, motivo) "
                    + "VALUES (?,?,?,?,?,?)");
            ps.setInt(1, idInsumo);
            ps.setString(2, !txtFechaUso.getText().trim().isEmpty()
                    ? txtFechaUso.getText().trim()
                    : new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            ps.setDouble(3, cantidad);
            ps.setDouble(4, precio);
            ps.setString(5, txtLoteUso.getText().trim());
            ps.setString(6, txtMotivoUso.getText().trim());
            ps.executeUpdate();

            PreparedStatement psSub = cn.prepareStatement(
                    "UPDATE insumo SET stock_actual = stock_actual - ? WHERE id_insumo = ?");
            psSub.setDouble(1, cantidad);
            psSub.setInt(2, idInsumo);
            psSub.executeUpdate();

            if (chkDescontarHerramienta.isSelected()) {
                PreparedStatement psHerramienta = cn.prepareStatement(
                        "UPDATE insumo SET stock_actual = GREATEST(stock_actual - 1, 0) "
                        + "WHERE (nombre LIKE '%aguja%' OR nombre LIKE '%jeringa%') AND activo = 1 LIMIT 1");
                psHerramienta.executeUpdate();
            }

            cn.commit();
            JOptionPane.showMessageDialog(this, "Aplicación guardada. Inventario descontado.");
            limpiarUso();
            cargarUsos();
            cargarInventarioActual();
        } catch (Exception e) {
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
            ResultSet rs = cn.prepareStatement("SELECT id_insumo FROM uso_insumo WHERE id_uso=" + id).executeQuery();
            if (rs.next()) {
                int idIns = rs.getInt("id_insumo");
                cn.prepareStatement("UPDATE insumo SET stock_actual = stock_actual + " + cant + " WHERE id_insumo=" + idIns).executeUpdate();
            }
            cn.prepareStatement("DELETE FROM uso_insumo WHERE id_uso=" + id).executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro eliminado y stock restaurado.");
            limpiarUso();
            cargarUsos();
            cargarInventarioActual();
        } catch (Exception e) {
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
        // La columna "Cuándo" ahora muestra texto legible (Hoy/Ayer/fecha),
        // no se reutiliza como valor editable para evitar guardar literalmente "Hoy".
        txtLoteUso.setText(str(tblUsos, row, 6));
        txtMotivoUso.setText(str(tblUsos, row, 7));
    }

    private void limpiarUso() {
        if (cmbInsumoUso.getItemCount() > 0) {
            cmbInsumoUso.setSelectedIndex(0);
        }
        txtCantidadUso.setText("");
        txtPrecioUso.setText("");
        txtLoteUso.setText("");
        txtMotivoUso.setText("");
        txtFechaUso.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        chkDescontarHerramienta.setSelected(false);
        tblUsos.clearSelection();
        lblUnidadUso.setText("Unidad: -");
        lblPrecioSugeridoUso.setText("Precio Ref: -");
    }

    /**
     * Muestra siempre el historial completo, ordenado del más reciente al más
     * antiguo, con la fecha presentada de forma legible (Hoy / Ayer /
     * dd-MM-yyyy) en vez de obligar al usuario a escribir o filtrar por fecha.
     */
    private void cargarUsos() {
        DefaultTableModel m = (DefaultTableModel) tblUsos.getModel();
        m.setRowCount(0);
        try (Connection cn = conexion.conectar()) {
            String sql = "SELECT u.id_uso, i.nombre, u.cantidad, u.precio_unitario, u.valor_total, u.fecha_uso, u.lote, u.motivo "
                    + "FROM uso_insumo u INNER JOIN insumo i ON u.id_insumo=i.id_insumo "
                    + "ORDER BY u.fecha_uso DESC, u.id_uso DESC";
            ResultSet rs = cn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5),
                    formatearFechaLegible(rs.getDate(6)), rs.getString(7), rs.getString(8)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convierte una fecha SQL en "Hoy", "Ayer" o dd-MM-yyyy, para que el
     * historial se lea de un vistazo sin necesidad de filtrar nada.
     */
    private String formatearFechaLegible(java.sql.Date fecha) {
        if (fecha == null) {
            return "-";
        }
        java.time.LocalDate f = fecha.toLocalDate();
        java.time.LocalDate hoy = java.time.LocalDate.now();
        if (f.isEqual(hoy)) {
            return "Hoy";
        }
        if (f.isEqual(hoy.minusDays(1))) {
            return "Ayer";
        }
        return new SimpleDateFormat("dd-MM-yyyy").format(fecha);
    }

    private void cargarCombosInsumos() {
        try (Connection cn = conexion.conectar()) {
            ResultSet rs = cn.prepareStatement("SELECT id_insumo, nombre FROM insumo WHERE activo=1 ORDER BY nombre").executeQuery();
            DefaultComboBoxModel<String> mU = new DefaultComboBoxModel<>();
            mU.addElement("-- Seleccione el Producto --");
            while (rs.next()) {
                mU.addElement(rs.getInt(1) + " - " + rs.getString(2));
            }
            cmbInsumoUso.setModel(mU);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // HELPERS
    // =========================================================================
    private void lbl(JPanel p, String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setFont(TemaFinca.FUENTE_LABEL);
        l.setForeground(TemaFinca.GRIS_TEXTO);
        l.setBounds(x, y, 870, 20);
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
        return c;
    }

    private void setBounds(JComboBox<String> c, JPanel p, int x, int y, int w) {
        c.setBounds(x, y, w, 30);
        p.add(c);
    }

    private void sep(JPanel p, int x, int y) {
        JSeparator s = new JSeparator();
        s.setBounds(x, y, 870, 2);
        s.setForeground(new Color(220, 220, 220));
        p.add(s);
    }

    private JScrollPane scrollPanel(JPanel p) {
        JScrollPane sp = new JScrollPane(p);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private String formatoMoneda(double valor) {
        return "$ " + String.format("%,.0f", valor).replace(",", ".");
    }

    /**
     * Convierte un texto tipo "$ 53.000" de vuelta a double (53000.0)
     */
    private double parseMoneda(String s) {
        if (s == null) {
            return 0;
        }
        String limpio = s.replace("$", "").replace(".", "").replace(",", "").trim();
        try {
            return Double.parseDouble(limpio);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDbl(String s) {
        if (s == null) {
            return 0;
        }
        try {
            return Double.parseDouble(s.trim().replace(",", "."));
        } catch (Exception e) {
            return 0;
        }
    }

    private String str(JTable t, int row, int col) {
        Object v = t.getValueAt(row, col);
        return v != null ? v.toString() : "";
    }

    private void setComboByNombre(JComboBox<String> c, String texto) {
        for (int i = 0; i < c.getItemCount(); i++) {
            if (c.getItemAt(i).contains(texto)) {
                c.setSelectedIndex(i);
                return;
            }
        }
    }

    private JPanel crearCardEstadistica(String t, Color f) {
        JPanel c = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(f);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        c.setOpaque(false);
        JLabel lT = new JLabel(t, SwingConstants.CENTER);
        lT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lT.setForeground(Color.WHITE);
        lT.setBounds(0, 14, 260, 20);
        JLabel lV = new JLabel("$ 0", SwingConstants.CENTER);
        lV.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lV.setForeground(Color.WHITE);
        lV.setBounds(0, 40, 260, 40);
        c.add(lT);
        c.add(lV);
        return c;
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
