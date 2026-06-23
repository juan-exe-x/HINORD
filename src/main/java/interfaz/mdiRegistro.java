package interfaz;

import clases.registro;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class mdiRegistro extends javax.swing.JInternalFrame {

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

    registro r = new registro();
    Object[][] dtrg;

    public mdiRegistro() {
        initComponents();
        aplicarEstiloPro();
        this.setClosable(true);
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
        txtnombre = new javax.swing.JTextField();
        txtIDICA = new javax.swing.JTextField();
        txtfecnac = new javax.swing.JTextField();
        txtraza = new javax.swing.JTextField();
        txtedad = new javax.swing.JTextField();
        cbosexo = new javax.swing.JComboBox<>();
        txtpeso = new javax.swing.JTextField();
        txtclasificacion = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        btnactualizar = new javax.swing.JButton();
        btnguardar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtidregistro = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cboEstado = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBackground(new java.awt.Color(165, 214, 167));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "REGISTRO ANIMAL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 3, 24), new java.awt.Color(75, 96, 67))); // NOI18N
        jPanel1.setName(""); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N

        jLabel2.setText("IDICA");

        jLabel3.setText("FECHA DE NACIMIENTO");

        jLabel4.setText("RAZA");

        jLabel5.setText("SEXO");

        jLabel6.setText("EDAD");

        jLabel7.setText("CLASIFICACION");

        jLabel8.setText("PESO");

        txtnombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnombreActionPerformed(evt);
            }
        });

        cbosexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--------SELECCIONE---------", "MACHO", "HEMBRA" }));

        jLabel9.setText("BUSCAR");

        btnbuscar.setBackground(new java.awt.Color(204, 204, 204));
        btnbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/buscar.png"))); // NOI18N
        btnbuscar.setText(".....");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        btnactualizar.setBackground(new java.awt.Color(204, 204, 204));
        btnactualizar.setText("ACTUALIZAR");
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });

        btnguardar.setBackground(new java.awt.Color(204, 204, 204));
        btnguardar.setText("GUARDAR");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btneliminar.setBackground(new java.awt.Color(199, 91, 57));
        btneliminar.setText("ELIMINAR");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        jLabel11.setText("NOMBRE");

        txtidregistro.setText("..");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/vaca (4).png"))); // NOI18N

        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--------SELECCIONE--------", "Activo", "Vendido", "Enfermo", "Muerto" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(txtidregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(106, 106, 106)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtpeso)
                            .addComponent(txtedad)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(87, 87, 87)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDICA, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtnombre)
                            .addComponent(txtfecnac, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtraza, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbosexo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(106, 106, 106)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtclasificacion)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnguardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnactualizar)
                                .addGap(29, 29, 29)
                                .addComponent(btneliminar)))))
                .addContainerGap(72, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(193, 193, 193)
                    .addComponent(jLabel14)
                    .addContainerGap(193, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(txtidregistro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIDICA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtfecnac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtraza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbosexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtpeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtclasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btneliminar)
                    .addComponent(btnactualizar)
                    .addComponent(btnguardar))
                .addGap(28, 28, 28)
                .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(238, 238, 238)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(245, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private int idregistroActual = -1;

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        try {
            // Validas que haya un animal seleccionado
            if (idregistroActual == -1) {
                JOptionPane.showMessageDialog(null, "Debe buscar y seleccionar un animal primero");
                return;
            }

            String nombre = txtnombre.getText().trim();

            int confirmar = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de eliminar a " + nombre + "?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {

                String resultado = r.eliminar(idregistroActual); // usa el ID, no el nombre

                JOptionPane.showMessageDialog(null, resultado);

                // Solo limpia si fue exitoso
                if (resultado.equals("Animal eliminado correctamente")) {
                    idregistroActual = -1; // resetea el ID
                    txtnombre.setText("");
                    txtIDICA.setText("");
                    txtfecnac.setText("");
                    txtraza.setText("");
                    txtedad.setText("");
                    cbosexo.setSelectedIndex(0);
                    txtpeso.setText("");
                    txtclasificacion.setText("");
                    cboEstado.setSelectedIndex(0);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        }
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        String nombre = txtnombre.getText();
        String IDICATEXTO = txtIDICA.getText();
        String fecnactexto = txtfecnac.getText();
        String raza = txtraza.getText();
        String sexo = cbosexo.getSelectedItem().toString();
        String edadtexto = txtedad.getText();
        String pesotexto = txtpeso.getText();
        String clasificacion = txtclasificacion.getText();
        String estado = cboEstado.getSelectedItem().toString();

        if (nombre.isEmpty() || IDICATEXTO.isEmpty() || fecnactexto.isEmpty() || raza.isEmpty()
                || sexo.equals("----------SELECCIONE----------") || edadtexto.isEmpty() || pesotexto.isEmpty()
                || clasificacion.isEmpty() || estado.equals("---Seleccione---") ) {
            JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int edad = Integer.parseInt(edadtexto);
                long IDICA = Long.parseLong(IDICATEXTO);
                double peso = Double.parseDouble(pesotexto);

                java.sql.Date fecnac = convertirFechaFlexible(fecnactexto);

                // ✅ CAMBIO: guardar retorna int (el ID generado), no boolean
                int idGenerado = r.guardar(nombre, IDICA, fecnac, raza, sexo, edad, peso, clasificacion,estado);

                if (idGenerado != -1) {
                    // Guardas el ID del animal recién creado
                    idregistroActual = idGenerado;
                    txtidregistro.setText(String.valueOf(idGenerado)); // muestra el ID en el campo

                    JOptionPane.showMessageDialog(null,
                            "Registro guardado correctamente. ID asignado: " + idGenerado);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo guardar el registro",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Edad, IDICA y peso deben ser números",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use dd/MM/yyyy",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnguardarActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
         // Validar que se haya seleccionado un estado válido
    String estadoSeleccionado = cboEstado.getSelectedItem().toString();
    if (estadoSeleccionado.contains("SELECCIONE")) {
        JOptionPane.showMessageDialog(this, 
            "Por favor seleccione el estado del animal.", 
            "Campo requerido", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

        
        try {
            String idTexto = txtidregistro.getText();
            String nombre = txtnombre.getText();
            String IDICATEXTO = txtIDICA.getText();
            String fecnactexto = txtfecnac.getText();
            String raza = txtraza.getText();
            String sexo = cbosexo.getSelectedItem().toString();
            String edadtexto = txtedad.getText();
            String pesotexto = txtpeso.getText();
            String clasificacion = txtclasificacion.getText();
            String estado = cboEstado.getSelectedItem().toString();
            

            if (idTexto.isEmpty() || nombre.isEmpty() || IDICATEXTO.isEmpty() || fecnactexto.isEmpty()
                    || raza.isEmpty() || sexo.equals("----------SELECCIONE----------") || edadtexto.isEmpty()
                    || pesotexto.isEmpty() || clasificacion.isEmpty() || estado.equals("---Seleccione---"))  {
                JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idregistro = Integer.parseInt(idTexto);
            long IDICA = Long.parseLong(IDICATEXTO);
            int edad = Integer.parseInt(edadtexto);
            double peso = Double.parseDouble(pesotexto);

            System.out.println("ID a actualizar: " + idregistro);

            java.sql.Date fecnac = convertirFechaFlexible(fecnactexto);

            boolean actualizado = r.actualizar(idregistro, nombre, IDICA, fecnac, raza, sexo, edad, peso, clasificacion,estado);
            if (actualizado) {
                JOptionPane.showMessageDialog(null, "Registro Actualizado Correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el registro o no hubo cambios.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Edad, IDICA y peso deben ser números", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use yyyy/MM/dd o dd/MM/yyy", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        try {
            int IDICA = Integer.parseInt(txtbuscar.getText());
            dtrg = r.buscarIDICA(IDICA);
            if (dtrg.length > 0) {
                txtnombre.setText(dtrg[0][0].toString());
                txtIDICA.setText(dtrg[0][1].toString());
                txtfecnac.setText(dtrg[0][2].toString());
                txtraza.setText(dtrg[0][3].toString());
                cbosexo.setSelectedItem(dtrg[0][4].toString());
                txtedad.setText(dtrg[0][5].toString());
                txtpeso.setText(dtrg[0][6].toString());
                txtclasificacion.setText(dtrg[0][7].toString());
                txtidregistro.setText(dtrg[0][8].toString());
                idregistroActual = Integer.parseInt(dtrg[0][8].toString());
                String estado = (dtrg[0][9] != null) ? dtrg[0][9].toString() : "Activo";
                cboEstado.setSelectedItem(estado);

            } else {
                JOptionPane.showMessageDialog(null, "IDICA no enconttrado");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "IDICA no valido" + e);
        }
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void txtnombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnombreActionPerformed

    }//GEN-LAST:event_txtnombreActionPerformed

    private void aplicarEstiloPro() {

        // ═══════════════════════════════════════
        // 🎨 COLORES
        // ═══════════════════════════════════════
        java.awt.Color verdeOscuro = new java.awt.Color(27, 94, 32);
        java.awt.Color verdeMedio = new java.awt.Color(46, 125, 50);
        java.awt.Color verdeClaro = new java.awt.Color(232, 245, 233);
        java.awt.Color dorado = new java.awt.Color(212, 175, 55);
        java.awt.Color doradoClaro = new java.awt.Color(255, 236, 153);
        java.awt.Color blanco = java.awt.Color.WHITE;
        java.awt.Color grisTexto = new java.awt.Color(55, 55, 55);

        // ═══════════════════════════════════════
        // 🖼️ PANEL PRINCIPAL — tarjeta centrada
        // ═══════════════════════════════════════
        jPanel1.setLayout(null);
        jPanel1.setBackground(verdeClaro);
        jPanel1.setPreferredSize(new java.awt.Dimension(520, 720));
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(dorado, 2),
                javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        int anchoPanel = 520;

        // ═══════════════════════════════════════
        // 🐄 HEADER VERDE OSCURO con imagen
        // ═══════════════════════════════════════
        // Imagen de la vaca centrada arriba
        javax.swing.JPanel header = new javax.swing.JPanel(null) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                try {
                    java.awt.Image img = new javax.swing.ImageIcon(
                            getClass().getResource("/Imagenes/vaca (4).png")).getImage();
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

                    // 📐 Calcular tamaño manteniendo proporción
                    int imgW = img.getWidth(this);
                    int imgH = img.getHeight(this);
                    double escala = Math.min((double) getWidth() / imgW, (double) getHeight() / imgH);
                    int nuevoW = (int) (imgW * escala);
                    int nuevoH = (int) (imgH * escala);

                    // 📍 Centrar la imagen
                    int x = (getWidth() - nuevoW) / 2;
                    int y = (getHeight() - nuevoH) / 2;

                    g2.setComposite(java.awt.AlphaComposite.getInstance(
                            java.awt.AlphaComposite.SRC_OVER, 0.15f));
                    g2.drawImage(img, x, y, nuevoW, nuevoH, this);
                    g2.setComposite(java.awt.AlphaComposite.getInstance(
                            java.awt.AlphaComposite.SRC_OVER, 1f));
                } catch (Exception ignored) {
                }
            }
        };

        header.setBackground(verdeOscuro);
        header.setBounds(0, 0, anchoPanel, 100);
        // Título centrado sobre la imagen de fondo
        javax.swing.JLabel lblTitulo = new javax.swing.JLabel("REGISTRO ANIMAL", javax.swing.SwingConstants.CENTER);
        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        lblTitulo.setForeground(dorado);
        lblTitulo.setBounds(0, 30, anchoPanel, 40);
        header.add(lblTitulo);
        jPanel1.add(header);

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 4, new Color(0, 0, 0, 40)),
                javax.swing.BorderFactory.createLineBorder(dorado, 2)
        ));

        // ═══════════════════════════════════════
        // 🔍 BARRA DE BÚSQUEDA
        // ═══════════════════════════════════════
        javax.swing.JPanel panelBuscar = new javax.swing.JPanel(null);
        panelBuscar.setBackground(verdeMedio);
        panelBuscar.setBounds(0, 100, anchoPanel, 50);

        javax.swing.JLabel lblBuscar = new javax.swing.JLabel("🔍 Buscar por IDICA:");
        lblBuscar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        lblBuscar.setForeground(blanco);
        lblBuscar.setBounds(20, 12, 160, 25);
        panelBuscar.add(lblBuscar);

        txtbuscar.setBounds(185, 12, 200, 28);
        txtbuscar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        txtbuscar.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(dorado, 1),
                javax.swing.BorderFactory.createEmptyBorder(2, 7, 2, 7)
        ));
        panelBuscar.add(txtbuscar);

        btnbuscar.setBounds(393, 12, 100, 28);
        btnbuscar.setText("BUSCAR");
        estilizarBotonPro(btnbuscar, verdeMedio, dorado, "/Imagenes/buscar.png");
        panelBuscar.add(btnbuscar);
        jPanel1.add(panelBuscar);
        txtbuscar.setText("Ingrese IDICA...");
        txtbuscar.setForeground(Color.GRAY);
        txtbuscar.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtbuscar.getText().equals("Ingrese IDICA...")) {
                    txtbuscar.setText("");
                    txtbuscar.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtbuscar.getText().isEmpty()) {
                    txtbuscar.setText("Ingrese IDICA...");
                    txtbuscar.setForeground(Color.GRAY);
                }
            }
        });

        // ═══════════════════════════════════════
        // 📋 CAMPOS DEL FORMULARIO
        // ═══════════════════════════════════════
        java.awt.Font fuenteLabel = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12);
        java.awt.Font fuenteInput = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);

        javax.swing.border.Border bordeInput = javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(180, 180, 180), 1),
                javax.swing.BorderFactory.createEmptyBorder(4, 10, 4, 10)
        );

        // ID oculto (label solo de referencia)
        txtidregistro.setBounds(430, 108, 60, 20);
        txtidregistro.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
        txtidregistro.setForeground(new java.awt.Color(150, 150, 150));
        jPanel1.add(txtidregistro);

        int startY = 165;
        int gapY = 58;
        int xIcono = 20;
        int xLabel = 55;
        int xInput = 55;
        int anchoInput = 430;
        int altoInput = 32;

        // ── NOMBRE ──────────────────────────────
        agregarCampo(jPanel1, jLabel11, "NOMBRE", txtnombre,
                xIcono, xLabel, xInput, startY + gapY * 0,
                anchoInput, altoInput, null,
                fuenteLabel, fuenteInput, bordeInput, grisTexto, dorado);

        // ── IDICA ────────────────────────────────
        agregarCampo(jPanel1, jLabel2, "IDICA", txtIDICA,
                xIcono, xLabel, xInput, startY + gapY * 1,
                anchoInput, altoInput, "/imagenes/idica.png",
                fuenteLabel, fuenteInput, bordeInput, grisTexto, dorado);

        // ── FECHA NACIMIENTO ─────────────────────
        agregarCampo(jPanel1, jLabel3, "FECHA DE NACIMIENTO  (Año/Mes/Dia)", txtfecnac,
                xIcono, xLabel, xInput, startY + gapY * 2,
                anchoInput, altoInput, "/imagenes/fecnac.png",
                fuenteLabel, fuenteInput, bordeInput, grisTexto, dorado);

        // ── RAZA ─────────────────────────────────
        agregarCampo(jPanel1, jLabel4, "RAZA", txtraza,
                xIcono, xLabel, xInput, startY + gapY * 3,
                anchoInput, altoInput, "/imagenes/raza.png",
                fuenteLabel, fuenteInput, bordeInput, grisTexto, dorado);

        // ── SEXO (ComboBox) ───────────────────────
        jLabel5.setText("SEXO");
        jLabel5.setFont(fuenteLabel);
        jLabel5.setForeground(grisTexto);
        jLabel5.setBounds(xLabel, startY + gapY * 4, anchoInput, 18);

        try {
            javax.swing.JLabel iconoSexo = new javax.swing.JLabel(
                    new javax.swing.ImageIcon(getClass().getResource("/imagenes/sexo.png")));
            iconoSexo.setBounds(xIcono, startY + gapY * 4 + 18, 28, 28);
            jPanel1.add(iconoSexo);
        } catch (Exception ignored) {
        }

        cbosexo.setBounds(xInput, startY + gapY * 4 + 20, anchoInput, altoInput);
        cbosexo.setFont(fuenteInput);
        cbosexo.setBackground(blanco);
        cbosexo.setBorder(bordeInput);
        jPanel1.add(jLabel5);
        jPanel1.add(cbosexo);

        // ── EDAD ──────────────────────────────────
        agregarCampo(jPanel1, jLabel6, "EDAD (años)", txtedad,
                xIcono, xLabel, xInput, startY + gapY * 5,
                anchoInput, altoInput, null,
                fuenteLabel, fuenteInput, bordeInput, grisTexto, dorado);

        // ── PESO ──────────────────────────────────
        agregarCampo(jPanel1, jLabel8, "PESO (kg)", txtpeso,
                xIcono, xLabel, xInput, startY + gapY * 6,
                anchoInput, altoInput, null,
                fuenteLabel, fuenteInput, bordeInput, grisTexto, dorado);

        // ── CLASIFICACIÓN ─────────────────────────
        agregarCampo(jPanel1, jLabel7, "CLASIFICACIÓN", txtclasificacion,
                xIcono, xLabel, xInput, startY + gapY * 7,
                anchoInput, altoInput, "/imagenes/clasificar.png",
                fuenteLabel, fuenteInput, bordeInput, grisTexto, dorado);
        
        // ── ESTADO (ComboBox) ───────────────────────
        jLabel9.setText("ESTADO DEL ANIMAL ");
        jLabel9.setFont(fuenteLabel);
        jLabel9.setForeground(grisTexto);
        jLabel9.setBounds(xLabel, startY + gapY * 8, anchoInput, 18);
        
        cboEstado.setBounds(xInput, startY + gapY * 8 + 20, anchoInput, altoInput);
        cboEstado.setFont(fuenteInput);
        cboEstado.setBackground(blanco);
        cboEstado.setBorder(bordeInput);
        jPanel1.add(jLabel9);
        jPanel1.add(cboEstado);

        // ═══════════════════════════════════════
        // 🔘 BOTONES ACCIÓN
        // ═══════════════════════════════════════
        int yBotones = startY + gapY * 9 + 10;

        btnguardar.setBounds(20, yBotones, 150, 40);
        btnactualizar.setBounds(185, yBotones, 150, 40);
        btneliminar.setBounds(350, yBotones, 150, 40);

        estilizarBotonPro(btnguardar, new java.awt.Color(40, 167, 69), dorado, "/imagenes/guardar.png");
        estilizarBotonPro(btnactualizar, new java.awt.Color(0, 123, 255), dorado, "/imagenes/actualizarr.png");
        estilizarBotonPro(btneliminar, new java.awt.Color(220, 53, 69), dorado, "/imagenes/papelera.png");

        jPanel1.add(btnguardar);
        jPanel1.add(btnactualizar);
        jPanel1.add(btneliminar);

        // ═══════════════════════════════════════
        // 📐 AJUSTE FINAL DEL INTERNAL FRAME
        // ═══════════════════════════════════════
        int alturaTotal = yBotones + 80;
        jPanel1.setPreferredSize(new java.awt.Dimension(anchoPanel, alturaTotal));
        setSize(anchoPanel + 20, alturaTotal + 60);
    }

    private void agregarCampo(
            javax.swing.JPanel panel,
            javax.swing.JLabel label, String textoLabel,
            javax.swing.JTextField input,
            int xIcono, int xLabel, int xInput, int y,
            int anchoInput, int altoInput,
            String rutaIcono,
            java.awt.Font fuenteLabel, java.awt.Font fuenteInput,
            javax.swing.border.Border bordeInput,
            java.awt.Color colorLabel, java.awt.Color colorIconoBorde) {

        // Label encima
        label.setText(textoLabel);
        label.setFont(fuenteLabel);
        label.setForeground(colorLabel);
        label.setBounds(xLabel, y, anchoInput, 18);
        panel.add(label);

        // Icono a la izquierda del input (si hay ruta)
        if (rutaIcono != null) {
            try {
                javax.swing.JLabel icono = new javax.swing.JLabel(
                        new javax.swing.ImageIcon(getClass().getResource(rutaIcono)));
                icono.setBounds(xIcono, y + 20, 24, 24);
                panel.add(icono);
            } catch (Exception ignored) {
            }
        }

        // Input
        input.setFont(fuenteInput);
        input.setBorder(bordeInput);
        input.setBounds(xInput, y + 20, anchoInput, altoInput);
        panel.add(input);
    }

    private void estilizarBotonPro(JButton btn, Color fondo,
            Color colorBorde, String rutaIcono) {

        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hover = fondo.brighter();

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(fondo);
            }
        });

        try {
            btn.setIcon(new ImageIcon(getClass().getResource(rutaIcono)));
        } catch (Exception ignored) {
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<String> cbosexo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JTextField txtclasificacion;
    private javax.swing.JTextField txtedad;
    private javax.swing.JTextField txtfecnac;
    private javax.swing.JLabel txtidregistro;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtpeso;
    private javax.swing.JTextField txtraza;
    // End of variables declaration//GEN-END:variables
}
