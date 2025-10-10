package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaCorrente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaPoupanca;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.TipoDeContaInvalidaException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaResumoDTO(
        @NotBlank
        String numeroDaConta,
        @NotBlank
        String tipo,
        @NotNull
        BigDecimal saldo
) {
    public Conta toEntity(Cliente cliente) {
        if ("CORRENTE".equalsIgnoreCase(tipo)) {
            return ContaCorrente.builder()
                    .numeroDaConta(this.numeroDaConta)
                    .saldo(this.saldo)
                    .ativa(true)
                    .cliente(cliente)
                    .limite(new BigDecimal("500.00"))
                    .taxa(new BigDecimal("0.05"))
                    .build();
        } else if ("POUPANCA".equalsIgnoreCase(tipo)) {
            return ContaPoupanca.builder()
                    .numeroDaConta(this.numeroDaConta)
                    .saldo(this.saldo)
                    .ativa(true)
                    .cliente(cliente)
                    .rendimento(new BigDecimal("0.01"))
                    .build();
        }
        throw new TipoDeContaInvalidaException(this.tipo);

    }

    public static ContaResumoDTO fromEntity(Conta conta) {
        return new ContaResumoDTO(
                conta.getNumeroDaConta(),
                conta.getTipo(),
                conta.getSaldo()
        );
    }
}
