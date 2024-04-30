package com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ClienteResumo {

    private Long id;
    private String nome;
    private String celular;
    private Integer diasRetorno;
    
}
