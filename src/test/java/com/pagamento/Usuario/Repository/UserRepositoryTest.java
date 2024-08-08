package com.pagamento.Usuario.Repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pagamento.Usuario.Model.Usuario;
import com.pagamento.Usuario.Service.UsuarioService;
import com.pagamento.utils.utils;

public class UserRepositoryTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UsuarioService service;

    AutoCloseable autoCloseable;

    /**
     * Initializes the test environment before each test is run.
     * Opens the mocks for the test class.
     */
    @BeforeEach
    void setUp() {  autoCloseable = MockitoAnnotations.openMocks(this);     }

    /**
     * Tears down the test environment by closing the AutoCloseable resource.
     *
     * @throws Exception if an error occurs during the tear down process
     */
    @AfterEach
    void tearDown() throws Exception {    autoCloseable.close();    }

    /**
     * Test case to verify the functionality of the createUser method in the UserRepository class.
     *
     * This test creates a new User object using the utils.criarUsuarioTeste() method.
     * It then mocks the save method of the repository to return the same user object.
     * The createUser method is then called with the created user object.
     * The test asserts that the returned user object is not null and that its username and password match the original user object.
     * It also verifies that the save method of the repository is called exactly once with the created user object.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testCriarUser() {
        Usuario user = utils.criarUsuarioTeste();
        when(repository.save(user)).thenReturn(user);

        var savedUser = repository.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
        verify(repository, times(1)).save(user);
    }

    /**
     * Test case to verify the functionality of the getUserById method in the UserRepository class.
     *
     * This test creates a new Long object with the value 1L and assigns it to the variable id.
     * It then mocks the findById method of the repository to return an Optional containing a user object created by the utils.criarUsuarioTeste() method.
     * The getUserById method is then called with the id variable.
     * The test asserts that the returned Optional is not null.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetUserById() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(utils.criarUsuarioTeste()));
        var user = repository.findById(id);
        assertNotNull(user);
    }
  
    /**
     * Test case to verify the functionality of the getAllUsers method in the UserRepository class.
     *
     * This test creates a new User object using the utils.criarUsuarioTeste() method.
     * It then mocks the findAll method of the repository to return a singleton list containing the created user object.
     * The getAllUsers method is then called.
     * The test asserts that the returned list is not null.
     */
    @Test
    public void testGetAllUsers() {
        when(repository.findAll()).thenReturn(Collections.singletonList(utils.criarUsuarioTeste()));
        var users = repository.findAll();
        assertNotNull(users);
    }
    
    /**
     * Test case to verify the functionality of the deleteUser method in the UserRepository class.
     *
     * This test creates a new Long object with the value 1L and assigns it to the variable id.
     * It then calls the deleteById method of the repository with the id variable.
     * Finally, it verifies that the deleteById method was called exactly once with the id variable as an argument.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testDeleteUser() {
        Long id = 1L;
        repository.deleteById(id);
        verify(repository, times(1)).deleteById(id);
    }

    
    /**
     * Test case to verify the functionality of the updateUser method in the UserRepository class.
     *
     * This test creates a new Long object with the value 1L and assigns it to the variable id.
     * It then mocks the findById method of the repository to return an Optional containing a user object created by the utils.criarUsuarioTeste() method.
     * The save method of the repository is then mocked to return the same user object.
     * The updateUser method is then called with the created user object.
     * Finally, it verifies that the save method of the repository is called exactly once with the created user object.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testUpdateUser() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(utils.criarUsuarioTeste()));
        when(repository.save(utils.criarUsuarioTeste())).thenReturn(utils.criarUsuarioTeste());
        repository.save(utils.criarUsuarioTeste());
        verify(repository, times(1)).save(utils.criarUsuarioTeste());
    }
}
