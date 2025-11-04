package com.senai.senai_conta_bancaria_spring_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dispositivo_iot")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoIoT {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String codigoSerial;

    @Column(nullable = false, length = 1024) // Chave pública pode ser longa
    private String chavePublica;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_dispositivo_cliente"))
    private Cliente cliente;
}
