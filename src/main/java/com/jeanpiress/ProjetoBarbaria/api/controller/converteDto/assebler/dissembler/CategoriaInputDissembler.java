package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.CategoriaInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoriaInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Categoria toDomainObject (CategoriaInput categoriaInput) {

        return modelMapper.map(categoriaInput, Categoria.class);
    }

    public void copyToDomainObject(CategoriaInput categoriaInput, Categoria categoria) {

        modelMapper.map(categoriaInput, categoria);
    }
}
