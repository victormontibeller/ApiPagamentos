package com.pagamento.Usuario.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

    @DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);
	}

    @BeforeAll
    static void beforeAll() {
        container.start();
    }    

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		container.close();
	}

	@Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}

    @Test
    void testSaveUser() {
        Usuario user = new Usuario(12l, "administrador","administrador");
        when(repository.save(user)).thenReturn(user);
        
        var savedUser = service.saveUser(user);
        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    void testGetAllUsers() {
        Usuario user1 = new Usuario(122l, "administrador1","administrador1");
        when(repository.save(user1)).thenReturn(user1);
        Usuario user = new Usuario(12l, "administrador","administrador");
        when(repository.save(user)).thenReturn(user);
        var result = service.getAllUsers();
        
        assertNotNull(result);
    }

    @Test
    void testGetUserById() {
        Usuario user = new Usuario(12l, "administrador","administrador");
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        
        var userEncontrado = service.getUserById(user.getId());        

        assertNotNull(user);
        assertEquals(userEncontrado.get().getUsername(), user.getUsername());
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;
        Usuario userDetails = new Usuario();
        userDetails.setUsername("newUsername");
        userDetails.setPassword("newPassword");
        Usuario user = new Usuario();
        user.setId(id);
        user.setUsername("oldUsername");
        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        Usuario updatedUser = service.updateUser(id, userDetails);

        assertEquals("newUsername", updatedUser.getUsername());
        verify(repository, times(1)).save(user);               
    }

   @Test
    void testDeleteUser() {
        Usuario user = new Usuario(12l, "administrador","administrador");
        when(repository.save(user)).thenReturn(user);

        repository.deleteById(user.getId());

        verify(repository, times(1)).deleteById(user.getId());
    }

}
