package com.casajojo.menu.service;

import com.casajojo.menu.dto.request.CategoriaRequest;
import com.casajojo.menu.dto.response.CategoriaResponse;
import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.exception.ResourceNotFoundException;
import com.casajojo.menu.exception.ResourceConflictException;
import com.casajojo.menu.mapper.CategoriaMapper;
import com.casajojo.menu.repository.CategoriaRepository;

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
class CategoriaServiceTest {

    // Simula el acceso a la base de datos.
    @Mock
    private CategoriaRepository categoriaRepository;

    // Utiliza el mapper real.
    @Spy
    private CategoriaMapper categoriaMapper = new CategoriaMapper();

    // Inyecta las dependencias en el servicio.
    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaRequest request;

    // Prepara los datos antes de cada prueba.
    @BeforeEach
    void setUp() {

        categoria = new Categoria(
                1,
                "PROTEINA",
                "Proteínas",
                true
        );

        request = new CategoriaRequest(
                "PROTEINA",
                "Proteínas",
                true
        );
    }

    @Test
    void listarCategorias_DebeRetornarCategorias() {

        when(categoriaRepository.findAll())
                .thenReturn(List.of(categoria));

        List<CategoriaResponse> resultado =
                categoriaService.listarCategorias();

        assertEquals(1, resultado.size());
        assertEquals("PROTEINA", resultado.get(0).getCodigoCategoria());

        verify(categoriaRepository).findAll();
    }

    @Test
    void listarCategorias_DebeRetornarListaVacia() {

        when(categoriaRepository.findAll())
                .thenReturn(List.of());

        List<CategoriaResponse> resultado =
                categoriaService.listarCategorias();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarCategoriasActivas_DebeRetornarCategoriasActivas() {

        when(categoriaRepository.findByActivoTrue())
                .thenReturn(List.of(categoria));

        List<CategoriaResponse> resultado =
                categoriaService.listarCategoriasActivas();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActivo());
    }

    @Test
    void buscarCategoriaPorId_DebeRetornarCategoria() {

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        CategoriaResponse resultado =
                categoriaService.buscarCategoriaPorId(1);

        assertEquals(1, resultado.getIdCategoria());
        assertEquals("Proteínas", resultado.getNombreCategoria());
    }

    @Test
    void buscarCategoriaPorId_DebeLanzarExcepcionSiNoExiste() {

        when(categoriaRepository.findById(9999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.buscarCategoriaPorId(9999)
        );
    }

    @Test
    void crearCategoria_DebeGuardarCategoria() {

        when(categoriaRepository.existsByCodigoCategoriaIgnoreCase("PROTEINA"))
                .thenReturn(false);

        when(categoriaRepository.existsByNombreCategoriaIgnoreCase("Proteínas"))
                .thenReturn(false);

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoria);

        CategoriaResponse resultado =
                categoriaService.crearCategoria(request);

        assertEquals(1, resultado.getIdCategoria());
        assertEquals("PROTEINA", resultado.getCodigoCategoria());
        assertTrue(resultado.isActivo());

        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void crearCategoria_DebeRechazarCodigoDuplicado() {

        when(categoriaRepository.existsByCodigoCategoriaIgnoreCase("PROTEINA"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> categoriaService.crearCategoria(request)
        );

        // Comprueba que no se guarde un registro inválido.
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void crearCategoria_DebeRechazarNombreDuplicado() {

        when(categoriaRepository.existsByCodigoCategoriaIgnoreCase("PROTEINA"))
                .thenReturn(false);

        when(categoriaRepository.existsByNombreCategoriaIgnoreCase("Proteínas"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> categoriaService.crearCategoria(request)
        );

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoria_DebeActualizarDatos() {

        CategoriaRequest actualizacion = new CategoriaRequest(
                "PROTEINAS",
                "Proteínas actualizadas",
                true
        );

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByCodigoCategoriaIgnoreCase("PROTEINAS"))
                .thenReturn(false);

        when(categoriaRepository.existsByNombreCategoriaIgnoreCase("Proteínas actualizadas"))
                .thenReturn(false);

        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponse resultado =
                categoriaService.actualizarCategoria(1, actualizacion);

        assertEquals("PROTEINAS", resultado.getCodigoCategoria());
        assertEquals("Proteínas actualizadas", resultado.getNombreCategoria());

        verify(categoriaRepository).save(categoria);
    }

    @Test
    void actualizarCategoria_DebeRechazarCodigoDuplicado() {

        CategoriaRequest actualizacion = new CategoriaRequest(
                "POSTRE",
                "Proteínas",
                true
        );

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByCodigoCategoriaIgnoreCase("POSTRE"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> categoriaService.actualizarCategoria(1, actualizacion)
        );

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoria_DebeRechazarNombreDuplicado() {

        CategoriaRequest actualizacion = new CategoriaRequest(
                "PROTEINA",
                "Postres",
                true
        );

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.existsByNombreCategoriaIgnoreCase("Postres"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> categoriaService.actualizarCategoria(1, actualizacion)
        );

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoria_DebePermitirConservarDatosOriginales() {

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponse resultado =
                categoriaService.actualizarCategoria(1, request);

        assertEquals("PROTEINA", resultado.getCodigoCategoria());
        assertEquals("Proteínas", resultado.getNombreCategoria());
    }

    @Test
    void cambiarEstadoCategoria_DebeDesactivarCategoria() {

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponse resultado =
                categoriaService.cambiarEstadoCategoria(1, false);

        assertFalse(resultado.isActivo());

        verify(categoriaRepository).save(categoria);
    }

    @Test
    void cambiarEstadoCategoria_DebeActivarCategoria() {

        categoria.setActivo(false);

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CategoriaResponse resultado =
                categoriaService.cambiarEstadoCategoria(1, true);

        assertTrue(resultado.isActivo());
    }

    @Test
    void eliminarCategoria_DebeRealizarEliminacionLogica() {

        when(categoriaRepository.findById(1))
                .thenReturn(Optional.of(categoria));

        categoriaService.eliminarCategoria(1);

        assertFalse(categoria.isActivo());

        verify(categoriaRepository).save(categoria);

        // La eliminación no debe borrar físicamente el registro.
        verify(categoriaRepository, never()).delete(any(Categoria.class));
        verify(categoriaRepository, never()).deleteById(1);
    }
}