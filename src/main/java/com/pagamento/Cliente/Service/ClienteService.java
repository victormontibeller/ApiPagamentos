package com.pagamento.Cliente.Service;

import com.pagamento.Cliente.DTO.ClienteDTO;
import com.pagamento.Cliente.Excecoes.ResourceNotFoundException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Usuario.Model.Usuario;
import com.pagamento.Cliente.Repository.ClienteRepository;
import com.pagamento.Cliente.Repository.EnderecoRepository;
import com.pagamento.Cliente.Utils.CPFValidator;
import com.pagamento.Usuario.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository ClienteRepository;
    private final EnderecoRepository EnderecoRepository;
    private final UserRepository UserRepository;
    private final CPFValidator cpfValidator;

    public ClienteService(ClienteRepository ClienteRepository, EnderecoRepository EnderecoRepository, UserRepository UserRepository, CPFValidator cpfValidator) {
        this.ClienteRepository =ClienteRepository;
        this.EnderecoRepository = EnderecoRepository;
        this.UserRepository = UserRepository;
        this.cpfValidator = cpfValidator;
    }

    public ClienteDTO criarCliente(ClienteDTO clienteDTO) throws ResourceNotFoundException {
        Cliente cliente = toEntity(clienteDTO);

        Cliente finalCliente = cliente;
        Endereco endereco = EnderecoRepository.findById(cliente.getEndereco().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado para este id: " + finalCliente.getEndereco().getId()));

        Usuario usuario = UserRepository.findById(cliente.getUsuario().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para este id: " + finalCliente.getUsuario().getId()));

        if (!cpfValidator.validarCPF(cliente.getCpf())) {
            throw new ResourceNotFoundException("CPF inválido! ");
        }

        try {
            cliente = ClienteRepository.save(cliente);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Erro ao inserir o cliente: " + e.getMessage());
        } 

        return toDTO(cliente);
    }

    public List<Cliente> buscarClientes() throws ResourceNotFoundException {
        List<Cliente> cliente = ClienteRepository.findAll();
        if (cliente.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum cliente encontrado.");
        }
        return cliente;
    }

    public Cliente buscarCliente(long id) throws ResourceNotFoundException {
        Cliente cliente = ClienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para este id: " + id));
        return cliente;
    }

    public Cliente buscarClientePorEmail(String email) throws ResourceNotFoundException {
        Cliente cliente = ClienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente com o email: " + email + " não encontrado.");
        }
        return cliente;
    }

    public Cliente buscarClientePorCpf(String cpf) throws ResourceNotFoundException {
        Cliente cliente = ClienteRepository.findByCpf(cpf);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente com o CPF: " + cpf + " não encontrado.");
        }
        return cliente;
    }    

    public Cliente buscarClientePorNome(String nome) throws ResourceNotFoundException {
        Cliente cliente = ClienteRepository.findByNome(nome);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente com o nome: " + nome + " não encontrado.");
        }
        return cliente;
    }

   // delete
   public String excluirCliente(long id) throws ResourceNotFoundException {
    try {
        Cliente cliente = ClienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para este id : " + id));
        ClienteRepository.deleteById(cliente.getId());
    } catch (Exception e) {
        throw new ResourceNotFoundException("Cliente não encontrado para este id : " + id);
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
                cliente.getEndereco(),
                cliente.getUsuario());
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
        cliente.setUsuario(clienteDTO.usuario());

        return cliente;
    }    

}
