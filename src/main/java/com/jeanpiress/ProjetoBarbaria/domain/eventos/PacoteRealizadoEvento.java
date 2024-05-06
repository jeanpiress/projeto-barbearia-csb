package com.jeanpiress.ProjetoBarbaria.domain.eventos;


import com.jeanpiress.ProjetoBarbaria.domain.model.Pacote;

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
