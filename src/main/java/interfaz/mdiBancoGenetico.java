package interfaz;

import dao.LoteSemenDAO;
import clases.LoteSemen;
import estilos.EstiloFormularioBase;
import estilos.TemaFinca;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.time.LocalDate;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;



public class mdiBancoGenetico extends javax.swing.JInternalFrame {

    private final LoteSemenDAO loteDAO = new LoteSemenDAO();

    private DefaultTableModel modelo;

    private int idLoteSeleccionado = 0;

    public mdiBancoGenetico() {
        initComponents();

        inicializarTabla();
        cargarEstados();
        cargarTabla();

        spnDosisDisponibles.setModel(
                new javax.swing.SpinnerNumberModel(
                        0,
                        0,
                        100000,
                        1
                )
        );
        aplicarEstilo();

        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
    }
    
    private void aplicarEstilo() {

    getContentPane().removeAll();
    getContentPane().setBackground(TemaFinca.VERDE_CLARO);
    getContentPane().setLayout(null);

    setSize(980, 950);

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
            } catch (Exception ignored) {}
        }
    };
    header.setBackground(TemaFinca.VERDE_OSCURO);
    header.setBounds(0, 0, 980, 80);

    JLabel titulo = new JLabel("Banco Genetico", javax.swing.SwingConstants.CENTER);
    titulo.setFont(TemaFinca.FUENTE_TITULO);
    titulo.setForeground(TemaFinca.DORADO);
    titulo.setBounds(0, 18, 980, 44);
    header.add(titulo);
    add(header);

    // ── Panel blanco principal ────────────────────────────────────────────
    panel = new JPanel();
    panel.setLayout(null);
    panel.setBackground(java.awt.Color.WHITE);
    panel.setBounds(35, 100, 910, 800);
    add(panel);

    // ── Coordenadas base ─────────────────────────────────────────────────
    int x1 = 40, x2 = 470;
    int y = 25, espacioY = 65;

    // Fila 1: Código de Lote | Raza del Toro
    estiloCampo(panel, new JLabel(), txtCodigoLote, "CÓDIGO DE LOTE", x1, y);
    estiloCampo(panel, new JLabel(), txtRazaToro,   "RAZA DEL TORO",  x2, y);
    y += espacioY;

    // Fila 2: Nombre del Toro | Proveedor
    estiloCampo(panel, new JLabel(), txtNombreToro, "NOMBRE DEL TORO", x1, y);
    estiloCampo(panel, new JLabel(), txtProveedor,  "PROVEEDOR",        x2, y);
    y += espacioY;

    // Fila 3: Fecha de Ingreso | Estado
    estiloCampo(panel, new JLabel(), txtFechaIngreso, "FECHA DE INGRESO (YYYY-MM-DD)", x1, y);
    estiloCombo(panel, "ESTADO", cmbEstado, x2, y);
    y += espacioY;

    // Fila 4: Dosis disponibles (spinner) | N° de Servicio
    JLabel lblDosis = new JLabel("DOSIS DISPONIBLES");
    lblDosis.setFont(TemaFinca.FUENTE_LABEL);
    lblDosis.setForeground(TemaFinca.GRIS_TEXTO);
    lblDosis.setBounds(x1, y, 350, 20);
    spnDosisDisponibles = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
    spnDosisDisponibles.setBounds(x1, y + 20, 160, 28);
    spnDosisDisponibles.setFont(TemaFinca.FUENTE_INPUT);
    panel.add(lblDosis);
    panel.add(spnDosisDisponibles);

    JLabel lblNro = new JLabel("N° DE SERVICIO (INTENTO)");
    lblNro.setFont(TemaFinca.FUENTE_LABEL);
    lblNro.setForeground(TemaFinca.GRIS_TEXTO);
    lblNro.setBounds(x2, y, 350, 20);
    JSpinner spnServicio = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    spnServicio.setBounds(x2, y + 20, 160, 28);
    spnServicio.setFont(TemaFinca.FUENTE_INPUT);
    panel.add(lblNro);
    panel.add(spnServicio);
    y += espacioY;

    // Fila 5: Observaciones (ancho completo)
    JLabel lblObs = new JLabel("OBSERVACIONES");
    lblObs.setFont(TemaFinca.FUENTE_LABEL);
    lblObs.setForeground(TemaFinca.GRIS_TEXTO);
    lblObs.setBounds(x1, y, 300, 20);
    txtObservaciones.setFont(TemaFinca.FUENTE_INPUT);
    jScrollPane2.setBounds(x1, y + 20, 830, 60);
    jScrollPane2.setBorder(TemaFinca.bordeCampo());
    panel.add(lblObs);
    panel.add(jScrollPane2);
    y += 95;

    // ── Barra de búsqueda ────────────────────────────────────────────────
    JLabel lblBuscar = new JLabel("BUSCAR POR CÓDIGO");
    lblBuscar.setFont(TemaFinca.FUENTE_LABEL);
    lblBuscar.setForeground(TemaFinca.GRIS_TEXTO);
    lblBuscar.setBounds(x1, y, 250, 20);
    txtBuscar.setBounds(x1, y + 20, 280, 28);
    txtBuscar.setFont(TemaFinca.FUENTE_INPUT);
    txtBuscar.setBorder(TemaFinca.bordeCampo());

    btnBuscar.setText("BUSCAR");
    btnBuscar.setBounds(x1 + 290, y + 18, 110, 32);
    EstiloFormularioBase.estilizarBoton(btnBuscar, TemaFinca.VERDE_OSCURO, null);

    btnRefrescar.setText("REFRESCAR");
    btnRefrescar.setBounds(x1 + 415, y + 18, 120, 32);
    EstiloFormularioBase.estilizarBoton(btnRefrescar, new java.awt.Color(100, 100, 100), null);

    panel.add(lblBuscar);
    panel.add(txtBuscar);
    panel.add(btnBuscar);
    panel.add(btnRefrescar);
    y += 65;

    // ── Tabla ────────────────────────────────────────────────────────────
    jScrollPane1.setBounds(x1, y, 830, 175);
    jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(220, 220, 220)));
    jScrollPane1.getViewport().setBackground(java.awt.Color.WHITE);

    tblLotes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    tblLotes.setRowHeight(28);
    tblLotes.setGridColor(new java.awt.Color(240, 240, 240));
    tblLotes.getTableHeader().setFont(
            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    tblLotes.getTableHeader().setBackground(TemaFinca.VERDE_OSCURO);
    tblLotes.getTableHeader().setForeground(java.awt.Color.WHITE);
    tblLotes.getTableHeader().setReorderingAllowed(false);
    panel.add(jScrollPane1);
    y += 195;

    // ── Indicadores ──────────────────────────────────────────────────────
    lblTotalLotes.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    lblTotalLotes.setForeground(TemaFinca.VERDE_OSCURO);
    lblTotalLotes.setBounds(x1, y, 200, 22);

    lblTotalDosis.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    lblTotalDosis.setForeground(TemaFinca.VERDE_OSCURO);
    lblTotalDosis.setBounds(x1 + 210, y, 200, 22);

    lblStockBajo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    lblStockBajo.setForeground(new java.awt.Color(180, 50, 50));
    lblStockBajo.setBounds(x1 + 420, y, 200, 22);

    panel.add(lblTotalLotes);
    panel.add(lblTotalDosis);
    panel.add(lblStockBajo);
    y += 35;

    // ── Botones ──────────────────────────────────────────────────────────
    btnNuevo.setText("NUEVO");
    btnGuardar.setText("GUARDAR");
    btnActualizar.setText("ACTUALIZAR");
    btnEliminar.setText("ELIMINAR");

    try {
        btnNuevo.setIcon(new ImageIcon(getClass().getResource("/imagenes/buscar.png")));
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/imagenes/expediente.png")));
        btnActualizar.setIcon(new ImageIcon(getClass().getResource("/imagenes/actualizarr.png")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/imagenes/papelera.png")));
    } catch (Exception ignored) {}

    int btnW = 185, btnH = 42, btnGap = 18;
    int totalW = 4 * btnW + 3 * btnGap;
    int startX = (910 - totalW) / 2;

    btnNuevo.setBounds(startX,                       y, btnW, btnH);
    btnGuardar.setBounds(startX + (btnW + btnGap),   y, btnW, btnH);
    btnActualizar.setBounds(startX + 2*(btnW+btnGap),y, btnW, btnH);
    btnEliminar.setBounds(startX + 3*(btnW+btnGap),  y, btnW, btnH);

    EstiloFormularioBase.estilizarBoton(btnNuevo,     TemaFinca.VERDE_OSCURO,    null);
    EstiloFormularioBase.estilizarBoton(btnGuardar,   TemaFinca.BTN_GUARDAR,     null);
    EstiloFormularioBase.estilizarBoton(btnActualizar,TemaFinca.BTN_ACTUALIZAR,  null);
    EstiloFormularioBase.estilizarBoton(btnEliminar,  new java.awt.Color(180,50,50), null);

    panel.add(btnNuevo);
    panel.add(btnGuardar);
    panel.add(btnActualizar);
    panel.add(btnEliminar);

    revalidate();
    repaint();
    panel.revalidate();
    panel.repaint();
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

    private void inicializarTabla() {

        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Código");
        modelo.addColumn("Raza");
        modelo.addColumn("Toro");
        modelo.addColumn("Proveedor");
        modelo.addColumn("Fecha");
        modelo.addColumn("Dosis");
        modelo.addColumn("Estado");

        tblLotes.setModel(modelo);
    }

    private void cargarEstados() {

        cmbEstado.removeAllItems();

        cmbEstado.addItem("Activo");
        cmbEstado.addItem("Inactivo");
    }

    private void cargarTabla() {

        modelo.setRowCount(0);

        int totalLotes = 0;
        int totalDosis = 0;
        int stockBajo = 0;

        for (LoteSemen ls : loteDAO.obtenerTodosLosLotes()) {

            modelo.addRow(new Object[]{
                ls.getIdLote(),
                ls.getCodigoLote(),
                ls.getRazaToro(),
                ls.getNombreToro(),
                ls.getProveedor(),
                ls.getFechaIngreso(),
                ls.getDosisDisponibles(),
                ls.isActivo() ? "Activo" : "Inactivo"
            });

            totalLotes++;

            totalDosis += ls.getDosisDisponibles();

            if (ls.getDosisDisponibles() <= 10) {
                stockBajo++;
            }
        }

        lblTotalLotes.setText("Total lotes: " + totalLotes);
        lblTotalDosis.setText("Total dosis: " + totalDosis);
        lblStockBajo.setText("Stock bajo: " + stockBajo);
    }

    private void actualizarIndicadores() {

        lblTotalLotes.setText(
                "Total Lotes: "
                + loteDAO.obtenerTotalLotes());

        lblTotalDosis.setText(
                "Total Dosis: "
                + loteDAO.obtenerTotalDosis());

        lblStockBajo.setText(
                "Stock Bajo: "
                + loteDAO.obtenerStockBajo());
    }

    private void limpiar() {

        txtCodigoLote.setText("");
        txtRazaToro.setText("");
        txtNombreToro.setText("");
        txtProveedor.setText("");
        txtFechaIngreso.setText("");

        spnDosisDisponibles.setValue(0);

        cmbEstado.setSelectedIndex(0);

        idLoteSeleccionado = 0;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtCodigoLote = new javax.swing.JTextField();
        txtRazaToro = new javax.swing.JTextField();
        txtNombreToro = new javax.swing.JTextField();
        txtProveedor = new javax.swing.JTextField();
        txtFechaIngreso = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        spnDosisDisponibles = new javax.swing.JSpinner();
        cmbEstado = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLotes = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        lblTotalLotes = new javax.swing.JLabel();
        lblTotalDosis = new javax.swing.JLabel();
        lblStockBajo = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        panel = new javax.swing.JPanel();

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        tblLotes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Codigo", "Raza", "Toro", "Proveedor", "fecha ingreso", "Dosis ", "Estado"
            }
        ));
        tblLotes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLotesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLotes);

        txtObservaciones.setColumns(20);
        txtObservaciones.setRows(5);
        jScrollPane2.setViewportView(txtObservaciones);

        lblTotalLotes.setText("Total de Lotes");

        lblTotalDosis.setText("Total de  Dosis");

        lblStockBajo.setText("Stock Bajo");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnRefrescar.setText("Refrescar");
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

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
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBuscar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRefrescar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spnDosisDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreToro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRazaToro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalLotes)
                            .addComponent(lblTotalDosis)
                            .addComponent(lblStockBajo)
                            .addComponent(btnNuevo)
                            .addComponent(btnGuardar)
                            .addComponent(btnActualizar)
                            .addComponent(btnEliminar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(95, 95, 95))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(txtCodigoLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRazaToro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreToro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(spnDosisDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(lblTotalLotes)
                .addGap(18, 18, 18)
                .addComponent(lblTotalDosis)
                .addGap(18, 18, 18)
                .addComponent(lblStockBajo)
                .addGap(18, 18, 18)
                .addComponent(btnNuevo)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnActualizar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnRefrescar)
                .addGap(18, 18, 18)
                .addComponent(btnBuscar))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {

            LoteSemen ls = new LoteSemen();

            ls.setCodigoLote(txtCodigoLote.getText());
            ls.setRazaToro(txtRazaToro.getText());
            ls.setNombreToro(txtNombreToro.getText());
            ls.setProveedor(txtProveedor.getText());

            ls.setFechaIngreso(
                    LocalDate.parse(txtFechaIngreso.getText())
            );

            ls.setDosisDisponibles(
                    Integer.parseInt(spnDosisDisponibles.getValue().toString())
            );

            ls.setActivo(
                    cmbEstado.getSelectedItem().toString().equals("Activo")
            );

            if (loteDAO.insertar(ls)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Lote registrado correctamente"
                );

                limpiar();
                cargarTabla();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo guardar"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage()
            );
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        if (idLoteSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un lote"
            );

            return;
        }

        try {

            LoteSemen ls = new LoteSemen();

            ls.setIdLote(idLoteSeleccionado);

            ls.setCodigoLote(txtCodigoLote.getText());
            ls.setRazaToro(txtRazaToro.getText());
            ls.setNombreToro(txtNombreToro.getText());
            ls.setProveedor(txtProveedor.getText());

            ls.setFechaIngreso(
                    LocalDate.parse(
                            txtFechaIngreso.getText()
                    )
            );

            ls.setDosisDisponibles(
                    (Integer) spnDosisDisponibles.getValue()
            );

            ls.setActivo(
                    cmbEstado.getSelectedItem()
                            .toString()
                            .equals("Activo")
            );

            if (loteDAO.actualizar(ls)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Registro actualizado"
                );

                limpiar();
                cargarTabla();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (idLoteSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un lote"
            );

            return;
        }

        if (loteDAO.desactivar(idLoteSeleccionado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Lote desactivado"
            );

            limpiar();
            cargarTabla();
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        modelo.setRowCount(0);

        for (LoteSemen ls
                : loteDAO.buscarPorCodigo(
                        txtBuscar.getText())) {

            modelo.addRow(new Object[]{
                ls.getIdLote(),
                ls.getCodigoLote(),
                ls.getRazaToro(),
                ls.getNombreToro(),
                ls.getProveedor(),
                ls.getFechaIngreso(),
                ls.getDosisDisponibles(),
                ls.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        cargarTabla();
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void tblLotesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLotesMouseClicked
        int fila = tblLotes.getSelectedRow();

        if (fila < 0) {
            return;
        }

        idLoteSeleccionado
                = Integer.parseInt(
                        tblLotes.getValueAt(fila, 0).toString());

        LoteSemen ls
                = loteDAO.obtenerPorId(idLoteSeleccionado);

        if (ls != null) {

            txtCodigoLote.setText(ls.getCodigoLote());
            txtRazaToro.setText(ls.getRazaToro());
            txtNombreToro.setText(ls.getNombreToro());
            txtProveedor.setText(ls.getProveedor());

            txtFechaIngreso.setText(
                    ls.getFechaIngreso().toString()
            );

            spnDosisDisponibles.setValue(
                    ls.getDosisDisponibles()
            );

            cmbEstado.setSelectedItem(
                    ls.isActivo()
                    ? "Activo"
                    : "Inactivo"
            );
        }
    }//GEN-LAST:event_tblLotesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblStockBajo;
    private javax.swing.JLabel lblTotalDosis;
    private javax.swing.JLabel lblTotalLotes;
    private javax.swing.JPanel panel;
    private javax.swing.JSpinner spnDosisDisponibles;
    private javax.swing.JTable tblLotes;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigoLote;
    private javax.swing.JTextField txtFechaIngreso;
    private javax.swing.JTextField txtNombreToro;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtProveedor;
    private javax.swing.JTextField txtRazaToro;
    // End of variables declaration//GEN-END:variables
}
