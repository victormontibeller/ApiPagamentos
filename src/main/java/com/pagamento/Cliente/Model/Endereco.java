package com.pagamento.Cliente.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "endereco")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Endereco {

    @Id
    @Column(unique = true)
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
}
