package com.jeanpiress.ProjetoBarbaria.api.controller.dtos.input;

import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.resumo.ClienteId;
import com.jeanpiress.ProjetoBarbaria.api.controller.dtos.resumo.ProfissionalId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class PedidoInput {

    @NotNull
    private OffsetDateTime horario;
    @NotNull
    private ClienteId cliente;
    @NotNull
    private ProfissionalId profissional;
}
