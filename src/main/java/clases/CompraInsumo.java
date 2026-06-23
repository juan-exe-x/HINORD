package clases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo para una compra (entrada) de insumo. Tabla: compra_insumo
 */
public class CompraInsumo {

    private int idCompra;
    private int idInsumo;
    private String nombreInsumo;        // join de lectura
    private String unidadMedida;        // join de lectura
    private LocalDate fechaCompra;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal valorTotal;          // columna generada en BD
    private String proveedor;
    private String factura;
    private String observaciones;
    private LocalDateTime fechaRegistro;

    // ── Constructor ────────────────────────────────────────────────
    public CompraInsumo() {
        this.cantidad = BigDecimal.ZERO;
        this.precioUnitario = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
    }

    // ── Helper: calcula el total localmente antes de persistir ─────
    public BigDecimal calcularTotal() {
        if (cantidad != null && precioUnitario != null) {
            return cantidad.multiply(precioUnitario);
        }
        return BigDecimal.ZERO;
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int v) {
        this.idCompra = v;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int v) {
        this.idInsumo = v;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String v) {
        this.nombreInsumo = v;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String v) {
        this.unidadMedida = v;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate v) {
        this.fechaCompra = v;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal v) {
        this.cantidad = v;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal v) {
        this.precioUnitario = v;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal v) {
        this.valorTotal = v;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String v) {
        this.proveedor = v;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String v) {
        this.factura = v;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String v) {
        this.observaciones = v;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime v) {
        this.fechaRegistro = v;
    }
}
