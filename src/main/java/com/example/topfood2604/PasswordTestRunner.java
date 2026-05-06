package com.example.topfood2604;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordTestRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    public PasswordTestRunner(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        System.out.println("123456 加密後 = " + passwordEncoder.encode("123456"));
    }
}