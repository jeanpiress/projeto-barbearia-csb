package com.jeanpiress.ProjetoBarbearia.domain.eventos;


import com.jeanpiress.ProjetoBarbearia.domain.model.Pacote;

public class PacoteRealizadoEvento {

    private Pacote pacote;

    public PacoteRealizadoEvento(Pacote pacote) {
        super();
        this.pacote = pacote;
    }

    public Pacote getPacote() {
        return pacote;
    }
}
