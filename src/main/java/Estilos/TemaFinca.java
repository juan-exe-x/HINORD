
package estilos;

import java.awt.*;

/**
 * ╔══════════════════════════════════════════════════════╗
 *  TemaFinca — Paleta central del sistema
 *  Todos los colores, fuentes y constantes del tema
 *  verde/dorado están definidos aquí en UN solo lugar.
 *  Para cambiar el look de toda la aplicación, solo
 *  modifica esta clase.
 * ╚══════════════════════════════════════════════════════╝
 */
public final class TemaFinca {

    // ── Constructor privado: clase utilitaria, no se instancia ──
    private TemaFinca() {}

    // ═══════════════════════════════════════════════
    //  COLORES
    // ═══════════════════════════════════════════════
    public static final Color VERDE_OSCURO   = new Color(27,  94,  32);
    public static final Color VERDE_MEDIO    = new Color(46,  125, 50);
    public static final Color VERDE_CLARO    = new Color(232, 245, 233);
    public static final Color DORADO         = new Color(212, 175, 55);
    public static final Color DORADO_CLARO   = new Color(255, 236, 153);
    public static final Color BLANCO         = Color.WHITE;
    public static final Color GRIS_TEXTO     = new Color(55,  55,  55);
    public static final Color GRIS_HINT      = Color.GRAY;

    // Colores de botones de acción
    public static final Color BTN_GUARDAR    = new Color(40,  167, 69);
    public static final Color BTN_ACTUALIZAR = new Color(0,   123, 255);
    public static final Color BTN_ELIMINAR   = new Color(220, 53,  69);
    public static final Color BTN_BUSCAR     = VERDE_MEDIO;

    // Sombra de panel
    public static final Color SOMBRA         = new Color(0, 0, 0, 40);

    // ═══════════════════════════════════════════════
    //  FUENTES
    // ═══════════════════════════════════════════════
    public static final Font FUENTE_TITULO   = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FUENTE_LABEL    = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FUENTE_INPUT    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_BTN      = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FUENTE_BUSCAR   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_ID       = new Font("Segoe UI", Font.PLAIN, 10);

    // ═══════════════════════════════════════════════
    //  DIMENSIONES ESTÁNDAR
    // ═══════════════════════════════════════════════
    public static final int ANCHO_PANEL      = 520;
    public static final int ALTO_HEADER      = 100;
    public static final int ALTO_BARRA       = 50;
    public static final int ALTO_INPUT       = 32;
    public static final int GAP_Y            = 58;   // espacio vertical entre campos
    public static final int START_Y          = 165;  // Y donde empieza el primer campo
    public static final int X_ICONO          = 20;
    public static final int X_LABEL          = 55;
    public static final int X_INPUT          = 55;
    public static final int ANCHO_INPUT      = 430;
    public static final int X_COLUMNA_1 = 40;
    public static final int X_COLUMNA_2 = 330;
    public static final int ANCHO_INPUT_CORTO = 220;

    // ═══════════════════════════════════════════════
    //  BORDES REUTILIZABLES
    // ═══════════════════════════════════════════════
    public static javax.swing.border.Border bordeCampo() {
        return javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10)
        );
    }

    public static javax.swing.border.Border bordePanel() {
        return javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 4, SOMBRA),
            javax.swing.BorderFactory.createLineBorder(DORADO, 2)
        );
    }

    public static javax.swing.border.Border bordeBuscar() {
        return javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(DORADO, 1),
            javax.swing.BorderFactory.createEmptyBorder(2, 7, 2, 7)
        );
    }
}
