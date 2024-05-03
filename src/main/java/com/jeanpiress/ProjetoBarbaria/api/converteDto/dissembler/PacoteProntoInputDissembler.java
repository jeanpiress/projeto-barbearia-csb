package com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PacoteProntoInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.PacotePronto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacoteProntoInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public PacotePronto toDomainObject (PacoteProntoInput pacoteProntoInput) {

        return modelMapper.map(pacoteProntoInput, PacotePronto.class);
    }

    public void copyToDomainObject(PacoteProntoInput pacoteProntoInput, PacotePronto pacotePronto) {

        modelMapper.map(pacoteProntoInput, pacotePronto);
    }
}
