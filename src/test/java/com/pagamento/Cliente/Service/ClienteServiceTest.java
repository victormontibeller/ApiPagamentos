package com.pagamento.Cliente.Service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import com.pagamento.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pagamento.Exception.ServiceException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Repository.ClienteRepository;
import com.pagamento.utils.utils;

@SpringBootTest
@Testcontainers
class ClienteServiceTest {
    
	@Autowired
	private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

	@Container
	private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
															.withDatabaseName("testdb")
															.withUsername("admin")
															.withPassword("admin")
															.withInitScript("test-data.sql");

    /**
     * Initializes the test environment before all tests are run.
     * Starts the container used for testing.
     */
    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    /**
     * Sets up the test environment by creating and saving two new client DTOs to the repository.
     *
     * This method is called before each test case is executed. It creates two new client DTOs
     * using the `utils.criarClienteTeste()` and `utils.criarClienteTeste1()` methods, converts
     * them to entity objects using the `clienteService.toEntity()` method, and saves them to the repository
     * using the `clienteRepository.save()` method.
     *
     * @throws ServiceException if there is an error converting the client DTO to an entity object
     */
    @BeforeEach
    void setUp() {
        var novoCliente1 = clienteService.toDTO(utils.criarClienteTeste());
        clienteRepository.save(clienteService.toEntity(novoCliente1));
        var novoCliente2 = clienteService.toDTO(utils.criarClienteTeste1());
        clienteRepository.save(clienteService.toEntity(novoCliente2));
    }

    /**
     * Cleans up the test environment by deleting all entities from the repository.
     *
     * This method is called after each test case is executed. It uses the `clienteRepository.deleteAll()` method to remove all entities from the repository. This ensures that the test environment is clean and ready for the next test case.
     */
    @AfterEach
    void cleanUp() {
        clienteRepository.deleteAll();
    }


	/**
	 * Closes the container after all tests have been executed.
	 *
	 * @throws Exception if there is an error closing the container
	 */
	@AfterAll
	static void tearDown() {
		container.close();
	}
		
