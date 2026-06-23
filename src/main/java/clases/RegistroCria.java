package clases;

public class RegistroCria {

    private int idCria;
    private int idParto;
    private int idAnimalNuevo;
    private int idMadre;
    private String idPadreOLote;
    private String sexoCria;
    private double pesoNacimiento;
    private String condicionNacimiento;
    private String observaciones;

    public RegistroCria() {
    }

    public int getIdCria() {
        return idCria;
    }

    public void setIdCria(int idCria) {
        this.idCria = idCria;
    }

    public int getIdParto() {
        return idParto;
    }

    public void setIdParto(int idParto) {
        this.idParto = idParto;
    }

    public int getIdAnimalNuevo() {
        return idAnimalNuevo;
    }

    public void setIdAnimalNuevo(int idAnimalNuevo) {
        this.idAnimalNuevo = idAnimalNuevo;
    }

    public int getIdMadre() {
        return idMadre;
    }

    public void setIdMadre(int idMadre) {
        this.idMadre = idMadre;
    }

    public String getIdPadreOLote() {
        return idPadreOLote;
    }

    public void setIdPadreOLote(String idPadreOLote) {
        this.idPadreOLote = idPadreOLote;
    }

    public String getSexoCria() {
        return sexoCria;
    }

    public void setSexoCria(String sexoCria) {
        this.sexoCria = sexoCria;
    }

    public double getPesoNacimiento() {
        return pesoNacimiento;
    }

    public void setPesoNacimiento(double pesoNacimiento) {
        this.pesoNacimiento = pesoNacimiento;
    }

    public String getCondicionNacimiento() {
        return condicionNacimiento;
    }

    public void setCondicionNacimiento(String condicionNacimiento) {
        this.condicionNacimiento = condicionNacimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
