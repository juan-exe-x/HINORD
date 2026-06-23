package clases;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para el módulo de Insumos. Cubre: CategoriaInsumo, Insumo, CompraInsumo,
 * UsoInsumo.
 *
 * Convención del proyecto: - Conexión obtenida vía Conexion.getConexion() - Sin
 * manejo de transacciones externas (cada operación es autocommit) - Excepciones
 * propagadas al llamador (formulario Swing)
 */
public class InsumoDAO {

    // ══════════════════════════════════════════════════════════════
    //  CATEGORÍAS
    // ══════════════════════════════════════════════════════════════
    /**
     * Devuelve todas las categorías activas. Usado en JComboBox.
     */
    public List<CategoriaInsumo> listarCategorias() throws SQLException {
        List<CategoriaInsumo> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, activo "
                + "FROM categoria_insumo WHERE activo = 1 ORDER BY nombre";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapCategoria(rs));
            }
        }
        return lista;
    }

    // ══════════════════════════════════════════════════════════════
    //  CATÁLOGO DE INSUMOS
    // ══════════════════════════════════════════════════════════════
    /**
     * Lista todos los insumos activos con su categoría (para la tabla principal
     * del MDI).
     */
    public List<Insumo> listarInsumos() throws SQLException {
        List<Insumo> lista = new ArrayList<>();
        String sql = "SELECT i.id_insumo, i.id_categoria, ci.nombre AS nom_cat, "
                + "       i.nombre, i.descripcion, i.unidad_medida, "
                + "       i.precio_unitario, i.stock_actual, i.stock_minimo, "
                + "       i.activo, i.fecha_registro "
                + "FROM insumo i "
                + "JOIN categoria_insumo ci ON ci.id_categoria = i.id_categoria "
                + "WHERE i.activo = 1 "
                + "ORDER BY ci.nombre, i.nombre";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapInsumo(rs));
            }
        }
        return lista;
    }

    /**
     * Devuelve un insumo por su ID (para precargar formulario de edición).
     */
    public Insumo obtenerInsumo(int idInsumo) throws SQLException {
        String sql = "SELECT i.id_insumo, i.id_categoria, ci.nombre AS nom_cat, "
                + "       i.nombre, i.descripcion, i.unidad_medida, "
                + "       i.precio_unitario, i.stock_actual, i.stock_minimo, "
                + "       i.activo, i.fecha_registro "
                + "FROM insumo i "
                + "JOIN categoria_insumo ci ON ci.id_categoria = i.id_categoria "
                + "WHERE i.id_insumo = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idInsumo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapInsumo(rs);
                }
            }
        }
        return null;
    }

    /**
     * Inserta un nuevo insumo. Devuelve el ID generado.
     */
    public int insertarInsumo(Insumo ins) throws SQLException {
        String sql = "INSERT INTO insumo "
                + "(id_categoria, nombre, descripcion, unidad_medida, "
                + " precio_unitario, stock_actual, stock_minimo, activo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ins.getIdCategoria());
            ps.setString(2, ins.getNombre());
            ps.setString(3, ins.getDescripcion());
            ps.setString(4, ins.getUnidadMedida());
            ps.setBigDecimal(5, ins.getPrecioUnitario());
            ps.setBigDecimal(6, ins.getStockActual());
            ps.setBigDecimal(7, ins.getStockMinimo());
            ps.setBoolean(8, ins.isActivo());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Actualiza nombre, descripción, precio y stock mínimo de un insumo.
     */
    public boolean actualizarInsumo(Insumo ins) throws SQLException {
        String sql = "UPDATE insumo SET "
                + "id_categoria = ?, nombre = ?, descripcion = ?, "
                + "unidad_medida = ?, precio_unitario = ?, "
                + "stock_minimo = ?, activo = ? "
                + "WHERE id_insumo = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, ins.getIdCategoria());
            ps.setString(2, ins.getNombre());
            ps.setString(3, ins.getDescripcion());
            ps.setString(4, ins.getUnidadMedida());
            ps.setBigDecimal(5, ins.getPrecioUnitario());
            ps.setBigDecimal(6, ins.getStockMinimo());
            ps.setBoolean(7, ins.isActivo());
            ps.setInt(8, ins.getIdInsumo());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Desactiva un insumo (baja lógica).
     */
    public boolean desactivarInsumo(int idInsumo) throws SQLException {
        String sql = "UPDATE insumo SET activo = 0 WHERE id_insumo = ?";
        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idInsumo);
            return ps.executeUpdate() > 0;
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  COMPRAS (ENTRADAS)
    // ══════════════════════════════════════════════════════════════
    /**
     * Lista compras en un rango de fechas. Pasa null en ambas fechas para traer
     * todas.
     *
     * @param desde
     * @param hasta
     */
    public List<CompraInsumo> listarCompras(LocalDate desde,
            LocalDate hasta) throws SQLException {
        List<CompraInsumo> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.id_compra, c.id_insumo, i.nombre AS nom_ins, "
                + "       i.unidad_medida, c.fecha_compra, c.cantidad, "
                + "       c.precio_unitario, c.valor_total, "
                + "       c.proveedor, c.factura, c.observaciones, c.fecha_registro "
                + "FROM compra_insumo c "
                + "JOIN insumo i ON i.id_insumo = c.id_insumo "
                + "WHERE 1=1 "
        );

        if (desde != null) {
            sql.append("AND c.fecha_compra >= ? ");
        }
        if (hasta != null) {
            sql.append("AND c.fecha_compra <= ? ");
        }
        sql.append("ORDER BY c.fecha_compra DESC");

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (desde != null) {
                ps.setDate(idx++, Date.valueOf(desde));
            }
            if (hasta != null) {
                ps.setDate(idx, Date.valueOf(hasta));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapCompra(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Inserta una compra Y actualiza el stock y precio vigente del insumo. Se
     * ejecuta en una sola transacción.
     */
    public boolean insertarCompra(CompraInsumo compra) throws SQLException {
        String sqlCompra = "INSERT INTO compra_insumo "
                + "(id_insumo, fecha_compra, cantidad, precio_unitario, "
                + " proveedor, factura, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlStock = "UPDATE insumo SET "
                + "stock_actual = stock_actual + ?, "
                + "precio_unitario = ? " // actualiza precio vigente
                + "WHERE id_insumo = ?";

        Connection cn = conexion.getConexion();
        try {
            cn.setAutoCommit(false);

            try (PreparedStatement ps1 = cn.prepareStatement(sqlCompra)) {
                ps1.setInt(1, compra.getIdInsumo());
                ps1.setDate(2, Date.valueOf(compra.getFechaCompra()));
                ps1.setBigDecimal(3, compra.getCantidad());
                ps1.setBigDecimal(4, compra.getPrecioUnitario());
                ps1.setString(5, compra.getProveedor());
                ps1.setString(6, compra.getFactura());
                ps1.setString(7, compra.getObservaciones());
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = cn.prepareStatement(sqlStock)) {
                ps2.setBigDecimal(1, compra.getCantidad());
                ps2.setBigDecimal(2, compra.getPrecioUnitario());
                ps2.setInt(3, compra.getIdInsumo());
                ps2.executeUpdate();
            }

            cn.commit();
            return true;

        } catch (SQLException ex) {
            cn.rollback();
            throw ex;
        } finally {
            cn.setAutoCommit(true);
            cn.close();
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  USOS (SALIDAS)
    // ══════════════════════════════════════════════════════════════
    /**
     * Lista los usos de insumos. Filtra opcionalmente por animal. Pasa idAnimal
     * = 0 para traer todos.
     *
     * @param idAnimal
     */
    public List<UsoInsumo> listarUsos(int idAnimal,
            LocalDate desde,
            LocalDate hasta) throws SQLException {
        List<UsoInsumo> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT u.id_uso, u.id_insumo, i.nombre AS nom_ins, "
                + "       i.unidad_medida, u.fecha_uso, u.cantidad, "
                + "       u.precio_unitario, u.valor_total, "
                + "       u.id_animal, u.lote, u.motivo, u.fecha_registro "
                + "FROM uso_insumo u "
                + "JOIN insumo i ON i.id_insumo = u.id_insumo "
                + "WHERE 1=1 "
        );

        if (idAnimal > 0) {
            sql.append("AND u.id_animal = ? ");
        }
        if (desde != null) {
            sql.append("AND u.fecha_uso >= ? ");
        }
        if (hasta != null) {
            sql.append("AND u.fecha_uso <= ? ");
        }
        sql.append("ORDER BY u.fecha_uso DESC");

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (idAnimal > 0) {
                ps.setInt(idx++, idAnimal);
            }
            if (desde != null) {
                ps.setDate(idx++, Date.valueOf(desde));
            }
            if (hasta != null) {
                ps.setDate(idx, Date.valueOf(hasta));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapUso(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Registra un uso Y descuenta del stock. Lanza SQLException si no hay stock
     * suficiente.
     */
    public boolean insertarUso(UsoInsumo uso) throws SQLException {
        // Verificar stock disponible
        String sqlCheck = "SELECT stock_actual FROM insumo WHERE id_insumo = ?";
        String sqlUso = "INSERT INTO uso_insumo "
                + "(id_insumo, fecha_uso, cantidad, precio_unitario, "
                + " id_animal, lote, motivo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE insumo SET stock_actual = stock_actual - ? "
                + "WHERE id_insumo = ?";

        Connection cn = conexion.getConexion();
        try {
            cn.setAutoCommit(false);

            // Verificar stock
            try (PreparedStatement psCheck = cn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, uso.getIdInsumo());
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        BigDecimal stockDisp = rs.getBigDecimal("stock_actual");
                        if (stockDisp.compareTo(uso.getCantidad()) < 0) {
                            cn.rollback();
                            throw new SQLException(
                                    "Stock insuficiente. Disponible: "
                                    + stockDisp.toPlainString()
                                    + " — Solicitado: "
                                    + uso.getCantidad().toPlainString()
                            );
                        }
                    }
                }
            }

            // Insertar uso
            try (PreparedStatement ps1 = cn.prepareStatement(sqlUso)) {
                ps1.setInt(1, uso.getIdInsumo());
                ps1.setDate(2, Date.valueOf(uso.getFechaUso()));
                ps1.setBigDecimal(3, uso.getCantidad());
                ps1.setBigDecimal(4, uso.getPrecioUnitario());
                if (uso.getIdAnimal() != null) {
                    ps1.setInt(5, uso.getIdAnimal());
                } else {
                    ps1.setNull(5, Types.INTEGER);
                }
                ps1.setString(6, uso.getLote());
                ps1.setString(7, uso.getMotivo());
                ps1.executeUpdate();
            }

            // Descontar stock
            try (PreparedStatement ps2 = cn.prepareStatement(sqlStock)) {
                ps2.setBigDecimal(1, uso.getCantidad());
                ps2.setInt(2, uso.getIdInsumo());
                ps2.executeUpdate();
            }

            cn.commit();
            return true;

        } catch (SQLException ex) {
            cn.rollback();
            throw ex;
        } finally {
            cn.setAutoCommit(true);
            cn.close();
        }
    }

    // ══════════════════════════════════════════════════════════════
    //  REPORTE: GASTO POR ANIMAL
    // ══════════════════════════════════════════════════════════════
    /**
     * Devuelve el total gastado en insumos para un animal. Usado por el módulo
     * de Reportes / rentabilidad.
     */
    public BigDecimal totalGastoInsumoAnimal(int idAnimal) throws SQLException {
        String sql = "SELECT COALESCE(SUM(valor_total), 0) AS total "
                + "FROM uso_insumo WHERE id_animal = ?";

        try (Connection cn = conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAnimal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    // ══════════════════════════════════════════════════════════════
    //  MAPPERS PRIVADOS
    // ══════════════════════════════════════════════════════════════
    private CategoriaInsumo mapCategoria(ResultSet rs) throws SQLException {
        CategoriaInsumo c = new CategoriaInsumo();
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setActivo(rs.getBoolean("activo"));
        return c;
    }

    private Insumo mapInsumo(ResultSet rs) throws SQLException {
        Insumo i = new Insumo();
        i.setIdInsumo(rs.getInt("id_insumo"));
        i.setIdCategoria(rs.getInt("id_categoria"));
        i.setNombreCategoria(rs.getString("nom_cat"));
        i.setNombre(rs.getString("nombre"));
        i.setDescripcion(rs.getString("descripcion"));
        i.setUnidadMedida(rs.getString("unidad_medida"));
        i.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        i.setStockActual(rs.getBigDecimal("stock_actual"));
        i.setStockMinimo(rs.getBigDecimal("stock_minimo"));
        i.setActivo(rs.getBoolean("activo"));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) {
            i.setFechaRegistro(ts.toLocalDateTime());
        }
        return i;
    }

    private CompraInsumo mapCompra(ResultSet rs) throws SQLException {
        CompraInsumo c = new CompraInsumo();
        c.setIdCompra(rs.getInt("id_compra"));
        c.setIdInsumo(rs.getInt("id_insumo"));
        c.setNombreInsumo(rs.getString("nom_ins"));
        c.setUnidadMedida(rs.getString("unidad_medida"));
        Date d = rs.getDate("fecha_compra");
        if (d != null) {
            c.setFechaCompra(d.toLocalDate());
        }
        c.setCantidad(rs.getBigDecimal("cantidad"));
        c.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        c.setValorTotal(rs.getBigDecimal("valor_total"));
        c.setProveedor(rs.getString("proveedor"));
        c.setFactura(rs.getString("factura"));
        c.setObservaciones(rs.getString("observaciones"));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) {
            c.setFechaRegistro(ts.toLocalDateTime());
        }
        return c;
    }

    private UsoInsumo mapUso(ResultSet rs) throws SQLException {
        UsoInsumo u = new UsoInsumo();
        u.setIdUso(rs.getInt("id_uso"));
        u.setIdInsumo(rs.getInt("id_insumo"));
        u.setNombreInsumo(rs.getString("nom_ins"));
        u.setUnidadMedida(rs.getString("unidad_medida"));
        Date d = rs.getDate("fecha_uso");
        if (d != null) {
            u.setFechaUso(d.toLocalDate());
        }
        u.setCantidad(rs.getBigDecimal("cantidad"));
        u.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        u.setValorTotal(rs.getBigDecimal("valor_total"));
        int idAnim = rs.getInt("id_animal");
        u.setIdAnimal(rs.wasNull() ? null : idAnim);
        u.setLote(rs.getString("lote"));
        u.setMotivo(rs.getString("motivo"));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) {
            u.setFechaRegistro(ts.toLocalDateTime());
        }
        return u;
    }
}
