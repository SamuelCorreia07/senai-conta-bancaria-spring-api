package com.senai.senai_conta_bancaria_spring_api.domain.repository;

import com.senai.senai_conta_bancaria_spring_api.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
    // método para buscar pagamentos pelo email do cliente da conta
    @Query("SELECT p FROM Pagamento p JOIN p.conta c JOIN c.cliente cl WHERE cl.email = :email")
    List<Pagamento> findAllByClienteEmail(String email);
}
