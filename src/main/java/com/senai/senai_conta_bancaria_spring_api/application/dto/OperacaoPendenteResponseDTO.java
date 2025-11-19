package com.senai.senai_conta_bancaria_spring_api.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Resposta da API REST informando que a autenticação foi iniciada.
 */
public record OperacaoPendenteResponseDTO(
        @NotBlank
        String mensagem,
        @NotBlank
        String codigoId
) {
}