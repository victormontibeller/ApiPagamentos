package com.pagamento.utils;

import java.time.LocalDate;

import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Usuario.Model.Usuario;

public class utils {

    /**
     * Creates a test Cliente object with the given parameters.
     *
     */
    public static Cliente criarClienteTeste() {
        return new Cliente(1L,
                           "João almeida dos Santos",
                           "joao@example.com",
                           "33475078007",
                           LocalDate.of(1990,01,01),
                           criarEnderecoTeste(),
                            criarUsuarioTeste());

    }
    
    /**
     * Creates a test Endereco object with the given parameters.
     *
     */
    public static Endereco criarEnderecoTeste() {
        return new Endereco(4l,
                            "Rua Teste jjjjjjj",
                            "123",
                            "12345000",
                            "apto 10", null);
    }

    /**
     * Creates a test Cliente object with the given parameters.
     *
     */
    public static Cliente criarClienteTeste1() {
        return new Cliente(3L,
                           "Joaquim pasquale pereira",
                           "123Oliveira@example.com",
                           "44593864020",
                           LocalDate.of(1993,6,01),
                           criarEnderecoTeste1(),
                        criarUsuarioTeste1());

    }
    
    /**
     * Creates a test Endereco object.
     *
     */
    public static Endereco criarEnderecoTeste1() {
        return new Endereco(2l,
                            "Rua Teste kkkkk",
                            "445",
                            "12300222",
                            "apto 12", null);
    }

    /**
     * Creates a test Usuario object with id 1, name "Pedro Almeida", and phone number "1234567890".
     */
    public static Usuario criarUsuarioTeste() {
        return new Usuario(1L,
                           "Pedro Almeida",
                           "1234567890");
    }

    /**
     * Creates a test Usuario object with id 2, name "almeida pedro", and phone number "123456789120".
     */
    public static Usuario criarUsuarioTeste1() {
        return new Usuario(2L,
                           "almeida pedro",
                           "123456789120");
    }
}
