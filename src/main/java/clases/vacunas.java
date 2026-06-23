
package clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class vacunas {
    
public boolean guardar (String nombre, int idmascotas, String nombrevacuna,String tipoprevencion, double dosis, Date fecaplicacion, Date fecproxima,  String veterinario,  String viaadministracion ){
    
    String sql = "INSERT INTO vacunas (nombre,idmascotas,nombrevacuna, tipoprevencion, dosis,viaadministracion, fecaplicacion, fecproxima,veterinario )VALUES(?,?,?,?,?,?,?,?,?)";
    
        try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)){
            
            
        ps.setString(1, nombre);
        ps.setInt(2, idmascotas);
        ps.setString(3, nombrevacuna);
        ps.setString(4, tipoprevencion);
        ps.setDouble(5, dosis);           
        ps.setString(6, viaadministracion); 
        ps.setDate(7, fecaplicacion);
        ps.setDate(8, fecproxima);
        ps.setString(9, veterinario);
           
            
             int filas = ps.executeUpdate();
        return filas > 0;  
        
        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
        return false;
        }
        
}
 public void actualizar (int idvacunas,String nombrevacuna,String tipoprevencion, double dosis, Date fecaplicacion, Date fecproxima,  String veterinario,  String viaadministracion ){
        
        String sql = "UPDATE vacunas set nombrevacuna=? , tipoprevencion=? , dosis=? , viaadministracion=? , fecaplicacion=? , fecproxima=?,  veterinario=?  WHERE idvacunas=?";
        try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)){
           
            ps.setString(1,nombrevacuna );
            ps.setString(2,tipoprevencion ); 
            ps.setDouble(3, dosis);
             ps.setString(4,viaadministracion );
            ps.setDate(5, fecaplicacion);
            ps.setDate(6, fecproxima);
            ps.setString(7,veterinario );
            ps.setInt(8, idvacunas);
            
             int filas = ps.executeUpdate();

        if (filas > 0) {
            System.out.println("Registro de vacunas actualizado correctamente");
        } else {
            System.out.println("No se encontró el registro de prevenciones con ese ID");
        }
        
        } catch (Exception e) {
            System.out.println("Error al actualizar : " + e.getMessage());
       
        }
    }
 
  public Object[][] buscarPornombremascotaoidvacunas(String nombremascota, int idvacunas) {
    Object[][] datos = new Object[0][0];

    String sqlMascotas = "SELECT nombre, idmascotas FROM mascotas WHERE nombre LIKE ?";
    String sqlVacunas = "SELECT m.nombre, m.idmascotas, v.idvacunas, v.nombrevacuna, "
            + "v.tipoprevencion, v.dosis, v.viaadministracion, "
            + "v.fecaplicacion, v.fecproxima, v.veterinario "
            + "FROM vacunas v "
            + "INNER JOIN mascotas m ON v.idmascotas = m.idmascotas "
            + "WHERE v.idvacunas = ?";

    try (Connection conn = conexion.conectar()) {
        
        // 🐾 Buscar por nombre de mascota
        if (nombremascota != null && !nombremascota.isEmpty()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlMascotas)) {
                ps.setString(1, "%" + nombremascota + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        datos = new Object[1][2];
                        datos[0][0] = rs.getString("nombre");
                        datos[0][1] = rs.getInt("idmascotas");
                    }
                }
            }
        }

        // 💉 Buscar por ID de vacuna
        else if (idvacunas > 0) {
            try (PreparedStatement ps2 = conn.prepareStatement(sqlVacunas)) {
                ps2.setInt(1, idvacunas);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        datos = new Object[1][10];
                        datos[0][0] = rs2.getString("nombre");             
                        datos[0][1] = rs2.getInt("idmascotas");            
                        datos[0][2] = rs2.getInt("idvacunas");              
                        datos[0][3] = rs2.getString("nombrevacuna");        
                        datos[0][4] = rs2.getString("tipoprevencion");      
                        datos[0][5] = rs2.getDouble("dosis");            
                        datos[0][6] = rs2.getString("viaadministracion");   
                        datos[0][7] = rs2.getDate("fecaplicacion");         
                        datos[0][8] = rs2.getDate("fecproxima");            
                        datos[0][9] = rs2.getString("veterinario");        
                    }
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("❌ Error al buscar: " + e.getMessage());
    }

    return datos;
}

  public void eliminar(int idvacunas){
        
        String sql = "DELETE FROM vacunas WHERE idvacunas  = ? ";
        
        try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idvacunas);
            ps.execute();
            
            
            
        } catch (Exception e) {
            System.out.println("Error al eliminar: "+ e.getMessage());
        }
        
    }

}


