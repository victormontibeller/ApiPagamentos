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

    @BeforeEach
    void setUp() {  autoCloseable = MockitoAnnotations.openMocks(this);     }


    @AfterEach
    void tearDown() throws Exception {    autoCloseable.close();    }


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

    @Test
    public void testGetUserById() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(utils.criarUsuarioTeste()));
        var user = repository.findById(id);
        assertNotNull(user);
    }

    @Test
    public void testGetAllUsers() {
        when(repository.findAll()).thenReturn(Collections.singletonList(utils.criarUsuarioTeste()));
        var users = repository.findAll();
        assertNotNull(users);
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;
        repository.deleteById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdateUser() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(utils.criarUsuarioTeste()));
        when(repository.save(utils.criarUsuarioTeste())).thenReturn(utils.criarUsuarioTeste());
        repository.save(utils.criarUsuarioTeste());
        verify(repository, times(1)).save(utils.criarUsuarioTeste());
    }
}
