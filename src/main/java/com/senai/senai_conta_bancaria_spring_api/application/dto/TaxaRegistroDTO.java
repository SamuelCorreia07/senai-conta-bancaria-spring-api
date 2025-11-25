package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TaxaRegistroDTO(
        @Schema(description = "Descrição da taxa", example = "IOF")
        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @Schema(description = "Percentual da taxa (0.05 = 5%)", example = "0.05")
        @NotNull(message = "O percentual é obrigatório")
        @PositiveOrZero(message = "O percentual não pode ser negativo")
        BigDecimal percentual,

        @Schema(description = "Valor fixo adicional da taxa", example = "2.50")
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
