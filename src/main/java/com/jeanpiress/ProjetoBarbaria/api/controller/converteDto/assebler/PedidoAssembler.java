package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public PedidoDto toModel(Pedido pedido){
        return modelMapper.map(pedido, PedidoDto.class);
    }

    public List<PedidoDto> collectionToModel(List<Pedido> pedidos){
         return pedidos.stream().map(pedido -> toModel(pedido))
                .collect(Collectors.toList());
    }
}
