package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRegistroDTO dto) {
        ClienteResponseDTO novoCliente = service.registrarClienteOuAnexarConta(dto);
        return ResponseEntity.created(
                URI.create("/api/cliente/cpf/" + novoCliente.cpf())
        ).body(novoCliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos() {
        return ResponseEntity.ok(service.listarClientesAtivos());
    }

    @GetMapping ("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> listarClienteAtivoPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.listarClienteAtivoPorCpf(cpf));
    }

    @PutMapping ("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> atuializarCliente(@PathVariable String cpf,
                                                                @Valid @RequestBody ClienteRegistroDTO dto) {
        return ResponseEntity.ok(service.atualizarCliente(cpf, dto));
    }

    @DeleteMapping ("/cpf/{cpf}")
    public ResponseEntity<Void> deletarCliente(@Valid @PathVariable String cpf) {
        service.deletarCliente(cpf);
        return ResponseEntity.noContent().build();
    }
}
