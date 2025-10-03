package com.senai.senai_conta_bancaria_spring_api.domain.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade, String atributo, String identificador) {
        super(entidade + " com " + atributo + " " + identificador + " não encontrado(a) ou inativo(a).");
    }
}
