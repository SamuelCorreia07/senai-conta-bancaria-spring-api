package com.senai.senai_conta_bancaria_spring_api.domain.enums;

public enum StatusPagamento {
    SUCESSO,
    FALHA_SALDO_INSUFICIENTE,
    FALHA_BOLETO_VENCIDO,
    FALHA_CONTA_INATIVA,
    FALHA_TAXA_NAO_ENCONTRADA,
    PENDENTE_AUTENTICACAO,
    FALHA_AUTENTICACAO
}
