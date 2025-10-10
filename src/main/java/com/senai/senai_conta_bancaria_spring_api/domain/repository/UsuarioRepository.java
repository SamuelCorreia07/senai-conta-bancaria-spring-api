package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.modelo_autenticacao_autorizacao.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
}
