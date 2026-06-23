
package interfaz;

import clases.prevenciones;
import clases.registro;
import java.awt.HeadlessException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class mdiPrevencion extends javax.swing.JInternalFrame {
    
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

    
    prevenciones p = new prevenciones();
    Object [][] dtpv;

    
    public mdiPrevencion() {
        initComponents();
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtvacunaaplicada = new javax.swing.JTextField();
        txtviaadmin = new javax.swing.JTextField();
        txtdosis = new javax.swing.JTextField();
        txtfecapli = new javax.swing.JTextField();
        txtfecproxi = new javax.swing.JTextField();
        txtveteresponsable = new javax.swing.JTextField();
        txtenfermedadpreviene = new javax.swing.JTextField();
        btnguardar = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnbuscar = new javax.swing.JButton();
        txtbuscar = new javax.swing.JTextField();
        txtprevenciones = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtIDICA = new javax.swing.JTextField();
        txtidregistro = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBackground(new java.awt.Color(165, 214, 167));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "REGISTRO PREVENCIONES", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 24), new java.awt.Color(75, 96, 67))); // NOI18N

        jLabel1.setText("NOMBRE");

        jLabel3.setText("IDICA");

        jLabel4.setText("IDREGISTRO");

        jLabel5.setText("VACUNA APLICADA");

        jLabel6.setText("VIA DE ADMINISTRACION");

        jLabel7.setText("DOSIS APLICADA");

        jLabel8.setText("FECHA DE APLICACION");

        jLabel9.setText("FECHA PROXIMA ");

        jLabel10.setText("VETERINARIO RESPONSABLE");

        jLabel11.setText("ENFERMEDAD QUE PREVIENE");

        jLabel12.setText("IDPREVENCIONES");

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

        btnbuscar.setBackground(new java.awt.Color(204, 204, 204));
        btnbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar.png"))); // NOI18N
        btnbuscar.setText(".....");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        txtprevenciones.setText("-");

        jLabel13.setText("BUSCAR");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/vaca (4).png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(88, 88, 88)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtvacunaaplicada)
                            .addComponent(txtviaadmin)
                            .addComponent(txtdosis)
                            .addComponent(txtfecapli)
                            .addComponent(txtfecproxi)
                            .addComponent(txtveteresponsable)
                            .addComponent(txtenfermedadpreviene)
                            .addComponent(txtprevenciones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(btneliminar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtnombre)
                            .addComponent(txtIDICA)
                            .addComponent(txtidregistro)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnguardar)
                        .addGap(50, 50, 50)
                        .addComponent(btnactualizar)))
                .addContainerGap(44, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(205, 205, 205)
                    .addComponent(jLabel14)
                    .addContainerGap(205, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(14, 20, Short.MAX_VALUE)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel1)
                                                            .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel3)
                                                            .addComponent(txtIDICA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel4)
                                                            .addComponent(txtidregistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(jLabel5)
                                                            .addComponent(txtvacunaaplicada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jLabel6))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtviaadmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel7))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(txtdosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel8))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(txtfecapli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtfecproxi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtveteresponsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(txtenfermedadpreviene, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtprevenciones))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(btnactualizar)
                    .addComponent(btneliminar))
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(249, 249, 249)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(249, Short.MAX_VALUE)))
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
       String IDICATEXTO = txtIDICA.getText();
       String idregistrotxt = txtidregistro.getText();
       String vacunaaplicada = txtvacunaaplicada.getText();
       String viaadministracion = txtviaadmin.getText();
       String dosistexto = txtdosis.getText();
       String fecaplicadatexto = txtfecapli.getText();
       String fecproximatexto = txtfecproxi.getText();
       String veterinario = txtveteresponsable.getText();
       String enfermedadpreviene = txtenfermedadpreviene.getText();
       
        if (nombre.isEmpty() || IDICATEXTO.isEmpty() || idregistrotxt.isEmpty() || vacunaaplicada.isEmpty() || viaadministracion.isEmpty() || dosistexto.isEmpty() || fecaplicadatexto.isEmpty() || fecproximatexto.isEmpty() || veterinario.isEmpty() || enfermedadpreviene.isEmpty()) {
            
        }else{
            try {
                long IDICA = Long.parseLong(IDICATEXTO);
                int idregistro = Integer.parseInt(idregistrotxt);
                double dosis = Double.parseDouble(dosistexto);
                java.util.Date fechaUtil = new SimpleDateFormat("dd/MM/yyyy").parse(fecaplicadatexto);
                java.sql.Date fechaapli = new java.sql.Date(fechaUtil.getTime());
                
                java.util.Date fechaut = new SimpleDateFormat("dd/MM/yyyy").parse(fecproximatexto);
                java.sql.Date fechaproxima = new java.sql.Date(fechaut.getTime());
                
                boolean guardado = p.guardar(nombre, IDICA, idregistro, vacunaaplicada, viaadministracion, dosis, fechaapli, fechaproxima, veterinario, enfermedadpreviene);
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
       String vacunaaplicada = txtvacunaaplicada.getText();
       String viaadministracion = txtviaadmin.getText();
       String dosistexto = txtdosis.getText();
       String fecaplicadatexto = txtfecapli.getText();
       String fecproximatexto = txtfecproxi.getText();
       String veterinario = txtveteresponsable.getText();
       String enfermedadpreviene = txtenfermedadpreviene.getText();
       String idprevencionestexto = txtprevenciones.getText();
       
       if (vacunaaplicada.isEmpty()|| viaadministracion.isEmpty() || dosistexto.isEmpty() || fecaplicadatexto.isEmpty() || fecproximatexto.isEmpty() || veterinario.isEmpty() || enfermedadpreviene.isEmpty() || idprevencionestexto.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
    }
       
       int idprevenciones = Integer.parseInt(idprevencionestexto);

       double dosis = Double.parseDouble(dosistexto);
       System.out.println("ID a actualizar: " + idprevenciones);
       
        java.sql.Date fecnaca = convertirFechaFlexible(fecaplicadatexto);
         java.sql.Date fecna = convertirFechaFlexible(fecproximatexto);
        
        p.actualizar(idprevenciones, fecnaca, fecna, dosis, veterinario, vacunaaplicada, viaadministracion, enfermedadpreviene);
        
        JOptionPane.showMessageDialog(null, "Vacunaciones Actualizadas Correctamente");
        
        } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ParseException ex) {
        JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use yyyy/MM/dd o dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
    }



    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        try {
         String idprevencionestxt = txtprevenciones.getText();

            if (idprevencionestxt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar o buscar un nombre primero");
                return;
            }
            
            int idprevenciones = Integer.parseInt(idprevencionestxt);

            int confirmar = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar a " + idprevencionestxt + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {

                p.eliminar(idprevenciones);
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");

                txtnombre.setText("");
                txtIDICA.setText("");
                txtvacunaaplicada.setText("");
                txtviaadmin.setText("");
                txtdosis.setText("");
                txtfecapli.setText("");
                txtfecproxi.setText("");
                txtveteresponsable.setText("");
                txtenfermedadpreviene.setText("");
                txtprevenciones.setText("");
                txtidregistro.setText("");
                
                
                
                
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        
         try {
        int id = Integer.parseInt(txtbuscar.getText());
        Object[][] datos = p.buscarPorIDICAOPrevencion(id);

        if (datos.length > 0) {

            
            if (datos[0].length == 3) {
                txtnombre.setText(datos[0][0].toString());
                txtIDICA.setText(datos[0][1].toString());
                txtidregistro.setText(datos[0][2].toString());
                
                JOptionPane.showMessageDialog(null, "Animal encontrado ");
            }

            
            else if (datos[0].length == 11) {
                txtnombre.setText(datos[0][0].toString());
                txtIDICA.setText(datos[0][1].toString());
                txtidregistro.setText(datos[0][2].toString());
                txtprevenciones.setText(datos[0][3].toString());
                txtvacunaaplicada.setText(datos[0][4].toString());
                txtviaadmin.setText(datos[0][5].toString());
                txtdosis.setText(datos[0][6].toString());
                txtfecapli.setText(datos[0][7].toString());
                txtfecproxi.setText(datos[0][8].toString());
                txtveteresponsable.setText(datos[0][9].toString());
                txtenfermedadpreviene.setText(datos[0][10].toString());
                JOptionPane.showMessageDialog(null, "Datos completos encontrados");
            }

        } else {
            JOptionPane.showMessageDialog(null, "No se encontró ningún registro con ese ID.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "ID no válido: " + e.getMessage());
    }

        
    }//GEN-LAST:event_btnbuscarActionPerformed

    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtIDICA;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtdosis;
    private javax.swing.JTextField txtenfermedadpreviene;
    private javax.swing.JTextField txtfecapli;
    private javax.swing.JTextField txtfecproxi;
    private javax.swing.JTextField txtidregistro;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JLabel txtprevenciones;
    private javax.swing.JTextField txtvacunaaplicada;
    private javax.swing.JTextField txtveteresponsable;
    private javax.swing.JTextField txtviaadmin;
    // End of variables declaration//GEN-END:variables
}
