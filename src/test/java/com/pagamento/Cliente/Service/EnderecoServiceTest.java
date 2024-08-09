package com.pagamento.Cliente.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Repository.EnderecoRepository;
import com.pagamento.utils.utils;

@SpringBootTest
@Testcontainers
public class EnderecoServiceTest {

    @Autowired
    private EnderecoRepository repository;

    @Autowired
    private EnderecoService service;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
                                                                .withDatabaseName("testdb")
                                                                .withUsername("admin")
                                                                .withPassword("admin")
                                                                .withInitScript("test-data.sql");
	
                                                                
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
     * Initializes the test environment before all test cases.
     * Starts the container to ensure the necessary resources are available.
     *
     */
    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    
    /**
     * Stops the container after all tests have been executed.
     *
     */
    @AfterAll
    static void afterAll() {
        container.stop();
    }

    
    /**
     * Sets up the test environment by creating and saving two new address DTOs to the repository.
     *
     * This method is called before each test case is executed. It creates two new address DTOs
     * using the `utils.criarEnderecoTeste()` and `utils.criarEnderecoTeste1()` methods, converts
     * them to entity objects using the `service.toEntity()` method, and saves them to the repository
     * using the `repository.save()` method.
     */
    @BeforeEach
    void setUp() {
        var novoEndereco = service.toDTO(utils.criarEnderecoTeste());
        repository.save(service.toEntity(novoEndereco));
        var novoEndereco1 = service.toDTO(utils.criarEnderecoTeste1());
        repository.save(service.toEntity(novoEndereco1));
    }

    
    /**
     * Cleans up the test environment by deleting all entities from the repository.
     *
     * This method is called after each test case is executed. It uses the `deleteAll()` method
     * of the `repository` object to remove all entities from the repository. This ensures that
     * the repository is in a clean state before running the next test case.
     */
    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
    

	/**
	 * Tests the creation of a database by asserting that the container is running and created.
	 *
	 */
	@Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}

    
    /**
     * Test case for the `testBuscarEndereco` method in the `EnderecoService` class.
     * This test verifies that the method correctly retrieves a list of enderecos.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testBuscarEndereco() throws ServiceException {
        List<Endereco> enderecos = service.buscarEnderecos();
        assertTrue(!enderecos.isEmpty());
        assertTrue(enderecos.size() == 2);
    }

    
    /**
     * Tests the functionality of the `buscarEnderecoPorId` method in the `EnderecoService` class.
     * This test verifies that the method correctly retrieves an endereco by its ID.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testBuscarEnderecoPorId() throws ServiceException {
        Endereco endereco = service.buscarEnderecoPorCep("12345000");
        Endereco endFoundById = service.buscarEndereco(endereco.getId());
        assertTrue(endereco.getId() == endFoundById.getId());
    }

    
    /**
     * Test case for the `testBuscarEnderecoPorCep` method.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testBuscarEnderecoPorCep() throws ServiceException {
        String cep = "12300222";
        var endereco = service.buscarEnderecoPorCep("12300222");

        assertTrue(endereco != null);
        assertEquals(endereco.getCep(), cep);
        assertTrue(endereco.getCep().equals(cep));
    }

    
    /**
     * Test case for the `testExcluirEndereco` method.
     *
     * This test verifies the functionality of the `excluirEndereco` method in the `service` object.
     * The test retrieves an endereco by its CEP, deletes it using the `excluirEndereco` method,
     * and then checks if the endereco is no longer found by its CEP. Finally, it checks if the
     * number of enderecos in the repository is equal to 1.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test 
    void testExcluirEndereco() throws ServiceException {
        var endereco = service.buscarEnderecoPorCep("12300222");
        service.excluirEndereco(endereco.getId());
        assertThrows(ServiceException.class, () -> service.buscarEnderecoPorCep("12300-222"));
        assertTrue(service.buscarEnderecos().size() == 1);
    }

    
    /**
     * Test case for the `testExcluirEnderecoFalha_throwsServiceException` method.
     *
     * This test verifies that the `excluirEndereco` method throws a `ServiceException`
     * when an invalid `id` is provided.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testExcluirEnderecoFalha_throwsServiceException() {
        assertThrows(ServiceException.class, 
                    () -> service.excluirEndereco(100l));
    }


    /**
     * Test case for the `testCriarEnderecoFalha_throwsServiceException` method.
     *
     * This test verifies that the `criarEndereco` method throws a `ServiceException`
     * when an invalid `Endereco` object is provided.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testCriarEnderecoFalha_throwsServiceException() {
        var novoEndereco = new Endereco();
        assertThrows(ServiceException.class, 
                    () -> service.criarEndereco(service.toDTO(novoEndereco)));
    }

    
    /**
     * Test case for the `buscarEnderecosFalha_throwsServiceException` method.
     *
     * This test verifies that the `buscarEnderecos` method in the `service` object
     * throws a `ServiceException` when there are no enderecos in the repository.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void buscarEnderecosFalha_throwsServiceException() throws ServiceException {
        repository.deleteAll();
        assertThrows(ServiceException.class, 
                    () -> service.buscarEnderecos());
    }
    

    /**
     * Test case for the method `buscarEnderecoPorIdFalha` in the `EnderecoService` class.
     * This test case verifies that the method throws a `ServiceException` when the `buscarEndereco` method is called with an invalid ID.
     *
     * @throws ServiceException if the `buscarEndereco` method throws a `ServiceException`.
     */
    @Test
    void testBuscarEnderecoPorIdFalha_throwsServiceException() {
        assertThrows(ServiceException.class, 
                    () -> service.buscarEndereco(100l));
    }

    /**
     * Test case for the `testBuscarEnderecoPorCepFalha_throwsServiceException` method.
     *
     * This test verifies that the `buscarEnderecoPorCep` method in the `service` object
     * throws a `ServiceException` when an invalid CEP is provided.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testBuscarEnderecoPorCepFalha_throwsServiceException() {
        assertThrows(ServiceException.class, 
                    () -> service.buscarEnderecoPorCep("12300000"));
    }












    
}
