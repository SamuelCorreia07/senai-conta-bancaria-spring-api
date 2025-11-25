package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.DispositivoRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.DispositivoResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.DispositivoIoTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Dispositivos IoT", description = "Operações para gerenciamento de dispositivos de autenticação IoT (Apenas ADMIN)")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/dispositivos-iot")
@RequiredArgsConstructor
public class DispositivoIoTController {

    private final DispositivoIoTService service;

    @Operation(
            summary = "Vincular dispositivo a um cliente",
            description = "Registra um novo dispositivo IoT e o vincula a um cliente existente. Requer permissão de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dispositivo vinculado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Cliente já possui dispositivo ou dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
            }
    )
    @PostMapping("/vincular")
    public ResponseEntity<DispositivoResponseDTO> vincularDispositivo(@Valid @org.springframework.web.bind.annotation.RequestBody DispositivoRegistroDTO dto) {
        DispositivoResponseDTO response = service.vincularDispositivo(dto);
        return ResponseEntity.created(
                URI.create("/api/dispositivos-iot/cliente/" + response.cpfCliente())
        ).body(response);
    }

    @Operation(
            summary = "Buscar dispositivo por CPF do cliente",
            description = "Retorna os detalhes do dispositivo vinculado a um cliente. Requer permissão de ADMIN.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dispositivo encontrado",
                            content = @Content(schema = @Schema(implementation = DispositivoResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Nenhum dispositivo encontrado para este cliente")
            }
    )
    @GetMapping("/cliente/{cpf}")
    public ResponseEntity<DispositivoResponseDTO> buscarPorClienteCpf(@Parameter(description = "CPF do cliente") @PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarPorClienteCpf(cpf));
    }

    @Operation(
            summary = "Ativar/Desativar dispositivo",
            description = "Muda o status de um dispositivo (ativo/inativo) pelo CPF do cliente. Requer permissão de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status do dispositivo alterado"),
                    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
            }
    )
    @PutMapping("/cliente/{cpf}/status")
    public ResponseEntity<DispositivoResponseDTO> ativarDesativarDispositivo(
            @Parameter(description = "CPF do cliente") @PathVariable String cpf,
            @Parameter(description = "Status desejado (true para ativar, false para desativar)") @RequestParam boolean ativar) {

        DispositivoResponseDTO response = service.ativarDesativarDispositivo(cpf, ativar);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Desvincular dispositivo",
            description = "Remove o vínculo de um dispositivo com um cliente (exclui o dispositivo). Requer permissão de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Dispositivo desvinculado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
            }
    )
    @DeleteMapping("/cliente/{cpf}/desvincular")
    public ResponseEntity<Void> desvincularDispositivo(@Parameter(description = "CPF do cliente") @PathVariable String cpf) {
        service.desvincularDispositivo(cpf);
        return ResponseEntity.noContent().build();
    }
}