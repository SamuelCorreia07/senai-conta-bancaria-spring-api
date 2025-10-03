package com.senai.senai_conta_bancaria_spring_api.domain.exceptions;

public class ValorNegativoException extends RuntimeException {
    public ValorNegativoException(String operacao) {
        super("Não é possível realizar a operação de " + operacao + " com valor negativo.");
    }
}
