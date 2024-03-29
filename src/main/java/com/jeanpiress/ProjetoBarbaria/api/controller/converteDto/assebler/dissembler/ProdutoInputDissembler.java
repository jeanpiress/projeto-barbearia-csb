package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProdutoInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Produto toDomainObject (ProdutoInput produtoInput) {

        return modelMapper.map(produtoInput, Produto.class);
    }

    public void copyToDomainObject(ProdutoInput produtoInput, Produto produto) {
        produto.setCategoria(new Categoria());
        modelMapper.map(produtoInput, produto);
    }
}
