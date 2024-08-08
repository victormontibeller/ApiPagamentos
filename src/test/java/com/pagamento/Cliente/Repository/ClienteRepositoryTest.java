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

	/**
	 * Initializes the test environment before each test is run.
	 * Opens the mocks for the test class.
	 */
	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tears down the test environment by closing the AutoCloseable resource.
	 *
	 * @throws Exception if an error occurs during the tear down process
	 */
	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

    /**
     * Test method for creating a new cliente in the repository.
     *
     * @return  void
     */
	@Test
    public void testCriarCliente() {
        Cliente cliente = utils.criarClienteTeste();
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        
        var clienteSalvo = clienteRepository.save(cliente);
        
        assertNotNull(clienteSalvo);
        assertEquals(cliente.getNome(), clienteSalvo.getNome());
        assertEquals(cliente.getCpf(), clienteSalvo.getCpf());
        assertEquals(cliente.getEmail(), clienteSalvo.getEmail());
        assertEquals(cliente.getNascimento(), clienteSalvo.getNascimento());
        assertEquals(cliente.getEndereco(), clienteSalvo.getEndereco());
        assertEquals(cliente.getId(), clienteSalvo.getId());
        verify(clienteRepository, times(1)).save(cliente);
    }

	
	 /**
	  * Tests the method for finding a cliente by its ID in the repository.
	  *
	  * @return  void
	  */
	 @Test
	 public void testClienteFindById() {
		 Long id = 1L;
		 Cliente cliente = utils.criarClienteTeste();
		 when(clienteRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(cliente));
		 
		 var clienteEncontrado = clienteRepository.findById(id);
		 
		 assertNotNull(clienteEncontrado);
		 assertEquals(cliente.getNome(), clienteEncontrado.get().getNome());
		 assertEquals(cliente.getCpf(), clienteEncontrado.get().getCpf());
		 assertEquals(cliente.getEmail(), clienteEncontrado.get().getEmail());
		 assertEquals(cliente.getNascimento(), clienteEncontrado.get().getNascimento());
		 assertEquals(cliente.getEndereco(), clienteEncontrado.get().getEndereco());
		 assertEquals(cliente.getId(), clienteEncontrado.get().getId());
		 verify(clienteRepository, times(1)).findById(id);
	 }  
	 
	 /**
	  * Test method for finding a cliente by its email in the repository.
	  *
	  * @return  void
	  */
	 @Test
	 public void testClienteFindByEmail() {
		 String email = "joao@example.com";
		 when(clienteRepository.findByEmail(email)).thenReturn(utils.criarClienteTeste());
		 
		 var clienteEncontrado = clienteRepository.findByEmail(email);
 
		 assertNotNull(clienteEncontrado);
		 assertEquals(utils.criarClienteTeste().getNome(), clienteEncontrado.getNome());
		 assertEquals(utils.criarClienteTeste().getCpf(), clienteEncontrado.getCpf());
		 assertEquals(utils.criarClienteTeste().getEmail(), clienteEncontrado.getEmail());
		 assertEquals(utils.criarClienteTeste().getNascimento(), clienteEncontrado.getNascimento());
		 assertEquals(utils.criarClienteTeste().getEndereco(), clienteEncontrado.getEndereco());
		 assertEquals(utils.criarClienteTeste().getId(), clienteEncontrado.getId());
		 verify(clienteRepository, times(1)).findByEmail(email);
	 }
 
	/**
	  * Test case for the `findByCpf` method in the `ClienteRepository` class.
	  *
	  * This test verifies that the `findByCpf` method returns the correct `Cliente` object
	  * when given a valid CPF. It mocks the `clienteRepository` object and sets up the
	  * `findByCpf` method to return a test `Cliente` object. Then, it calls the `findByCpf`
	  * method with the test CPF and asserts that the returned `Cliente` object is not null and
	  * has the same values as the test `Cliente` object. Finally, it verifies that the
	  * `findByCpf` method was called exactly once with the test CPF.
	  *
	  * @throws Exception if an error occurs during the test
	  */
	 @Test
	 public void testClienteFindByCpf() {
		 String cpf = "334.750.780-07";
		 when(clienteRepository.findByCpf(cpf)).thenReturn(utils.criarClienteTeste());
		 
		 var clienteEncontrado = clienteRepository.findByCpf(cpf);
		 
		 assertNotNull(clienteEncontrado);
		 assertEquals(utils.criarClienteTeste().getNome(), clienteEncontrado.getNome());
		 assertEquals(utils.criarClienteTeste().getCpf(), clienteEncontrado.getCpf());
		 assertEquals(utils.criarClienteTeste().getEmail(), clienteEncontrado.getEmail());
		 assertEquals(utils.criarClienteTeste().getNascimento(), clienteEncontrado.getNascimento());
		 assertEquals(utils.criarClienteTeste().getEndereco(), clienteEncontrado.getEndereco());
		 assertEquals(utils.criarClienteTeste().getId(), clienteEncontrado.getId());
		 verify(clienteRepository, times(1)).findByCpf(cpf);
	 }
 
	 
	 /**
	  * Test case for the deletion of a client.
	  *
	  * This test verifies that the `deleteById` method in the `ClienteRepository` class
	  * correctly deletes a client. It mocks the `clienteRepository` object and sets up
	  * the `findById` and `save` methods to return a test `Cliente` object. Then, it
	  * calls the `deleteById` method with the test ID and verifies that the `deleteById`
	  * method was called exactly once with the test ID.
	  *
	  * @throws Exception if an error occurs during the test
	  */
	 @Test
	 public void testDeletarCliente() {
		 Long id = 1L;
		 when(clienteRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(utils.criarClienteTeste()));
		 when(clienteRepository.save(utils.criarClienteTeste())).thenReturn(utils.criarClienteTeste());
		 
		 clienteRepository.deleteById(id);
		 
		 verify(clienteRepository, times(1)).deleteById(id);
	 }
}
