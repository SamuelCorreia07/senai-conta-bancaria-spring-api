package com.senai.senai_conta_bancaria_spring_api.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoRequestDTO(
        @NotBlank(message = "O número da conta de origem é obrigatório")
        String numeroDaContaOrigem,

        @NotBlank(message = "O código do boleto é obrigatório")
        String boleto,

        @NotNull(message = "O valor a pagar é obrigatório")
        @Positive(message = "O valor do pagamento deve ser positivo")
        BigDecimal valorPago,

        // Lista de IDs das taxas que devem ser aplicadas
        @NotEmpty(message = "Pelo menos uma taxa deve ser informada (mesmo que seja uma taxa 'zero')")
        List<String> taxaIds
) {
}