package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

public record ClienteRegistroDTO(
        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        @NotBlank
        String nome,

        @Schema(description = "CPF do cliente (com pontuação)", example = "123.456.789-00")
        @NotBlank
        String cpf,

        @Schema(description = "E-mail para contato e login", example = "joao@email.com")
        @NotBlank
        String email,

        @Schema(description = "Senha de acesso", example = "senhaSegura123")
        @NotBlank
        String senha,

        @Schema(description = "Dados da conta inicial a ser criada")
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
