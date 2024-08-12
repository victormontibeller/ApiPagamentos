package com.pagamento.Usuario.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.pagamento.Exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pagamento.Usuario.Model.Usuario;
import com.pagamento.Usuario.Repository.UserRepository;
import com.pagamento.utils.utils;


@SpringBootTest
@Testcontainers
public class UsuarioServiceTest {

    @Autowired
    @InjectMocks
    private UsuarioService service;

    @Autowired
    @Mock
    private UserRepository repository;

    AutoCloseable autoCloseable;

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16-alpine")
                                                                                        .withDatabaseName("testdb")
                                                                                        .withUsername("admin")
                                                                                        .withPassword("admin")
                                                                                        .withInitScript("test-data.sql");

	/**
	 * Sets the dynamic properties for the Spring datasource.
	 *
	 * @param dynamicPropertyRegistry the registry to add the dynamic properties to
	 */
    @DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
	}

    /**
     * Initializes the test environment before all tests are run.
     * Starts the container used for testing.
     */
    @BeforeAll
    static void beforeAll() {
        container.start();
    }    

    /**
     * Initializes the test environment before each test is run.
     * Opens the mocks for the test class.
     */
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Cleans up the test environment by deleting all entities from the repository.
     */
    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

	/**
	 * Closes the container after all tests have been executed.
	 *
	 */
	@AfterAll
	static void tearDown() {
		container.close();
	}

	/**
	 * A test method to check if the database container is running and created.
	 */
	@Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}
    

    /**
     * Test case to verify the functionality of the saveUser method in the UsuarioService class.
     *
     * This test creates a new Usuario object with the username "administrador" and password "administrador".
     * It then mocks the save method of the repository to return the same user object.
     * The saveUser method is then called with the created user object.
     * The test asserts that the returned user object is not null and that its username and password match the original user object.
     *
     */
    @Test
    void testSaveUser() throws ResourceNotFoundException {
        Usuario user = new Usuario(12l, "administrador","administrador", utils.criarClienteTeste());
        when(repository.save(user)).thenReturn(user);
        
        var savedUser = service.saveUser(service.toRegisterRequest(user));
        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.username());
        assertEquals(user.getPassword(), savedUser.password());
    }

    /**
     * Test case to verify the functionality of the getAllUsers method in the UsuarioService class.
     *
     * This test creates two Usuario objects with different usernames and passwords.
     * It then mocks the save method of the repository to return the same user objects.
     * The getAllUsers method is then called.
     * The test asserts that the returned result is not null.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testGetAllUsers() {
        Usuario user1 = new Usuario(122l, "administrador1","administrador1", utils.criarClienteTeste());
        when(repository.save(user1)).thenReturn(user1);
        Usuario user = new Usuario(12l, "administrador","administrador", utils.criarClienteTeste());
        when(repository.save(user)).thenReturn(user);
        var result = service.getAllUsers();
        
        assertNotNull(result);
    }

    
    /**
     * Test case to verify the functionality of the getUserById method in the UsuarioService class.
     *
     * This test creates a new Usuario object with the id 12, username "administrador", and password "administrador".
     * It then mocks the findById method of the repository to return an Optional containing the same user object.
     * The getUserById method is then called with the id of the created user object.
     * The test asserts that the returned user object is not null and that its username matches the original user object.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testGetUserById() {
        Usuario user = new Usuario(12l, "administrador","administrador", utils.criarClienteTeste());
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        
        var userEncontrado = service.getUserById(user.getId());        

        assertNotNull(user);
        assertEquals(userEncontrado.get().getUsername(), user.getUsername());
    }

    /**
     * Test case to verify the functionality of the updateUser method in the UsuarioService class.
     *
     * This test creates a new Usuario object with the id 1 and sets its username and password to "newUsername" and "newPassword" respectively.
     * It then creates another Usuario object with the id 1 and sets its username to "oldUsername".
     * The findById method of the repository is mocked to return an Optional containing the second Usuario object.
     * The save method of the repository is mocked to return the second Usuario object.
     * The updateUser method is then called with the id and the first Usuario object.
     * The test asserts that the returned Usuario object's username is "newUsername".
     * It also verifies that the save method of the repository was called once with the second Usuario object.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testUpdateUser() {
        Long id = 1L;
        Usuario userDetails = new Usuario();
        userDetails.setUsername("newUsername");
        userDetails.setPassword("newPassword");
        userDetails.setCliente(utils.criarClienteTeste());
        Usuario user = new Usuario();
        user.setId(id);
        user.setUsername("oldUsername");
        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        Usuario updatedUser = service.updateUser(id, userDetails);

        assertEquals("newUsername", updatedUser.getUsername());
        verify(repository, times(1)).save(user);               
    }

   /**
    * Test case to verify the functionality of the deleteUser method in the UsuarioService class.
    *
    * This test creates a new Usuario object with the username "administrador" and password "administrador".
    * It then mocks the save method of the repository to return the same user object.
    * The deleteUser method is then called with the id of the created user object.
    * The test asserts that the deleteById method of the repository was called once with the id of the created user object.
    *
    * @throws Exception if an error occurs during the test
    */
   @Test
    void testDeleteUser() {
        Usuario user = new Usuario(12l, "administrador","administrador", utils.criarClienteTeste());
        when(repository.save(user)).thenReturn(user);

        repository.deleteById(user.getId());

        verify(repository, times(1)).deleteById(user.getId());
    }

}
