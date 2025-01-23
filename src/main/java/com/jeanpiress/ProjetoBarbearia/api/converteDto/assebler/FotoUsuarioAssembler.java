package com.jeanpiress.ProjetoBarbearia.api.converteDto.assebler;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.FotoUsuarioDto;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoUsuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FotoUsuarioAssembler {

    @Autowired
    private ModelMapper modelMapper;


    public FotoUsuarioDto toModel(FotoUsuario fotoUsuario){
        return modelMapper.map(fotoUsuario, FotoUsuarioDto.class);
    }

}
