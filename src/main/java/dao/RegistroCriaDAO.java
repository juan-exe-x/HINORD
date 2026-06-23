package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistroCriaDAO {

    conexion cn = new conexion();

    public boolean registrarCria(RegistroCria cria) {

        String sql = "INSERT INTO cria("
                + "id_parto,"
                + "id_animal_nuevo,"
                + "id_madre,"
                + "id_padre_o_lote,"
                + "sexo_cria,"
                + "peso_nacimiento,"
                + "condicion_nacimiento,"
                + "observaciones)"
                + " VALUES (?,?,?,?,?,?,?,?)";

        try {

            Connection con = cn.conectar();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, cria.getIdParto());
            ps.setInt(2, cria.getIdAnimalNuevo());
            ps.setInt(3, cria.getIdMadre());
            ps.setString(4, cria.getIdPadreOLote());
            ps.setString(5, cria.getSexoCria());
            ps.setDouble(6, cria.getPesoNacimiento());
            ps.setString(7, cria.getCondicionNacimiento());
            ps.setString(8, cria.getObservaciones());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error registrar cria: " + e);

            return false;
        }
    }
}