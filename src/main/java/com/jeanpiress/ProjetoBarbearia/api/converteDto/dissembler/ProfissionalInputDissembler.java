package com.jeanpiress.ProjetoBarbearia.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.input.ProfissionalInput;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfissionalInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Profissional toDomainObject (ProfissionalInput profissionalInput) {

        return modelMapper.map(profissionalInput, Profissional.class);
    }

    public void copyToDomainObject(ProfissionalInput profissionalInput, Profissional profissional) {

        modelMapper.map(profissionalInput, profissional);
    }
}
