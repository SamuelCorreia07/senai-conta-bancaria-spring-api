package com.senai.senai_conta_bancaria_spring_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaAtualizacaoDTO(
        @Schema(description = "Saldo atual da conta", example = "1500.75")
        @NotNull
        BigDecimal saldo,
        @Schema(description = "Rendimento atual da conta", example = "2.5")
        BigDecimal rendimento,
        @Schema(description = "Limite de crédito da conta", example = "500.00")
        BigDecimal limite,
        BigDecimal taxa
) {

}
