package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.PedidoInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Pedido toDomainObject (PedidoInput pedidoInput) {

        return modelMapper.map(pedidoInput, Pedido.class);
    }

    public void copyToDomainObject(PedidoInput pedidoInput, Pedido pedido) {
        pedido.setCliente(new Cliente());
        pedido.setProfissional(new Profissional());
        modelMapper.map(pedidoInput, pedido);
    }
}
