package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import com.senai.senai_conta_bancaria_spring_api.domain.enums.TipoPagamento;

import java.math.BigDecimal;

public record TaxaResponseDTO(
        String id,
        String descricao,
        BigDecimal percentual,
        BigDecimal valorFixo,
        TipoPagamento tipoPagamento
) {
    public static TaxaResponseDTO fromEntity(Taxa taxa) {
        return new TaxaResponseDTO(
                taxa.getId(),
                taxa.getDescricao(),
                taxa.getPercentual(),
                taxa.getValorFixo(),
                taxa.getTipoPagamento()
        );
    }
}