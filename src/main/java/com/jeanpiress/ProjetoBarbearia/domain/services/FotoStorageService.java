package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.model.FotoRecuperada;
import com.jeanpiress.ProjetoBarbearia.domain.model.NovaFoto;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.util.List;
import java.util.UUID;

public interface FotoStorageService {

    void armazenar(NovaFoto novaFoto, String diretorio);

    void remover(String nomeArquivo, String diretorio);

    FotoRecuperada recuperar(String nomeArquivo, String diretorio);

    default String gerarNomeArquivo(String nomeOriginal) {
        return UUID.randomUUID().toString() + "_" + nomeOriginal;
    }

    default void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }

    }

    void substituir(String nomeArquivoAntigo, NovaFoto novaFoto, String diretorio);

}
