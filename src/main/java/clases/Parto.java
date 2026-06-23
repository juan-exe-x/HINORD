package clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Parto {

    // ==========================
    // GUARDAR PARTO
    // ==========================
    public boolean guardar(
            int idGestacion,
            Date fechaHoraParto,
            String tipoParto,
            String duracionTrabajo,
            String estadoVacaPosparto,
            String expulsionPlacenta,
            String calostroSuministrado,
            boolean asistenciaVeterinaria,
            String medicamentosAplicados,
            String observaciones) {

        String sql = "INSERT INTO partos_reproductivo ("
                + "id_gestacion,"
                + "fecha_hora_parto,"
                + "tipo_parto,"
                + "duracion_trabajo,"
                + "estado_vaca_posparto,"
                + "expulsion_placenta,"
                + "calostro_suministrado,"
                + "asistencia_veterinaria,"
                + "medicamentos_aplicados,"
                + "observaciones)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGestacion);
            ps.setDate(2, fechaHoraParto);
            ps.setString(3, tipoParto);
            ps.setString(4, duracionTrabajo);
            ps.setString(5, estadoVacaPosparto);
            ps.setString(6, expulsionPlacenta);
            ps.setString(7, calostroSuministrado);
            ps.setBoolean(8, asistenciaVeterinaria);
            ps.setString(9, medicamentosAplicados);
            ps.setString(10, observaciones);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al guardar parto: " + e.getMessage());
            return false;
        }
    }

    // ==========================
    // ACTUALIZAR PARTO
    // ==========================
    public boolean actualizar(
            int idParto,
            int idGestacion,
            Date fechaHoraParto,
            String tipoParto,
            String duracionTrabajo,
            String estadoVacaPosparto,
            String expulsionPlacenta,
            String calostroSuministrado,
            boolean asistenciaVeterinaria,
            String medicamentosAplicados,
            String observaciones) {

        String sql = "UPDATE partos_reproductivo SET "
                + "id_gestacion=?, "
                + "fecha_hora_parto=?, "
                + "tipo_parto=?, "
                + "duracion_trabajo=?, "
                + "estado_vaca_posparto=?, "
                + "expulsion_placenta=?, "
                + "calostro_suministrado=?, "
                + "asistencia_veterinaria=?, "
                + "medicamentos_aplicados=?, "
                + "observaciones=? "
                + "WHERE id_parto=?";

        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGestacion);
            ps.setDate(2, fechaHoraParto);
            ps.setString(3, tipoParto);
            ps.setString(4, duracionTrabajo);
            ps.setString(5, estadoVacaPosparto);
            ps.setString(6, expulsionPlacenta);
            ps.setString(7, calostroSuministrado);
            ps.setBoolean(8, asistenciaVeterinaria);
            ps.setString(9, medicamentosAplicados);
            ps.setString(10, observaciones);
            ps.setInt(11, idParto);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar parto: " + e.getMessage());
            return false;
        }
    }

    // ==========================
    // ELIMINAR
    // ==========================
    public boolean eliminar(int idParto) {
        String sql = "DELETE FROM partos_reproductivo WHERE id_parto=?";

        try (Connection conn = conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idParto);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al eliminar parto: " + e.getMessage());
            return false;
        }
    }
   public ResultSet buscarMadreYCrias(String filtro) {

    String sql =
        "SELECT " +
        "p.id_parto, " +
        "r.nombre AS nombre_madre, " +
        "p.fecha_hora_parto AS fecha_parto, " +
        "p.tipo_parto, " +
        "1 AS cantidad_crias, " +
        "p.estado_vaca_posparto AS estado_cria, " +
        "p.estado_vaca_posparto AS condicion_madre, " +
        "p.observaciones " +
        "FROM partos_reproductivo p " +
        "INNER JOIN gestacion g ON p.id_gestacion = g.id_gestacion " +
        "INNER JOIN registro r ON g.id_vaca = r.idregistro " +
        "WHERE r.nombre LIKE ? " +
        "   OR CAST(p.id_parto AS CHAR) LIKE ? " +
        "ORDER BY p.fecha_hora_parto DESC";

    try {
        Connection conn = conexion.conectar();
        PreparedStatement ps = conn.prepareStatement(sql);
        String like = "%" + filtro + "%";
        ps.setString(1, like);
        ps.setString(2, like);
        return ps.executeQuery();
    } catch (Exception e) {
        System.out.println("Error buscarMadreYCrias: " + e.getMessage());
        return null;
    }
}
}