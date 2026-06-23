package interfaz;

import clases.Parto;
import clases.Gestacion;
import clases.conexion;
import clases.registro;
import dao.GestacionDAO;
import estilos.EstiloFormularioBase;
import estilos.TemaFinca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class mdiRegistroParto extends javax.swing.JInternalFrame {

    // ── DAOs ──────────────────────────────────────────────────────────────────
    private final Parto partoDAO = new Parto();
    private final GestacionDAO gestDAO = new GestacionDAO();
    private final registro animalDAO = new registro();

    // ── Estado ────────────────────────────────────────────────────────────────
    private int idGestacionSeleccionada = 0;
    private int idMadreSeleccionada = 0;

    private JScrollPane scrollObs;

    // ── Componentes (todos creados en aplicarEstilo) ───────────────────────────
    private JComboBox<String> cmbGestacionesPendientes;
    private JComboBox<String> cmbTipoParto, cmbCondicionMadre,
            cmbComplicacionMadre, cmbEstadoCria,
            cmbSexoCria, cmbRazaCria, cmbClasificacionCria;

    private JTextField txtDuracionHoras,
            txtFechaRevision, txtNombreCria, txtIdicaCria, txtPesoCria;

    private JCheckBox chkAsistenciaVet;
    private JSpinner spnCantidadCrias;
    private JTable tblPartos;

    private JLabel lblInfoMadre, lblInfoGestacion;

    private JButton btnNuevo, btnGuardar, btnActualizar, btnEliminar;

    private java.sql.Date convertirFechaFlexible(String textoFecha) throws ParseException {
        String[] formatosPosibles = {
            "dd/MM/yyyy", "yyyy/MM/dd", "dd-MM-yyyy", "yyyy-MM-dd"
        };

        for (String formato : formatosPosibles) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                sdf.setLenient(false);
                java.util.Date fechaUtil = sdf.parse(textoFecha);
                return new java.sql.Date(fechaUtil.getTime());
            } catch (ParseException e) {

            }
        }
        throw new ParseException("Formato de fecha no reconocido: " + textoFecha, 0);
    }

    public mdiRegistroParto() {
        setTitle("Registro de Partos");
        aplicarEstilo();
        cargarGestacionesPendientes();
        cargarTablaPartos();

        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);

        // ── Agregar esta línea al final ──
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    // =========================================================================
    // CARGAR COMBO DE GESTACIONES
    // =========================================================================
    private void cargarGestacionesPendientes() {
        cmbGestacionesPendientes.removeAllItems();
        cmbGestacionesPendientes.addItem("-- Seleccione gestación --");

        for (Gestacion g : gestDAO.obtenerActivas()) {
            if (!"parto_registrado".equals(g.getEstado())) {
                cmbGestacionesPendientes.addItem(
                        g.getIdGestacion()
                        + " | " + g.getIdicaVaca()
                        + " - " + g.getNombreVaca()
                        + " | Parto est: " + g.getFechaPartoEstimada()
                        + " (" + g.getDiasParaParto() + " días)");
            }
        }
        cmbGestacionesPendientes.addActionListener(e -> rellenarDatosMadre());
    }

    // =========================================================================
    // RELLENAR INFO AL SELECCIONAR GESTACIÓN
    // =========================================================================
    private void rellenarDatosMadre() {
        int idx = cmbGestacionesPendientes.getSelectedIndex();
        if (idx <= 0) {
            lblInfoMadre.setText("Madre: —");
            lblInfoGestacion.setText("Gestación: —");
            idGestacionSeleccionada = 0;
            idMadreSeleccionada = 0;
            return;
        }
        String sel = cmbGestacionesPendientes.getSelectedItem().toString();
        try {
            idGestacionSeleccionada = Integer.parseInt(sel.split("\\|")[0].trim());
        } catch (NumberFormatException ex) {
            return;
        }

        Gestacion g = gestDAO.obtenerPorId(idGestacionSeleccionada);
        if (g != null) {
            idMadreSeleccionada = g.getIdVaca();
            lblInfoMadre.setText("Madre: " + g.getNombreVaca()
                    + "   IDICA: " + g.getIdicaVaca());
            lblInfoGestacion.setText("Tipo: " + g.getTipoFecundacion()
                    + "   |   Origen: " + g.getOrigenGenetico()
                    + "   |   Serv.: " + g.getFechaServicio());
            txtFechaParto.setText(g.getFechaPartoEstimada().toString());
            txtFechaRevision.setText(g.getFechaPartoEstimada().plusDays(7).toString());
        }
    }

    // =========================================================================
    // CARGAR TABLA HISTORIAL
    // =========================================================================
    private void cargarTablaPartos() {
    DefaultTableModel modelo = (DefaultTableModel) tblPartos.getModel();
    modelo.setRowCount(0);
    ResultSet rs = partoDAO.buscarMadreYCrias("");
    if (rs == null) return;
    try {
        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id_parto"),           // ← era "idparto"
                rs.getString("nombre_madre"),
                rs.getDate("fecha_parto"),        // ← alias del SELECT
                rs.getString("tipo_parto"),
                rs.getInt("cantidad_crias"),
                rs.getString("estado_cria"),
                rs.getString("condicion_madre"),
                rs.getString("observaciones")
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar partos: " + e.getMessage());
    }
}
    // =========================================================================
    // GUARDAR PARTO + CRÍA + MARCAR GESTACIÓN
    // =========================================================================
    private void guardarParto() {

        if (idGestacionSeleccionada == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una gestación.");
            return;
        }

        try {
            java.sql.Date fechaParto = java.sql.Date.valueOf(
                    LocalDate.parse(txtFechaParto.getText().trim()));

            String tipoParto = cmbTipoParto.getSelectedItem().toString();

            String duracion = txtDuracionHoras.getText().trim().isEmpty()
                    ? "normal"
                    : txtDuracionHoras.getText().trim();

            String condicion = cmbCondicionMadre.getSelectedItem().toString();
            String complicacion = cmbComplicacionMadre.getSelectedItem().toString();

            String expulsion = "normal";
            String calostro = "si_madre";

            boolean asistencia = chkAsistenciaVet.isSelected();

            String medicamentos = txtVeterinario.getText().trim();

            String observaciones = txtObservaciones.getText().trim();

            boolean ok = partoDAO.guardar(
                    idGestacionSeleccionada,
                    fechaParto,
                    tipoParto,
                    duracion,
                    condicion,
                    expulsion,
                    calostro,
                    asistencia,
                    medicamentos,
                    observaciones
            );

            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "✔ Parto registrado correctamente.");

                gestDAO.marcarPartoRegistrado(idGestacionSeleccionada);

                limpiar();
                cargarTablaPartos();
                cargarGestacionesPendientes();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar el parto.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // ELIMINAR
    // =========================================================================
    private void eliminarPartoSeleccionado() {
        int fila = tblPartos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un parto de la tabla para eliminar.");
            return;
        }
        int idParto = Integer.parseInt(tblPartos.getValueAt(fila, 0).toString());
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el parto ID " + idParto + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            if (partoDAO.eliminar(idParto)) {
                JOptionPane.showMessageDialog(this, "Parto eliminado.");
                cargarTablaPartos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el parto.");
            }
        }
    }

    // =========================================================================
    // LIMPIAR
    // =========================================================================
    private void limpiar() {
        cmbGestacionesPendientes.setSelectedIndex(0);
        txtFechaParto.setText("");
        txtDuracionHoras.setText("");
        txtVeterinario.setText("");
        txtFechaRevision.setText("");
        txtNombreCria.setText("");
        txtIdicaCria.setText("");
        txtPesoCria.setText("");
        txtObservaciones.setText("");
        chkAsistenciaVet.setSelected(false);
        spnCantidadCrias.setValue(1);
        cmbTipoParto.setSelectedIndex(0);
        cmbCondicionMadre.setSelectedIndex(0);
        cmbComplicacionMadre.setSelectedIndex(0);
        cmbEstadoCria.setSelectedIndex(0);
        cmbSexoCria.setSelectedIndex(0);
        lblInfoMadre.setText("Madre: —");
        lblInfoGestacion.setText("Gestación: —");
        idGestacionSeleccionada = 0;
        idMadreSeleccionada = 0;
        tblPartos.clearSelection();
    }

    // =========================================================================
    // APLICAR ESTILO — construye TODO el UI
    // =========================================================================
    private void aplicarEstilo() {
        getContentPane().removeAll();
        getContentPane().setBackground(TemaFinca.VERDE_CLARO);
        getContentPane().setLayout(null);
        setSize(980, 800); // ← tamaño del frame visible (no importa cuánto mida el contenido)

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
        JLabel titulo = new JLabel("REGISTRO DE PARTOS", SwingConstants.CENTER);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBounds(0, 18, 980, 44);
        header.add(titulo);
        add(header);

        // ── Panel blanco con TODO el contenido ───────────────────────────────
        panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        // El ancho es 910, el alto se calcula al final según cuánto contenido haya
        // Lo ponemos grande para que quepa todo (se ajusta abajo)

        int x1 = 40, x2 = 470;
        int y = 18, gap = 72;

        // ── BLOQUE 1: Gestación ───────────────────────────────────────────────
        lbl(panel, "GESTACIÓN PENDIENTE DE PARTO", x1, y);
        cmbGestacionesPendientes = combo(new String[]{});
        cmbGestacionesPendientes.setBounds(x1, y + 20, 830, 30);
        panel.add(cmbGestacionesPendientes);
        y += 58;

        lblInfoMadre = new JLabel("Madre: —");
        lblInfoMadre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfoMadre.setForeground(TemaFinca.VERDE_OSCURO);
        lblInfoMadre.setBounds(x1, y, 830, 20);
        panel.add(lblInfoMadre);

        lblInfoGestacion = new JLabel("Gestación: —");
        lblInfoGestacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfoGestacion.setForeground(TemaFinca.GRIS_TEXTO);
        lblInfoGestacion.setBounds(x1, y + 22, 830, 18);
        panel.add(lblInfoGestacion);
        y += 48;

        sep(panel, x1, y);
        y += 15;

        // Fila 1
        lbl(panel, "FECHA DEL PARTO (AAAA-MM-DD)", x1, y);
        txtFechaParto = campo(x1, y + 20, 350);
        panel.add(txtFechaParto);
        lbl(panel, "TIPO DE PARTO", x2, y);
        cmbTipoParto = combo(new String[]{"Normal", "Distócico", "Gemelar", "Por cesárea"});
        setBounds(cmbTipoParto, x2, y + 20, 350);
        y += gap;

        // Fila 2
        lbl(panel, "DURACIÓN DEL PARTO (HORAS)", x1, y);
        txtDuracionHoras = campo(x1, y + 20, 350);
        panel.add(txtDuracionHoras);
        lbl(panel, "CONDICIÓN DE LA MADRE", x2, y);
        cmbCondicionMadre = combo(new String[]{"Normal", "Con complicaciones", "Crítica", "Falleció"});
        setBounds(cmbCondicionMadre, x2, y + 20, 350);
        y += gap;

        // Fila 3
        lbl(panel, "ASISTENCIA VETERINARIA", x1, y);
        chkAsistenciaVet = new JCheckBox("¿Hubo asistencia veterinaria?");
        chkAsistenciaVet.setFont(TemaFinca.FUENTE_INPUT);
        chkAsistenciaVet.setBackground(Color.WHITE);
        chkAsistenciaVet.setBounds(x1, y + 20, 240, 28);
        panel.add(chkAsistenciaVet);

        lbl(panel, "NOMBRE DEL VETERINARIO", x1 + 250, y);
        txtVeterinario = campo(x1 + 250, y + 20, 175);
        panel.add(txtVeterinario);

        lbl(panel, "COMPLICACIÓN EN LA MADRE", x2, y);
        cmbComplicacionMadre = combo(new String[]{
            "Ninguna", "Retención de placenta", "Prolapso", "Hemorragia", "Infección", "Otra"});
        setBounds(cmbComplicacionMadre, x2, y + 20, 350);
        y += gap;

        // Separador sección cría
        sep(panel, x1, y);
        JLabel lblSeccion = new JLabel("— DATOS DE LA CRÍA");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSeccion.setForeground(TemaFinca.VERDE_OSCURO);
        lblSeccion.setBounds(x1, y - 18, 400, 20);
        panel.add(lblSeccion);
        y += 16;

        // Fila 4
        lbl(panel, "CANTIDAD DE CRÍAS", x1, y);
        spnCantidadCrias = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        spnCantidadCrias.setFont(TemaFinca.FUENTE_INPUT);
        spnCantidadCrias.setBounds(x1, y + 20, 160, 28);
        panel.add(spnCantidadCrias);
        lbl(panel, "ESTADO DE LA CRÍA", x2, y);
        cmbEstadoCria = combo(new String[]{"Vivo", "Vivo con atención", "Mortinato", "Murió post-parto"});
        setBounds(cmbEstadoCria, x2, y + 20, 350);
        y += gap;

        // Fila 5
        lbl(panel, "NOMBRE DE LA CRÍA", x1, y);
        txtNombreCria = campo(x1, y + 20, 350);
        panel.add(txtNombreCria);
        lbl(panel, "IDICA DE LA CRÍA (opcional)", x2, y);
        txtIdicaCria = campo(x2, y + 20, 350);
        panel.add(txtIdicaCria);
        y += gap;

        // Fila 6
        lbl(panel, "PESO AL NACER (kg)", x1, y);
        txtPesoCria = campo(x1, y + 20, 350);
        panel.add(txtPesoCria);
        lbl(panel, "SEXO DE LA CRÍA", x2, y);
        cmbSexoCria = combo(new String[]{"Hembra", "Macho"});
        setBounds(cmbSexoCria, x2, y + 20, 350);
        y += gap;

        // Fila 7
        lbl(panel, "RAZA DE LA CRÍA", x1, y);
        cmbRazaCria = combo(new String[]{
            "Holstein", "Normando", "Angus", "Brahman",
            "Simmental", "Gyr", "Girolando", "Cruzado", "Otra"});
        setBounds(cmbRazaCria, x1, y + 20, 350);
        lbl(panel, "CLASIFICACIÓN", x2, y);
        cmbClasificacionCria = combo(new String[]{
            "Ternero/a de levante", "Novillo", "Novilla", "Otro"});
        setBounds(cmbClasificacionCria, x2, y + 20, 350);
        y += gap;

        // Fila 8
        lbl(panel, "FECHA DE REVISIÓN POST-PARTO (AAAA-MM-DD)", x1, y);
        txtFechaRevision = campo(x1, y + 20, 350);
        panel.add(txtFechaRevision);
        y += gap;

        // Observaciones
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

        // Tabla historial
        lbl(panel, "HISTORIAL DE PARTOS", x1, y);
        y += 20;

        tblPartos = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Madre", "Fecha", "Tipo",
                    "Crías", "Estado Cría", "Condición", "Observaciones"}));
        tblPartos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblPartos.setRowHeight(26);
        tblPartos.setGridColor(new Color(240, 240, 240));
        tblPartos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblPartos.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblPartos.getTableHeader().setForeground(Color.WHITE);
        tblPartos.getTableHeader().setReorderingAllowed(false);

        jScrollPane1 = new JScrollPane(tblPartos);
        jScrollPane1.setBounds(x1, y, 830, 130);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(jScrollPane1);
        y += 148;

        // Botones
        btnNuevo = new JButton("NUEVO");
        btnGuardar = new JButton("GUARDAR");
        btnActualizar = new JButton("ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");

        try {
            btnNuevo.setIcon(new ImageIcon(getClass().getResource("/imagenes/buscar.png")));
            btnGuardar.setIcon(new ImageIcon(getClass().getResource("/imagenes/expediente.png")));
            btnActualizar.setIcon(new ImageIcon(getClass().getResource("/imagenes/actualizarr.png")));
            btnEliminar.setIcon(new ImageIcon(getClass().getResource("/imagenes/papelera.png")));
        } catch (Exception ignored) {
        }

        int bW = 185, bH = 42, bGap = 18;
        int totalW = 4 * bW + 3 * bGap;
        int sx = (910 - totalW) / 2;

        btnNuevo.setBounds(sx, y, bW, bH);
        btnGuardar.setBounds(sx + bW + bGap, y, bW, bH);
        btnActualizar.setBounds(sx + 2 * (bW + bGap), y, bW, bH);
        btnEliminar.setBounds(sx + 3 * (bW + bGap), y, bW, bH);

        EstiloFormularioBase.estilizarBoton(btnNuevo, TemaFinca.VERDE_OSCURO, null);
        EstiloFormularioBase.estilizarBoton(btnGuardar, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnActualizar, TemaFinca.BTN_ACTUALIZAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminar, new Color(180, 50, 50), null);

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardarParto());
        btnActualizar.addActionListener(e
                -> JOptionPane.showMessageDialog(this,
                        "Seleccione un parto en el historial para modificarlo."));
        btnEliminar.addActionListener(e -> eliminarPartoSeleccionado());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);

        y += bH + 20; // margen inferior

        // ── CLAVE: ajustar alto del panel al contenido real ──────────────────
        panel.setPreferredSize(new Dimension(910, y));

        // ── Envolver el panel en un JScrollPane ──────────────────────────────
        JScrollPane scrollPrincipal = new JScrollPane(panel);
        scrollPrincipal.setBounds(35, 90, 910, 620); // altura visible del scroll
        scrollPrincipal.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16); // velocidad scroll
        add(scrollPrincipal);

        revalidate();
        repaint();
    }

    // =========================================================================
    // HELPERS INTERNOS DE CONSTRUCCIÓN UI
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

    /**
     * Posiciona el combo y lo agrega al panel
     */
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

    // =========================================================
