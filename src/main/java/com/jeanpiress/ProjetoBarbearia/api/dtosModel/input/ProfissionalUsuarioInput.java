package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProfissionalUsuarioInput {

    @Valid
    private ProfissionalInput profissional;

    @Valid
    private UsuarioInput usuario;

}
