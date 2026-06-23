
package clases;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Tema visual reutilizable para todo el sistema ganadero
 */
public class TemaGanadero {

    // ═══════════════════════════════════════
    // 🎨 COLORES
    // ═══════════════════════════════════════
    public static final Color VERDE_OSCURO = new Color(27, 94, 32);
    public static final Color VERDE_MEDIO  = new Color(46, 125, 50);
    public static final Color VERDE_CLARO  = new Color(232, 245, 233);

    public static final Color DORADO       = new Color(212, 175, 55);
    public static final Color DORADO_CLARO = new Color(255, 236, 153);

    public static final Color BLANCO       = Color.WHITE;
    public static final Color GRIS_TEXTO   = new Color(55, 55, 55);

    // ═══════════════════════════════════════
    // 🔤 FUENTES
    // ═══════════════════════════════════════
    public static final Font FUENTE_TITULO =
            new Font("Segoe UI", Font.BOLD, 22);

    public static final Font FUENTE_LABEL =
            new Font("Segoe UI", Font.BOLD, 12);

    public static final Font FUENTE_INPUT =
            new Font("Segoe UI", Font.PLAIN, 13);

    public static final Font FUENTE_BOTON =
            new Font("Segoe UI", Font.BOLD, 13);

    // ═══════════════════════════════════════
    // 📦 BORDES
    // ═══════════════════════════════════════
    public static final Border BORDE_INPUT =
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            new Color(180, 180, 180), 1
                    ),
                    BorderFactory.createEmptyBorder(4, 10, 4, 10)
            );

    // ═══════════════════════════════════════
    // 🖼️ APLICAR PANEL PRINCIPAL
    // ═══════════════════════════════════════
    public static void aplicarPanelPrincipal(JPanel panel) {

        panel.setLayout(null);

        panel.setBackground(VERDE_CLARO);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 0, 4, 4,
                                new Color(0, 0, 0, 40)
                        ),
                        BorderFactory.createLineBorder(
                                DORADO, 2
                        )
                )
        );

    }

    // ═══════════════════════════════════════
    // 🐄 CREAR HEADER
    // ═══════════════════════════════════════
    public static JPanel crearHeader(
            String titulo,
            int ancho
    ) {

        JPanel header = new JPanel(null);

        header.setBackground(VERDE_OSCURO);

        header.setBounds(0, 0, ancho, 100);

        JLabel lblTitulo = new JLabel(
                titulo,
                SwingConstants.CENTER
        );

        lblTitulo.setFont(FUENTE_TITULO);

        lblTitulo.setForeground(DORADO);

        lblTitulo.setBounds(0, 30, ancho, 40);

        header.add(lblTitulo);

        return header;

    }

    // ═══════════════════════════════════════
    // ✍️ ESTILIZAR INPUT
    // ═══════════════════════════════════════
    public static void estilizarInput(JTextField txt) {

        txt.setFont(FUENTE_INPUT);

        txt.setBorder(BORDE_INPUT);

    }

    // ═══════════════════════════════════════
    // 📋 ESTILIZAR COMBOBOX
    // ═══════════════════════════════════════
    public static void estilizarCombo(JComboBox combo) {

        combo.setFont(FUENTE_INPUT);

        combo.setBackground(BLANCO);

        combo.setBorder(BORDE_INPUT);

    }

    // ═══════════════════════════════════════
    // 🔘 ESTILIZAR BOTÓN
    // ═══════════════════════════════════════
    public static void estilizarBoton(
            JButton btn,
            Color fondo,
            String rutaIcono
    ) {

        btn.setBackground(fondo);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setFont(FUENTE_BOTON);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBorderPainted(false);

        Color hover = fondo.brighter();

        btn.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(
                    java.awt.event.MouseEvent evt
            ) {

                btn.setBackground(hover);

            }

            @Override
            public void mouseExited(
                    java.awt.event.MouseEvent evt
            ) {

                btn.setBackground(fondo);

            }

        });

        try {

            btn.setIcon(
                    new ImageIcon(
                            TemaGanadero.class.getResource(
                                    rutaIcono
                            )
                    )
            );

        } catch (Exception ignored) {
        }

    }

    // ═══════════════════════════════════════
    // 📋 AGREGAR CAMPO
    // ═══════════════════════════════════════
    public static void agregarCampo(

            JPanel panel,

            JLabel label,
            String textoLabel,

            JTextField input,

            int xIcono,
            int xLabel,
            int xInput,
            int y,

            int anchoInput,
            int altoInput,

            String rutaIcono

    ) {

        // LABEL
        label.setText(textoLabel);

        label.setFont(FUENTE_LABEL);

        label.setForeground(GRIS_TEXTO);

        label.setBounds(
                xLabel,
                y,
                anchoInput,
                18
        );

        panel.add(label);

        // ICONO
        if (rutaIcono != null) {

            try {

                JLabel icono = new JLabel(

                        new ImageIcon(
                                TemaGanadero.class.getResource(
                                        rutaIcono
                                )
                        )

                );

                icono.setBounds(
                        xIcono,
                        y + 20,
                        24,
                        24
                );

                panel.add(icono);

            } catch (Exception ignored) {
            }

        }

        // INPUT
        estilizarInput(input);

        input.setBounds(
                xInput,
                y + 20,
                anchoInput,
                altoInput
        );

        panel.add(input);

    }

}