package clases;

import interfaz.mdiRegistro;
import java.awt.*;
import javax.swing.*;

public class panelmenudegradado extends JDesktopPane {

    private final Image imagen;

    public panelmenudegradado() {
        imagen = new ImageIcon(getClass().getResource("/imagenes/geia.png")).getImage();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // 🔹 Suavizado
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 🔹 Degradado
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(34, 139, 34),
                0, getHeight(), new Color(144, 238, 144)
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 🔹 Imagen
        if (imagen != null) {
            g2d.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        int maxX = 0;
        int maxY = 0;
        for (javax.swing.JInternalFrame frame : getAllFrames()) {
            int frameRight = frame.getX() + frame.getWidth();
            int frameBottom = frame.getY() + frame.getHeight();
            if (frameRight > maxX) {
                maxX = frameRight;
            }
            if (frameBottom > maxY) {
                maxY = frameBottom;
            }
        }
        // Si no hay frames abiertos, usa el tamaño actual del panel
        if (maxX == 0 && maxY == 0) {
            return super.getPreferredSize();
        }
        return new java.awt.Dimension(maxX + 30, maxY + 30);
    }
}
