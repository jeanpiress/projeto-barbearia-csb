package com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.PedidoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
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
