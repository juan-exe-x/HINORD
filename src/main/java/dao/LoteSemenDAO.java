package dao;

import clases.LoteSemen;
import clases.conexion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla lote_semen.
 */
public class LoteSemenDAO {

    // =====================================================
    // OBTENER LOTES DISPONIBLES
    // =====================================================

    /**
     * Devuelve lotes activos con dosis disponibles.
     */
    public List<LoteSemen> obtenerLotesDisponibles() {

        List<LoteSemen> lista = new ArrayList<>();

        String sql =
                "SELECT id_lote, codigo_lote, raza_toro, nombre_toro, "
              + "proveedor, fecha_ingreso, dosis_disponibles, activo "
              + "FROM lote_semen "
              + "WHERE activo = 1 "
              + "AND dosis_disponibles > 0 "
              + "ORDER BY codigo_lote DESC";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                LoteSemen ls = mapearLote(rs);

                lista.add(ls);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener lotes disponibles: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =====================================================
    // OBTENER TODOS LOS LOTES
    // =====================================================

    /**
     * Incluye agotados e inactivos.
     */
    public List<LoteSemen> obtenerTodosLosLotes() {

        List<LoteSemen> lista = new ArrayList<>();

        String sql =
                "SELECT id_lote, codigo_lote, raza_toro, nombre_toro, "
              + "proveedor, fecha_ingreso, dosis_disponibles, activo "
              + "FROM lote_semen "
              + "ORDER BY codigo_lote DESC";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                LoteSemen ls = mapearLote(rs);

                lista.add(ls);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener todos los lotes: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =====================================================
    // INSERTAR LOTE
    // =====================================================

    /**
     * Inserta un nuevo lote de semen.
     */
    public boolean insertar(LoteSemen ls) {

        String sql =
                "INSERT INTO lote_semen "
              + "(codigo_lote, raza_toro, nombre_toro, proveedor, "
              + "fecha_ingreso, dosis_disponibles, activo) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, ls.getCodigoLote());
            pst.setString(2, ls.getRazaToro());
            pst.setString(3, ls.getNombreToro());
            pst.setString(4, ls.getProveedor());

            if (ls.getFechaIngreso() != null) {
                pst.setDate(5, Date.valueOf(ls.getFechaIngreso()));
            } else {
                pst.setNull(5, java.sql.Types.DATE);
            }

            pst.setInt(6, ls.getDosisDisponibles());

            pst.setBoolean(7, ls.isActivo());

            return pst.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {

            System.err.println(
                    "El código de lote ya existe: "
                    + ls.getCodigoLote()
            );

        } catch (SQLException e) {

            System.err.println(
                    "Error al insertar lote: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // =====================================================
    // DESCONTAR DOSIS
    // =====================================================

    /**
     * Descuenta una dosis después de registrar IA.
     */
    public boolean descontarDosis(int idLote) {

        String sql =
                "UPDATE lote_semen "
              + "SET dosis_disponibles = dosis_disponibles - 1 "
              + "WHERE id_lote = ? "
              + "AND dosis_disponibles > 0";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idLote);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al descontar dosis: "
                    + e.getMessage()
            );
        }

        return false;
    }

    // =====================================================
    // OBTENER LOTE POR ID
    // =====================================================

    public LoteSemen obtenerPorId(int idLote) {

        String sql =
                "SELECT id_lote, codigo_lote, raza_toro, nombre_toro, "
              + "proveedor, fecha_ingreso, dosis_disponibles, activo "
              + "FROM lote_semen "
              + "WHERE id_lote = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idLote);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    return mapearLote(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al obtener lote por ID: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // =====================================================
    // MÉTODO PRIVADO PARA MAPEAR
    // =====================================================

    /**
     * Convierte un ResultSet en objeto LoteSemen.
     */
    private LoteSemen mapearLote(ResultSet rs) throws SQLException {

        LoteSemen ls = new LoteSemen();

        ls.setIdLote(rs.getInt("id_lote"));
        ls.setCodigoLote(rs.getString("codigo_lote"));
        ls.setRazaToro(rs.getString("raza_toro"));
        ls.setNombreToro(rs.getString("nombre_toro"));
        ls.setProveedor(rs.getString("proveedor"));
        ls.setDosisDisponibles(rs.getInt("dosis_disponibles"));
        ls.setActivo(rs.getBoolean("activo"));

        Date fecha = rs.getDate("fecha_ingreso");

        if (fecha != null) {
            ls.setFechaIngreso(fecha.toLocalDate());
        }

        return ls;
    }
    public boolean actualizar(LoteSemen ls) {

    String sql =
            "UPDATE lote_semen "
          + "SET codigo_lote = ?, "
          + "raza_toro = ?, "
          + "nombre_toro = ?, "
          + "proveedor = ?, "
          + "fecha_ingreso = ?, "
          + "dosis_disponibles = ?, "
          + "activo = ? "
          + "WHERE id_lote = ?";

    try (Connection con = conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, ls.getCodigoLote());
        pst.setString(2, ls.getRazaToro());
        pst.setString(3, ls.getNombreToro());
        pst.setString(4, ls.getProveedor());

        if (ls.getFechaIngreso() != null) {
            pst.setDate(5, Date.valueOf(ls.getFechaIngreso()));
        } else {
            pst.setNull(5, Types.DATE);
        }

        pst.setInt(6, ls.getDosisDisponibles());
        pst.setBoolean(7, ls.isActivo());

        pst.setInt(8, ls.getIdLote());

        return pst.executeUpdate() > 0;

    } catch (SQLException e) {

        System.err.println(
                "Error al actualizar lote: "
                + e.getMessage()
        );
    }

    return false;
}
    
    public boolean desactivar(int idLote) {

    String sql =
            "UPDATE lote_semen "
          + "SET activo = 0 "
          + "WHERE id_lote = ?";

    try (Connection con = conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, idLote);

        return pst.executeUpdate() > 0;

    } catch (SQLException e) {

        System.err.println(
                "Error al desactivar lote: "
                + e.getMessage()
        );
    }

    return false;
}
    
    public List<LoteSemen> buscarPorCodigo(String codigo) {

    List<LoteSemen> lista = new ArrayList<>();

    String sql =
            "SELECT * "
          + "FROM lote_semen "
          + "WHERE codigo_lote LIKE ? "
          + "ORDER BY codigo_lote DESC";

    try (Connection con = conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, "%" + codigo + "%");

        try (ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                lista.add(mapearLote(rs));
            }
        }

    } catch (SQLException e) {

        System.err.println(
                "Error al buscar lote: "
                + e.getMessage()
        );
    }

    return lista;
}
    public int obtenerTotalLotes() {

    String sql = "SELECT COUNT(*) FROM lote_semen";

    try (Connection con = conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return 0;
}
    public int obtenerTotalDosis() {

    String sql =
            "SELECT SUM(dosis_disponibles) "
          + "FROM lote_semen";

    try (Connection con = conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return 0;
}
    public int obtenerStockBajo() {

    String sql =
            "SELECT COUNT(*) "
          + "FROM lote_semen "
          + "WHERE dosis_disponibles <= 10 "
          + "AND activo = 1";

    try (Connection con = conexion.getConexion();
         PreparedStatement pst = con.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return 0;
}
}
