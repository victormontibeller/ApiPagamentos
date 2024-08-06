package com.pagamento.Cliente.Repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pagamento.Cliente.Excecoes.ServiceException;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Service.EnderecoService;
import com.pagamento.utils.utils;

public class EnderecoRepositoryTest {

    @Mock
    private EnderecoRepository repository;

    @InjectMocks
    private EnderecoService service;

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
    void testCriarEndereco() {
        Endereco endereco = utils.criarEnderecoTeste();

        when(repository.save(endereco)).thenReturn(endereco);

        var enderecoSalvo = repository.save(endereco);

        assertNotNull(enderecoSalvo);
        assertEquals(endereco.getRua(), enderecoSalvo.getRua());
        assertEquals(endereco.getNumero(), enderecoSalvo.getNumero());
        assertEquals(endereco.getCep(), enderecoSalvo.getCep());
        assertEquals(endereco.getComplemento(), enderecoSalvo.getComplemento());
    }

    @Test
    void testEnderecoFindById() {
        Endereco endereco = utils.criarEnderecoTeste();
        when(repository.findById(2L)).thenReturn(java.util.Optional.ofNullable(endereco));
        
        var enderecoEncontrado = repository.findById(2L);
        
        assertNotNull(enderecoEncontrado);
        assertEquals(endereco.getRua(), enderecoEncontrado.get().getRua());
        assertEquals(endereco.getNumero(), enderecoEncontrado.get().getNumero());
        assertEquals(endereco.getCep(), enderecoEncontrado.get().getCep());
        assertEquals(endereco.getComplemento(), enderecoEncontrado.get().getComplemento());
    }

    @Test
    void testBuscarEnderecos_throwsServiceException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class,
                () -> service.buscarEnderecos());
    }

    @Test
    void testBuscarEnderecos() throws ServiceException {
        List<Endereco> enderecos = List.of(utils.criarEnderecoTeste(), 
                                           utils.criarEnderecoTeste1());
        when(repository.findAll()).thenReturn(enderecos);

        List<Endereco> result = service.buscarEnderecos();

        assertEquals(enderecos, result);
    }

    @Test
    void testExcluirEndereco() throws ServiceException {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(java.util.Optional.ofNullable(utils.criarEnderecoTeste()));
        when(repository.save(utils.criarEnderecoTeste())).thenReturn(utils.criarEnderecoTeste());
        repository.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }




}
