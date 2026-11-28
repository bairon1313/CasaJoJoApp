
package com.casajojo.menu.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casajojo.menu.entity.MenuDiario;

@Repository
public interface MenuDiarioRepository extends JpaRepository<MenuDiario, Integer> {

    // Busca el menú correspondiente a una fecha específica.
    Optional<MenuDiario> findByFechaMenu(LocalDate fechaMenu);

    // Comprueba si ya existe un menú para evitar duplicar fechas.
    boolean existsByFechaMenu(LocalDate fechaMenu);

    // Obtiene los menús según su estado.
    List<MenuDiario> findByEstado(String estado);

    // Busca el menú de una fecha que tenga un estado determinado.
    Optional<MenuDiario> findByFechaMenuAndEstado(
            LocalDate fechaMenu, String estado);

    // Obtiene los menús dentro de un rango de fechas.
    List<MenuDiario> findByFechaMenuBetween(
            LocalDate fechaInicio, LocalDate fechaFin);

    // Lista todos los menús desde el más reciente al más antiguo.
    List<MenuDiario> findAllByOrderByFechaMenuDesc();

}
