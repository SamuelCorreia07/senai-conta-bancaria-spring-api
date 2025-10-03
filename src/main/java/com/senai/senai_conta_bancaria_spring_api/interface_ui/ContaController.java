package com.senai.senai_conta_bancaria_spring_api.interface_ui;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaAtualizacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.OperacaoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaResumoDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.TransferenciaDTO;
import com.senai.senai_conta_bancaria_spring_api.application.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService service;

    @GetMapping
    public ResponseEntity<List<ContaResumoDTO>> listarContasAtivas() {
        return ResponseEntity.ok(service.listarContasAtivas());
    }

    @GetMapping ("/{numeroDaConta}")
    public ResponseEntity<ContaResumoDTO> listarContaPorNumero(@PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.buscarContaPorNumero(numeroDaConta));
    }

    @PutMapping ("/{numeroDaConta}")
    public ResponseEntity<ContaResumoDTO> atualizarConta(@PathVariable String numeroDaConta,
                                                         @RequestBody ContaAtualizacaoDTO dto) {
        return ResponseEntity.ok(service.atualizarConta(numeroDaConta, dto));
    }

    @DeleteMapping ("/{numeroDaConta}")
    public ResponseEntity<Void> deletarConta(@PathVariable String numeroDaConta) {
        service.deletarConta(numeroDaConta);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{numeroDaConta}/sacar")
    public ResponseEntity<ContaResumoDTO> sacar(@PathVariable String numeroDaConta,
                                                @RequestBody OperacaoDTO dto) {
        return ResponseEntity.ok(service.sacar(numeroDaConta, dto));
    }

    @PostMapping("/{numeroDaConta}/depositar")
    public ResponseEntity<ContaResumoDTO> depositar(@PathVariable String numeroDaConta,
                                                    @RequestBody OperacaoDTO dto) {
        return ResponseEntity.ok(service.depositar(numeroDaConta, dto));
    }

    @PostMapping("/{numeroDaContaOrigem}/transferir")
    public ResponseEntity<ContaResumoDTO> transferir(@PathVariable String numeroDaContaOrigem,
                                                     @RequestBody TransferenciaDTO dto) {
        return ResponseEntity.ok(service.transferir(numeroDaContaOrigem, dto));
    }

    @PostMapping("/{numeroDaConta}/rendimento")
    public ResponseEntity<ContaResumoDTO> aplicarRendimento(@PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.aplicarRendimento(numeroDaConta));
    }
}
