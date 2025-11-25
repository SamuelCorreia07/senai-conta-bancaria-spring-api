package com.senai.senai_conta_bancaria_spring_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoRequestDTO(
        @Schema(description = "Número da conta de origem do pagamento", example = "12345-6")
        @NotBlank(message = "O número da conta de origem é obrigatório")
        String numeroDaContaOrigem,

        @Schema(description = "Código do boleto ou identificador do pagamento", example = "34191.79001.01043.510047.91020.1.50000000010000")
        @NotBlank(message = "O código do boleto é obrigatório")
        String boleto,

        @Schema(description = "Valor original do pagamento", example = "100.00")
        @NotNull(message = "O valor a pagar é obrigatório")
        @Positive(message = "O valor do pagamento deve ser positivo")
        BigDecimal valorPago,

        // Lista de IDs das taxas que devem ser aplicadas
        @Schema(description = "Lista de IDs das taxas a serem aplicadas", example = "[\"uuid-taxa-1\", \"uuid-taxa-2\"]")
        @NotEmpty(message = "Pelo menos uma taxa deve ser informada (mesmo que seja uma taxa 'zero')")
        List<String> taxaIds
) {
}