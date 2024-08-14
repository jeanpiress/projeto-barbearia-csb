package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioNovaSenhaInput {

    @NotBlank
    @Email
    @Schema(example = "joao@csb.com.br", required = true)
    private String email;

    @NotBlank
    @Schema(example = "123456", required = true)
    private String senhaAtual;

    @NotBlank
    @Schema(example = "789123", required = true)
    private String novaSenha;

    @NotBlank
    @Schema(example = "789123", required = true)
    private String confirmarSenha;


}
