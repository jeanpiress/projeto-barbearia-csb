package com.jeanpiress.ProjetoBarbearia.api.dtosModel.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FotoInput {

    @NotNull
    private MultipartFile arquivo;
    @NotNull
    private String descricao;



}
