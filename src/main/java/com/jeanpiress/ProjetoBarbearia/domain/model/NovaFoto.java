package com.jeanpiress.ProjetoBarbearia.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

@Builder
@Getter
public class NovaFoto {

    private String nomeArquivo;
    private String contentType;
    private InputStream inputStream;
}
