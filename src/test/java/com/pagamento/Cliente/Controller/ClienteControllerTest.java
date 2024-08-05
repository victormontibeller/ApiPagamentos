package com.pagamento.Cliente.Controller;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pagamento.Cliente.DTO.ClienteDTO;
import com.pagamento.Cliente.Excecoes.ResourceNotFoundException;
import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Service.ClienteService;
import com.pagamento.utils.utils;

public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

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
    void testBuscarClientes_WhenClientesExist_ReturnsOkStatus() throws ResourceNotFoundException {
        // Arrange
        List<Cliente> clientes = List.of(utils.criarClienteTeste(), utils.criarClienteTeste1());
        when(clienteService.buscarClientes()).thenReturn(clientes);

        // Act
        ResponseEntity<List<Cliente>> response = clienteController.buscarClientes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testBuscarClientes_WhenNoClientesExist_ReturnsOkStatus() throws ResourceNotFoundException {
        // Arrange
        when(clienteService.buscarClientes()).thenReturn(List.of());

        // Act
        ResponseEntity<List<Cliente>> response = clienteController.buscarClientes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testBuscarCliente_whenClienteExists_returnsCliente() throws ResourceNotFoundException {
        // Arrange
        long id = 1L;
        Cliente expectedCliente = utils.criarClienteTeste();
        when(clienteService.buscarCliente(id)).thenReturn(expectedCliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.buscarCliente(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCliente, response.getBody());
    }

    @Test
    public void testBuscarCliente_whenClienteDoesNotExist_throwsResourceNotFoundException() throws ResourceNotFoundException {
        
        long id = 1L;
        // Arrange
        when(clienteService.buscarCliente(id)).thenThrow(ResourceNotFoundException.class);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteController.buscarCliente(id));
    }

    @Test
    public void testBuscarClientePorEmail() throws ResourceNotFoundException {
        Cliente cliente = utils.criarClienteTeste();

        when(clienteService.buscarClientePorEmail(cliente.getEmail())).thenReturn(cliente);

        ResponseEntity<Cliente> response = clienteController.buscarClientePorEmail(cliente.getEmail());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    public void testBuscarClientePorEmailNotFound() throws ResourceNotFoundException {
        ResponseEntity<Cliente> response = clienteController.buscarClientePorEmail(null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testBuscarClientePorCpf_ExistingCpf_ReturnsCliente() throws ResourceNotFoundException {
        // Arrange
        Cliente cliente = utils.criarClienteTeste();
        when(clienteService.buscarClientePorCpf(cliente.getCpf())).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.buscarClientePorCpf(cliente.getCpf());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    public void testBuscarClientePorCpf_NonExistingCpf_ReturnsNotFound() throws ResourceNotFoundException {
        // Arrange
        String cpf = "12345678901";
        
        when(clienteService.buscarClientePorCpf(cpf)).thenThrow(new ResourceNotFoundException("Cliente not found"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteController.buscarClientePorCpf(cpf));
    }

    @Test
    public void testBuscarClientePorCpf_NullCpf_ReturnsBadRequest() throws ResourceNotFoundException {
        ResponseEntity<Cliente> response = clienteController.buscarClientePorCpf(null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testCriarClienteValid() throws ResourceNotFoundException {
        // Arrange
        ClienteDTO clienteDTO = clienteService.toDTO(utils.criarClienteTeste());
        when(clienteService.criarCliente(clienteDTO)).thenReturn(clienteDTO);

        // Act
        ResponseEntity<ClienteDTO> response = clienteController.criarCliente(clienteDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clienteDTO, response.getBody());
        verify(clienteService, times(1)).criarCliente(clienteDTO);
    }

    @Test
    public void testCriarClienteInvalid() throws ResourceNotFoundException {
        // Arrange
        ClienteDTO clienteDTO = clienteService.toDTO(new Cliente());
        when(clienteService.criarCliente(clienteDTO)).thenThrow(new ResourceNotFoundException());

        // Act
        assertThrows(ResourceNotFoundException.class, () -> clienteController.criarCliente(clienteDTO));
        verify(clienteService, times(1)).criarCliente(clienteDTO);
    }

    @Test
    public void testExcluirCliente_ClientExists_SuccessMessageAndHttpStatus200() throws ResourceNotFoundException {
        // Arrange
        long id = 1L;
        String expectedMessage = "Cliente excluído com sucesso!";
        HttpStatus expectedStatusCode = HttpStatus.OK;

        // Mock the clienteService to return the expected message
        Mockito.when(clienteService.excluirCliente(id)).thenReturn(expectedMessage);

        // Act
        ResponseEntity<String> response = clienteController.excluirCliente(id);

        // Assert
        assertEquals(expectedMessage, response.getBody());
        assertEquals(expectedStatusCode, response.getStatusCode());
    }


}
