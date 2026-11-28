package com.casajojo.menu.service;

import com.casajojo.menu.dto.request.PlatoRequest;
import com.casajojo.menu.dto.response.PlatoResponse;
import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.exception.BusinessException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.mapper.PlatoMapper;
import com.casajojo.menu.repository.CategoriaRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PlatoServiceTest {

    // Simula los repositories sin utilizar Supabase.
    @Mock
    private PlatoRepository platoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Spy
    private PlatoMapper platoMapper = new PlatoMapper();

    @InjectMocks
    private PlatoService platoService;

    private Categoria categoria;
    private Plato plato;
    private PlatoRequest request;

    // Prepara los datos antes de cada prueba.
    @BeforeEach
    void setUp() {

        categoria = new Categoria(
                1,
                "PROTEINA",
                "Proteínas",
                true);

        plato = new Plato(
                1,
                categoria,
                "Carne Mechada",
                "Vacuno cocinado a fuego lento",
                4000,
                true);

        request = new PlatoRequest(
                1,
                "Carne Mechada",
                "Vacuno cocinado a fuego lento",
                4000,
                true);
    }

    // Pruebas de listado y búsqueda.

    @Test
    void listarPlatos_DebeRetornarPlatos() {

        when(platoRepository.findAll())
                .thenReturn(List.of(plato));

        List<PlatoResponse> resultado = platoService.listarPlatos();

        assertEquals(1, resultado.size());
        assertEquals("Carne Mechada", resultado.get(0).getNombrePlato());
        assertEquals("PROTEINA", resultado.get(0).getCodigoCategoria());

        verify(platoRepository).findAll();
    }

    @Test
    void listarPlatos_DebeRetornarListaVacia() {

        when(platoRepository.findAll())
                .thenReturn(List.of());

        List<PlatoResponse> resultado = platoService.listarPlatos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarPlatosDisponibles_DebeRetornarPlatosDisponibles() {

        when(platoRepository.findByDisponibleTrue())
                .thenReturn(List.of(plato));

        List<PlatoResponse> resultado = platoService.listarPlatosDisponibles();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isDisponible());
    }

    @Test
    void listarPlatosDisponibles_DebeExcluirCategoriasInactivas() {

        categoria.setActivo(false);

        when(platoRepository.findByDisponibleTrue())
                .thenReturn(List.of(plato));

        List<PlatoResponse> resultado = platoService.listarPlatosDisponibles();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPlatoPorId_DebeRetornarPlato() {

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        PlatoResponse resultado = platoService.buscarPlatoPorId(1);

        assertEquals(1, resultado.getIdPlato());
        assertEquals("Carne Mechada", resultado.getNombrePlato());
        assertEquals(4000, resultado.getPrecioPlato());
    }

    @Test
    void buscarPlatoPorId_DebeLanzarExcepcionSiNoExiste() {

        when(platoRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> platoService.buscarPlatoPorId(9999));
    }

    @Test
    void listarPlatosPorCategoria_DebeRetornarPlatos() {

        when(platoRepository.findByCategoria_CodigoCategoria("PROTEINA"))
                .thenReturn(List.of(plato));

        List<PlatoResponse> resultado = platoService.listarPlatosPorCategoria("proteina");

        assertEquals(1, resultado.size());
        assertEquals("PROTEINA", resultado.get(0).getCodigoCategoria());
    }

    @Test
    void buscarPlatosPorNombre_DebeRetornarCoincidencias() {

        when(platoRepository.findByNombrePlatoContainingIgnoreCase("carne"))
                .thenReturn(List.of(plato));

        List<PlatoResponse> resultado = platoService.buscarPlatosPorNombre("carne");

        assertEquals(1, resultado.size());
        assertEquals("Carne Mechada", resultado.get(0).getNombrePlato());
    }

    // Pruebas de creación de platos.

    @Test
    void crearPlato_DebeGuardarPlato() {

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(platoRepository.existsByNombrePlatoIgnoreCase("Carne Mechada"))
                .thenReturn(false);

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocation -> {
                    Plato nuevo = invocation.getArgument(0);
                    nuevo.setIdPlato(1);
                    return nuevo;
                });

        PlatoResponse resultado = platoService.crearPlato(request);

        assertEquals(1, resultado.getIdPlato());
        assertEquals("Carne Mechada", resultado.getNombrePlato());
        assertEquals(4000, resultado.getPrecioPlato());
        assertEquals(1, resultado.getIdCategoria());
        assertTrue(resultado.isDisponible());

        verify(platoRepository).save(any(Plato.class));
    }

    @Test
    void crearPlato_DebeRechazarNombreDuplicado() {

        when(platoRepository.existsByNombrePlatoIgnoreCase("Carne Mechada"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> platoService.crearPlato(request));

        verify(platoRepository, never()).save(any(Plato.class));
    }

    @Test
    void crearPlato_DebeRechazarCategoriaInexistente() {

        request.setIdCategoria(9999);

        when(categoriaRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> platoService.crearPlato(request));

        verify(platoRepository, never()).save(any(Plato.class));
    }

    @Test
    void crearPlato_DebeRechazarCategoriaInactiva() {

        categoria.setActivo(false);

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        assertThrows(
                BusinessException.class,
                () -> platoService.crearPlato(request));

        verify(platoRepository, never()).save(any(Plato.class));
    }

    @Test
    void crearPlato_DebePermitirDescripcionNula() {

        request.setDescripcion(null);

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlatoResponse resultado = platoService.crearPlato(request);

        assertEquals("", resultado.getDescripcion());
    }

    // Pruebas de actualización de platos.

    @Test
    void actualizarPlato_DebeActualizarDatosYCategoria() {

        Categoria nuevaCategoria = new Categoria(
                2,
                "FONDO",
                "Platos de fondo",
                true);

        PlatoRequest actualizacion = new PlatoRequest(
                2,
                "Pastel de Choclo",
                "Pastel tradicional",
                4500,
                true);

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(categoriaRepository.findById(2))
                .thenReturn(Optional.of(nuevaCategoria));

        when(platoRepository.existsByNombrePlatoIgnoreCase("Pastel de Choclo"))
                .thenReturn(false);

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlatoResponse resultado = platoService.actualizarPlato(1, actualizacion);

        assertEquals(1, resultado.getIdPlato());
        assertEquals("Pastel de Choclo", resultado.getNombrePlato());
        assertEquals(4500, resultado.getPrecioPlato());
        assertEquals(2, resultado.getIdCategoria());
        assertEquals("FONDO", resultado.getCodigoCategoria());

        verify(platoRepository).save(plato);
    }

    @Test
    void actualizarPlato_DebePermitirConservarNombreOriginal() {

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlatoResponse resultado = platoService.actualizarPlato(1, request);

        assertEquals("Carne Mechada", resultado.getNombrePlato());

        verify(platoRepository).save(plato);
    }

    @Test
    void actualizarPlato_DebeRechazarNombreDuplicado() {

        request.setNombrePlato("Papas Fritas");

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(platoRepository.existsByNombrePlatoIgnoreCase("Papas Fritas"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> platoService.actualizarPlato(1, request));

        verify(platoRepository, never()).save(any(Plato.class));
    }

    @Test
    void actualizarPlato_DebeRechazarPlatoInexistente() {

        when(platoRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> platoService.actualizarPlato(9999, request));
    }

    // Pruebas de disponibilidad.

    @Test
    void cambiarDisponibilidadPlato_DebeDesactivarPlato() {

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlatoResponse resultado = platoService.cambiarDisponibilidadPlato(1, false);

        assertFalse(resultado.isDisponible());

        verify(platoRepository).save(plato);
    }

    @Test
    void cambiarDisponibilidadPlato_DebeActivarPlato() {

        plato.setDisponible(false);

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlatoResponse resultado = platoService.cambiarDisponibilidadPlato(1, true);

        assertTrue(resultado.isDisponible());
    }

    @Test
    void cambiarDisponibilidadPlato_DebeRechazarCategoriaInactiva() {

        plato.setDisponible(false);
        categoria.setActivo(false);

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        assertThrows(
                BusinessException.class,
                () -> platoService.cambiarDisponibilidadPlato(1, true));

        verify(platoRepository, never()).save(any(Plato.class));
    }

    // Comprueba que la eliminación sea lógica y no física.
    @Test
    void eliminarPlato_DebeRealizarEliminacionLogica() {

        when(platoRepository.findById(1))
                .thenReturn(Optional.of(plato));

        platoService.eliminarPlato(1);

        assertFalse(plato.isDisponible());

        verify(platoRepository).save(plato);
        verify(platoRepository, never()).delete(any(Plato.class));
        verify(platoRepository, never()).deleteById(1);
    }
}