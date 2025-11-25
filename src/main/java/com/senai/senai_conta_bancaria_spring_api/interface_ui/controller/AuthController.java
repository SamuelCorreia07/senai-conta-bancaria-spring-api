package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.AuthDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Operações de autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @Operation(
            summary = "Autenticação de usuário",
            description = "Autentica um usuário com base nas credenciais fornecidas e retorna um token JWT.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthDTO.LoginRequest.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo de login", value = """
                                        {
                                          "email": "usuario@email.com",
                                          "senha": "senha_usuario"
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário autenticado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDTO.TokenResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciais inválidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthDTO.TokenResponse> login(@org.springframework.web.bind.annotation.RequestBody AuthDTO.LoginRequest req) {
        System.out.println("Recebendo requisição de login para o email: " + req.email());
        String token = auth.login(req);
        return ResponseEntity.ok(new AuthDTO.TokenResponse(token));
    }
}
