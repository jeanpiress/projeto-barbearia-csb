package com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.CategoriaInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
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
