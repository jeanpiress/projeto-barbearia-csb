package com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos;

import com.jeanpiress.ProjetoBarbearia.domain.model.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FotoProdutoDto {

    @Schema(description = "Nome do arquivo", example = "Foto Shampoo")
    private String nomeArquivo;

    @Schema(description = "Descrição do arquivo", example = "Shampoo da marca Csb")
    private String descricao;

    @Schema(description = "MultiPartFile do arquivo")
    private String contentType;

    @Schema(description = "Tamanho do arquivo", example = "500Kb")
    private Long tamanho;
}
