package com.jeanpiress.ProjetoBarbearia.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

@Getter
@Builder
public class FotoRecuperada {

    private InputStream inputStream;
    private String url;

    public boolean temUrl(){
        return url != null;
    }
}
