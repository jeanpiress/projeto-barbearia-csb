package com.jeanpiress.ProjetoBarbaria.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.ItemPedidoDto;
import com.jeanpiress.ProjetoBarbaria.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemPedidoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public ItemPedidoDto toModel(ItemPedido itemPedido){
        return modelMapper.map(itemPedido, ItemPedidoDto.class);
    }

    public List<ItemPedidoDto> collectionToModel(List<ItemPedido> itensPedidos){
        return itensPedidos.stream().map(itemPedido -> toModel(itemPedido))
                .collect(Collectors.toList());

    }
}
