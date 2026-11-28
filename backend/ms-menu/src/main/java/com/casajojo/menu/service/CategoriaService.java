
package com.casajojo.menu.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.repository.CategoriaRepository;
import com.casajojo.menu.mapper.CategoriaMapper;
import com.casajojo.menu.dto.request.CategoriaRequest;
import com.casajojo.menu.dto.response.CategoriaResponse;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.exception.ResourceConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    // Lista todas las categorías registradas.
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategorias() {

        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    // Lista únicamente las categorías activas.
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategoriasActivas() {

        return categoriaRepository.findByActivoTrue()
                .stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    // Busca una categoría mediante su identificador.
    @Transactional(readOnly = true)
    public CategoriaResponse buscarCategoriaPorId(Integer idCategoria) {

        Categoria categoria = obtenerCategoria(idCategoria);

        return categoriaMapper.toResponse(categoria);
    }

    // Registra una categoría validando que no esté duplicada.
    public CategoriaResponse crearCategoria(CategoriaRequest request) {

        normalizarDatos(request);

        if (categoriaRepository.existsByCodigoCategoriaIgnoreCase(
                request.getCodigoCategoria())) {

            throw new ResourceConflictException(
                    "El código de la categoría ya existe");
        }

        if (categoriaRepository.existsByNombreCategoriaIgnoreCase(
                request.getNombreCategoria())) {

            throw new ResourceConflictException(
                    "El nombre de la categoría ya existe");
        }

        Categoria categoria = categoriaMapper.toEntity(request);

        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.toResponse(categoria);
    }

    // Actualiza una categoría evitando conflictos con otros registros.
    public CategoriaResponse actualizarCategoria(Integer idCategoria,
            CategoriaRequest request) {

        Categoria categoria = obtenerCategoria(idCategoria);

        normalizarDatos(request);

        if (!categoria.getCodigoCategoria().equalsIgnoreCase(
                request.getCodigoCategoria())
                && categoriaRepository.existsByCodigoCategoriaIgnoreCase(
                        request.getCodigoCategoria())) {

            throw new ResourceConflictException(
                    "El código de la categoría ya existe");
        }

        if (!categoria.getNombreCategoria().equalsIgnoreCase(
                request.getNombreCategoria())
                && categoriaRepository.existsByNombreCategoriaIgnoreCase(
                        request.getNombreCategoria())) {

            throw new ResourceConflictException(
                    "El nombre de la categoría ya existe");
        }

        categoriaMapper.updateEntity(request, categoria);

        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.toResponse(categoria);
    }

    // Permite activar o desactivar una categoría.
    public CategoriaResponse cambiarEstadoCategoria(Integer idCategoria,
            boolean activo) {

        Categoria categoria = obtenerCategoria(idCategoria);

        categoria.setActivo(activo);

        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.toResponse(categoria);
    }

    // Eliminación lógica para conservar las relaciones existentes.
    public void eliminarCategoria(Integer idCategoria) {

        Categoria categoria = obtenerCategoria(idCategoria);

        categoria.setActivo(false);

        categoriaRepository.save(categoria);
    }

    // Centraliza la búsqueda y validación de existencia.
    private Categoria obtenerCategoria(Integer idCategoria) {

        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una categoría con ID " + idCategoria));
    }

    // Elimina espacios innecesarios y estandariza el código.
    private void normalizarDatos(CategoriaRequest request) {

        request.setCodigoCategoria(
                request.getCodigoCategoria().trim().toUpperCase(Locale.ROOT));

        request.setNombreCategoria(
                request.getNombreCategoria().trim());
    }

}
