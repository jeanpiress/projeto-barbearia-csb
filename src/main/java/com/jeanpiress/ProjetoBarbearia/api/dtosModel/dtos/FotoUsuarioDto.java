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
public class FotoUsuarioDto {

    @Schema(description = "Nome do usuario", example = "Foto Usuario")
    private String nomeArquivo;

    @Schema(description = "Descrição do arquivo", example = "Foto do usario de id 007")
    private String descricao;

    @Schema(description = "MultiPartFile do arquivo")
    private String contentType;

    @Schema(description = "Tamanho do arquivo", example = "500Kb")
    private Long tamanho;
}
