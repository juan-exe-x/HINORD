package interfaz;

import clases.conexion;
import clases.prevenciones;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.Border;

public class mdiPrevenciones extends javax.swing.JInternalFrame {

    // ── ① Mapas estáticos ────────────────────────────────────
    private static final java.util.Map<String, Integer> INTERVALOS_VACUNA;
    private static final java.util.Map<String, String>  NOTAS_VACUNA;
    static {
        INTERVALOS_VACUNA = new java.util.LinkedHashMap<>();
        INTERVALOS_VACUNA.put("Fiebre Aftosa",               180);
        INTERVALOS_VACUNA.put("Brucelosis",                  365);
        INTERVALOS_VACUNA.put("IBR (Rinotraqueitis)",        365);
        INTERVALOS_VACUNA.put("DVB (Diarrea Viral Bovina)",  365);
        INTERVALOS_VACUNA.put("Leptospirosis",               180);
        INTERVALOS_VACUNA.put("Carbón Bacteridiano",         365);
        INTERVALOS_VACUNA.put("Rabia",                       365);
        INTERVALOS_VACUNA.put("Clostridiosis",               180);
        INTERVALOS_VACUNA.put("Mastitis (secado)",            90);
        INTERVALOS_VACUNA.put("Diarrea Neonatal",             90);
        INTERVALOS_VACUNA.put("Otra",                          0);
 
        NOTAS_VACUNA = new java.util.LinkedHashMap<>();
        NOTAS_VACUNA.put("Fiebre Aftosa",             "Obligatoria por ley. Revacunar cada 6 meses.");
        NOTAS_VACUNA.put("Brucelosis",                "Única dosis en hembras 3-8 meses. Anual en machos.");
        NOTAS_VACUNA.put("IBR (Rinotraqueitis)",      "Aplicar antes de época reproductiva.");
        NOTAS_VACUNA.put("DVB (Diarrea Viral Bovina)","Revacunar anualmente, especialmente gestantes.");
        NOTAS_VACUNA.put("Leptospirosis",             "Alta incidencia en épocas lluviosas.");
        NOTAS_VACUNA.put("Carbón Bacteridiano",       "Obligatoria en zonas endémicas.");
        NOTAS_VACUNA.put("Rabia",                     "Obligatoria en zonas silvestres de riesgo.");
        NOTAS_VACUNA.put("Clostridiosis",             "Aplicar antes de temporada húmeda.");
        NOTAS_VACUNA.put("Mastitis (secado)",         "Aplicar al inicio del período seco.");
        NOTAS_VACUNA.put("Diarrea Neonatal",          "Aplicar a la madre 3 semanas antes del parto.");
    }
 
    // ── ② Atributos de instancia ─────────────────────────────
    private final prevenciones p = new prevenciones();
 
    // Campos de datos (igual que antes, pero ahora todo se posiciona
    // de forma secuencial dentro de un panel con scroll)
    private JTextField txtnombre, txtIDICA, txtidregistro, txtvacunaaplicada,
            txtviaadmin, txtfecapli, txtfecproxi, txtveteresponsable,
            txtenfermedadpreviene, txtdosis, txtprevenciones, txtbuscar,
            txtdiasmanual;
 
    private JButton btnguardar, btnactualizar, btneliminar, btnbuscar;
 
    private JComboBox<String> cboenfermedadpreviene = new JComboBox<>();
    private JLabel lblNotaClinica  = new JLabel();
    private JLabel lblDiasManual   = new JLabel();
 
    // ── Insumo / frasco del inventario (opcional) ────────────
    private JComboBox<String> cboInsumo = new JComboBox<>();
    private JLabel lblStockInfo = new JLabel();
    private final java.util.Map<String, Integer> mapaInsumos = new java.util.LinkedHashMap<>();
    private final java.util.Map<Integer, Double> mapaStock   = new java.util.HashMap<>();
 
    // Panel interno (contenido real) y referencia para poder
    // recalcular tamaño cuando se muestra/oculta el campo "días manual"
    private JPanel panel;
    private JScrollPane scrollPrincipal;
 
