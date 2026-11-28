
package com.casajojo.menu.mapper;

import org.springframework.stereotype.Component;

import com.casajojo.menu.entity.MenuDiario;
import com.casajojo.menu.dto.request.MenuDiarioRequest;
import com.casajojo.menu.dto.response.MenuDiarioResponse;

@Component
public class MenuDiarioMapper {

    // Convierte el Request en un nuevo menú diario.
    public MenuDiario toEntity(MenuDiarioRequest request) {

        MenuDiario menuDiario = new MenuDiario();

        menuDiario.setFechaMenu(request.getFechaMenu());

        // Todo menú nuevo comienza en estado BORRADOR.
        menuDiario.setEstado("BORRADOR");

        return menuDiario;
    }

    // Convierte la entidad en una respuesta para el cliente.
    public MenuDiarioResponse toResponse(MenuDiario menuDiario) {

        MenuDiarioResponse response = new MenuDiarioResponse();

        response.setIdMenu(menuDiario.getIdMenu());
        response.setFechaMenu(menuDiario.getFechaMenu());
        response.setEstado(menuDiario.getEstado());

        return response;
    }

    // Actualiza la fecha sin modificar el identificador ni el estado.
    public void updateEntity(MenuDiarioRequest request, MenuDiario menuDiario) {

        menuDiario.setFechaMenu(request.getFechaMenu());

    }

}
