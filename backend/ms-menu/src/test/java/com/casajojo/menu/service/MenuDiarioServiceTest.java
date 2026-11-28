package com.casajojo.menu.service;

import com.casajojo.menu.dto.request.MenuDiarioRequest;
import com.casajojo.menu.dto.response.MenuDiarioResponse;
import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.entity.MenuDiario;
import com.casajojo.menu.entity.MenuDetalle;
import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.entity.PlantillaDetalle;
import com.casajojo.menu.exception.BusinessException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.mapper.MenuDiarioMapper;
import com.casajojo.menu.repository.MenuDiarioRepository;
import com.casajojo.menu.repository.MenuDetalleRepository;
import com.casajojo.menu.repository.PlantillaMenuRepository;
import com.casajojo.menu.repository.PlantillaDetalleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MenuDiarioServiceTest {

    // Simula el acceso a las cuatro tablas relacionadas.
    @Mock
    private MenuDiarioRepository menuDiarioRepository;

    @Mock
    private MenuDetalleRepository menuDetalleRepository;

    @Mock
    private PlantillaMenuRepository plantillaMenuRepository;

    @Mock
    private PlantillaDetalleRepository plantillaDetalleRepository;

    @Spy
    private MenuDiarioMapper menuDiarioMapper = new MenuDiarioMapper();

    @InjectMocks
    private MenuDiarioService menuDiarioService;

    private MenuDiario menu;
    private MenuDiarioRequest request;
    private Categoria categoria;
    private Plato plato;
    private MenuDetalle menuDetalle;
    private PlantillaMenu plantilla;
    private PlantillaDetalle plantillaDetalle;
    private LocalDate fecha;

    // Prepara datos independientes antes de cada prueba.
    @BeforeEach
    void setUp() {

        fecha = LocalDate.now();

        menu = new MenuDiario(
                1,
                fecha,
                "BORRADOR"
        );

        request = new MenuDiarioRequest(fecha);

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

        menuDetalle = new MenuDetalle(
                1,
                menu,
                plato,
                3500,
                true
        );

        plantilla = new PlantillaMenu(
                1,
                "Plantilla de prueba",
                true
        );

        plantillaDetalle = new PlantillaDetalle(
                1,
                plantilla,
                plato
        );
    }

    // ==========================================
    // PRUEBAS DE LISTADO Y BÚSQUEDA
    // ==========================================

    @Test
    void listarMenus_DebeRetornarMenus() {

        when(menuDiarioRepository.findAllByOrderByFechaMenuDesc())
                .thenReturn(List.of(menu));

        List<MenuDiarioResponse> resultado =
                menuDiarioService.listarMenus();

        assertEquals(1, resultado.size());
        assertEquals(fecha, resultado.get(0).getFechaMenu());

        verify(menuDiarioRepository)
                .findAllByOrderByFechaMenuDesc();
    }

    @Test
    void listarMenus_DebeRetornarListaVacia() {

        when(menuDiarioRepository.findAllByOrderByFechaMenuDesc())
                .thenReturn(List.of());

        List<MenuDiarioResponse> resultado =
                menuDiarioService.listarMenus();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarMenuPorId_DebeRetornarMenu() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        MenuDiarioResponse resultado =
                menuDiarioService.buscarMenuPorId(1);

        assertEquals(1, resultado.getIdMenu());
        assertEquals(fecha, resultado.getFechaMenu());
        assertEquals("BORRADOR", resultado.getEstado());
    }

    @Test
    void buscarMenuPorId_DebeRechazarMenuInexistente() {

        when(menuDiarioRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDiarioService.buscarMenuPorId(9999)
        );
    }

    @Test
    void buscarMenuPorFecha_DebeRetornarMenu() {

        when(menuDiarioRepository.findByFechaMenu(fecha))
                .thenReturn(Optional.of(menu));

        MenuDiarioResponse resultado =
                menuDiarioService.buscarMenuPorFecha(fecha);

        assertEquals(fecha, resultado.getFechaMenu());
    }

    @Test
    void buscarMenuPorFecha_DebeRechazarFechaInexistente() {

        when(menuDiarioRepository.findByFechaMenu(fecha))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDiarioService.buscarMenuPorFecha(fecha)
        );
    }

    @Test
    void buscarMenuPublicado_DebeRetornarMenuPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDiarioRepository.findByFechaMenuAndEstado(
                fecha, "PUBLICADO"))
                .thenReturn(Optional.of(menu));

        MenuDiarioResponse resultado =
                menuDiarioService.buscarMenuPublicado(fecha);

        assertEquals("PUBLICADO", resultado.getEstado());
    }

    @Test
    void buscarMenuPublicado_DebeRechazarMenuInexistente() {

        when(menuDiarioRepository.findByFechaMenuAndEstado(
                fecha, "PUBLICADO"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDiarioService.buscarMenuPublicado(fecha)
        );
    }

    @Test
    void listarMenusPorFechas_DebeRetornarMenus() {

        LocalDate fechaInicio = fecha.minusDays(1);
        LocalDate fechaFin = fecha.plusDays(1);

        when(menuDiarioRepository.findByFechaMenuBetween(
                fechaInicio, fechaFin))
                .thenReturn(List.of(menu));

        List<MenuDiarioResponse> resultado =
                menuDiarioService.listarMenusPorFechas(
                        fechaInicio, fechaFin);

        assertEquals(1, resultado.size());
        assertEquals(fecha, resultado.get(0).getFechaMenu());
    }

    @Test
    void listarMenusPorFechas_DebeRechazarRangoInvalido() {

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.listarMenusPorFechas(
                        fecha.plusDays(1), fecha)
        );

        verify(menuDiarioRepository, never())
                .findByFechaMenuBetween(any(), any());
    }

    // ==========================================
    // PRUEBAS DE CREACIÓN
    // ==========================================

    @Test
    void crearMenu_DebeGuardarMenuEnBorrador() {

        when(menuDiarioRepository.existsByFechaMenu(fecha))
                .thenReturn(false);

        when(menuDiarioRepository.save(any(MenuDiario.class)))
                .thenAnswer(invocation -> {
                    MenuDiario nuevo = invocation.getArgument(0);
                    nuevo.setIdMenu(1);
                    return nuevo;
                });

        MenuDiarioResponse resultado =
                menuDiarioService.crearMenu(request);

        assertEquals(1, resultado.getIdMenu());
        assertEquals(fecha, resultado.getFechaMenu());
        assertEquals("BORRADOR", resultado.getEstado());

        verify(menuDiarioRepository).save(any(MenuDiario.class));
    }

    @Test
    void crearMenu_DebeRechazarFechaDuplicada() {

        when(menuDiarioRepository.existsByFechaMenu(fecha))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> menuDiarioService.crearMenu(request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    // ==========================================
    // PRUEBAS DE ACTUALIZACIÓN
    // ==========================================

    @Test
    void actualizarMenu_DebeActualizarFecha() {

        LocalDate nuevaFecha = fecha.plusDays(1);
        request.setFechaMenu(nuevaFecha);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDiarioRepository.existsByFechaMenu(nuevaFecha))
                .thenReturn(false);

        when(menuDiarioRepository.save(any(MenuDiario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDiarioResponse resultado =
                menuDiarioService.actualizarMenu(1, request);

        assertEquals(nuevaFecha, resultado.getFechaMenu());
        assertEquals("BORRADOR", resultado.getEstado());

        verify(menuDiarioRepository).save(menu);
    }

    @Test
    void actualizarMenu_DebePermitirConservarFechaOriginal() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDiarioRepository.save(any(MenuDiario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDiarioResponse resultado =
                menuDiarioService.actualizarMenu(1, request);

        assertEquals(fecha, resultado.getFechaMenu());
    }

    @Test
    void actualizarMenu_DebeRechazarFechaDuplicada() {

        LocalDate nuevaFecha = fecha.plusDays(1);
        request.setFechaMenu(nuevaFecha);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDiarioRepository.existsByFechaMenu(nuevaFecha))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> menuDiarioService.actualizarMenu(1, request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void actualizarMenu_DebeRechazarMenuPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.actualizarMenu(1, request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void actualizarMenu_DebeRechazarMenuCerrado() {

        menu.setEstado("CERRADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.actualizarMenu(1, request)
        );
    }

    // ==========================================
    // PRUEBAS DE PUBLICACIÓN
    // ==========================================

    @Test
    void publicarMenu_DebeCambiarEstadoAPublicado() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of(menuDetalle));

        when(menuDiarioRepository.save(any(MenuDiario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDiarioResponse resultado =
                menuDiarioService.publicarMenu(1);

        assertEquals("PUBLICADO", resultado.getEstado());

        verify(menuDiarioRepository).save(menu);
    }

    @Test
    void publicarMenu_DebeRechazarMenuSinPlatos() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of());

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.publicarMenu(1)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void publicarMenu_DebeRechazarPlatosNoDisponibles() {

        plato.setDisponible(false);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of(menuDetalle));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.publicarMenu(1)
        );
    }

    @Test
    void publicarMenu_DebeRechazarCategoriasInactivas() {

        categoria.setActivo(false);

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDetalleRepository
                .findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of(menuDetalle));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.publicarMenu(1)
        );
    }

    @Test
    void publicarMenu_DebeRechazarMenuYaPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.publicarMenu(1)
        );
    }

    @Test
    void publicarMenu_DebeRechazarMenuCerrado() {

        menu.setEstado("CERRADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.publicarMenu(1)
        );
    }

    // ==========================================
    // PRUEBAS DE CIERRE
    // ==========================================

    @Test
    void cerrarMenu_DebeCambiarEstadoACerrado() {

        menu.setEstado("PUBLICADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        when(menuDiarioRepository.save(any(MenuDiario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuDiarioResponse resultado =
                menuDiarioService.cerrarMenu(1);

        assertEquals("CERRADO", resultado.getEstado());

        verify(menuDiarioRepository).save(menu);
    }

    @Test
    void cerrarMenu_DebeRechazarMenuEnBorrador() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.cerrarMenu(1)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void cerrarMenu_DebeRechazarMenuYaCerrado() {

        menu.setEstado("CERRADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.cerrarMenu(1)
        );
    }

    // ==========================================
    // PRUEBAS DE ELIMINACIÓN
    // ==========================================

    @Test
    void eliminarMenu_DebeEliminarMenuEnBorrador() {

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        menuDiarioService.eliminarMenu(1);

        verify(menuDiarioRepository).delete(menu);
    }

    @Test
    void eliminarMenu_DebeRechazarMenuPublicado() {

        menu.setEstado("PUBLICADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.eliminarMenu(1)
        );

        verify(menuDiarioRepository, never())
                .delete(any(MenuDiario.class));
    }

    @Test
    void eliminarMenu_DebeRechazarMenuCerrado() {

        menu.setEstado("CERRADO");

        when(menuDiarioRepository.findById(1))
                .thenReturn(Optional.of(menu));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.eliminarMenu(1)
        );
    }

    // ==========================================
    // PRUEBAS DE GENERACIÓN DESDE PLANTILLAS
    // ==========================================

    @Test
    void crearMenuDesdePlantilla_DebeGenerarMenuYDetalles() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(1))
                .thenReturn(List.of(plantillaDetalle));

        when(menuDiarioRepository.save(any(MenuDiario.class)))
                .thenAnswer(invocation -> {
                    MenuDiario nuevo = invocation.getArgument(0);
                    nuevo.setIdMenu(2);
                    return nuevo;
                });

        MenuDiarioResponse resultado =
                menuDiarioService.crearMenuDesdePlantilla(1, request);

        assertEquals(2, resultado.getIdMenu());
        assertEquals("BORRADOR", resultado.getEstado());
        assertEquals(fecha, resultado.getFechaMenu());

        // Captura el detalle para comprobar los datos copiados.
        ArgumentCaptor<MenuDetalle> captor =
                ArgumentCaptor.forClass(MenuDetalle.class);

        verify(menuDetalleRepository).save(captor.capture());

        MenuDetalle detalleGenerado = captor.getValue();

        assertEquals(2, detalleGenerado.getMenuDiario().getIdMenu());
        assertEquals(1, detalleGenerado.getPlato().getIdPlato());
        assertEquals(4000, detalleGenerado.getPrecioMenu());
        assertTrue(detalleGenerado.isDisponible());
    }

    @Test
    void crearMenuDesdePlantilla_DebeRechazarFechaDuplicada() {

        when(menuDiarioRepository.existsByFechaMenu(fecha))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> menuDiarioService.crearMenuDesdePlantilla(
                        1, request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void crearMenuDesdePlantilla_DebeRechazarPlantillaInexistente() {

        when(plantillaMenuRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> menuDiarioService.crearMenuDesdePlantilla(
                        9999, request)
        );
    }

    @Test
    void crearMenuDesdePlantilla_DebeRechazarPlantillaInactiva() {

        plantilla.setActivo(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.crearMenuDesdePlantilla(
                        1, request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void crearMenuDesdePlantilla_DebeRechazarPlantillaVacia() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(1))
                .thenReturn(List.of());

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.crearMenuDesdePlantilla(
                        1, request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void crearMenuDesdePlantilla_DebeRechazarPlatoNoDisponible() {

        plato.setDisponible(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(1))
                .thenReturn(List.of(plantillaDetalle));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.crearMenuDesdePlantilla(
                        1, request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }

    @Test
    void crearMenuDesdePlantilla_DebeRechazarCategoriaInactiva() {

        categoria.setActivo(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(1))
                .thenReturn(List.of(plantillaDetalle));

        assertThrows(
                BusinessException.class,
                () -> menuDiarioService.crearMenuDesdePlantilla(
                        1, request)
        );

        verify(menuDiarioRepository, never())
                .save(any(MenuDiario.class));
    }
}