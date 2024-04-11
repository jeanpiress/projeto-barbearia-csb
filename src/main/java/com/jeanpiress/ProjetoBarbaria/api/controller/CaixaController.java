package com.jeanpiress.ProjetoBarbaria.api.controller;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.dtos.PedidoDto;
import com.jeanpiress.ProjetoBarbaria.domain.model.CaixaDiario;
import com.jeanpiress.ProjetoBarbaria.domain.services.CaixaDiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/caixa")
public class CaixaController {

    @Autowired
    private CaixaDiarioService caixaDiarioService;

    @GetMapping
    public ResponseEntity<CaixaDiario> buscarCaixaDiario(){
        CaixaDiario caixaDiario = caixaDiarioService.gerarCaixa();
        return ResponseEntity.ok(caixaDiario);
    }

    @DeleteMapping("/fechar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fecharCaixa(){
        caixaDiarioService.fecharCaixa();
    }




}
