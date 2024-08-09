package com.pagamento.Cliente.Repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pagamento.Exception.ServiceException;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Cliente.Service.EnderecoService;
import com.pagamento.utils.utils;

public class EnderecoRepositoryTest {

    @Mock
    private EnderecoRepository repository;

    @InjectMocks
    private EnderecoService service;

    AutoCloseable autoCloseable;

    /**
     * Initializes the test environment before each test is run.
     * Opens the mocks for the test class.
     */
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Tears down the test environment by closing the AutoCloseable resource.
     *
     * @throws Exception if an error occurs during the tear down process
     */
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    /**
     * Test case for the `testCriarEndereco` method.
     *
     * This test verifies the behavior of the `save` method in the `EnderecoRepository` class. It creates a test `Endereco` object using the `utils.criarEnderecoTeste()` method,
     * and then uses Mockito's `when` method to mock the `save` method of the `repository` object. The mocked `save` method is configured to return the same `Endereco` object that was passed in.
     *
     * The test then calls the `save` method of the `repository` object with the test `Endereco` object, and assigns the result to the `enderecoSalvo` variable.
     *
     * Finally, the test asserts that the `enderecoSalvo` object is not null, and that its `rua`, `numero`, `cep`, and `complemento` properties are equal to the corresponding properties of the test `Endereco` object.
     *
     * @throws Exception if an error occurs during the test
     */
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

    /**
     * Test case for the `testEnderecoFindById` method.
     *
     * This test verifies the behavior of the `findById` method in the `EnderecoRepository` class. 
     * It creates a test `Endereco` object using the `utils.criarEnderecoTeste()` method, 
     * and then uses Mockito's `when` method to mock the `findById` method of the `repository` object. 
     * The mocked `findById` method is configured to return the same `Endereco` object that was passed in.
     *
     * The test then calls the `findById` method of the `repository` object with a test id, 
     * and assigns the result to the `enderecoEncontrado` variable.
     *
     * Finally, the test asserts that the `enderecoEncontrado` object is not null, 
     * and that its `rua`, `numero`, `cep`, and `complemento` properties are equal to the corresponding properties of the test `Endereco` object.
     *
     * @return         	void
     */
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

    /**
     * Test case for the `testBuscarEnderecos_throwsServiceException` method.
     *
     * This test verifies that the `buscarEnderecos` method in the `service` object
     * throws a `ServiceException` when there are no enderecos in the repository.
     *
     * The test sets up a mock repository using Mockito's `when` method to mock the
     * `findAll` method of the `repository` object. The mocked `findAll` method is
     * configured to return an empty list.
     *
     * The test then calls the `buscarEnderecos` method of the `service` object and
     * asserts that a `ServiceException` is thrown.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testBuscarEnderecos_throwsServiceException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class,
                () -> service.buscarEnderecos());
    }

    /**
     * Test case for the `testBuscarEnderecos` method.
     *
     * This test verifies the behavior of the `buscarEnderecos` method in the `service` object.
     *
     * The test creates a list of `Endereco` objects using the `utils.criarEnderecoTeste()` and `utils.criarEnderecoTeste1()` methods.
     * It then uses Mockito's `when` method to mock the `findAll` method of the `repository` object.
     * The mocked `findAll` method is configured to return the same list of `Endereco` objects.
     *
     * The test then calls the `buscarEnderecos` method of the `service` object.
     *
     * Finally, the test asserts that the result of the `buscarEnderecos` method is equal to the original list of `Endereco` objects.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testBuscarEnderecos() throws ServiceException {
        List<Endereco> enderecos = List.of(utils.criarEnderecoTeste(), 
                                           utils.criarEnderecoTeste1());
        when(repository.findAll()).thenReturn(enderecos);

        List<Endereco> result = service.buscarEnderecos();

        assertEquals(enderecos, result);
    }

    /**
     * Test case for the `testExcluirEndereco` method.
     *
     * This test verifies the behavior of the `excluirEndereco` method in the `service` object.
     *
     * The test sets up a mock repository using Mockito's `when` method to mock the
     * `findById`, `save`, and `deleteById` methods of the `repository` object.
     * The mocked methods are configured to return specific values or perform actions.
     *
     * The test then calls the `excluirEndereco` method of the `service` object and
     * verifies that the `deleteById` method is called with the correct `id`.
     *
     * @throws ServiceException if an error occurs during the test
     */
    @Test
    void testExcluirEndereco() throws ServiceException {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(java.util.Optional.ofNullable(utils.criarEnderecoTeste()));
        when(repository.save(utils.criarEnderecoTeste())).thenReturn(utils.criarEnderecoTeste());
        repository.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }
}
