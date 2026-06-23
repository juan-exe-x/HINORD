
package dao;

import clases.Animal;
import clases.conexion;
import java.sql.Connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para leer animales de la tabla "registro".
 * Separa vacas (HEMBRA) y toros (MACHO) para los combos del formulario.
 */
public class AnimalDAO {

    /**
     * Devuelve todas las hembras activas.
     * Úsalo para el combo "Seleccionar vaca" y "Vaca donante".
     * @return 
     */
    public List<Animal> obtenerVacasActivas() {
        List<Animal> lista = new ArrayList<>();
        String sql = "SELECT idregistro, nombre, IDICA, raza, sexo, edad, estado_animal "
                   + "FROM registro "
                   + "WHERE UPPER(sexo) = 'HEMBRA' "
                   + "  AND UPPER(estado_animal) = 'ACTIVO' "
                   + "ORDER BY nombre ASC";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearAnimal(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener vacas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Devuelve todos los machos activos.
     * Úsalo para el combo "Seleccionar toro" en monta natural.
     * @return 
     */
    public List<Animal> obtenerTorosActivos() {
        List<Animal> lista = new ArrayList<>();
        String sql = "SELECT idregistro, nombre, IDICA, raza, sexo, edad, estado_animal "
                   + "FROM registro "
                   + "WHERE UPPER(sexo) = 'MACHO' "
                   + "  AND UPPER(estado_animal) = 'ACTIVO' "
                   + "ORDER BY IDICA ASC";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearAnimal(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener toros: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un animal por su idregistro.
     * @return 
     */
    public Animal obtenerPorId(int idregistro) {
        String sql = "SELECT idregistro, nombre, IDICA, raza, sexo, edad, estado_animal "
                   + "FROM registro WHERE idregistro = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idregistro);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapearAnimal(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener animal por ID: " + e.getMessage());
        }
        return null;
    }

    private Animal mapearAnimal(ResultSet rs) throws SQLException {
        return new Animal(
            rs.getInt("idregistro"),
            rs.getString("nombre"),
            rs.getLong("IDICA"),
            rs.getString("raza"),
            rs.getString("sexo"),
            rs.getInt("edad"),
            rs.getString("estado_animal")
        );
    }
}
