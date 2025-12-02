package com.senai.senai_conta_bancaria_spring_api.application.service;

import com.senai.senai_conta_bancaria_spring_api.application.dto.AutenticacaoIoTPayloadDTO;
import com.senai.senai_conta_bancaria_spring_api.application.dto.ValidacaoIoTRequestDTO;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.Cliente;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.CodigoAutenticacao;
import com.senai.senai_conta_bancaria_spring_api.domain.entity.DispositivoIoT;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.AutenticacaoIoTException;
import com.senai.senai_conta_bancaria_spring_api.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.CodigoAutenticacaoRepository;
import com.senai.senai_conta_bancaria_spring_api.domain.repository.DispositivoIoTRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoIoTService {

    private final DispositivoIoTRepository dispositivoIoTRepository;
    private final CodigoAutenticacaoRepository codigoAutenticacaoRepository;

    @Transactional
    public AutenticacaoIoTPayloadDTO iniciarAutenticacao(Cliente cliente) {
        log.info("Iniciando autenticação para o cliente: {}", cliente.getEmail());

        DispositivoIoT dispositivo = dispositivoIoTRepository.findByClienteCpf(cliente.getCpf())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("DispositivoIoT", "CPF do cliente", cliente.getCpf()));

        if (!dispositivo.isAtivo()) {
            throw new AutenticacaoIoTException("O dispositivo IoT deste cliente não está ativo.");
        }

        CodigoAutenticacao codigoAuth = CodigoAutenticacao.builder()
                .cliente(cliente)
                .codigo(null)
                .expiraEm(null)
                .validado(false)
                .build();

        codigoAuth.setCodigo(codigoAuth.gerarCodigo());
        codigoAuth.setExpiraEm(codigoAuth.gerarExpiracao());

        codigoAutenticacaoRepository.save(codigoAuth);
        log.info("Código de autenticação salvo (ID: {})", codigoAuth.getId());

        return AutenticacaoIoTPayloadDTO.builder()
                .idCliente(cliente.getId())
                .mensagem("Por favor, autentique a operação pendente.")
                .codigoId(codigoAuth.getId())
                .build();
    }

    @Transactional
    public void validarCodigo(ValidacaoIoTRequestDTO dto) {
        log.info("Processando validação para Cliente ID: {}", dto.idCliente());

        try {
            Optional<CodigoAutenticacao> codigoOpt = codigoAutenticacaoRepository
                    .findFirstByClienteIdAndCodigoAndValidadoFalseAndExpiraEmAfterOrderByExpiraEmDesc(
                            dto.idCliente(), dto.codigo(), LocalDateTime.now()
                    );

            if (codigoOpt.isPresent()) {
                // SUCESSO: Código encontrado e válido!
                CodigoAutenticacao codigo = codigoOpt.get();
                codigo.setValidado(true);
                codigoAutenticacaoRepository.save(codigo);
                log.info("Autenticação IoT SUCESSO. Cliente ID: {}. Código: {}", dto.idCliente(), dto.codigo());
            } else {
                // FALHA: Código errado, expirado ou já usado
                log.warn("Autenticação IoT FALHA. Cliente ID: {}. Código: {}. Motivo: Não encontrado, expirado ou já validado.", dto.idCliente(), dto.codigo());
            }

        } catch (Exception e) {
            log.error("Erro ao processar validação IoT para Cliente ID [{}]: {}", dto.idCliente(), e.getMessage());
        }
    }

    /**
     * Verifica se existe um código validado recentemente para o cliente
     * e o "consome" (deleta) para que não possa ser usado novamente.
     * Lança exceção se não for encontrado.
     */
    @Transactional
    public void verificarEValidarCodigo(Cliente cliente) {
        log.info("Verificando código validado para o cliente: {}", cliente.getId());

        Optional<CodigoAutenticacao> codigoOpt = codigoAutenticacaoRepository
                .findFirstByClienteIdAndValidadoTrueAndExpiraEmAfterOrderByExpiraEmDesc(
                        cliente.getId(), LocalDateTime.now()
                );

        if (codigoOpt.isPresent()) {
            // SUCESSO: Código encontrado e validado.
            // Consumimos o código para evitar reuso.
            CodigoAutenticacao codigo = codigoOpt.get();
            codigoAutenticacaoRepository.delete(codigo);
            log.info("Código de autenticação (ID: {}) verificado e consumido com sucesso.", codigo.getId());
        } else {
            // FALHA: Nenhum código válido encontrado
            log.warn("Tentativa de confirmação falhou. Nenhum código validado encontrado para o cliente: {}", cliente.getId());
            throw new AutenticacaoIoTException("Autenticação IoT não encontrada, inválida ou expirada.");
        }
    }
}