
package clases;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelDegradado extends JPanel {

    private Image imagen; // 🔹 ATRIBUTO (VA AQUÍ)

    // 🔹 CONSTRUCTOR (VA FUERA)
    public PanelDegradado() {
        imagen = new ImageIcon(getClass().getResource("/imagenes/fondo.jpg")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // 🔹 Suavizado (MEJORA CALIDAD)
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 🔹 Degradado
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(34,139,34),
            0, getHeight(), new Color(144,238,144)
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 🔹 Imagen encima
        if (imagen != null) {
            g2d.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
         
}