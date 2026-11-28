package com.casajojo.menu.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.casajojo.menu.service.MenuDiarioService;
import com.casajojo.menu.dto.request.MenuDiarioRequest;
import com.casajojo.menu.dto.response.MenuDiarioResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
@Tag(name = "Menús diarios", description = "Administración de los menús diarios")
public class MenuDiarioController {

    private final MenuDiarioService menuDiarioService;

    // Obtiene todos los menús diarios registrados.
    @GetMapping
    @Operation(summary = "Listar todos los menús")
    public ResponseEntity<List<MenuDiarioResponse>> listarMenus() {

        return ResponseEntity.ok(menuDiarioService.listarMenus());
    }

    // Busca un menú mediante su identificador.
    @GetMapping("/{idMenu}")
    @Operation(summary = "Buscar menú por ID")
    public ResponseEntity<MenuDiarioResponse> buscarMenuPorId(
            @PathVariable Integer idMenu) {

        return ResponseEntity.ok(
                menuDiarioService.buscarMenuPorId(idMenu));
    }

    // Busca un menú diario para una fecha específica.
    @GetMapping("/fecha")
    @Operation(summary = "Buscar menú por fecha")
    public ResponseEntity<MenuDiarioResponse> buscarMenuPorFecha(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaMenu) {

        return ResponseEntity.ok(
                menuDiarioService.buscarMenuPorFecha(fechaMenu));
    }

    // Obtiene el menú publicado que podrán consultar los clientes.
    @GetMapping("/publicado")
    @Operation(summary = "Buscar menú publicado por fecha")
    public ResponseEntity<MenuDiarioResponse> buscarMenuPublicado(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaMenu) {

        return ResponseEntity.ok(
                menuDiarioService.buscarMenuPublicado(fechaMenu));
    }

    // Consulta los menús registrados dentro de un rango de fechas.
    @GetMapping("/rango")
    @Operation(summary = "Listar menús por rango de fechas")
    public ResponseEntity<List<MenuDiarioResponse>> listarMenusPorFechas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin) {

        return ResponseEntity.ok(
                menuDiarioService.listarMenusPorFechas(
                        fechaInicio, fechaFin));
    }

    // Registra un nuevo menú en estado BORRADOR.
    @PostMapping
    @Operation(summary = "Crear un menú diario")
    public ResponseEntity<MenuDiarioResponse> crearMenu(
            @Valid @RequestBody MenuDiarioRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuDiarioService.crearMenu(request));
    }

    // Genera un menú diario utilizando una plantilla existente.
    @PostMapping("/plantilla/{idPlantilla}")
    @Operation(summary = "Crear un menú desde una plantilla")
    public ResponseEntity<MenuDiarioResponse> crearMenuDesdePlantilla(
            @PathVariable Integer idPlantilla,
            @Valid @RequestBody MenuDiarioRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuDiarioService.crearMenuDesdePlantilla(
                        idPlantilla, request));
    }

    // Actualiza la fecha de un menú existente.
    @PutMapping("/{idMenu}")
    @Operation(summary = "Actualizar un menú diario")
    public ResponseEntity<MenuDiarioResponse> actualizarMenu(
            @PathVariable Integer idMenu,
            @Valid @RequestBody MenuDiarioRequest request) {

        return ResponseEntity.ok(
                menuDiarioService.actualizarMenu(idMenu, request));
    }

    // Cambia el estado del menú de BORRADOR a PUBLICADO.
    @PatchMapping("/{idMenu}/publicar")
    @Operation(summary = "Publicar un menú diario")
    public ResponseEntity<MenuDiarioResponse> publicarMenu(
            @PathVariable Integer idMenu) {

        return ResponseEntity.ok(
                menuDiarioService.publicarMenu(idMenu));
    }

    // Cambia el estado del menú de PUBLICADO a CERRADO.
    @PatchMapping("/{idMenu}/cerrar")
    @Operation(summary = "Cerrar un menú diario")
    public ResponseEntity<MenuDiarioResponse> cerrarMenu(
            @PathVariable Integer idMenu) {

        return ResponseEntity.ok(
                menuDiarioService.cerrarMenu(idMenu));
    }

    // Elimina un menú diario que se encuentra en borrador.
    @DeleteMapping("/{idMenu}")
    @Operation(summary = "Eliminar un menú diario")
    public ResponseEntity<Void> eliminarMenu(
            @PathVariable Integer idMenu) {

        menuDiarioService.eliminarMenu(idMenu);

        return ResponseEntity.noContent().build();
    }

}
