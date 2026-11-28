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

import com.casajojo.menu.service.MenuDetalleService;
import com.casajojo.menu.dto.request.MenuDetalleRequest;
import com.casajojo.menu.dto.response.MenuDetalleResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menu-detalles")
@RequiredArgsConstructor
@Tag(name = "Detalles de menú", description = "Administración de los platos del menú diario")
public class MenuDetalleController {

    private final MenuDetalleService menuDetalleService;

    // Obtiene todos los detalles de menú registrados.
    @GetMapping
    @Operation(summary = "Listar todos los detalles de menú")
    public ResponseEntity<List<MenuDetalleResponse>> listarDetalles() {

        return ResponseEntity.ok(
                menuDetalleService.listarDetalles());
    }

    // Busca un detalle mediante su identificador.
    @GetMapping("/{idMenuDetalle}")
    @Operation(summary = "Buscar detalle de menú por ID")
    public ResponseEntity<MenuDetalleResponse> buscarDetallePorId(
            @PathVariable Integer idMenuDetalle) {

        return ResponseEntity.ok(
                menuDetalleService.buscarDetallePorId(idMenuDetalle));
    }

    // Obtiene todos los platos asociados a un menú diario.
    @GetMapping("/menu/{idMenu}")
    @Operation(summary = "Listar platos de un menú diario")
    public ResponseEntity<List<MenuDetalleResponse>> listarDetallesPorMenu(
            @PathVariable Integer idMenu) {

        return ResponseEntity.ok(
                menuDetalleService.listarDetallesPorMenu(idMenu));
    }

    // Obtiene los platos disponibles de un menú publicado.
    @GetMapping("/menu/{idMenu}/disponibles")
    @Operation(summary = "Listar platos disponibles de un menú")
    public ResponseEntity<List<MenuDetalleResponse>> listarDetallesDisponibles(
            @PathVariable Integer idMenu) {

        return ResponseEntity.ok(
                menuDetalleService.listarDetallesDisponibles(idMenu));
    }

    // Incorpora un plato existente a un menú diario.
    @PostMapping
    @Operation(summary = "Incorporar un plato al menú")
    public ResponseEntity<MenuDetalleResponse> crearDetalle(
            @Valid @RequestBody MenuDetalleRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuDetalleService.crearDetalle(request));
    }

    // Actualiza los datos de un detalle existente.
    @PutMapping("/{idMenuDetalle}")
    @Operation(summary = "Actualizar un detalle de menú")
    public ResponseEntity<MenuDetalleResponse> actualizarDetalle(
            @PathVariable Integer idMenuDetalle,
            @Valid @RequestBody MenuDetalleRequest request) {

        return ResponseEntity.ok(
                menuDetalleService.actualizarDetalle(
                        idMenuDetalle, request));
    }

    // Permite cambiar la disponibilidad de un plato durante la jornada.
    @PatchMapping("/{idMenuDetalle}/disponibilidad")
    @Operation(summary = "Cambiar disponibilidad de un plato del menú")
    public ResponseEntity<MenuDetalleResponse> cambiarDisponibilidadDetalle(
            @PathVariable Integer idMenuDetalle,
            @RequestParam boolean disponible) {

        return ResponseEntity.ok(
                menuDetalleService.cambiarDisponibilidadDetalle(
                        idMenuDetalle, disponible));
    }

    // Elimina la asociación entre el menú diario y el plato.
    @DeleteMapping("/{idMenuDetalle}")
    @Operation(summary = "Eliminar un plato del menú diario")
    public ResponseEntity<Void> eliminarDetalle(
            @PathVariable Integer idMenuDetalle) {

        menuDetalleService.eliminarDetalle(idMenuDetalle);

        return ResponseEntity.noContent().build();
    }

}
