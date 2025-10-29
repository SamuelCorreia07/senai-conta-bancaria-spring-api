package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ClienteResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Cliente", description = "Operações relacionadas a clientes")
@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @Operation(
            summary = "Registrar um novo cliente ou anexar uma conta a um cliente existente",
            description = "Registra um novo cliente no sistema ou anexa uma nova conta a um cliente existente com base no CPF fornecido.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                          "nome": "João da Silva",
                                          "cpf": "123.456.789-00",
                                          "email": "joao@email.com",
                                          "senha": "senha_segura",
                                            "contaDTO": {
                                                "numeroDaConta": "00012345-6",
                                                "tipo": "CORRENTE",
                                                "saldo": 1000.00
                                            }
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "CPF inválido",
                                                    summary = "CPF fornecido não está no formato correto",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/invalid-cpf",
                                                              "title": "CPF inválido",
                                                              "status": 400,
                                                              "detail": "O CPF fornecido não é válido.",
                                                              "instance": "/api/cliente"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Dados obrigatórios ausentes",
                                                    summary = "Campos obrigatórios estão faltando na solicitação",
                                                    value = """
                                                            {
                                                              "type": "https://example.com/missing-fields",
                                                              "title": "Dados obrigatórios ausentes",
                                                              "status": 400,
                                                              "detail": "Os seguintes campos são obrigatórios: nome, cpf, rendaMensal.",
                                                              "instance": "/api/cliente"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRegistroDTO dto) {
        ClienteResponseDTO novoCliente = service.registrarClienteOuAnexarConta(dto);
        return ResponseEntity.created(
                URI.create("/api/cliente/cpf/" + novoCliente.cpf())
        ).body(novoCliente);
    }

    @Operation(
            summary = "Listar todos os clientes ativos",
            description = "Retorna uma lista de todos os clientes que estão ativos no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos() {
        return ResponseEntity.ok(service.listarClientesAtivos());
    }

    @Operation(
            summary = "Obter detalhes de um cliente ativo por CPF",
            description = "Retorna os detalhes de um cliente ativo com base no CPF fornecido.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser buscado", required = true, schema = @Schema(type = "string") )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes do cliente retornados com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "type": "https://example.com/cliente-not-found",
                                                      "title": "Cliente não encontrado",
                                                      "status": 404,
                                                      "detail": "Nenhum cliente encontrado com o CPF fornecido.",
                                                      "instance": "/api/cliente/cpf/{cpf}"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping ("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> listarClienteAtivoPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.listarClienteAtivoPorCpf(cpf));
    }

    @Operation(
            summary = "Atualizar os dados de um cliente por CPF",
            description = "Atualiza as informações de um cliente existente com base no CPF fornecido.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser atualizado", required = true, schema = @Schema(type = "string") )
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                            {
                                            "nome": "João da Silva Atualizado",
                                            "cpf": "123.456.789-00",
                                            "email": "joaosilva@email.com",
                                            "senha": "nova_senha_segura",
                                            }
                                        """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "type": "https://example.com/cliente-not-found",
                                                      "title": "Cliente não encontrado",
                                                      "status": 404,
                                                      "detail": "Nenhum cliente encontrado com o CPF fornecido.",
                                                      "instance": "/api/cliente/cpf/{cpf}"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Erro de validação")
            }
    )
    @PutMapping ("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable String cpf,
                                                                @Valid @RequestBody ClienteRegistroDTO dto) {
        return ResponseEntity.ok(service.atualizarCliente(cpf, dto));
    }

    @Operation(
            summary = "Deletar um cliente por CPF",
            description = "Remove um cliente do sistema com base no CPF fornecido.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser deletado", required = true, schema = @Schema(type = "string") )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "type": "https://example.com/cliente-not-found",
                                                      "title": "Cliente não encontrado",
                                                      "status": 404,
                                                      "detail": "Nenhum cliente encontrado com o CPF fornecido.",
                                                      "instance": "/api/cliente/cpf/{cpf}"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping ("/cpf/{cpf}")
    public ResponseEntity<Void> deletarCliente(@Valid @PathVariable String cpf) {
        service.deletarCliente(cpf);
        return ResponseEntity.noContent().build();
    }
}
