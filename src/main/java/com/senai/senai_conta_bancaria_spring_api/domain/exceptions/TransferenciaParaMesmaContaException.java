package com.senai.senai_conta_bancaria_spring_api.domain.exceptions;

public class TransferenciaParaMesmaContaException extends RuntimeException {
    public TransferenciaParaMesmaContaException() {
        super("Não é possível realizar uma transferência para a mesma conta.");
    }
}
