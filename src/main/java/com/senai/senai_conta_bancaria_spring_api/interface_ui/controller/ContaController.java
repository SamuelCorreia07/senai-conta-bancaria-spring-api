package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.*;
import com.senai.senai_conta_bancaria_spring_api.application.service.ContaService;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Conta", description = "Operações relacionadas a contas bancárias")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService service;

    @Operation(
            summary = "Listar todas as contas ativas",
            description = "Retorna uma lista de todas as contas bancárias que estão ativas no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de contas ativas retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<ContaResumoDTO>> listarContasAtivas() {
        return ResponseEntity.ok(service.listarContasAtivas());
    }

    @Operation(
            summary = "Buscar conta por número",
            description = "Retorna os detalhes de uma conta bancária específica com base no número da conta fornecido.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta a ser buscada", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes da conta retornados com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaConta}"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @GetMapping ("/{numeroDaConta}")
    public ResponseEntity<ContaResumoDTO> listarContaPorNumero(@PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.buscarContaPorNumero(numeroDaConta));
    }

    @Operation(
            summary = "Atualizar dados da conta",
            description = "Atualiza os dados de uma conta bancária específica com base no número da conta fornecido.",
            requestBody = @RequestBody(
                    description = "Dados atualizados da conta.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ContaAtualizacaoDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      
                                    }
                                    """)
                    )
            ),
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta a ser atualizada", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaConta}"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
            }
    )
    @PutMapping ("/{numeroDaConta}")
    public ResponseEntity<ContaResumoDTO> atualizarConta(@PathVariable String numeroDaConta,
                                                         @Valid @RequestBody ContaAtualizacaoDTO dto) {
        return ResponseEntity.ok(service.atualizarConta(numeroDaConta, dto));
    }

    @Operation(
            summary = "Deletar conta por número",
            description = "Remove uma conta bancária do sistema com base no número da conta fornecido.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta a ser deletada", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaConta}"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @DeleteMapping ("/{numeroDaConta}")
    public ResponseEntity<Void> deletarConta(@PathVariable String numeroDaConta) {
        service.deletarConta(numeroDaConta);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Sacar valor da conta",
            description = "Realiza um saque em uma conta bancária específica com base no número da conta fornecido.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta onde o saque será realizado", required = true)
            },
            requestBody = @RequestBody(
                    description = "Valor a ser sacado da conta.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = OperacaoDTO.class),
                            examples = @ExampleObject(value = "{\"valor\": 150.75}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaConta}/sacar"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos")
            }
    )
    @PostMapping("/{numeroDaConta}/sacar")
    @MqttPublisher("banco/autenticacao/iniciar")
    public ResponseEntity<AutenticacaoIoTPayloadDTO> sacar(@PathVariable String numeroDaConta,
                                                @Valid @RequestBody OperacaoDTO dto) {
        AutenticacaoIoTPayloadDTO payload = service.iniciarSaque(numeroDaConta, dto);
        return ResponseEntity.ok(payload);
    }

    @Operation(
            summary = "Confirmar Saque (Pós-IoT)",
            description = "Confirma e executa o saque após a validação biométrica."
    )
    @PostMapping("/{numeroDaConta}/sacar/confirmar")
    public ResponseEntity<ContaResumoDTO> confirmarSaque(@PathVariable String numeroDaConta,
                                                         @Valid @RequestBody OperacaoDTO dto) {

        ContaResumoDTO conta = service.confirmarSaque(numeroDaConta, dto);
        return ResponseEntity.ok(conta);
    }

    @Operation(
            summary = "Depositar valor na conta",
            description = "Realiza um depósito em uma conta bancária específica com base no número da conta fornecido.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta onde o depósito será realizado", required = true)
            },
            requestBody = @RequestBody(
                    description = "Valor a ser depositado na conta.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = OperacaoDTO.class),
                            examples = @ExampleObject(value = "{\"valor\": 200.00}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaConta}/depositar"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
            }
    )
    @PostMapping("/{numeroDaConta}/depositar")
    public ResponseEntity<ContaResumoDTO> depositar(@PathVariable String numeroDaConta,
                                                    @Valid @RequestBody OperacaoDTO dto) {
        return ResponseEntity.ok(service.depositar(numeroDaConta, dto));
    }

    @Operation(
            summary = "Transferir valor entre contas",
            description = "Realiza uma transferência de valor entre duas contas bancárias.",
            parameters = {
                    @Parameter(name = "numeroDaContaOrigem", description = "Número da conta de origem da transferência", required = true)
            },
            requestBody = @RequestBody(
                    description = "Detalhes da transferência, incluindo conta de destino e valor.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TransferenciaDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "numeroDaContaDestino": "1234567890",
                                      "valor": 100.00
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaContaOrigem}/transferir"
                                                            }
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos")
            }
    )
    @PostMapping("/{numeroDaContaOrigem}/transferir")
    @MqttPublisher("banco/autenticacao/iniciar")
    public ResponseEntity<AutenticacaoIoTPayloadDTO> transferir(@PathVariable String numeroDaContaOrigem,
                                                     @Valid @RequestBody TransferenciaDTO dto) {
        AutenticacaoIoTPayloadDTO payload = service.iniciarTransferencia(numeroDaContaOrigem, dto);
        return ResponseEntity.ok(payload);
    }

    @Operation(
            summary = "Confirmar Transferência (Pós-IoT)",
            description = "Confirma e executa a transferência após a validação biométrica.",
            parameters = {
                    @Parameter(name = "numeroDaContaOrigem", description = "Número da conta de origem da transferência", required = true)
            },
            requestBody = @RequestBody(
                    description = "Detalhes da transferência, incluindo conta de destino e valor.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TransferenciaDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "numeroDaContaDestino": "1234567890",
                                      "valor": 100.00
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência confirmada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaContaOrigem}/transferir/confirmar"
                                                            }
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos")
            }
    )
    @PostMapping("/{numeroDaContaOrigem}/transferir/confirmar")
    public ResponseEntity<ContaResumoDTO> confirmarTransferencia(@PathVariable String numeroDaContaOrigem,
                                                                 @Valid @RequestBody TransferenciaDTO dto) {

        ContaResumoDTO conta = service.confirmarTransferencia(numeroDaContaOrigem, dto);
        return ResponseEntity.ok(conta);
    }

    @Operation(
            summary = "Aplicar rendimento na conta",
            description = "Aplica um rendimento na conta bancária específica com base no número da conta fornecido.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta onde o rendimento será aplicado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rendimento aplicado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ContaResumoDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Conta não encontrada",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/probs/conta-nao-encontrada",
                                                              "title": "Entidade não encontrada.",
                                                              "status": 404,
                                                              "detail": "A conta com o número fornecido não foi encontrada.",
                                                              "instance": "/api/conta/{numeroDaConta}/rendimento"
                                                            }
                                                            """
                                            )
                                    }
                            )),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
            }
    )
    @PostMapping("/{numeroDaConta}/rendimento")
    public ResponseEntity<ContaResumoDTO> aplicarRendimento(@PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.aplicarRendimento(numeroDaConta));
    }
}
