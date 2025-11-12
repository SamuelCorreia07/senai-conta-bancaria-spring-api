package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.DispositivoIoT;

public record DispositivoResponseDTO(
        String id,
        String codigoSerial,
        boolean ativo,
        String cpfCliente
) {
    public static DispositivoResponseDTO fromEntity(DispositivoIoT dispositivo) {
        return new DispositivoResponseDTO(
                dispositivo.getId(),
                dispositivo.getCodigoSerial(),
                dispositivo.isAtivo(),
                dispositivo.getCliente().getCpf()
        );
    }
}
