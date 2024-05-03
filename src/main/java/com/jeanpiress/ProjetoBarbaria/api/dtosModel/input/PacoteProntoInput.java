package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;


import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.ItemPacoteId;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class PacoteProntoInput {

    @NotNull
    private Integer validade;
    @NotBlank
    private String nome;
    private String descricao;
    @NotNull
    private List<ItemPacoteId> itensAtivos;



}
