package com.senai.senai_conta_bancaria_spring_api.domain.service;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.PagamentoInvalidoException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PagamentoDomainService {

    /**
     * Calcula o valor final a ser debitado, somando o valor principal e as taxas.
     */
    public BigDecimal calcularValorFinal(BigDecimal valorPrincipal, List<Taxa> taxas) {
        BigDecimal totalTaxas = BigDecimal.ZERO;

        for (Taxa taxa : taxas) {
            BigDecimal taxaPercentual = valorPrincipal.multiply(taxa.getPercentual());
            totalTaxas = totalTaxas.add(taxaPercentual).add(taxa.getValorFixo());
        }

        return valorPrincipal.add(totalTaxas);
    }

    /**
     * Valida se o boleto é pagável (ex: não vencido).
     */
    public void validarBoleto(String boleto) {
        // Simulação da validação de boleto
        if (boleto.startsWith("BOLETO_VENCIDO")) {
            throw new PagamentoInvalidoException("O boleto está vencido e não pode ser pago.");
        }
    }

    /**
     * Valida se a conta tem saldo suficiente (incluindo limite) para o débito.
     */
    public void validarSaldo(Conta conta, BigDecimal valorFinal) {
        conta.debitar(valorFinal);
    }
}