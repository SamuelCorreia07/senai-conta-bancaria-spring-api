package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.ContaMesmoTipoException;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final PasswordEncoder encoder;

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ClienteResponseDTO registrarClienteOuAnexarConta(ClienteRegistroDTO dto) {

        var clienteExistente = repository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> repository.save(dto.toEntity())
        );
        clienteExistente.setSenha(encoder.encode(dto.senha()));
        var contas = clienteExistente.getContas();
        var novaConta = dto.contaDTO().toEntity(clienteExistente);

        boolean contaTipoExistente = clienteExistente.validarContaExistente(novaConta);

        if (contaTipoExistente) throw new ContaMesmoTipoException(dto.cpf(), novaConta.getClass().getSimpleName());

        clienteExistente.getContas().add(novaConta);

        return ClienteResponseDTO.fromEntity(repository.save(clienteExistente));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ClienteResponseDTO> listarClientesAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ClienteResponseDTO listarClienteAtivoPorCpf(String cpf) {
        var cliente = getClienteAtivoPorCpf(cpf);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ClienteResponseDTO atualizarCliente(String cpf, ClienteRegistroDTO dto) {
        var cliente = getClienteAtivoPorCpf(cpf);

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setSenha(encoder.encode(dto.senha()));
        return ClienteResponseDTO.fromEntity(repository.save(cliente));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deletarCliente(String cpf) {
        var cliente = getClienteAtivoPorCpf(cpf);
        cliente.setAtivo(false);
        cliente.getContas().forEach(conta -> conta.setAtiva(false));
        repository.save(cliente);
    }

    private Cliente getClienteAtivoPorCpf(String cpf) {
        return repository.findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", "CPF", cpf));
    }
}
