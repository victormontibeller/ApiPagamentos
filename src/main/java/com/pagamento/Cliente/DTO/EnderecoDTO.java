package com.pagamento.Cliente.DTO;

public  record EnderecoDTO(
        long id,
        String rua,
        String numero,
        String cep,
        String complemento
) {
}
