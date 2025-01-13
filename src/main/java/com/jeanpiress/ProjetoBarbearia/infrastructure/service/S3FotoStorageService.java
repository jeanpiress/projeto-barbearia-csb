package com.jeanpiress.ProjetoBarbearia.infrastructure.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.jeanpiress.ProjetoBarbearia.core.storage.StorageProperties;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoRecuperada;
import com.jeanpiress.ProjetoBarbearia.domain.model.NovaFoto;
import com.jeanpiress.ProjetoBarbearia.domain.services.FotoStorageService;
import com.jeanpiress.ProjetoBarbearia.infrastructure.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;

public class S3FotoStorageService implements FotoStorageService {


    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {
            String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
            var objecMetadata = new ObjectMetadata();
            objecMetadata.setContentType(novaFoto.getContentType());

            var putObjectRequest = new PutObjectRequest(storageProperties.getS3().getBucket(), caminhoArquivo,
                    novaFoto.getInputStream(), objecMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);
        }catch (Exception e) {
            throw new StorageException("Não foi possivel enviar arquivo para Amazon S3", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {
        try {
            var deleteObjectRequest = new DeleteObjectRequest(
                    storageProperties.getS3().getBucket(),
                    this.getCaminhoArquivo(nomeArquivo));

            amazonS3.deleteObject(deleteObjectRequest);
        }catch (Exception e) {
            throw new StorageException("Não foi possivel excluir arquivo de Amazon S3", e);
        }
    }

    @Override
    public FotoRecuperada recuperar(String nomeArquivo) {
        String caminhoArquivo = getCaminhoArquivo(nomeArquivo);
        URL url = amazonS3.getUrl(storageProperties.getS3().getBucket(), caminhoArquivo);
        return FotoRecuperada.builder().url(url.toString()).build();
    }

    @Override
    public void substituir(String nomeArquivoAntigo, NovaFoto novaFoto) {
        this.armazenar(novaFoto);
        if(nomeArquivoAntigo != null) {
            this.remover(nomeArquivoAntigo);
        }
    }


    private String getCaminhoArquivo(String nomeArquivo) {
        return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
    }
}
