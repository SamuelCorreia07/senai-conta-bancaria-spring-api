package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TaxaDTO(
        String id,
        @NotBlank
        String descricao,
        @NotNull
        @PositiveOrZero
        BigDecimal percentual,
        @NotNull
        @PositiveOrZero
        BigDecimal valorFixo,
        boolean ativo
) {
    public static TaxaDTO fromEntity(Taxa taxa) {
        return new TaxaDTO(
                taxa.getId(),
                taxa.getDescricao(),
                taxa.getPercentual(),
                taxa.getValorFixo(),
                taxa.isAtivo()
        );
    }

    public Taxa toEntity() {
        return Taxa.builder()
                .descricao(this.descricao)
                .percentual(this.percentual)
                .valorFixo(this.valorFixo)
                .ativo(true)
                .build();
    }
}
