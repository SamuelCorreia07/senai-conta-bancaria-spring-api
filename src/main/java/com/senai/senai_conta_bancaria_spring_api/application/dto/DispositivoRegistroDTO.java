package com.senai.senai_conta_bancaria_spring_api.application.dto;

import jakarta.validation.constraints.NotBlank;

public record DispositivoRegistroDTO(
        @NotBlank(message = "O CPF do cliente é obrigatório")
        String cpfCliente,

        @NotBlank(message = "O código serial do dispositivo é obrigatório")
        String codigoSerial,

        @NotBlank(message = "A chave pública do dispositivo é obrigatória")
        String chavePublica
) {
}
