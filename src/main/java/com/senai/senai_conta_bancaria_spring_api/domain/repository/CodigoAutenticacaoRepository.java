package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.CodigoAutenticacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodigoAutenticacaoRepository extends JpaRepository<CodigoAutenticacao, String> {
}
