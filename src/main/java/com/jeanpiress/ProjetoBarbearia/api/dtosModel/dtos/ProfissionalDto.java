package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.domain.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ProfissionalDto {

    @Schema(description = "ID do profissional", example = "Corte")
    private Long id;

    @Schema(example = "João Paulo Ferreira")
    private String nome;

    @Schema(example = "João")
    private String nomeExibicao;

    @Schema(example = "34999999999")
    private String celular;

    @Schema(example = "123.456.789-10")
    private String cpf;

    @Schema(description = "Data nascimento", example = "1991-12-25")
    private LocalDate dataNascimento;

    @Schema(example = "0.00")
    private BigDecimal salarioFixo;

    @Schema(example = "5")
    private Integer diaPagamento;

    @Schema(example = "true")
    private boolean ativo;

    private Endereco endereco;

}
