package com.pagamento.Usuario.Controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.pagamento.Usuario.Repository.UserRepository;
import com.pagamento.Usuario.Service.UsuarioService;
import com.pagamento.utils.utils;


@SpringBootTest
@Testcontainers
public class UsuarioControllerTest {
    
    @Autowired
    private UsuarioController controller;

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
        var user = utils.criarUsuarioTeste();
        repository.save(user);
    }

    @AfterAll
    static void tearDown() {
        container.close();
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
	void testeCriarBancoDeDados(){
		assertTrue(container.isRunning());
		assertTrue(container.isCreated());
	}

    @Test
    void testCreateUser() {

    }

    @Test
    void testDeleteUser() {

    }

    @Test
    void testGetAllUsers() {

    }

    @Test
    void testGetUserById() {

    }

    @Test
    void testUpdateUser() {

    }
}
