package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbearia.domain.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteInput {


    @NotBlank
    @Schema(example = "João", required = true)
    private String nome;

    @NotBlank
    @Schema(example = "34999999999", required = true)
    private String celular;

    @Schema(description = "Data de nascimento do cliente", example = "1991-12-25")
    private LocalDate dataNascimento;

    @Schema(example = "Degrade até a maquina zero")
    private String observacao;

    @NotNull
    @Schema(description = "A quatidade de dias que o cliente cosutma voltar", example = "30", required = true)
    private Integer diasRetorno;

    private Endereco endereco;
}
