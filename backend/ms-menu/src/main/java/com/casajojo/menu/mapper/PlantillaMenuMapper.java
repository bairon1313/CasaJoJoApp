
package com.casajojo.menu.mapper;

import org.springframework.stereotype.Component;

import com.casajojo.menu.entity.PlantillaMenu;
import com.casajojo.menu.dto.request.PlantillaMenuRequest;
import com.casajojo.menu.dto.response.PlantillaMenuResponse;

@Component
public class PlantillaMenuMapper {

    // Convierte el Request en una nueva plantilla de menú.
    public PlantillaMenu toEntity(PlantillaMenuRequest request) {

        PlantillaMenu plantilla = new PlantillaMenu();

        plantilla.setNombrePlantilla(request.getNombrePlantilla());
        plantilla.setActivo(request.getActivo());

        return plantilla;
    }

    // Convierte la entidad en una respuesta para el cliente.
    public PlantillaMenuResponse toResponse(PlantillaMenu plantilla) {

        PlantillaMenuResponse response = new PlantillaMenuResponse();

        response.setIdPlantilla(plantilla.getIdPlantilla());
        response.setNombrePlantilla(plantilla.getNombrePlantilla());
        response.setActivo(plantilla.isActivo());

        return response;
    }

    // Actualiza la plantilla conservando su identificador.
    public void updateEntity(PlantillaMenuRequest request,
            PlantillaMenu plantilla) {

        plantilla.setNombrePlantilla(request.getNombrePlantilla());
        plantilla.setActivo(request.getActivo());

    }

}
