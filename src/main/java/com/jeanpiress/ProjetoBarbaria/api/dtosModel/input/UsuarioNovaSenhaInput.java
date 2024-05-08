package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioNovaSenhaInput {

    @NotBlank
    @Email
    @ApiModelProperty(example = "joao@csb.com.br", required = true)
    private String email;

    @NotBlank
    @ApiModelProperty(example = "123456", required = true)
    private String senhaAtual;

    @NotBlank
    @ApiModelProperty(example = "789123", required = true)
    private String novaSenha;

    @NotBlank
    @ApiModelProperty(example = "789123", required = true)
    private String confirmarSenha;


}
