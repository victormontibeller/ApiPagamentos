package com.pagamento.Usuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pagamento.Usuario.Model.Usuario;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByUsername(String username);
}

