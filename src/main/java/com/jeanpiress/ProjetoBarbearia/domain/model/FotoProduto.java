package com.jeanpiress.ProjetoBarbearia.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class FotoProduto {

    @EqualsAndHashCode.Include
    @Id
    @Schema(example = "1")
    @Column(name = "produto_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Produto produto;

    @Column(name = "nome_arquivo")
    @Schema(example = "fotoShampoo")
    private String nomeArquivo;

    @Column(name = "descricao")
    @Schema(example = "foto do shampoo da marca csb")
    private String descricao;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "tamanho")
    @Schema(example = "500")
    private Long tamanho;


}
