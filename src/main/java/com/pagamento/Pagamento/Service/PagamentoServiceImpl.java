package com.pagamento.Pagamento.Service;

import com.pagamento.Cartao.Service.CartaoService;
import com.pagamento.Exception.ServiceException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Repository.ClienteRepository;
import com.pagamento.Exception.LimiteCartaoException;
import com.pagamento.Exception.MessageNotFoundException;
import com.pagamento.Pagamento.DTO.CartaoDto;
import com.pagamento.Pagamento.DTO.PagamentoPorClienteDto;
import com.pagamento.Pagamento.Model.Pagamento;
import com.pagamento.Pagamento.Repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ClienteRepository clienteRepository;
    private final CartaoService cartaoService;

    @Override
    public Pagamento cadastrarPagamento(Pagamento pagamento)  throws ServiceException {

       Cliente cliente = clienteRepository.findByCpf(pagamento.getCpf());
       if (cliente == null) {
           throw new MessageNotFoundException("Cliente não encontrado para este CPF: " + pagamento.getCpf());
       }

       CartaoDto cartaoDto = (CartaoDto) cartaoService.buscarCartaoPorNumero(pagamento.getNumero());

        if (cartaoDto == null) {
            throw new MessageNotFoundException("Cartão inexistente");
        } else if (!cartaoDto.getCpf().equals(pagamento.getCpf())) {
            throw new MessageNotFoundException("Cartão não pertence a esse cliente");
        } else if (cartaoDto.getLimite().compareTo(pagamento.getValor()) < 0) {
            throw new LimiteCartaoException("Limite insuficiente para a compra");
        } else if (!cartaoDto.getCvv().equals(pagamento.getCvv())) {
            throw new MessageNotFoundException("Código CVV incorreto, compra recusada");
        }

        return pagamentoRepository.save(pagamento);
    }

    @Override
    public List<PagamentoPorClienteDto> listaPagamentosPorCliente(String cpf) {
        List<PagamentoPorClienteDto> retorno = new ArrayList<>();
        PagamentoPorClienteDto pagamentoDto = null;

        List<Pagamento> pagamentos = pagamentoRepository.findAllByCpf(cpf);

        for (Pagamento p : pagamentos) {
            pagamentoDto = new PagamentoPorClienteDto();
            pagamentoDto.setValor(p.getValor());
            pagamentoDto.setMetodoPagamento("Cartão de crédito");
            pagamentoDto.setDescricao("Compra");
            pagamentoDto.setStatus("Aprovado");

            retorno.add(pagamentoDto);
        }

        return retorno;
    }

}
