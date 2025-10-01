package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaCorrente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaPoupanca;

import java.math.BigDecimal;

public record ContaResumoDTO(
        String numeroDaConta,
        String tipo,
        BigDecimal saldo
) {
    public Conta toEntity(Cliente cliente) {
        if ("CORRENTE".equalsIgnoreCase(tipo)) {
            return ContaCorrente.builder()
                    .numeroDaConta(this.numeroDaConta)
                    .saldo(this.saldo)
                    .ativa(true)
                    .cliente(cliente)
                    .build();
        } else if ("POUPANCA".equalsIgnoreCase(tipo)) {
            return ContaPoupanca.builder()
                    .numeroDaConta(this.numeroDaConta)
                    .saldo(this.saldo)
                    .ativa(true)
                    .cliente(cliente)
                    .build();
        } else {
            throw new IllegalArgumentException("Tipo de conta inválido: " + this.tipo);
        }
    }

    public static ContaResumoDTO fromEntity(Conta conta) {
        return new ContaResumoDTO(
                conta.getNumeroDaConta(),
                conta.getTipo(),
                conta.getSaldo()
        );
    }
}
