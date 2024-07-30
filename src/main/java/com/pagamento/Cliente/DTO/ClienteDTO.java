package com.pagamento.Cliente.DTO;

import com.pagamento.Cliente.Model.Endereco;

import java.time.LocalDate;

public record ClienteDTO(
    long id,
    String nome,
    String email,
    String cpf,
    LocalDate nascimento,
    Endereco endereco) {
}
