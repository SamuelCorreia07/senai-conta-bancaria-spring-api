package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, String> {
    List<Conta> findAllByAtivaTrue();
    Optional<Conta> findByNumeroDaContaAndAtivaTrue(String numeroDaConta);
}
