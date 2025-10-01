package com.senai.senai_conta_bancaria_spring_api.domain.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table(name = "conta",
uniqueConstraints = {
        @UniqueConstraint(name = "uk_conta_numeroDaConta", columnNames = "numeroDaConta"),
        @UniqueConstraint(name = "uk_conta_cliente", columnNames = {"cliente_id", "tipo_conta"})
        }
)
@SuperBuilder
@NoArgsConstructor
public abstract class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 20)
    private String numeroDaConta;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    private boolean ativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", foreignKey = @ForeignKey(name = "fk_conta_cliente"))
    private Cliente cliente;

    public abstract String getTipo();

    public void sacar(BigDecimal valor) {
        validarValorMaiorQueZero(valor);
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para saque.");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        validarValorMaiorQueZero(valor);
        this.saldo = this.saldo.add(valor);
    }

    public void transferir(Conta contaDestino, BigDecimal valor) {
        validarValorMaiorQueZero(valor);
        if (this.id.equals(contaDestino.getId())) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta.");
        }
        this.sacar(valor);
        contaDestino.depositar(valor);
    }

    protected static void validarValorMaiorQueZero(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da operação deve ser maior que zero.");
        }
    }
}
