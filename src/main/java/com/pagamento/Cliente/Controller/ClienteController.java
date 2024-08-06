package com.pagamento.Cliente.Controller;

import com.pagamento.Cliente.DTO.ClienteDTO;
import com.pagamento.Cliente.Excecoes.ResourceNotFoundException;
import com.pagamento.Cliente.Excecoes.ServiceException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarClientes() throws ResourceNotFoundException {
        return ResponseEntity.ok().body(clienteService.buscarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable long id) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(clienteService.buscarCliente(id));
    }

    @GetMapping("/email/{email}/")
    public ResponseEntity<Cliente> buscarClientePorEmail(@PathVariable String email) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(clienteService.buscarClientePorEmail(email));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> buscarClientePorCpf(@PathVariable String cpf) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(clienteService.buscarClientePorCpf(cpf));
    }    

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Cliente> buscarClientePorNome(@PathVariable String nome) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(clienteService.buscarClientePorNome(nome));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> criarCliente(@Valid @RequestBody 
                ClienteDTO clienteDTO) throws ResourceNotFoundException {
        
        if (clienteDTO.cpf() == null || clienteDTO.email() == null)
            throw new ServiceException("CPF e email devem ser informados");
        
        ClienteDTO clienteSalva = clienteService.criarCliente(clienteDTO);
        return new ResponseEntity<>(clienteSalva, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirCliente(@PathVariable long id) throws ResourceNotFoundException {
        String msg = clienteService.excluirCliente(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
