package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.domain.model.relatorios.CaixaModel;
import com.jeanpiress.ProjetoBarbaria.domain.services.CaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/caixa")
public class CaixaController {

    @Autowired
    private CaixaService caixaDiarioService;

    @GetMapping
    public ResponseEntity<CaixaModel> buscarCaixaDiario(){
        CaixaModel caixaDiario = caixaDiarioService.gerarCaixaDiario();
        return ResponseEntity.ok(caixaDiario);
    }

    @DeleteMapping("/fechar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fecharCaixa(){
        caixaDiarioService.fecharCaixa();
    }


}
