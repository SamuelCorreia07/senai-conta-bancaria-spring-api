package com.senai.senai_conta_bancaria_spring_api.domain.exceptions;

public class TipoDeContaInvalidaException extends RuntimeException {
    public TipoDeContaInvalidaException(String tipoConta) {
        super("Tipo de conta inválida: " + tipoConta + ". Os tipos válidos são: 'corrente' ou 'poupanca'.");
    }
}
