package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Pagamento;
import com.senai.senai_conta_bancaria_spring_api.domain.enums.StatusPagamento;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PagamentoResponseDTO(
        String id,
        String numeroDaConta,
        String boleto,
        BigDecimal valorPago,
        BigDecimal valorTotalCobrado,
        LocalDateTime dataPagamento,
        StatusPagamento status,
        List<TaxaResponseDTO> taxasAplicadas
) {
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        List<TaxaResponseDTO> taxasDTO = pagamento.getTaxas().stream()
                .map(TaxaResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getConta().getNumeroDaConta(),
                pagamento.getBoleto(),
                pagamento.getValorPago(),
                pagamento.getValorTotalCobrado(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                taxasDTO
        );
    }
}