package com.pagamento.utils;

import java.time.LocalDate;

import com.pagamento.Cliente.Model.Cliente;
import com.pagamento.Cliente.Model.Endereco;

public class utils {

    public static Cliente criarClienteTeste() {
        return new Cliente(1L,
                           "João almeida dos Santos",
                           "joao@example.com",
                           "334.750.780-07",
                           LocalDate.of(1990,01,01),
                           criarEnderecoTeste());

    }
    
    public static Endereco criarEnderecoTeste() {
        return new Endereco(2l,
                            "Rua Teste jjjjjjj",
                            "123",
                            "12345-000",
                            "apto 10", null);
    }

    public static Cliente criarClienteTeste1() {
        return new Cliente(3L,
                           "Joaquim pasquale pereira",
                           "123Oliveira@example.com",
                           "445.938.640-20",
                           LocalDate.of(1993,6,01),
                           criarEnderecoTeste1());

    }
    
    public static Endereco criarEnderecoTeste1() {
        return new Endereco(4l,
                            "Rua Teste kkkkk",
                            "445",
                            "12300-222",
                            "apto 12", null);
    }
}
