package clases;

/**
 * Representa un animal de la tabla "registro".
 * Se usa para llenar los combos de vacas y toros.
 */
public class Animal {

    private int idregistro;
    private String nombre;
    private long IDICA;
    private String raza;
    private String sexo;
    private int edad;
    private String estadoAnimal;

    public Animal() {}

    public Animal(int idregistro, String nombre, long IDICA,
                  String raza, String sexo, int edad,
                  String estadoAnimal) {

        this.idregistro = idregistro;
        this.nombre = nombre;
        this.IDICA = IDICA;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.estadoAnimal = estadoAnimal;
    }

    // Getters y Setters

    public int getIdregistro() {
        return idregistro;
    }

    public void setIdregistro(int idregistro) {
        this.idregistro = idregistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getIDICA() {
        return IDICA;
    }

    public void setIDICA(long idica) {
        this.IDICA = idica;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEstadoAnimal() {
        return estadoAnimal;
    }

    public void setEstadoAnimal(String estadoAnimal) {
        this.estadoAnimal = estadoAnimal;
    }

    /**
     * Lo que aparece en el JComboBox
     */
    @Override
    public String toString() {
        return IDICA + " · " + nombre + " · " + raza;
    }
}