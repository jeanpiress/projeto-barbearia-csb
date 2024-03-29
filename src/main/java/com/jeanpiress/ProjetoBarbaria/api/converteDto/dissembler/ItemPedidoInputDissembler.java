package com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.ItemPedidoInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
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
