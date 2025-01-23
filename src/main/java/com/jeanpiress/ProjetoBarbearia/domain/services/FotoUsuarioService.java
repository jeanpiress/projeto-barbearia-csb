package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.FotoNaoEncontradaException;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoProduto;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoUsuario;
import com.jeanpiress.ProjetoBarbearia.domain.model.NovaFoto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.FotoProdutoRepository;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.FotoUsuarioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Log4j2
@Service
public class FotoUsuarioService {

    @Autowired
    private FotoUsuarioRepository fotoUsuarioRepository;

    @Autowired
    private FotoStorageService fotoStorageService;

    public FotoUsuario buscarbyId(Long id){
        return fotoUsuarioRepository.findById(id).
                orElseThrow(() -> new FotoNaoEncontradaException("Não foi encontrada foto para esse usuario"));
    }

     public FotoUsuario salvar(FotoUsuario foto, InputStream dadosArquivo, String diretorio) {
         Long fotoId = foto.getUsuario().getId();
         String nomeArquivoExistente = null;

         if(fotoUsuarioRepository.existsById(fotoId)) {
             nomeArquivoExistente = buscarbyId(fotoId).getNomeArquivo();
             fotoUsuarioRepository.deleteById(fotoId);
             log.info("Foto do usuario de id: {} foi excluida", fotoId);
         }

         String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
         foto.setNomeArquivo(nomeNovoArquivo);

         foto = fotoUsuarioRepository.save(foto);
         fotoUsuarioRepository.flush();

         NovaFoto novaFoto = NovaFoto.builder()
                 .nomeArquivo(foto.getNomeArquivo())
                 .contentType(foto.getContentType())
                 .inputStream(dadosArquivo)
                 .build();

         fotoStorageService.substituir(nomeArquivoExistente, novaFoto, diretorio);

         return foto;
    }
}
