package clases;

import clases.VentaLeche;
import clases.conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaLecheDAO {

    // =========================================================================
    // PRODUCCIÓN POR VACA en un rango de fechas
    // Retorna filas: [idregistro, nombre, litros, valorVaca, porcentaje]
    // =========================================================================
    public List<Object[]> getProduccionPorVaca(Date desde, Date hasta,
            double precioLitro)
            throws SQLException {

        List<Object[]> lista = new ArrayList<>();

        String sql
                = "SELECT r.idregistro, r.nombre, "
                + "       COALESCE(SUM(pl.litros), 0) AS litros "
                + "FROM registro r "
                + "LEFT JOIN produccion_leche pl "
                + "     ON pl.idregistro = r.idregistro "
                + "     AND pl.fecha BETWEEN ? AND ? "
                + "WHERE r.estado_animal = 'Activo' "
                + "GROUP BY r.idregistro, r.nombre "
                + "HAVING litros > 0 "
                + "ORDER BY litros DESC";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, desde);
            ps.setDate(2, hasta);
            ResultSet rs = ps.executeQuery();

            // Primera pasada: acumular total general
            List<Object[]> temp = new ArrayList<>();
            double totalLitros = 0;

            while (rs.next()) {
                double litros = rs.getDouble("litros");
                totalLitros += litros;
                temp.add(new Object[]{
                    rs.getInt("idregistro"),
                    rs.getString("nombre"),
                    litros
                });
            }

            // Segunda pasada: calcular valor y % con el total ya conocido
            for (Object[] fila : temp) {
                double litros = (double) fila[2];
                double valor = litros * precioLitro;
                double pct = totalLitros > 0
                        ? (litros / totalLitros) * 100.0
                        : 0.0;
                lista.add(new Object[]{
                    fila[0], // idregistro
                    fila[1], // nombre
                    litros, // litros
                    valor, // valor $
                    pct // % del total
                });
            }
        }
        return lista;
    }

    // =========================================================================
    // TOTALES GENERALES del período
    // Retorna: [litrosTotales, valorTotal]
    // =========================================================================
    public double[] getTotales(Date desde, Date hasta, double precioLitro)
            throws SQLException {

        String sql
                = "SELECT COALESCE(SUM(pl.litros), 0) AS total "
                + "FROM produccion_leche pl "
                + "WHERE pl.fecha BETWEEN ? AND ?";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, desde);
            ps.setDate(2, hasta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double litros = rs.getDouble("total");
                return new double[]{litros, litros * precioLitro};
            }
        }
        return new double[]{0, 0};
    }

    // =========================================================================
    // GUARDAR VENTA
    // =========================================================================
    public boolean guardarVenta(VentaLeche v) throws SQLException {
        String sql
                = "INSERT INTO venta_leche "
                + "(fecha_venta, fecha_desde, fecha_hasta, entidad, "
                + " precio_litro, litros_totales, valor_total, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, v.getFechaVenta());
            ps.setDate(2, v.getFechaDesde());
            ps.setDate(3, v.getFechaHasta());
            ps.setString(4, v.getEntidad());
            ps.setDouble(5, v.getPrecioLitro());
            ps.setDouble(6, v.getLitrosTotales());
            ps.setDouble(7, v.getValorTotal());
            ps.setString(8, v.getObservaciones());

            return ps.executeUpdate() > 0;
        }
    }

    // =========================================================================
    // HISTORIAL DE VENTAS guardadas
    // =========================================================================
    public List<VentaLeche> getHistorial() throws SQLException {
        List<VentaLeche> lista = new ArrayList<>();

        String sql
                = "SELECT * FROM venta_leche ORDER BY fecha_venta DESC";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VentaLeche vl = new VentaLeche();
                vl.setIdVenta(rs.getInt("id_venta"));
                vl.setFechaVenta(rs.getDate("fecha_venta"));
                vl.setFechaDesde(rs.getDate("fecha_desde"));
                vl.setFechaHasta(rs.getDate("fecha_hasta"));
                vl.setEntidad(rs.getString("entidad"));
                vl.setPrecioLitro(rs.getDouble("precio_litro"));
                vl.setLitrosTotales(rs.getDouble("litros_totales"));
                vl.setValorTotal(rs.getDouble("valor_total"));
                vl.setObservaciones(rs.getString("observaciones"));
                lista.add(vl);
            }
        }
        return lista;
    }

    // =========================================================================
    // ELIMINAR VENTA
    // =========================================================================
    public boolean eliminarVenta(int idVenta) throws SQLException {
        String sql = "DELETE FROM venta_leche WHERE id_venta = ?";
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            return ps.executeUpdate() > 0;
        }
    }
}
