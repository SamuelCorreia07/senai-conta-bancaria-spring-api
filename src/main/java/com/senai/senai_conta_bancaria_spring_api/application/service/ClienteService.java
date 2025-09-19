package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteResponseDTO registrarClienteOuAnexarConta(ClienteRegistroDTO dto) {

        var clienteExistente = repository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> repository.save(dto.toEntity())
        );
        var contas = clienteExistente.getContas();
        contas.add(dto.contaDTO().toEntity());

        return null;
    }
}
