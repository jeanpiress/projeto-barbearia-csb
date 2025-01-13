package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.FotoProdutoDto;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoProduto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FotoProdutoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public FotoProdutoDto toModel(FotoProduto fotoProduto){
        return modelMapper.map(fotoProduto, FotoProdutoDto.class);
    }

}
