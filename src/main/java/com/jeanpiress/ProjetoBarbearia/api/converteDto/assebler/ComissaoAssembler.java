package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ComissaoDto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Comissao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComissaoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public ComissaoDto toModel(Comissao comissao){
        return modelMapper.map(comissao, ComissaoDto.class);
    }

    public List<ComissaoDto> collectionToModel(List<Comissao> comissoes){
        return comissoes.stream().map(comissao -> toModel(comissao))
                .collect(Collectors.toList());

    }
}
