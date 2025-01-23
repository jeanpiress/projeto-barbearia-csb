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
public class FotoUsuario {

    @EqualsAndHashCode.Include
    @Id
    @Schema(example = "1")
    @Column(name = "produto_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Usuario usuario;

    @Column(name = "nome_arquivo")
    @Schema(example = "fotoUsuario01")
    private String nomeArquivo;

    @Column(name = "descricao")
    @Schema(example = "foto do usuario de id 01")
    private String descricao;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "tamanho")
    @Schema(example = "500")
    private Long tamanho;


}
