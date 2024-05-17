package com.jeanpiress.ProjetoBarbearia.domain.eventos;


import com.jeanpiress.ProjetoBarbearia.domain.model.Cliente;

public class ClienteAtendidoEvento {

    private Cliente cliente;

    public ClienteAtendidoEvento(Cliente cliente) {
        super();
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
