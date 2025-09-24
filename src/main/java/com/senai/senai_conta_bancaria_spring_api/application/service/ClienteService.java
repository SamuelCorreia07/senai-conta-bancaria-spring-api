package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteResponseDTO registrarClienteOuAnexarConta(ClienteRegistroDTO dto) {

        var clienteExistente = repository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> repository.save(dto.toEntity())
        );
        var contas = clienteExistente.getContas();
        var novaConta = dto.contaDTO().toEntity(clienteExistente);

        boolean contaTipoExistente = contas.stream()
                .anyMatch(conta -> conta.getClass().equals(novaConta.getClass()) && conta.isAtiva());

        if (contaTipoExistente) throw new IllegalArgumentException("O cliente já possui uma conta ativa do tipo: " + novaConta.getClass().getSimpleName());

        clienteExistente.getContas().add(novaConta);

        return ClienteResponseDTO.fromEntity(repository.save(clienteExistente));
    }

    public List<ClienteResponseDTO> listarClientesAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }
}
