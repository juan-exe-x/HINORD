package dao;

import clases.Gestacion;
import clases.conexion;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO principal del módulo reproductivo.
 */
public class GestacionDAO {

    // =====================================================
    // INSERTAR GESTACIÓN
    // =====================================================

    public boolean insertar(Gestacion g) {

        // Calcular fecha de parto automáticamente (283 días bovinos)
        LocalDate fechaParto = g.getFechaServicio().plusDays(283);
        g.setFechaPartoEstimada(fechaParto);

        // ── Si no viene numeroServicio desde la UI, calcularlo automáticamente ──
        if (g.getNumeroServicio() <= 0) {
            g.setNumeroServicio(contarServiciosVaca(g.getIdVaca()) + 1);
        }

        String sql = "INSERT INTO gestacion "
                + "(id_vaca, tipo_fecundacion, id_toro, id_lote_semen, "
                + "id_donante, fecha_servicio, fecha_parto_estimada, "
                + "fecha_confirmacion, tipo_confirmacion, estado, "
                + "observaciones, numero_servicio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, g.getIdVaca());
            pst.setString(2, g.getTipoFecundacion());

            setNullableInt(pst, 3, g.getIdToro());
            setNullableInt(pst, 4, g.getIdLoteSemen());
            setNullableInt(pst, 5, g.getIdDonante());

            pst.setDate(6, Date.valueOf(g.getFechaServicio()));
            pst.setDate(7, Date.valueOf(g.getFechaPartoEstimada()));

            if (g.getFechaConfirmacion() != null) {
                pst.setDate(8, Date.valueOf(g.getFechaConfirmacion()));
            } else {
                pst.setNull(8, Types.DATE);
            }

            pst.setString(9, g.getTipoConfirmacion());
            pst.setString(10, g.getEstado() != null ? g.getEstado() : "pendiente_confirmacion");
            pst.setString(11, g.getObservaciones());
            pst.setInt(12, g.getNumeroServicio());

            int filas = pst.executeUpdate();

            if (filas > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    g.setIdGestacion(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar gestación: " + e.getMessage());
        }

        return false;
    }

    // =====================================================
    // CONTAR SERVICIOS ANTERIORES DE UNA VACA
    // (para calcular numero_servicio automáticamente)
    // =====================================================

    public int contarServiciosVaca(int idVaca) {

        String sql = "SELECT COUNT(*) FROM gestacion WHERE id_vaca = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idVaca);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar servicios: " + e.getMessage());
        }

