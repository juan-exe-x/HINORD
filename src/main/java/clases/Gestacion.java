package clases;

import java.time.LocalDate;

/**
 * Representa una gestación de la tabla "gestacion".
 */
public class Gestacion {

    private int    idGestacion;
    private int    idVaca;
    private String tipoFecundacion;

    // Solo uno de estos se usa según el tipo
    private Integer idToro;
    private Integer idLoteSemen;
    private Integer idDonante;

    private LocalDate fechaServicio;
    private LocalDate fechaPartoEstimada;
    private LocalDate fechaConfirmacion;

    private String tipoConfirmacion;
    private String estado;
    private String observaciones;

    // ── NUEVO: número de servicio (1°, 2°, 3°…) ──────────────────────────────
    private int numeroServicio;

    // Campos extra para mostrar en tablas
    private String nombreVaca;
    private long   idicaVaca;
    private String origenGenetico;
    private int    diasParaParto;
    private String alerta;

    public Gestacion() {}

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public int getIdGestacion()                        { return idGestacion; }
    public void setIdGestacion(int idGestacion)        { this.idGestacion = idGestacion; }

    public int getIdVaca()                             { return idVaca; }
    public void setIdVaca(int idVaca)                  { this.idVaca = idVaca; }

    public String getTipoFecundacion()                 { return tipoFecundacion; }
    public void setTipoFecundacion(String t)           { this.tipoFecundacion = t; }

    public Integer getIdToro()                         { return idToro; }
    public void setIdToro(Integer idToro)              { this.idToro = idToro; }

    public Integer getIdLoteSemen()                    { return idLoteSemen; }
    public void setIdLoteSemen(Integer idLoteSemen)    { this.idLoteSemen = idLoteSemen; }

    public Integer getIdDonante()                      { return idDonante; }
    public void setIdDonante(Integer idDonante)        { this.idDonante = idDonante; }

    public LocalDate getFechaServicio()                { return fechaServicio; }
    public void setFechaServicio(LocalDate f)          { this.fechaServicio = f; }

    public LocalDate getFechaPartoEstimada()           { return fechaPartoEstimada; }
    public void setFechaPartoEstimada(LocalDate f)     { this.fechaPartoEstimada = f; }

    public LocalDate getFechaConfirmacion()            { return fechaConfirmacion; }
    public void setFechaConfirmacion(LocalDate f)      { this.fechaConfirmacion = f; }

    public String getTipoConfirmacion()                { return tipoConfirmacion; }
    public void setTipoConfirmacion(String t)          { this.tipoConfirmacion = t; }

    public String getEstado()                          { return estado; }
    public void setEstado(String estado)               { this.estado = estado; }

    public String getObservaciones()                   { return observaciones; }
    public void setObservaciones(String o)             { this.observaciones = o; }

    /** Número de intento: 1 = primer servicio, 2 = segundo, etc. */
    public int getNumeroServicio()                     { return numeroServicio; }
    public void setNumeroServicio(int n)               { this.numeroServicio = n; }

    public String getNombreVaca()                      { return nombreVaca; }
    public void setNombreVaca(String n)                { this.nombreVaca = n; }

    public long getIdicaVaca()                         { return idicaVaca; }
    public void setIdicaVaca(long idicaVaca)           { this.idicaVaca = idicaVaca; }

    public String getOrigenGenetico()                  { return origenGenetico; }
    public void setOrigenGenetico(String o)            { this.origenGenetico = o; }

    public int getDiasParaParto()                      { return diasParaParto; }
    public void setDiasParaParto(int d)                { this.diasParaParto = d; }

    public String getAlerta()                          { return alerta; }
    public void setAlerta(String alerta)               { this.alerta = alerta; }
}