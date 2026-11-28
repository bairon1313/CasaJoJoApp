
package com.casajojo.menu.mapper;

import org.springframework.stereotype.Component;

import com.casajojo.menu.entity.PlantillaDetalle;
import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.entity.Plato;
import com.casajojo.menu.dto.request.PlantillaDetalleRequest;
import com.casajojo.menu.dto.response.PlantillaDetalleResponse;

@Component
public class PlantillaDetalleMapper {

    // Crea una asociación entre una plantilla y un plato existente.
    public PlantillaDetalle toEntity(PlantillaDetalleRequest request,
            PlantillaMenu plantillaMenu, Plato plato) {

        PlantillaDetalle detalle = new PlantillaDetalle();

        detalle.setPlantillaMenu(plantillaMenu);
        detalle.setPlato(plato);

        return detalle;
    }

    // Convierte la entidad en una respuesta para el cliente.
    public PlantillaDetalleResponse toResponse(PlantillaDetalle detalle) {

        PlantillaDetalleResponse response = new PlantillaDetalleResponse();

        response.setIdPlantillaDetalle(detalle.getIdPlantillaDetalle());

        // Información de la plantilla asociada.
        response.setIdPlantilla(detalle.getPlantillaMenu().getIdPlantilla());
        response.setNombrePlantilla(detalle.getPlantillaMenu().getNombrePlantilla());

        // Información del plato asociado.
        Plato plato = detalle.getPlato();

        response.setIdPlato(plato.getIdPlato());
        response.setNombrePlato(plato.getNombrePlato());
        response.setDescripcion(plato.getDescripcion());
        response.setPrecioPlato(plato.getPrecioPlato());
        response.setDisponible(plato.isDisponible());

        // Información de la categoría del plato.
        response.setIdCategoria(plato.getCategoria().getIdCategoria());
        response.setCodigoCategoria(plato.getCategoria().getCodigoCategoria());
        response.setNombreCategoria(plato.getCategoria().getNombreCategoria());

        return response;
    }

    // Actualiza las asociaciones conservando el identificador del detalle.
    public void updateEntity(PlantillaDetalleRequest request,
            PlantillaDetalle detalle, PlantillaMenu plantillaMenu, Plato plato) {

        detalle.setPlantillaMenu(plantillaMenu);
        detalle.setPlato(plato);

    }

}
