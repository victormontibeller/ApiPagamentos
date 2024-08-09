package com.pagamento.Exception;

public class LimiteCartaoException extends RuntimeException {
    public LimiteCartaoException(String ex) {
        super(ex);
    }
}