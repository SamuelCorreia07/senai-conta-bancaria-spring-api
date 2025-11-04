package com.senai.senai_conta_bancaria_spring_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "taxa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Taxa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 100, unique = true)
    private String descricao;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal percentual = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal valorFixo = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToMany(mappedBy = "taxas")
    private Set<Pagamento> pagamentos;
}
