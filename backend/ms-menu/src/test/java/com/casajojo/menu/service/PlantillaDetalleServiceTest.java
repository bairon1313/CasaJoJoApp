package com.casajojo.menu.service;

import com.casajojo.menu.dto.request.PlantillaDetalleRequest;
import com.casajojo.menu.dto.response.PlantillaDetalleResponse;
import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.entity.PlantillaDetalle;
import com.casajojo.menu.exception.BusinessException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.mapper.PlantillaDetalleMapper;
import com.casajojo.menu.repository.PlantillaDetalleRepository;
import com.casajojo.menu.repository.PlantillaMenuRepository;
import com.casajojo.menu.repository.PlatoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

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
class PlantillaDetalleServiceTest {

    // Simula el acceso a las tablas relacionadas.
    @Mock
    private PlantillaDetalleRepository plantillaDetalleRepository;

    @Mock
    private PlantillaMenuRepository plantillaMenuRepository;

    @Mock
    private PlatoRepository platoRepository;

    // Utiliza el mapper real para comprobar la conversión de datos.
    @Spy
    private PlantillaDetalleMapper plantillaDetalleMapper =
            new PlantillaDetalleMapper();

    @InjectMocks
    private PlantillaDetalleService plantillaDetalleService;

    private Categoria categoria;
    private Plato plato;
    private PlantillaMenu plantilla;
    private PlantillaDetalle detalle;
    private PlantillaDetalleRequest request;

    // Reinicia los datos antes de cada prueba.
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

        plantilla = new PlantillaMenu(
                1,
                "Menú tradicional",
                true
        );

        detalle = new PlantillaDetalle(
                1,
                plantilla,
                plato
        );

