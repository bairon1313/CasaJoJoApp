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

import com.casajojo.menu.service.PlantillaMenuService;
import com.casajojo.menu.dto.request.PlantillaMenuRequest;
import com.casajojo.menu.dto.response.PlantillaMenuResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plantillas")
@RequiredArgsConstructor
@Tag(name = "Plantillas de menú", description = "Administración de plantillas reutilizables")
public class PlantillaMenuController {

    private final PlantillaMenuService plantillaMenuService;

    // Obtiene todas las plantillas registradas.
    @GetMapping
    @Operation(summary = "Listar todas las plantillas")
    public ResponseEntity<List<PlantillaMenuResponse>> listarPlantillas() {

        return ResponseEntity.ok(
                plantillaMenuService.listarPlantillas());
    }

    // Obtiene únicamente las plantillas activas.
    @GetMapping("/activas")
    @Operation(summary = "Listar plantillas activas")
    public ResponseEntity<List<PlantillaMenuResponse>> listarPlantillasActivas() {

        return ResponseEntity.ok(
                plantillaMenuService.listarPlantillasActivas());
    }

    // Busca una plantilla mediante su identificador.
    @GetMapping("/{idPlantilla}")
    @Operation(summary = "Buscar plantilla por ID")
    public ResponseEntity<PlantillaMenuResponse> buscarPlantillaPorId(
            @PathVariable Integer idPlantilla) {

        return ResponseEntity.ok(
                plantillaMenuService.buscarPlantillaPorId(idPlantilla));
    }

    // Busca plantillas por coincidencia parcial de su nombre.
    @GetMapping("/buscar")
    @Operation(summary = "Buscar plantillas por nombre")
    public ResponseEntity<List<PlantillaMenuResponse>> buscarPlantillasPorNombre(
            @RequestParam String nombrePlantilla) {

        return ResponseEntity.ok(
                plantillaMenuService.buscarPlantillasPorNombre(nombrePlantilla));
    }

    // Registra una nueva plantilla reutilizable.
    @PostMapping
    @Operation(summary = "Crear una plantilla")
    public ResponseEntity<PlantillaMenuResponse> crearPlantilla(
            @Valid @RequestBody PlantillaMenuRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(plantillaMenuService.crearPlantilla(request));
    }

    // Actualiza los datos de una plantilla existente.
    @PutMapping("/{idPlantilla}")
    @Operation(summary = "Actualizar una plantilla")
    public ResponseEntity<PlantillaMenuResponse> actualizarPlantilla(
            @PathVariable Integer idPlantilla,
            @Valid @RequestBody PlantillaMenuRequest request) {

        return ResponseEntity.ok(
                plantillaMenuService.actualizarPlantilla(idPlantilla, request));
    }

    // Permite activar o desactivar una plantilla.
    @PatchMapping("/{idPlantilla}/estado")
    @Operation(summary = "Cambiar estado de una plantilla")
    public ResponseEntity<PlantillaMenuResponse> cambiarEstadoPlantilla(
            @PathVariable Integer idPlantilla,
            @RequestParam boolean activo) {

        return ResponseEntity.ok(
                plantillaMenuService.cambiarEstadoPlantilla(
                        idPlantilla, activo));
    }

    // Realiza una eliminación lógica de la plantilla.
    @DeleteMapping("/{idPlantilla}")
    @Operation(summary = "Eliminar una plantilla")
    public ResponseEntity<Void> eliminarPlantilla(
            @PathVariable Integer idPlantilla) {

        plantillaMenuService.eliminarPlantilla(idPlantilla);

        return ResponseEntity.noContent().build();
    }

}
