package com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ClienteInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
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
