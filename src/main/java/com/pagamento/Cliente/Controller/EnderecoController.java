package com.pagamento.Cliente.Controller;

import com.pagamento.Cliente.DTO.EnderecoDTO;
import com.pagamento.Cliente.Excecoes.ResourceNotFoundException;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> buscarEnderecos() throws ResourceNotFoundException {
        return ResponseEntity.ok().body(enderecoService.buscarEnderecos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarEndereco(@PathVariable long id) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(enderecoService.buscarEndereco(id));
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<Endereco> buscarEnderecoPorCep(@PathVariable String cep) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(enderecoService.buscarEnderecoPorCep(cep));
    }    

    @PostMapping
    public ResponseEntity<EnderecoDTO> inserirEndereco(@Valid @RequestBody EnderecoDTO enderecoDTO) throws ResourceNotFoundException {
        EnderecoDTO enderecoSalva = enderecoService.criarEndereco(enderecoDTO);
        return new ResponseEntity<>(enderecoSalva, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirEndereco(@PathVariable long id) throws ResourceNotFoundException {
        String msg = enderecoService.excluirEndereco(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
