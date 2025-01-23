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
    public void armazenar(NovaFoto novaFoto, String diretorio) {

        try{
            Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo(), diretorio);

            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        }catch (Exception e){
            throw new StorageException("Não foi possivel armazenar arquivo", e);
        }
    }

    @Override
    public void remover(String nomeArquivo, String diretorio) {
        try {
            Path arquivoPath = getArquivoPath(nomeArquivo, diretorio);
            Files.deleteIfExists(arquivoPath);
        } catch (Exception e){
            throw new StorageException("Não foi possivel excluir arquivo", e);
        }

    }

    @Override
    public FotoRecuperada recuperar(String nomeArquivo, String diretorio) {
        try{
            Path arquivoPath = getArquivoPath(nomeArquivo, diretorio);

            return FotoRecuperada.builder()
                    .inputStream(Files.newInputStream(arquivoPath))
                    .build();

        }catch (Exception e){
            throw new StorageException("Não foi possivel armazenar arquivo", e);
        }
    }

    @Override
    public void substituir(String nomeArquivoAntigo, NovaFoto novaFoto, String diretorio) {
        this.armazenar(novaFoto, diretorio);
        if(nomeArquivoAntigo != null) {
            this.remover(nomeArquivoAntigo, diretorio);
        }
    }

    private Path getArquivoPath(String nomeArquivo, String diretorio) {
        return storageProperties.getLocal().getDiretorioFotos()
                .resolve(diretorio)
                .resolve(nomeArquivo);
    }

}
