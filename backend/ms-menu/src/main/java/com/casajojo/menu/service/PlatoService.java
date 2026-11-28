
package com.casajojo.menu.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.repository.PlatoRepository;
import com.casajojo.menu.repository.CategoriaRepository;
import com.casajojo.menu.mapper.PlatoMapper;
import com.casajojo.menu.dto.request.PlatoRequest;
import com.casajojo.menu.dto.response.PlatoResponse;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PlatoService {

    private final PlatoRepository platoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PlatoMapper platoMapper;

    // Lista todos los platos registrados en el catálogo.
    @Transactional(readOnly = true)
    public List<PlatoResponse> listarPlatos() {

        return platoRepository.findAll()
                .stream()
                .map(platoMapper::toResponse)
                .toList();
    }

    // Lista platos disponibles que pertenecen a categorías activas.
    @Transactional(readOnly = true)
    public List<PlatoResponse> listarPlatosDisponibles() {

        return platoRepository.findByDisponibleTrue()
                .stream()
                .filter(plato -> plato.getCategoria().isActivo())
                .map(platoMapper::toResponse)
                .toList();
    }

    // Busca un plato mediante su identificador.
    @Transactional(readOnly = true)
    public PlatoResponse buscarPlatoPorId(Integer idPlato) {

        Plato plato = obtenerPlato(idPlato);

        return platoMapper.toResponse(plato);
    }

    // Lista los platos asociados a una categoría.
    @Transactional(readOnly = true)
    public List<PlatoResponse> listarPlatosPorCategoria(
            String codigoCategoria) {

        String codigo = codigoCategoria.trim()
                .toUpperCase(Locale.ROOT);

        return platoRepository.findByCategoria_CodigoCategoria(codigo)
                .stream()
                .map(platoMapper::toResponse)
                .toList();
    }

    // Busca platos por coincidencia parcial de su nombre.
    @Transactional(readOnly = true)
    public List<PlatoResponse> buscarPlatosPorNombre(String nombrePlato) {

        return platoRepository
                .findByNombrePlatoContainingIgnoreCase(nombrePlato.trim())
                .stream()
                .map(platoMapper::toResponse)
                .toList();
    }

    // Registra un plato validando su nombre y categoría.
    public PlatoResponse crearPlato(PlatoRequest request) {

        normalizarDatos(request);

        if (platoRepository.existsByNombrePlatoIgnoreCase(
                request.getNombrePlato())) {

            throw new ResourceConflictException(
                    "Ya existe un plato con ese nombre");
        }

        Categoria categoria = obtenerCategoriaActiva(
                request.getIdCategoria());

        Plato plato = platoMapper.toEntity(request, categoria);

        plato = platoRepository.save(plato);

        return platoMapper.toResponse(plato);
    }

    // Actualiza un plato conservando su identificador.
    public PlatoResponse actualizarPlato(Integer idPlato,
            PlatoRequest request) {

        Plato plato = obtenerPlato(idPlato);

        normalizarDatos(request);

        if (!plato.getNombrePlato().equalsIgnoreCase(
                request.getNombrePlato())
                && platoRepository.existsByNombrePlatoIgnoreCase(
                        request.getNombrePlato())) {

            throw new ResourceConflictException(
                    "Ya existe un plato con ese nombre");
        }

        Categoria categoria = obtenerCategoriaActiva(
                request.getIdCategoria());

        platoMapper.updateEntity(request, plato, categoria);

        plato = platoRepository.save(plato);

        return platoMapper.toResponse(plato);
    }

    // Modifica la disponibilidad general del plato.
    public PlatoResponse cambiarDisponibilidadPlato(
            Integer idPlato, boolean disponible) {

        Plato plato = obtenerPlato(idPlato);

        if (disponible && !plato.getCategoria().isActivo()) {

            throw new BusinessException(
                    "No se puede habilitar un plato de una categoría inactiva");
        }

        plato.setDisponible(disponible);

        plato = platoRepository.save(plato);

        return platoMapper.toResponse(plato);
    }

    // Eliminación lógica para conservar las relaciones existentes.
    public void eliminarPlato(Integer idPlato) {

        Plato plato = obtenerPlato(idPlato);

        plato.setDisponible(false);

        platoRepository.save(plato);
    }

    // Valida que el plato exista en la base de datos.
    private Plato obtenerPlato(Integer idPlato) {

        return platoRepository.findById(idPlato)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un plato con ID " + idPlato));
    }

    // Valida la existencia y el estado de la categoría.
    private Categoria obtenerCategoriaActiva(Integer idCategoria) {

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una categoría con ID " + idCategoria));

        if (!categoria.isActivo()) {

            throw new BusinessException(
                    "La categoría seleccionada se encuentra inactiva");
        }

        return categoria;
    }

    // Elimina espacios innecesarios de los datos recibidos.
    private void normalizarDatos(PlatoRequest request) {

        request.setNombrePlato(request.getNombrePlato().trim());

        if (request.getDescripcion() != null) {

            request.setDescripcion(request.getDescripcion().trim());
        }
    }

}
