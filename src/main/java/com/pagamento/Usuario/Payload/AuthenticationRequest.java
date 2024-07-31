package com.pagamento.Usuario.Payload;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String usuario;
    private String senha;

}