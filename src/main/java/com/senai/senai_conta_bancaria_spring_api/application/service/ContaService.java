package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaAtualizacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaResumoDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaCorrente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.ContaPoupanca;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {

    private final ContaRepository repository;

    @Transactional(readOnly = true)
    public List<ContaResumoDTO> listarContasAtivas() {
        return repository.findAllByAtivaTrue().stream()
                .map(ContaResumoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaResumoDTO buscarContaPorNumero(String numeroDaConta) {
        return repository.findByNumeroContaAndAtivaTrue(numeroDaConta)
                .map(ContaResumoDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Conta com número " + numeroDaConta + " não encontrada ou inativa."));
    }

    public ContaResumoDTO atualizarConta(String numeroDaConta, ContaAtualizacaoDTO dto) {
        var conta = repository.findByNumeroContaAndAtivaTrue(numeroDaConta)
                .orElseThrow(() -> new IllegalArgumentException("Conta com número " + numeroDaConta + " não encontrada ou inativa."));

        conta.setSaldo(dto.saldo());
        if (conta instanceof ContaPoupanca poupanca) {
            poupanca.setRendimento(dto.rendimento());
        } else if (conta instanceof ContaCorrente corrente) {
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        }

        return ContaResumoDTO.fromEntity(repository.save(conta));
    }
}
