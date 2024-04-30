package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioNovaSenhaInput {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senhaAtual;

    @NotBlank
    private String novaSenha;

    @NotBlank
    private String confirmarSenha;


}
