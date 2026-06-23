
package interfaz;

import clases.conexion;
import java.awt.Font;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;


public class mdiProduccionC extends javax.swing.JInternalFrame {
    
    Connection con = conexion.conectar();

    
    public mdiProduccionC() {
        initComponents();
         txtarearespuesta.setLineWrap(true);
        txtarearespuesta.setWrapStyleWord(true);
        txtarearespuesta.setEditable(false);
        txtarearespuesta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtarearespuesta.setMargin(new Insets(8, 8, 8, 8));


        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        txtarearespuesta.setRows(8);
    }
    
    private void ajustarAlturaSegunLineas(JTextArea area, String texto, int minRows, int maxRows) {
    if (area == null) return;
    int lines = 0;
    if (texto != null && !texto.isEmpty()) {
        lines = texto.split("\r?\n").length;
    }
    int rows = Math.max(minRows, Math.min(lines, maxRows));
    area.setRows(rows);
    area.revalidate();
}

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtIDICA = new javax.swing.JTextField();
        txtedad = new javax.swing.JTextField();
        txtpeso = new javax.swing.JTextField();
        txtidregistro = new javax.swing.JTextField();
        txtbuscar = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtarearespuesta = new javax.swing.JTextArea();
        btnrecomendaciones = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(165, 214, 167));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBackground(new java.awt.Color(165, 214, 167));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RECOMENDACIONES DE PRODUCCIÓN DE CARNE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 24), new java.awt.Color(75, 96, 67))); // NOI18N

        jLabel1.setText("NOMBRE ");

        jLabel2.setText("IDICA");

        jLabel3.setText("EDAD");

        jLabel5.setText("PESO");

        jLabel6.setText("IDREGISTRO");

        jLabel8.setText("BUSCAR");

        btnbuscar.setBackground(new java.awt.Color(204, 204, 204));
        btnbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar.png"))); // NOI18N
        btnbuscar.setText("..");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        txtarearespuesta.setBackground(new java.awt.Color(245, 245, 220));
        txtarearespuesta.setColumns(20);
        txtarearespuesta.setRows(5);
        jScrollPane1.setViewportView(txtarearespuesta);

        btnrecomendaciones.setBackground(new java.awt.Color(204, 204, 204));
        btnrecomendaciones.setText("RECOMENDACIONES");
        btnrecomendaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrecomendacionesActionPerformed(evt);
            }
        });

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/vaca (4).png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIDICA, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtedad, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpeso, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtidregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnbuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnrecomendaciones)
                .addGap(112, 112, 112))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(325, 325, 325)
                    .addComponent(jLabel15)
                    .addContainerGap(326, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtIDICA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtpeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtidregistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27)
                .addComponent(btnrecomendaciones)
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(133, 133, 133)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(134, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        String valor = txtbuscar.getText().trim();
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
                txtnombre.setText(rs.getString("nombre"));
                txtIDICA.setText(rs.getString("IDICA"));
                txtedad.setText(rs.getString("edad"));
                txtpeso.setText(rs.getString("peso"));
                txtidregistro.setText(rs.getString("idregistro"));

            }else{
                JOptionPane.showMessageDialog(null,"No existe ese registro" );
                this.limpiar();
            }

        } catch (Exception e) {
            System.err.println("Errror"+e);
        }
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void btnrecomendacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrecomendacionesActionPerformed
        String nombre = txtnombre.getText().trim();
        String IDICA = txtIDICA.getText().trim();
        String edad = txtedad.getText().trim();
        String peso = txtpeso.getText().trim();
       
        if (nombre.isEmpty() || IDICA.isEmpty()|| edad.isEmpty()|| peso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe llenar los campos antes de pedir recomendaciones");
            return;
        }

        String prompt = "La vaca llamada " + nombre + " con IDICA " + IDICA +
        " tiene " + edad + " años, pesa " + peso + " kg, " +
        "Dame recomendaciones para mejorar su producción de carne para luego venderla y sacar la mejor ganacia posible " +
        "según buenas prácticas ganaderas.";

       
        txtarearespuesta.setText("Obteniendo recomendaciones... por favor espere.");
        txtarearespuesta.setCaretPosition(0);
        btnrecomendaciones.setEnabled(false);

        // SwingWorker para no bloquear la UI
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                com.produccion.ia.GeminiIA gemini = new com.produccion.ia.GeminiIA();
                return gemini.responder(prompt);
            }

            @Override
            protected void done() {
                try {
                    String resp = get(); // se ejecuta en el EDT
                    if (resp == null) resp = "No se recibió respuesta.";
                    // Asegurar saltos de línea reales
                    resp = resp.replace("\\n", "\n");
                    txtarearespuesta.setText(resp);
                    txtarearespuesta.setCaretPosition(0);

                    ajustarAlturaSegunLineas(txtarearespuesta, resp, 5, 20);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    txtarearespuesta.setText("Error al obtener respuesta: " + ex.getMessage());
                } finally {
                    btnrecomendaciones.setEnabled(true);
                }
            }
        }.execute();

    }//GEN-LAST:event_btnrecomendacionesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnrecomendaciones;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtIDICA;
    private javax.swing.JTextArea txtarearespuesta;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtedad;
    private javax.swing.JTextField txtidregistro;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtpeso;
    // End of variables declaration//GEN-END:variables

    private void limpiar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
