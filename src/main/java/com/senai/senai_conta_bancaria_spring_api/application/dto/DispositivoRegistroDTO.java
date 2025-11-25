package com.senai.senai_conta_bancaria_spring_api.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DispositivoRegistroDTO(
        @Schema(description = "CPF do cliente proprietário do dispositivo", example = "123.456.789-00")
        @NotBlank(message = "O CPF do cliente é obrigatório")
        String cpfCliente,

        @Schema(description = "Código serial único do dispositivo físico", example = "SN-987654321")
        @NotBlank(message = "O código serial do dispositivo é obrigatório")
        String codigoSerial,

        @Schema(description = "Chave pública para validação de assinatura", example = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz...")
        @NotBlank(message = "A chave pública do dispositivo é obrigatória")
        String chavePublica
) {
}
