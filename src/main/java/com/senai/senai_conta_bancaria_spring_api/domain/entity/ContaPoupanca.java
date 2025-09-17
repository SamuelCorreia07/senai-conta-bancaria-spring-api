package com.senai.senai_conta_bancaria_spring_api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CONTA_POUPANCA")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContaPoupanca extends Conta{

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal rendimento;
}
