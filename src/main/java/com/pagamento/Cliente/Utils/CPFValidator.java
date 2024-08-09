package com.pagamento.Cliente.Utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CPFValidator {

    public boolean validarCPF(String cpf) {
        // Verifica se o CPF possui 11 dígitos e se é composto apenas por números
        if (cpf == null || !Pattern.matches("\\d{11}", cpf)) {
            return false;
        }

        // Calcula os dígitos verificadores
        int[] multiplicadoresPrimeiroDigito = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] multiplicadoresSegundoDigito = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        if (calcularDigitoVerificador(cpf.substring(0, 9), multiplicadoresPrimeiroDigito) != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }
        if (calcularDigitoVerificador(cpf.substring(0, 10), multiplicadoresSegundoDigito) != Character.getNumericValue(cpf.charAt(10))) {
            return false;
        }

        return true;
    }

    private int calcularDigitoVerificador(String cpfParcial, int[] multiplicadores) {
        int total = 0;
        for (int i = 0; i < cpfParcial.length(); i++) {
            total += Character.getNumericValue(cpfParcial.charAt(i)) * multiplicadores[i];
        }
        int resto = total % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }
}