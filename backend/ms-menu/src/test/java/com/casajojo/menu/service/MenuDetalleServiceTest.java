package com.casajojo.menu.service;

import com.casajojo.menu.dto.request.MenuDetalleRequest;
import com.casajojo.menu.dto.response.MenuDetalleResponse;
import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.entity.MenuDiario;
import com.casajojo.menu.entity.MenuDetalle;
import com.casajojo.menu.exception.BusinessException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.mapper.MenuDetalleMapper;
import com.casajojo.menu.repository.MenuDetalleRepository;
import com.casajojo.menu.repository.MenuDiarioRepository;
import com.casajojo.menu.repository.PlatoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MenuDetalleServiceTest {

    // Simula el acceso a las tres tablas relacionadas.
    @Mock
    private MenuDetalleRepository menuDetalleRepository;

    @Mock
    private MenuDiarioRepository menuDiarioRepository;

    @Mock
    private PlatoRepository platoRepository;

    // Utiliza el mapper real para comprobar la conversión a DTO.
    @Spy
    private MenuDetalleMapper menuDetalleMapper = new MenuDetalleMapper();

    @InjectMocks
    private MenuDetalleService menuDetalleService;

    private Categoria categoria;
    private Plato plato;
    private MenuDiario menu;
    private MenuDetalle detalle;
    private MenuDetalleRequest request;

    // Reinicia los datos antes de ejecutar cada prueba.
    @BeforeEach
    void setUp() {

        categoria = new Categoria(
                1,
                "PROTEINA",
                "Proteínas",
                true
        );

        plato = new Plato(
                1,
                categoria,
                "Carne Mechada",
                "Vacuno cocinado a fuego lento",
                4000,
                true
        );

        menu = new MenuDiario(
                1,
                LocalDate.of(2026, 11, 28),
                "BORRADOR"
        );

        detalle = new MenuDetalle(
                1,
                menu,
                plato,
                3500,
                true
        );

        request = new MenuDetalleRequest(
                1,
                1,
                3500,
                true
        );
    }

    // ==========================================
    // PRUEBAS DE LISTADO Y BÚSQUEDA
    // ==========================================

    @Test
    void listarDetalles_DebeRetornarDetalles() {

        when(menuDetalleRepository.findAll())
                .thenReturn(List.of(detalle));

        List<MenuDetalleResponse> resultado =
                menuDetalleService.listarDetalles();

        assertEquals(1, resultado.size());
        assertEquals("Carne Mechada", resultado.get(0).getNombrePlato());
        assertEquals(3500, resultado.get(0).getPrecioMenu());

        verify(menuDetalleRepository).findAll();
    }

    @Test
    void listarDetalles_DebeRetornarListaVacia() {

        when(menuDetalleRepository.findAll())
                .thenReturn(List.of());

        List<MenuDetalleResponse> resultado =
                menuDetalleService.listarDetalles();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarDetallePorId_DebeRetornarDetalle() {

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        MenuDetalleResponse resultado =
                menuDetalleService.buscarDetallePorId(1);

        assertEquals(1, resultado.getIdMenuDetalle());
        assertEquals(1, resultado.getIdMenu());
        assertEquals(1, resultado.getIdPlato());
        assertEquals(3500, resultado.getPrecioMenu());
    }

    @Test
    void buscarDetallePorId_DebeRechazarDetalleInexistente() {

        when(menuDetalleRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDetalleService.buscarDetallePorId(9999)
        );
    }

    @Test
    void listarDetallesPorMenu_DebeRetornarDetalles() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository.findByMenuDiario_IdMenu(1))
                .thenReturn(List.of(detalle));

        List<MenuDetalleResponse> resultado =
                menuDetalleService.listarDetallesPorMenu(1);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdMenu());
    }

    @Test
    void listarDetallesPorMenu_DebeRechazarMenuInexistente() {

        when(menuDiarioRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDetalleService.listarDetallesPorMenu(9999)
        );
    }

    // ==========================================
    // PRUEBAS DE DISPONIBILIDAD PARA CLIENTES
    // ==========================================

    @Test
    void listarDetallesDisponibles_DebeRetornarDetallesDisponibles() {

        menu.setEstado("PUBLICADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of(detalle));

        List<MenuDetalleResponse> resultado =
                menuDetalleService.listarDetallesDisponibles(1);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isDisponible());
        assertEquals(3500, resultado.get(0).getPrecioMenu());
    }

    @Test
    void listarDetallesDisponibles_DebeExcluirPlatosNoDisponibles() {

        menu.setEstado("PUBLICADO");
        plato.setDisponible(false);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of(detalle));

        List<MenuDetalleResponse> resultado =
                menuDetalleService.listarDetallesDisponibles(1);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarDetallesDisponibles_DebeExcluirCategoriasInactivas() {

        menu.setEstado("PUBLICADO");
        categoria.setActivo(false);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of(detalle));

        List<MenuDetalleResponse> resultado =
                menuDetalleService.listarDetallesDisponibles(1);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarDetallesDisponibles_DebeRechazarMenuEnBorrador() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.listarDetallesDisponibles(1)
        );
    }

    @Test
    void listarDetallesDisponibles_DebeRechazarMenuCerrado() {

        menu.setEstado("CERRADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.listarDetallesDisponibles(1)
        );
    }

    // ==========================================
    // PRUEBAS DE CREACIÓN
    // ==========================================

    @Test
    void crearDetalle_DebeGuardarDetalle() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(menuDetalleRepository.save(any(MenuDetalle.class)))
                .thenAnswer(invocation -> {
                    MenuDetalle nuevo = invocation.getArgument(0);
                    nuevo.setIdMenuDetalle(1);
                    return nuevo;
                });

        MenuDetalleResponse resultado =
                menuDetalleService.crearDetalle(request);

        assertEquals(1, resultado.getIdMenuDetalle());
        assertEquals(1, resultado.getIdMenu());
        assertEquals(1, resultado.getIdPlato());
        assertEquals(3500, resultado.getPrecioMenu());
        assertTrue(resultado.isDisponible());

        // El precio especial no modifica el catálogo original.
        assertEquals(4000, plato.getPrecioPlato());

        verify(menuDetalleRepository)
                .save(any(MenuDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarMenuInexistente() {

        request.setIdMenu(9999);

        when(menuDiarioRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDetalleService.crearDetalle(request)
        );

        verify(menuDetalleRepository, never())
                .save(any(MenuDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarPlatoInexistente() {

        request.setIdPlato(9999);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDetalleService.crearDetalle(request)
        );
    }

    @Test
    void crearDetalle_DebeRechazarMenuPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.crearDetalle(request)
        );

        verify(menuDetalleRepository, never())
                .save(any(MenuDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarMenuCerrado() {

        menu.setEstado("CERRADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.crearDetalle(request)
        );
    }

    @Test
    void crearDetalle_DebeRechazarPlatoNoDisponible() {

        plato.setDisponible(false);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.crearDetalle(request)
        );

        verify(menuDetalleRepository, never())
                .save(any(MenuDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarCategoriaInactiva() {

        categoria.setActivo(false);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.crearDetalle(request)
        );
    }

    @Test
    void crearDetalle_DebeRechazarPlatoDuplicado() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(menuDetalleRepository
                .existsByMenuDiario_IdMenuAndPlato_IdPlato(1, 1))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> menuDetalleService.crearDetalle(request)
        );

        verify(menuDetalleRepository, never())
                .save(any(MenuDetalle.class));
    }

    // ==========================================
    // PRUEBAS DE ACTUALIZACIÓN
    // ==========================================

    @Test
    void actualizarDetalle_DebeActualizarPrecio() {

        request.setPrecioMenu(3000);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(menuDetalleRepository.save(any(MenuDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDetalleResponse resultado =
                menuDetalleService.actualizarDetalle(1, request);

        assertEquals(1, resultado.getIdMenuDetalle());
        assertEquals(3000, resultado.getPrecioMenu());
        assertEquals(4000, plato.getPrecioPlato());

        verify(menuDetalleRepository).save(detalle);
    }

    @Test
    void actualizarDetalle_DebePermitirCambiarPlato() {

        Plato nuevoPlato = new Plato(
                2,
                categoria,
                "Pechuga a la Plancha",
                "Filete de pollo",
                3500,
                true
        );

        request.setIdPlato(2);
        request.setPrecioMenu(3200);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(2))
                .thenReturn(Optional.of(nuevoPlato));

        when(menuDetalleRepository.save(any(MenuDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDetalleResponse resultado =
                menuDetalleService.actualizarDetalle(1, request);

        assertEquals(2, resultado.getIdPlato());
        assertEquals("Pechuga a la Plancha", resultado.getNombrePlato());
        assertEquals(3200, resultado.getPrecioMenu());
    }

    @Test
    void actualizarDetalle_DebePermitirCambiarMenu() {

        MenuDiario nuevoMenu = new MenuDiario(
                2,
                menu.getFechaMenu().plusDays(1),
                "BORRADOR"
        );

        request.setIdMenu(2);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDiarioRepository.findById(2))
                .thenReturn(Optional.of(nuevoMenu));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(menuDetalleRepository.save(any(MenuDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDetalleResponse resultado =
                menuDetalleService.actualizarDetalle(1, request);

        assertEquals(2, resultado.getIdMenu());
        assertEquals(nuevoMenu.getFechaMenu(), resultado.getFechaMenu());
    }

    @Test
    void actualizarDetalle_DebeRechazarDetalleInexistente() {

        when(menuDetalleRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDetalleService.actualizarDetalle(9999, request)
        );
    }

    @Test
    void actualizarDetalle_DebeRechazarMenuOriginalPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.actualizarDetalle(1, request)
        );

        verify(menuDetalleRepository, never())
                .save(any(MenuDetalle.class));
    }

    @Test
    void actualizarDetalle_DebeRechazarMenuDestinoPublicado() {

        MenuDiario nuevoMenu = new MenuDiario(
                2,
                menu.getFechaMenu().plusDays(1),
                "PUBLICADO"
        );

        request.setIdMenu(2);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDiarioRepository.findById(2))
                .thenReturn(Optional.of(nuevoMenu));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.actualizarDetalle(1, request)
        );
    }

    @Test
    void actualizarDetalle_DebeRechazarPlatoDuplicado() {

        Plato nuevoPlato = new Plato(
                2,
                categoria,
                "Pechuga a la Plancha",
                "Filete de pollo",
                3500,
                true
        );

        request.setIdPlato(2);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(platoRepository.findById(2))
                .thenReturn(Optional.of(nuevoPlato));

        when(menuDetalleRepository
                .existsByMenuDiario_IdMenuAndPlato_IdPlato(1, 2))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> menuDetalleService.actualizarDetalle(1, request)
        );

        verify(menuDetalleRepository, never())
                .save(any(MenuDetalle.class));
    }

    // ==========================================
    // PRUEBAS DE CAMBIO DE DISPONIBILIDAD
    // ==========================================

    @Test
    void cambiarDisponibilidadDetalle_DebeDesactivarDetalle() {

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDetalleRepository.save(any(MenuDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDetalleResponse resultado =
                menuDetalleService.cambiarDisponibilidadDetalle(1, false);

        assertFalse(resultado.isDisponible());

        // Desactivar el detalle no desactiva el plato del catálogo.
        assertTrue(plato.isDisponible());

        verify(menuDetalleRepository).save(detalle);
    }

    @Test
    void cambiarDisponibilidadDetalle_DebeActivarDetalle() {

        detalle.setDisponible(false);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDetalleRepository.save(any(MenuDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDetalleResponse resultado =
                menuDetalleService.cambiarDisponibilidadDetalle(1, true);

        assertTrue(resultado.isDisponible());
    }

    @Test
    void cambiarDisponibilidadDetalle_DebePermitirMenuPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(menuDetalleRepository.save(any(MenuDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDetalleResponse resultado =
                menuDetalleService.cambiarDisponibilidadDetalle(1, false);

        assertFalse(resultado.isDisponible());
        assertEquals("PUBLICADO", menu.getEstado());
    }

    @Test
    void cambiarDisponibilidadDetalle_DebeRechazarMenuCerrado() {

        menu.setEstado("CERRADO");

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.cambiarDisponibilidadDetalle(1, false)
        );

        verify(menuDetalleRepository, never())
                .save(any(MenuDetalle.class));
    }

    @Test
    void cambiarDisponibilidadDetalle_DebeRechazarPlatoNoDisponible() {

        detalle.setDisponible(false);
        plato.setDisponible(false);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.cambiarDisponibilidadDetalle(1, true)
        );
    }

    @Test
    void cambiarDisponibilidadDetalle_DebeRechazarCategoriaInactiva() {

        detalle.setDisponible(false);
        categoria.setActivo(false);

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.cambiarDisponibilidadDetalle(1, true)
        );
    }

    // ==========================================
    // PRUEBAS DE ELIMINACIÓN
    // ==========================================

    @Test
    void eliminarDetalle_DebeEliminarDetalleEnBorrador() {

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        menuDetalleService.eliminarDetalle(1);

        // Elimina la asociación, no el plato original.
        verify(menuDetalleRepository).delete(detalle);
        verify(platoRepository, never()).delete(any(Plato.class));
        verify(menuDiarioRepository, never()).delete(any(MenuDiario.class));
    }

    @Test
    void eliminarDetalle_DebeRechazarMenuPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.eliminarDetalle(1)
        );

        verify(menuDetalleRepository, never())
                .delete(any(MenuDetalle.class));
    }

    @Test
    void eliminarDetalle_DebeRechazarMenuCerrado() {

        menu.setEstado("CERRADO");

        when(menuDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> menuDetalleService.eliminarDetalle(1)
        );
    }
}