package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteResumo;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.PermissaoIdNome;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalResumo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UsuarioDto {

    private String email;

    private ClienteResumo cliente;

    private ProfissionalResumo profissional;

    private Set<PermissaoIdNome> permissoes = new HashSet<>();


}
