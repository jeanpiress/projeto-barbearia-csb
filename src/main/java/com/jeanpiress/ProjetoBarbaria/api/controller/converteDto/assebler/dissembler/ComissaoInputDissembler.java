package com.jeanpiress.ProjetoBarbaria.api.controller.converteDto.assebler.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input.ComissaoInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Comissao;
import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComissaoInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Comissao toDomainObject (ComissaoInput comissaoInput) {

        return modelMapper.map(comissaoInput, Comissao.class);
    }

    public void copyToDomainObject(ComissaoInput comissaoInput, Comissao comissao) {
        comissao.setProfissional(new Profissional());
        comissao.setProduto(new Produto());
        modelMapper.map(comissaoInput, comissao);
    }
}
