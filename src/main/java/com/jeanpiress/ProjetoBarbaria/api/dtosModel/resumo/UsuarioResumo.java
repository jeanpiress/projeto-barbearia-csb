package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import com.jeanpiress.ProjetoBarbaria.domain.model.Permissao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResumo {

    private Long id;
    private String nome;
    private String permissao;
    
}
