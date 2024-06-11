package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.ClientesRetorno;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.ClientesRetornoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientesRetornoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public ClientesRetornoDto toModel(ClientesRetorno clientesRetorno){
        return modelMapper.map(clientesRetorno, ClientesRetornoDto.class);
    }

    public List<ClientesRetornoDto> collectionToModel(List<ClientesRetorno> pacotesProntos){
         return pacotesProntos.stream().map(clientesRetorno -> toModel(clientesRetorno))
                .collect(Collectors.toList());
    }
}
