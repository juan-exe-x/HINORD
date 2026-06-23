
package interfaz;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import java.awt.Font;
import java.awt.Color;
import org.jfree.chart.plot.PiePlot;
import clases.conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class mdiEstadisticas extends javax.swing.JInternalFrame {



   
    private JPanel crearTarjeta(String titulo, String valor, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(180, 90));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValor.setForeground(Color.WHITE);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);

        return panel;
    }
    
    private void cargarKPIs() {

    int total = 0, hembras = 0, machos = 0;
    int sanos = 0, tratamiento = 0;

    try (Connection cn = conexion.conectar()) {

        // INVENTARIO
        String sqlInv = """
            SELECT
                COUNT(*) AS total,
                SUM(CASE WHEN sexo = 'HEMBRA' THEN 1 ELSE 0 END) AS hembras,
                SUM(CASE WHEN sexo = 'MACHO' THEN 1 ELSE 0 END) AS machos
            FROM registro
        """;

        PreparedStatement ps = cn.prepareStatement(sqlInv);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            total = rs.getInt("total");
            hembras = rs.getInt("hembras");
            machos = rs.getInt("machos");
        }

        // SALUD
        String sqlSalud = """
            SELECT
                SUM(CASE WHEN p.idregistro IS NULL THEN 1 ELSE 0 END) AS sanos,
                SUM(CASE WHEN p.idregistro IS NOT NULL THEN 1 ELSE 0 END) AS tratamiento
            FROM registro r
            LEFT JOIN prevenciones p ON r.idregistro = p.idregistro
        """;

        PreparedStatement ps2 = cn.prepareStatement(sqlSalud);
        ResultSet rs2 = ps2.executeQuery();

        if (rs2.next()) {
            sanos = rs2.getInt("sanos");
            tratamiento = rs2.getInt("tratamiento");
        }

    } catch (Exception e) {
        System.out.println("Error KPIs: " + e.getMessage());
    }

    // 🔥 LIMPIAR Y AGREGAR TARJETAS
    panelDashboard.removeAll();

    panelDashboard.add(crearTarjeta("TOTAL", String.valueOf(total), new Color(52, 152, 219)));
    panelDashboard.add(crearTarjeta("HEMBRAS", String.valueOf(hembras), new Color(231, 76, 60)));
    panelDashboard.add(crearTarjeta("MACHOS", String.valueOf(machos), new Color(46, 204, 113)));
    panelDashboard.add(crearTarjeta("SANOS", String.valueOf(sanos), new Color(39, 174, 96)));
    panelDashboard.add(crearTarjeta("EN TRATAMIENTO", String.valueOf(tratamiento), new Color(241, 196, 15)));

    panelDashboard.revalidate();
    panelDashboard.repaint();
}



public mdiEstadisticas() {
    initComponents();
 cargarKPIs();
    
}

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelDashboard = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jPanel1.setBackground(new java.awt.Color(165, 214, 167));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ESTADISTICAS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 24), new java.awt.Color(0, 0, 0))); // NOI18N

        javax.swing.GroupLayout panelDashboardLayout = new javax.swing.GroupLayout(panelDashboard);
        panelDashboard.setLayout(panelDashboardLayout);
        panelDashboardLayout.setHorizontalGroup(
            panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        panelDashboardLayout.setVerticalGroup(
            panelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addComponent(panelDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 566, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelgrafico1;
    private javax.swing.JPanel jPanelgrafico2;
    private javax.swing.JPanel panelDashboard;
    // End of variables declaration//GEN-END:variables
}
