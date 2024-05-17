package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteProntoDto;
import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PacoteProntoAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public PacoteProntoDto toModel(PacotePronto pacotePronto){
        return modelMapper.map(pacotePronto, PacoteProntoDto.class);
    }

    public List<PacoteProntoDto> collectionToModel(List<PacotePronto> pacotesProntos){
         return pacotesProntos.stream().map(pacotePronto -> toModel(pacotePronto))
                .collect(Collectors.toList());
    }
}
