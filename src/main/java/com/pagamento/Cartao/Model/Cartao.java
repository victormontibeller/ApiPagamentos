package com.pagamento.Cartao.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "cartao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cartao {

    @Id
    @Column(name = "numero", nullable = false, unique = true)
    @NotNull(message = "O número do cartão não pode ser nulo")
    @Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter 16 dígitos")
    private String numero;

    @Column(name = "cpf", nullable = false)
    @NotNull(message = "O CPF não pode ser nulo")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos")
    private String cpf;

    @Column(name = "limite", nullable = false)
    @NotNull(message = "O limite não pode ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "O limite deve ser maior que zero")
    private BigDecimal limite;

    @Column(name = "data_validade", nullable = false)
    @NotNull(message = "A data de validade não pode ser nula")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "A data de validade deve estar no formato MM/YY")
    private String data_validade;

    @Column(name = "cvv", nullable = false)
    @NotNull(message = "O CVV não pode ser nulo")
    @Pattern(regexp = "\\d{3}", message = "O CVV deve conter 3 dígitos")
    private String cvv;

}