package com.casajojo.menu.mapper;

import org.springframework.stereotype.Component;

import com.casajojo.menu.entity.MenuDetalle;
import com.casajojo.menu.entity.MenuDiario;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.dto.request.MenuDetalleRequest;
import com.casajojo.menu.dto.response.MenuDetalleResponse;

@Component
public class MenuDetalleMapper {

    // Crea un detalle asociando el menú diario con un plato existente.
    public MenuDetalle toEntity(MenuDetalleRequest request,
            MenuDiario menuDiario, Plato plato) {

        MenuDetalle detalle = new MenuDetalle();

        detalle.setMenuDiario(menuDiario);
        detalle.setPlato(plato);
        detalle.setPrecioMenu(request.getPrecioMenu());
        detalle.setDisponible(request.getDisponible());

        return detalle;
    }

    // Convierte la entidad en una respuesta para Android.
    public MenuDetalleResponse toResponse(MenuDetalle detalle) {

        MenuDetalleResponse response = new MenuDetalleResponse();

        response.setIdMenuDetalle(detalle.getIdMenuDetalle());

        // Información del menú diario.
        response.setIdMenu(detalle.getMenuDiario().getIdMenu());
        response.setFechaMenu(detalle.getMenuDiario().getFechaMenu());

        // Información del plato asociado.
        Plato plato = detalle.getPlato();

        response.setIdPlato(plato.getIdPlato());
        response.setNombrePlato(plato.getNombrePlato());
        response.setDescripcion(plato.getDescripcion());

        // Información de la categoría del plato.
        response.setIdCategoria(plato.getCategoria().getIdCategoria());
        response.setCodigoCategoria(plato.getCategoria().getCodigoCategoria());
        response.setNombreCategoria(plato.getCategoria().getNombreCategoria());

        // Precio y disponibilidad específicos del menú diario.
        response.setPrecioMenu(detalle.getPrecioMenu());
        response.setDisponible(detalle.isDisponible());

        return response;
    }

    // Actualiza el detalle conservando su identificador.
    public void updateEntity(MenuDetalleRequest request,
            MenuDetalle detalle, MenuDiario menuDiario, Plato plato) {

        detalle.setMenuDiario(menuDiario);
        detalle.setPlato(plato);
        detalle.setPrecioMenu(request.getPrecioMenu());
        detalle.setDisponible(request.getDisponible());

    }

}
