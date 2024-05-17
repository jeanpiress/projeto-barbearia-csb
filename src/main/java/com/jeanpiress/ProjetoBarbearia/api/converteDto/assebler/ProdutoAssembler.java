package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProdutoDto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public ProdutoDto toModel(Produto produto){
        return modelMapper.map(produto, ProdutoDto.class);
    }

    public List<ProdutoDto> collectionToModel(List<Produto> produtos){
         return produtos.stream().map(produto -> toModel(produto))
                .collect(Collectors.toList());
    }
}
