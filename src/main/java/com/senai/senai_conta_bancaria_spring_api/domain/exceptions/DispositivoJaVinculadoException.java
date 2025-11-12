package com.senai.senai_conta_bancaria_spring_api.domain.exceptions;

public class DispositivoJaVinculadoException extends RuntimeException {
    public DispositivoJaVinculadoException(String cpf) {
        super("O cliente com CPF " + cpf + " já possui um dispositivo IoT vinculado.");
    }
}
