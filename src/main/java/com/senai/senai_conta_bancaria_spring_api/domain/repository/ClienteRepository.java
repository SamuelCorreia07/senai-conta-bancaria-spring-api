package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByCpfAndAtivoTrue(String cpf);
}
