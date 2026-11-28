package com.casajojo.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casajojo.menu.entity.Categoria;

@Repository 
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByActivoTrue();

    Optional<Categoria> findByCodigoCategoria(String codigoCategoria);

    boolean existsByCodigoCategoriaIgnoreCase(String codigoCategoria);

    boolean existsByNombreCategoriaIgnoreCase(String nombreCategoria);
}
