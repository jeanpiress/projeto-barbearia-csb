package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteResumo;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.PermissaoIdNome;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalResumo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UsuarioDto {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "joao@csb.com.br")
    private String email;

    private ClienteResumo cliente;

    private ProfissionalResumo profissional;

    private Set<PermissaoIdNome> permissoes;


}
