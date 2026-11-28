
package com.casajojo.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casajojo.menu.entity.PlantillaDetalle;

@Repository
public interface PlantillaDetalleRepository extends JpaRepository<PlantillaDetalle, Integer> {

    // Obtiene todos los platos asociados a una plantilla.
    List<PlantillaDetalle> findByPlantillaMenu_IdPlantilla(
            Integer idPlantilla);

    // Obtiene las plantillas en las que está incorporado un plato.
    List<PlantillaDetalle> findByPlato_IdPlato(Integer idPlato);

    // Busca un plato específico dentro de una plantilla.
    Optional<PlantillaDetalle> findByPlantillaMenu_IdPlantillaAndPlato_IdPlato(
            Integer idPlantilla, Integer idPlato);

    // Comprueba si un plato ya está incorporado a la plantilla.
    boolean existsByPlantillaMenu_IdPlantillaAndPlato_IdPlato(
            Integer idPlantilla, Integer idPlato);

    // Elimina los detalles de una plantilla dentro de una transacción.
    void deleteByPlantillaMenu_IdPlantilla(Integer idPlantilla);

}
