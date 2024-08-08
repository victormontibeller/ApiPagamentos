package com.pagamento.Cliente.Controller;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pagamento.Cliente.Excecoes.ResourceNotFoundException;
import com.pagamento.Cliente.Excecoes.ServiceException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Repository.ClienteRepository;
import com.pagamento.Cliente.Service.ClienteService;
import com.pagamento.utils.utils;

@SpringBootTest
@Testcontainers
public class ClienteControllerTest {
    
    @Autowired
    private ClienteController controller;

    @Autowired
    private ClienteService service;

    @Autowired
    private ClienteRepository repository;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
															.withDatabaseName("testdb")
															.withUsername("admin")
															.withPassword("admin")
															.withInitScript("test-data.sql");

    /**
     * Initializes the test environment before all tests are executed.
     * Starts the container to set up the necessary dependencies for the tests.
     */
    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    /**
     * Sets up the test environment by creating and saving two test client objects.
     *
     * This method is called before each test case is executed. It creates two test client objects
     * using the `utils.criarClienteTeste()` and `utils.criarClienteTeste1()` methods, converts them to
     * DTOs using the `service.toDTO()` method, and saves them as entities using the `repository.save()` method.
     *
     */
    @BeforeEach
    void setUp() {
        var novoCliente1 = service.toDTO(utils.criarClienteTeste());
        repository.save(service.toEntity(novoCliente1));
        var novoCliente2 = service.toDTO(utils.criarClienteTeste1());
        repository.save(service.toEntity(novoCliente2));
    
    }

    /**
     * Cleans up the test environment by deleting all entities in the repository.
     *
     * This method is called after each test case is executed. It uses the `repository.deleteAll()` method to remove all entities from the repository. This ensures that the test environment is clean and ready for the next test case.
     */
    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

	/**
	 * Closes the container after all tests have been executed.
	 *
	 * This method is called after all tests have been run. It closes the container to release any resources and clean up the test environment.
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
	 * This test method checks if the database is running and has been created.
	 * It asserts that the `container.isRunning()` method returns `true` and
	 * the `container.isCreated()` method also returns `true`.
	 *
	 */
	@Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}    

    /**
     * Tests the successful execution of the `buscarClientesControllerComSucesso` method.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientesControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<List<Cliente>> clientes = controller.buscarClientes();
        assertEquals(HttpStatus.OK, clientes.getStatusCode());
        assertEquals(2, clientes.getBody().size());
    }

    /**
     * Tests the failed execution of the `buscarClientesController` method.
     *
     * This test method checks if the `buscarClientesController` method throws a `ServiceException` when no clients are found.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientesControllerComErro() throws ResourceNotFoundException{
        Cliente novoCliente = service.buscarClientePorCpf("44593864020");
        Cliente novoCliente1 = service.buscarClientePorCpf("33475078007");
        
        service.excluirCliente(novoCliente.getId());
        service.excluirCliente(novoCliente1.getId());
        
        assertThrows(ServiceException.class, () -> controller.buscarClientes());
    }
    
    /**
     * A test method for successfully searching for a client by ID in the controller.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorIdControllerComSucesso() throws ResourceNotFoundException{
        Cliente novoCliente = service.buscarClientePorCpf("44593864020");
        long id = novoCliente.getId();
      
        ResponseEntity<Cliente> cliente = controller.buscarCliente(id);
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }

    /**
     * Tests the failed execution of the `buscarClientePorIdController` method.
     *
     * This test method checks if the `buscarClientePorIdController` method throws a `ServiceException` when an invalid ID is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorIdControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarCliente(0L));
    }

    /**
     * A test method for successfully searching for a client by email in the controller.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorEmailControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorEmail("123Oliveira@example.com");
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }
    
    /**
     * Tests the failed execution of the `buscarClientePorEmailController` method.
     *
     * This test method checks if the `buscarClientePorEmailController` method throws a `ServiceException` when an invalid email is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorEmailControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarClientePorEmail("banana@bancadabanana.com"));
    }

    /**
     * Tests the successful execution of the `buscarClientePorCpfController` method.
     *
     * This test method checks if the `buscarClientePorCpfController` method returns a `ResponseEntity` with a status code of `HttpStatus.OK` and a `Cliente` body with the name "Joaquim pasquale pereira" when a valid CPF is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorCpfControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorCpf("44593864020");
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }

    /**
     * Tests the failed execution of the `buscarClientePorCpfController` method.
     *
     * This test method checks if the `buscarClientePorCpfController` method throws a `ServiceException` when an invalid CPF is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorCpfControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarClientePorCpf("banana"));
    }

    /**
     * Tests the successful execution of the `buscarClientePorNomeController` method.
     *
     * This test method checks if the `buscarClientePorNomeController` method returns a `ResponseEntity` with a status code of `HttpStatus.OK` and a `Cliente` body with the name "Joaquim pasquale pereira" when a valid name is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorNomeControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorNome("Joaquim");
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }
    
    /**
     * Tests the successful execution of the `buscarClientePorNomeController` method.
     *
     * This test method checks if the `buscarClientePorNomeController` method returns a `ResponseEntity` with a status code of `HttpStatus.OK` and a `Cliente` body with the name "Joaquim pasquale pereira" when a valid name is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeBuscarClientePorNomeControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarClientePorNome("banana"));
    }

    /**
     * Tests the successful execution of the `criarClienteController` method.
     *
     * This test method checks if the `criarClienteController` method returns a `ResponseEntity` with a status code of `HttpStatus.OK` and a `Cliente` body with the name "Joaquim pasquale pereira" when a valid CPF is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeCriarClienteControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorCpf("44593864020");
        
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }
       
    /**
     * Tests the unsuccessful execution of the `criarClienteController` method.
     *
     * This test method checks if the `criarClienteController` method throws a `ServiceException` when an invalid or empty `Cliente` is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeCriarClienteControllerComErro() throws ResourceNotFoundException{
        
        //assertEquals(httpStatus.NO_CONTENT, )
        assertThrows(ServiceException.class, 
                    () -> controller.criarCliente(service.toDTO(new Cliente())));
    }

    /**
     * Tests the successful deletion of a client in the controller.
     *
     * This test method verifies that the `excluirCliente` method in the `ClienteController` class deletes a client successfully.
     * It first retrieves the client using the `buscarClientePorCpf` method with a valid CPF. Then, it extracts the ID of the client.
     * Finally, it calls the `excluirCliente` method with the extracted ID and asserts that the response status code is `HttpStatus.OK`.
     *
     * @throws ResourceNotFoundException if the client with the provided CPF is not found
     */
    @Test
    void testeDeletarClienteControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorCpf("44593864020");
        long id = cliente.getBody().getId();
        
        controller.excluirCliente(id);
        
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
    }

    /**
     * Tests the failed deletion of a client in the controller.
     *
     * This test method checks if the `excluirCliente` method in the `ClienteController` class throws a `ServiceException` when an invalid client ID is provided.
     *
     * @throws ResourceNotFoundException if the resource is not found
     */
    @Test
    void testeDeletarClienteControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.excluirCliente(0L));
    }
}

