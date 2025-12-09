package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.StatusAutenticacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.AutenticacaoIoTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação IoT", description = "Verificação de status da autenticação biométrica")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/autenticacao-iot")
@RequiredArgsConstructor
public class AutenticacaoStatusController {
    private final AutenticacaoIoTService service;

    @Operation(
            summary = "Verificar status da autenticação",
            description = "Verifica se o código de autenticação gerado já foi validado pelo dispositivo IoT via MQTT. O Front-end deve consultar este endpoint periodicamente (polling)."
    )
    @GetMapping("/status/{codigoId}")
    public ResponseEntity<StatusAutenticacaoDTO> verificarStatus(
            @Parameter(description = "ID do código de autenticação recebido na resposta da operação pendente")
            @PathVariable String codigoId) {

        StatusAutenticacaoDTO status = service.verificarStatus(codigoId);
        return ResponseEntity.ok(status);
    }
}
