
package clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class mascotas {
   public boolean guardar (String nombre, String especie, String raza, int edad, String sexo, String color, Date fecnac,String esterilizado) {
    
    String sql = "INSERT INTO mascotas (nombre, especie, raza, edad, sexo, color, fecnac,esterilizado) VALUES (?,?,?,?,?,?,?,?)";
     try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nombre);
        ps.setString(2, especie);
        ps.setString(3, raza);
        ps.setInt(4, edad);
        ps.setString(5, sexo);
        ps.setString(6, color);
        ps.setDate(7, fecnac);
        ps.setString(8, esterilizado);
        
        int filas = ps.executeUpdate();
        return filas > 0;  
    } catch (Exception e) {
        System.out.println("Error al guardar: " + e.getMessage());
        return false;
    }
    
} 
   public void actualizar (int idmascotas, String nombre, String especie, String raza, int edad, String sexo, String color, Date fecnac,String esterilizado) {
       
        String sql = "UPDATE mascotas set nombre=?, especie=?, raza=?, edad=?, sexo=?, color=?, fecnac=?, esterilizado=? WHERE idmascotas=?";
        
        try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, nombre);
        ps.setString(2, especie);
        ps.setString(3, raza);
        ps.setInt(4, edad);
        ps.setString(5, sexo);
        ps.setString(6, color);
        ps.setDate(7,  new java.sql.Date(fecnac.getTime()));
        ps.setString(8, esterilizado);
        ps.setInt(9, idmascotas);
        
        int filas = ps.executeUpdate();
        
            if (filas > 0) {
            
                JOptionPane.showMessageDialog(null, "Registro Actualizado Correctamente");
        } else {
            System.out.println("No se encontró el registro con ese ID");
        }
    } catch (Exception e) {
        System.out.println("Error al guardar: " + e.getMessage());
       
    }
      
      
    
   }
   
   public void eliminar (int idmascotas){
        String sql = "DELETE FROM mascotas WHERE idmascotas = ? ";
        
        try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idmascotas);
            ps.execute();
            
            
            
        } catch (Exception e) {
            System.out.println("Error al eliminar: "+ e.getMessage());
        }
   }
   
   
   public Object [][] buscaridmascota (int idmascotas){
          String countSql = "SELECT COUNT(*) FROM mascotas WHERE idmascotas= ?";
        String dataSql = "SELECT * FROM mascotas WHERE idmascotas = ?";
        int total = 0;
        
        try(Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(countSql)) {
           ps.setInt(1, idmascotas);
           
            try(ResultSet countRes = ps.executeQuery()) {
                
                if (countRes.next()){
                    total = countRes.getInt(1);
                }
                
            } 
            Object [][] datos = new Object[total][9]; 
            
            try  (PreparedStatement dataps = conn.prepareStatement(dataSql)) {
                dataps.setInt(1,idmascotas);
                try (ResultSet datares = dataps.executeQuery()){
                    
                    int  i= 0;
                    
                    while (datares.next()){
                        datos [i][0] = datares.getString("nombre");
                        datos [i][1] = datares.getString("especie");
                        datos [i][2] = datares.getString("raza");
                        datos [i][3] = datares.getInt("edad");
                        datos [i][4] = datares.getString("Sexo");
                        datos [i][5] = datares.getString("color");
                        datos [i][6] = datares.getDate("fecnac");
                        datos [i][7] = datares.getString("esterilizado");
                         datos [i][8] = datares.getInt("idmascotas");
                        
                        i++;
                        
                    }
                }
                
            }
            
            return datos; 
        } catch (SQLException e) {
            System.out.println("Error al buscar datos: "+ e.getMessage());
            return new Object[0][0];
        }
      }
       


   }