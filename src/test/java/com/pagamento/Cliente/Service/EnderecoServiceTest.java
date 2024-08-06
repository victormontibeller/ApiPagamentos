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

import com.pagamento.Cliente.Excecoes.ServiceException;
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
	
    @DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
	}

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        var novoEndereco = service.toDTO(utils.criarEnderecoTeste());
        repository.save(service.toEntity(novoEndereco));
        var novoEndereco1 = service.toDTO(utils.criarEnderecoTeste1());
        repository.save(service.toEntity(novoEndereco1));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

	@Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}

    @Test
    void testBuscarEndereco() throws ServiceException {
        List<Endereco> enderecos = service.buscarEnderecos();
        assertTrue(!enderecos.isEmpty());
        assertTrue(enderecos.size() == 2);
    }

    @Test
    void testBuscarEnderecoPorId() throws ServiceException {
        Endereco endereco = service.buscarEnderecoPorCep("12345000");
        Endereco endFoundById = service.buscarEndereco(endereco.getId());
        assertTrue(endereco.getId() == endFoundById.getId());
    }

    @Test
    void testBuscarEnderecoPorCep() throws ServiceException {
        String cep = "12300222";
        var endereco = service.buscarEnderecoPorCep("12300222");

        assertTrue(endereco != null);
        assertEquals(endereco.getCep(), cep);
        assertTrue(endereco.getCep().equals(cep));
    }

    @Test 
    void testExcluirEndereco() throws ServiceException {
        var endereco = service.buscarEnderecoPorCep("12300222");
        service.excluirEndereco(endereco.getId());
        assertThrows(ServiceException.class, () -> service.buscarEnderecoPorCep("12300-222"));
        assertTrue(service.buscarEnderecos().size() == 1);
    }

    @Test
    void testExcluirEnderecoFalha_throwsServiceException() {
        assertThrows(ServiceException.class, 
                    () -> service.excluirEndereco(100l));
    }

    @Test
    void testCriarEnderecoFalha_throwsServiceException() {
        var novoEndereco = new Endereco();
        assertThrows(ServiceException.class, 
                    () -> service.criarEndereco(service.toDTO(novoEndereco)));
    }

    @Test
    void buscarEnderecosFalha_throwsServiceException() throws ServiceException {
        repository.deleteAll();
        assertThrows(ServiceException.class, 
                    () -> service.buscarEnderecos());
    }

    @Test
    void testBuscarEnderecoPorIdFalha_throwsServiceException() {
        assertThrows(ServiceException.class, 
                    () -> service.buscarEndereco(100l));
    }

    @Test
    void testBuscarEnderecoPorCepFalha_throwsServiceException() {
        assertThrows(ServiceException.class, 
                    () -> service.buscarEnderecoPorCep("12300000"));
    }












    
}
