package com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResumo {

    @Schema(description ="Id do usuario", example = "1")
    private Long id;
    @Schema(description ="Nome do usuario", example = "João")
    private String nome;
    @Schema(description ="Lista de permissoes do usuario", example = "[CLIENTE, PROFISSIONAL]")
    private String permissao;
    
}
