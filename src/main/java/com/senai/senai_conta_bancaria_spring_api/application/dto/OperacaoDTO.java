package com.senai.senai_conta_bancaria_spring_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OperacaoDTO(
        @Schema(description = "Valor da operação monetária", example = "100.50")
        @NotNull
        BigDecimal valor
) {
}
