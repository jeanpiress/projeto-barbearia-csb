package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.ClienteInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Cliente toDomainObject (ClienteInput clienteInput) {

        return modelMapper.map(clienteInput, Cliente.class);
    }

    public void copyToDomainObject(ClienteInput clienteInput, Cliente cliente) {

        modelMapper.map(clienteInput, cliente);
    }
}
