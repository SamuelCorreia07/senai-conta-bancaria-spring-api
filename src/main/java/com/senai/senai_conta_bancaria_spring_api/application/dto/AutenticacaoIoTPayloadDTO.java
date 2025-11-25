package com.senai.senai_conta_bancaria_spring_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO para o payload enviado ao tópico MQTT "banco/autenticacao/iniciar".
 * Baseado no MensagemMqttDTO do teste_mqtt.
 */
@Builder
@Schema(description = "Payload enviado para o dispositivo IoT via MQTT para solicitar autenticação")
public record AutenticacaoIoTPayloadDTO(
        @Schema(description = "ID interno do cliente", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotBlank
        String idCliente,
        @Schema(description = "Mensagem de instrução para o usuário", example = "Por favor, autentique a operação pendente.")
        @NotBlank
        String mensagem,
        @Schema(description = "ID do código de autenticação gerado", example = "a1b2c3d4-...")
        @NotBlank
        String codigoId
) {
}