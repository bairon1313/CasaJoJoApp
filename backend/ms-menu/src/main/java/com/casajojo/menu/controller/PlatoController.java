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

import com.casajojo.menu.service.PlatoService;
import com.casajojo.menu.dto.request.PlatoRequest;
import com.casajojo.menu.dto.response.PlatoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "Administración del catálogo de platos")
public class PlatoController {

    private final PlatoService platoService;

    // Obtiene todos los platos registrados en el catálogo.
    @GetMapping
    @Operation(summary = "Listar todos los platos")
    public ResponseEntity<List<PlatoResponse>> listarPlatos() {

        return ResponseEntity.ok(platoService.listarPlatos());
    }

    // Obtiene los platos disponibles de categorías activas.
    @GetMapping("/disponibles")
    @Operation(summary = "Listar platos disponibles")
    public ResponseEntity<List<PlatoResponse>> listarPlatosDisponibles() {

        return ResponseEntity.ok(
                platoService.listarPlatosDisponibles());
    }

    // Busca un plato mediante su identificador.
    @GetMapping("/{idPlato}")
    @Operation(summary = "Buscar plato por ID")
    public ResponseEntity<PlatoResponse> buscarPlatoPorId(
            @PathVariable Integer idPlato) {

        return ResponseEntity.ok(
                platoService.buscarPlatoPorId(idPlato));
    }

    // Filtra los platos utilizando el código de la categoría.
    @GetMapping("/categoria/{codigoCategoria}")
    @Operation(summary = "Listar platos por categoría")
    public ResponseEntity<List<PlatoResponse>> listarPlatosPorCategoria(
            @PathVariable String codigoCategoria) {

        return ResponseEntity.ok(
                platoService.listarPlatosPorCategoria(codigoCategoria));
    }

    // Busca platos por coincidencia parcial de su nombre.
    @GetMapping("/buscar")
    @Operation(summary = "Buscar platos por nombre")
    public ResponseEntity<List<PlatoResponse>> buscarPlatosPorNombre(
            @RequestParam String nombrePlato) {

        return ResponseEntity.ok(
                platoService.buscarPlatosPorNombre(nombrePlato));
    }

    // Registra un plato validando los datos recibidos.
    @PostMapping
    @Operation(summary = "Crear un plato")
    public ResponseEntity<PlatoResponse> crearPlato(
            @Valid @RequestBody PlatoRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(platoService.crearPlato(request));
    }

    // Actualiza los datos de un plato existente.
    @PutMapping("/{idPlato}")
    @Operation(summary = "Actualizar un plato")
    public ResponseEntity<PlatoResponse> actualizarPlato(
            @PathVariable Integer idPlato,
            @Valid @RequestBody PlatoRequest request) {

        return ResponseEntity.ok(
                platoService.actualizarPlato(idPlato, request));
    }

    // Permite habilitar o deshabilitar un plato del catálogo.
    @PatchMapping("/{idPlato}/disponibilidad")
    @Operation(summary = "Cambiar disponibilidad de un plato")
    public ResponseEntity<PlatoResponse> cambiarDisponibilidadPlato(
            @PathVariable Integer idPlato,
            @RequestParam boolean disponible) {

        return ResponseEntity.ok(
                platoService.cambiarDisponibilidadPlato(
                        idPlato, disponible));
    }

    // Realiza una eliminación lógica del plato.
    @DeleteMapping("/{idPlato}")
    @Operation(summary = "Eliminar un plato")
    public ResponseEntity<Void> eliminarPlato(
            @PathVariable Integer idPlato) {

        platoService.eliminarPlato(idPlato);

        return ResponseEntity.noContent().build();
    }

}
