package estilos;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * ╔══════════════════════════════════════════════════════╗
 *  EstiloFormularioBase
 *
 *  Clase utilitaria estática con todos los métodos de
 *  estilo del sistema. Cualquier formulario puede llamar
 *  estos métodos sin extender ninguna clase, lo que
 *  permite usarlos en JFrame, JInternalFrame, JDialog, etc.
 *
 *  USO RÁPIDO:
 *    EstiloFormularioBase.aplicarHeader(jPanel1, "MI FORMULARIO");
 *    EstiloFormularioBase.aplicarCampo(panel, label, "NOMBRE", txtNombre, y);
 *    EstiloFormularioBase.estilizarBoton(btn, TemaFinca.BTN_GUARDAR, "/img/save.png");
 * ╚══════════════════════════════════════════════════════╝
 */
public final class EstiloFormularioBase {

    private EstiloFormularioBase() {}

    // ═══════════════════════════════════════════════════════
    //  PANEL PRINCIPAL
    // ═══════════════════════════════════════════════════════

    /**
     * Configura el panel principal con el fondo verde claro,
     * borde dorado y layout nulo.
     * @param panel
     */
    public static void aplicarPanelPrincipal(JPanel panel) {
        panel.setLayout(null);
        panel.setBackground(TemaFinca.VERDE_CLARO);
        panel.setPreferredSize(new Dimension(TemaFinca.ANCHO_PANEL, 720));
        panel.setBorder(TemaFinca.bordePanel());
    }

    // ═══════════════════════════════════════════════════════
    //  HEADER VERDE OSCURO CON IMAGEN DE FONDO
    // ═══════════════════════════════════════════════════════

