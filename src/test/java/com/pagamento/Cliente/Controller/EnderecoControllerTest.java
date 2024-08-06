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

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        var novoEndereco1 = service.toDTO(utils.criarEnderecoTeste());
        repository.save(service.toEntity(novoEndereco1));
        var novoEndereco2 = service.toDTO(utils.criarEnderecoTeste1());
        repository.save(service.toEntity(novoEndereco2));
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @AfterAll
	static void tearDown() {
		container.close();
	}

   	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
	}

    @Test
    void testeCriarBancoDeDados() {
        assertTrue(container.isRunning());
        assertTrue(container.isCreated());
    }

    //TODO: Falta testar o buscarEnderecos
    @Test
    void testBuscarEnderecos() throws ResourceNotFoundException {
        ResponseEntity<List<Endereco>> result = controller.buscarEnderecos();
        assertEquals(2, result.getBody().size());
    }

    //TODO: Falta testar o buscarEnderecosComErro
    @Test
    void testBuscarEnderecosComErro() {
        Endereco novoEndereco = service.buscarEnderecoPorCep("12345000");
        Endereco novoEndereco1 = service.buscarEnderecoPorCep("12300222");
        
        service.excluirEndereco(novoEndereco.getId());
        service.excluirEndereco(novoEndereco1.getId());
        
        assertThrows(ServiceException.class, () -> controller.buscarEnderecos().getBody());
    }

    //TODO: Falta testar o buscarEnderecosPorId
    @Test
    void testBuscarEnderecosPorId() throws ResourceNotFoundException {
        Endereco novoEndereco = service.buscarEnderecoPorCep("12345000");

        ResponseEntity<Endereco> result = controller.buscarEndereco(novoEndereco.getId());
        assertEquals(novoEndereco.getId(), result.getBody().getId());
    }

    //TODO: Falta testar o buscarEnderecosPorIdComErro
    @Test
    void testBuscarEnderecosPorIdComErro() {
        assertThrows(ServiceException.class, 
                     () -> controller.buscarEndereco(31L).getBody());
    }

    //TODO: Falta testar o buscarEnderecosPorCep
    @Test
    void testBuscarEnderecosPorCep() throws ResourceNotFoundException {
        ResponseEntity<Endereco> endereco = controller.buscarEnderecoPorCep("12345000");//controller.buscarEnderecoPorCep("12345000");

        assertEquals("12345000", endereco.getBody().getCep());
    }

    //TODO: Falta testar o buscarEnderecosPorCepComErro
    @Test
    void testBuscarEnderecosPorCepComErro() {
        assertThrows(ServiceException.class,    
                     () -> controller.buscarEnderecoPorCep("0000000").getBody());
    }
    
    //TODO: Falta testar o inserirEndereco
    @Test
    void testInserirEndereco() throws ResourceNotFoundException {

        ResponseEntity<Endereco> novoEndereco = controller.buscarEnderecoPorCep("12345000");

        assertEquals(HttpStatus.OK, novoEndereco.getStatusCode());
        assertEquals("12345000", novoEndereco.getBody().getCep());
    }

    //TODO: Falta testar o inserirEnderecoComErro
    @Test
    void testInserirEnderecoComErro() {
        assertThrows(ServiceException.class, 
                     () -> controller.inserirEndereco(service.toDTO(new Endereco())));
    }

    //TODO: Falta testar o deletarEnderecos
    @Test
    void testDeletarEnderecos() throws ResourceNotFoundException {
        ResponseEntity<Endereco> novoEndereco = controller.buscarEnderecoPorCep("12345000");
        ResponseEntity<Endereco> novoEndereco1 = controller.buscarEnderecoPorCep("12300222");

        service.excluirEndereco(novoEndereco.getBody().getId());
        service.excluirEndereco(novoEndereco1.getBody().getId());

        assertEquals(HttpStatus.OK, novoEndereco.getStatusCode());
        assertEquals(HttpStatus.OK, novoEndereco1.getStatusCode());
    }

    //TODO: Falta testar o deletarEnderecosComErro

}