    // Paleta — igual que TemaFinca (verde oscuro / dorado de las cards)
    private static final Color VERDE_OSCURO = new Color(27, 94, 32);
    private static final Color VERDE_MEDIO  = new Color(46, 125, 50);
    private static final Color VERDE_CLARO  = new Color(232, 245, 233);
    private static final Color DORADO       = new Color(212, 175, 55);
    private static final Color GRIS_TEXTO   = new Color(55, 55, 55);
    private static final Font  FUENTE_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font  FUENTE_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    // ── ③ Constructor ────────────────────────────────────────
    public mdiPrevenciones() {
        aplicarEstiloVacunacion();
        cargarInsumosDisponibles();
    }
    // =========================================================================
    // CONSTRUCCIÓN DE TODO EL UI — layout secuencial, igual patrón que
    // mdiInsumos / mdiRegistroParto: header fijo + JScrollPane con un
    // panel interno cuyo alto se ajusta al contenido real.
    // =========================================================================

    private void aplicarEstiloVacunacion() {

        getContentPane().removeAll();
        getContentPane().setBackground(VERDE_CLARO);
        getContentPane().setLayout(null);
        setSize(620, 800);

        int anchoFrame = 620;

        // ── Header (igual estilo que el resto de módulos) ───────────────────
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
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
                    g2.drawImage(img, (getWidth() - nw) / 2, (getHeight() - nh) / 2, nw, nh, this);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } catch (Exception ignored) {
                }
            }
        };
        header.setBackground(VERDE_OSCURO);
        header.setBounds(0, 0, anchoFrame, 80);
        JLabel titulo = new JLabel("REGISTRO DE VACUNACIÓN", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(DORADO);
        titulo.setBounds(0, 18, anchoFrame, 44);
        header.add(titulo);
        getContentPane().add(header);

        // ── Barra de búsqueda ────────────────────────────────────────────────
        JPanel panelBuscar = new JPanel(null);
        panelBuscar.setBackground(VERDE_MEDIO);
        panelBuscar.setBounds(0, 80, anchoFrame, 48);

        JLabel lblBuscar = new JLabel("🔍 Buscar por IDICA:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblBuscar.setForeground(Color.WHITE);
        lblBuscar.setBounds(20, 12, 150, 25);
        panelBuscar.add(lblBuscar);

        txtbuscar = new JTextField();
        txtbuscar.setBounds(170, 10, 170, 28);
        txtbuscar.setFont(FUENTE_INPUT);
        txtbuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DORADO, 1),
                BorderFactory.createEmptyBorder(2, 7, 2, 7)));
        panelBuscar.add(txtbuscar);

        btnbuscar = new JButton("BUSCAR");
        btnbuscar.setBounds(355, 8, 120, 32);
        estilizarBotonPro(btnbuscar, VERDE_MEDIO, "/imagenes/buscar.png");
        btnbuscar.addActionListener(this::btnbuscarActionPerformed);
        panelBuscar.add(btnbuscar);
        getContentPane().add(panelBuscar);

        txtbuscar.setText("Ingrese IDICA...");
        txtbuscar.setForeground(Color.GRAY);
        txtbuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtbuscar.getText().equals("Ingrese IDICA...")) {
                    txtbuscar.setText("");
                    txtbuscar.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtbuscar.getText().isEmpty()) {
                    txtbuscar.setText("Ingrese IDICA...");
                    txtbuscar.setForeground(Color.GRAY);
                }
            }
        });

        // ── Panel de contenido (scrollable) — layout secuencial ────────────
        panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        int x1 = 20, anchoCampo = 540, y = 18, gap = 60;
        Border bordeInput = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10));

        // Campos ocultos de referencia interna (no visibles, tamaño 0)
        txtidregistro = new JTextField();
        txtidregistro.setBounds(0, 0, 0, 0);
        panel.add(txtidregistro);

        txtprevenciones = new JTextField();
        txtprevenciones.setBounds(0, 0, 0, 0);
        panel.add(txtprevenciones);

        // Fila — NOMBRE DEL ANIMAL
        txtnombre = campoConLabel(panel, "NOMBRE DEL ANIMAL", x1, y, anchoCampo, bordeInput);
        y += gap;

        // Fila — IDICA
        txtIDICA = campoConLabel(panel, "IDICA", x1, y, anchoCampo, bordeInput);
        y += gap;

        // Fila — INSUMO / FRASCO DEL INVENTARIO (opcional)
        lbl(panel, "INSUMO / FRASCO DEL INVENTARIO (opcional)", x1, y, anchoCampo);
        cboInsumo.setBounds(x1, y + 20, anchoCampo, 30);
        cboInsumo.setFont(FUENTE_INPUT);
        cboInsumo.setBackground(Color.WHITE);
        cboInsumo.addActionListener(e -> onInsumoSeleccionado());
        panel.add(cboInsumo);
        y += 52;

        lblStockInfo.setText("");
        lblStockInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStockInfo.setForeground(VERDE_MEDIO);
        lblStockInfo.setBounds(x1, y, anchoCampo, 18);
        panel.add(lblStockInfo);
        y += 26;

        sep(panel, x1, y, anchoCampo);
        y += 16;

        // Fila — VACUNA APLICADA
        txtvacunaaplicada = campoConLabel(panel, "VACUNA APLICADA", x1, y, anchoCampo, bordeInput);
        y += gap;

        // Fila — VÍA DE ADMINISTRACIÓN
        txtviaadmin = campoConLabel(panel, "VÍA DE ADMINISTRACIÓN", x1, y, anchoCampo, bordeInput);
        y += gap;

        // Fila — DOSIS
        txtdosis = campoConLabel(panel, "DOSIS (ml / unidades)", x1, y, anchoCampo, bordeInput);
        y += gap;

        // Fila — FECHA DE APLICACIÓN
        txtfecapli = campoConLabel(panel, "FECHA DE APLICACIÓN (dd/MM/yyyy)", x1, y, anchoCampo, bordeInput);
        txtfecapli.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularProximaVacunacion();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularProximaVacunacion();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });
        y += gap;

        // Fila — ENFERMEDAD (ComboBox)
        lbl(panel, "ENFERMEDAD QUE PREVIENE", x1, y, anchoCampo);
        cboenfermedadpreviene.removeAllItems();
        cboenfermedadpreviene.addItem("Seleccionar enfermedad...");
        INTERVALOS_VACUNA.keySet().forEach(cboenfermedadpreviene::addItem);
        cboenfermedadpreviene.setBounds(x1, y + 20, anchoCampo, 30);
        cboenfermedadpreviene.setFont(FUENTE_INPUT);
        cboenfermedadpreviene.setBackground(Color.WHITE);
        cboenfermedadpreviene.addActionListener(e -> onEnfermedadSeleccionada());
        panel.add(cboenfermedadpreviene);
        y += 52;

        lblNotaClinica.setText("");
        lblNotaClinica.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNotaClinica.setForeground(VERDE_MEDIO);
        lblNotaClinica.setBounds(x1, y, anchoCampo, 18);
        panel.add(lblNotaClinica);
        y += 26;

        // Fila — DÍAS MANUAL (oculto hasta elegir "Otra")
        lblDiasManual.setText("DÍAS HASTA PRÓXIMA VACUNACIÓN (manual)");
        lblDiasManual.setFont(FUENTE_LABEL);
        lblDiasManual.setForeground(GRIS_TEXTO);
        lblDiasManual.setBounds(x1, y, anchoCampo, 18);
        lblDiasManual.setVisible(false);
        panel.add(lblDiasManual);

        txtdiasmanual = new JTextField();
        txtdiasmanual.setBounds(x1, y + 20, anchoCampo, 32);
        txtdiasmanual.setFont(FUENTE_INPUT);
        txtdiasmanual.setBorder(bordeInput);
        txtdiasmanual.setVisible(false);
        txtdiasmanual.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularDesdeManual();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularDesdeManual();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });
        panel.add(txtdiasmanual);
        // Nota: este bloque ocupa espacio reservado SIEMPRE (visible o no)
        // para que el cálculo de 'y' sea estable y no se solapen filas.
        y += gap;

        // Fila — PRÓXIMA VACUNACIÓN (auto, solo lectura)
        lbl(panel, "PRÓXIMA VACUNACIÓN (automática)", x1, y, anchoCampo);
        txtfecproxi = new JTextField();
        txtfecproxi.setFont(FUENTE_INPUT);
        txtfecproxi.setEditable(false);
        txtfecproxi.setBackground(new Color(255, 253, 231));
        txtfecproxi.setForeground(new Color(93, 64, 55));
        txtfecproxi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DORADO, 2),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        txtfecproxi.setBounds(x1, y + 20, anchoCampo, 32);
        panel.add(txtfecproxi);
        y += gap;

        // Fila — VETERINARIO
        txtveteresponsable = campoConLabel(panel, "VETERINARIO RESPONSABLE", x1, y, anchoCampo, bordeInput);
        y += gap;

        sep(panel, x1, y, anchoCampo);
        y += 20;

        // ── Botones ──────────────────────────────────────────────────────────
        btnguardar = new JButton("GUARDAR");
        btnactualizar = new JButton("ACTUALIZAR");
        btneliminar = new JButton("ELIMINAR");

        int bW = 165, bH = 42, bGap = 15;
        btnguardar.setBounds(x1, y, bW, bH);
        btnactualizar.setBounds(x1 + bW + bGap, y, bW, bH);
        btneliminar.setBounds(x1 + 2 * (bW + bGap), y, bW, bH);

        estilizarBotonPro(btnguardar, new Color(40, 167, 69), "/imagenes/expediente.png");
        estilizarBotonPro(btnactualizar, new Color(0, 123, 255), "/imagenes/actualizarr.png");
        estilizarBotonPro(btneliminar, new Color(220, 53, 69), "/imagenes/papelera.png");

        btnguardar.addActionListener(this::btnguardarActionPerformed);
        btnactualizar.addActionListener(this::btnactualizarActionPerformed);
        btneliminar.addActionListener(this::btneliminarActionPerformed);

        panel.add(btnguardar);
        panel.add(btnactualizar);
        panel.add(btneliminar);

        y += bH + 20;

        // ── CLAVE: el panel mide exactamente lo que necesita su contenido ──
        panel.setPreferredSize(new Dimension(anchoCampo + 40, y));

        scrollPrincipal = new JScrollPane(panel);
        scrollPrincipal.setBounds(0, 128, anchoFrame, 650);
        scrollPrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPrincipal.setBorder(BorderFactory.createEmptyBorder());
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        getContentPane().add(scrollPrincipal);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        revalidate();
        repaint();
    }

    // =========================================================================
    // HELPERS DE CONSTRUCCIÓN UI (mismo patrón que mdiInsumos)
    // =========================================================================
    private void lbl(JPanel p, String texto, int x, int y, int w) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_LABEL);
        l.setForeground(GRIS_TEXTO);
        l.setBounds(x, y, w, 18);
        p.add(l);
    }

    /**
     * Crea label + JTextField apilados, devuelve el campo ya añadido al panel
     */
    private JTextField campoConLabel(JPanel p, String texto, int x, int y, int w, Border borde) {
        lbl(p, texto, x, y, w);
        JTextField t = new JTextField();
        t.setFont(FUENTE_INPUT);
        t.setBorder(borde);
        t.setBounds(x, y + 20, w, 32);
        p.add(t);
        return t;
    }

    private void sep(JPanel p, int x, int y, int w) {
        JSeparator s = new JSeparator();
        s.setBounds(x, y, w, 2);
        s.setForeground(new Color(220, 220, 220));
        p.add(s);
    }

    private void estilizarBotonPro(JButton btn, Color fondo, String rutaIcono) {
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setOpaque(true);

        Color hover = fondo.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(fondo);
            }
        });

        try {
            btn.setIcon(new ImageIcon(getClass().getResource(rutaIcono)));
        } catch (Exception ignored) {
        }
    }

    // =========================================================================
    // LÓGICA DE NEGOCIO — idéntica a la versión anterior
    // =========================================================================
    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {
        String nombre = txtnombre.getText();
        String IDICATEXTO = txtIDICA.getText();
        String idregistrotxt = txtidregistro.getText();
        String vacunaaplicada = txtvacunaaplicada.getText();
        String viaadministracion = txtviaadmin.getText();
        String dosistexto = txtdosis.getText();
        String fecaplicadatexto = txtfecapli.getText();
        String fecproximatexto = txtfecproxi.getText();
        String veterinario = txtveteresponsable.getText();
        String enfermedadpreviene = txtenfermedadpreviene != null ? txtenfermedadpreviene.getText()
                : (String) cboenfermedadpreviene.getSelectedItem();

        if (nombre.isEmpty() || IDICATEXTO.isEmpty() || idregistrotxt.isEmpty()
                || vacunaaplicada.isEmpty() || viaadministracion.isEmpty()
                || dosistexto.isEmpty() || fecaplicadatexto.isEmpty()
                || fecproximatexto.isEmpty() || veterinario.isEmpty()
                || enfermedadpreviene == null || enfermedadpreviene.isEmpty()
                || enfermedadpreviene.startsWith("Seleccionar")) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            long IDICA = Long.parseLong(IDICATEXTO);
            int idregistro = Integer.parseInt(idregistrotxt);
            double dosis = Double.parseDouble(dosistexto);
            java.sql.Date fechaapli = convertirFechaFlexible(fecaplicadatexto);
            java.sql.Date fechaproxima = convertirFechaFlexible(fecproximatexto);

            Integer idInsumoSeleccionado = obtenerIdInsumoSeleccionado();

            if (idInsumoSeleccionado != null) {
                Double stockDisponible = mapaStock.get(idInsumoSeleccionado);
                if (stockDisponible != null && stockDisponible < dosis) {
                    JOptionPane.showMessageDialog(null,
                            "Stock insuficiente en el insumo seleccionado.\n"
                            + "Stock disponible: " + stockDisponible + "\n"
                            + "Dosis solicitada: " + dosis,
                            "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            boolean guardado = p.guardar(nombre, IDICA, idregistro, vacunaaplicada,
                    viaadministracion, dosis, fechaapli, fechaproxima, veterinario,
                    enfermedadpreviene, idInsumoSeleccionado);

            if (guardado) {
                String msg = "Registro guardado correctamente";
                // =========================================================
                    // 🔥 NUEVO: NOTIFICAR AL DASHBOARD PRINCIPAL EL CAMBIO
                    // =========================================================
                    java.awt.Window ventanaAnfitriona = javax.swing.SwingUtilities.getWindowAncestor(this);
                    
                    // NOTA: Reemplaza 'VentanaPrincipal' por el nombre EXACTO de tu clase JFrame principal
                    if (ventanaAnfitriona instanceof frmnuevomenu) {
                        ((frmnuevomenu) ventanaAnfitriona).actualizarDatosDashboard();
                    }
                    // =========================================================
                if (idInsumoSeleccionado != null) {
                    msg += "\nStock de la vacuna descontado automáticamente.\nSe descontó también 1 aguja del inventario (si había disponible).";
                }
                JOptionPane.showMessageDialog(null, msg);
                cargarInsumosDisponibles();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "IDICA, ID Registro o Dosis inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String vacunaaplicada = txtvacunaaplicada.getText();
            String viaadministracion = txtviaadmin.getText();
            String dosistexto = txtdosis.getText();
            String fecaplicadatexto = txtfecapli.getText();
            String fecproximatexto = txtfecproxi.getText();
            String veterinario = txtveteresponsable.getText();
            String enfermedadpreviene = (String) cboenfermedadpreviene.getSelectedItem();
            String idprevencionestxt = txtprevenciones.getText();

            if (vacunaaplicada.isEmpty() || viaadministracion.isEmpty() || dosistexto.isEmpty()
                    || fecaplicadatexto.isEmpty() || fecproximatexto.isEmpty()
                    || veterinario.isEmpty() || enfermedadpreviene == null || enfermedadpreviene.isEmpty()
                    || idprevencionestxt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idprevenciones = Integer.parseInt(idprevencionestxt);
            double dosis = Double.parseDouble(dosistexto);
            java.sql.Date fecApli = convertirFechaFlexible(fecaplicadatexto);
            java.sql.Date fecProx = convertirFechaFlexible(fecproximatexto);

            boolean actualizado = p.actualizar(idprevenciones, fecApli, fecProx, dosis,
                    veterinario, vacunaaplicada, viaadministracion,
                    enfermedadpreviene);
            if (actualizado) {
                JOptionPane.showMessageDialog(null, "Vacunación actualizada correctamente");
                // =========================================================
                    // 🔥 NUEVO: NOTIFICAR AL DASHBOARD PRINCIPAL EL CAMBIO
                    // =========================================================
                    java.awt.Window ventanaAnfitriona = javax.swing.SwingUtilities.getWindowAncestor(this);
                    
                    // NOTA: Reemplaza 'VentanaPrincipal' por el nombre EXACTO de tu clase JFrame principal
                    if (ventanaAnfitriona instanceof frmnuevomenu) {
                        ((frmnuevomenu) ventanaAnfitriona).actualizarDatosDashboard();
                    }
                    // =========================================================
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el registro.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID o Dosis inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {
        String idtexto = txtprevenciones.getText();

        if (idtexto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el ID de la prevención a eliminar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar esta prevención?\n"
                + "Nota: el stock del insumo NO se restaurará automáticamente.",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int idprevenciones = Integer.parseInt(idtexto);
            boolean eliminado = p.eliminar(idprevenciones);
            if (eliminado) {
                JOptionPane.showMessageDialog(null, "Prevención eliminada correctamente");
                // =========================================================
                    // 🔥 NUEVO: NOTIFICAR AL DASHBOARD PRINCIPAL EL CAMBIO
                    // =========================================================
                    java.awt.Window ventanaAnfitriona = javax.swing.SwingUtilities.getWindowAncestor(this);
                    
                    // NOTA: Reemplaza 'VentanaPrincipal' por el nombre EXACTO de tu clase JFrame principal
                    if (ventanaAnfitriona instanceof frmnuevomenu) {
                        ((frmnuevomenu) ventanaAnfitriona).actualizarDatosDashboard();
                    }
                    // =========================================================
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el registro.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String texto = txtbuscar.getText().trim();
            if (texto.equals("Ingrese IDICA...") || texto.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese un IDICA para buscar.");
                return;
            }
            int id = Integer.parseInt(texto);
            Object[][] datos = p.buscarPorIDICAOPrevencion(id);

            if (datos.length > 0) {
                if (datos[0].length == 3) {
                    txtnombre.setText(datos[0][0].toString());
                    txtIDICA.setText(datos[0][1].toString());
                    txtidregistro.setText(datos[0][2].toString());
                    JOptionPane.showMessageDialog(null, "Animal encontrado. Complete los datos de vacunación.");
                } else if (datos[0].length == 11) {
                    txtnombre.setText(datos[0][0].toString());
                    txtIDICA.setText(datos[0][1].toString());
                    txtidregistro.setText(datos[0][2].toString());
                    txtprevenciones.setText(datos[0][3].toString());
                    txtvacunaaplicada.setText(datos[0][4].toString());
                    txtviaadmin.setText(datos[0][5].toString());
                    txtfecapli.setText(datos[0][6].toString());
                    txtfecapli.setText(datos[0][7].toString());
                    txtfecproxi.setText(datos[0][8].toString());
                    txtveteresponsable.setText(datos[0][9].toString());
                    String enfGuardada = datos[0][10].toString();
                    cboenfermedadpreviene.setSelectedItem(enfGuardada);
                    JOptionPane.showMessageDialog(null, "Datos completos encontrados.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún registro con ese ID.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID no válido.");
        }
    }

    // ── Lógica de enfermedad seleccionada ──────────────────
    private void onEnfermedadSeleccionada() {
        String enf = (String) cboenfermedadpreviene.getSelectedItem();
        if (enf == null || enf.startsWith("Seleccionar")) {
            lblNotaClinica.setText("");
            lblDiasManual.setVisible(false);
            txtdiasmanual.setVisible(false);
            txtfecproxi.setText("");
            return;
        }

        String nota = NOTAS_VACUNA.getOrDefault(enf, "");
        lblNotaClinica.setText("ℹ " + nota);

        boolean esManual = enf.equals("Otra");
        lblDiasManual.setVisible(esManual);
        txtdiasmanual.setVisible(esManual);

        if (!esManual) {
            calcularProximaVacunacion();
        } else {
            txtfecproxi.setText("");
        }

        // Auto-selecciona en el combo de insumo la vacuna que corresponde
        // a la enfermedad elegida, para no obligar al usuario a buscarla
        // manualmente. Si no la encuentra (sin stock o no existe), deja
        // el combo en "Ninguno" y el usuario puede elegir manualmente.
        autoSeleccionarInsumoPorEnfermedad(enf);
    }

    /**
     * Busca en cboInsumo una vacuna cuyo nombre coincida con la enfermedad
     * elegida
     */
    private void autoSeleccionarInsumoPorEnfermedad(String enfermedad) {
        if (esManualEnfermedad(enfermedad)) {
            return;
        }

        String nombreEsperado = "vacuna " + enfermedad.toLowerCase();
        // Quita paréntesis y su contenido para comparar de forma más flexible
        // (ej: "IBR (Rinotraqueitis)" -> "ibr")
        String nombreSimplificado = nombreEsperado.split("\\(")[0].trim();

        for (int i = 0; i < cboInsumo.getItemCount(); i++) {
            String item = cboInsumo.getItemAt(i).toLowerCase();
            if (item.contains(nombreSimplificado)) {
                cboInsumo.setSelectedIndex(i);
                return;
            }
        }
        // No se encontró (sin stock o no creada aún) → deja "Ninguno"
        cboInsumo.setSelectedIndex(0);
    }

    private boolean esManualEnfermedad(String enf) {
        return enf == null || enf.startsWith("Seleccionar") || enf.equals("Otra");
    }

    // ── Lógica de insumo seleccionado ─────────────────
    private void cargarInsumosDisponibles() {
        cboInsumo.removeAllItems();
        mapaInsumos.clear();
        mapaStock.clear();

        cboInsumo.addItem("Ninguno (no descontar inventario)");

        try (Connection cn = conexion.conectar()) {
            String sql
                    = "SELECT i.id_insumo, i.nombre, i.stock_actual, i.unidad_medida, c.nombre AS categoria "
                    + "FROM insumo i "
                    + "JOIN categoria_insumo c ON i.id_categoria = c.id_categoria "
                    + "WHERE i.activo = 1 AND i.stock_actual > 0 "
                    + "AND c.nombre IN ('Vacuna', 'Medicamento', 'Material Veterinario') "
                    + "ORDER BY i.nombre";
            ResultSet rs = cn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                int idIns = rs.getInt("id_insumo");
                double stock = rs.getDouble("stock_actual");
                String texto = rs.getString("nombre") + "  (disp: " + stock + " "
                        + rs.getString("unidad_medida") + ")";
                cboInsumo.addItem(texto);
                mapaInsumos.put(texto, idIns);
                mapaStock.put(idIns, stock);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar insumos disponibles: " + e.getMessage());
        }
    }

    private Integer obtenerIdInsumoSeleccionado() {
        String sel = (String) cboInsumo.getSelectedItem();
        if (sel == null || sel.startsWith("Ninguno")) {
            return null;
        }
        return mapaInsumos.get(sel);
    }

    private void onInsumoSeleccionado() {
        Integer idIns = obtenerIdInsumoSeleccionado();
        if (idIns == null) {
            lblStockInfo.setText("");
            return;
        }
        String sel = (String) cboInsumo.getSelectedItem();
        String nombreLimpio = sel.split("  \\(disp:")[0].trim();
        txtvacunaaplicada.setText(nombreLimpio);

        Double stock = mapaStock.get(idIns);
        lblStockInfo.setText("ℹ Stock disponible: " + (stock != null ? stock : "?")
                + " — se descontará según la dosis indicada.");
    }

    // ── Cálculo desde campo manual ─────────────────────
    private void calcularDesdeManual() {
        try {
            int dias = Integer.parseInt(txtdiasmanual.getText().trim());
            if (dias > 0) {
                calcularProximaConDias(dias);
            }
        } catch (NumberFormatException ignored) {
        }
    }

    private void calcularProximaVacunacion() {
        String enf = (String) cboenfermedadpreviene.getSelectedItem();
        if (enf == null || enf.startsWith("Seleccionar") || enf.equals("Otra")) {
            return;
        }
        int dias = INTERVALOS_VACUNA.getOrDefault(enf, 0);
        if (dias > 0) {
            calcularProximaConDias(dias);
        }
    }

    private void calcularProximaConDias(int dias) {
        String fechaStr = txtfecapli.getText().trim();
        if (fechaStr.isEmpty()) {
            txtfecproxi.setText("");
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            java.util.Date base = sdf.parse(fechaStr);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(base);
            cal.add(java.util.Calendar.DAY_OF_YEAR, dias);
            txtfecproxi.setText(sdf.format(cal.getTime()));

            long diffDias = (cal.getTimeInMillis() - System.currentTimeMillis()) / 86_400_000L;
            if (diffDias < 0) {
                txtfecproxi.setBackground(new Color(255, 235, 238));
                txtfecproxi.setForeground(new Color(183, 28, 28));
            } else if (diffDias <= 15) {
                txtfecproxi.setBackground(new Color(255, 243, 224));
                txtfecproxi.setForeground(new Color(230, 81, 0));
            } else {
                txtfecproxi.setBackground(new Color(255, 253, 231));
                txtfecproxi.setForeground(new Color(93, 64, 55));
            }
        } catch (ParseException ex) {
            txtfecproxi.setText("Fecha inválida");
            txtfecproxi.setBackground(new Color(255, 235, 238));
        }
    }

    private java.sql.Date convertirFechaFlexible(String textoFecha) throws ParseException {
        String[] formatos = {"dd/MM/yyyy", "yyyy/MM/dd", "dd-MM-yyyy", "yyyy-MM-dd"};
        for (String fmt : formatos) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(fmt);
                sdf.setLenient(false);
                return new java.sql.Date(sdf.parse(textoFecha).getTime());
            } catch (ParseException ignored) {
            }
        }
        throw new ParseException("Formato no reconocido: " + textoFecha, 0);
    }

    private void limpiarCampos() {
        txtnombre.setText("");
        txtIDICA.setText("");
        txtvacunaaplicada.setText("");
        txtviaadmin.setText("");
        txtfecapli.setText("");
        txtfecproxi.setText("");
        txtveteresponsable.setText("");
        txtprevenciones.setText("");
        txtidregistro.setText("");
        txtdiasmanual.setText("");
        cboenfermedadpreviene.setSelectedIndex(0);
        cboInsumo.setSelectedIndex(0);
        lblNotaClinica.setText("");
        lblStockInfo.setText("");
    }

    // Campo de compatibilidad: el código viejo lo usaba para sincronizar
    // el combo de enfermedad con un JTextField. Ya no es necesario porque
    // ahora se lee directo de cboenfermedadpreviene, pero se deja como
    // referencia nula seguro por si algo externo llega a invocarlo.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