// BUSCADOR DE MADRE desde tabla registro
// =========================================================
    private void abrirBuscadorMadre() {

        // ── Cuadro de búsqueda ────────────────────────────────
        String input = javax.swing.JOptionPane.showInputDialog(this,
                "Buscar por nombre o IDICA del animal:",
                "Buscar madre", javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        // ── Query a tabla registro ────────────────────────────
        String sql = "SELECT idregistro, nombre, IDICA, raza, sexo, edad, "
                + "       peso, clasificacion "
                + "FROM registro "
                + "WHERE nombre LIKE ? "
                + "   OR CAST(IDICA AS CHAR) LIKE ? "
                + "   OR CAST(idregistro AS CHAR) LIKE ? "
                + "ORDER BY nombre";

        String[] columnas = {
            "ID Registro", "Nombre", "IDICA", "Raza",
            "Sexo", "Edad", "Peso (kg)", "Clasificación"
        };

        javax.swing.table.DefaultTableModel modelo
                = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        try (java.sql.Connection conn = conexion.conectar(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + input.trim() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            java.sql.ResultSet rs = ps.executeQuery();
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                modelo.addRow(new Object[]{
                    rs.getInt("idregistro"),
                    rs.getString("nombre"),
                    rs.getString("IDICA"),
                    rs.getString("raza"),
                    rs.getString("sexo"),
                    rs.getInt("edad"),
                    rs.getDouble("peso"),
                    rs.getString("clasificacion")
                });
            }
            rs.close();

            if (!hayDatos) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se encontró ningún animal con: \"" + input + "\"",
                        "Sin resultados", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ── Tabla resultado ───────────────────────────────────
        javax.swing.JTable tabla = new javax.swing.JTable(modelo);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tabla.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Panel de información debajo de la tabla
        javax.swing.JLabel lblInfo = new javax.swing.JLabel(
                "Seleccione un animal y presione «Seleccionar»");
        lblInfo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
        lblInfo.setForeground(java.awt.Color.GRAY);

        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(tabla);
        scroll.setPreferredSize(new java.awt.Dimension(820, 220));

        javax.swing.JPanel panelDlg = new javax.swing.JPanel(
                new java.awt.BorderLayout(0, 8));
        panelDlg.add(scroll, java.awt.BorderLayout.CENTER);
        panelDlg.add(lblInfo, java.awt.BorderLayout.SOUTH);

        // ── Diálogo con opciones ──────────────────────────────
        String[] opciones = {"Seleccionar", "Cancelar"};

        int respuesta = javax.swing.JOptionPane.showOptionDialog(
                this,
                panelDlg,
                "Seleccionar madre del parto",
                javax.swing.JOptionPane.DEFAULT_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]);

        if (respuesta != 0) {
            return;   // canceló
        }
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Seleccione una fila primero.",
                    "Sin selección", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ── Llenar txtIdMadre y mostrar nombre como tooltip ───
        int idSeleccionado = (int) modelo.getValueAt(fila, 0);
        String nombreSeleccionado = (String) modelo.getValueAt(fila, 1);
        String razaSeleccionada = (String) modelo.getValueAt(fila, 3);
        Object edadSeleccionada = modelo.getValueAt(fila, 5);

        txtIdMadre.setText(String.valueOf(idSeleccionado));

        // Tooltip con resumen del animal seleccionado
        txtIdMadre.setToolTipText(
                "Madre: " + nombreSeleccionado
                + " | Raza: " + razaSeleccionada
                + " | Edad: " + edadSeleccionada + " años");

        // Confirmación visual discreta
        javax.swing.JOptionPane.showMessageDialog(this,
                "✔ Madre seleccionada:\n"
                + "  ID: " + idSeleccionado + "\n"
                + "  Nombre: " + nombreSeleccionado + "\n"
                + "  Raza: " + razaSeleccionada + "\n"
                + "  Edad: " + edadSeleccionada + " años",
                "Animal seleccionado",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

// ── ⑥ Método estilizarBotonPro ───────────────────────────
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox4 = new javax.swing.JComboBox<>();
        txtIdMadre = new javax.swing.JTextField();
        txtFechaParto = new javax.swing.JTextField();
        txtDuracion = new javax.swing.JTextField();
        txtComplicacion = new javax.swing.JTextField();
        txtCantidadCrias = new javax.swing.JTextField();
        cboTipoParto = new javax.swing.JComboBox<>();
        cboAsistencia = new javax.swing.JComboBox<>();
        cboCondicion = new javax.swing.JComboBox<>();
        cboEstadoCria = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        btnguardar = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btnbuscar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        lblCantidadCrias = new javax.swing.JLabel();
        lblComplicacion = new javax.swing.JLabel();
        lblDuracion = new javax.swing.JLabel();
        lblFechaParto = new javax.swing.JLabel();
        lblFechaRevision = new javax.swing.JLabel();
        lblIdMadre = new javax.swing.JLabel();
        lblObservaciones = new javax.swing.JLabel();
        lblVeterinario = new javax.swing.JLabel();
        lblIdParto = new javax.swing.JLabel();
        panelPrincipal = new javax.swing.JPanel();
        panel = new javax.swing.JPanel();
        btnBuscarMadre = new javax.swing.JButton();
        txtVeterinario = new javax.swing.JTextField();

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtIdMadre.setText("jTextField1");

        txtFechaParto.setText("jTextField2");

        txtDuracion.setText("jTextField3");

        txtComplicacion.setText("jTextField5");

        txtCantidadCrias.setText("jTextField6");

        cboTipoParto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboAsistencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboCondicion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboEstadoCria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtObservaciones.setColumns(20);
        txtObservaciones.setRows(5);
        jScrollPane1.setViewportView(txtObservaciones);

        btnguardar.setText("jButton1");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btnactualizar.setText("jButton1");
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });

        btnbuscar.setText("jButton1");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        btneliminar.setText("jButton1");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        lblCantidadCrias.setText("jLabel1");

        lblComplicacion.setText("jLabel1");

        lblDuracion.setText("jLabel1");

        lblFechaParto.setText("jLabel1");

        lblFechaRevision.setText("jLabel1");

        lblIdMadre.setText("jLabel1");

        lblObservaciones.setText("jLabel1");

        lblVeterinario.setText("jLabel8");

        lblIdParto.setText("jLabel9");

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        btnBuscarMadre.setText("Buscar Madre");

        txtVeterinario.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblComplicacion))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnguardar)
                                .addGap(18, 18, 18)
                                .addComponent(btnactualizar)
                                .addGap(18, 18, 18)
                                .addComponent(btnbuscar)
                                .addGap(18, 18, 18)
                                .addComponent(btneliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                .addComponent(lblIdParto))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtFechaParto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtIdMadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txtCantidadCrias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtComplicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtVeterinario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(64, 64, 64)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cboEstadoCria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cboTipoParto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cboAsistencia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cboCondicion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblCantidadCrias, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblDuracion, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblFechaParto, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(52, 52, 52)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblVeterinario)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblFechaRevision, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblIdMadre, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblObservaciones, javax.swing.GroupLayout.Alignment.TRAILING))))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBuscarMadre)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(102, 102, 102)
                                        .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(158, 158, 158))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdMadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTipoParto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCantidadCrias))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblComplicacion)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaParto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCondicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDuracion))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboEstadoCria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFechaParto)
                    .addComponent(txtVeterinario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtComplicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtCantidadCrias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFechaRevision)
                        .addGap(18, 18, 18)
                        .addComponent(lblIdMadre)
                        .addGap(18, 18, 18)
                        .addComponent(lblObservaciones))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(lblVeterinario)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnguardar)
                            .addComponent(btnactualizar)
                            .addComponent(btnbuscar)
                            .addComponent(btneliminar)
                            .addComponent(lblIdParto))
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarMadre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        if (lblIdParto.getText().trim().isEmpty()
                || lblIdParto.getText().equals("jLabel9")) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Primero busque el parto que desea eliminar.",
                    "Sin selección", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea ELIMINAR el parto con ID "
                + lblIdParto.getText() + "?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int idParto = Integer.parseInt(lblIdParto.getText().trim());
            Parto parto = new Parto();
            boolean ok = parto.eliminar(idParto);

            if (ok) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "✔ Parto eliminado correctamente.",
                        "Eliminado", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                limpiar();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se encontró el parto o ya fue eliminado.",
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "ID de parto inválido.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed

    }//GEN-LAST:event_btnbuscarActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
     if (lblIdParto.getText().trim().isEmpty()
            || lblIdParto.getText().equals("jLabel9")) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Primero busque el parto que desea actualizar.",
                "Sin selección", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
            "¿Desea actualizar el parto con ID " + lblIdParto.getText() + "?",
            "Confirmar actualización",
            javax.swing.JOptionPane.YES_NO_OPTION);

    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

    try {
        int idParto = Integer.parseInt(lblIdParto.getText().trim());
        int idMadre = Integer.parseInt(txtIdMadre.getText().trim());

        java.sql.Date fechaParto = convertirFechaFlexible(txtFechaParto.getText().trim());

        // ====================================================================
        // 🛠️ ¡AQUÍ ESTÁ EL CAMBIO! MAPEO DE LOS VALORES PARA COMPATIBILIDAD ENUM
        // ====================================================================
        
        // 1. Mapear tipo_parto ('eutocico', 'distocico_asistido', 'cesarea')
        String tipoPartoSel = cboTipoParto.getSelectedItem().toString().toLowerCase();
        String tipoParto = "eutocico"; // Valor por defecto
        if (tipoPartoSel.contains("distocico") || tipoPartoSel.contains("asistido")) {
            tipoParto = "distocico_asistido";
        } else if (tipoPartoSel.contains("cesarea")) {
            tipoParto = "cesarea";
        }

        // 2. Mapear duracion_trabajo ('normal', 'prolongado', 'muy_prolongado')
        double duracionNum = txtDuracion.getText().trim().isEmpty() 
                ? 0 : Double.parseDouble(txtDuracion.getText().trim());
        String duracionTrabajo = "normal"; // Valor por defecto
        if (duracionNum > 2 && duracionNum <= 4) {
            duracionTrabajo = "prolongado";
        } else if (duracionNum > 4) {
            duracionTrabajo = "muy_prolongado";
        }

        // 3. Mapear estado_vaca_posparto ('normal', 'con_complicaciones', 'critico', 'fallecida')
        String condicionSel = cboCondicion.getSelectedIndex() == 0 
                ? "normal" : cboCondicion.getSelectedItem().toString().toLowerCase();
        String condicion = "normal";
        if (condicionSel.contains("complicac")) condicion = "con_complicaciones";
        if (condicionSel.contains("critic"))     condicion = "critico";
        if (condicionSel.contains("fallecid") || condicionSel.contains("muert")) condicion = "fallecida";

        // 4. Mapear expulsion_placenta ('normal', 'retenida')
        String complicacionTexto = txtComplicacion.getText().trim().toLowerCase();
        String expulsionPlacenta = "normal";
        if (complicacionTexto.contains("retenida") || complicacionTexto.contains("si") || complicacionTexto.contains("no expulso")) {
            expulsionPlacenta = "retenida";
        }

        // 5. Mapear calostro_suministrado ('si_madre', 'si_banco', 'no')
        String estadoCriaSel = cboEstadoCria.getSelectedIndex() == 0 
                ? "no" : cboEstadoCria.getSelectedItem().toString().toLowerCase();
        String calostroSuministrado = "no";
        if (estadoCriaSel.contains("madre")) calostroSuministrado = "si_madre";
        if (estadoCriaSel.contains("banco")) calostroSuministrado = "si_banco";

        // Campos de texto plano y booleanos
        boolean asistencia = cboAsistencia.getSelectedItem().toString().equalsIgnoreCase("Sí");
        String veterinario = txtVeterinario.getText().trim();
        String observaciones = txtObservaciones.getText().trim();

        int cantidadCrias = txtCantidadCrias.getText().trim().isEmpty()
                ? 1 : Integer.parseInt(txtCantidadCrias.getText().trim());

        java.sql.Date fechaRevision = null;
        if (!txtFechaRevision.getText().trim().isEmpty()) {
            fechaRevision = convertirFechaFlexible(txtFechaRevision.getText().trim());
        }

        // Instancia de la clase de negocio
        Parto parto = new Parto();

        // Ejecución del método con las variables limpias y adaptadas a los ENUM
        boolean ok = parto.actualizar(
                idParto,                      // 1. id_parto
                idMadre,                      // 2. id_gestacion
                fechaParto,                   // 3. fecha_hora_parto
                tipoParto,                    // 4. tipo_parto ('eutocico', 'distocico_asistido', 'cesarea')
                duracionTrabajo,              // 5. duracion_trabajo ('normal', 'prolongado', etc.)
                condicion,                    // 6. estado_vaca_posparto
                expulsionPlacenta,            // 7. expulsion_placenta
                calostroSuministrado,         // 8. calostro_suministrado
                asistencia,                   // 9. asistencia_veterinaria (boolean)
                veterinario,                  // 10. medicamentos_aplicados
                observaciones                 // 11. observaciones
        );

        if (ok) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "✔ Parto actualizado correctamente.",
                    "Actualizado", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el parto.",
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Verifique que ID, duración y cantidad sean números válidos.",
                "Dato inválido", javax.swing.JOptionPane.ERROR_MESSAGE);
    } catch (java.text.ParseException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Formato de fecha no reconocido. Use dd/MM/yyyy o yyyy-MM-dd.",
                "Fecha inválida", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
                                             
  // --- Validar campos obligatorios ---
    if (txtIdMadre.getText().trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo ID MADRE es obligatorio.", "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
        txtIdMadre.requestFocus();
        return;
    }
    if (txtFechaParto.getText().trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "El campo FECHA PARTO es obligatorio.", "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
        txtFechaParto.requestFocus();
        return;
    }
    if (cboTipoParto.getSelectedIndex() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Seleccione el TIPO DE PARTO.", "Campo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    // --- Parsear y convertir valores ---
    try {
        int idMadre = Integer.parseInt(txtIdMadre.getText().trim());
        java.sql.Date fechaParto = convertirFechaFlexible(txtFechaParto.getText().trim());

        // ====================================================================
        // 🔒 SOLUCIÓN INTACTA: ASIGNACIÓN DIRECTA POR ÍNDICE DEL COMBO
        // ====================================================================
        String tipoParto = "eutocico"; 
        int indexTipo = cboTipoParto.getSelectedIndex();
        if (indexTipo == 1) {
            tipoParto = "eutocico";
        } else if (indexTipo == 2) {
            tipoParto = "distocico_asistido";
        } else if (indexTipo == 3) {
            tipoParto = "cesarea";
        }

        // 2. Duración del trabajo
        double duracionNum = txtDuracion.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtDuracion.getText().trim());
        String duracionTrabajo = "normal"; 
        if (duracionNum > 2 && duracionNum <= 4) duracionTrabajo = "prolongado";
        else if (duracionNum > 4) duracionTrabajo = "muy_prolongado";

        // 3. Estado de la vaca posparto
        String condicion = "normal";
        int indexCondicion = cboCondicion.getSelectedIndex();
        if (indexCondicion == 1) condicion = "normal";
        else if (indexCondicion == 2) condicion = "con_complicaciones";
        else if (indexCondicion == 3) condicion = "critico";
        else if (indexCondicion == 4) condicion = "fallecida";

        // 4. Expulsión de placenta
        String complicacionTexto = txtComplicacion.getText().trim().toLowerCase();
        String expulsionPlacenta = "normal";
        if (complicacionTexto.contains("retenida") || complicacionTexto.contains("si")) {
            expulsionPlacenta = "retenida";
        }

        // 5. Calostro suministrado
        String calostroSuministrado = "no";
        int indexCalostro = cboEstadoCria.getSelectedIndex(); 
        if (indexCalostro == 1) calostroSuministrado = "si_madre";
        else if (indexCalostro == 2) calostroSuministrado = "si_banco";

        // Variables restantes
        boolean asistencia = cboAsistencia.getSelectedItem().toString().equalsIgnoreCase("Sí");
        String veterinario = txtVeterinario.getText().trim();
        String observaciones = txtObservaciones.getText().trim();

        // Llamada limpia a la clase Parto
        Parto parto = new Parto();
        boolean ok = parto.guardar(
                idMadre,                // 1
                fechaParto,             // 2
                tipoParto,              // 3 <- Aquí va estrictamente 'eutocico', 'distocico_asistido' o 'cesarea'
                duracionTrabajo,        // 4
                condicion,              // 5
                expulsionPlacenta,      // 6
                calostroSuministrado,   // 7
                asistencia,             // 8
                veterinario,            // 9
                observaciones           // 10
        );

        if (ok) {
            javax.swing.JOptionPane.showMessageDialog(this, "✔ Parto registrado exitosamente.", "Guardado", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo guardar el parto en la Base de Datos.", "Error al guardar", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "ID MADRE y DURACIÓN deben ser numéricos.", "Dato inválido", javax.swing.JOptionPane.ERROR_MESSAGE);
    } catch (java.text.ParseException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Formato de fecha no reconocido.", "Fecha inválida", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnguardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarMadre;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JComboBox<String> cboAsistencia;
    private javax.swing.JComboBox<String> cboCondicion;
    private javax.swing.JComboBox<String> cboEstadoCria;
    private javax.swing.JComboBox<String> cboTipoParto;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCantidadCrias;
    private javax.swing.JLabel lblComplicacion;
    private javax.swing.JLabel lblDuracion;
    private javax.swing.JLabel lblFechaParto;
    private javax.swing.JLabel lblFechaRevision;
    private javax.swing.JLabel lblIdMadre;
    private javax.swing.JLabel lblIdParto;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblVeterinario;
    private javax.swing.JPanel panel;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JTextField txtCantidadCrias;
    private javax.swing.JTextField txtComplicacion;
    private javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtFechaParto;
    private javax.swing.JTextField txtIdMadre;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtVeterinario;
    // End of variables declaration//GEN-END:variables

}
