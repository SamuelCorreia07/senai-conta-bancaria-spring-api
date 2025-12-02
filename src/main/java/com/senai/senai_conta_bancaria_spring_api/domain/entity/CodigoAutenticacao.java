package com.senai.senai_conta_bancaria_spring_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "codigo_autenticacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodigoAutenticacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private LocalDateTime expiraEm;

    @Column(nullable = false)
    private boolean validado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_codigo_cliente"))
    private Cliente cliente;

    private static final long EXPIRACAO_CODIGO_MINUTOS = 3;

    public String gerarCodigo() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public LocalDateTime gerarExpiracao() {
        return LocalDateTime.now().plusMinutes(EXPIRACAO_CODIGO_MINUTOS);
    }
}
