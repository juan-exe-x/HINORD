package clases;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo para insumo del catálogo maestro. Tabla: insumo
 */
public class Insumo {

    private int idInsumo;
    private int idCategoria;
    private String nombreCategoria;     // join de lectura, no se persiste
    private String nombre;
    private String descripcion;
    private String unidadMedida;
    private BigDecimal precioUnitario;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    // ── Constructores ──────────────────────────────────────────────
    public Insumo() {
        this.precioUnitario = BigDecimal.ZERO;
        this.stockActual = BigDecimal.ZERO;
        this.stockMinimo = BigDecimal.ZERO;
        this.activo = true;
    }

    // ── toString (para JComboBox en uso/compra) ───────────────────
    @Override
    public String toString() {
        return nombre + " [" + unidadMedida + "]";
    }

    // ── Helpers ────────────────────────────────────────────────────
    /**
     * Verdadero si el stock está en o por debajo del mínimo.
     */
    public boolean isStockBajo() {
        return stockActual.compareTo(stockMinimo) <= 0;
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int v) {
        this.idInsumo = v;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int v) {
        this.idCategoria = v;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String v) {
        this.nombreCategoria = v;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String v) {
        this.nombre = v;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String v) {
        this.descripcion = v;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String v) {
        this.unidadMedida = v;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal v) {
        this.precioUnitario = v;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }

    public void setStockActual(BigDecimal v) {
        this.stockActual = v;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal v) {
        this.stockMinimo = v;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean v) {
        this.activo = v;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime v) {
        this.fechaRegistro = v;
    }
}
