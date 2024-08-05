package com.pagamento.Cliente.Controller;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    //@Test
    void testBuscarClientes_WhenExceptionThrown_ReturnsInternalServerErrorStatus() throws ResourceNotFoundException {
        // Arrange
        doThrow(ResourceNotFoundException.class).when(clienteService).buscarClientes();

        // Act
        ResponseEntity<List<Cliente>> response = clienteController.buscarClientes();

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteController.buscarClientes());
}
}
