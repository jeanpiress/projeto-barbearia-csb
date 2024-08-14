package com.jeanpiress.ProjetoBarbearia.api.controller;

import com.jeanpiress.ProjetoBarbearia.api.controller.openapi.CaixaControllerOpenApi;
import com.jeanpiress.ProjetoBarbearia.api.dtosModel.dtos.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbearia.domain.services.CaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/caixa", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaixaController implements CaixaControllerOpenApi {

    @Autowired
    private CaixaService caixaService;



    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @GetMapping
    public ResponseEntity<CaixaModel> buscarCaixaDiario(){
        CaixaModel caixaDiario = caixaService.gerarCaixaDiario();
        return ResponseEntity.ok(caixaDiario);
    }

    //@PreAuthorize("hasAuthority('RECEPCAO')")
    @DeleteMapping("/fechar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fecharCaixa(){
        caixaService.fecharCaixa();
    }


}
