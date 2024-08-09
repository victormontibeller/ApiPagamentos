package com.pagamento.Cliente.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "endereco")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Endereco {

    @Id
    // @Column(unique = true)
    // ENTENDO QUE DUAS PESSOAS RESIDENTES NO MESMO ENDEREÇO PODEM TER CADASTROS EM UM MESMO ENDERECO
    // PORTANTO NÃO CABE UMA CHAVE PRIMARIA PARA TABELA ENDERECO
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotEmpty(message = "O nome da rua não pode estar vazio.")
    @Size(min = 10, max = 50, message = "O nome da rua deve ter entre 10 e 50 caracteres")
    private String rua;

    @Column(nullable = false)
    @NotEmpty(message = "O número não pode estar vazio.")
    private String numero;

    @Column(nullable = false)
    @NotEmpty(message = "O CEP não pode estar vazio.")
    private String cep;

    @Column(nullable = false)
    @NotEmpty(message = "O complemento não pode estar vazio.")
    private String complemento;

    @OneToMany(mappedBy = "endereco")
    List<Cliente> clientes;
}
