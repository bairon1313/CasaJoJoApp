
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.casajojo.menu.service.PlantillaDetalleService;
import com.casajojo.menu.dto.request.PlantillaDetalleRequest;
import com.casajojo.menu.dto.response.PlantillaDetalleResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantilla-detalles")
@RequiredArgsConstructor
@Tag(name = "Detalles de plantilla", description = "Administración de los platos de las plantillas")
public class PlantillaDetalleController {

    private final PlantillaDetalleService plantillaDetalleService;

    // Obtiene todos los detalles de las plantillas.
    @GetMapping
    @Operation(summary = "Listar todos los detalles de plantilla")
    public ResponseEntity<List<PlantillaDetalleResponse>> listarDetalles() {

        return ResponseEntity.ok(
                plantillaDetalleService.listarDetalles());
    }

    // Busca un detalle mediante su identificador.
    @GetMapping("/{idPlantillaDetalle}")
    @Operation(summary = "Buscar detalle de plantilla por ID")
    public ResponseEntity<PlantillaDetalleResponse> buscarDetallePorId(
            @PathVariable Integer idPlantillaDetalle) {

        return ResponseEntity.ok(
                plantillaDetalleService.buscarDetallePorId(
                        idPlantillaDetalle));
    }

    // Obtiene todos los platos asociados a una plantilla.
    @GetMapping("/plantilla/{idPlantilla}")
    @Operation(summary = "Listar platos de una plantilla")
    public ResponseEntity<List<PlantillaDetalleResponse>> listarDetallesPorPlantilla(
            @PathVariable Integer idPlantilla) {

        return ResponseEntity.ok(
                plantillaDetalleService.listarDetallesPorPlantilla(
                        idPlantilla));
    }

    // Incorpora un plato existente a una plantilla.
    @PostMapping
    @Operation(summary = "Incorporar un plato a una plantilla")
    public ResponseEntity<PlantillaDetalleResponse> crearDetalle(
            @Valid @RequestBody PlantillaDetalleRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(plantillaDetalleService.crearDetalle(request));
    }

    // Actualiza las asociaciones de un detalle existente.
    @PutMapping("/{idPlantillaDetalle}")
    @Operation(summary = "Actualizar un detalle de plantilla")
    public ResponseEntity<PlantillaDetalleResponse> actualizarDetalle(
            @PathVariable Integer idPlantillaDetalle,
            @Valid @RequestBody PlantillaDetalleRequest request) {

        return ResponseEntity.ok(
                plantillaDetalleService.actualizarDetalle(
                        idPlantillaDetalle, request));
    }

    // Elimina la asociación sin eliminar el plato del catálogo.
    @DeleteMapping("/{idPlantillaDetalle}")
    @Operation(summary = "Eliminar un plato de una plantilla")
    public ResponseEntity<Void> eliminarDetalle(
            @PathVariable Integer idPlantillaDetalle) {

        plantillaDetalleService.eliminarDetalle(idPlantillaDetalle);

        return ResponseEntity.noContent().build();
    }

}
