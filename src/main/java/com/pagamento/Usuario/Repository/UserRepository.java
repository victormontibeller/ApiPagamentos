package com.pagamento.Usuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagamento.Usuario.Model.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}

