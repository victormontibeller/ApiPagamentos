package com.pagamento.Pagamento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoPorClienteDto {

    private BigDecimal valor;
    private String descricao;
    private String metodoPagamento;
    private String status;

}