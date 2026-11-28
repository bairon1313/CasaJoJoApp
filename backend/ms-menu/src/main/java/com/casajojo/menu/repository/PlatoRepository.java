
package com.casajojo.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casajojo.menu.entity.Plato;

// JpaRepository proporciona las operaciones CRUD automáticamente.
@Repository
public interface PlatoRepository extends JpaRepository<Plato, Integer> {

    // Obtiene todos los platos disponibles.
    List<Plato> findByDisponibleTrue();

    // Busca platos mediante el identificador de su categoría.
    List<Plato> findByCategoria_IdCategoria(Integer idCategoria);

    // Busca platos mediante el código de su categoría.
    List<Plato> findByCategoria_CodigoCategoria(String codigoCategoria);

    // Obtiene los platos disponibles de una categoría específica.
    List<Plato> findByCategoria_CodigoCategoriaAndDisponibleTrue(
            String codigoCategoria);

    // Busca coincidencias parciales sin distinguir mayúsculas.
    List<Plato> findByNombrePlatoContainingIgnoreCase(String nombrePlato);

    // Comprueba si existe un plato con el mismo nombre.
    boolean existsByNombrePlatoIgnoreCase(String nombrePlato);

}
