package com.casajojo.menu.mapper;

import org.springframework.stereotype.Component;

import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.dto.request.PlatoRequest;
import com.casajojo.menu.dto.response.PlatoResponse;

@Component
public class PlatoMapper {

    // Convierte el Request en una nueva entidad con su categoría asociada.
    public Plato toEntity(PlatoRequest request, Categoria categoria) {

        Plato plato = new Plato();

        plato.setCategoria(categoria);
        plato.setNombrePlato(request.getNombrePlato());
        plato.setDescripcion(request.getDescripcion() != null
                ? request.getDescripcion() : "");
        plato.setPrecioPlato(request.getPrecioPlato());
        plato.setDisponible(request.getDisponible());

        return plato;
    }

    // Convierte la entidad en una respuesta para Android.
    public PlatoResponse toResponse(Plato plato) {

        PlatoResponse response = new PlatoResponse();

        response.setIdPlato(plato.getIdPlato());
        response.setNombrePlato(plato.getNombrePlato());
        response.setDescripcion(plato.getDescripcion());
        response.setPrecioPlato(plato.getPrecioPlato());
        response.setDisponible(plato.isDisponible());

        // Obtiene los datos de la categoría relacionada.
        response.setIdCategoria(plato.getCategoria().getIdCategoria());
        response.setCodigoCategoria(plato.getCategoria().getCodigoCategoria());
        response.setNombreCategoria(plato.getCategoria().getNombreCategoria());

        return response;
    }

    // Actualiza los datos del plato conservando su identificador.
    public void updateEntity(PlatoRequest request, Plato plato,
            Categoria categoria) {

        plato.setCategoria(categoria);
        plato.setNombrePlato(request.getNombrePlato());
        plato.setDescripcion(request.getDescripcion() != null
                ? request.getDescripcion() : "");
        plato.setPrecioPlato(request.getPrecioPlato());
        plato.setDisponible(request.getDisponible());

    }

}
