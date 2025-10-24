package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaAtualizacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.OperacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaResumoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.TransferenciaDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ContaResumoDTO> sacar(@PathVariable String numeroDaConta,
                                                @Valid @RequestBody OperacaoDTO dto) {
        return ResponseEntity.ok(service.sacar(numeroDaConta, dto));
    }

    @Operation(
            summary = "Depositar valor na conta",
            description = "Realiza um depósito em uma conta bancária específica com base no número da conta fornecido.",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta onde o depósito será realizado", required = true)
            },
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
    public ResponseEntity<ContaResumoDTO> transferir(@PathVariable String numeroDaContaOrigem,
                                                     @Valid @RequestBody TransferenciaDTO dto) {
        return ResponseEntity.ok(service.transferir(numeroDaContaOrigem, dto));
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
