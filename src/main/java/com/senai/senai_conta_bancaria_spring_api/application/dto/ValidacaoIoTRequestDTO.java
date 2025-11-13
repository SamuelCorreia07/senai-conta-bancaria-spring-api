package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * DTO para o payload recebido do tópico MQTT "banco/autenticacao/validar".
 * Baseado no MensagemMqttDTO do teste_mqtt.
 */
@Builder
public record ValidacaoIoTRequestDTO(
        @NotBlank
        @JsonProperty("idCliente")
        String idCliente,

        @NotBlank
        @JsonProperty("codigo")
        String codigo
) {
}