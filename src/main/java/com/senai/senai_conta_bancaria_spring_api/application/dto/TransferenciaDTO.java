package com.senai.senai_conta_bancaria_spring_api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferenciaDTO(
        @NotBlank
        String numeroContaDestino,
        @NotNull
        BigDecimal valor
) {
}
