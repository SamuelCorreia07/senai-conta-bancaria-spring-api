package com.senai.senai_conta_bancaria_spring_api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CORRENTE")
@Data
@SuperBuilder
@NoArgsConstructor
public class ContaCorrente extends Conta{
    @Column(precision = 20, scale = 2)
    private BigDecimal limite = new BigDecimal("500.00");
    @Column(precision = 10, scale = 4)
    private BigDecimal taxa = new BigDecimal("0.05");

    @Override
    public String getTipo() {
        return "CORRENTE";
    }

    @Override
    public void sacar(BigDecimal valor) {
        validarValorMaiorQueZero(valor);
        BigDecimal custoSaque = valor.multiply(taxa != null ? taxa : BigDecimal.ZERO);
        BigDecimal totalSaque = valor.add(custoSaque);

        if (this.getSaldo().add(limite != null ? limite : BigDecimal.ZERO).compareTo(totalSaque) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para saque, considerando o limite.");
        }
        this.setSaldo(this.getSaldo().subtract(valor));
    }
}
