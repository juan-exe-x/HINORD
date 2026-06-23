package interfaz;

import clases.Animal;
import clases.Gestacion;
import clases.LoteSemen;
import dao.AnimalDAO;
import dao.GestacionDAO;
import dao.LoteSemenDAO;
import estilos.EstiloFormularioBase;
import estilos.TemaFinca;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class mdiGestacion extends javax.swing.JInternalFrame {

    private final AnimalDAO animalDAO = new AnimalDAO();
    private final GestacionDAO gestacionDAO = new GestacionDAO();
    private final LoteSemenDAO loteDAO = new LoteSemenDAO();

    private JSpinner spnNumeroServicio;

    public mdiGestacion() {
        initComponents();

        cargarVacas();
        cargarToros();
        cargarLotes();

        cargarTabla();
        cargarDonantes();

        // ── Cargar opciones fijas de tipo de fecundación ─────────────────────
        cargarTipoFecundacion();

        // ── Cargar opciones fijas de tipo de confirmación ────────────────────
        cargarTipoConfirmacion();

        // ── Estado inicial de combos dependientes ────────────────────────────
        actualizarCombosSegunTipo();

        // ── Listener: al cambiar tipo de fecundación, ajustar combos ─────────
        cmbTipoFecundacion.addActionListener(e -> actualizarCombosSegunTipo());

        // ── Listener: calcular fecha parto al escribir fecha servicio ─────────
        txtFechaServicio.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularFechaParto();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularFechaParto();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularFechaParto();
            }
        });

        cargarTabla();
        aplicarEstilo();

        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);

    }

    private void cargarTipoFecundacion() {
        cmbTipoFecundacion.removeAllItems();
        cmbTipoFecundacion.addItem("Monta Natural");
        cmbTipoFecundacion.addItem("Inseminacion Artificial");
        cmbTipoFecundacion.addItem("Transferencia de Embriones");
    }

    private void cargarTipoConfirmacion() {
        cmbTipoConfirmacion.removeAllItems();
        cmbTipoConfirmacion.addItem("");                    // opción vacía
        cmbTipoConfirmacion.addItem("Ecografia");
        cmbTipoConfirmacion.addItem("Tacto Rectal");
        cmbTipoConfirmacion.addItem("Observacion Visual");
        cmbTipoConfirmacion.addItem("No Gestante");
    }

    // =========================================================================
    // LÓGICA HABILITAR / DESHABILITAR COMBOS SEGÚN TIPO DE FECUNDACIÓN
    // =========================================================================
    private void actualizarCombosSegunTipo() {
        String tipo = obtenerTipoSeleccionado();

        switch (tipo) {
            case "Monta Natural":
                // Solo el toro aplica
                habilitarCombo(cmbToro, true);
                habilitarCombo(cmbLoteSemen, false);
                habilitarCombo(cmbDonante, false);
                break;

            case "Inseminacion Artificial":
                // Solo el lote de semen aplica
                habilitarCombo(cmbToro, false);
                habilitarCombo(cmbLoteSemen, true);
                habilitarCombo(cmbDonante, false);
                break;

            case "Transferencia de Embriones":
                // Aplica lote de semen (del toro donante) + vaca donante
                habilitarCombo(cmbToro, false);
                habilitarCombo(cmbLoteSemen, true);
                habilitarCombo(cmbDonante, true);
                break;

            default:
                // Sin tipo seleccionado: todo deshabilitado
                habilitarCombo(cmbToro, false);
                habilitarCombo(cmbLoteSemen, false);
                habilitarCombo(cmbDonante, false);
        }
    }

    /**
     * Habilita o deshabilita un combo y cambia su color para que quede
     * visualmente gris.
     */
    private void habilitarCombo(javax.swing.JComboBox<?> combo, boolean habilitar) {
        combo.setEnabled(habilitar);
        combo.setBackground(habilitar
                ? java.awt.Color.WHITE
                : new java.awt.Color(220, 220, 220));   // gris cuando deshabilitado
    }

    /**
     * Devuelve el tipo de fecundación seleccionado como String seguro.
     */
    private String obtenerTipoSeleccionado() {
        Object sel = cmbTipoFecundacion.getSelectedItem();
        return sel != null ? sel.toString() : "";
    }

    // =========================================================================
    // CÁLCULO AUTOMÁTICO DE FECHA DE PARTO (283 días bovinos)
    // =========================================================================
    private void calcularFechaParto() {
        String texto = txtFechaServicio.getText().trim();
        if (texto.length() == 10) {   // AAAA-MM-DD tiene 10 caracteres
            try {
                java.time.LocalDate fechaServicio = java.time.LocalDate.parse(texto);
                java.time.LocalDate fechaParto = fechaServicio.plusDays(283);
                txtFechaPartoEstimada.setText(fechaParto.toString());
            } catch (java.time.format.DateTimeParseException ex) {
                txtFechaPartoEstimada.setText("");
            }
        } else {
            txtFechaPartoEstimada.setText("");
        }
    }

    private void aplicarEstilo() {

        getContentPane().removeAll();

        getContentPane().setBackground(TemaFinca.VERDE_CLARO);
        getContentPane().setLayout(null);

        setSize(980, 880);

        // ── Header ───────────────────────────────────────────────────────────
        JPanel header = new JPanel(null) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                try {
                    java.awt.Image img = new javax.swing.ImageIcon(
                            getClass().getResource("/Imagenes/vaca (4).png")).getImage();
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
                    int imgW = img.getWidth(this), imgH = img.getHeight(this);
                    double esc = Math.min((double) getWidth() / imgW, (double) getHeight() / imgH);
                    int nw = (int) (imgW * esc), nh = (int) (imgH * esc);
                    g2.setComposite(java.awt.AlphaComposite.getInstance(
                            java.awt.AlphaComposite.SRC_OVER, 0.12f));
                    g2.drawImage(img, (getWidth() - nw) / 2, (getHeight() - nh) / 2, nw, nh, this);
                    g2.setComposite(java.awt.AlphaComposite.getInstance(
                            java.awt.AlphaComposite.SRC_OVER, 1f));
                } catch (Exception ignored) {
                }
            }
        };
        header.setBackground(TemaFinca.VERDE_OSCURO);
        header.setBounds(0, 0, 980, 80);

        JLabel titulo = new JLabel("MÓDULO DE GESTACIÓN Y REPRODUCCIÓN",
                javax.swing.SwingConstants.CENTER);
        titulo.setFont(TemaFinca.FUENTE_TITULO);
        titulo.setForeground(TemaFinca.DORADO);
        titulo.setBounds(0, 18, 980, 44);
        header.add(titulo);
        add(header);

        // ── Panel blanco principal ────────────────────────────────────────────
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBounds(35, 100, 910, 730);
        add(panel);

        // ── Posiciones ───────────────────────────────────────────────────────
        int x1 = 40, x2 = 470;
        int y = 25, espacioY = 65;

        // Fila 1: Vaca + Toro
        estiloCombo(panel, "VACA (RECEPTORA / MADRE)", cmbVaca, x1, y);
        estiloCombo(panel, "TORO (PADRE)", cmbToro, x2, y);
        y += espacioY;

        // Fila 2: Tipo Fecundación + Lote Semen
        estiloCombo(panel, "TIPO DE FECUNDACIÓN", cmbTipoFecundacion, x1, y);
        estiloCombo(panel, "LOTE DE SEMEN ASOCIADO", cmbLoteSemen, x2, y);
        y += espacioY;

        // Fila 3: Vaca Donante + Tipo Confirmación
        estiloCombo(panel, "VACA DONANTE (SI APLICA)", cmbDonante, x1, y);
        estiloCombo(panel, "ESTADO / TIPO DE CONFIRMACIÓN", cmbTipoConfirmacion, x2, y);
        y += espacioY;

        // Fila 4: Fecha Servicio + Fecha Parto
        estiloCampo(panel, new JLabel(), txtFechaServicio, "FECHA DE SERVICIO (AAAA-MM-DD)", x1, y);
        estiloCampo(panel, new JLabel(), txtFechaPartoEstimada, "FECHA DE PARTO ESTIMADA (auto)", x2, y);
        // La fecha de parto se llena sola; solo lectura
        txtFechaPartoEstimada.setEditable(false);
        txtFechaPartoEstimada.setBackground(new java.awt.Color(240, 240, 240));
        y += espacioY;

        // Fila 5: N° Servicio (spinner) — izquierda; derecha vacía
        JLabel lblNro = new JLabel("N° DE SERVICIO (INTENTO)");
        lblNro.setFont(TemaFinca.FUENTE_LABEL);
        lblNro.setForeground(TemaFinca.GRIS_TEXTO);
        lblNro.setBounds(x1, y, 350, 20);

        spnNumeroServicio = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        spnNumeroServicio.setBounds(x1, y + 20, 160, 28);
        spnNumeroServicio.setFont(TemaFinca.FUENTE_INPUT);

        panel.add(lblNro);
        panel.add(spnNumeroServicio);
        y += espacioY;

        // Fila 6: Observaciones (ancho completo)
        estiloCampoGrande(panel, new JLabel(), txtObservaciones, "OBSERVACIONES GENERALES", x1, y);
        y += 75;

        // ── Tabla ────────────────────────────────────────────────────────────
        jScrollPane1.setBounds(40, y, 830, 160);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(
                new java.awt.Color(220, 220, 220)));
        jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);

        tblGestaciones.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tblGestaciones.setRowHeight(28);
        tblGestaciones.setGridColor(new java.awt.Color(240, 240, 240));
        tblGestaciones.getTableHeader().setFont(
                new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        tblGestaciones.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
        tblGestaciones.getTableHeader().setForeground(java.awt.Color.WHITE);
        tblGestaciones.getTableHeader().setReorderingAllowed(false);
        panel.add(jScrollPane1);

        y += 180;

        // ── Botones ──────────────────────────────────────────────────────────
        btnnuevo.setText("NUEVO");
        btnguardar.setText("GUARDAR");
        btnactualizar.setText("ACTUALIZAR");
        btnconfirmar.setText("CONFIRMAR");

        try {
            btnnuevo.setIcon(new ImageIcon(getClass().getResource("/imagenes/buscar.png")));
            btnguardar.setIcon(new ImageIcon(getClass().getResource("/imagenes/expediente.png")));
            btnactualizar.setIcon(new ImageIcon(getClass().getResource("/imagenes/actualizarr.png")));
            btnconfirmar.setIcon(new ImageIcon(getClass().getResource("/imagenes/papelera.png")));
        } catch (Exception ignored) {
        }

        int btnW = 160, btnH = 40, btnGap = 25;
        int totalW = 4 * btnW + 3 * btnGap;
        int startX = (910 - totalW) / 2;

        btnnuevo.setBounds(startX, y, btnW, btnH);
        btnguardar.setBounds(startX + (btnW + btnGap), y, btnW, btnH);
        btnactualizar.setBounds(startX + 2 * (btnW + btnGap), y, btnW, btnH);
        btnconfirmar.setBounds(startX + 3 * (btnW + btnGap), y, btnW, btnH);

        EstiloFormularioBase.estilizarBoton(btnnuevo, TemaFinca.VERDE_OSCURO, null);
        EstiloFormularioBase.estilizarBoton(btnguardar, TemaFinca.BTN_GUARDAR, null);
        EstiloFormularioBase.estilizarBoton(btnactualizar, TemaFinca.BTN_ACTUALIZAR, null);
        EstiloFormularioBase.estilizarBoton(btnconfirmar, TemaFinca.BTN_GUARDAR, null);

        panel.add(btnnuevo);
        panel.add(btnguardar);
        panel.add(btnactualizar);
        panel.add(btnconfirmar);

        validate();
        repaint();
        panel.revalidate();
        panel.repaint();

        revalidate();
        repaint();
    }

    // =========================================================================
    // MÉTODOS DE ESTILO
    // =========================================================================
    private void estiloCombo(JPanel panel, String texto,
            javax.swing.JComboBox<?> combo, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(TemaFinca.FUENTE_LABEL);
        lbl.setForeground(TemaFinca.GRIS_TEXTO);
        lbl.setBounds(x, y, 350, 20);
        combo.setBounds(x, y + 20, 350, 30);
        combo.setFont(TemaFinca.FUENTE_INPUT);
        combo.setBackground(java.awt.Color.WHITE);
        combo.setForeground(new java.awt.Color(50, 50, 50));
        panel.add(lbl);
        panel.add(combo);
    }

    private void estiloCampo(JPanel panel, JLabel label, javax.swing.JTextField txt,
            String texto, int x, int y) {
        label.setText(texto);
        label.setFont(TemaFinca.FUENTE_LABEL);
        label.setForeground(TemaFinca.GRIS_TEXTO);
        label.setBounds(x, y, 350, 20);
        txt.setBounds(x, y + 20, 350, 28);
        txt.setFont(TemaFinca.FUENTE_INPUT);
        txt.setBorder(TemaFinca.bordeCampo());
        panel.add(label);
        panel.add(txt);
    }

    private void estiloCampoGrande(JPanel panel, JLabel label, javax.swing.JTextField txt,
            String texto, int x, int y) {
        label.setText(texto);
        label.setFont(TemaFinca.FUENTE_LABEL);
        label.setForeground(TemaFinca.GRIS_TEXTO);
        label.setBounds(x, y, 300, 20);
        txt.setBounds(x, y + 20, 830, 28);
        txt.setFont(TemaFinca.FUENTE_INPUT);
        txt.setBorder(TemaFinca.bordeCampo());
        panel.add(label);
        panel.add(txt);
    }

    private void cargarVacas() {
        cmbVaca.removeAllItems();
        for (Animal a : animalDAO.obtenerVacasActivas()) {
            cmbVaca.addItem(a);
        }
    }

    private void cargarDonantes() {
           cmbDonante.removeAllItems();
    cmbDonante.addItem(null);

    for (Animal a : animalDAO.obtenerVacasActivas()) {
        cmbDonante.addItem(a);
    }
    }

    private void cargarToros() {
        cmbToro.removeAllItems();

        for (Animal a : animalDAO.obtenerTorosActivos()) {
            cmbToro.addItem(a);
        }

    }

    private void cargarLotes() {
        cmbLoteSemen.removeAllItems();

    for (LoteSemen l : loteDAO.obtenerLotesDisponibles()) {
        cmbLoteSemen.addItem(l);
    }
    }

    private void cargarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tblGestaciones.getModel();
        modelo.setRowCount(0);
        for (Gestacion g : gestacionDAO.obtenerActivas()) {
            modelo.addRow(new Object[]{
                g.getIdGestacion(),
                g.getIdicaVaca(),
                g.getNombreVaca(),
                g.getTipoFecundacion(),
                g.getOrigenGenetico(),
                g.getFechaServicio(),
                g.getFechaPartoEstimada(),
                g.getDiasParaParto(),
                g.getAlerta(),
                g.getEstado()
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbVaca = new javax.swing.JComboBox<>();
        cmbTipoFecundacion = new javax.swing.JComboBox<>();
        cmbToro = new javax.swing.JComboBox<>();
        cmbLoteSemen = new javax.swing.JComboBox<>();
        cmbDonante = new javax.swing.JComboBox<>();
        cmbTipoConfirmacion = new javax.swing.JComboBox<>();
        txtFechaServicio = new javax.swing.JTextField();
        txtFechaPartoEstimada = new javax.swing.JTextField();
        txtObservaciones = new javax.swing.JTextField();
        btnguardar = new javax.swing.JButton();
        btnnuevo = new javax.swing.JButton();
        btnconfirmar = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGestaciones = new javax.swing.JTable();
        panel = new javax.swing.JPanel();

        txtFechaServicio.setText("jTextField1");

        txtFechaPartoEstimada.setText("jTextField2");

        txtObservaciones.setText("jTextField3");

        btnguardar.setText("jButton1");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btnnuevo.setText("jButton2");
        btnnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnuevoActionPerformed(evt);
            }
        });

        btnconfirmar.setText("jButton3");
        btnconfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnconfirmarActionPerformed(evt);
            }
        });

        btnactualizar.setText("jButton1");
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });

        tblGestaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "IDICA", "VACA", "TIPO", "ORIGEN", "SERVICIO", "PARTO ESTIMADO", "DIAS", "ALERTA ", "ESTADO"
            }
        ));
        tblGestaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGestacionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGestaciones);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbDonante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnnuevo))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbLoteSemen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnguardar))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbToro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbTipoFecundacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtFechaPartoEstimada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbVaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtFechaServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbTipoConfirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnactualizar)
                                    .addComponent(btnconfirmar))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbVaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTipoFecundacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaPartoEstimada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbToro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbLoteSemen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnguardar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbDonante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnnuevo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTipoConfirmacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnconfirmar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnactualizar)
                        .addGap(46, 46, 46)
                        .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(133, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        try {
            String fechaTxt = txtFechaServicio.getText().trim();
            if (fechaTxt.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Debe ingresar la fecha de servicio (AAAA-MM-DD).");
                return;
            }
            if (cmbVaca.getSelectedItem() == null) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una vaca.");
                return;
            }

            java.time.LocalDate fechaServicio;
            try {
                fechaServicio = java.time.LocalDate.parse(fechaTxt);
            } catch (java.time.format.DateTimeParseException e) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Formato de fecha inválido. Use: AAAA-MM-DD (Ej: 2026-05-28).");
                return;
            }

            Animal vaca = (Animal) cmbVaca.getSelectedItem();
            String tipoFecundacion = obtenerTipoSeleccionado();

            Gestacion g = new Gestacion();
            g.setIdVaca(vaca.getIdregistro());
            g.setTipoFecundacion(tipoFecundacion);
            g.setFechaServicio(fechaServicio);
            g.setObservaciones(txtObservaciones.getText().trim());
            g.setNumeroServicio((int) spnNumeroServicio.getValue());

            // Asignar solo el campo que aplica según tipo
            switch (tipoFecundacion) {
                case "Monta Natural":
                    if (cmbToro.getSelectedItem() != null) {
                        g.setIdToro(((Animal) cmbToro.getSelectedItem()).getIdregistro());
                    }
                    break;
                case "Inseminacion Artificial":
                    if (cmbLoteSemen.getSelectedItem() != null) {
                        g.setIdLoteSemen(((LoteSemen) cmbLoteSemen.getSelectedItem()).getIdLote());
                    }
                    break;
                case "Transferencia de Embriones":
                    if (cmbLoteSemen.getSelectedItem() != null) {
                        g.setIdLoteSemen(((LoteSemen) cmbLoteSemen.getSelectedItem()).getIdLote());
                    }
                    if (cmbDonante.getSelectedItem() != null) {
                        g.setIdDonante(((Animal) cmbDonante.getSelectedItem()).getIdregistro());
                    }
                    break;
            }

            boolean insertado = gestacionDAO.insertar(g);

            if (insertado) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Gestación registrada correctamente.\n"
                        + "N° de servicio: " + g.getNumeroServicio()
                        + "   |   Parto estimado: " + g.getFechaPartoEstimada());

                if ("Inseminacion Artificial".equals(tipoFecundacion)
                        && g.getIdLoteSemen() != null) {
                    loteDAO.descontarDosis(g.getIdLoteSemen());
                    cargarLotes();
                }

                cargarTabla();
                btnnuevoActionPerformed(evt);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la gestación en la base de datos.");
            }

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error en la operación: " + e.getMessage());
        }

    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnuevoActionPerformed
        if (cmbVaca.getItemCount() > 0) {
            cmbVaca.setSelectedIndex(0);
        }
        if (cmbToro.getItemCount() > 0) {
            cmbToro.setSelectedIndex(0);
        }
        if (cmbLoteSemen.getItemCount() > 0) {
            cmbLoteSemen.setSelectedIndex(0);
        }
        if (cmbDonante.getItemCount() > 0) {
            cmbDonante.setSelectedIndex(0);
        }
        if (cmbTipoFecundacion.getItemCount() > 0) {
            cmbTipoFecundacion.setSelectedIndex(0);
        }
        if (cmbTipoConfirmacion.getItemCount() > 0) {
            cmbTipoConfirmacion.setSelectedIndex(0);
        }

        txtFechaServicio.setText("");
        txtFechaPartoEstimada.setText("");
        txtObservaciones.setText("");
        if (spnNumeroServicio != null) {
            spnNumeroServicio.setValue(1);
        }

        tblGestaciones.clearSelection();
        actualizarCombosSegunTipo();
    }//GEN-LAST:event_btnnuevoActionPerformed

    private void btnconfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconfirmarActionPerformed
        int fila = tblGestaciones.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Seleccione una fila de la tabla para confirmar.");
            return;
        }

        Object tipoSel = cmbTipoConfirmacion.getSelectedItem();
        if (tipoSel == null || tipoSel.toString().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Seleccione el tipo de confirmación antes de confirmar.");
            return;
        }

        try {
            int idGestacion = Integer.parseInt(tblGestaciones.getValueAt(fila, 0).toString());
            String tipoConf = tipoSel.toString();
            java.time.LocalDate hoy = java.time.LocalDate.now();
            String nuevoEstado = "No Gestante".equals(tipoConf) ? "no_gestante" : "gestante_confirmada";

            boolean exito = gestacionDAO.confirmar(idGestacion, nuevoEstado, tipoConf, hoy);

            if (exito) {
                javax.swing.JOptionPane.showMessageDialog(this, "Gestación confirmada con éxito.");
                cargarTabla();
                btnnuevoActionPerformed(evt);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo actualizar la confirmación.");
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al confirmar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnconfirmarActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        int fila = tblGestaciones.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Seleccione un registro en la tabla para modificar.");
            return;
        }

        try {
            int idGestacion = Integer.parseInt(tblGestaciones.getValueAt(fila, 0).toString());
            Animal vaca = (Animal) cmbVaca.getSelectedItem();
            java.time.LocalDate nuevaFecha
                    = java.time.LocalDate.parse(txtFechaServicio.getText().trim());

            Gestacion g = new Gestacion();
            g.setIdGestacion(idGestacion);
            g.setIdVaca(vaca.getIdregistro());
            g.setTipoFecundacion(obtenerTipoSeleccionado());
            g.setFechaServicio(nuevaFecha);
            g.setObservaciones(txtObservaciones.getText().trim());
            g.setNumeroServicio((int) spnNumeroServicio.getValue());

            // Ids condicionales según tipo
            switch (obtenerTipoSeleccionado()) {
                case "Monta Natural":
                    if (cmbToro.getSelectedItem() != null) {
                        g.setIdToro(((Animal) cmbToro.getSelectedItem()).getIdregistro());
                    }
                    break;
                case "Inseminacion Artificial":
                    if (cmbLoteSemen.getSelectedItem() != null) {
                        g.setIdLoteSemen(((LoteSemen) cmbLoteSemen.getSelectedItem()).getIdLote());
                    }
                    break;
                case "Transferencia de Embriones":
                    if (cmbLoteSemen.getSelectedItem() != null) {
                        g.setIdLoteSemen(((LoteSemen) cmbLoteSemen.getSelectedItem()).getIdLote());
                    }
                    if (cmbDonante.getSelectedItem() != null) {
                        g.setIdDonante(((Animal) cmbDonante.getSelectedItem()).getIdregistro());
                    }
                    break;
            }

            if (gestacionDAO.actualizar(g)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Registro modificado exitosamente.");
                cargarTabla();
                btnnuevoActionPerformed(evt);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "No se pudo actualizar el registro.");
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de datos: " + e.getMessage());
        }
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void tblGestacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGestacionesMouseClicked
        int fila = tblGestaciones.getSelectedRow();
        if (fila == -1) {
            return;
        }

        int idGestacion = Integer.parseInt(tblGestaciones.getValueAt(fila, 0).toString());
        Gestacion g = gestacionDAO.obtenerPorId(idGestacion);

        if (g != null) {
            txtFechaServicio.setText(g.getFechaServicio().toString());
            txtFechaPartoEstimada.setText(g.getFechaPartoEstimada().toString());
            txtObservaciones.setText(g.getObservaciones() != null ? g.getObservaciones() : "");

            // ── FIX: sincronizar cmbTipoFecundacion por texto ─────────────────
            cmbTipoFecundacion.setSelectedItem(g.getTipoFecundacion());
            actualizarCombosSegunTipo();

            // Número de servicio
            if (spnNumeroServicio != null) {
                spnNumeroServicio.setValue(Math.max(1, g.getNumeroServicio()));
            }

            // Vincular combo Vaca por ID
            for (int i = 0; i < cmbVaca.getItemCount(); i++) {
                if (cmbVaca.getItemAt(i).getIdregistro() == g.getIdVaca()) {
                    cmbVaca.setSelectedIndex(i);
                    break;
                }
            }

            // Vincular combo Toro por ID (si aplica)
            if (g.getIdToro() != null) {
                for (int i = 0; i < cmbToro.getItemCount(); i++) {
                    if (cmbToro.getItemAt(i).getIdregistro() == g.getIdToro()) {
                        cmbToro.setSelectedIndex(i);
                        break;
                    }
                }
            }

            // Vincular combo Donante por ID (si aplica)
            if (g.getIdDonante() != null) {
                for (int i = 0; i < cmbDonante.getItemCount(); i++) {
                    Animal a = cmbDonante.getItemAt(i);
                    if (a != null && a.getIdregistro() == g.getIdDonante()) {
                        cmbDonante.setSelectedIndex(i);
                        break;
                    }
                }
            }

            // Vincular combo LoteSemen por ID (si aplica)
            if (g.getIdLoteSemen() != null) {
                for (int i = 0; i < cmbLoteSemen.getItemCount(); i++) {
                    if (cmbLoteSemen.getItemAt(i).getIdLote() == g.getIdLoteSemen()) {
                        cmbLoteSemen.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }//GEN-LAST:event_tblGestacionesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnconfirmar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnnuevo;
    private javax.swing.JComboBox<clases.Animal> cmbDonante;
    private javax.swing.JComboBox<clases.LoteSemen> cmbLoteSemen;
    private javax.swing.JComboBox<String> cmbTipoConfirmacion;
    private javax.swing.JComboBox<String> cmbTipoFecundacion;
    private javax.swing.JComboBox<clases.Animal> cmbToro;
    private javax.swing.JComboBox<clases.Animal> cmbVaca;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel;
    private javax.swing.JTable tblGestaciones;
    private javax.swing.JTextField txtFechaPartoEstimada;
    private javax.swing.JTextField txtFechaServicio;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
