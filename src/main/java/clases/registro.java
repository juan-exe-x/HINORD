
package clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class registro {
    
  
    
    
    public int guardar (String nombre, long IDICA, Date fecnac, String raza, String sexo, int edad, double peso, String clasificacion, String estado_animal){
        
       
    String sql = "{CALL insertar_animal(?, ?, ?, ?, ?, ?, ?, ?,?)}";
    
     try (Connection conn = conexion.conectar();
         CallableStatement cs = conn.prepareCall(sql)) {  // prepareCall en lugar de prepareStatement

        // Los parámetros se asignan igual que antes
        cs.setString(1, nombre);
        cs.setLong(2, IDICA);
        cs.setDate(3, fecnac);
        cs.setString(4, raza);
        cs.setString(5, sexo);
        cs.setInt(6, edad);
        cs.setDouble(7, peso);
        cs.setString(8, clasificacion);
        cs.setString(9, estado_animal);

        // executeQuery porque tu SP retorna el LAST_INSERT_ID()
        ResultSet rs = cs.executeQuery();
        if (rs.next()) {
            return rs.getInt("nuevo_idregistro"); // retorna el id generado
        }

    } catch (Exception e) {
        System.out.println("Error al guardar: " + e.getMessage());
    }

    return -1; // si algo falló

    
   

    }
    
    public boolean actualizar ( int idregistro, String nombre, long IDICA, Date fecnac, String raza, String sexo, int edad, double peso, String clasificacion, String estado_animal){
        
        String sql = "{CALL actualizar_animal(?, ?, ?, ?, ?, ?, ?, ?, ?,?)}";

    try (Connection conn = conexion.conectar();
         CallableStatement cs = conn.prepareCall(sql)) {

        cs.setInt(1, idregistro);
        cs.setString(2, nombre);
        cs.setLong(3, IDICA);
        cs.setDate(4, fecnac);
        cs.setString(5, raza);
        cs.setString(6, sexo);
        cs.setInt(7, edad);
        cs.setDouble(8, peso);
        cs.setString(9, clasificacion);
        cs.setString(10, estado_animal);

        ResultSet rs = cs.executeQuery();
        if (rs.next()) {
            return rs.getInt("filas_afectadas") > 0;
        }

    } catch (Exception e) {
        System.out.println("Error al actualizar: " + e.getMessage());
    }

    return false;
    }
    
    
    
      public Object [][] buscarIDICA (int IDICA){
          String countSql = "SELECT COUNT(*) FROM registro WHERE IDICA = ?";
        String dataSql = "SELECT * FROM registro WHERE IDICA = ?";
        int total = 0;
        
        try(Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(countSql)) {
           ps.setInt(1, IDICA);
           
            try(ResultSet countRes = ps.executeQuery()) {
                
                if (countRes.next()){
                    total = countRes.getInt(1);
                }
                
            } 
            Object [][] datos = new Object[total][10]; 
            
            try  (PreparedStatement dataps = conn.prepareStatement(dataSql)) {
                dataps.setInt(1,IDICA);
                try (ResultSet datares = dataps.executeQuery()){
                    
                    int  i= 0;
                    
                    while (datares.next()){
                        datos [i][0] = datares.getString("nombre");
                        datos [i][1] = datares.getLong("IDICA");
                        datos [i][2] = datares.getDate("fecnac");
                        datos [i][3] = datares.getString("raza");
                        datos [i][4] = datares.getString("Sexo");
                        datos [i][5] = datares.getInt("edad");
                        datos [i][6] = datares.getDouble("peso");
                        datos [i][7] = datares.getString("clasificacion");
                        datos [i][8] = datares.getString("idregistro");
                        datos [i][9] = datares.getString("estado_animal");
                        
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
       
     public String eliminar(int idregistro ){
        
    String sql = "{CALL eliminar_animal(?, ?)}";

    try (Connection conn = conexion.conectar();
         CallableStatement cs = conn.prepareCall(sql)) {

        // Parámetro IN
        cs.setInt(1, idregistro);

        // Parámetro OUT — registras que esperas un VARCHAR de salida
        cs.registerOutParameter(2, java.sql.Types.VARCHAR);

        cs.execute();

        // Lees el mensaje que retorna el procedimiento
        return cs.getString(2); // "Animal eliminado correctamente" o "ERROR: Animal no encontrado"

    } catch (Exception e) {
        System.out.println("Error al eliminar: " + e.getMessage());
        return "ERROR: " + e.getMessage();
    }
        
    }
            
}
