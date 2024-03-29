package com.jeanpiress.ProjetoBarbaria.api.controller.dtos;

import com.jeanpiress.ProjetoBarbaria.domain.model.Produto;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Getter
@Setter
public class ComissaoDto {

    private Long id;
    private Produto produto;
    private Profissional profissional;
    private BigDecimal porcentagemComissao;

}
