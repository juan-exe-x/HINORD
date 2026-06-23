
package clases;
import java.sql.*;


public class conexion {
    //servidor local
    
    private static final String URL = "jdbc:mariadb://localhost:3307/ganadb";
    
    private static final String USER = "root";
    
    private static final String PASS = "12345678"; 
    
  
    
         public static Connection conectar(){
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("conexion exitosa");
            return con;
        } catch (SQLException e) {
            System.out.println("Error:"+e.getMessage());
            
        }
        return null;
    }

    public static Connection getConexion() {
        return conectar();
    }
    
    
}
