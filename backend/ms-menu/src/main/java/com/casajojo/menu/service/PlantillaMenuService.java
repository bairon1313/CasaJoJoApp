package com.casajojo.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.repository.PlantillaMenuRepository;
import com.casajojo.menu.mapper.PlantillaMenuMapper;
import com.casajojo.menu.dto.request.PlantillaMenuRequest;
import com.casajojo.menu.dto.response.PlantillaMenuResponse;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.exception.ResourceConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantillaMenuService {

    private final PlantillaMenuRepository plantillaMenuRepository;
    private final PlantillaMenuMapper plantillaMenuMapper;

    // Lista todas las plantillas registradas.
    @Transactional(readOnly = true)
    public List<PlantillaMenuResponse> listarPlantillas() {

        return plantillaMenuRepository.findAll()
                .stream()
                .map(plantillaMenuMapper::toResponse)
                .toList();
    }

    // Lista únicamente las plantillas activas.
    @Transactional(readOnly = true)
    public List<PlantillaMenuResponse> listarPlantillasActivas() {

        return plantillaMenuRepository.findByActivoTrue()
                .stream()
                .map(plantillaMenuMapper::toResponse)
                .toList();
    }

    // Busca una plantilla mediante su identificador.
    @Transactional(readOnly = true)
    public PlantillaMenuResponse buscarPlantillaPorId(Integer idPlantilla) {

        PlantillaMenu plantilla = obtenerPlantilla(idPlantilla);

        return plantillaMenuMapper.toResponse(plantilla);
    }

    // Busca plantillas por coincidencia parcial de su nombre.
    @Transactional(readOnly = true)
    public List<PlantillaMenuResponse> buscarPlantillasPorNombre(
            String nombrePlantilla) {

        return plantillaMenuRepository
                .findByNombrePlantillaContainingIgnoreCase(
                        nombrePlantilla.trim())
                .stream()
                .map(plantillaMenuMapper::toResponse)
                .toList();
    }

    // Registra una plantilla evitando nombres duplicados.
    public PlantillaMenuResponse crearPlantilla(
            PlantillaMenuRequest request) {

        normalizarDatos(request);

        if (plantillaMenuRepository.existsByNombrePlantillaIgnoreCase(
                request.getNombrePlantilla())) {

            throw new ResourceConflictException(
                    "Ya existe una plantilla con ese nombre");
        }

        PlantillaMenu plantilla = plantillaMenuMapper.toEntity(request);

        plantilla = plantillaMenuRepository.save(plantilla);

        return plantillaMenuMapper.toResponse(plantilla);
    }

    // Actualiza una plantilla evitando conflictos con otros registros.
    public PlantillaMenuResponse actualizarPlantilla(Integer idPlantilla,
            PlantillaMenuRequest request) {

        PlantillaMenu plantilla = obtenerPlantilla(idPlantilla);

        normalizarDatos(request);

        if (!plantilla.getNombrePlantilla().equalsIgnoreCase(
                request.getNombrePlantilla())
                && plantillaMenuRepository.existsByNombrePlantillaIgnoreCase(
                        request.getNombrePlantilla())) {

            throw new ResourceConflictException(
                    "Ya existe una plantilla con ese nombre");
        }

        plantillaMenuMapper.updateEntity(request, plantilla);

        plantilla = plantillaMenuRepository.save(plantilla);

        return plantillaMenuMapper.toResponse(plantilla);
    }

    // Permite activar o desactivar una plantilla.
    public PlantillaMenuResponse cambiarEstadoPlantilla(
            Integer idPlantilla, boolean activo) {

        PlantillaMenu plantilla = obtenerPlantilla(idPlantilla);

        plantilla.setActivo(activo);

        plantilla = plantillaMenuRepository.save(plantilla);

        return plantillaMenuMapper.toResponse(plantilla);
    }

    // Eliminación lógica para conservar los platos asociados.
    public void eliminarPlantilla(Integer idPlantilla) {

        PlantillaMenu plantilla = obtenerPlantilla(idPlantilla);

        plantilla.setActivo(false);

        plantillaMenuRepository.save(plantilla);
    }

    // Valida la existencia de la plantilla.
    private PlantillaMenu obtenerPlantilla(Integer idPlantilla) {

        return plantillaMenuRepository.findById(idPlantilla)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una plantilla con ID " + idPlantilla));
    }

    // Elimina espacios innecesarios del nombre.
    private void normalizarDatos(PlantillaMenuRequest request) {

        request.setNombrePlantilla(
                request.getNombrePlantilla().trim());
    }

}
