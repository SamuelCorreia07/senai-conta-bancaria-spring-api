package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.AutenticacaoIoTPayloadDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.PagamentoRequestDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.PagamentoResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoAppService {

    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final TaxaRepository taxaRepository;
    private final PagamentoDomainService domainService;
    private final AutenticacaoIoTService autenticacaoIoTService;

    @Transactional
    @PreAuthorize("hasRole('CLIENTE')")
    public AutenticacaoIoTPayloadDTO iniciarPagamento(PagamentoRequestDTO dto, String emailAutenticado) {
        var conta = getContaAtivaPorNumero(dto.numeroDaContaOrigem());

        // Validação de segurança
        if (!conta.getCliente().getEmail().equals(emailAutenticado)) {
            throw new AccessDeniedException("O usuário autenticado não tem permissão para usar esta conta.");
        }

        Cliente cliente = conta.getCliente();

        return autenticacaoIoTService.iniciarAutenticacao(cliente);
    }

    //Confirma e executa o pagamento após a validação do IoT.
    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDTO confirmarPagamento(PagamentoRequestDTO dto, String emailAutenticado) {
        var conta = getContaAtivaPorNumero(dto.numeroDaContaOrigem());

        if (!conta.getCliente().getEmail().equals(emailAutenticado)) {
            throw new AccessDeniedException("O usuário autenticado não tem permissão para usar esta conta.");
        }

        autenticacaoIoTService.verificarEValidarCodigo(conta.getCliente());

        return this.realizarPagamento(dto, emailAutenticado);
    }

    // Usamos 'NOT_SUPPORTED' pois vamos controlar a transação manualmente
    // para garantir que o pagamento seja salvo mesmo em caso de falha
    @Transactional(noRollbackFor = {SaldoInsuficienteException.class, PagamentoInvalidoException.class})
    @PreAuthorize("hasRole('CLIENTE')") // Segurança a nível de método
    public PagamentoResponseDTO realizarPagamento(PagamentoRequestDTO dto, String emailAutenticado) {

        var conta = getContaAtivaPorNumero(dto.numeroDaContaOrigem());

        // Validação de segurança
        if (!conta.getCliente().getEmail().equals(emailAutenticado)) {
            throw new AccessDeniedException("O usuário autenticado não tem permissão para usar esta conta.");
        }

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
            if(status == StatusPagamento.FALHA_SALDO_INSUFICIENTE) throw new SaldoInsuficienteException();
            throw new PagamentoInvalidoException("Pagamento falhou. Motivo: " + status.name());
        }

        return PagamentoResponseDTO.fromEntity(pagamentoSalvo);
    }

    // Lista todos os pagamentos (sucesso ou falha) associados ao usuário autenticado.
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public List<PagamentoResponseDTO> listarPagamentosDoUsuario(String emailAutenticado) {
        return pagamentoRepository.findAllByClienteEmail(emailAutenticado).stream()
                .map(PagamentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private Conta getContaAtivaPorNumero(String numeroDaConta) {
        return contaRepository.findByNumeroDaContaAndAtivaTrue(numeroDaConta)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta", "número", numeroDaConta));
    }
}