        return 0;
    }

    // =====================================================
    // OBTENER GESTACIONES ACTIVAS
    // =====================================================

    public List<Gestacion> obtenerActivas() {

        List<Gestacion> lista = new ArrayList<>();

        String sql = "SELECT * FROM v_gestaciones_activas "
                + "ORDER BY dias_para_parto ASC";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Gestacion g = new Gestacion();

                g.setIdGestacion(rs.getInt("id_gestacion"));
                g.setIdicaVaca(rs.getLong("idica_vaca"));
                g.setNombreVaca(rs.getString("nombre_vaca"));
                g.setTipoFecundacion(rs.getString("tipo_fecundacion"));
                g.setOrigenGenetico(rs.getString("origen_genetico"));

                g.setFechaServicio(rs.getDate("fecha_servicio").toLocalDate());
                g.setFechaPartoEstimada(rs.getDate("fecha_parto_estimada").toLocalDate());

                g.setDiasParaParto(rs.getInt("dias_para_parto"));
                g.setAlerta(rs.getString("alerta"));
                g.setEstado(rs.getString("estado"));

                // numero_servicio (si la vista lo expone; si no, queda en 0)
                try { g.setNumeroServicio(rs.getInt("numero_servicio")); }
                catch (SQLException ignored) {}

                Date fc = rs.getDate("fecha_confirmacion");
                if (fc != null) g.setFechaConfirmacion(fc.toLocalDate());

                lista.add(g);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener gestaciones activas: " + e.getMessage());
        }

        return lista;
    }

    // =====================================================
    // OBTENER GESTACIONES POR VACA
    // =====================================================

    public List<Gestacion> obtenerPorVaca(int idVaca) {

        List<Gestacion> lista = new ArrayList<>();

        String sql = "SELECT g.id_gestacion, "
                + "r.IDICA, "
                + "r.nombre, "
                + "g.tipo_fecundacion, "
                + "g.numero_servicio, "
                + "CASE "
                + "WHEN g.tipo_fecundacion = 'Monta Natural' "
                + "THEN CONCAT(rt.nombre, ' (', rt.IDICA, ')') "
                + "WHEN g.tipo_fecundacion = 'Inseminacion Artificial' "
                + "THEN CONCAT('IA: ', ls.codigo_lote) "
                + "ELSE CONCAT('TE: ', rd.nombre) "
                + "END AS origen, "
                + "g.fecha_servicio, "
                + "g.fecha_parto_estimada, "
                + "DATEDIFF(g.fecha_parto_estimada, CURDATE()) AS dias, "
                + "g.estado, "
                + "g.fecha_confirmacion "
                + "FROM gestacion g "
                + "JOIN registro r ON r.idregistro = g.id_vaca "
                + "LEFT JOIN registro rt ON rt.idregistro = g.id_toro "
                + "LEFT JOIN lote_semen ls ON ls.id_lote = g.id_lote_semen "
                + "LEFT JOIN registro rd ON rd.idregistro = g.id_donante "
                + "WHERE g.id_vaca = ? "
                + "ORDER BY g.fecha_servicio DESC";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idVaca);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    Gestacion g = new Gestacion();

                    g.setIdGestacion(rs.getInt("id_gestacion"));
                    g.setIdicaVaca(rs.getLong("IDICA"));
                    g.setNombreVaca(rs.getString("nombre"));
                    g.setTipoFecundacion(rs.getString("tipo_fecundacion"));
                    g.setNumeroServicio(rs.getInt("numero_servicio"));
                    g.setOrigenGenetico(rs.getString("origen"));

                    g.setFechaServicio(rs.getDate("fecha_servicio").toLocalDate());
                    g.setFechaPartoEstimada(rs.getDate("fecha_parto_estimada").toLocalDate());

                    g.setDiasParaParto(rs.getInt("dias"));
                    g.setEstado(rs.getString("estado"));

                    Date fc = rs.getDate("fecha_confirmacion");
                    if (fc != null) g.setFechaConfirmacion(fc.toLocalDate());

                    lista.add(g);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }

        return lista;
    }

    // =====================================================
    // CONFIRMAR GESTACIÓN
    // =====================================================

    public boolean confirmar(int idGestacion,
                             String estado,
                             String tipoConfirmacion,
                             LocalDate fechaConfirmacion) {

        String sql = "UPDATE gestacion "
                + "SET estado = ?, tipo_confirmacion = ?, fecha_confirmacion = ? "
                + "WHERE id_gestacion = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, estado);
            pst.setString(2, tipoConfirmacion);
            pst.setDate(3, Date.valueOf(fechaConfirmacion));
            pst.setInt(4, idGestacion);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al confirmar gestación: " + e.getMessage());
        }

        return false;
    }

    // =====================================================
    // MARCAR PARTO REGISTRADO
    // =====================================================

    public boolean marcarPartoRegistrado(int idGestacion) {

        String sql = "UPDATE gestacion SET estado = 'parto_registrado' WHERE id_gestacion = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idGestacion);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al marcar parto registrado: " + e.getMessage());
        }

        return false;
    }

    // =====================================================
    // OBTENER GESTACIÓN POR ID  ← FIX: ahora mapea observaciones
    // =====================================================

    public Gestacion obtenerPorId(int idGestacion) {

        String sql = "SELECT g.*, "
                + "r.IDICA, "
                + "r.nombre AS nombre_vaca, "
                + "CASE "
                + "WHEN g.tipo_fecundacion = 'Monta Natural' "
                + "THEN CONCAT(rt.nombre, ' (', rt.IDICA, ')') "
                + "WHEN g.tipo_fecundacion = 'Inseminacion Artificial' "
                + "THEN CONCAT('IA: ', ls.codigo_lote) "
                + "ELSE CONCAT('TE: ', rd.nombre) "
                + "END AS origen_genetico "
                + "FROM gestacion g "
                + "JOIN registro r ON r.idregistro = g.id_vaca "
                + "LEFT JOIN registro rt ON rt.idregistro = g.id_toro "
                + "LEFT JOIN lote_semen ls ON ls.id_lote = g.id_lote_semen "
                + "LEFT JOIN registro rd ON rd.idregistro = g.id_donante "
                + "WHERE g.id_gestacion = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idGestacion);

            try (ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {

                    Gestacion g = new Gestacion();

                    g.setIdGestacion(rs.getInt("id_gestacion"));
                    g.setIdVaca(rs.getInt("id_vaca"));
                    g.setIdicaVaca(rs.getLong("IDICA"));
                    g.setNombreVaca(rs.getString("nombre_vaca"));
                    g.setTipoFecundacion(rs.getString("tipo_fecundacion"));
                    g.setOrigenGenetico(rs.getString("origen_genetico"));

                    g.setFechaServicio(rs.getDate("fecha_servicio").toLocalDate());
                    g.setFechaPartoEstimada(rs.getDate("fecha_parto_estimada").toLocalDate());
                    g.setEstado(rs.getString("estado"));

                    // ── FIX: observaciones y numero_servicio ahora se mapean ──
                    g.setObservaciones(rs.getString("observaciones"));
                    g.setNumeroServicio(rs.getInt("numero_servicio"));

                    // IDs opcionales para repoblar combos
                    g.setIdToro(rs.getObject("id_toro") != null ? rs.getInt("id_toro") : null);
                    g.setIdLoteSemen(rs.getObject("id_lote_semen") != null ? rs.getInt("id_lote_semen") : null);
                    g.setIdDonante(rs.getObject("id_donante") != null ? rs.getInt("id_donante") : null);

                    long dias = ChronoUnit.DAYS.between(LocalDate.now(), g.getFechaPartoEstimada());
                    g.setDiasParaParto((int) dias);

                    return g;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener gestación: " + e.getMessage());
        }

        return null;
    }

    // =====================================================
    // ACTUALIZAR GESTACIÓN
    // =====================================================

    public boolean actualizar(Gestacion g) {

        // Recalcular fecha de parto al actualizar
        LocalDate fechaParto = g.getFechaServicio().plusDays(283);
        g.setFechaPartoEstimada(fechaParto);

        String sql = "UPDATE gestacion "
                + "SET id_vaca = ?, tipo_fecundacion = ?, id_toro = ?, "
                + "id_lote_semen = ?, id_donante = ?, "
                + "fecha_servicio = ?, fecha_parto_estimada = ?, "
                + "observaciones = ?, numero_servicio = ? "
                + "WHERE id_gestacion = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, g.getIdVaca());
            pst.setString(2, g.getTipoFecundacion());
            setNullableInt(pst, 3, g.getIdToro());
            setNullableInt(pst, 4, g.getIdLoteSemen());
            setNullableInt(pst, 5, g.getIdDonante());
            pst.setDate(6, Date.valueOf(g.getFechaServicio()));
            pst.setDate(7, Date.valueOf(g.getFechaPartoEstimada()));
            pst.setString(8, g.getObservaciones());
            pst.setInt(9, g.getNumeroServicio());
            pst.setInt(10, g.getIdGestacion());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar gestación: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // MÉTODO AUXILIAR
    // =====================================================

    private void setNullableInt(PreparedStatement pst, int index, Integer valor) throws SQLException {
        if (valor != null) {
            pst.setInt(index, valor);
        } else {
            pst.setNull(index, Types.INTEGER);
        }
    }
}