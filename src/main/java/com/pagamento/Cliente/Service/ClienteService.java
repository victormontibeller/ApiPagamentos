package com.pagamento.Cliente.Service;

import com.pagamento.Cliente.DTO.ClienteDTO;
import com.pagamento.Cliente.Excecoes.ServiceException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Repository.ClienteRepository;
import com.pagamento.Cliente.Repository.EnderecoRepository;
import com.pagamento.Cliente.Utils.CPFValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository ClienteRepository;
    private final EnderecoRepository EnderecoRepository;
    private final CPFValidator cpfValidator;

    public ClienteService(ClienteRepository ClienteRepository, EnderecoRepository EnderecoRepository, CPFValidator cpfValidator) {
        this.ClienteRepository =ClienteRepository;
        this.EnderecoRepository = EnderecoRepository;
        this.cpfValidator = cpfValidator;
    }

    public ClienteDTO criarCliente(ClienteDTO clienteDTO) throws ServiceException {        
        Cliente cliente = toEntity(clienteDTO);

        Cliente finalCliente = cliente;
        Endereco endereco = EnderecoRepository.findById(cliente.getEndereco().getId())
                .orElseThrow(() -> new ServiceException("Endereço não encontrado para este id: " + finalCliente.getEndereco().getId()));

        if (!cpfValidator.validarCPF(cliente.getCpf())) {
            throw new ServiceException("CPF inválido! ");
        }

        try {
            cliente = ClienteRepository.save(cliente);
        } catch (Exception e) {
            throw new ServiceException("Erro ao inserir o cliente: " + e.getMessage());
        } 

        return toDTO(cliente);
    }

    public List<Cliente> buscarClientes() throws ServiceException {
        List<Cliente> cliente = ClienteRepository.findAll();
        if (cliente.isEmpty()) {
            throw new ServiceException("Nenhum cliente encontrado.");
        }
        return cliente;
    }

    public Cliente buscarCliente(long id) throws ServiceException {
        Cliente cliente = ClienteRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Cliente não encontrado para este id: " + id));
        return cliente;
    }

    public Cliente buscarClientePorEmail(String email) throws ServiceException {
        Cliente cliente = ClienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new ServiceException("Cliente com o email: " + email + " não encontrado.");
        }
        return cliente;
    }

    public Cliente buscarClientePorCpf(String cpf) throws ServiceException {
        Cliente cliente = ClienteRepository.findByCpf(cpf);
        if (cliente == null) {
            throw new ServiceException("Cliente com o CPF: " + cpf + " não encontrado.");
        }
        return cliente;
    }    

    public Cliente buscarClientePorNome(String nome) throws ServiceException {
        Cliente cliente = ClienteRepository.findByNome(nome);
        if (cliente == null) {
            throw new ServiceException("Cliente com o nome: " + nome + " não encontrado.");
        }
        return cliente;
    }

   // delete
   public String excluirCliente(long id) throws ServiceException {
    try {
        Cliente cliente = ClienteRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Cliente não encontrado para este id : " + id));
        ClienteRepository.deleteById(cliente.getId());
    } catch (Exception e) {
        throw new ServiceException("Cliente não encontrado para este id : " + id);
    }
    return "Cliente excluído com sucesso!";
}    

    public ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpf(),
                cliente.getNascimento(), 
                cliente.getEndereco());
    }            

    public Cliente toEntity(ClienteDTO clienteDTO) {
        // Convertendo ClienteDTO para Cliente
        Cliente cliente = new Cliente();
        cliente.setId(clienteDTO.id());
        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(clienteDTO.email());
        cliente.setCpf(clienteDTO.cpf());
        cliente.setNascimento(clienteDTO.nascimento());
        cliente.setEndereco(clienteDTO.endereco());

        return cliente;
    }    

}
