package com.pagamento.Cliente.Controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pagamento.Cliente.Excecoes.ResourceNotFoundException;
import com.pagamento.Cliente.Excecoes.ServiceException;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Service.EnderecoService;
import com.pagamento.utils.utils;

public class EnderecoControllerTest {

    @Mock
    private EnderecoService service;

    @InjectMocks
    private EnderecoController controller;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    //AINDA PRECISA SER REVISADO
    @Test
    void shouldReturnOkResponseWithListOfEnderecos() throws ResourceNotFoundException {
        List<Endereco> enderecos = List.of(utils.criarEnderecoTeste(), 
                                           utils.criarEnderecoTeste1());
        when(service.buscarEnderecos()).thenReturn(enderecos);

        ResponseEntity<List<Endereco>> response = controller.buscarEnderecos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enderecos, response.getBody());
    }

    //@Test
    void shouldThrowServiceException() throws ResourceNotFoundException {
        when(service.buscarEnderecos()).thenReturn(List.of());

        ResponseEntity<List<Endereco>> response = controller.buscarEnderecos();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
