package com.fit.ecommercialmarketplacebe;

import com.fit.ecommercialmarketplacebe.entity.Role;
import com.fit.ecommercialmarketplacebe.entity.User;
import com.fit.ecommercialmarketplacebe.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ECommercialMarketPlaceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommercialMarketPlaceBeApplication.class, args);
    }



}
