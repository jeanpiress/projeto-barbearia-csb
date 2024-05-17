package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteResumo;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PermissaoIdNome;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ProfissionalResumo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UsuarioDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "joao@csb.com.br")
    private String email;

    private ClienteResumo cliente;

    private ProfissionalResumo profissional;

    private Set<PermissaoIdNome> permissoes;


}
