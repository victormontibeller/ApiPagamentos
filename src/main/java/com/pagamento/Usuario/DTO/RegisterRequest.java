package com.pagamento.Usuario.DTO;

import com.pagamento.Cliente.Model.Cliente;

public record RegisterRequest(String username, String password, Cliente cliente) { }
