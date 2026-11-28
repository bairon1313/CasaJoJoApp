package com.casajojo.menu.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.casajojo.menu.service.CategoriaService;
import com.casajojo.menu.dto.request.CategoriaRequest;
import com.casajojo.menu.dto.response.CategoriaResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Administración de las categorías de platos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Obtiene todas las categorías registradas.
    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {

        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    // Obtiene únicamente las categorías activas.
    @GetMapping("/activas")
    @Operation(summary = "Listar categorías activas")
    public ResponseEntity<List<CategoriaResponse>> listarCategoriasActivas() {

        return ResponseEntity.ok(
                categoriaService.listarCategoriasActivas());
    }

    // Busca una categoría mediante su identificador.
    @GetMapping("/{idCategoria}")
    @Operation(summary = "Buscar categoría por ID")
    public ResponseEntity<CategoriaResponse> buscarCategoriaPorId(
            @PathVariable Integer idCategoria) {

        return ResponseEntity.ok(
                categoriaService.buscarCategoriaPorId(idCategoria));
    }

    // Registra una categoría validando los datos del Request.
    @PostMapping
    @Operation(summary = "Crear una categoría")
    public ResponseEntity<CategoriaResponse> crearCategoria(
            @Valid @RequestBody CategoriaRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.crearCategoria(request));
    }

    // Actualiza una categoría existente.
    @PutMapping("/{idCategoria}")
    @Operation(summary = "Actualizar una categoría")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @PathVariable Integer idCategoria,
            @Valid @RequestBody CategoriaRequest request) {

        return ResponseEntity.ok(
                categoriaService.actualizarCategoria(idCategoria, request));
    }

    // Permite activar o desactivar una categoría.
    @PatchMapping("/{idCategoria}/estado")
    @Operation(summary = "Cambiar estado de una categoría")
    public ResponseEntity<CategoriaResponse> cambiarEstadoCategoria(
            @PathVariable Integer idCategoria,
            @RequestParam boolean activo) {

        return ResponseEntity.ok(
                categoriaService.cambiarEstadoCategoria(idCategoria, activo));
    }

    // Realiza una eliminación lógica de la categoría.
    @DeleteMapping("/{idCategoria}")
    @Operation(summary = "Eliminar una categoría")
    public ResponseEntity<Void> eliminarCategoria(
            @PathVariable Integer idCategoria) {

        categoriaService.eliminarCategoria(idCategoria);

        return ResponseEntity.noContent().build();
    }

}
