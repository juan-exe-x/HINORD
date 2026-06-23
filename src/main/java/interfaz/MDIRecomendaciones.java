
package interfaz;

import clases.RecomendacionesIA;
import clases.conexion;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MDIRecomendaciones extends javax.swing.JInternalFrame {
    
    Connection con = conexion.conectar();
 // ── Colores corporativos ──────────────────────────────────────────────────
    private static final Color VERDE_OSCURO    = new Color(27,  94,  32);
    private static final Color VERDE_MEDIO     = new Color(46, 125,  50);
    private static final Color VERDE_CLARO     = new Color(232, 245, 233);
    private static final Color VERDE_BTN       = new Color(46, 125,  50);
    private static final Color ROJO_BTN        = new Color(198,  40,  40);
    private static final Color AMARILLO_TITULO = new Color(245, 200,  66);
    private static final Color AZUL_INFO_BG    = new Color(227, 242, 253);
    private static final Color AZUL_INFO_TXT   = new Color( 21, 101, 192);
    private static final Color NARANJA_INFO_BG = new Color(255, 243, 224);
    private static final Color NARANJA_INFO_TXT= new Color(230,  81,   0);
    private static final Color BORDE_GRIS      = new Color(176, 196, 177);
    private static final Color FONDO_PANEL     = new Color(240, 244, 240);
 
    // ── Componentes de la UI ──────────────────────────────────────────────────
    private JTextField    txtIdRegistro;
    private JLabel        lblNombre, lblIdica, lblEdad, lblPeso;
    private JTextArea     txtRespuesta;
    private JLabel        lblBadge;
 
    private JPanel        panelProduccion, panelSalud;
    private JToggleButton btnModoProduccion, btnModoSalud;
 
    private final JCheckBox[] chkSintomas = new JCheckBox[RecomendacionesIA.SINTOMAS.length];
 
    // ── Lógica (separada de la vista) ─────────────────────────────────────────
    private final RecomendacionesIA logica = new RecomendacionesIA();
 
    // ── Estado de la vista ────────────────────────────────────────────────────
    private String modoActual = "produccion";
 
    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────────────
    public MDIRecomendaciones() throws PropertyVetoException {
        setTitle("Recomendaciones IA — Ganadería");
       
        setResizable(false);
        setSize(670, 800);
        setClosable(true);
        setMaximizable(true);
        
       
 
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(FONDO_PANEL);
 
        contenedor.add(crearEncabezado());
        contenedor.add(espacio(10));
        contenedor.add(conMargen(crearSeccionBusqueda()));
        contenedor.add(espacio(8));
        contenedor.add(conMargen(crearSeccionInfo()));
        contenedor.add(espacio(8));
        contenedor.add(conMargen(crearSeccionConsulta()));
        contenedor.add(espacio(8));
        contenedor.add(conMargen(crearSeccionRespuesta()));
        contenedor.add(espacio(10));
        contenedor.add(crearPiePagina());
        contenedor.add(espacio(14));
 
        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        add(scroll);
        setVisible(true);
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // SECCIÓN: ENCABEZADO
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearEncabezado() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(VERDE_OSCURO);
        p.setBorder(new EmptyBorder(14, 20, 12, 20));
 
        JLabel titulo = new JLabel("RECOMENDACIONES IA", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(AMARILLO_TITULO);
 
        JLabel subtitulo = new JLabel(
            "Mejora la producción y salud de un animal específico", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(220, 220, 220));
 
        JPanel texto = new JPanel(new GridLayout(2, 1, 0, 3));
        texto.setOpaque(false);
        texto.add(titulo);
        texto.add(subtitulo);
        p.add(texto, BorderLayout.CENTER);
        return p;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // SECCIÓN: BUSCAR ANIMAL
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearSeccionBusqueda() {
        JPanel sec = crearSeccion("🔍", "BUSCAR ANIMAL POR REGISTRO");
 
        JPanel body = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        body.setBackground(VERDE_CLARO);
 
        JLabel lbl = new JLabel("ID REGISTRO:");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        body.add(lbl);
 
        txtIdRegistro = new JTextField(20);
        txtIdRegistro.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtIdRegistro.setPreferredSize(new Dimension(230, 30));
        // Permite buscar presionando Enter
        txtIdRegistro.addActionListener(e -> buscarAnimal());
        body.add(txtIdRegistro);
 
        JButton btnBuscar = crearBoton("🔍 BUSCAR", VERDE_BTN);
        btnBuscar.addActionListener(e -> buscarAnimal());
        body.add(btnBuscar);
 
        sec.add(body, BorderLayout.CENTER);
        return sec;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // SECCIÓN: INFORMACIÓN DEL ANIMAL
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearSeccionInfo() {
        JPanel sec = crearSeccion("🐄", "INFORMACIÓN DEL ANIMAL");
 
        JPanel body = new JPanel(new GridLayout(1, 4, 10, 0));
        body.setBackground(VERDE_CLARO);
        body.setBorder(new EmptyBorder(10, 12, 10, 12));
 
        lblNombre = agregarTarjetaInfo(body, "👤 NOMBRE");
        lblIdica  = agregarTarjetaInfo(body, "🪪 IDICA");
        lblEdad   = agregarTarjetaInfo(body, "📅 EDAD (años)");
        lblPeso   = agregarTarjetaInfo(body, "⚖ PESO (kg)");
 
        sec.add(body, BorderLayout.CENTER);
        return sec;
    }
 
    private JLabel agregarTarjetaInfo(JPanel parent, String titulo) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDE_GRIS),
            new EmptyBorder(6, 8, 6, 8)
        ));
        JLabel lTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lTitulo.setFont(new Font("SansSerif", Font.BOLD, 10));
        lTitulo.setForeground(new Color(100, 100, 100));
 
        JLabel lValor = new JLabel("---", SwingConstants.CENTER);
        lValor.setFont(new Font("SansSerif", Font.BOLD, 14));
 
        card.add(lTitulo, BorderLayout.NORTH);
        card.add(lValor,  BorderLayout.CENTER);
        parent.add(card);
        return lValor;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // SECCIÓN: TIPO DE CONSULTA IA
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearSeccionConsulta() {
        JPanel sec = crearSeccion("🛡", "TIPO DE CONSULTA IA");
 
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(VERDE_CLARO);
        body.setBorder(new EmptyBorder(10, 12, 10, 12));
 
        // — Toggle de modo —
        JPanel toggleRow = new JPanel(new GridLayout(1, 2, 8, 0));
        toggleRow.setBackground(VERDE_CLARO);
        toggleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
 
        ButtonGroup bg = new ButtonGroup();
        btnModoProduccion = new JToggleButton("Producción y mejora");
        btnModoSalud      = new JToggleButton("Animal enfermo");
 
        for (JToggleButton b : new JToggleButton[]{btnModoProduccion, btnModoSalud}) {
            b.setFont(new Font("SansSerif", Font.PLAIN, 12));
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            bg.add(b);
            toggleRow.add(b);
        }
        btnModoProduccion.setSelected(true);
        estilizarToggle(btnModoProduccion, true);
        estilizarToggle(btnModoSalud, false);
 
        btnModoProduccion.addActionListener(e -> cambiarModo("produccion"));
        btnModoSalud.addActionListener(e -> cambiarModo("salud"));
 
        body.add(toggleRow);
        body.add(espacio(10));
 
        // — Panel producción —
        panelProduccion = new JPanel();
        panelProduccion.setLayout(new BoxLayout(panelProduccion, BoxLayout.Y_AXIS));
        panelProduccion.setOpaque(false);
 
        panelProduccion.add(crearBanner(
            "La IA analizará edad y peso del animal para recomendar dieta, suplementos y manejo óptimo.",
            AZUL_INFO_BG, AZUL_INFO_TXT));
        panelProduccion.add(espacio(8));
 
        JButton btnGenerar = crearBoton("⚡ GENERAR RECOMENDACIONES DE PRODUCCIÓN", VERDE_BTN);
        btnGenerar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenerar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnGenerar.addActionListener(e -> generarIA());
        panelProduccion.add(btnGenerar);
 
        // — Panel salud —
        panelSalud = new JPanel();
        panelSalud.setLayout(new BoxLayout(panelSalud, BoxLayout.Y_AXIS));
        panelSalud.setOpaque(false);
        panelSalud.setVisible(false);
 
        panelSalud.add(crearBanner(
            "Selecciona los síntomas observados. La IA sugerirá primeros auxilios urgentes para el dueño de la finca.",
            NARANJA_INFO_BG, NARANJA_INFO_TXT));
        panelSalud.add(espacio(8));
 
        JPanel gridSintomas = new JPanel(new GridLayout(3, 3, 8, 6));
        gridSintomas.setOpaque(false);
        for (int i = 0; i < RecomendacionesIA.SINTOMAS.length; i++) {
            chkSintomas[i] = new JCheckBox(RecomendacionesIA.SINTOMAS[i]);
            chkSintomas[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            chkSintomas[i].setBackground(Color.WHITE);
            chkSintomas[i].setOpaque(true);
            chkSintomas[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_GRIS),
                new EmptyBorder(4, 8, 4, 8)
            ));
            gridSintomas.add(chkSintomas[i]);
        }
        panelSalud.add(gridSintomas);
        panelSalud.add(espacio(8));
 
        JButton btnAnalizarSalud = crearBoton("🛡 ANALIZAR SÍNTOMAS CON IA", ROJO_BTN);
        btnAnalizarSalud.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAnalizarSalud.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnAnalizarSalud.addActionListener(e -> generarIA());
        panelSalud.add(btnAnalizarSalud);
 
        body.add(panelProduccion);
        body.add(panelSalud);
        sec.add(body, BorderLayout.CENTER);
        return sec;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // SECCIÓN: RESPUESTA DE LA IA
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearSeccionRespuesta() {
        JPanel sec = crearSeccion("📋", "RESPUESTA DE LA IA");
 
        JPanel body = new JPanel(new BorderLayout(0, 6));
        body.setBackground(VERDE_CLARO);
        body.setBorder(new EmptyBorder(10, 12, 10, 12));
 
        // Fila etiqueta + badge
        JPanel labelRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        labelRow.setOpaque(false);
        JLabel lbResp = new JLabel("⏱ Respuesta generada");
        lbResp.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbResp.setForeground(new Color(100, 100, 100));
        lblBadge = new JLabel("");
        lblBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblBadge.setBorder(new EmptyBorder(2, 8, 2, 8));
        lblBadge.setOpaque(true);
        labelRow.add(lbResp);
        labelRow.add(lblBadge);
 
        // Área de texto
        txtRespuesta = new JTextArea(8, 44);
        txtRespuesta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtRespuesta.setLineWrap(true);
        txtRespuesta.setWrapStyleWord(true);
        txtRespuesta.setEditable(false);
        txtRespuesta.setBackground(Color.WHITE);
        txtRespuesta.setBorder(new EmptyBorder(8, 10, 8, 10));
        setTextoPlaceholder("Aquí se mostrará la respuesta personalizada de la IA...");
 
        JScrollPane scrollResp = new JScrollPane(txtRespuesta);
        scrollResp.setBorder(BorderFactory.createLineBorder(BORDE_GRIS));
 
        body.add(labelRow,   BorderLayout.NORTH);
        body.add(scrollResp, BorderLayout.CENTER);
        sec.add(body, BorderLayout.CENTER);
        return sec;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // PIE DE PÁGINA
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel crearPiePagina() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setBackground(FONDO_PANEL);
        JButton btnExportar = crearBoton("📥 EXPORTAR RECOMENDACIONES", VERDE_BTN);
        btnExportar.addActionListener(e -> exportar());
        p.add(btnExportar);
        return p;
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // ACCIONES (delegadas a la clase lógica)
    // ─────────────────────────────────────────────────────────────────────────
 
    private void buscarAnimal() {
        String valor = txtIdRegistro.getText().trim();
        if (valor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un IDICA o ID de registro.");
            return;
        }

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM registro WHERE idregistro =? or IDICA=?");

            ps.setString(1, valor);
            ps.setString(2, valor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblNombre.setText(rs.getString("nombre"));
                lblIdica.setText(rs.getString("IDICA"));
                lblEdad.setText(rs.getString("edad"));
                lblPeso.setText(rs.getString("peso"));
                txtIdRegistro.setText(rs.getString("idregistro"));
                
                logica.setAnimalDesdeDB(
                lblNombre.getText(),
                lblIdica.getText(),
                lblEdad.getText().replace(" años", ""),
                lblPeso.getText().replace(" kg", "")
        );      

            }else{
                JOptionPane.showMessageDialog(null,"No existe ese registro" );
                this.limpiar();
            }

        } catch (Exception e) {
            System.err.println("Errror"+e);
        }
    }
 
    private void cambiarModo(String modo) {
        modoActual = modo;
        boolean esProd = "produccion".equals(modo);
        panelProduccion.setVisible(esProd);
        panelSalud.setVisible(!esProd);
        estilizarToggle(btnModoProduccion, esProd);
        estilizarToggle(btnModoSalud, !esProd);
        setTextoPlaceholder("Aquí se mostrará la respuesta personalizada de la IA...");
        lblBadge.setText("");
        lblBadge.setBackground(null);
    }
 
    private void generarIA() {
        if (logica.getAnimalActual() == null) {
            setTextoError("Primero busque un animal por ID de registro.");
            return;
        }
 
        // Recopilar síntomas si estamos en modo salud
        List<String> sintomas = new ArrayList<>();
        if ("salud".equals(modoActual)) {
            for (int i = 0; i < chkSintomas.length; i++) {
                if (chkSintomas[i].isSelected())
                    sintomas.add(RecomendacionesIA.SINTOMAS[i]);
            }
            if (sintomas.isEmpty()) {
                setTextoError("Seleccione al menos un síntoma antes de analizar.");
                return;
            }
        }
 
        // Construir prompt
        String prompt = "produccion".equals(modoActual)
                ? logica.construirPromptProduccion()
                : logica.construirPromptSalud(sintomas);
 
        // Mostrar indicador de carga
        setTextoPlaceholder("Analizando con IA, por favor espere...");
        lblBadge.setText("");
 
        // Llamada asíncrona para no bloquear la UI
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                return logica.llamarAPI(prompt);
            }
            @Override
            protected void done() {
                try {
                    String resp = get();
                    setTextoNormal(resp);
                    if ("salud".equals(modoActual)) {
                        if (logica.esRespuestaUrgente()) {
                            mostrarBadge("⚠ Requiere atención",
                                new Color(255, 235, 238), new Color(183, 28, 28));
                        } else {
                            mostrarBadge("✓ Guía de cuidado",
                                new Color(232, 245, 233), new Color(46, 125, 50));
                        }
                    }
                } catch (Exception ex) {
                    setTextoError("Error al conectar con la IA: " + ex.getMessage());
                }
            }
        };
        worker.execute();
        
        
        
    }
 
    private void exportar() {
        if (logica.getRespuestaActual().isBlank()) {
            JOptionPane.showMessageDialog(this,
                "Primero genera una recomendación.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser();
        String nombre = logica.getAnimalActual() != null ? logica.getAnimalActual()[0] : "animal";
        fc.setSelectedFile(new File("recomendacion_" + nombre + ".txt"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                logica.exportarRecomendacion(fc.getSelectedFile());
                JOptionPane.showMessageDialog(this,
                    "Archivo guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS DE TEXTO Y BADGE
    // ─────────────────────────────────────────────────────────────────────────
 
    private void setTextoPlaceholder(String texto) {
        txtRespuesta.setText(texto);
        txtRespuesta.setForeground(new Color(140, 140, 140));
        txtRespuesta.setFont(new Font("SansSerif", Font.ITALIC, 13));
    }
 
    private void setTextoNormal(String texto) {
        txtRespuesta.setText(texto);
        txtRespuesta.setForeground(Color.BLACK);
        txtRespuesta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtRespuesta.setCaretPosition(0);
    }
 
    private void setTextoError(String texto) {
        txtRespuesta.setText(texto);
        txtRespuesta.setForeground(new Color(180, 0, 0));
        txtRespuesta.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }
 
    private void mostrarBadge(String texto, Color fondo, Color letra) {
        lblBadge.setText(texto);
        lblBadge.setBackground(fondo);
        lblBadge.setForeground(letra);
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS DE CONSTRUCCIÓN DE UI
    // ─────────────────────────────────────────────────────────────────────────
 
    /** Crea un panel con barra de título verde. */
    private JPanel crearSeccion(String icono, String titulo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BORDE_GRIS);
        p.setBorder(BorderFactory.createLineBorder(BORDE_GRIS, 1, true));
 
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        header.setBackground(VERDE_MEDIO);
        JLabel lbl = new JLabel(icono + "  " + titulo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        p.add(header, BorderLayout.NORTH);
        return p;
    }
 
    /** Banner de información coloreado. */
    private JPanel crearBanner(String texto, Color fondo, Color letra) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(fondo);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(letra.brighter(), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        JLabel l = new JLabel("<html><body style='width:460px'>" + texto + "</body></html>");
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(letra);
        p.add(l, BorderLayout.CENTER);
        return p;
    }
 
    private JButton crearBoton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        return b;
    }
 
    private void estilizarToggle(JToggleButton b, boolean activo) {
        b.setBackground(activo ? VERDE_CLARO : Color.WHITE);
        b.setForeground(activo ? VERDE_OSCURO : new Color(100, 100, 100));
        b.setBorder(BorderFactory.createLineBorder(
            activo ? VERDE_BTN : BORDE_GRIS, activo ? 2 : 1));
        b.setOpaque(true);
    }
 
    /** Envuelve un panel con márgenes laterales. */
    private JPanel conMargen(JPanel sec) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(FONDO_PANEL);
        wrap.setBorder(new EmptyBorder(0, 10, 0, 10));
        wrap.add(sec, BorderLayout.CENTER);
        return wrap;
    }
 
    /** Espacio vertical invisible. */
    private Component espacio(int alto) {
        return Box.createVerticalStrut(alto);
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    // MAIN
    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
       
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setClosable(true);
        setMaximizable(true);
        setResizable(true);

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

    private void limpiar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
