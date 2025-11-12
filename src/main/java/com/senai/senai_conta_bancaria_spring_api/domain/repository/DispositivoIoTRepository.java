package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.DispositivoIoT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DispositivoIoTRepository extends JpaRepository<DispositivoIoT, String> {
    Optional<DispositivoIoT> findByClienteCpf(String cpf);
    boolean existsByClienteCpf(String cpf);
}
