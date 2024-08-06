package com.pagamento.Cliente.Excecoes;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}

