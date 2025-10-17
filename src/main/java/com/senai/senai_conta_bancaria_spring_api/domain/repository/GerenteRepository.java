package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, String> {
    Optional<Gerente> findByEmail(String email);
}
