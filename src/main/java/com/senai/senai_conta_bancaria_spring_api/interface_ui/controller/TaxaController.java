package com.senai.senai_conta_bancaria_spring_api.interface_ui.controller;

import com.senai.senai_conta_bancaria_spring_api.application.dto.TaxaDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.TaxaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Taxas", description = "Operações de gerenciamento de taxas (Apenas ADMIN)")
@RestController
@RequestMapping("/api/taxas")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class TaxaController {

    private final TaxaService service;

    @Operation(
            summary = "Listar todas as taxas ativas",
            description = "Retorna uma lista de todas as taxas que estão ativas no sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de taxas ativas retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaDTO.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<TaxaDTO>> listarTaxasAtivas() {
        return ResponseEntity.ok(service.listarTaxasAtivas());
    }

    @Operation(
            summary = "Buscar taxa ativa por ID",
            description = "Retorna os detalhes de uma taxa ativa específica com base no ID.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser buscada", required = true, example = "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Taxa encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Taxa não encontrada ou inativa",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = "{\"title\": \"Entidade não encontrada.\", \"detail\": \"Taxa com ID a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8 não encontrado(a) ou inativo(a).\", \"status\": 404, ...}"))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaxaDTO> buscarTaxaPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarTaxaAtivaPorId(id));
    }

    @Operation(
            summary = "Criar nova taxa",
            description = "Registra uma nova taxa no sistema. A taxa é criada como 'ativa' por padrão.",
            requestBody = @RequestBody(
                    description = "Dados da nova taxa. 'id' e 'ativo' são ignorados.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaDTO.class),
                            examples = @ExampleObject(value = "{\"descricao\": \"Taxa de Emissão de Boleto\", \"valor\": 5.50}"))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Taxa criada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = "{\"title\": \"Erro de validação.\", \"detail\": \"Um ou mais campos estão inválidos.\", \"status\": 400, \"errors\": {\"valor\": \"must be positive\"}}"))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TaxaDTO> criarTaxa(@Valid @org.springframework.web.bind.annotation.RequestBody TaxaDTO dto) {
        TaxaDTO novaTaxa = service.criarTaxa(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(novaTaxa.id()).toUri();
        return ResponseEntity.created(uri).body(novaTaxa);
    }

    @Operation(
            summary = "Atualizar taxa existente",
            description = "Atualiza a descrição e/ou o valor de uma taxa ativa existente.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser atualizada", required = true, example = "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8")
            },
            requestBody = @RequestBody(
                    description = "Novos dados da taxa. 'id' e 'ativo' são ignorados.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaDTO.class),
                            examples = @ExampleObject(value = "{\"descricao\": \"Nova Taxa de Boleto\", \"valor\": 6.00}"))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Taxa atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Taxa não encontrada ou inativa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(value = "{\"title\": \"Entidade não encontrada.\", ...}"))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(value = "{\"title\": \"Erro de validação.\", ...}")))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaxaDTO> atualizarTaxa(@PathVariable String id, @Valid @RequestBody TaxaDTO dto) {
        return ResponseEntity.ok(service.atualizarTaxa(id, dto));
    }

    @Operation(
            summary = "Desativar taxa por ID",
            description = "Realiza a exclusão lógica (soft delete) de uma taxa, tornando-a inativa. A taxa não é removida do banco.",
            parameters = {
                    @Parameter(name = "id", description = "ID da taxa a ser desativada", required = true, example = "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Taxa desativada com sucesso (Sem conteúdo)"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Taxa não encontrada ou inativa",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(value = "{\"title\": \"Entidade não encontrada.\", ...}"))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarTaxa(@PathVariable String id) {
        service.desativarTaxa(id);
        return ResponseEntity.noContent().build();
    }
}
