package com.senai.senai_conta_bancaria_spring_api.interface_ui;

import com.senai.senai_conta_bancaria_spring_api.application.dto.ContaResumoDTO;
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
                                                         @RequestBody ContaResumoDTO dto) {
        return ResponseEntity.ok(service.atualizarConta(numeroDaConta, dto));
    }
}
