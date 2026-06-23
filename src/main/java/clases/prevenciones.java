package clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class prevenciones {

    /**
     * Guarda una prevención. idInsumo es OPCIONAL: - Pasa un Integer válido si
     * la vacuna/medicamento se descontó de un frasco/insumo del inventario
     * (esto activa el trigger que descuenta stock automáticamente). - Pasa null
     * si la prevención no usó ningún insumo del inventario (comportamiento
     * idéntico al anterior, sin cambios).
     */
    public boolean guardar(String nombre, long IDICA, int idregistro, String vacunaaplicada,
            String viaadministracion, double dosis, Date fechaapli, Date fechaproxima,
            String veteresponsable, String enfermedadpreviene, Integer idInsumo) {

        String sql = "{CALL registrar_prevencion(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = conexion.conectar(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, nombre);
            cs.setLong(2, IDICA);
            cs.setInt(3, idregistro);
            cs.setString(4, vacunaaplicada);
            cs.setString(5, viaadministracion);
            cs.setDouble(6, dosis);
            cs.setDate(7, fechaapli);
            cs.setDate(8, fechaproxima);
            cs.setString(9, veteresponsable);
            cs.setString(10, enfermedadpreviene);

            // id_insumo es opcional: NULL si no se usó insumo del inventario
            if (idInsumo != null) {
                cs.setInt(11, idInsumo);
            } else {
                cs.setNull(11, Types.INTEGER);
            }

            cs.execute();
            ResultSet rs = cs.getResultSet();
            if (rs != null && rs.next()) {
                int nuevoId = rs.getInt("nuevo_idprevencion");
                System.out.println("Prevención guardada con ID: " + nuevoId);
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error al guardar prevención: " + e.getMessage());
        }
        return false;
    }

    /**
     * Sobrecarga de compatibilidad: si algún código viejo todavía llama
     * guardar() sin idInsumo, sigue funcionando exactamente igual que antes
     * (sin tocar inventario).
     */
    public boolean guardar(String nombre, long IDICA, int idregistro, String vacunaaplicada,
            String viaadministracion, double dosis, Date fechaapli, Date fechaproxima,
            String veteresponsable, String enfermedadpreviene) {
        return guardar(nombre, IDICA, idregistro, vacunaaplicada, viaadministracion,
                dosis, fechaapli, fechaproxima, veteresponsable, enfermedadpreviene, null);
    }

    public boolean actualizar(int idprevenciones, Date fechaapli, Date fechaproxima, double dosis,
            String veteresponsable, String vacunaaplicada, String viaadministracion,
            String enfermedadpreviene) {

        String sql = "UPDATE prevenciones SET vacunaaplicada=?, viaadministracion=?, dosis=?, "
                + "fechaapli=?, fechaproxima=?, veteresponsable=?, enfermedadpreviene=? "
                + "WHERE idprevenciones=?";

        try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vacunaaplicada);
            ps.setString(2, viaadministracion);
            ps.setDouble(3, dosis);
            ps.setDate(4, fechaapli);
            ps.setDate(5, fechaproxima);
            ps.setString(6, veteresponsable);
            ps.setString(7, enfermedadpreviene);
            ps.setInt(8, idprevenciones);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public Object[][] buscarPorIDICAOPrevencion(int id) {
        Object[][] datos = new Object[0][0];

        String sqlRegistro = "SELECT nombre, idica, idregistro FROM registro WHERE idica = ? ";

        String sqlPrevencion = "SELECT r.nombre, r.idica, r.idregistro,p.idprevenciones, p.vacunaaplicada,"
                + " p.viaadministracion, p.dosis, p.fechaapli, "
                + "p.fechaproxima, p.veteresponsable, p.enfermedadpreviene "
                + "FROM prevenciones p INNER JOIN registro r ON p.idregistro = r.idregistro WHERE p.idprevenciones = ?";

        try (Connection conn = conexion.conectar()) {

            try (PreparedStatement ps = conn.prepareStatement(sqlRegistro)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        datos = new Object[1][3];
                        datos[0][0] = rs.getString("nombre");
                        datos[0][1] = rs.getLong("idica");
                        datos[0][2] = rs.getInt("idregistro");
                        return datos;
                    }
                }
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlPrevencion)) {
                ps2.setInt(1, id);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    if (rs2.next()) {
                        datos = new Object[1][11];
                        datos[0][0] = rs2.getString("nombre");
                        datos[0][1] = rs2.getLong("idica");
                        datos[0][2] = rs2.getInt("idregistro");
                        datos[0][3] = rs2.getInt("idprevenciones");
                        datos[0][4] = rs2.getString("vacunaaplicada");
                        datos[0][5] = rs2.getString("viaadministracion");
                        datos[0][6] = rs2.getInt("dosis");
                        datos[0][7] = rs2.getDate("fechaapli");
                        datos[0][8] = rs2.getString("fechaproxima");
                        datos[0][9] = rs2.getString("veteresponsable");
                        datos[0][10] = rs2.getString("enfermedadpreviene");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar: " + e.getMessage());
        }

        return datos;
    }

    public boolean eliminar(int idprevenciones) {

        String sql = "DELETE FROM prevenciones WHERE idprevenciones = ?";

        try (Connection conn = conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idprevenciones);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}
