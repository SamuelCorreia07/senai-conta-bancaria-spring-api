package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.AuthDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.TokenResponse> login(@RequestBody AuthDTO.LoginRequest req) {
        String token = auth.login(req);
        return ResponseEntity.ok(new AuthDTO.TokenResponse(token));
    }
}
