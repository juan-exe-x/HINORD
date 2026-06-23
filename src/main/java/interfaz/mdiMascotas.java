
package interfaz;

import clases.mascotas;
import clases.prevenciones;
import java.awt.HeadlessException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;


public class mdiMascotas extends javax.swing.JInternalFrame {
    
      private java.sql.Date convertirFechaFlexible(String textoFecha) throws ParseException {
    String[] formatosPosibles = {
        "dd/MM/yyyy", "yyyy/MM/dd", "dd-MM-yyyy", "yyyy-MM-dd"
    };
    
    for (String formato : formatosPosibles) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            sdf.setLenient(false); 
            java.util.Date fechaUtil = sdf.parse(textoFecha);
            return new java.sql.Date(fechaUtil.getTime());
        } catch (ParseException e) {
           
        }
    }
    throw new ParseException("Formato de fecha no reconocido: " + textoFecha, 0);
}
    
    
    mascotas m = new mascotas();
    Object [][] dtmt;


    public mdiMascotas() {
        initComponents();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtespecie = new javax.swing.JTextField();
        txtraza = new javax.swing.JTextField();
        txtcolor = new javax.swing.JTextField();
        cbosexo = new javax.swing.JComboBox<>();
        txtfecnac = new javax.swing.JTextField();
        txtedad = new javax.swing.JTextField();
        cboesteril = new javax.swing.JComboBox<>();
        txtbuscar = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        txtidmascotas = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(189, 132, 26));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBackground(new java.awt.Color(165, 214, 167));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "REGISTRO DE MASCOTAS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 24), new java.awt.Color(75, 96, 67))); // NOI18N

        jLabel1.setBackground(new java.awt.Color(47, 62, 30));
        jLabel1.setText("NOMBRE");

        jLabel2.setText("ESPECIE");

        jLabel3.setText("RAZA");

        jLabel4.setText("EDAD");

        jLabel5.setText("SEXO");

        jLabel6.setText("COLOR");

        jLabel7.setText("FECHA DE NACIMIENTO");

        jLabel8.setText("ESTERLIZADO");

        jLabel9.setText("IDMASCOTA");

        jLabel10.setText("BUSCAR");

        cbosexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------SELECCIONE------", " ", "MACHO", " ", "HEMBRA" }));

        cboesteril.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------SELECCIONE------", " ", "SI", " ", "NO" }));

        btnbuscar.setBackground(new java.awt.Color(204, 204, 204));
        btnbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar.png"))); // NOI18N
        btnbuscar.setText("...");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        btnguardar.setBackground(new java.awt.Color(204, 204, 204));
        btnguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/expediente.png"))); // NOI18N
        btnguardar.setText("GUARDAR");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btnactualizar.setBackground(new java.awt.Color(204, 204, 204));
        btnactualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnactualizar.setText("ACTUALIZAR");
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });

        btneliminar.setBackground(new java.awt.Color(199, 91, 57));
        btneliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/expediente (1).png"))); // NOI18N
        btneliminar.setText("ELIMINAR");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        txtidmascotas.setText("..");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/vaca (4).png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtfecnac, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcolor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtraza, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                .addComponent(txtespecie, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                .addComponent(txtnombre)
                                .addComponent(cbosexo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtedad, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnguardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnactualizar)
                        .addGap(18, 18, 18)
                        .addComponent(btneliminar))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(51, 51, 51)
                            .addComponent(cboesteril, 0, 172, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(51, 51, 51)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addComponent(txtidmascotas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(165, 165, 165)
                    .addComponent(jLabel11)
                    .addContainerGap(165, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1)
                                            .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel2))
                                    .addComponent(txtespecie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtraza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(12, 12, 12))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(cbosexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6))
                                    .addComponent(txtcolor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7))
                            .addComponent(txtfecnac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8))
                    .addComponent(cboesteril, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtidmascotas))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(btnactualizar)
                    .addComponent(btneliminar))
                .addContainerGap(63, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(172, 172, 172)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(224, Short.MAX_VALUE)))
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

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
       String nombre = txtnombre.getText();
       String especie = txtespecie.getText();
       String raza = txtraza.getText();
       String edadtxt = txtedad.getText();
       String sexo = cbosexo.getSelectedItem().toString();
       String color = txtcolor.getText();
       String fecnactxt = txtfecnac.getText();
       String esterilizado = cboesteril.getSelectedItem().toString();
     
       
        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || edadtxt.isEmpty() || sexo.isEmpty() || color.isEmpty() || fecnactxt.isEmpty() || esterilizado.isEmpty()  ) {
            
            
        }else{
            try {
                int edad = Integer.parseInt(edadtxt);
                java.util.Date fechaut = new SimpleDateFormat("dd/MM/yyyy").parse(fecnactxt);
                java.sql.Date fecnac = new java.sql.Date(fechaut.getTime());
                
                boolean guardado = m.guardar(nombre, especie, raza, edad, sexo, color, fecnac, esterilizado);
                if (guardado) {
                    JOptionPane.showMessageDialog(null, "Registro guardado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo guardar el registro", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
       

    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        try{
       String nombre = txtnombre.getText();
       String especie = txtespecie.getText();
       String raza = txtraza.getText();
       String edadtxt = txtedad.getText();
       String sexo = cbosexo.getSelectedItem().toString();
       String color = txtcolor.getText();
       String fecnactxt = txtfecnac.getText();
       String esterilizado = cboesteril.getSelectedItem().toString();
       String idmascotastxt = txtidmascotas.getText();
       
       if (nombre.isEmpty()|| especie.isEmpty() || raza.isEmpty() || edadtxt.isEmpty() || sexo.isEmpty() || color.isEmpty() || fecnactxt.isEmpty() ||esterilizado.isEmpty() || idmascotastxt.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
    }
     
       
       int idmascotas = Integer.parseInt(idmascotastxt);

       int edad =  Integer.parseInt(edadtxt);
       System.out.println("ID a actualizar: " + idmascotas);
       
        java.sql.Date fecnaca = convertirFechaFlexible(fecnactxt);
           
        m.actualizar(idmascotas, nombre, especie, raza, edad, sexo, color, fecnaca, esterilizado);
  
        } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Edad, IDICA y peso deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ParseException ex) {
        JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use yyyy/MM/dd o dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
    }
        
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
       try {
         String idmascotastxt = txtidmascotas.getText();

            if (idmascotastxt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar o buscar un nombre primero");
                return;
            }
            
            int idmascotas = Integer.parseInt(idmascotastxt);

            int confirmar = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar a " + idmascotas + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {

                m.eliminar(idmascotas);
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");

                txtnombre.setText("");
                txtespecie.setText("");
                txtraza.setText("");
                txtedad.setText("");
                cbosexo.setSelectedIndex(0);
                txtfecnac.setText("");
                cboesteril.setSelectedIndex(0);
                txtidmascotas.setText("");
                
               
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        } 
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        try {
            int idmascotas = Integer.parseInt(txtbuscar.getText());
            dtmt = m.buscaridmascota(idmascotas);
            if (dtmt.length> 0){
                txtnombre.setText(dtmt[0][0].toString());
                txtespecie.setText(dtmt[0][1].toString());
                txtraza.setText(dtmt[0][2].toString());
                txtedad.setText(dtmt[0][3].toString());
                cbosexo.setSelectedItem(dtmt[0][4].toString());
                txtcolor.setText(dtmt[0][5].toString());
                txtfecnac.setText(dtmt[0][6].toString());
                cboesteril.setSelectedItem(dtmt[0][7].toString());
                txtidmascotas.setText(dtmt[0][8].toString());
                

            }else{
                JOptionPane.showMessageDialog(null, "idmascotas no encontrado");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null,"idmascotas no valido"+e);
        }
    }//GEN-LAST:event_btnbuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JComboBox<String> cboesteril;
    private javax.swing.JComboBox<String> cbosexo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcolor;
    private javax.swing.JTextField txtedad;
    private javax.swing.JTextField txtespecie;
    private javax.swing.JTextField txtfecnac;
    private javax.swing.JLabel txtidmascotas;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtraza;
    // End of variables declaration//GEN-END:variables
}