        request = new PlantillaDetalleRequest(
                1,
                1
        );
    }

    // ==========================================
    // PRUEBAS DE LISTADO Y BÚSQUEDA
    // ==========================================

    @Test
    void listarDetalles_DebeRetornarDetalles() {

        when(plantillaDetalleRepository.findAll())
                .thenReturn(List.of(detalle));

        List<PlantillaDetalleResponse> resultado =
                plantillaDetalleService.listarDetalles();

        assertEquals(1, resultado.size());
        assertEquals("Carne Mechada",
                resultado.get(0).getNombrePlato());
        assertEquals("Menú tradicional",
                resultado.get(0).getNombrePlantilla());

        verify(plantillaDetalleRepository).findAll();
    }

    @Test
    void listarDetalles_DebeRetornarListaVacia() {

        when(plantillaDetalleRepository.findAll())
                .thenReturn(List.of());

        List<PlantillaDetalleResponse> resultado =
                plantillaDetalleService.listarDetalles();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarDetallePorId_DebeRetornarDetalle() {

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        PlantillaDetalleResponse resultado =
                plantillaDetalleService.buscarDetallePorId(1);

        assertEquals(1, resultado.getIdPlantillaDetalle());
        assertEquals(1, resultado.getIdPlantilla());
        assertEquals(1, resultado.getIdPlato());
        assertEquals(4000, resultado.getPrecioPlato());
    }

    @Test
    void buscarDetallePorId_DebeRechazarDetalleInexistente() {

        when(plantillaDetalleRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaDetalleService.buscarDetallePorId(9999)
        );
    }

    @Test
    void listarDetallesPorPlantilla_DebeRetornarDetalles() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(1))
                .thenReturn(List.of(detalle));

        List<PlantillaDetalleResponse> resultado =
                plantillaDetalleService.listarDetallesPorPlantilla(1);

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdPlantilla());
        assertEquals("Carne Mechada",
                resultado.get(0).getNombrePlato());
    }

    @Test
    void listarDetallesPorPlantilla_DebeRetornarListaVacia() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaDetalleRepository
                .findByPlantillaMenu_IdPlantilla(1))
                .thenReturn(List.of());

        List<PlantillaDetalleResponse> resultado =
                plantillaDetalleService.listarDetallesPorPlantilla(1);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarDetallesPorPlantilla_DebeRechazarPlantillaInexistente() {

        when(plantillaMenuRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaDetalleService
                        .listarDetallesPorPlantilla(9999)
        );
    }

    // ==========================================
    // PRUEBAS DE CREACIÓN
    // ==========================================

    @Test
    void crearDetalle_DebeGuardarDetalle() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        // Simula la generación automática del identificador.
        when(plantillaDetalleRepository
                .save(any(PlantillaDetalle.class)))
                .thenAnswer(invocation -> {
                    PlantillaDetalle nuevo = invocation.getArgument(0);
                    nuevo.setIdPlantillaDetalle(1);
                    return nuevo;
                });

        PlantillaDetalleResponse resultado =
                plantillaDetalleService.crearDetalle(request);

        assertEquals(1, resultado.getIdPlantillaDetalle());
        assertEquals(1, resultado.getIdPlantilla());
        assertEquals(1, resultado.getIdPlato());
        assertEquals("Carne Mechada", resultado.getNombrePlato());
        assertEquals(4000, resultado.getPrecioPlato());

        verify(plantillaDetalleRepository)
                .save(any(PlantillaDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarPlantillaInexistente() {

        request.setIdPlantilla(9999);

        when(plantillaMenuRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaDetalleService.crearDetalle(request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarPlantillaInactiva() {

        plantilla.setActivo(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        assertThrows(
                BusinessException.class,
                () -> plantillaDetalleService.crearDetalle(request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarPlatoInexistente() {

        request.setIdPlato(9999);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaDetalleService.crearDetalle(request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarPlatoNoDisponible() {

        plato.setDisponible(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        assertThrows(
                BusinessException.class,
                () -> plantillaDetalleService.crearDetalle(request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarCategoriaInactiva() {

        categoria.setActivo(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        assertThrows(
                BusinessException.class,
                () -> plantillaDetalleService.crearDetalle(request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    @Test
    void crearDetalle_DebeRechazarPlatoDuplicado() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(plantillaDetalleRepository
                .existsByPlantillaMenu_IdPlantillaAndPlato_IdPlato(1, 1))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> plantillaDetalleService.crearDetalle(request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    // ==========================================
    // PRUEBAS DE ACTUALIZACIÓN
    // ==========================================

    @Test
    void actualizarDetalle_DebePermitirConservarDatosOriginales() {

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(plantillaDetalleRepository
                .save(any(PlantillaDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaDetalleResponse resultado =
                plantillaDetalleService.actualizarDetalle(1, request);

        assertEquals(1, resultado.getIdPlantilla());
        assertEquals(1, resultado.getIdPlato());

        verify(plantillaDetalleRepository).save(detalle);
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

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(2))
                .thenReturn(Optional.of(nuevoPlato));

        when(plantillaDetalleRepository
                .save(any(PlantillaDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaDetalleResponse resultado =
                plantillaDetalleService.actualizarDetalle(1, request);

        assertEquals(1, resultado.getIdPlantillaDetalle());
        assertEquals(2, resultado.getIdPlato());
        assertEquals("Pechuga a la Plancha", resultado.getNombrePlato());
        assertEquals(3500, resultado.getPrecioPlato());

        verify(plantillaDetalleRepository).save(detalle);
    }

    @Test
    void actualizarDetalle_DebePermitirCambiarPlantilla() {

        PlantillaMenu nuevaPlantilla = new PlantillaMenu(
                2,
                "Menú especial",
                true
        );

        request.setIdPlantilla(2);

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(plantillaMenuRepository.findById(2))
                .thenReturn(Optional.of(nuevaPlantilla));

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(plantillaDetalleRepository
                .save(any(PlantillaDetalle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaDetalleResponse resultado =
                plantillaDetalleService.actualizarDetalle(1, request);

        assertEquals(2, resultado.getIdPlantilla());
        assertEquals("Menú especial", resultado.getNombrePlantilla());
        assertEquals(1, resultado.getIdPlato());
    }

    @Test
    void actualizarDetalle_DebeRechazarDetalleInexistente() {

        when(plantillaDetalleRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaDetalleService.actualizarDetalle(
                        9999, request)
        );
    }

    @Test
    void actualizarDetalle_DebeRechazarPlantillaOriginalInactiva() {

        plantilla.setActivo(false);

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> plantillaDetalleService.actualizarDetalle(1, request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    @Test
    void actualizarDetalle_DebeRechazarPlantillaDestinoInactiva() {

        PlantillaMenu nuevaPlantilla = new PlantillaMenu(
                2,
                "Menú especial",
                false
        );

        request.setIdPlantilla(2);

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(plantillaMenuRepository.findById(2))
                .thenReturn(Optional.of(nuevaPlantilla));

        assertThrows(
                BusinessException.class,
                () -> plantillaDetalleService.actualizarDetalle(1, request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
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

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(platoRepository.findById(2))
                .thenReturn(Optional.of(nuevoPlato));

        when(plantillaDetalleRepository
                .existsByPlantillaMenu_IdPlantillaAndPlato_IdPlato(1, 2))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> plantillaDetalleService.actualizarDetalle(1, request)
        );

        verify(plantillaDetalleRepository, never())
                .save(any(PlantillaDetalle.class));
    }

    // ==========================================
    // PRUEBAS DE ELIMINACIÓN
    // ==========================================

    @Test
    void eliminarDetalle_DebeEliminarAsociacion() {

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        plantillaDetalleService.eliminarDetalle(1);

        // Elimina el detalle sin borrar la plantilla ni el plato.
        verify(plantillaDetalleRepository).delete(detalle);

        verify(plantillaMenuRepository, never())
                .delete(any(PlantillaMenu.class));

        verify(platoRepository, never())
                .delete(any(Plato.class));
    }

    @Test
    void eliminarDetalle_DebeRechazarPlantillaInactiva() {

        plantilla.setActivo(false);

        when(plantillaDetalleRepository.findById(1))
                .thenReturn(Optional.of(detalle));

        assertThrows(
                BusinessException.class,
                () -> plantillaDetalleService.eliminarDetalle(1)
        );

        verify(plantillaDetalleRepository, never())
                .delete(any(PlantillaDetalle.class));
    }

    @Test
    void eliminarDetalle_DebeRechazarDetalleInexistente() {

        when(plantillaDetalleRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaDetalleService.eliminarDetalle(9999)
        );
    }
}