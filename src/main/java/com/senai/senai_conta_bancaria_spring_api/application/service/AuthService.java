package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.AuthDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.UsuarioNaoEncontradoException;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Usuario;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.UsuarioRepository;
import com.senai.senai_conta_bancaria_spring_api.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public String login(AuthDTO.LoginRequest req) {
        System.out.println("Autenticando usuário: " + req.email());
        Usuario usuario = usuarios.findByEmail(req.email())
                .orElseThrow(() ->  new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!encoder.matches(req.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}


