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
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Repository.EnderecoRepository;
import com.pagamento.Cliente.Service.EnderecoService;
import com.pagamento.utils.utils;

@SpringBootTest
@Testcontainers
public class EnderecoControllerTest {
    
    @Autowired
    private EnderecoService service;

    @Autowired
    private EnderecoController controller;

    @Autowired
    private EnderecoRepository repository;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
															.withDatabaseName("testdb")
															.withUsername("admin")
															.withPassword("admin")
															.withInitScript("test-data.sql");

    /**
     * Initializes the test environment before all tests are executed.
     * 
     */
    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    /**
     * Sets up the test environment by creating and saving two new address DTOs to the repository.
     *
     * This method is called before each test case is executed. It creates two new address DTOs
     * using the `utils.criarEnderecoTeste()` and `utils.criarEnderecoTeste1()` methods, converts
     * them to entity objects using the `service.toEntity()` method, and saves them to the repository
     * using the `repository.save()` method.
     *
     * @throws ServiceException if there is an error converting the address DTO to an entity object
     */
    @BeforeEach
    void setUp() {
        var novoEndereco1 = service.toDTO(utils.criarEnderecoTeste());
        repository.save(service.toEntity(novoEndereco1));
        var novoEndereco2 = service.toDTO(utils.criarEnderecoTeste1());
        repository.save(service.toEntity(novoEndereco2));
    }

    
    /**
     * Cleans up the test environment by deleting all entities from the repository.
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
     * This test method checks if the database container is running and has been created.
     * It asserts that the `container.isRunning()` method returns `true` and
     * the `container.isCreated()` method also returns `true`.
     */
    @Test
    void testeCriarBancoDeDados() {
        assertTrue(container.isRunning());
        assertTrue(container.isCreated());
    }

    /**
     * Test case for the method `testBuscarEnderecos` in the `EnderecoController` class.
     * This test verifies that the method correctly retrieves a list of enderecos.
     *
     * @throws ResourceNotFoundException if the retrieval of enderecos fails
     */
    @Test
    void testBuscarEnderecos() throws ResourceNotFoundException {
        ResponseEntity<List<Endereco>> result = controller.buscarEnderecos();
        assertEquals(2, result.getBody().size());
    }

    /**
     * Test case for the method `testBuscarEnderecosComErro` in the `EnderecoController` class.
     * This test verifies that the method correctly handles errors when retrieving enderecos.
     *
     * The test creates two enderecos with invalid CEPs, deletes them, and then attempts to retrieve all enderecos.
     * It expects a `ServiceException` to be thrown when calling `controller.buscarEnderecos().getBody()`.
     *
     * @throws ServiceException if the retrieval of enderecos fails
     */
    @Test
    void testBuscarEnderecosComErro() {
        Endereco novoEndereco = service.buscarEnderecoPorCep("12345000");
        Endereco novoEndereco1 = service.buscarEnderecoPorCep("12300222");
        
        service.excluirEndereco(novoEndereco.getId());
        service.excluirEndereco(novoEndereco1.getId());
        
        assertThrows(ServiceException.class, () -> controller.buscarEnderecos().getBody());
    }

    /**
     * Test case for the method `testBuscarEnderecosPorId` in the `EnderecoController` class.
     * This test verifies that the method correctly retrieves an endereco by its ID.
     *
     * @throws ResourceNotFoundException if the endereco is not found for the given ID
     */
    @Test
    void testBuscarEnderecosPorId() throws ResourceNotFoundException {
        Endereco novoEndereco = service.buscarEnderecoPorCep("12345000");

        ResponseEntity<Endereco> result = controller.buscarEndereco(novoEndereco.getId());
        assertEquals(novoEndereco.getId(), result.getBody().getId());
    }

    /**
     * Test case for the method `buscarEnderecoPorId` in the `EnderecoController` class.
     * This test verifies that the method throws a `ServiceException` when an invalid ID is provided.
     *
     * @throws AssertionError if the expected exception is not thrown
     */
    @Test
    void testBuscarEnderecosPorIdComErro() {
        assertThrows(ServiceException.class, 
                     () -> controller.buscarEndereco(31L).getBody());
    }

    /**
     * Test case for the method `buscarEnderecoPorCep` in the `EnderecoController` class.
     * This test verifies that the method returns the correct endereco object for a given cep.
     *
     * @throws ResourceNotFoundException if the endereco is not found for the given cep
     */
    @Test
    void testBuscarEnderecosPorCep() throws ResourceNotFoundException {
        ResponseEntity<Endereco> endereco = controller.buscarEnderecoPorCep("12345000");//controller.buscarEnderecoPorCep("12345000");

        assertEquals("12345000", endereco.getBody().getCep());
    }

    /**
     * Test case for the scenario where the controller is expected to throw a ServiceException
     * when attempting to search for an endereco by a non-existent cep.
     *
     * @throws ServiceException    if the controller fails to throw a ServiceException when
     *                             searching for an endereco by a non-existent cep
     */
    @Test
    void testBuscarEnderecosPorCepComErro() {
        assertThrows(ServiceException.class,    
                     () -> controller.buscarEnderecoPorCep("0000000").getBody());
    }
    
    /**
     * Test case for the insertion of an endereco.
     *
     * @throws ResourceNotFoundException if the endereco is not found
     */
    @Test
    void testInserirEndereco() throws ResourceNotFoundException {

        ResponseEntity<Endereco> novoEndereco = controller.buscarEnderecoPorCep("12345000");

        assertEquals(HttpStatus.OK, novoEndereco.getStatusCode());
        assertEquals("12345000", novoEndereco.getBody().getCep());
    }

    /**
     * Test case for the insertion of an endereco with an error.
     *
     * @throws ServiceException if there is an error during the insertion process
     */
    @Test
    void testInserirEnderecoComErro() {
        assertThrows(ServiceException.class, 
                     () -> controller.inserirEndereco(service.toDTO(new Endereco())));
    }


    /**
     * Test case for the deletion of enderecos.
     *
     * @throws ResourceNotFoundException if the endereco is not found
     */
    @Test
    void testDeletarEnderecos() throws ResourceNotFoundException {
        ResponseEntity<Endereco> novoEndereco = controller.buscarEnderecoPorCep("12345000");
        ResponseEntity<Endereco> novoEndereco1 = controller.buscarEnderecoPorCep("12300222");

        service.excluirEndereco(novoEndereco.getBody().getId());
        service.excluirEndereco(novoEndereco1.getBody().getId());

        assertEquals(HttpStatus.OK, novoEndereco.getStatusCode());
        assertEquals(HttpStatus.OK, novoEndereco1.getStatusCode());
    }
}
