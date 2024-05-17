package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.ProfissionalDto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfissionalAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public ProfissionalDto toModel(Profissional profissional){
        return modelMapper.map(profissional, ProfissionalDto.class);
    }

    public List<ProfissionalDto> collectionToModel(List<Profissional> profissionals){
         return profissionals.stream().map(profissional -> toModel(profissional))
                .collect(Collectors.toList());
    }
}
