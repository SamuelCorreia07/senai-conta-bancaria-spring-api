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
@DiscriminatorValue("CORRENTEE")
@Data
@SuperBuilder
@NoArgsConstructor
public class ContaCorrente extends Conta{
    @Column(precision = 20, scale = 2)
    private BigDecimal limite;
    @Column(precision = 10, scale = 4)
    private BigDecimal taxa;

    @Override
    public String getTipo() {
        return "CORRENTE";
    }
}
