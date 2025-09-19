package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaCorrente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaPoupanca;

import java.math.BigDecimal;

public record ContaResumoDTO(
        String numero,
        String tipo,
        BigDecimal saldo
) {
    public Conta toEntity() {
        if ("CONTA_CORRENTE".equalsIgnoreCase(tipo)) {
            return ContaCorrente.builder()
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .build();
        } else if ("CONTA_POUPANCA".equalsIgnoreCase(tipo)) {
            return ContaPoupanca.builder()
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .build();
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido: " + this.tipo);

        }
    }
}
