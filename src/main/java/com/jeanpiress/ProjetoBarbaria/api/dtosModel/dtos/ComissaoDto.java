package com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ComissaoDto {

    private Long id;
    private Produto produto;
    private Profissional profissional;
    private BigDecimal porcentagemComissao;

}
