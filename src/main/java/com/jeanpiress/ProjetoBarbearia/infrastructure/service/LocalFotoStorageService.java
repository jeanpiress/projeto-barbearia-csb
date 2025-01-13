package com.jeanpiress.ProjetoBarbearia.infrastructure.service;

import com.jeanpiress.ProjetoBarbearia.core.storage.StorageProperties;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoRecuperada;
import com.jeanpiress.ProjetoBarbearia.domain.model.NovaFoto;
import com.jeanpiress.ProjetoBarbearia.domain.services.FotoStorageService;
import com.jeanpiress.ProjetoBarbearia.infrastructure.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFotoStorageService implements FotoStorageService {

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public void armazenar(NovaFoto novaFoto) {

        try{
            Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        }catch (Exception e){
            throw new StorageException("Não foi possivel armazenar arquivo", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {
        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            Files.deleteIfExists(arquivoPath);
        } catch (Exception e){
            throw new StorageException("Não foi possivel excluir arquivo", e);
        }

    }

    @Override
    public FotoRecuperada recuperar(String nomeArquivo) {
        try{
            Path arquivoPath = getArquivoPath(nomeArquivo);

            return FotoRecuperada.builder()
                    .inputStream(Files.newInputStream(arquivoPath))
                    .build();

        }catch (Exception e){
            throw new StorageException("Não foi possivel armazenar arquivo", e);
        }
    }

    @Override
    public void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
        this.armazenar(novaFoto);
        if(nomeArquivoAntigo != null) {
            this.remover(nomeArquivoAntigo);
        }
    }

    private Path getArquivoPath(String nomeArquivo) {
        return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
    }
}
