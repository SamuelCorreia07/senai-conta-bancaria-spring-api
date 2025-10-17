package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

public record ClienteResponseDTO(
        @NotBlank
        String id,
        @NotBlank
        String nome,
        @CPF
        String cpf,
        @NotBlank
        String email,
        @NotBlank
        String senha,
        List<ContaResumoDTO> contas
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        List<ContaResumoDTO> contasDTO = cliente.getContas().stream()
                .map(ContaResumoDTO::fromEntity)
                .toList();

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getSenha(),
                contasDTO
        );
    }
}
