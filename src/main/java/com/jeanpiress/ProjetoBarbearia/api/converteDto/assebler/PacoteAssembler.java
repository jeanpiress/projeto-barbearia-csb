package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.PacoteDto;
import com.jeanpiress.ProjetoBarbearia.domain.model.Pacote;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PacoteAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public PacoteDto toModel(Pacote pacote){
        return modelMapper.map(pacote, PacoteDto.class);
    }

    public List<PacoteDto> collectionToModel(List<Pacote> pacotes){
         return pacotes.stream().map(pacote -> toModel(pacote))
                .collect(Collectors.toList());
    }
}
