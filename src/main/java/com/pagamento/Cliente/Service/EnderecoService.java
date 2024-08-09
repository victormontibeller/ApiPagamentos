package com.pagamento.Cliente.Service;

import com.pagamento.Cliente.DTO.EnderecoDTO;
import com.pagamento.Exception.ServiceException;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Repository.EnderecoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    private  final EnderecoRepository EnderecoRepository;

    public EnderecoService(EnderecoRepository EnderecoRepository){
        this.EnderecoRepository = EnderecoRepository;
    }

    public EnderecoDTO criarEndereco(@Valid EnderecoDTO enderecoDTO) throws ServiceException {
        Endereco endereco = toEntity(enderecoDTO);

         try {
            endereco = EnderecoRepository.save(endereco);
        } catch (Exception e) {
            throw new ServiceException("Erro ao inserir o endereco: " + e.getMessage());
        }

        return toDTO(endereco);
    }

    public List<Endereco> buscarEnderecos() throws ServiceException {
        List<Endereco> endereco = EnderecoRepository.findAll();
        if (endereco.isEmpty()) {
            throw new ServiceException("Nenhum endereço encontrado.");
        }
        return endereco;         
    }

    public Endereco buscarEndereco(long id) throws ServiceException {
        Endereco endereco = EnderecoRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Endereço não encontrado para este id : " + id));
        return endereco;
    }

    public Endereco buscarEnderecoPorCep(String cep) throws ServiceException {
        Endereco endereco = EnderecoRepository.findByCep(cep);
        if (endereco == null) {
            throw new ServiceException("Endereço com o CEP: " + cep + " não encontrado.");
        }
        return endereco;
    }    

    public String excluirEndereco(long id) throws ServiceException {
        try {
            Endereco endereco = EnderecoRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("Endereço não encontrado para este id : " + id));
            EnderecoRepository.deleteById(endereco.getId());
        } catch (Exception e) {
            throw new ServiceException("Endereço não encontrado para este id : " + id);
        }
        return "Endereço excluído com sucesso!";
    }

    public EnderecoDTO toDTO(Endereco endereco) {
        return new EnderecoDTO(
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCep(),
                endereco.getComplemento());
    }

    public Endereco toEntity(EnderecoDTO enderecoDTO) {
        Endereco endereco = new Endereco();
        endereco.setRua(enderecoDTO.rua());
        endereco.setNumero(enderecoDTO.numero());
        endereco.setCep(enderecoDTO.cep());
        endereco.setComplemento(enderecoDTO.complemento());
        
        return endereco;
    }

}
