package com.pagamento.Cliente.Service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
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

import com.pagamento.Cliente.Excecoes.ResourceNotFoundException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Repository.ClienteRepository;
import com.pagamento.utils.utils;

@SpringBootTest
@Testcontainers
class ClienteRepositoryIT {
    
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

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        var novoCliente1 = clienteService.toDTO(utils.criarClienteTeste());
        clienteRepository.save(clienteService.toEntity(novoCliente1));
        var novoCliente2 = clienteService.toDTO(utils.criarClienteTeste1());
        clienteRepository.save(clienteService.toEntity(novoCliente2));
    }

    @AfterEach
    void tearDelete() {
        clienteRepository.deleteAll();
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

    @Test
    void testBuscarClientes() throws ResourceNotFoundException {
        List<Cliente> clienteBuscado = clienteService.buscarClientes();
        
        assertNotNull(clienteBuscado);
        assertTrue(!clienteBuscado.isEmpty());
        assertTrue(clienteBuscado.size() == 2);
        
    }

    @Test
    void testDeletarClientePorId() throws ResourceNotFoundException {
        var cliente = clienteService.buscarClientePorCpf("445.938.640-20");
        clienteService.excluirCliente(cliente.getId());
        assertThrows(ResourceNotFoundException.class, 
                    () -> clienteService.buscarClientePorCpf("445.938.640-20"));
    }

    @Test
    void testCriarCliente() throws ResourceNotFoundException {
        var novoCliente = clienteService.toDTO(new Cliente(5l, 
                                                           "um nome que eu quiser", 
                                                           "emailTeste@teste.com", 
                                                           "10614072093", 
                                                           LocalDate.now(), 
                                                           utils.criarEnderecoTeste()));
        clienteService.criarCliente(novoCliente);
        assertNotNull(clienteService.buscarClientePorCpf(novoCliente.cpf()));
        assertTrue(novoCliente.cpf().equals(clienteService.buscarClientePorCpf(novoCliente.cpf()).getCpf()));
    }

    @Test
    void testClienteFindByEmail() throws ResourceNotFoundException {
        var clienteEncontrado = clienteService.buscarClientePorEmail("joao@example.com");
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste().getNome(), clienteEncontrado.getNome());
        assertEquals(utils.criarClienteTeste().getCpf(), clienteEncontrado.getCpf());
        assertEquals(utils.criarClienteTeste().getEmail(), clienteEncontrado.getEmail());
        assertEquals(utils.criarClienteTeste().getNascimento(), clienteEncontrado.getNascimento());
    }

    @Test
    void testClienteFindByCpf() throws ResourceNotFoundException {
        var clienteEncontrado = clienteService.buscarClientePorCpf("445.938.640-20");
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste1().getNome(), clienteEncontrado.getNome());
        assertEquals(utils.criarClienteTeste1().getCpf(), clienteEncontrado.getCpf());
        assertEquals(utils.criarClienteTeste1().getEmail(), clienteEncontrado.getEmail());
        assertEquals(utils.criarClienteTeste1().getNascimento(), clienteEncontrado.getNascimento());
    }

    @Test
    void testBuscarClientePorId() throws ResourceNotFoundException {
        var clienteEncontrado = clienteService.buscarClientePorCpf("445.938.640-20");
        var ClienteEncontradoPorId = clienteService.buscarCliente(clienteEncontrado.getId());
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste1().getNome(), ClienteEncontradoPorId.getNome());
        assertEquals(utils.criarClienteTeste1().getCpf(), ClienteEncontradoPorId.getCpf());
        assertEquals(utils.criarClienteTeste1().getEmail(), ClienteEncontradoPorId.getEmail());
        assertEquals(utils.criarClienteTeste1().getNascimento(), ClienteEncontradoPorId.getNascimento());
    }

    @Test
    void testBuscarClientePorNome() throws ResourceNotFoundException {
        var clienteEncontrado = clienteService.buscarClientePorNome("Joaquim pasquale pereira");
        
        assertNotNull(clienteEncontrado);
        assertEquals(utils.criarClienteTeste1().getNome(), clienteEncontrado.getNome());
        assertEquals(utils.criarClienteTeste1().getCpf(), clienteEncontrado.getCpf());
        assertEquals(utils.criarClienteTeste1().getEmail(), clienteEncontrado.getEmail());
        assertEquals(utils.criarClienteTeste1().getNascimento(), clienteEncontrado.getNascimento());
    }

    @Test
    void testBuscarClientePorIdfalha_throwsResourceNotFoundException () {        
        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarCliente(100l));
    }

    @Test
    void testBuscarPorEmailClientefalha_throwsResourceNotFoundException () {        
        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarClientePorEmail("antonio@teste.com"));
    }

    @Test
    void testBuscarPorNomeClientefalha_throwsResourceNotFoundException () {        
        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarClientePorNome("açsldk"));
    }

    @Test
    void testBuscarPorNomeCpfFalha_throwsResourceNotFoundException () {        
        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarClientePorCpf("açsldk"));
    }

    @Test
    void testExcluirClienteFalha_throwsResourceNotFoundException () {        
        assertThrows(ResourceNotFoundException.class, () -> clienteService.excluirCliente(100l));
    }

    @Test
    void testCriarClienteComCpfInvalido_throwsResourceNotFoundException () {
        var novoCliente = new Cliente(1L,
                           "João almeida dos Santos",
                           "joao@example.com",
                           "33478007",
                           LocalDate.of(1990,01,01),
                           utils.criarEnderecoTeste());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.criarCliente(clienteService.toDTO(novoCliente)));   
    }

    @Test
    void testCriarClienteNull_throwsResourceNotFoundException () {
        var novoCliente = new Cliente();
        assertThrows(NullPointerException.class, () -> clienteService.criarCliente(clienteService.toDTO(novoCliente)));
    }
}