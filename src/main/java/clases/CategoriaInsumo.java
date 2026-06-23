package clases;

/**
 * Modelo para categoría de insumo. Tabla: categoria_insumo
 */
public class CategoriaInsumo {

    private int idCategoria;
    private String nombre;
    private String descripcion;
    private boolean activo;

    // ── Constructores ──────────────────────────────────────────────
    public CategoriaInsumo() {
    }

    public CategoriaInsumo(int idCategoria, String nombre,
            String descripcion, boolean activo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    // ── toString (usado en JComboBox directamente) ─────────────────
    @Override
    public String toString() {
        return nombre;
    }

    // ── Getters / Setters ──────────────────────────────────────────
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int v) {
        this.idCategoria = v;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean v) {
        this.activo = v;
    }
}
