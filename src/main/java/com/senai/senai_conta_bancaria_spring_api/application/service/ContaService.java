package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.*;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaCorrente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaPoupanca;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.RendimentoInvalidoException;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {

    private final ContaRepository repository;
    private final AutenticacaoIoTService autenticacaoIoTService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public List<ContaResumoDTO> listarContasAtivas() {
        return repository.findAllByAtivaTrue().stream()
                .map(ContaResumoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ContaResumoDTO buscarContaPorNumero(String numeroDaConta) {
        return repository.findByNumeroDaContaAndAtivaTrue(numeroDaConta)
                .map(ContaResumoDTO::fromEntity)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta", "número", numeroDaConta));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ContaResumoDTO atualizarConta(String numeroDaConta, ContaAtualizacaoDTO dto) {
        var conta = getContaAtivaPorNumero(numeroDaConta);

        conta.setSaldo(dto.saldo());
        if (conta instanceof ContaPoupanca poupanca) {
            poupanca.setRendimento(dto.rendimento());
        } else if (conta instanceof ContaCorrente corrente) {
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        }

        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deletarConta(String numeroDaConta) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        conta.setAtiva(false);
        repository.save(conta);
    }

    public AutenticacaoIoTPayloadDTO iniciarSaque(String numeroDaConta, OperacaoDTO dto) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        Cliente cliente = conta.getCliente();

        return autenticacaoIoTService.iniciarAutenticacao(cliente);
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO confirmarSaque(String numeroDaConta, OperacaoDTO dto) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        autenticacaoIoTService.verificarEValidarCodigo(conta.getCliente());
        conta.sacar(dto.valor());
        Conta contaSalva = repository.save(conta);

        return ContaResumoDTO.fromEntity(contaSalva);
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO depositar(String numeroDaConta, OperacaoDTO dto) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        conta.depositar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    public AutenticacaoIoTPayloadDTO iniciarTransferencia(String numeroDaContaOrigem, TransferenciaDTO dto) {
        var contaOrigem = getContaAtivaPorNumero(numeroDaContaOrigem);
        getContaAtivaPorNumero(dto.numeroContaDestino());
        Cliente cliente = contaOrigem.getCliente();
        return autenticacaoIoTService.iniciarAutenticacao(cliente);
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO confirmarTransferencia(String numeroDaContaOrigem, TransferenciaDTO dto) {
        var contaOrigem = getContaAtivaPorNumero(numeroDaContaOrigem);
        var contaDestino = getContaAtivaPorNumero(dto.numeroContaDestino());

        autenticacaoIoTService.verificarEValidarCodigo(contaOrigem.getCliente());

        contaOrigem.transferir(contaDestino, dto.valor());

        repository.save(contaDestino);
        Conta contaOrigemSalva = repository.save(contaOrigem);

        return ContaResumoDTO.fromEntity(contaOrigemSalva);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ContaResumoDTO aplicarRendimento(String numeroDaConta) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        if (conta instanceof ContaPoupanca poupanca) {
            poupanca.aplicarRendimento();
            return ContaResumoDTO.fromEntity(repository.save(poupanca));
        }
        throw new RendimentoInvalidoException();
    }

    private Conta getContaAtivaPorNumero(String numeroDaConta) {
        return repository.findByNumeroDaContaAndAtivaTrue(numeroDaConta)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta", "número", numeroDaConta));
    }
}
