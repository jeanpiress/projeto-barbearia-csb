package com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemPedidoInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public ItemPedido toDomainObject (ItemPedidoInput itemPedidoInput) {

        return modelMapper.map(itemPedidoInput, ItemPedido.class);
    }

    public void copyToDomainObject(ItemPedidoInput itemPedidoInput, ItemPedido itemPedido) {
        itemPedido.setProduto(new Produto());
        modelMapper.map(itemPedidoInput, itemPedido);
    }
}
