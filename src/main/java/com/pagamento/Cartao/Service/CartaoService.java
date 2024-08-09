package com.pagamento.Cartao.Service;

import com.pagamento.Cartao.DTO.CartaoDTO;
import com.pagamento.Cartao.Model.Cartao;
import com.pagamento.Cartao.Repository.CartaoRepository;
import com.pagamento.Exception.ServiceException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartaoService {

    private  final CartaoRepository CartaoRepository;

    public CartaoService(CartaoRepository CartaoRepository){
        this.CartaoRepository = CartaoRepository;
    }

    public CartaoDTO criarCartao(@Valid Cartao cartaoDTO) throws ServiceException {
        Cartao cartao = toEntity(cartaoDTO);

        try {
            cartao = CartaoRepository.save(cartao);
        } catch (Exception e) {
            throw new ServiceException("Erro ao inserir o Cartao: " + e.getMessage());
        }
        return toDTO(cartao);
    }

    public List<Cartao> buscarCartao() throws ServiceException {
        List<Cartao> Cartao = CartaoRepository.findAll();
        if (Cartao.isEmpty()) {
            throw new ServiceException("Nenhum cartão encontrado.");
        }
        return Cartao;
    }

    public List<Cartao> buscarCartaoPorNumero(String numero) throws ServiceException {
        List<Cartao> Cartao = (List<Cartao>) CartaoRepository.findByNumero(numero);
        if (Cartao.isEmpty()) {
            throw new ServiceException("Cartão não encontrado para este numero : " + numero);
        }
        return Cartao;
    }


    public CartaoDTO toDTO(Cartao cartao) {
        return new CartaoDTO(
                cartao.getNumero(),
                cartao.getCpf(),
                cartao.getLimite(),
                cartao.getData_validade(),
                cartao.getCvv());
    }

    public Cartao toEntity(Cartao cartaoDTO) {
        Cartao cartao = new Cartao();
        cartao.setNumero(cartaoDTO.getNumero());
        cartao.setCpf(cartaoDTO.getCpf());
        cartao.setLimite(cartaoDTO.getLimite());
        cartao.setData_validade(cartaoDTO.getData_validade());
        cartao.setCvv(cartaoDTO.getCvv());

        return cartao;
    }

}
