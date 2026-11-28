package com.casajojo.menu.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casajojo.menu.entity.MenuDiario;
import com.casajojo.menu.entity.MenuDetalle;
import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.entity.PlantillaDetalle;
import com.casajojo.menu.entity.Plato;

import com.casajojo.menu.repository.MenuDiarioRepository;
import com.casajojo.menu.repository.MenuDetalleRepository;
import com.casajojo.menu.repository.PlantillaMenuRepository;
import com.casajojo.menu.repository.PlantillaDetalleRepository;

import com.casajojo.menu.mapper.MenuDiarioMapper;
import com.casajojo.menu.dto.request.MenuDiarioRequest;
import com.casajojo.menu.dto.response.MenuDiarioResponse;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuDiarioService {

    private final MenuDiarioRepository menuDiarioRepository;
    private final MenuDetalleRepository menuDetalleRepository;
    private final PlantillaMenuRepository plantillaMenuRepository;
    private final PlantillaDetalleRepository plantillaDetalleRepository;
    private final MenuDiarioMapper menuDiarioMapper;

    // Lista todos los menús ordenados desde el más reciente.
    @Transactional(readOnly = true)
    public List<MenuDiarioResponse> listarMenus() {

        return menuDiarioRepository.findAllByOrderByFechaMenuDesc()
                .stream()
                .map(menuDiarioMapper::toResponse)
                .toList();
    }

    // Busca un menú diario mediante su identificador.
    @Transactional(readOnly = true)
    public MenuDiarioResponse buscarMenuPorId(Integer idMenu) {

        MenuDiario menu = obtenerMenu(idMenu);

        return menuDiarioMapper.toResponse(menu);
    }

    // Busca un menú mediante su fecha.
    @Transactional(readOnly = true)
    public MenuDiarioResponse buscarMenuPorFecha(LocalDate fechaMenu) {

        MenuDiario menu = menuDiarioRepository.findByFechaMenu(fechaMenu)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un menú para la fecha " + fechaMenu));

        return menuDiarioMapper.toResponse(menu);
    }

    // Obtiene únicamente el menú publicado para la fecha solicitada.
    @Transactional(readOnly = true)
    public MenuDiarioResponse buscarMenuPublicado(LocalDate fechaMenu) {

        MenuDiario menu = menuDiarioRepository
                .findByFechaMenuAndEstado(fechaMenu, "PUBLICADO")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un menú publicado para la fecha " + fechaMenu));

        return menuDiarioMapper.toResponse(menu);
    }

    // Lista los menús registrados dentro de un rango de fechas.
    @Transactional(readOnly = true)
    public List<MenuDiarioResponse> listarMenusPorFechas(
            LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio.isAfter(fechaFin)) {

            throw new BusinessException(
                    "La fecha de inicio no puede ser posterior a la fecha final");
        }

        return menuDiarioRepository
                .findByFechaMenuBetween(fechaInicio, fechaFin)
                .stream()
                .map(menuDiarioMapper::toResponse)
                .toList();
    }

    // Crea un menú en estado BORRADOR evitando fechas duplicadas.
    public MenuDiarioResponse crearMenu(MenuDiarioRequest request) {

        if (menuDiarioRepository.existsByFechaMenu(
                request.getFechaMenu())) {

            throw new ResourceConflictException(
                    "Ya existe un menú para la fecha seleccionada");
        }

        MenuDiario menu = menuDiarioMapper.toEntity(request);

        menu = menuDiarioRepository.save(menu);

        return menuDiarioMapper.toResponse(menu);
    }

    // Genera un menú diario utilizando los platos de una plantilla.
    public MenuDiarioResponse crearMenuDesdePlantilla(
            Integer idPlantilla, MenuDiarioRequest request) {

        if (menuDiarioRepository.existsByFechaMenu(
                request.getFechaMenu())) {

            throw new ResourceConflictException(
                    "Ya existe un menú para la fecha seleccionada");
        }

        PlantillaMenu plantilla = plantillaMenuRepository
                .findById(idPlantilla)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una plantilla con ID " + idPlantilla));

        if (!plantilla.isActivo()) {

            throw new BusinessException(
                    "La plantilla seleccionada se encuentra inactiva");
        }

        // Recupera los platos asociados a la plantilla.
        List<PlantillaDetalle> detalles = plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(idPlantilla);

        if (detalles.isEmpty()) {

            throw new BusinessException(
                    "No se puede generar un menú desde una plantilla vacía");
        }

        // Verifica la disponibilidad de todos los platos.
        for (PlantillaDetalle detalle : detalles) {

            Plato plato = detalle.getPlato();

            if (!plato.isDisponible() || !plato.getCategoria().isActivo()) {

                throw new BusinessException(
                        "El plato " + plato.getNombrePlato()
                        + " no se encuentra disponible");
            }
        }

        // Crea el menú diario en estado BORRADOR.
        MenuDiario menu = menuDiarioMapper.toEntity(request);

        menu = menuDiarioRepository.save(menu);

        // Copia los platos utilizando los precios actuales del catálogo.
        for (PlantillaDetalle detalle : detalles) {

            Plato plato = detalle.getPlato();

            MenuDetalle menuDetalle = new MenuDetalle();

            menuDetalle.setMenuDiario(menu);
            menuDetalle.setPlato(plato);
            menuDetalle.setPrecioMenu(plato.getPrecioPlato());
            menuDetalle.setDisponible(true);

            menuDetalleRepository.save(menuDetalle);
        }

        return menuDiarioMapper.toResponse(menu);
    }

    // Permite modificar la fecha únicamente en estado BORRADOR.
    public MenuDiarioResponse actualizarMenu(Integer idMenu,
            MenuDiarioRequest request) {

        MenuDiario menu = obtenerMenu(idMenu);

        validarEstadoBorrador(menu);

        if (!menu.getFechaMenu().equals(request.getFechaMenu())
                && menuDiarioRepository.existsByFechaMenu(
                        request.getFechaMenu())) {

            throw new ResourceConflictException(
                    "Ya existe un menú para la fecha seleccionada");
        }

        menuDiarioMapper.updateEntity(request, menu);

        menu = menuDiarioRepository.save(menu);

        return menuDiarioMapper.toResponse(menu);
    }

    // Publica el menú si contiene al menos un plato disponible.
    public MenuDiarioResponse publicarMenu(Integer idMenu) {

        MenuDiario menu = obtenerMenu(idMenu);

        validarEstadoBorrador(menu);

        boolean tienePlatosDisponibles = menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(idMenu)
                .stream()
                .anyMatch(detalle -> detalle.getPlato().isDisponible()
                        && detalle.getPlato().getCategoria().isActivo());

        if (!tienePlatosDisponibles) {

            throw new BusinessException(
                    "No se puede publicar un menú sin platos disponibles");
        }

        menu.setEstado("PUBLICADO");

        menu = menuDiarioRepository.save(menu);

        return menuDiarioMapper.toResponse(menu);
    }

    // Cierra un menú previamente publicado.
    public MenuDiarioResponse cerrarMenu(Integer idMenu) {

        MenuDiario menu = obtenerMenu(idMenu);

        if (!"PUBLICADO".equals(menu.getEstado())) {

            throw new BusinessException(
                    "Solo se pueden cerrar menús publicados");
        }

        menu.setEstado("CERRADO");

        menu = menuDiarioRepository.save(menu);

        return menuDiarioMapper.toResponse(menu);
    }

    // Elimina únicamente menús que todavía están en borrador.
    public void eliminarMenu(Integer idMenu) {

        MenuDiario menu = obtenerMenu(idMenu);

        validarEstadoBorrador(menu);

        menuDiarioRepository.delete(menu);
    }

    // Valida la existencia del menú diario.
    private MenuDiario obtenerMenu(Integer idMenu) {

        return menuDiarioRepository.findById(idMenu)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un menú con ID " + idMenu));
    }

    // Restringe determinadas operaciones a menús en borrador.
    private void validarEstadoBorrador(MenuDiario menu) {

        if (!"BORRADOR".equals(menu.getEstado())) {

            throw new BusinessException(
                    "Esta operación solo se permite en menús en borrador");
        }
    }

}
