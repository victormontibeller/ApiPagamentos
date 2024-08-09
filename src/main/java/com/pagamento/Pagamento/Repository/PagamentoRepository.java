package com.pagamento.Pagamento.Repository;

import com.pagamento.Pagamento.Model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    List<Pagamento> findAllByCpf(String cpf);

}