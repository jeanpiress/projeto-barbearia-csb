package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.model.FotoRecuperada;
import com.jeanpiress.ProjetoBarbearia.domain.model.NovaFoto;

import java.util.UUID;

public interface FotoStorageService {

    void armazenar(NovaFoto novaFoto);

    void remover(String nomeArquivo);

    FotoRecuperada recuperar(String nomeArquivo);

    default String gerarNomeArquivo(String nomeOriginal) {
        return UUID.randomUUID().toString() + "_" + nomeOriginal;
    }

    void substituir(String nomeArquivoAntigo, NovaFoto novaFoto);

}
