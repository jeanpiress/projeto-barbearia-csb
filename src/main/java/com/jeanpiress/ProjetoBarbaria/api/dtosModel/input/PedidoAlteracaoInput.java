package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ClienteId;
import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ProfissionalId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class PedidoAlteracaoInput {

    @NotNull
    @ApiModelProperty(example = "2024-04-19T14:30:00-03:00", required = true)
    private OffsetDateTime horario;

    @NotNull
    @Valid
    @ApiModelProperty(value = "ID do profissional", example = "1", required = true)
    private ProfissionalId profissional;
}
