package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaAtualizacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.OperacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaResumoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.TransferenciaDTO;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public void deletarConta(String numeroDaConta) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        conta.setAtiva(false);
        repository.save(conta);
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO sacar(String numeroDaConta, OperacaoDTO dto) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        conta.sacar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO depositar(String numeroDaConta, OperacaoDTO dto) {
        var conta = getContaAtivaPorNumero(numeroDaConta);
        conta.depositar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO transferir(String numeroDaContaOrigem, TransferenciaDTO dto) {
        var contaOrigem = getContaAtivaPorNumero(numeroDaContaOrigem);
        var contaDestino = getContaAtivaPorNumero(dto.numeroContaDestino());

        contaOrigem.transferir(contaDestino, dto.valor());

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));
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
