package com.jeanpiress.ProjetoBarbaria.api.converteDto.dissembler;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.input.UsuarioInput;
import com.jeanpiress.ProjetoBarbaria.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInputDissembler {

    @Autowired
    private ModelMapper modelMapper;

    public Usuario toDomainObject (UsuarioInput usuarioInput) {

        return modelMapper.map(usuarioInput, Usuario.class);
    }

    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {

        modelMapper.map(usuarioInput, usuario);
    }
}
