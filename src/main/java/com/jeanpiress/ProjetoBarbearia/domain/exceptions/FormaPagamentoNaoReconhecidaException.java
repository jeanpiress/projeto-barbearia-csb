package com.jeanpiress.ProjetoBarbearia.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormaPagamentoNaoReconhecidaException extends NegocioException{
    private static final long serialVersionUID = 1L;

    public FormaPagamentoNaoReconhecidaException(String mensagem) {
        super(mensagem);
    }

    public FormaPagamentoNaoReconhecidaException() {

        this("Forma de pagamento não reconhecida");
    }
}
