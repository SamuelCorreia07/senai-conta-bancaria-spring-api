package com.senai.senai_conta_bancaria_spring_api.application.dto;

import java.util.List;

public record ClienteResponseDTO(
        String id,
        String nome,
        String cpf,
        List<ContaResumoDTO> contas
) {
}
