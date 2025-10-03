package com.senai.senai_conta_bancaria_spring_api.domain.exceptions;

public class RendimentoInvalidoException extends RuntimeException {
    public RendimentoInvalidoException() {
        super("Rendimento deve ser apenas para conta poupança.");
    }
}
