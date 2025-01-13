package com.jeanpiress.ProjetoBarbearia.core.storage;

import com.amazonaws.regions.Regions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Getter
@Setter
@Component
@ConfigurationProperties("csb.storage")
public class StorageProperties {

    private Local local;
    private S3 s3;
    private TipoStorage tipo = TipoStorage.LOCAL;


    @Getter
    @Setter
    public static class Local {
        private Path diretorioFotos;
    }

    @Getter
    @Setter
    public static class S3{
        private String idChaveAcesso;
        private String chaveAcessoSecreta;
        private String bucket;
        private Regions regiao;
        private String diretorioFotos;

    }

    public enum TipoStorage {
        LOCAL, S3;
    }
}
