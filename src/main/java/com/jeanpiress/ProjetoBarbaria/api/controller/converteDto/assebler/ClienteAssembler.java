package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.ClienteDto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public ClienteDto toModel(Cliente cliente){
        return modelMapper.map(cliente, ClienteDto.class);
    }

    public List<ClienteDto> collectionToModel(List<Cliente> clientes){
         List<ClienteDto> clientesDto = clientes.stream().map(cliente -> toModel(cliente))
                .collect(Collectors.toList());

        return clientesDto;
    }
}
