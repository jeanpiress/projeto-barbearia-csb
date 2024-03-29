package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.CategoriaDto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoriaAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public CategoriaDto toModel(Categoria categoria){
        return modelMapper.map(categoria, CategoriaDto.class);
    }

    public List<CategoriaDto> collectionToModel(List<Categoria> categorias){
         return categorias.stream().map(categoria -> toModel(categoria))
                .collect(Collectors.toList());
    }
}
