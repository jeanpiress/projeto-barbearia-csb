package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResumo {

    @ApiModelProperty(value ="Id do usuario", example = "1")
    private Long id;
    @ApiModelProperty(value ="Nome do usuario", example = "João")
    private String nome;
    @ApiModelProperty(value ="Lista de permissoes do usuario", example = "[CLIENTE, PROFISSIONAL]")
    private String permissao;
    
}
