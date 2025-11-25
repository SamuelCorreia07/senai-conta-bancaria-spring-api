package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.TaxaRegistroDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.TaxaResponseDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.TaxaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Taxas", description = "Operações relacionadas ao gerenciamento de taxas financeiras (Apenas ADMIN)")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/taxas")
@RequiredArgsConstructor
public class TaxaController {

    private final TaxaService service;

    @Operation(
            summary = "Registrar uma nova taxa",
            description = "Registra uma nova taxa no sistema (Ex: IOF, Tarifa Bancária). Requer permissão de ADMIN.",
            requestBody = @RequestBody(
                    description = "Dados da nova taxa a ser registrada",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaxaRegistroDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Taxa registrada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados da taxa"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @PostMapping
    public ResponseEntity<TaxaResponseDTO> registrarTaxa(@Valid @org.springframework.web.bind.annotation.RequestBody TaxaRegistroDTO dto) {
        TaxaResponseDTO novaTaxa = service.registrarTaxa(dto);
        return ResponseEntity.created(
                URI.create("/api/taxas/" + novaTaxa.id())
        ).body(novaTaxa);
    }

    @Operation(
            summary = "Listar todas as taxas",
            description = "Retorna uma lista de todas as taxas cadastradas no sistema. Requer permissão de ADMIN.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de taxas",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaxaResponseDTO.class)))
                    ),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @GetMapping
    public ResponseEntity<List<TaxaResponseDTO>> listarTaxas() {
        return ResponseEntity.ok(service.listarTaxas());
    }

    @Operation(
            summary = "Obter detalhes de uma taxa por ID",
            description = "Retorna os detalhes de uma taxa específica com base no ID. Requer permissão de ADMIN.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser buscada", required = true, schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes da taxa retornados com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Taxa não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = """
                                                    {
                                                      "type": "https://example.com/taxa-not-found",
                                                      "title": "Entidade não encontrada.",
                                                      "status": 404,
                                                      "detail": "Taxa com ID 12345 não encontrado(a) ou inativo(a).",
                                                      "instance": "/api/taxas/12345"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaxaResponseDTO> buscarTaxaPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarTaxaPorId(id));
    }

    @Operation(
            summary = "Atualizar os dados de uma taxa por ID",
            description = "Atualiza as informações de uma taxa existente com base no ID. Requer permissão de ADMIN.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser atualizada", required = true, schema = @Schema(type = "string"))
            },
            requestBody = @RequestBody (
                    description = "Dados atualizados da taxa",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaxaRegistroDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Taxa atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Taxa não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaxaResponseDTO> atualizarTaxa(@PathVariable String id,
                                                         @Valid @org.springframework.web.bind.annotation.RequestBody TaxaRegistroDTO dto) {
        return ResponseEntity.ok(service.atualizarTaxa(id, dto));
    }

    @Operation(
            summary = "Deletar uma taxa por ID",
            description = "Remove uma taxa do sistema com base no ID. Requer permissão de ADMIN.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser deletada", required = true, schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Taxa deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Taxa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado (requer ADMIN)")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTaxa(@PathVariable String id) {
        service.deletarTaxa(id);
        return ResponseEntity.noContent().build();
    }
}