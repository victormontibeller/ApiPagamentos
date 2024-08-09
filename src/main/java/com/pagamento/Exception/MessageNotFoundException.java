package com.pagamento.Exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String ex) {
        super(ex);
    }
}