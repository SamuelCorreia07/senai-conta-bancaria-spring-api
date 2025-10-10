package com.senai.senai_conta_bancaria_spring_api.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(
        name = "cliente",
        uniqueConstraints = @UniqueConstraint(name = "uk_cliente_cpf", columnNames = "cpf")
)
public class Cliente extends Usuario{

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Conta> contas;

    public boolean validarContaExistente(Conta novaConta) {
        return contas.stream()
                .anyMatch(conta -> conta.getClass().equals(novaConta.getClass()) && conta.isAtiva());

    }
}
