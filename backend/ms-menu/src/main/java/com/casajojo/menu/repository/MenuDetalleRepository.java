
package com.casajojo.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casajojo.menu.entity.MenuDetalle;

@Repository
public interface MenuDetalleRepository extends JpaRepository<MenuDetalle, Integer> {

    // Obtiene todos los platos asociados a un menú diario.
    List<MenuDetalle> findByMenuDiario_IdMenu(Integer idMenu);

    // Obtiene solamente los platos disponibles de un menú diario.
    List<MenuDetalle> findByMenuDiario_IdMenuAndDisponibleTrue(
            Integer idMenu);

    // Busca los menús en los que está incorporado un plato.
    List<MenuDetalle> findByPlato_IdPlato(Integer idPlato);

    // Busca un plato específico dentro de un menú diario.
    Optional<MenuDetalle> findByMenuDiario_IdMenuAndPlato_IdPlato(
            Integer idMenu, Integer idPlato);

    // Comprueba si un plato ya está incorporado al menú.
    boolean existsByMenuDiario_IdMenuAndPlato_IdPlato(
            Integer idMenu, Integer idPlato);

    // Elimina todos los detalles asociados a un menú diario.
    void deleteByMenuDiario_IdMenu(Integer idMenu);

}
