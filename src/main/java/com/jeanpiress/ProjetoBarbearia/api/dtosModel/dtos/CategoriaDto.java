package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoriaDto {

    @Schema(description = "ID da categoria", example = "1")
    private Long id;

    @Schema(example = "Cabelo")
    private String nome;
}
