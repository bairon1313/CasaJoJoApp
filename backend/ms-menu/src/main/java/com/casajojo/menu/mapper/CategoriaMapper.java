
package com.casajojo.menu.mapper;

import org.springframework.stereotype.Component;

import com.casajojo.menu.entity.Categoria;
import com.casajojo.menu.dto.request.CategoriaRequest;
import com.casajojo.menu.dto.response.CategoriaResponse;

@Component
public class CategoriaMapper {

    // Convierte los datos del Request en una nueva entidad.
    public Categoria toEntity(CategoriaRequest request) {

        Categoria categoria = new Categoria();

        categoria.setCodigoCategoria(request.getCodigoCategoria());
        categoria.setNombreCategoria(request.getNombreCategoria());
        categoria.setActivo(request.getActivo());

        return categoria;
    }

    // Convierte la entidad en una respuesta para el cliente.
    public CategoriaResponse toResponse(Categoria categoria) {

        CategoriaResponse response = new CategoriaResponse();

        response.setIdCategoria(categoria.getIdCategoria());
        response.setCodigoCategoria(categoria.getCodigoCategoria());
        response.setNombreCategoria(categoria.getNombreCategoria());
        response.setActivo(categoria.isActivo());

        return response;
    }

    // Actualiza una entidad existente sin modificar su identificador.
    public void updateEntity(CategoriaRequest request, Categoria categoria) {

        categoria.setCodigoCategoria(request.getCodigoCategoria());
        categoria.setNombreCategoria(request.getNombreCategoria());
        categoria.setActivo(request.getActivo());

    }

}
