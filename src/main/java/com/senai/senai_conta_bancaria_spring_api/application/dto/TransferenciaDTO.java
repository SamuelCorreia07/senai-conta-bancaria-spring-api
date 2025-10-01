package com.senai.senai_conta_bancaria_spring_api.application.dto;

import java.math.BigDecimal;

public record TransferenciaDTO(
        String numeroContaDestino,
        BigDecimal valor
) {
}
