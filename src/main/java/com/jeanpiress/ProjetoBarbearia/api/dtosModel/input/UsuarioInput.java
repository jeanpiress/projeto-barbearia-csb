package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalId;
import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbearia.domain.model.Profissional;
import io.swagger.annotations.ApiModelProperty;
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

    @Schema(example = "1")
    private Long permissao;

    @Schema(description = "nome do usuario", example = "João")
    private String nome;


}
