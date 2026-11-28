package com.casajojo.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casajojo.menu.entity.MenuDetalle;
import com.casajojo.menu.entity.MenuDiario;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.repository.MenuDetalleRepository;
import com.casajojo.menu.repository.MenuDiarioRepository;
import com.casajojo.menu.repository.PlatoRepository;
import com.casajojo.menu.mapper.MenuDetalleMapper;
import com.casajojo.menu.dto.request.MenuDetalleRequest;
import com.casajojo.menu.dto.response.MenuDetalleResponse;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuDetalleService {

    private final MenuDetalleRepository menuDetalleRepository;
    private final MenuDiarioRepository menuDiarioRepository;
    private final PlatoRepository platoRepository;
    private final MenuDetalleMapper menuDetalleMapper;

    // Lista todos los detalles registrados.
    @Transactional(readOnly = true)
    public List<MenuDetalleResponse> listarDetalles() {

        return menuDetalleRepository.findAll()
                .stream()
                .map(menuDetalleMapper::toResponse)
                .toList();
    }

    // Busca un detalle mediante su identificador.
    @Transactional(readOnly = true)
    public MenuDetalleResponse buscarDetallePorId(Integer idMenuDetalle) {

        MenuDetalle detalle = obtenerDetalle(idMenuDetalle);

        return menuDetalleMapper.toResponse(detalle);
    }

    // Lista todos los platos asociados a un menú diario.
    @Transactional(readOnly = true)
    public List<MenuDetalleResponse> listarDetallesPorMenu(Integer idMenu) {

        obtenerMenu(idMenu);

        return menuDetalleRepository.findByMenuDiario_IdMenu(idMenu)
                .stream()
                .map(menuDetalleMapper::toResponse)
                .toList();
    }

    // Lista únicamente los platos disponibles de un menú publicado.
    @Transactional(readOnly = true)
    public List<MenuDetalleResponse> listarDetallesDisponibles(Integer idMenu) {

        MenuDiario menu = obtenerMenu(idMenu);

        if (!"PUBLICADO".equals(menu.getEstado())) {

            throw new BusinessException(
                    "El menú seleccionado no se encuentra publicado");
        }

        return menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(idMenu)
                .stream()
                .filter(detalle -> detalle.getPlato().isDisponible())
                .filter(detalle -> detalle.getPlato().getCategoria().isActivo())
                .map(menuDetalleMapper::toResponse)
                .toList();
    }

    // Incorpora un plato a un menú evitando asociaciones duplicadas.
    public MenuDetalleResponse crearDetalle(MenuDetalleRequest request) {

        MenuDiario menu = obtenerMenu(request.getIdMenu());

        validarEstadoBorrador(menu);

        Plato plato = obtenerPlato(request.getIdPlato());

        validarPlatoDisponible(plato);

        if (menuDetalleRepository.existsByMenuDiario_IdMenuAndPlato_IdPlato(
                request.getIdMenu(), request.getIdPlato())) {

            throw new ResourceConflictException(
                    "El plato ya se encuentra incorporado en este menú");
        }

        MenuDetalle detalle = menuDetalleMapper.toEntity(
                request, menu, plato);

        detalle = menuDetalleRepository.save(detalle);

        return menuDetalleMapper.toResponse(detalle);
    }

    // Actualiza un detalle validando el menú y el plato asociado.
    public MenuDetalleResponse actualizarDetalle(Integer idMenuDetalle,
            MenuDetalleRequest request) {

        MenuDetalle detalle = obtenerDetalle(idMenuDetalle);

        validarEstadoBorrador(detalle.getMenuDiario());

        MenuDiario menu = obtenerMenu(request.getIdMenu());

        validarEstadoBorrador(menu);

        Plato plato = obtenerPlato(request.getIdPlato());

        validarPlatoDisponible(plato);

        boolean cambioAsociacion = !detalle.getMenuDiario().getIdMenu().equals(request.getIdMenu())
                || !detalle.getPlato().getIdPlato().equals(request.getIdPlato());

        if (cambioAsociacion
                && menuDetalleRepository
                        .existsByMenuDiario_IdMenuAndPlato_IdPlato(
                                request.getIdMenu(), request.getIdPlato())) {

            throw new ResourceConflictException(
                    "El plato ya se encuentra incorporado en este menú");
        }

        menuDetalleMapper.updateEntity(request, detalle, menu, plato);

        detalle = menuDetalleRepository.save(detalle);

        return menuDetalleMapper.toResponse(detalle);
    }

    // Permite habilitar o deshabilitar platos durante la jornada.
    public MenuDetalleResponse cambiarDisponibilidadDetalle(
            Integer idMenuDetalle, boolean disponible) {

        MenuDetalle detalle = obtenerDetalle(idMenuDetalle);

        if ("CERRADO".equals(detalle.getMenuDiario().getEstado())) {

            throw new BusinessException(
                    "No se puede modificar un menú cerrado");
        }

        if (disponible) {

            validarPlatoDisponible(detalle.getPlato());
        }

        detalle.setDisponible(disponible);

        detalle = menuDetalleRepository.save(detalle);

        return menuDetalleMapper.toResponse(detalle);
    }

    // Elimina la asociación únicamente si el menú está en borrador.
    public void eliminarDetalle(Integer idMenuDetalle) {

        MenuDetalle detalle = obtenerDetalle(idMenuDetalle);

        validarEstadoBorrador(detalle.getMenuDiario());

        menuDetalleRepository.delete(detalle);
    }

    // Valida la existencia del detalle.
    private MenuDetalle obtenerDetalle(Integer idMenuDetalle) {

        return menuDetalleRepository.findById(idMenuDetalle)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un detalle con ID " + idMenuDetalle));
    }

    // Valida la existencia del menú diario.
    private MenuDiario obtenerMenu(Integer idMenu) {

        return menuDiarioRepository.findById(idMenu)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un menú con ID " + idMenu));
    }

    // Valida la existencia del plato.
    private Plato obtenerPlato(Integer idPlato) {

        return platoRepository.findById(idPlato)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un plato con ID " + idPlato));
    }

    // Verifica la disponibilidad general del plato y su categoría.
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

    // Restringe las modificaciones estructurales a menús en borrador.
    private void validarEstadoBorrador(MenuDiario menu) {

        if (!"BORRADOR".equals(menu.getEstado())) {

            throw new BusinessException(
                    "Esta operación solo se permite en menús en borrador");
        }
    }

}
