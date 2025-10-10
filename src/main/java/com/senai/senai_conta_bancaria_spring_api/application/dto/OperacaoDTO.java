package com.senai.senai_conta_bancaria_spring_api.application.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OperacaoDTO(
        @NotNull
        BigDecimal valor
) {
}
