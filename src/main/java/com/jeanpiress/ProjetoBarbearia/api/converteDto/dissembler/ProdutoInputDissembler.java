package com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProdutoInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Categoria;
import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
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
