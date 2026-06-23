
package interfaz;

import clases.prevenciones;
import clases.vacunas;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;


public class mdiVacunasmascotas extends javax.swing.JInternalFrame {
    
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
        
     vacunas v = new vacunas();
    Object [][] dtvc;

    public mdiVacunasmascotas() {
        initComponents();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextField8 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtnombrevacuna = new javax.swing.JTextField();
        txtidmascotas = new javax.swing.JTextField();
        txtviaadmin = new javax.swing.JTextField();
        txtdosis = new javax.swing.JTextField();
        txtprevencion = new javax.swing.JTextField();
        txtfecproxima = new javax.swing.JTextField();
        txtverinario = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtidvacunas = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtfecapli = new javax.swing.JTextField();
        btnguardar = new javax.swing.JButton();
        btnaliminar = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btnbuscar = new javax.swing.JButton();
        txtbuscar = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(165, 214, 167));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel2.setBackground(new java.awt.Color(165, 214, 167));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "REGISTRO DE PREVENCIONES MASCOTAS", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 24), new java.awt.Color(75, 96, 67))); // NOI18N

        jLabel2.setText("NOMBRE");

        jLabel3.setText("IDMASCOTAS");

        jLabel4.setText("NOMBRE DE LA VACUINA");

        jLabel5.setText("VIA DE ADMINISTRACION");

        jLabel6.setText("DOSIS");

        jLabel8.setText("TIPO DE PREVENCION ");

        jLabel9.setText("FECHA DE APLICACION");

        jLabel10.setText("FECHA DE PROXIMA APLIACION");

        jLabel11.setText("VETERINARIO RESPONSABLE");

        jLabel12.setText("IDVACUNAS");

        txtidvacunas.setText("..");

        jLabel14.setText("BUSCAR");

        btnguardar.setBackground(new java.awt.Color(204, 204, 204));
        btnguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/expediente.png"))); // NOI18N
        btnguardar.setText("GUARDAR");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btnaliminar.setBackground(new java.awt.Color(199, 91, 57));
        btnaliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/expediente (1).png"))); // NOI18N
        btnaliminar.setText("ELIMINAR");
        btnaliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaliminarActionPerformed(evt);
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

        btnbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar.png"))); // NOI18N
        btnbuscar.setText("..");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        txtbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbuscarActionPerformed(evt);
            }
        });

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/vaca (4).png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(75, 75, 75)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtidvacunas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtidmascotas)
                                    .addComponent(txtnombrevacuna)
                                    .addComponent(txtnombre, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtviaadmin, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtdosis, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtprevencion, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtfecapli)
                                    .addComponent(txtfecproxima)
                                    .addComponent(txtverinario)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                        .addComponent(btnbuscar)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnguardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addComponent(btnactualizar)
                                .addGap(31, 31, 31)
                                .addComponent(btnaliminar)))
                        .addGap(48, 48, 48))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(181, 181, 181)
                    .addComponent(jLabel15)
                    .addContainerGap(181, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtidmascotas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtnombrevacuna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtviaadmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtdosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtprevencion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtfecproxima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtfecapli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtverinario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtidvacunas))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(btnaliminar)
                    .addComponent(btnactualizar))
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(210, 210, 210)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(211, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtbuscarActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
       try {
        String texto = txtbuscar.getText().trim();
        String nombremascota = "";
        int idvacunas = -1;

        // Si es número, busca por ID de vacuna
        if (texto.matches("\\d+")) {
            idvacunas = Integer.parseInt(texto);
        } else {
            nombremascota = texto; 
        }

        Object[][] datos = v.buscarPornombremascotaoidvacunas(nombremascota, idvacunas);

        if (datos.length > 0) {
            
       
            if (datos[0].length == 2) {
                txtnombre.setText(datos[0][0].toString());
                txtidmascotas.setText(datos[0][1].toString());
                JOptionPane.showMessageDialog(null, "Mascota encontrada.");
            }

           
            else if (datos[0].length == 10) {
                txtnombre.setText(datos[0][0].toString());           
                txtidmascotas.setText(String.valueOf(datos[0][1])); 
                txtidvacunas.setText(String.valueOf(datos[0][2]));   
                txtnombrevacuna.setText(datos[0][3].toString());     
                txtprevencion.setText(datos[0][4].toString());       
                txtdosis.setText(String.valueOf(datos[0][5]));      
                txtviaadmin.setText(datos[0][6].toString());         
                txtfecapli.setText(datos[0][7].toString());          
                txtfecproxima.setText(datos[0][8].toString());       
                txtverinario.setText(datos[0][9].toString());        

                JOptionPane.showMessageDialog(null, "Datos completos encontrados.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "No se encontró ningún registro con ese ID o nombre.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "ID no válido: " + e.getMessage());
    }

        
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
       String nombre = txtnombre.getText();
       String idmascotastxt = txtidmascotas.getText();
       String nombrevacuna = txtnombrevacuna.getText();
       String tipoprevencion = txtprevencion.getText();
       String viaadministracion = txtviaadmin.getText();
       String dosistexto = txtdosis.getText();
       String fecaplicadatexto = txtfecapli.getText();
       String fecproximatexto = txtfecproxima.getText();
       String veterinario = txtverinario.getText();
      
       
        if (nombre.isEmpty() || idmascotastxt.isEmpty() || nombrevacuna.isEmpty() || tipoprevencion.isEmpty() || viaadministracion.isEmpty() || dosistexto.isEmpty() || fecaplicadatexto.isEmpty() || fecproximatexto.isEmpty() || veterinario.isEmpty() ) {
            
        }else{
            try {
                int idmascotas = Integer.parseInt(idmascotastxt);
               
                double dosis = Double.parseDouble(dosistexto);
                
                java.util.Date fechaUtil = new SimpleDateFormat("dd/MM/yyyy").parse(fecaplicadatexto);
                java.sql.Date fechaapli = new java.sql.Date(fechaUtil.getTime());
                
                java.util.Date fechaut = new SimpleDateFormat("dd/MM/yyyy").parse(fecproximatexto);
                java.sql.Date fechaproxima = new java.sql.Date(fechaut.getTime());
                
                boolean guardado = v.guardar(nombre, idmascotas, nombrevacuna, tipoprevencion, dosis, fechaapli, fechaproxima, veterinario, viaadministracion);
                if (guardado) {
                    JOptionPane.showMessageDialog(null, "Registro vacunas guardado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo guardar el registro de vacunas", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            }

            }
        
       
    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
       try{
      
       String nombrevacuna = txtnombrevacuna.getText();
       String tipoprevencion = txtprevencion.getText();
       String viaadministracion = txtviaadmin.getText();
       String dosistexto = txtdosis.getText();
       String fecaplicadatexto = txtfecapli.getText();
       String fecproximatexto = txtfecproxima.getText();
       String veterinario = txtverinario.getText();
       String idvacunastxt = txtidvacunas.getText();
       
       if (nombrevacuna.isEmpty()|| viaadministracion.isEmpty() || dosistexto.isEmpty() || fecaplicadatexto.isEmpty() || fecproximatexto.isEmpty() || veterinario.isEmpty() || tipoprevencion.isEmpty() || idvacunastxt.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
    }
       
       int idvacunas = Integer.parseInt(idvacunastxt);

       double dosis = Double.parseDouble(dosistexto);
       System.out.println("ID a actualizar: " + idvacunas);
       
        java.sql.Date fecnaca = convertirFechaFlexible(fecaplicadatexto);
         java.sql.Date fecna = convertirFechaFlexible(fecproximatexto);
        
       v.actualizar(idvacunas, nombrevacuna, tipoprevencion, dosis, fecnaca, fecna, veterinario, viaadministracion);
        
        } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Edad, IDICA y peso deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ParseException ex) {
        JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use yyyy/MM/dd o dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
    }


    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btnaliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaliminarActionPerformed
       try {
         String idpvacunastxt = txtidvacunas.getText();

            if (idpvacunastxt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar o buscar un nombre primero");
                return;
            }
            
            int idvacunas = Integer.parseInt(idpvacunastxt);

            int confirmar = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar a " + idpvacunastxt + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {

               v.eliminar(idvacunas);
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");

                txtnombre.setText("");
                txtidmascotas.setText("");
                txtnombrevacuna.setText("");
                txtprevencion.setText("");
                txtdosis.setText("");
                txtfecapli.setText("");
                txtfecproxima.setText("");
                txtverinario.setText("");
                txtviaadmin.setText("");
                txtidvacunas.setText("");
                
                
                
                
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnaliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnaliminar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtdosis;
    private javax.swing.JTextField txtfecapli;
    private javax.swing.JTextField txtfecproxima;
    private javax.swing.JTextField txtidmascotas;
    private javax.swing.JLabel txtidvacunas;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtnombrevacuna;
    private javax.swing.JTextField txtprevencion;
    private javax.swing.JTextField txtverinario;
    private javax.swing.JTextField txtviaadmin;
    // End of variables declaration//GEN-END:variables
}
