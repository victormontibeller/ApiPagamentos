package com.pagamento.Usuario.Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.pagamento.Exception.ResourceNotFoundException;
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

import com.pagamento.Usuario.Model.Usuario;
import com.pagamento.Usuario.Repository.UserRepository;
import com.pagamento.Usuario.Service.UsuarioService;
import com.pagamento.utils.utils;


@SpringBootTest
@Testcontainers
public class UsuarioControllerTest {
    
    @Autowired
    private UsuarioController controller;

    private AuthenticationController authController;

    @Autowired
    private UsuarioService service;
    
    @Autowired
    private UserRepository repository;

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
     * Executes before all tests in the class.
     * Starts the container.
     */
    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    /**
     * Sets up the test environment by creating a user and saving it to the repository.
     *
     * This method is annotated with `@BeforeEach` and is called before each test method in the test class.
     * It creates a user using the `utils.criarUsuarioTeste()` method and saves it to the repository using the `repository.save()` method.
     * This ensures that a user is available for testing before each test method is executed.
     */
    @BeforeEach
    void setUp() throws ResourceNotFoundException {
        var user = utils.criarUsuarioTeste();
        service.saveUser(user);
    }

    /**
     * Closes the container after all tests have been executed.
     *
     * This method is called after all tests have been run. It closes the container to release any resources and clean up the test environment.
     */
    @AfterAll
    static void tearDown() {
        container.close();
    }

    /**
     * Cleans up the test environment by deleting all entities from the repository.
     */
    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

	/**
	 * Tests the creation of a database.
	 *
	 * This test method checks if the database is running and has been created.
	 * It asserts that the `container.isRunning()` method returns `true` and
	 * the `container.isCreated()` method also returns `true`.
	 *
	 */
    @Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}

    /**
     * Test the createUser method of the UsuarioController class.
     *
     * This test creates a new user using the createUser method of the UsuarioController class,
     * and asserts that the status code of the response entity is HttpStatus.OK and that the
     * username of the returned user object is "Pedro Almeida".
     *
     * @return         	void
     */
    @Test
    void testCreateUser() throws Exception {
        ResponseEntity<Usuario> user = (ResponseEntity<Usuario>) authController.criarUsuario(utils.criarUsuarioTeste());

        assertEquals(HttpStatus.OK, user.getStatusCode());
        assertEquals("Pedro Almeida", user.getBody().getUsername());
    }

    /**
     * Test the deleteUser method of the UsuarioController class.
     *
     * This test creates a new user using the createUser method of the UsuarioController class,
     * deletes the user using the deleteUser method, and asserts that the status code of the
     * response entity is HttpStatus.OK and that the user is no longer found in the repository.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testDeleteUser() throws Exception {
        ResponseEntity<Usuario> user = (ResponseEntity<Usuario>) authController.criarUsuario(utils.criarUsuarioTeste());
        controller.deleteUser(user.getBody().getId());

        assertEquals(HttpStatus.OK, user.getStatusCode());
        assertNull(repository.findById(user.getBody().getId()).orElse(null));
    }

    /**
     * Test the getAllUsers method of the UsuarioController class.
     *
     * This test creates two new users using the createUser method of the UsuarioController class,
     * retrieves all users using the getAllUsers method, and asserts that the number of users returned
     * is 2 and that the response entity has a body.
     *
     */
    @Test
    void testGetAllUsers() throws Exception {
        ResponseEntity<Usuario> user = (ResponseEntity<Usuario>) authController.criarUsuario(utils.criarUsuarioTeste());
        ResponseEntity<Usuario> user1 = (ResponseEntity<Usuario>) authController.criarUsuario(utils.criarUsuarioTeste1());

        ResponseEntity<List<Usuario>> users = controller.getAllUsers();
        assertEquals(2, users.getBody().size());
        assertTrue(users.hasBody());
    }

    /**
     * Test the getUserById method of the UsuarioController class.
     *
     * This test creates a new user using the createUser method of the UsuarioController class,
     * retrieves the user by their ID using the getUserById method, and asserts that the status code
     * of the response entity is HttpStatus.OK and that the username of the returned user object
     * is "Pedro Almeida".
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testGetUserById() throws Exception {
        ResponseEntity<Usuario> user = (ResponseEntity<Usuario>) authController.criarUsuario(utils.criarUsuarioTeste());

        ResponseEntity<Usuario> userEncontrado = controller.getUserById(user.getBody().getId());
        assertEquals(HttpStatus.OK, userEncontrado.getStatusCode());
        assertEquals("Pedro Almeida", userEncontrado.getBody().getUsername());
    }

    /**
     * Test the updateUser method of the UsuarioController class.
     *
     * This test creates a new user using the createUser method of the UsuarioController class,
     * updates the username of the user to "Paulo Almeida", and then calls the updateUser method
     * with the id and updated user object. It asserts that the status code of the response entity
     * is HttpStatus.OK and that the username of the returned user object is "Paulo Almeida".
     *
     * @throws Exception if an error occurs during the test
     */
//    @Test
//    void testUpdateUser() throws Exception {
//        ResponseEntity<Usuario> user = (ResponseEntity<Usuario>) authController.criarUsuario(utils.criarUsuarioTeste());
//        user.getBody().setUsername("Paulo Almeida");
//
//        ResponseEntity<Usuario> userEncontrado = controller.updateUser(user.getBody().getId(), user.getBody());
//        assertEquals(HttpStatus.OK, userEncontrado.getStatusCode());
//        assertEquals("Paulo Almeida", userEncontrado.getBody().getUsername());
//    }
}
