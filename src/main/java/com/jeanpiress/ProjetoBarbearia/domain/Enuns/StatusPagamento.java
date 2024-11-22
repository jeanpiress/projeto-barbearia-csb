package com.jeanpiress.ProjetoBarbearia.domain.Enuns;

import lombok.Getter;

@Getter
public enum StatusPagamento {

    AGUARDANDO_PAGAMENTO,
    PAGO,
    CANCELADO;
}
