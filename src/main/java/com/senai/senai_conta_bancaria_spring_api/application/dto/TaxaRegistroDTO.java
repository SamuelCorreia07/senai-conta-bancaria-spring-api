package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TaxaRegistroDTO(
        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "O percentual é obrigatório")
        @PositiveOrZero(message = "O percentual não pode ser negativo")
        BigDecimal percentual,

        @NotNull(message = "O valor fixo é obrigatório")
        @PositiveOrZero(message = "O valor fixo não pode ser negativo")
        BigDecimal valorFixo
) {
    public Taxa toEntity() {
        return Taxa.builder()
                .descricao(this.descricao)
                .percentual(this.percentual)
                .valorFixo(this.valorFixo)
                .build();
    }
}
