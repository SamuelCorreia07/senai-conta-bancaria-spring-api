package com.senai.senai_conta_bancaria_spring_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferenciaDTO(
        @Schema(description = "Número da conta de destino", example = "98765-4")
        @NotBlank
        String numeroContaDestino,

        @Schema(description = "Valor a ser transferido", example = "250.00")
        @NotNull
        BigDecimal valor
) {
}