    /**
     * Crea y agrega el header verde oscuro con título dorado
     * y la imagen de fondo semitransparente al panel dado.
     *
     * @param panel        Panel padre (jPanel1)
     * @param titulo       Texto del título (ej: "REGISTRO ANIMAL")
     * @param rutaImagen   Ruta del recurso de imagen (puede ser null)
     */
    public static void aplicarHeader(JPanel panel, String titulo, String rutaImagen) {
        final String ruta = rutaImagen;

        JPanel header = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (ruta == null) return;
                try {
                    Image img = new ImageIcon(getClass().getResource(ruta)).getImage();
                    Graphics2D g2 = (Graphics2D) g;
                    int imgW = img.getWidth(this);
                    int imgH = img.getHeight(this);
                    double escala = Math.min((double) getWidth() / imgW, (double) getHeight() / imgH);
                    int nuevoW = (int) (imgW * escala);
                    int nuevoH = (int) (imgH * escala);
                    int x = (getWidth()  - nuevoW) / 2;
                    int y = (getHeight() - nuevoH) / 2;
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
                    g2.drawImage(img, x, y, nuevoW, nuevoH, this);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } catch (Exception ignored) {}
            }
        };

        header.setBackground(TemaFinca.VERDE_OSCURO);
        header.setBounds(0, 0, TemaFinca.ANCHO_PANEL, TemaFinca.ALTO_HEADER);

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(TemaFinca.FUENTE_TITULO);
        lblTitulo.setForeground(TemaFinca.DORADO);
        lblTitulo.setBounds(0, 30, TemaFinca.ANCHO_PANEL, 40);
        header.add(lblTitulo);

        panel.add(header);
    }

    // ═══════════════════════════════════════════════════════
    //  BARRA DE BÚSQUEDA
    // ═══════════════════════════════════════════════════════

    /**
     * Crea y agrega la barra de búsqueda al panel.
     * El txtBuscar y btnBuscar ya deben estar declarados en el formulario.
     *
     * @param panel      Panel padre
     * @param txtBuscar  Campo de texto de búsqueda
     * @param btnBuscar  Botón de búsqueda
     * @param etiqueta   Texto de la etiqueta (ej: "🔍 Buscar por IDICA:")
     * @param hint       Placeholder del campo (ej: "Ingrese IDICA...")
     * @param rutaIcono  Ruta del ícono del botón (puede ser null)
     */
    public static void aplicarBarraBusqueda(JPanel panel,
            JTextField txtBuscar, JButton btnBuscar,
            String etiqueta, String hint, String rutaIcono) {

        JPanel panelBuscar = new JPanel(null);
        panelBuscar.setBackground(TemaFinca.VERDE_MEDIO);
        panelBuscar.setBounds(0, TemaFinca.ALTO_HEADER, TemaFinca.ANCHO_PANEL, TemaFinca.ALTO_BARRA);

        JLabel lblBuscar = new JLabel(etiqueta);
        lblBuscar.setFont(TemaFinca.FUENTE_BUSCAR);
        lblBuscar.setForeground(TemaFinca.BLANCO);
        lblBuscar.setBounds(20, 12, 160, 25);
        panelBuscar.add(lblBuscar);

        txtBuscar.setBounds(185, 12, 200, 28);
        txtBuscar.setFont(TemaFinca.FUENTE_BUSCAR);
        txtBuscar.setBorder(TemaFinca.bordeBuscar());
        panelBuscar.add(txtBuscar);

        btnBuscar.setBounds(393, 12, 100, 28);
        btnBuscar.setText("BUSCAR");
        estilizarBoton(btnBuscar, TemaFinca.BTN_BUSCAR, rutaIcono);
        panelBuscar.add(btnBuscar);

        panel.add(panelBuscar);

        // Placeholder animado
        aplicarPlaceholder(txtBuscar, hint);
    }

    // ═══════════════════════════════════════════════════════
    //  CAMPO DE FORMULARIO (label + input + ícono opcional)
    // ═══════════════════════════════════════════════════════

    /**
     * Agrega un campo completo (etiqueta + input + ícono) al panel.
     * Usa posición Y calculada automáticamente con START_Y + GAP_Y * índice.
     *
     * @param panel       Panel padre
     * @param label       JLabel del campo
     * @param textoLabel  Texto de la etiqueta
     * @param input       JTextField del campo
     * @param indice      Posición en el formulario (0 = primero, 1 = segundo, ...)
     * @param rutaIcono   Ruta del ícono (puede ser null)
     */
    public static void aplicarCampo(JPanel panel,
            JLabel label, String textoLabel,
            JTextField input, int indice, String rutaIcono) {

        int y = TemaFinca.START_Y + TemaFinca.GAP_Y * indice;
        aplicarCampoEnY(panel, label, textoLabel, input, y, rutaIcono);
    }

    /**
     * Igual que aplicarCampo pero con Y explícita (más flexible).
     */
    public static void aplicarCampoEnY(JPanel panel,
            JLabel label, String textoLabel,
            JTextField input, int y, String rutaIcono) {

        Border bordeInput = TemaFinca.bordeCampo();

        label.setText(textoLabel);
        label.setFont(TemaFinca.FUENTE_LABEL);
        label.setForeground(TemaFinca.GRIS_TEXTO);
        label.setBounds(TemaFinca.X_LABEL, y, TemaFinca.ANCHO_INPUT, 18);
        panel.add(label);

        if (rutaIcono != null) {
            try {
                JLabel icono = new JLabel(new ImageIcon(
                    EstiloFormularioBase.class.getResource(rutaIcono)));
                icono.setBounds(TemaFinca.X_ICONO, y + 20, 24, 24);
                panel.add(icono);
            } catch (Exception ignored) {}
        }

        input.setFont(TemaFinca.FUENTE_INPUT);
        input.setBorder(bordeInput);
        input.setBounds(TemaFinca.X_INPUT, y + 20, TemaFinca.ANCHO_INPUT, TemaFinca.ALTO_INPUT);
        panel.add(input);
    }

    /**
     * Agrega un ComboBox con su etiqueta al panel.
     */
    public static void aplicarComboBox(JPanel panel,
            JLabel label, String textoLabel,
            JComboBox<?> combo, int indice, String rutaIcono) {

        int y = TemaFinca.START_Y + TemaFinca.GAP_Y * indice;

        label.setText(textoLabel);
        label.setFont(TemaFinca.FUENTE_LABEL);
        label.setForeground(TemaFinca.GRIS_TEXTO);
        label.setBounds(TemaFinca.X_LABEL, y, TemaFinca.ANCHO_INPUT, 18);
        panel.add(label);

        if (rutaIcono != null) {
            try {
                JLabel icono = new JLabel(new ImageIcon(
                    EstiloFormularioBase.class.getResource(rutaIcono)));
                icono.setBounds(TemaFinca.X_ICONO, y + 18, 28, 28);
                panel.add(icono);
            } catch (Exception ignored) {}
        }

        combo.setBounds(TemaFinca.X_INPUT, y + 20, TemaFinca.ANCHO_INPUT, TemaFinca.ALTO_INPUT);
        combo.setFont(TemaFinca.FUENTE_INPUT);
        combo.setBackground(TemaFinca.BLANCO);
        combo.setBorder(TemaFinca.bordeCampo());
        panel.add(combo);
    }

    // ═══════════════════════════════════════════════════════
    //  BOTONES
    // ═══════════════════════════════════════════════════════

    /**
     * Aplica el estilo pro a cualquier botón.
     * Incluye color de fondo, fuente, cursor y efecto hover.
     */
    public static void estilizarBoton(JButton btn, Color fondo, String rutaIcono) {
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(TemaFinca.FUENTE_BTN);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        Color hover = fondo.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover); }
            public void mouseExited(java.awt.event.MouseEvent evt)  { btn.setBackground(fondo); }
        });

        if (rutaIcono != null) {
            try {
                btn.setIcon(new ImageIcon(EstiloFormularioBase.class.getResource(rutaIcono)));
            } catch (Exception ignored) {}
        }
    }

    /**
     * Coloca y estiliza los 3 botones estándar (Guardar, Actualizar, Eliminar)
     * en la posición correcta según el número de campos del formulario.
     *
     * @param panel          Panel padre
     * @param btnGuardar     Botón guardar
     * @param btnActualizar  Botón actualizar
     * @param btnEliminar    Botón eliminar
     * @param totalCampos    Número de campos del formulario (para calcular Y)
     * @param rutaGuardar    Ruta ícono guardar    (puede ser null)
     * @param rutaActualizar Ruta ícono actualizar (puede ser null)
     * @param rutaEliminar   Ruta ícono eliminar   (puede ser null)
     * @return Y final del panel de botones (útil para setPreferredSize)
     */
    public static int aplicarBotonesAccion(JPanel panel,
            JButton btnGuardar, JButton btnActualizar, JButton btnEliminar,
            int totalCampos,
            String rutaGuardar, String rutaActualizar, String rutaEliminar) {

        int yBotones = TemaFinca.START_Y + TemaFinca.GAP_Y * totalCampos + 10;

        btnGuardar.setBounds(20,  yBotones, 150, 40);
        btnActualizar.setBounds(185, yBotones, 150, 40);
        btnEliminar.setBounds(350, yBotones, 150, 40);

        estilizarBoton(btnGuardar,    TemaFinca.BTN_GUARDAR,    rutaGuardar);
        estilizarBoton(btnActualizar, TemaFinca.BTN_ACTUALIZAR, rutaActualizar);
        estilizarBoton(btnEliminar,   TemaFinca.BTN_ELIMINAR,   rutaEliminar);

        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);

        return yBotones;
    }

    // ═══════════════════════════════════════════════════════
    //  CAMPO OCULTO DE ID
    // ═══════════════════════════════════════════════════════

    /**
     * Estiliza el campo de ID oculto/secundario.
     */
    public static void aplicarCampoId(JPanel panel, JTextField txtId) {
        txtId.setBounds(430, 108, 60, 20);
        txtId.setFont(TemaFinca.FUENTE_ID);
        txtId.setForeground(new Color(150, 150, 150));
        panel.add(txtId);
    }

    // ═══════════════════════════════════════════════════════
    //  AJUSTE FINAL DEL FORMULARIO
    // ═══════════════════════════════════════════════════════

    /**
     * Ajusta el tamaño del panel y del JInternalFrame/JFrame
     * según la Y final de los botones.
     * Llama esto al final de tu método de inicialización.
     *
     * @param panel     Panel principal
     * @param frame     JInternalFrame (puede ser null si usas JFrame)
     * @param yBotones  Valor retornado por aplicarBotonesAccion()
     */
    public static void ajustarTamanio(JPanel panel, JInternalFrame frame, int yBotones) {
        int alturaTotal = yBotones + 70;
        panel.setPreferredSize(new Dimension(TemaFinca.ANCHO_PANEL, alturaTotal));
        if (frame != null) {
            frame.setSize(TemaFinca.ANCHO_PANEL + 20, alturaTotal + 60);
        }
    }

    // ═══════════════════════════════════════════════════════
    //  UTILIDADES
    // ═══════════════════════════════════════════════════════

    /**
     * Aplica un placeholder (hint) animado a cualquier JTextField.
     */
    public static void aplicarPlaceholder(JTextField campo, String hint) {
        campo.setText(hint);
        campo.setForeground(TemaFinca.GRIS_HINT);
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (campo.getText().equals(hint)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (campo.getText().isEmpty()) {
                    campo.setText(hint);
                    campo.setForeground(TemaFinca.GRIS_HINT);
                }
            }
        });
    }
    
    public static void aplicarCampoDoble(
        JPanel panel,
        JLabel label,
        String texto,
        JTextField input,
        int x,
        int y) {

    label.setText(texto);
    label.setFont(TemaFinca.FUENTE_LABEL);
    label.setForeground(TemaFinca.GRIS_TEXTO);
    label.setBounds(x, y, 200, 20);

    input.setBounds(
        x,
        y + 22,
        TemaFinca.ANCHO_INPUT_CORTO,
        TemaFinca.ALTO_INPUT
    );

    input.setFont(TemaFinca.FUENTE_INPUT);
    input.setBorder(TemaFinca.bordeCampo());

    panel.add(label);
    panel.add(input);
}
}