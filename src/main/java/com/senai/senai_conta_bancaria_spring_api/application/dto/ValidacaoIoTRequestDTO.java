package com.senai.senai_conta_bancaria_spring_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO para o payload recebido do tópico MQTT "banco/autenticacao/validar".
 * Baseado no MensagemMqttDTO do teste_mqtt.
 */
@Builder
@Schema(description = "Payload recebido do dispositivo IoT para validação")
public record ValidacaoIoTRequestDTO(
        @Schema(description = "ID do cliente que está validando", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotBlank
        String idCliente,

        @Schema(description = "Código de autenticação retornado pelo dispositivo", example = "X9Y8Z7")
        @NotBlank
        String codigo
) {
}