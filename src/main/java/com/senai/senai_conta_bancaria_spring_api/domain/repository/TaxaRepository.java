package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Taxa;
import com.senai.senai_conta_bancaria_spring_api.domain.enums.TipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaxaRepository extends JpaRepository<Taxa, String> {
    List<Taxa> findByTipoPagamentoAndAtivoTrue(TipoPagamento tipo);
}
