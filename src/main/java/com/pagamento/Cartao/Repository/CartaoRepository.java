package com.pagamento.Cartao.Repository;

import com.pagamento.Cartao.Model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartaoRepository extends JpaRepository<Cartao, String> {
    @Query("select count(numero) from Cartao where cpf = ?1")

    int countCartaoByCpf(String cpf);

    @Query("SELECT c FROM Cartao c WHERE upper(c.numero) like concat('%', upper(:numero), '%')")
    Cartao findByNumero(@Param("numero") String numero);

    @Query("SELECT c FROM Cliente c WHERE upper(c.cpf) = upper(:cpf)")
    List<Cartao> findByCpf(@Param("cpf") String cpf);
}