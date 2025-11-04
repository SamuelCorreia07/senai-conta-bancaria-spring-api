package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.PagamentoRequestDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.PagamentoResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Pagamento;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import com.senai.senai_conta_bancaria_spring_api.domain.enums.StatusPagamento;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.PagamentoInvalidoException;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.SaldoInsuficienteException;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.TaxaInvalidaException;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.ContaRepository;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.PagamentoRepository;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.TaxaRepository;
import com.senai.senai_conta_bancaria_spring_api.domain.service.PagamentoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PagamentoAppService {

    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;
    private final PagamentoDomainService domainService;

    // Usamos 'NOT_SUPPORTED' pois vamos controlar a transação manualmente
    // para garantir que o pagamento seja salvo mesmo em caso de falha
    @Transactional(noRollbackFor = {SaldoInsuficienteException.class, PagamentoInvalidoException.class})
    public PagamentoResponseDTO realizarPagamento(PagamentoRequestDTO dto) {

        Conta conta = contaRepository.findByNumeroDaContaAndAtivaTrue(dto.numeroDaContaOrigem())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta", "número", dto.numeroDaContaOrigem()));

        List<Taxa> taxas = taxaRepository.findAllById(dto.taxaIds());
        if (taxas.size() != dto.taxaIds().size()) {
            throw new TaxaInvalidaException("Uma ou mais taxas informadas não foram encontradas.");
        }

        BigDecimal valorFinal = domainService.calcularValorFinal(dto.valorPago(), taxas);
        StatusPagamento status;

        try {
            // Valida o boleto
            domainService.validarBoleto(dto.boleto());

            // Valida o saldo e debita o valor da conta
            conta.debitar(valorFinal);

            // Se chegou aqui, o pagamento foi sucesso
            status = StatusPagamento.SUCESSO;
            contaRepository.save(conta);

        } catch (SaldoInsuficienteException e) {
            status = StatusPagamento.FALHA_SALDO_INSUFICIENTE;
        } catch (PagamentoInvalidoException e) {
            status = StatusPagamento.FALHA_BOLETO_VENCIDO;
        }

        // Salva o registro do pagamento independentemente do status
        Pagamento pagamento = Pagamento.builder()
                .conta(conta)
                .boleto(dto.boleto())
                .valorPago(dto.valorPago())
                .valorTotalCobrado(valorFinal)
                .dataPagamento(LocalDateTime.now())
                .status(status)
                .taxas(new HashSet<>(taxas))
                .build();

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        // Se o pagamento falhou, lançamos a exceção para o controller
        // (o pagamento já foi salvo como FALHA)
        if (status != StatusPagamento.SUCESSO) {
            throw new PagamentoInvalidoException("Pagamento falhou. Motivo: " + status.name());
        }

        return PagamentoResponseDTO.fromEntity(pagamentoSalvo);
    }
}