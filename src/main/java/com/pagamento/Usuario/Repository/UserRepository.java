package com.pagamento.Usuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagamento.Usuario.Model.Usuario;

import java.util.List;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByUsername(String username);
}

