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

    @BeforeAll
    static void beforeAll() {
        container.start();
    }    
    @BeforeEach
    void setUp() {
        var novoCliente1 = service.toDTO(utils.criarClienteTeste());
        repository.save(service.toEntity(novoCliente1));
        var novoCliente2 = service.toDTO(utils.criarClienteTeste1());
        repository.save(service.toEntity(novoCliente2));
    
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
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}    

    //TODO: teste metodo buscarClientesControllerComSucesso
    @Test
    void testeBuscarClientesControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<List<Cliente>> clientes = controller.buscarClientes();
        assertEquals(HttpStatus.OK, clientes.getStatusCode());
        assertEquals(2, clientes.getBody().size());
    }

    //TODO: teste metodo buscarClientesControllerComErro
    @Test
    void testeBuscarClientesControllerComErro() throws ResourceNotFoundException{
        Cliente novoCliente = service.buscarClientePorCpf("44593864020");
        Cliente novoCliente1 = service.buscarClientePorCpf("33475078007");
        
        service.excluirCliente(novoCliente.getId());
        service.excluirCliente(novoCliente1.getId());
        
        assertThrows(ServiceException.class, () -> controller.buscarClientes());
    }
    
    //TODO: teste metodo buscarClientePorIdControllerComSucesso
    @Test
    void testeBuscarClientePorIdControllerComSucesso() throws ResourceNotFoundException{
        Cliente novoCliente = service.buscarClientePorCpf("44593864020");
        long id = novoCliente.getId();
      
        ResponseEntity<Cliente> cliente = controller.buscarCliente(id);
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }

    //TODO: teste metodo buscarClientePorIdControllerComErro
    @Test
    void testeBuscarClientePorIdControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarCliente(0L));
    }
    
    //TODO: teste metodo buscarClientePorEmailControllerComSucesso
    @Test
    void testeBuscarClientePorEmailControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorEmail("123Oliveira@example.com");
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }
    //TODO: teste metodo buscarClientePorEmailControllerComErro
    @Test
    void testeBuscarClientePorEmailControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarClientePorEmail("banana@bancadabanana.com"));
    }
    //TODO: teste metodo buscarClientePorCpfControllerComSucesso
    @Test
    void testeBuscarClientePorCpfControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorCpf("44593864020");
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }

    //TODO: teste metodo buscarClientePorCpfControllerComErro
    @Test
    void testeBuscarClientePorCpfControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarClientePorCpf("banana"));
    }

    //TODO: teste metodo buscarClientePorNomeControllerComSucesso
    @Test
    void testeBuscarClientePorNomeControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorNome("Joaquim");
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }
    
    //TODO: teste metodo buscarClientePorNomeControllerComErro
    @Test
    void testeBuscarClientePorNomeControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.buscarClientePorNome("banana"));
    }

    //TODO: teste metodo criarClienteControllerComSucesso
    @Test
    void testeCriarClienteControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorCpf("44593864020");
        
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
        assertEquals("Joaquim pasquale pereira", cliente.getBody().getNome());
    }
    
    //TODO: teste metodo criarClienteControllerComErro
    @Test
    void testeCriarClienteControllerComErro() throws ResourceNotFoundException{
        
        //assertEquals(httpStatus.NO_CONTENT, )
        assertThrows(ServiceException.class, 
                    () -> controller.criarCliente(service.toDTO(new Cliente())));
    }

    //TODO: teste metodo deletarClienteControllerComSucesso
    @Test
    void testeDeletarClienteControllerComSucesso() throws ResourceNotFoundException{
        ResponseEntity<Cliente> cliente = controller.buscarClientePorCpf("44593864020");
        long id = cliente.getBody().getId();
        
        controller.excluirCliente(id);
        
        assertEquals(HttpStatus.OK, cliente.getStatusCode());
    }
    //TODO: teste metodo deletarClienteControllerComErro
    @Test
    void testeDeletarClienteControllerComErro() throws ResourceNotFoundException{
        assertThrows(ServiceException.class, () -> controller.excluirCliente(0L));
    }
}

