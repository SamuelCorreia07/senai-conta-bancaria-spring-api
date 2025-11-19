package com.senai.senai_conta_bancaria_spring_api.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO para o payload enviado ao tópico MQTT "banco/autenticacao/iniciar".
 * Baseado no MensagemMqttDTO do teste_mqtt.
 */
@Builder
public record AutenticacaoIoTPayloadDTO(
        @NotBlank
        String idCliente,
        @NotBlank
        String mensagem,
        @NotBlank
        String codigoId
) {
}