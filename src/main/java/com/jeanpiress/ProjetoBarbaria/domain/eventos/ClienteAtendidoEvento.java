package com.jeanpiress.ProjetoBarbaria.domain.eventos;


import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;

public class ClienteAtendidoEvento {

    private Cliente Cliente;

    public ClienteAtendidoEvento(Cliente Cliente) {
        super();
        this.Cliente = Cliente;
    }

    public Cliente getCliente() {
        return Cliente;
    }
}
