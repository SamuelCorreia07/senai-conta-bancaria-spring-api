package com.senai.senai_conta_bancaria_spring_api.domain.enums;

public enum StatusPagamento {
    SUCESSO,
    FALHA,
    SALDO_INSUFICIENTE,
    PENDENTE_AUTENTICACAO,
    FALHA_AUTENTICACAO,
    FALHA_AUTENTICACAO_EXPIRADA
}
