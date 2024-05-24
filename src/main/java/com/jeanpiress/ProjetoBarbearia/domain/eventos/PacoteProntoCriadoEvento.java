package com.jeanpiress.ProjetoBarbearia.domain.eventos;


import com.jeanpiress.ProjetoBarbearia.domain.model.PacotePronto;

public class PacoteProntoCriadoEvento {

    private PacotePronto pacotePronto;

    public PacoteProntoCriadoEvento(PacotePronto pacotePronto) {
        super();
        this.pacotePronto = pacotePronto;
    }

    public PacotePronto getPacotePronto() {
        return pacotePronto;
    }
}
