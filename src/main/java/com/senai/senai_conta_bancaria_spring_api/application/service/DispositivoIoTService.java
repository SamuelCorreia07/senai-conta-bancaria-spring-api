package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.DispositivoRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.DispositivoResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.DispositivoIoT;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.DispositivoJaVinculadoException;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.ClienteRepository;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.DispositivoIoTRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DispositivoIoTService {

    private final DispositivoIoTRepository dispositivoIoTRepository;
    private final ClienteRepository clienteRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public DispositivoResponseDTO vincularDispositivo(DispositivoRegistroDTO dto) {
        Cliente cliente = clienteRepository.findByCpfAndAtivoTrue(dto.cpfCliente())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", "CPF", dto.cpfCliente()));

        if (dispositivoIoTRepository.existsByClienteCpf(dto.cpfCliente())) {
            throw new DispositivoJaVinculadoException(dto.cpfCliente());
        }

        DispositivoIoT novoDispositivo = DispositivoIoT.builder()
                .codigoSerial(dto.codigoSerial())
                .chavePublica(dto.chavePublica())
                .ativo(true) // Definimos como ativo no vínculo
                .cliente(cliente)
                .build();

        DispositivoIoT dispositivoSalvo = dispositivoIoTRepository.save(novoDispositivo);
        return DispositivoResponseDTO.fromEntity(dispositivoSalvo);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public DispositivoResponseDTO buscarPorClienteCpf(String cpf) {
        DispositivoIoT dispositivo = dispositivoIoTRepository.findByClienteCpf(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("DispositivoIoT", "CPF do cliente", cpf));
        return DispositivoResponseDTO.fromEntity(dispositivo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public DispositivoResponseDTO ativarDesativarDispositivo(String cpf, boolean ativar) {
        DispositivoIoT dispositivo = dispositivoIoTRepository.findByClienteCpf(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("DispositivoIoT", "CPF do cliente", cpf));

        dispositivo.setAtivo(ativar);
        DispositivoIoT dispositivoSalvo = dispositivoIoTRepository.save(dispositivo);
        return DispositivoResponseDTO.fromEntity(dispositivoSalvo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void desvincularDispositivo(String cpf) {
        DispositivoIoT dispositivo = dispositivoIoTRepository.findByClienteCpf(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("DispositivoIoT", "CPF do cliente", cpf));

        dispositivoIoTRepository.delete(dispositivo);
    }
}