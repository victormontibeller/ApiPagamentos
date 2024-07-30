package com.pagamento.Usuario.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "12345"; // Substitua pela senha que deseja codificar
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("password -> " + encodedPassword);
    }
}
