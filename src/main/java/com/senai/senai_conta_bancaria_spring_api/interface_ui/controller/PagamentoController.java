package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPublisher;
import com.senai.senai_conta_bancaria_spring_api.application.dto.AutenticacaoIoTPayloadDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.PagamentoRequestDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.PagamentoResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.PagamentoAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Pagamentos", description = "Operações relacionadas a pagamentos de boletos e contas")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoAppService service;

    @Operation(
            summary = "Realizar um pagamento",
            description = "Processa um pagamento de boleto debitando o valor (principal + taxas) da conta do cliente. Requer permissão de CLIENTE.",
            requestBody = @RequestBody(
                    description = "Dados do pagamento a ser realizado",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PagamentoRequestDTO.class),
                            examples = @ExampleObject(name = "Pagamento Exemplo",
                                    value = """
                                              {
                                                "numeroDaContaOrigem": "1234567890",
                                                "boleto": "34191.79001 01043.510047 91020.150008 8 76210000050000",
                                                "valorPago": 500.00,
                                                "taxaIds": ["taxa1", "taxa2"]
                                              }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pagamento realizado com SUCESSO"),
                    @ApiResponse(responseCode = "400", description = "Falha no pagamento (Ex: Saldo insuficiente, Boleto vencido, Dados inválidos)",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(name = "Saldo Insuficiente",
                                            value = """
                                                      {
                                                        "type": "https://example.com/probs/saldo-insuficiente",
                                                        "title": "Saldo insuficiente.",
                                                        "status": 400,
                                                        "detail": "Saldo insuficiente para realizar a operação.",
                                                        "instance": "/api/pagamentos"
                                                      }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Conta não pertence ao usuário)"),
                    @ApiResponse(responseCode = "404", description = "Conta ou Taxa não encontrada")
            }
    )
    @PostMapping
    @MqttPublisher("banco/autenticacao/iniciar")
    public ResponseEntity<AutenticacaoIoTPayloadDTO> realizarPagamento(
            @Valid @RequestBody PagamentoRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Passamos o email (username) do usuário autenticado para o service
        String emailAutenticado = userDetails.getUsername();
        AutenticacaoIoTPayloadDTO payload = service.iniciarPagamento(dto, emailAutenticado);

        return ResponseEntity.ok(payload);
    }

    @Operation(
            summary = "Confirmar Pagamento",
            description = "Confirma e executa o pagamento após a validação biométrica.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento confirmado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Falha na confirmação do pagamento (Ex: Autenticação IoT inválida ou expirada)",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(name = "Autenticação IoT Inválida",
                                            value = """
                                                      {
                                                        "type": "https://example.com/probs/autenticacao-iot-invalida",
                                                        "title": "Autenticação IoT inválida ou expirada.",
                                                        "status": 400,
                                                        "detail": "O código de autenticação IoT fornecido é inválido ou expirou.",
                                                        "instance": "/api/pagamentos/confirmar"
                                                      }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (Conta não pertence ao usuário)"),
                    @ApiResponse(responseCode = "404", description = "Conta ou Taxa não encontrada")
            }
    )
    @PostMapping("/confirmar")
    public ResponseEntity<PagamentoResponseDTO> confirmarPagamento(
            @Valid @RequestBody PagamentoRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailAutenticado = userDetails.getUsername();

        PagamentoResponseDTO response = service.confirmarPagamento(dto, emailAutenticado);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar meus pagamentos",
            description = "Retorna uma lista de todos os pagamentos (com sucesso ou falha) associados à conta do cliente autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de pagamentos retornada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @GetMapping("/meus-pagamentos")
    public ResponseEntity<List<PagamentoResponseDTO>> listarMeusPagamentos(
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailAutenticado = userDetails.getUsername();
        List<PagamentoResponseDTO> pagamentos = service.listarPagamentosDoUsuario(emailAutenticado);
        return ResponseEntity.ok(pagamentos);
    }
}