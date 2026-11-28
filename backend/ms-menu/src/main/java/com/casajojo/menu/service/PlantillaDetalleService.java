package com.casajojo.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casajojo.menu.entity.PlantillaDetalle;
import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.repository.PlantillaDetalleRepository;
import com.casajojo.menu.repository.PlantillaMenuRepository;
import com.casajojo.menu.repository.PlatoRepository;
import com.casajojo.menu.mapper.PlantillaDetalleMapper;
import com.casajojo.menu.dto.request.PlantillaDetalleRequest;
import com.casajojo.menu.dto.response.PlantillaDetalleResponse;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantillaDetalleService {

    private final PlantillaDetalleRepository plantillaDetalleRepository;
    private final PlantillaMenuRepository plantillaMenuRepository;
    private final PlatoRepository platoRepository;
    private final PlantillaDetalleMapper plantillaDetalleMapper;

    // Lista todos los detalles de las plantillas.
    @Transactional(readOnly = true)
    public List<PlantillaDetalleResponse> listarDetalles() {

        return plantillaDetalleRepository.findAll()
                .stream()
                .map(plantillaDetalleMapper::toResponse)
                .toList();
    }

    // Busca un detalle mediante su identificador.
    @Transactional(readOnly = true)
    public PlantillaDetalleResponse buscarDetallePorId(
            Integer idPlantillaDetalle) {

        PlantillaDetalle detalle = obtenerDetalle(idPlantillaDetalle);

        return plantillaDetalleMapper.toResponse(detalle);
    }

    // Lista todos los platos asociados a una plantilla.
    @Transactional(readOnly = true)
    public List<PlantillaDetalleResponse> listarDetallesPorPlantilla(
            Integer idPlantilla) {

        obtenerPlantilla(idPlantilla);

        return plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(idPlantilla)
                .stream()
                .map(plantillaDetalleMapper::toResponse)
                .toList();
    }

    // Incorpora un plato evitando asociaciones duplicadas.
    public PlantillaDetalleResponse crearDetalle(
            PlantillaDetalleRequest request) {

        PlantillaMenu plantilla = obtenerPlantilla(
                request.getIdPlantilla());

        validarPlantillaActiva(plantilla);

        Plato plato = obtenerPlato(request.getIdPlato());

        validarPlatoDisponible(plato);

        if (plantillaDetalleRepository
                .existsByPlantillaMenu_IdPlantillaAndPlato_IdPlato(
                        request.getIdPlantilla(), request.getIdPlato())) {

            throw new ResourceConflictException(
                    "El plato ya se encuentra incorporado en esta plantilla");
        }

        PlantillaDetalle detalle = plantillaDetalleMapper.toEntity(
                request, plantilla, plato);

        detalle = plantillaDetalleRepository.save(detalle);

        return plantillaDetalleMapper.toResponse(detalle);
    }

    // Actualiza las asociaciones de un detalle existente.
    public PlantillaDetalleResponse actualizarDetalle(
            Integer idPlantillaDetalle, PlantillaDetalleRequest request) {

        PlantillaDetalle detalle = obtenerDetalle(idPlantillaDetalle);

        validarPlantillaActiva(detalle.getPlantillaMenu());

        PlantillaMenu plantilla = obtenerPlantilla(
                request.getIdPlantilla());

        validarPlantillaActiva(plantilla);

        Plato plato = obtenerPlato(request.getIdPlato());

        validarPlatoDisponible(plato);

        boolean cambioAsociacion = !detalle.getPlantillaMenu().getIdPlantilla()
                .equals(request.getIdPlantilla())
                || !detalle.getPlato().getIdPlato()
                        .equals(request.getIdPlato());

        if (cambioAsociacion
                && plantillaDetalleRepository
                        .existsByPlantillaMenu_IdPlantillaAndPlato_IdPlato(
                                request.getIdPlantilla(),
                                request.getIdPlato())) {

            throw new ResourceConflictException(
                    "El plato ya se encuentra incorporado en esta plantilla");
        }

        plantillaDetalleMapper.updateEntity(
                request, detalle, plantilla, plato);

        detalle = plantillaDetalleRepository.save(detalle);

        return plantillaDetalleMapper.toResponse(detalle);
    }

    // Elimina la asociación sin eliminar el plato del catálogo.
    public void eliminarDetalle(Integer idPlantillaDetalle) {

        PlantillaDetalle detalle = obtenerDetalle(idPlantillaDetalle);

        validarPlantillaActiva(detalle.getPlantillaMenu());

        plantillaDetalleRepository.delete(detalle);
    }

    // Valida la existencia del detalle.
    private PlantillaDetalle obtenerDetalle(Integer idPlantillaDetalle) {

        return plantillaDetalleRepository.findById(idPlantillaDetalle)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un detalle con ID " + idPlantillaDetalle));
    }

    // Valida la existencia de la plantilla.
    private PlantillaMenu obtenerPlantilla(Integer idPlantilla) {

        return plantillaMenuRepository.findById(idPlantilla)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una plantilla con ID " + idPlantilla));
    }

    // Valida la existencia del plato.
    private Plato obtenerPlato(Integer idPlato) {

        return platoRepository.findById(idPlato)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un plato con ID " + idPlato));
    }

    // Verifica que la plantilla esté habilitada.
    private void validarPlantillaActiva(PlantillaMenu plantilla) {

        if (!plantilla.isActivo()) {

            throw new BusinessException(
                    "La plantilla seleccionada se encuentra inactiva");
        }
    }

    // Verifica la disponibilidad del plato y su categoría.
    private void validarPlatoDisponible(Plato plato) {

        if (!plato.isDisponible()) {

            throw new BusinessException(
                    "El plato seleccionado no se encuentra disponible");
        }

        if (!plato.getCategoria().isActivo()) {

            throw new BusinessException(
                    "La categoría del plato se encuentra inactiva");
        }
    }

}
