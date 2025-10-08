package com.senai.senai_conta_bancaria_spring_api.domain.exceptions;

public class ContaMesmoTipoException extends RuntimeException {
    public ContaMesmoTipoException(String cpf, String tipoConta) {
        super("O cliente com cpf " + cpf + " já possui uma conta ativa do tipo: " + tipoConta);
    }
}
