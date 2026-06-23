package clases;

import java.sql.Date;

public class VentaLeche {

    private int idVenta;
    private Date fechaVenta;
    private Date fechaDesde;
    private Date fechaHasta;
    private String entidad;
    private double precioLitro;
    private double litrosTotales;
    private double valorTotal;
    private String observaciones;

    public VentaLeche() {
    }

    public VentaLeche(Date fechaVenta, Date fechaDesde, Date fechaHasta,
            String entidad, double precioLitro,
            double litrosTotales, double valorTotal,
            String observaciones) {
        this.fechaVenta = fechaVenta;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.entidad = entidad;
        this.precioLitro = precioLitro;
        this.litrosTotales = litrosTotales;
        this.valorTotal = valorTotal;
        this.observaciones = observaciones;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int v) {
        this.idVenta = v;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date v) {
        this.fechaVenta = v;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date v) {
        this.fechaDesde = v;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date v) {
        this.fechaHasta = v;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String v) {
        this.entidad = v;
    }

    public double getPrecioLitro() {
        return precioLitro;
    }

    public void setPrecioLitro(double v) {
        this.precioLitro = v;
    }

    public double getLitrosTotales() {
        return litrosTotales;
    }

    public void setLitrosTotales(double v) {
        this.litrosTotales = v;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double v) {
        this.valorTotal = v;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String v) {
        this.observaciones = v;
    }
}
