package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resposta da API REST informando que a autenticação foi iniciada.
 */
public record OperacaoPendenteResponseDTO(
        @JsonProperty("mensagem")
        String mensagem,

        @JsonProperty("codigoId")
        String codigoId
) {
}