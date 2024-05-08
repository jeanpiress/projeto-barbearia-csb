package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioInput {

    @NotBlank
    @Email
    @ApiModelProperty(example = "joao@csb.com.br", required = true)
    private String email;

    @NotBlank
    @ApiModelProperty(example = "123456", required = true)
    private String senha;

    private Cliente cliente;

    private Profissional profissional;

    private Long permissao;

    @ApiModelProperty(value = "nome do usuario", example = "João")
    private String nome;


}
