package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioInput {

    @NotBlank
    @Email
    @Schema(example = "joao@csb.com.br", required = true)
    private String email;

    @NotBlank
    @Schema(example = "123456", required = true)
    private String senha;

    @NotBlank
    @Schema(example = "GERENTE")
    private String maiorPermissao;

    @Schema(description = "nome do usuario", example = "João")
    private String nome;


}
