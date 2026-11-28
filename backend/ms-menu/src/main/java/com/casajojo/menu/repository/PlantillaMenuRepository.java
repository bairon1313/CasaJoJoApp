
package com.casajojo.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casajojo.menu.entity.PlantillaMenu;

@Repository
public interface PlantillaMenuRepository extends JpaRepository<PlantillaMenu, Integer> {

    // Obtiene las plantillas activas que pueden utilizarse.
    List<PlantillaMenu> findByActivoTrue();

    // Busca una plantilla por su nombre exacto.
    Optional<PlantillaMenu> findByNombrePlantilla(String nombrePlantilla);

    // Busca coincidencias parciales sin distinguir mayúsculas.
    List<PlantillaMenu> findByNombrePlantillaContainingIgnoreCase(
            String nombrePlantilla);

    // Comprueba si existe una plantilla con el mismo nombre.
    boolean existsByNombrePlantillaIgnoreCase(String nombrePlantilla);

}
