package com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.PacoteInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Pacote;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacoteInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Pacote toDomainObject (PacoteInput pacoteInput) {

        return modelMapper.map(pacoteInput, Pacote.class);
    }

    public void copyToDomainObject(PacoteInput pacoteInput, Pacote pacote) {

        modelMapper.map(pacoteInput, pacote);
    }
}
