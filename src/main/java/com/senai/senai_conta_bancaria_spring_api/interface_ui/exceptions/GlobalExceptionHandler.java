package com.senai.senai_conta_bancaria_spring_api.interface_ui.exceptions;

import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValorNegativoException.class)
    public ProblemDetail handleValorNegativo(ValorNegativoException ex,
                                               HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Valores negativos não são permitidos.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
                                                             HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.NOT_FOUND,
                "Entidade não encontrada.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(RendimentoInvalidoException.class)
    public ProblemDetail handleRendimentoInvalidoException(RendimentoInvalidoException ex,
                                                           HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Rendimento inválido.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ProblemDetail handleSaldoInsuficienteException(SaldoInsuficienteException ex,
                                                          HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Saldo insuficiente.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TipoDeContaInvalidaException.class)
    public ProblemDetail handleTipoDeContaInvalidaException(TipoDeContaInvalidaException ex,
                                                            HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Tipo de conta inválido.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TransferenciaParaMesmaContaException.class)
    public ProblemDetail handleTransferenciaParaMesmaContaException(TransferenciaParaMesmaContaException ex,
                                                                    HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Transferência inválida.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(ContaMesmoTipoException.class)
    public ProblemDetail handleContaMesmoTipoException(ContaMesmoTipoException ex,
                                                                HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Conta do mesmo tipo já existe para este cliente.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex,
                                                HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação.",
                "Um ou mais campos estão inválidos. Verifique os detalhes.",
                request.getRequestURI()
        );
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(),
                        error.getDefaultMessage())
        );
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ProblemDetail handleConversionFailedException(ConversionFailedException ex,
                                                         HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de conversão.",
                "Falha ao converter o valor fornecido. Verifique os detalhes.",
                request.getRequestURI()
        );
        problem.setProperty("errors", ex.getMessage());
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex,
                                                            HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação nos parâmetros.",
                "Um ou mais parâmetros estão inválidos.",
                request.getRequestURI()
        );
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(path, message);
        });
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ProblemDetail handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Usuário não encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Método não permitido",
                String.format("O método %s não é suportado para esta rota. Métodos suportados: %s",
                        ex.getMethod(),
                        String.join(", ", ex.getSupportedMethods() != null ? ex.getSupportedMethods() : new String[]{})),
                request.getRequestURI()
        );
    }
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Não autenticado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex,
                                                HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no servidor.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }
}