	/**
	 * Sets the dynamic properties for the Spring datasource.
	 *
	 * @param dynamicPropertyRegistry the registry to add the dynamic properties to
	 */
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
	}

	/**
	 * Tests the creation of a database.
	 *
	 * This test method checks if the database container is running and has been created.
	 * It asserts that the `container.isRunning()` method returns `true` and
	 * the `container.isCreated()` method also returns `true`.
	 */
	@Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}

    /**
     * Tests the `buscarClientes` method of the `clienteService` to ensure it returns a non-null, non-empty list of `Cliente` objects with a size of 2.
     *
     * @throws ServiceException if there is an error retrieving the client list
     */
    @Test
    void testBuscarClientes() throws ServiceException {
        List<Cliente> clienteBuscado = clienteService.buscarClientes();
        
        assertNotNull(clienteBuscado);
        assertTrue(!clienteBuscado.isEmpty());
        assertTrue(clienteBuscado.size() == 2);
        
    }

    
    /**
     * Tests the deletion of a client by ID.
     *
     * This test method verifies that the `excluirCliente` method in the `ClienteService` class deletes a client successfully.
     * It first retrieves the client using the `buscarClientePorCpf` method with a valid CPF. Then, it extracts the ID of the client.
     * Finally, it calls the `excluirCliente` method with the extracted ID and asserts that the `buscarClientePorCpf` method throws a `ServiceException` when trying to retrieve the deleted client.
     *
     * @throws ServiceException if there is an error deleting the client
     */
    @Test
    void testDeletarClientePorId() throws ServiceException {
        var cliente = clienteService.buscarClientePorCpf("44593864020");
        clienteService.excluirCliente(cliente.getId());
        assertThrows(ServiceException.class, 
                    () -> clienteService.buscarClientePorCpf("44593864020"));
    }

    
    /**
     * Tests the creation of a client.
     *
     * This test method verifies that the `criarCliente` method in the `ClienteService` class
     * successfully creates a new client. It first creates a new `Cliente` object with a specific
     * set of attributes. Then, it converts the `Cliente` object to a DTO using the `toDTO`
     * method in the `ClienteService`. Next, it calls the `criarCliente` method with the DTO.
     * Finally, it asserts that the `buscarClientePorCpf` method returns a non-null client with
     * the same CPF as the original `Cliente` object.
     *
     * @throws ServiceException if there is an error creating the client
     */
    @Test
    void testCriarCliente() throws ServiceException, ResourceNotFoundException {
        var novoCliente = clienteService.toDTO(new Cliente(5l, 
                                                           "um nome que eu quiser", 
                                                           "emailTeste@teste.com", 
                                                           "10614072093", 
                                                           LocalDate.now(), 
                                                           utils.criarEnderecoTeste1(),
                                                            utils.criarUsuarioTeste1()));
        clienteService.criarCliente(novoCliente);
        assertNotNull(clienteService.buscarClientePorCpf(novoCliente.cpf()));
        assertTrue(novoCliente.cpf().equals(clienteService.buscarClientePorCpf(novoCliente.cpf()).getCpf()));
    }

    
    /**
     * Tests the `buscarClientePorEmail` method in the `ClienteService` class.
     *
     * This test method verifies that the `buscarClientePorEmail` method in the `ClienteService` class
     * successfully retrieves a client by their email address. It first calls the `buscarClientePorEmail`
     * method with a valid email address. Then, it asserts that the returned client is not null and has
     * the same name, CPF, email, and birthdate as the expected client created by the `criarClienteTeste`
     * method in the `utils` class.
     *
     * @throws ServiceException if there is an error retrieving the client by email
     */
    @Test
    void testClienteFindByEmail() throws ServiceException {
        var clienteEncontrado = clienteService.buscarClientePorEmail("joao@example.com");
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste().getNome(), clienteEncontrado.getNome());
        assertEquals(utils.criarClienteTeste().getCpf(), clienteEncontrado.getCpf());
        assertEquals(utils.criarClienteTeste().getEmail(), clienteEncontrado.getEmail());
        assertEquals(utils.criarClienteTeste().getNascimento(), clienteEncontrado.getNascimento());
    }

    
    /**
     * Tests the `buscarClientePorCpf` method in the `ClienteService` class.
     *
     * This test method verifies that the `buscarClientePorCpf` method in the `ClienteService` class
     * successfully retrieves a client by their CPF. It first calls the `buscarClientePorCpf` method
     * with a valid CPF. Then, it asserts that the returned client is not null and has the same name,
     * CPF, email, and birthdate as the expected client created by the `criarClienteTeste1` method in
     * the `utils` class.
     *
     * @throws ServiceException if there is an error retrieving the client by CPF
     */
    @Test
    void testClienteFindByCpf() throws ServiceException {
        var clienteEncontrado = clienteService.buscarClientePorCpf("44593864020");
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste1().getNome(), clienteEncontrado.getNome());
        assertEquals(utils.criarClienteTeste1().getCpf(), clienteEncontrado.getCpf());
        assertEquals(utils.criarClienteTeste1().getEmail(), clienteEncontrado.getEmail());
        assertEquals(utils.criarClienteTeste1().getNascimento(), clienteEncontrado.getNascimento());
    }

    
    /**
     * Tests the `buscarClientePorId` method in the `ClienteService` class.
     *
     * This test method verifies that the `buscarClientePorId` method in the `ClienteService` class
     * successfully retrieves a client by their ID. It first calls the `buscarClientePorCpf` method
     * with a valid CPF. Then, it calls the `buscarCliente` method with the retrieved client's ID.
     * Finally, it asserts that the returned client is not null and has the same name, CPF, email,
     * and birthdate as the expected client created by the `criarClienteTeste1` method in the `utils`
     * class.
     *
     * @throws ServiceException if there is an error retrieving the client by ID
     */
    @Test
    void testBuscarClientePorId() throws ServiceException {
        var clienteEncontrado = clienteService.buscarClientePorCpf("44593864020");
        var ClienteEncontradoPorId = clienteService.buscarCliente(clienteEncontrado.getId());
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste1().getNome(), ClienteEncontradoPorId.getNome());
        assertEquals(utils.criarClienteTeste1().getCpf(), ClienteEncontradoPorId.getCpf());
        assertEquals(utils.criarClienteTeste1().getEmail(), ClienteEncontradoPorId.getEmail());
        assertEquals(utils.criarClienteTeste1().getNascimento(), ClienteEncontradoPorId.getNascimento());
    }

    
    /**
     * Tests the `buscarClientePorNome` method in the `ClienteService` class.
     *
     * This test method verifies that the `buscarClientePorNome` method in the `ClienteService` class
     * successfully retrieves a client by their name. It first calls the `buscarClientePorNome` method
     * with a valid name. Then, it asserts that the returned client is not null and has the same name,
     * CPF, email, and birthdate as the expected client created by the `criarClienteTeste1` method in the
     * `utils` class.
     *
     * @throws ServiceException if there is an error retrieving the client by name
     */
    @Test
    void testBuscarClientePorNome() throws ServiceException {
        var clienteEncontrado = clienteService.buscarClientePorNome("Joaquim pasquale pereira");
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste1().getNome(), clienteEncontrado.getNome());
        assertEquals(utils.criarClienteTeste1().getCpf(), clienteEncontrado.getCpf());
        assertEquals(utils.criarClienteTeste1().getEmail(), clienteEncontrado.getEmail());
        assertEquals(utils.criarClienteTeste1().getNascimento(), clienteEncontrado.getNascimento());
    }

    
    /**
     * Tests the `buscarCliente` method in the `ClienteService` class for failure.
     *
     * This test method verifies that the `buscarCliente` method in the `ClienteService` class
     * correctly throws a `ServiceException` when attempting to retrieve a client by a non-existent ID.
     *
     * @throws ServiceException if there is an error retrieving the client by ID
     */
    @Test
    void testBuscarClientePorIdfalha_throwsServiceException () {        
        assertThrows(ServiceException.class, () -> clienteService.buscarCliente(100l));
    }

    
    /**
     * Tests the `buscarClientePorEmail` method in the `ClienteService` class for failure.
     *
     * This test method verifies that the `buscarClientePorEmail` method in the `ClienteService` class
     * correctly throws a `ServiceException` when attempting to retrieve a client by a non-existent email.
     *
     * @throws ServiceException if there is an error retrieving the client by email
     */
    @Test
    void testBuscarPorEmailClientefalha_throwsServiceException () {        
        assertThrows(ServiceException.class, () -> clienteService.buscarClientePorEmail("antonio@teste.com"));
    }

    
    /**
     * Tests the `buscarClientePorNome` method in the `ClienteService` class for failure.
     *
     * This test method verifies that the `buscarClientePorNome` method in the `ClienteService` class
     * correctly throws a `ServiceException` when attempting to retrieve a client by a non-existent name.
     *
     * @throws ServiceException if there is an error retrieving the client by name
     */
    @Test
    void testBuscarPorNomeClientefalha_throwsServiceException () {        
        assertThrows(ServiceException.class, () -> clienteService.buscarClientePorNome("açsldk"));
    }

    
    /**
     * Tests the `buscarClientePorCpf` method in the `ClienteService` class for failure.
     *
     * This test method verifies that the `buscarClientePorCpf` method in the `ClienteService` class
     * correctly throws a `ServiceException` when attempting to retrieve a client by a non-existent CPF.
     *
     * @throws ServiceException if there is an error retrieving the client by CPF
     */
    @Test
    void testBuscarPorNomeCpfFalha_throwsServiceException () {        
        assertThrows(ServiceException.class, () -> clienteService.buscarClientePorCpf("açsldk"));
    }

    
    /**
     * Tests the `excluirCliente` method in the `ClienteService` class for failure.
     *
     * This test method verifies that the `excluirCliente` method in the `ClienteService` class
     * correctly throws a `ServiceException` when attempting to delete a client by a non-existent ID.
     *
     * @throws ServiceException if there is an error deleting the client by ID
     */
    @Test
    void testExcluirClienteFalha_throwsServiceException () {        
        assertThrows(ServiceException.class, () -> clienteService.excluirCliente(100l));
    }

    
    /**
     * Tests the `criarCliente` method in the `ClienteService` class for failure when creating a client with an invalid CPF.
     *
     * This test method verifies that the `criarCliente` method in the `ClienteService` class correctly throws a `ServiceException`
     * when attempting to create a client with an invalid CPF. It creates a new `Cliente` object with a valid ID, name, email,
     * CPF, birth date, and an address created using the `criarEnderecoTeste` method from the `utils` object. It then asserts that
     * calling the `criarCliente` method with the created `Cliente` object as a DTO throws a `ServiceException`.
     *
     * @throws ServiceException if there is an error creating the client with an invalid CPF
     */
    @Test
    void testCriarClienteComCpfInvalido_throwsServiceException () {
        var novoCliente = new Cliente(1L,
                           "João almeida dos Santos",
                           "joao@example.com",
                           "33478007",
                           LocalDate.of(1990,01,01),
                           utils.criarEnderecoTeste(),
                           utils.criarUsuarioTeste());
        assertThrows(ServiceException.class, () -> clienteService.criarCliente(clienteService.toDTO(novoCliente)));   
    }

    
    /**
     * Tests the `criarCliente` method in the `ClienteService` class for failure when creating a client with a null object.
     *
     * This test method verifies that the `criarCliente` method in the `ClienteService` class correctly throws a `NullPointerException`
     * when attempting to create a client with a null object. It creates a new `Cliente` object with a null value. It then asserts
     * that calling the `criarCliente` method with the created `Cliente` object as a DTO throws a `NullPointerException`.
     *
     * @throws NullPointerException if there is a null pointer exception when creating the client
     */
    @Test
    void testCriarClienteNull_throwsServiceException () {
        var novoCliente = new Cliente();
        assertThrows(NullPointerException.class, () -> clienteService.criarCliente(clienteService.toDTO(novoCliente)));
    }
}