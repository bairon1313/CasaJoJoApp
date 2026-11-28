package com.casajojo.menu.service;

import com.casajojo.menu.dto.request.PlantillaMenuRequest;
import com.casajojo.menu.dto.response.PlantillaMenuResponse;
import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.mapper.PlantillaMenuMapper;
import com.casajojo.menu.repository.PlantillaMenuRepository;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PlantillaMenuServiceTest {

    // Simula las operaciones de acceso a la base de datos.
    @Mock
    private PlantillaMenuRepository plantillaMenuRepository;

    // Utiliza el mapper real para convertir entidades y DTO.
    @Spy
    private PlantillaMenuMapper plantillaMenuMapper =
            new PlantillaMenuMapper();

    @InjectMocks
    private PlantillaMenuService plantillaMenuService;

    private PlantillaMenu plantilla;
    private PlantillaMenuRequest request;

    // Prepara los datos antes de cada prueba.
    @BeforeEach
    void setUp() {

        plantilla = new PlantillaMenu(
                1,
                "Menú tradicional",
                true
        );

        request = new PlantillaMenuRequest(
                "Menú tradicional",
                true
        );
    }

    // ==========================================
    // PRUEBAS DE LISTADO Y BÚSQUEDA
    // ==========================================

    @Test
    void listarPlantillas_DebeRetornarPlantillas() {

        when(plantillaMenuRepository.findAll())
                .thenReturn(List.of(plantilla));

        List<PlantillaMenuResponse> resultado =
                plantillaMenuService.listarPlantillas();

        assertEquals(1, resultado.size());
        assertEquals("Menú tradicional",
                resultado.get(0).getNombrePlantilla());

        verify(plantillaMenuRepository).findAll();
    }

    @Test
    void listarPlantillas_DebeRetornarListaVacia() {

        when(plantillaMenuRepository.findAll())
                .thenReturn(List.of());

        List<PlantillaMenuResponse> resultado =
                plantillaMenuService.listarPlantillas();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPlantillasActivas_DebeRetornarPlantillasActivas() {

        when(plantillaMenuRepository.findByActivoTrue())
                .thenReturn(List.of(plantilla));

        List<PlantillaMenuResponse> resultado =
                plantillaMenuService.listarPlantillasActivas();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActivo());
    }

    @Test
    void listarPlantillasActivas_DebeRetornarListaVacia() {

        when(plantillaMenuRepository.findByActivoTrue())
                .thenReturn(List.of());

        List<PlantillaMenuResponse> resultado =
                plantillaMenuService.listarPlantillasActivas();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPlantillaPorId_DebeRetornarPlantilla() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        PlantillaMenuResponse resultado =
                plantillaMenuService.buscarPlantillaPorId(1);

        assertEquals(1, resultado.getIdPlantilla());
        assertEquals("Menú tradicional",
                resultado.getNombrePlantilla());
        assertTrue(resultado.isActivo());
    }

    @Test
    void buscarPlantillaPorId_DebeRechazarPlantillaInexistente() {

        when(plantillaMenuRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaMenuService.buscarPlantillaPorId(9999)
        );
    }

    @Test
    void buscarPlantillasPorNombre_DebeRetornarCoincidencias() {

        when(plantillaMenuRepository
                .findByNombrePlantillaContainingIgnoreCase("tradicional"))
                .thenReturn(List.of(plantilla));

        List<PlantillaMenuResponse> resultado =
                plantillaMenuService.buscarPlantillasPorNombre(
                        "tradicional");

        assertEquals(1, resultado.size());
        assertEquals("Menú tradicional",
                resultado.get(0).getNombrePlantilla());
    }

    @Test
    void buscarPlantillasPorNombre_DebeRetornarListaVacia() {

        when(plantillaMenuRepository
                .findByNombrePlantillaContainingIgnoreCase("inexistente"))
                .thenReturn(List.of());

        List<PlantillaMenuResponse> resultado =
                plantillaMenuService.buscarPlantillasPorNombre(
                        "inexistente");

        assertTrue(resultado.isEmpty());
    }

    // ==========================================
    // PRUEBAS DE CREACIÓN
    // ==========================================

    @Test
    void crearPlantilla_DebeGuardarPlantilla() {

        when(plantillaMenuRepository
                .existsByNombrePlantillaIgnoreCase("Menú tradicional"))
                .thenReturn(false);

        // Simula la asignación del ID generado por la base de datos.
        when(plantillaMenuRepository.save(any(PlantillaMenu.class)))
                .thenAnswer(invocation -> {
                    PlantillaMenu nueva = invocation.getArgument(0);
                    nueva.setIdPlantilla(1);
                    return nueva;
                });

        PlantillaMenuResponse resultado =
                plantillaMenuService.crearPlantilla(request);

        assertEquals(1, resultado.getIdPlantilla());
        assertEquals("Menú tradicional",
                resultado.getNombrePlantilla());
        assertTrue(resultado.isActivo());

        verify(plantillaMenuRepository)
                .save(any(PlantillaMenu.class));
    }

    @Test
    void crearPlantilla_DebeEliminarEspaciosDelNombre() {

        request.setNombrePlantilla("  Menú tradicional  ");

        when(plantillaMenuRepository
                .existsByNombrePlantillaIgnoreCase("Menú tradicional"))
                .thenReturn(false);

        when(plantillaMenuRepository.save(any(PlantillaMenu.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaMenuResponse resultado =
                plantillaMenuService.crearPlantilla(request);

        assertEquals("Menú tradicional",
                resultado.getNombrePlantilla());
    }

    @Test
    void crearPlantilla_DebeRechazarNombreDuplicado() {

        when(plantillaMenuRepository
                .existsByNombrePlantillaIgnoreCase("Menú tradicional"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> plantillaMenuService.crearPlantilla(request)
        );

        verify(plantillaMenuRepository, never())
                .save(any(PlantillaMenu.class));
    }

    // ==========================================
    // PRUEBAS DE ACTUALIZACIÓN
    // ==========================================

    @Test
    void actualizarPlantilla_DebeActualizarDatos() {

        request.setNombrePlantilla("Menú especial");
        request.setActivo(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaMenuRepository
                .existsByNombrePlantillaIgnoreCase("Menú especial"))
                .thenReturn(false);

        when(plantillaMenuRepository.save(any(PlantillaMenu.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaMenuResponse resultado =
                plantillaMenuService.actualizarPlantilla(1, request);

        assertEquals(1, resultado.getIdPlantilla());
        assertEquals("Menú especial",
                resultado.getNombrePlantilla());
        assertFalse(resultado.isActivo());

        verify(plantillaMenuRepository).save(plantilla);
    }

    @Test
    void actualizarPlantilla_DebePermitirConservarNombreOriginal() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaMenuRepository.save(any(PlantillaMenu.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaMenuResponse resultado =
                plantillaMenuService.actualizarPlantilla(1, request);

        assertEquals("Menú tradicional",
                resultado.getNombrePlantilla());

        verify(plantillaMenuRepository).save(plantilla);
    }

    @Test
    void actualizarPlantilla_DebeRechazarNombreDuplicado() {

        request.setNombrePlantilla("Menú vegetariano");

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaMenuRepository
                .existsByNombrePlantillaIgnoreCase("Menú vegetariano"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> plantillaMenuService.actualizarPlantilla(
                        1, request)
        );

        verify(plantillaMenuRepository, never())
                .save(any(PlantillaMenu.class));
    }

    @Test
    void actualizarPlantilla_DebeRechazarPlantillaInexistente() {

        when(plantillaMenuRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaMenuService.actualizarPlantilla(
                        9999, request)
        );
    }

    // ==========================================
    // PRUEBAS DE CAMBIO DE ESTADO
    // ==========================================

    @Test
    void cambiarEstadoPlantilla_DebeDesactivarPlantilla() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaMenuRepository.save(any(PlantillaMenu.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaMenuResponse resultado =
                plantillaMenuService.cambiarEstadoPlantilla(1, false);

        assertFalse(resultado.isActivo());

        verify(plantillaMenuRepository).save(plantilla);
    }

    @Test
    void cambiarEstadoPlantilla_DebeActivarPlantilla() {

        plantilla.setActivo(false);

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        when(plantillaMenuRepository.save(any(PlantillaMenu.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlantillaMenuResponse resultado =
                plantillaMenuService.cambiarEstadoPlantilla(1, true);

        assertTrue(resultado.isActivo());

        verify(plantillaMenuRepository).save(plantilla);
    }

    @Test
    void cambiarEstadoPlantilla_DebeRechazarPlantillaInexistente() {

        when(plantillaMenuRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaMenuService.cambiarEstadoPlantilla(
                        9999, false)
        );
    }

    // ==========================================
    // PRUEBAS DE ELIMINACIÓN LÓGICA
    // ==========================================

    @Test
    void eliminarPlantilla_DebeRealizarEliminacionLogica() {

        when(plantillaMenuRepository.findById(1))
                .thenReturn(Optional.of(plantilla));

        plantillaMenuService.eliminarPlantilla(1);

        assertFalse(plantilla.isActivo());

        verify(plantillaMenuRepository).save(plantilla);

        // Comprueba que no se elimine físicamente el registro.
        verify(plantillaMenuRepository, never())
                .delete(any(PlantillaMenu.class));

        verify(plantillaMenuRepository, never())
                .deleteById(1);
    }

    @Test
    void eliminarPlantilla_DebeRechazarPlantillaInexistente() {

        when(plantillaMenuRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> plantillaMenuService.eliminarPlantilla(9999)
        );
    }
}