package com.pagamento.Cliente.Repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Service.ClienteService;
import com.pagamento.utils.utils;

class ClienteRepositoryTest {

	@Mock
	private ClienteRepository clienteRepository;

	@InjectMocks
	private ClienteService clienteService;

	AutoCloseable autoCloseable;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
    public void testCriarCliente() {
        // Arrange
        Cliente cliente = utils.criarClienteTeste();
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        
        // Act
        var clienteSalvo = clienteRepository.save(cliente);
        
        // Assert
        assertNotNull(clienteSalvo);
        assertEquals(cliente.getNome(), clienteSalvo.getNome());
        assertEquals(cliente.getCpf(), clienteSalvo.getCpf());
        assertEquals(cliente.getEmail(), clienteSalvo.getEmail());
        assertEquals(cliente.getNascimento(), clienteSalvo.getNascimento());
        assertEquals(cliente.getEndereco(), clienteSalvo.getEndereco());
        assertEquals(cliente.getId(), clienteSalvo.getId());
        verify(clienteRepository, times(1)).save(cliente);
    }

	 //buscar cliente
	 @Test
	 public void testClienteFindById() {
		 // Arrange
		 Long id = 1L;
		 Cliente cliente = utils.criarClienteTeste();
		 when(clienteRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(cliente));
		 
		 // Act
		 var clienteEncontrado = clienteRepository.findById(id);
		 
		 // Assert
		 assertNotNull(clienteEncontrado);
		 assertEquals(cliente.getNome(), clienteEncontrado.get().getNome());
		 assertEquals(cliente.getCpf(), clienteEncontrado.get().getCpf());
		 assertEquals(cliente.getEmail(), clienteEncontrado.get().getEmail());
		 assertEquals(cliente.getNascimento(), clienteEncontrado.get().getNascimento());
		 assertEquals(cliente.getEndereco(), clienteEncontrado.get().getEndereco());
		 assertEquals(cliente.getId(), clienteEncontrado.get().getId());
		 verify(clienteRepository, times(1)).findById(id);
	 }  
	 
	 //buscar cliente por email
	 @Test
	 public void testClienteFindByEmail() {
		 // Arrange
		 String email = "joao@example.com";
		 when(clienteRepository.findByEmail(email)).thenReturn(utils.criarClienteTeste());
		 
		 // Act
		 var clienteEncontrado = clienteRepository.findByEmail(email);
 
		 // Assert
		 assertNotNull(clienteEncontrado);
		 assertEquals(utils.criarClienteTeste().getNome(), clienteEncontrado.getNome());
		 assertEquals(utils.criarClienteTeste().getCpf(), clienteEncontrado.getCpf());
		 assertEquals(utils.criarClienteTeste().getEmail(), clienteEncontrado.getEmail());
		 assertEquals(utils.criarClienteTeste().getNascimento(), clienteEncontrado.getNascimento());
		 assertEquals(utils.criarClienteTeste().getEndereco(), clienteEncontrado.getEndereco());
		 assertEquals(utils.criarClienteTeste().getId(), clienteEncontrado.getId());
		 verify(clienteRepository, times(1)).findByEmail(email);
	 }
 
	 //buscar cliente por cpf
 
	 @Test
	 public void testClienteFindByCpf() {
		 // Arrange
		 String cpf = "334.750.780-07";
		 when(clienteRepository.findByCpf(cpf)).thenReturn(utils.criarClienteTeste());
		 
		 // Act  
		 var clienteEncontrado = clienteRepository.findByCpf(cpf);
		 
		 // Assert
		 assertNotNull(clienteEncontrado);
		 assertEquals(utils.criarClienteTeste().getNome(), clienteEncontrado.getNome());
		 assertEquals(utils.criarClienteTeste().getCpf(), clienteEncontrado.getCpf());
		 assertEquals(utils.criarClienteTeste().getEmail(), clienteEncontrado.getEmail());
		 assertEquals(utils.criarClienteTeste().getNascimento(), clienteEncontrado.getNascimento());
		 assertEquals(utils.criarClienteTeste().getEndereco(), clienteEncontrado.getEndereco());
		 assertEquals(utils.criarClienteTeste().getId(), clienteEncontrado.getId());
		 verify(clienteRepository, times(1)).findByCpf(cpf);
	 }
 
	 //deletar cliente
	 @Test
	 public void testDeletarCliente() {
		 // Arrange
		 Long id = 1L;
		 when(clienteRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(utils.criarClienteTeste()));
		 when(clienteRepository.save(utils.criarClienteTeste())).thenReturn(utils.criarClienteTeste());
		 
		 // Act
		 clienteRepository.deleteById(id);
		 
		 // Assert
		 verify(clienteRepository, times(1)).deleteById(id);
	 }
}
