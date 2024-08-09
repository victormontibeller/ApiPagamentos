package com.pagamento.Cartao.DTO;

import java.math.BigDecimal;

public record CartaoDTO(
        String numero,
        String cpf,
        BigDecimal limite,
        String data_validade,
        String cvv
){

}
