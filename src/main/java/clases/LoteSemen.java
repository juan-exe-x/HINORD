
package clases;


import java.time.LocalDate;
 
/**
 * Representa un lote de semen de la tabla "lote_semen".
 */
public class LoteSemen {
 
    private int       idLote;
    private String    codigoLote;
    private String    razaToro;
    private String    nombreToro;
    private String    proveedor;
    private LocalDate fechaIngreso;
    private int       dosisDisponibles;
    private boolean   activo;
 
    public LoteSemen() {}
 
    public LoteSemen(int idLote, String codigoLote, String razaToro,
                     String nombreToro, String proveedor,
                     LocalDate fechaIngreso, int dosisDisponibles, boolean activo) {
        this.idLote           = idLote;
        this.codigoLote       = codigoLote;
        this.razaToro         = razaToro;
        this.nombreToro       = nombreToro;
        this.proveedor        = proveedor;
        this.fechaIngreso     = fechaIngreso;
        this.dosisDisponibles = dosisDisponibles;
        this.activo           = activo;
    }
 
    // Getters y Setters
    public int       getIdLote()           { return idLote; }
    public void      setIdLote(int idLote) { this.idLote = idLote; }
 
    public String    getCodigoLote()       { return codigoLote; }
    public void      setCodigoLote(String codigoLote) { this.codigoLote = codigoLote; }
 
    public String    getRazaToro()         { return razaToro; }
    public void      setRazaToro(String razaToro) { this.razaToro = razaToro; }
 
    public String    getNombreToro()       { return nombreToro; }
    public void      setNombreToro(String nombreToro) { this.nombreToro = nombreToro; }
 
    public String    getProveedor()        { return proveedor; }
    public void      setProveedor(String proveedor) { this.proveedor = proveedor; }
 
    public LocalDate getFechaIngreso()     { return fechaIngreso; }
    public void      setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
 
    public int       getDosisDisponibles() { return dosisDisponibles; }
    public void      setDosisDisponibles(int dosisDisponibles) { this.dosisDisponibles = dosisDisponibles; }
 
    public boolean   isActivo()            { return activo; }
    public void      setActivo(boolean activo) { this.activo = activo; }
 
    /**
     * Lo que aparece en el JComboBox: "LOT-2025-01 · Simmental · ABS"
     */
    @Override
    public String toString() {
        return codigoLote + " · " + razaToro + " · " + proveedor
               + " (" + dosisDisponibles + " dosis)";
    }
}
