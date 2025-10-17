package com.senai.senai_conta_bancaria_spring_api.application.dto;

public record AuthDTO() {
    public record LoginRequest(
            String email,
            String senha
    ) {}
    public record TokenResponse(
            String token
    ) {}
}
