package com.jeanpiress.ProjetoBarbearia.domain.Enuns;

import lombok.Getter;

@Getter
public enum StatusPedido {

    AGENDADO,
    CONFIRMADO,
    AGUARDANDO,
    EMATENDIMENTO,
    FINALIZADO,
    CANCELADO,
    EXCLUIDO;
}
