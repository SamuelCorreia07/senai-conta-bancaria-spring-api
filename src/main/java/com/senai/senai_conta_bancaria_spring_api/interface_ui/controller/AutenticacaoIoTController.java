package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPayload;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttSubscriber;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ValidacaoIoTRequestDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.AutenticacaoIoTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * Controller dedicado a ouvir tópicos MQTT de resposta (Subscriber).
 * Segue o padrão do MensagemController.
 * Usamos @Controller em vez de @RestController, pois ele não retorna @ResponseBody.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoIoTController {

    private final AutenticacaoIoTService autenticacaoIoTService;

    // Ouve o tópico de validação do dispositivo IoT.
    @Operation(
            summary = "Ouvir tópico MQTT de validação IoT",
            description = "Ouve o tópico MQTT 'banco/autenticacao/validar' para processar mensagens de validação de dispositivos IoT."
    )
    @MqttSubscriber("banco/autenticacao/validar")
    public void ouvirValidacao(@MqttPayload ValidacaoIoTRequestDTO dto) {
        log.info("Mensagem MQTT recebida no tópico 'banco/autenticacao/validar'. Payload: {}", dto);
        autenticacaoIoTService.validarCodigo(dto);
    }
}
