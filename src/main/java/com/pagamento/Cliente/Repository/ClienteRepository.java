package com.pagamento.Cliente.Repository;

import com.pagamento.Cliente.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository <Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE upper(c.cpf) = upper(:cpf)")
    Cliente findByCpf(@Param("cpf") String cpf);

    @Query("SELECT c FROM Cliente c WHERE upper(c.cpf) = upper(:cpf)")
    Boolean existeCPF(@Param("cpf") String cpf);    

    @Query("SELECT c FROM Cliente c WHERE upper(c.email) = upper(:email)")
    Cliente findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Cliente c WHERE upper(c.nome) like concat('%', upper(:nome), '%')")
    Cliente findByNome(@Param("nome") String nome);
 
}
