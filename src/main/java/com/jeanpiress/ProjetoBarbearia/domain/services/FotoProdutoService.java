package com.jeanpiress.ProjetoBarbearia.domain.services;

import com.jeanpiress.ProjetoBarbearia.domain.exceptions.FotoNaoEncontradaException;
import com.jeanpiress.ProjetoBarbearia.domain.model.FotoProduto;
import com.jeanpiress.ProjetoBarbearia.domain.model.NovaFoto;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.FotoProdutoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Log4j2
@Service
public class FotoProdutoService {

    @Autowired
    private FotoProdutoRepository fotoProdutoRepository;

    @Autowired
    private FotoStorageService fotoStorageService;

    public FotoProduto buscarbyId(Long id){
        return fotoProdutoRepository.findById(id).
                orElseThrow(() -> new FotoNaoEncontradaException("Não foi encontrada foto para esse produto"));
    }

     public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
         Long fotoId = foto.getProduto().getId();
         String nomeArquivoExistente = null;

         if(fotoProdutoRepository.existsById(fotoId)) {
             nomeArquivoExistente = buscarbyId(fotoId).getNomeArquivo();
             fotoProdutoRepository.deleteById(fotoId);
             log.info("Foto do produto de id: {} foi excluida", fotoId);
         }

         String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
         foto.setNomeArquivo(nomeNovoArquivo);

         foto = fotoProdutoRepository.save(foto);
         fotoProdutoRepository.flush();

         NovaFoto novaFoto = NovaFoto.builder()
                 .nomeArquivo(foto.getNomeArquivo())
                 .contentType(foto.getContentType())
                 .inputStream(dadosArquivo)
                 .build();

         fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

         return foto;
    }
}
