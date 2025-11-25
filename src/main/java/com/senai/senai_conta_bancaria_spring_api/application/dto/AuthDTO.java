package com.senai.senai_conta_bancaria_spring_api.application.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class AuthDTO {
    @Schema(description = "Dados para autenticação de usuário")
    public record LoginRequest(
            @Schema(description = "E-mail do usuário", example = "exemplo@email.com")
            @NotBlank
            String email,
            @Schema(description = "Senha do usuário", example = "exemplo123")
            @NotBlank
            String senha
    ) {}
    public record TokenResponse(
            @Schema(description = "Token JWT Bearer", example = "eyJhbGciOiJIUzI1NiJ9...")
            String token
    ) {}
}
