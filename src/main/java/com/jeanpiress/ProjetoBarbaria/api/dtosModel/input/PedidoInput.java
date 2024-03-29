package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteId;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class PedidoInput {

    @NotNull
    private OffsetDateTime horario;
    @NotNull
    @Valid
    private ClienteId cliente;
    @NotNull
    @Valid
    private ProfissionalId profissional;
}
