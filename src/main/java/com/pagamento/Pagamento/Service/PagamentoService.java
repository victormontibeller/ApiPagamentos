package com.pagamento.Pagamento.Service;

import com.pagamento.Pagamento.DTO.PagamentoPorClienteDto;
import com.pagamento.Pagamento.Model.Pagamento;
import java.util.List;

public interface PagamentoService {

    Pagamento cadastrarPagamento(Pagamento pagamento);
    List<PagamentoPorClienteDto> listaPagamentosPorCliente(String cpf);

}