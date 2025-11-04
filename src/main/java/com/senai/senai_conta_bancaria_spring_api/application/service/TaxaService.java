package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.TaxaDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxaService {

    private final TaxaRepository repository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<TaxaDTO> listarTaxasAtivas() {
        return repository.findAllByAtivoTrue().stream()
                .map(TaxaDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public TaxaDTO buscarTaxaAtivaPorId(String id) {
        return repository.findByIdAndAtivoTrue(id)
                .map(TaxaDTO::fromEntity)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Taxa", "ID", id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TaxaDTO criarTaxa(TaxaDTO dto) { // <-- RENOMEADO
        Taxa novaTaxa = dto.toEntity();
        // O DTO já garante que a taxa é criada como "ativa = true"
        Taxa taxaSalva = repository.save(novaTaxa);
        return TaxaDTO.fromEntity(taxaSalva);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TaxaDTO atualizarTaxa(String id, TaxaDTO dto) {
        Taxa taxa = getTaxaAtivaPorId(id);
        taxa.setDescricao(dto.descricao());
        taxa.setPercentual(dto.percentual());
        taxa.setValorFixo(dto.valorFixo());
        Taxa taxaSalva = repository.save(taxa);
        return TaxaDTO.fromEntity(taxaSalva);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void desativarTaxa(String id) {
        Taxa taxa = getTaxaAtivaPorId(id);
        taxa.setAtivo(false);
        repository.save(taxa);
    }

    // Método utilitário privado para evitar repetição
    private Taxa getTaxaAtivaPorId(String id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Taxa", "ID", id));
    }
}
