package com.estudos.desafio_itau.exception;

public class TransacaoInvalidaException extends RuntimeException {

    public TransacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
