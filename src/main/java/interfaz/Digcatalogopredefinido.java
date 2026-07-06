package interfaz;

import clases.conexion;
import estilos.TemaFinca;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana modal: el usuario elige un insumo del catálogo predefinido
 * (catalogo_insumos_predefinidos). Al confirmar, expone los valores
 * elegidos mediante getters para que el formulario que la invocó
 * (mdiInsumos) los cargue en sus campos — el usuario podrá editarlos
 * libremente antes de guardar. No se inserta nada en la tabla "insumo"
 * desde aquí.
 */
public class Digcatalogopredefinido extends JDialog {

    private JTextField txtBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnUsar, btnCancelar;

    // Resultado seleccionado (null si se canceló)
    private String  nombreSeleccionado;
    private String  unidadSeleccionada;
    private double  precioSeleccionado;
    private String  categoriaSeleccionada;
    private boolean confirmado = false;

    public Digcatalogopredefinido(Window owner) {
        super(owner, "Catálogo Predefinido de Insumos", ModalityType.APPLICATION_MODAL);
        construirUI();
        cargarDatos(null);
        setLocationRelativeTo(owner);
    }

    private void construirUI() {
        setSize(800, 560);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ── Header ───────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(TemaFinca.VERDE_OSCURO);
        header.setPreferredSize(new Dimension(800, 60));
        JLabel titulo = new JLabel("  📦  Elegir Insumo del Catálogo", SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(TemaFinca.DORADO);
        header.add(titulo, BorderLayout.CENTER);
        getContentPane().add(header, BorderLayout.NORTH);

        // ── Centro: buscador + tabla ─────────────────────────────────────────
        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setBackground(Color.WHITE);
        centro.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel panelBuscar = new JPanel(new BorderLayout(8, 0));
        panelBuscar.setBackground(Color.WHITE);
        JLabel lblBuscar = new JLabel("🔍 Buscar:");
        lblBuscar.setFont(TemaFinca.FUENTE_LABEL);
        txtBuscar = new JTextField();
        txtBuscar.setFont(TemaFinca.FUENTE_INPUT);
        txtBuscar.setBorder(TemaFinca.bordeCampo());
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { cargarDatos(txtBuscar.getText().trim()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { cargarDatos(txtBuscar.getText().trim()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });
        panelBuscar.add(lblBuscar, BorderLayout.WEST);
        panelBuscar.add(txtBuscar, BorderLayout.CENTER);
        centro.add(panelBuscar, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[][]{}, new String[]{
            "Nombre", "Unidad", "Precio Sugerido", "Categoría", "Uso Recomendado"
        }) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(28);
        tabla.setGridColor(new Color(240, 240, 240));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionBackground(new Color(200, 230, 201));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(220);

        // Doble clic = usar directamente
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) confirmarSeleccion();
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centro.add(scroll, BorderLayout.CENTER);

        getContentPane().add(centro, BorderLayout.CENTER);

        // ── Footer: botones ──────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        footer.setBackground(new Color(245, 245, 245));

        btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.addActionListener(e -> { confirmado = false; dispose(); });

        btnUsar = new JButton("USAR ESTE INSUMO");
        btnUsar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnUsar.setBackground(TemaFinca.BTN_GUARDAR);
        btnUsar.setForeground(Color.WHITE);
        btnUsar.setFocusPainted(false);
        btnUsar.addActionListener(e -> confirmarSeleccion());

        footer.add(btnCancelar);
        footer.add(btnUsar);
        getContentPane().add(footer, BorderLayout.SOUTH);
    }

    private void cargarDatos(String filtro) {
        modelo.setRowCount(0);
        try (Connection cn = conexion.conectar()) {
            String sql = "SELECT nombre, unidad_medida, precio_sugerido, categoria_sugerida, uso_recomendado " +
                         "FROM catalogo_insumos_predefinidos WHERE activo = 1 " +
                         (filtro != null && !filtro.isEmpty() ? " AND nombre LIKE ? " : "") +
                         " ORDER BY nombre";
            java.sql.PreparedStatement ps = cn.prepareStatement(sql);
            if (filtro != null && !filtro.isEmpty()) {
                ps.setString(1, "%" + filtro + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("unidad_medida"),
                    rs.getDouble("precio_sugerido"),
                    rs.getString("categoria_sugerida"),
                    rs.getString("uso_recomendado")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar catálogo: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarSeleccion() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo de la lista.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        nombreSeleccionado    = (String) modelo.getValueAt(row, 0);
        unidadSeleccionada    = (String) modelo.getValueAt(row, 1);
        precioSeleccionado    = (double) modelo.getValueAt(row, 2);
        categoriaSeleccionada = (String) modelo.getValueAt(row, 3);
        confirmado = true;
        dispose();
    }

    // ── Getters para que mdiInsumos lea el resultado tras mostrar el diálogo ──
    public boolean fueConfirmado()       { return confirmado; }
    public String  getNombreElegido()    { return nombreSeleccionado; }
    public String  getUnidadElegida()    { return unidadSeleccionada; }
    public double  getPrecioElegido()    { return precioSeleccionado; }
    public String  getCategoriaElegida() { return categoriaSeleccionada; }
}