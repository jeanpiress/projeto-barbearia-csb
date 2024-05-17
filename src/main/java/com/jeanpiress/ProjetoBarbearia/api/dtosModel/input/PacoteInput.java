package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.ClienteId;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.resumo.PacoteProntoId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PacoteInput {

    @NotNull
    @Valid
    private ClienteId cliente;
    @NotNull
    @Valid
    private PacoteProntoId pacotePronto;



}
