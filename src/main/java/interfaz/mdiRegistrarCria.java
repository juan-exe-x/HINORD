
package interfaz;
import clases.RegistroCria;
import clases.RegistroCriaDAO;
import clases.conexion;
import estilos.EstiloFormularioBase;
import estilos.TemaFinca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class mdiRegistrarCria extends javax.swing.JInternalFrame {
    
    // ── DAOs ──────────────────────────────────────────────────────────────────
    private final RegistroCriaDAO criaDAO = new RegistroCriaDAO();

    // ── Estado ────────────────────────────────────────────────────────────────
    private int idPartoSeleccionado = 0;
    private int idMadreSeleccionada = 0;
    private int idCriaSeleccionada  = 0;

    // ── Componentes ───────────────────────────────────────────────────────────
    private JPanel panel;
    private JScrollPane scrollObs;
    private JScrollPane jScrollPane1;

    private JComboBox<String> cmbPartosPendientes;
    private JComboBox<String> cmbSexoCria;
    private JComboBox<String> cmbCondicionNacimiento;

    private JTextField txtIdicaCria;
    private JTextField txtNombreCria;
    private JTextField txtRazaCria;
    private JTextField txtIdPadreOLote;
    private JTextField txtPesoNacimiento;

    private JTextArea txtObservaciones;

    private JLabel lblInfoParto;
    private JLabel lblInfoMadre;

    private JTable tblCrias;

    private JButton btnNuevo, btnGuardar, btnActualizar, btnEliminar;


    public mdiRegistrarCria() {
        setTitle("Registro de Crías");
        aplicarEstilo();
        cargarPartosPendientes();
        cargarTablaCrias();

        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);

        SwingUtilities.invokeLater(() -> { revalidate(); repaint(); });
    }
    
    // =========================================================================
    // CARGAR COMBO DE PARTOS
    // =========================================================================
    private void cargarPartosPendientes() {
        cmbPartosPendientes.removeAllItems();
        cmbPartosPendientes.addItem("-- Seleccione un parto --");

        String sql = "SELECT p.id_parto, r.nombre AS nombre_madre, r.idregistro AS id_madre, "
                   + "       p.fecha_hora_parto, p.tipo_parto "
                   + "FROM partos_reproductivo p "
                   + "JOIN gestacion g ON p.id_gestacion = g.id_gestacion "
                   + "JOIN registro r ON g.id_vaca = r.idregistro "
                   + "ORDER BY p.fecha_hora_parto DESC";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cmbPartosPendientes.addItem(
                    rs.getInt("id_parto")
                    + " | Madre: " + rs.getString("nombre_madre")
                    + " | Fecha: " + rs.getDate("fecha_hora_parto")
                    + " | " + rs.getString("tipo_parto"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar partos: " + e.getMessage());
        }
        cmbPartosPendientes.addActionListener(e -> rellenarDatosParto());
    }
    // =========================================================================
    // RELLENAR INFO AL SELECCIONAR PARTO
    // =========================================================================
    private void rellenarDatosParto() {
        int idx = cmbPartosPendientes.getSelectedIndex();
        if (idx <= 0) {
            lblInfoParto.setText("Parto: —");
            lblInfoMadre.setText("Madre: —");
            idPartoSeleccionado = 0;
            idMadreSeleccionada = 0;
            return;
        }
        String sel = cmbPartosPendientes.getSelectedItem().toString();
        try {
            idPartoSeleccionado = Integer.parseInt(sel.split("\\|")[0].trim());
        } catch (NumberFormatException ex) { return; }

        String sql = "SELECT p.id_parto, p.fecha_hora_parto, p.tipo_parto, "
                   + "       r.nombre, r.idregistro, r.raza, r.IDICA "
                   + "FROM partos_reproductivo p "
                   + "JOIN gestacion g ON p.id_gestacion = g.id_gestacion "
                   + "JOIN registro r ON g.id_vaca = r.idregistro "
                   + "WHERE p.id_parto = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPartoSeleccionado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idMadreSeleccionada = rs.getInt("idregistro");
                lblInfoMadre.setText("Madre: " + rs.getString("nombre")
                        + "   IDICA: " + rs.getString("IDICA")
                        + "   Raza: " + rs.getString("raza"));
                lblInfoParto.setText("Parto #" + rs.getInt("id_parto")
                        + "   Fecha: " + rs.getDate("fecha_hora_parto")
                        + "   Tipo: " + rs.getString("tipo_parto"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar info del parto: " + e.getMessage());
        }
    }


    // =========================================================================
    // CARGAR TABLA DE CRÍAS REGISTRADAS
    // =========================================================================
    private void cargarTablaCrias() {
        DefaultTableModel modelo = (DefaultTableModel) tblCrias.getModel();
        modelo.setRowCount(0);

        String sql = "SELECT c.id_cria, c.id_parto, rm.nombre AS nombre_madre, "
                   + "       rn.nombre AS nombre_cria, c.sexo_cria, "
                   + "       c.peso_nacimiento, c.condicion_nacimiento, "
                   + "       c.id_padre_o_lote, c.observaciones "
                   + "FROM cria c "
                   + "JOIN partos_reproductivo p ON c.id_parto  = p.id_parto "
                   + "JOIN gestacion g           ON p.id_gestacion = g.id_gestacion "
                   + "JOIN registro rm           ON g.id_vaca = rm.idregistro "
                   + "LEFT JOIN registro rn      ON c.id_animal_nuevo = rn.idregistro "
                   + "ORDER BY c.id_cria DESC";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_cria"),
                    rs.getInt("id_parto"),
                    rs.getString("nombre_madre"),
                    rs.getString("nombre_cria"),
                    rs.getString("sexo_cria"),
                    rs.getDouble("peso_nacimiento"),
                    rs.getString("condicion_nacimiento"),
                    rs.getString("id_padre_o_lote"),
                    rs.getString("observaciones")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar crías: " + e.getMessage());
        }
    }
    
    // =========================================================================
    // GUARDAR CRÍA
    // =========================================================================
    private void guardarCria() {
        if (idPartoSeleccionado == 0 || idMadreSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un parto de la lista.");
            return;
        }
        if (txtIdicaCria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el IDICA de la cría.");
            txtIdicaCria.requestFocus();
            return;
        }
        if (txtNombreCria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de la cría.");
            txtNombreCria.requestFocus();
            return;
        }
        if (txtRazaCria.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la raza de la cría.");
            txtRazaCria.requestFocus();
            return;
        }

        String sexo = cmbSexoCria.getSelectedItem().toString();
        String clasificacion = sexo.equalsIgnoreCase("MACHO") ? "Ternero" : "Ternera";

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Desea registrar esta cría en el sistema?\n\n"
            + "  IDICA:         " + txtIdicaCria.getText().trim() + "\n"
            + "  Nombre:        " + txtNombreCria.getText().trim() + "\n"
            + "  Raza:          " + txtRazaCria.getText().trim() + "\n"
            + "  Sexo:          " + sexo + "\n"
            + "  Clasificación: " + clasificacion + "\n\n"
            + "Se agregará automáticamente a la tabla de animales.",
            "Confirmar registro de cría",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            double peso = txtPesoNacimiento.getText().trim().isEmpty()
                    ? 0.0 : Double.parseDouble(txtPesoNacimiento.getText().trim());

            // ── 1. Obtener fecha del parto como fecha de nacimiento ───────────
            java.sql.Date fechaNac = null;
            String sqlFecha = "SELECT fecha_hora_parto FROM partos_reproductivo WHERE id_parto = ?";
            try (Connection con = conexion.conectar();
                 PreparedStatement ps = con.prepareStatement(sqlFecha)) {
                ps.setInt(1, idPartoSeleccionado);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    fechaNac = rs.getDate("fecha_hora_parto");
                }
            }

            if (fechaNac == null) {
                JOptionPane.showMessageDialog(this, "No se pudo obtener la fecha del parto.");
                return;
            }

            // ── 2. Calcular edad en meses ─────────────────────────────────────
            java.time.LocalDate hoy        = java.time.LocalDate.now();
            java.time.LocalDate nacimiento = fechaNac.toLocalDate();
            int edadMeses = (int) java.time.temporal.ChronoUnit.MONTHS.between(nacimiento, hoy);

            // ── 3. Insertar en tabla registro ─────────────────────────────────
            int idAnimalNuevo = -1;
            String sqlRegistro = "INSERT INTO registro "
                    + "(nombre, IDICA, fecnac, raza, sexo, edad, peso, clasificacion, estado_animal) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Activo')";

            try (Connection con = conexion.conectar();
                 PreparedStatement ps = con.prepareStatement(sqlRegistro,
                         PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, txtNombreCria.getText().trim());
                ps.setString(2, txtIdicaCria.getText().trim());
                ps.setDate(3, fechaNac);
                ps.setString(4, txtRazaCria.getText().trim());
                ps.setString(5, sexo);
                ps.setInt(6, edadMeses);
                ps.setDouble(7, peso);
                ps.setString(8, clasificacion);

                int filas = ps.executeUpdate();
                if (filas > 0) {
                    ResultSet keys = ps.getGeneratedKeys();
                    if (keys.next()) {
                        idAnimalNuevo = keys.getInt(1);
                    }
                }
            }

            if (idAnimalNuevo == -1) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el animal en el sistema.");
                return;
            }

            // ── 4. Insertar en tabla cria ─────────────────────────────────────
            RegistroCria cria = new RegistroCria();
            cria.setIdParto(idPartoSeleccionado);
            cria.setIdMadre(idMadreSeleccionada);
            cria.setIdAnimalNuevo(idAnimalNuevo);
            cria.setIdPadreOLote(txtIdPadreOLote.getText().trim());
            cria.setSexoCria(sexo);
            cria.setPesoNacimiento(peso);
            cria.setCondicionNacimiento(cmbCondicionNacimiento.getSelectedItem().toString());
            cria.setObservaciones(txtObservaciones.getText().trim());

            if (criaDAO.registrarCria(cria)) {
                JOptionPane.showMessageDialog(this,
                    "✔ Cría registrada correctamente.\n\n"
                    + "  ID en sistema:       " + idAnimalNuevo + "\n"
                    + "  IDICA:               " + txtIdicaCria.getText().trim() + "\n"
                    + "  Fecha de nacimiento: " + fechaNac + "\n"
                    + "  Edad actual:         " + edadMeses + " meses",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiar();
                cargarTablaCrias();
            } else {
                // Rollback manual: eliminar el registro recién creado
                String sqlRollback = "DELETE FROM registro WHERE idregistro = ?";
                try (Connection con = conexion.conectar();
                     PreparedStatement ps = con.prepareStatement(sqlRollback)) {
                    ps.setInt(1, idAnimalNuevo);
                    ps.executeUpdate();
                }
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la cría. Intente de nuevo.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El peso debe ser un valor numérico.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    
    // =========================================================================
    // ACTUALIZAR CRÍA SELECCIONADA EN TABLA
    // =========================================================================
    private void actualizarCria() {
        if (idCriaSeleccionada == 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una cría en la tabla para actualizar.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Actualizar la cría ID " + idCriaSeleccionada + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "UPDATE cria SET "
                   + "id_padre_o_lote=?, sexo_cria=?, peso_nacimiento=?, "
                   + "condicion_nacimiento=?, observaciones=? "
                   + "WHERE id_cria=?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtIdPadreOLote.getText().trim());
            ps.setString(2, cmbSexoCria.getSelectedItem().toString());
            ps.setDouble(3, txtPesoNacimiento.getText().trim().isEmpty()
                    ? 0.0 : Double.parseDouble(txtPesoNacimiento.getText().trim()));
            ps.setString(4, cmbCondicionNacimiento.getSelectedItem().toString());
            ps.setString(5, txtObservaciones.getText().trim());
            ps.setInt(6, idCriaSeleccionada);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "✔ Cría actualizada correctamente.");
                limpiar();
                cargarTablaCrias();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la cría.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    // =========================================================================
    // ELIMINAR CRÍA SELECCIONADA EN TABLA
    // =========================================================================
    private void eliminarCriaSeleccionada() {
        int fila = tblCrias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una cría de la tabla para eliminar.");
            return;
        }
        int idCria = Integer.parseInt(tblCrias.getValueAt(fila, 0).toString());
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la cría ID " + idCria + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM cria WHERE id_cria = ?";
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCria);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Cría eliminada.");
                cargarTablaCrias();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la cría.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    // =========================================================================
    // CARGAR FILA SELECCIONADA DE LA TABLA AL FORMULARIO
    // =========================================================================
    private void cargarFilaSeleccionada() {
        int fila = tblCrias.getSelectedRow();
        if (fila < 0) return;
        idCriaSeleccionada = Integer.parseInt(tblCrias.getValueAt(fila, 0).toString());
        txtIdPadreOLote.setText(tblCrias.getValueAt(fila, 7) != null
                ? tblCrias.getValueAt(fila, 7).toString() : "");
        String sexo = tblCrias.getValueAt(fila, 4).toString();
        cmbSexoCria.setSelectedItem(sexo);
        Object peso = tblCrias.getValueAt(fila, 5);
        txtPesoNacimiento.setText(peso != null ? peso.toString() : "");
        String cond = tblCrias.getValueAt(fila, 6).toString();
        cmbCondicionNacimiento.setSelectedItem(cond);
        txtObservaciones.setText(tblCrias.getValueAt(fila, 8) != null
                ? tblCrias.getValueAt(fila, 8).toString() : "");
    }


    // =========================================================================
    // LIMPIAR
    // =========================================================================
    private void limpiar() {
        cmbPartosPendientes.setSelectedIndex(0);
        txtIdicaCria.setText("");
        txtNombreCria.setText("");
        txtRazaCria.setText("");
        txtIdPadreOLote.setText("");
        txtPesoNacimiento.setText("");
        txtObservaciones.setText("");
        cmbSexoCria.setSelectedIndex(0);
        cmbCondicionNacimiento.setSelectedIndex(0);
        lblInfoParto.setText("Parto: —");
        lblInfoMadre.setText("Madre: —");
        idPartoSeleccionado = 0;
        idMadreSeleccionada = 0;
        idCriaSeleccionada  = 0;
        tblCrias.clearSelection();
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
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon(
                            getClass().getResource("/Imagenes/vaca (4).png")).getImage();
                    Graphics2D g2 = (Graphics2D) g;
                    int iw = img.getWidth(this), ih = img.getHeight(this);
                    double e = Math.min((double)getWidth()/iw, (double)getHeight()/ih);
                    int nw = (int)(iw*e), nh = (int)(ih*e);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                    g2.drawImage(img, (getWidth()-nw)/2, (getHeight()-nh)/2, nw, nh, this);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } catch (Exception ignored) {}
            }
        };
        header.setBackground(TemaFinca.VERDE_OSCURO);
        header.setBounds(0, 0, 980, 80);
        JLabel titulo = new JLabel("REGISTRO DE CRÍAS", SwingConstants.CENTER);
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

        // ── BLOQUE 1: Parto origen ────────────────────────────────────────────
        lbl(panel, "PARTO DE ORIGEN", x1, y);
        cmbPartosPendientes = combo(new String[]{});
        cmbPartosPendientes.setBounds(x1, y+20, 830, 30);
        panel.add(cmbPartosPendientes);
        y += 58;

        lblInfoMadre = new JLabel("Madre: —");
        lblInfoMadre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfoMadre.setForeground(TemaFinca.VERDE_OSCURO);
        lblInfoMadre.setBounds(x1, y, 830, 20);
        panel.add(lblInfoMadre);

        lblInfoParto = new JLabel("Parto: —");
        lblInfoParto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfoParto.setForeground(TemaFinca.GRIS_TEXTO);
        lblInfoParto.setBounds(x1, y+22, 830, 18);
        panel.add(lblInfoParto);
        y += 48;

        sep(panel, x1, y); y += 15;

        // Fila 1 — IDICA + Nombre
        lbl(panel, "IDICA DE LA CRÍA (nuevo)", x1, y);
        txtIdicaCria = campo(x1, y+20, 350);
        panel.add(txtIdicaCria);

        lbl(panel, "NOMBRE DE LA CRÍA", x2, y);
        txtNombreCria = campo(x2, y+20, 350);
        panel.add(txtNombreCria);
        y += gap;

        // Fila 2 — Raza + Padre/Lote
        lbl(panel, "RAZA DE LA CRÍA", x1, y);
        txtRazaCria = campo(x1, y+20, 350);
        panel.add(txtRazaCria);

        lbl(panel, "ID PADRE O LOTE (texto libre)", x2, y);
        txtIdPadreOLote = campo(x2, y+20, 350);
        panel.add(txtIdPadreOLote);
        y += gap;

        // Fila 3 — Sexo + Peso
        lbl(panel, "SEXO DE LA CRÍA", x1, y);
        cmbSexoCria = combo(new String[]{"MACHO", "HEMBRA"});
        setBounds(cmbSexoCria, x1, y+20, 350);

        lbl(panel, "PESO AL NACER (kg)", x2, y);
        txtPesoNacimiento = campo(x2, y+20, 350);
        panel.add(txtPesoNacimiento);
        y += gap;

        // Fila 4 — Condición nacimiento
        lbl(panel, "CONDICIÓN AL NACIMIENTO", x1, y);
        cmbCondicionNacimiento = combo(new String[]{
            "vivo_normal", "vivo_debil", "mortinato"});
        setBounds(cmbCondicionNacimiento, x1, y+20, 350);
        y += gap;

        // Observaciones
        lbl(panel, "OBSERVACIONES", x1, y);
        txtObservaciones = new JTextArea(3, 10);
        txtObservaciones.setFont(TemaFinca.FUENTE_INPUT);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setBounds(x1, y+20, 830, 55);
        scrollObs.setBorder(TemaFinca.bordeCampo());
        panel.add(scrollObs);
        y += 82;

        // ── Tabla historial ───────────────────────────────────────────────────
        lbl(panel, "HISTORIAL DE CRÍAS REGISTRADAS", x1, y);
        y += 20;

        tblCrias = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID Cría","ID Parto","Madre","Animal",
                             "Sexo","Peso (kg)","Condición","Padre/Lote","Observaciones"}));
        tblCrias.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblCrias.setRowHeight(26);
        tblCrias.setGridColor(new Color(240, 240, 240));
        tblCrias.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblCrias.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblCrias.getTableHeader().setForeground(Color.WHITE);
        tblCrias.getTableHeader().setReorderingAllowed(false);
        tblCrias.getSelectionModel().addListSelectionListener(e -> cargarFilaSeleccionada());

        jScrollPane1 = new JScrollPane(tblCrias);
        jScrollPane1.setBounds(x1, y, 830, 130);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(jScrollPane1);
        y += 148;

        // ── Botones ───────────────────────────────────────────────────────────
        btnNuevo      = new JButton("NUEVO");
        btnGuardar    = new JButton("GUARDAR");
        btnActualizar = new JButton("ACTUALIZAR");
        btnEliminar   = new JButton("ELIMINAR");

        try {
            btnNuevo.setIcon(new ImageIcon(getClass().getResource("/imagenes/buscar.png")));
            btnGuardar.setIcon(new ImageIcon(getClass().getResource("/imagenes/expediente.png")));
            btnActualizar.setIcon(new ImageIcon(getClass().getResource("/imagenes/actualizarr.png")));
            btnEliminar.setIcon(new ImageIcon(getClass().getResource("/imagenes/papelera.png")));
        } catch (Exception ignored) {}

        int bW = 185, bH = 42, bGap = 18;
        int totalW = 4*bW + 3*bGap;
        int sx = (910 - totalW) / 2;

        btnNuevo.setBounds(sx,                  y, bW, bH);
        btnGuardar.setBounds(sx+bW+bGap,        y, bW, bH);
        btnActualizar.setBounds(sx+2*(bW+bGap), y, bW, bH);
        btnEliminar.setBounds(sx+3*(bW+bGap),   y, bW, bH);

        EstiloFormularioBase.estilizarBoton(btnNuevo,      TemaFinca.VERDE_OSCURO,   null);
        EstiloFormularioBase.estilizarBoton(btnGuardar,    TemaFinca.BTN_GUARDAR,    null);
        EstiloFormularioBase.estilizarBoton(btnActualizar, TemaFinca.BTN_ACTUALIZAR, null);
        EstiloFormularioBase.estilizarBoton(btnEliminar,   new Color(180, 50, 50),   null);

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardarCria());
        btnActualizar.addActionListener(e -> actualizarCria());
        btnEliminar.addActionListener(e -> eliminarCriaSeleccionada());

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);

        y += bH + 20;

        // ── Scroll principal ──────────────────────────────────────────────────
        panel.setPreferredSize(new Dimension(910, y));
        JScrollPane scrollPrincipal = new JScrollPane(panel);
        scrollPrincipal.setBounds(35, 90, 910, 620);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPrincipal);

        revalidate(); repaint();
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
