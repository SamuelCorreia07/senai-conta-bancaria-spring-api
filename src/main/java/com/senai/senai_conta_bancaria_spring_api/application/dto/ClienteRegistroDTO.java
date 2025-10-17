package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public record ClienteRegistroDTO(
        @NotBlank
        String nome,
        @NotBlank
        String cpf,
        @NotBlank
        String email,
        @NotBlank
        String senha,
        ContaResumoDTO contaDTO
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .contas(new ArrayList<Conta>())
                .role(Role.CLIENTE)
                .build();
    }
}
