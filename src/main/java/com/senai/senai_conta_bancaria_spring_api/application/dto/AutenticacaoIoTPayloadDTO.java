package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * DTO para o payload enviado ao tópico MQTT "banco/autenticacao/iniciar".
 * Baseado no MensagemMqttDTO do teste_mqtt.
 */
@Builder
public record AutenticacaoIoTPayloadDTO(
        @JsonProperty("idCliente")
        String idCliente,

        @JsonProperty("mensagem")
        String mensagem,

        @JsonProperty("codigoId")
        String codigoId
) {
}