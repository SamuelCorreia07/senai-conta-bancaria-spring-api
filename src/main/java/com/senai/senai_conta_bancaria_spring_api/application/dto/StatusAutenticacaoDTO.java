package com.senai.senai_conta_bancaria_spring_api.application.dto;

import com.senai.senai_conta_bancaria_spring_api.domain.enums.StatusAutenticacao;

public record StatusAutenticacaoDTO(
        StatusAutenticacao status
) {
}
