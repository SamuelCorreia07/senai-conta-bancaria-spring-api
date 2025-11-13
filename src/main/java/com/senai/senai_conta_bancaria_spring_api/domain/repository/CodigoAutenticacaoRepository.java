package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.CodigoAutenticacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CodigoAutenticacaoRepository extends JpaRepository<CodigoAutenticacao, String> {
    /**
     * Busca o primeiro código (mais recente) que pertence ao cliente,
     * corresponde ao código enviado, ainda não foi validado,
     * e não está expirado.
     */
    Optional<CodigoAutenticacao> findFirstByClienteIdAndCodigoAndValidadoFalseAndExpiraEmAfterOrderByExpiraEmDesc(
            String clienteId, String codigo, LocalDateTime agora);

    /**
     * Busca o primeiro código (mais recente) que pertence ao cliente,
     * que JÁ FOI VALIDADO (validado = true),
     * e que ainda não expirou.
     */
    Optional<CodigoAutenticacao> findFirstByClienteIdAndValidadoTrueAndExpiraEmAfterOrderByExpiraEmDesc(
            String clienteId, LocalDateTime agora);
}
