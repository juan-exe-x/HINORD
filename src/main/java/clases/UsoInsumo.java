package clases;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo para un uso (salida) de insumo. Puede estar asociado a un animal
 * específico o a un lote/potrero. Tabla: uso_insumo
 */
public class UsoInsumo {

    private int idUso;
    private int idInsumo;
    private String nombreInsumo;        // join de lectura
    private String unidadMedida;        // join de lectura
    private LocalDate fechaUso;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal valorTotal;          // columna generada en BD
    private Integer idAnimal;            // null = aplicado a lote/finca
    private String lote;
    private String motivo;
    private LocalDateTime fechaRegistro;

    // ── Constructor ────────────────────────────────────────────────
    public UsoInsumo() {
        this.cantidad = BigDecimal.ZERO;
        this.precioUnitario = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
    }

    // ── Helper ─────────────────────────────────────────────────────
    public BigDecimal calcularTotal() {
        if (cantidad != null && precioUnitario != null) {
            return cantidad.multiply(precioUnitario);
        }
        return BigDecimal.ZERO;
    }

    /**
     * Indica si el uso fue aplicado a un animal individual.
     */
    public boolean esUsoIndividual() {
        return idAnimal != null && idAnimal > 0;
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public int getIdUso() {
        return idUso;
    }

    public void setIdUso(int v) {
        this.idUso = v;
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

    public LocalDate getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(LocalDate v) {
        this.fechaUso = v;
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

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer v) {
        this.idAnimal = v;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String v) {
        this.lote = v;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String v) {
        this.motivo = v;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime v) {
        this.fechaRegistro = v;
    }
}
