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


    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.SELLER);
                admin.setFullName("Nguyen Van Admin");
                admin.setPhone("0909999999");
                admin.setAddress("HCM City");

                userRepository.save(admin);
                System.out.println("✅ Created default admin user");
            }

            if (userRepository.findByUsername("buyer").isEmpty()) {
                User buyer = new User();
                buyer.setUsername("buyer");
                buyer.setPassword(passwordEncoder.encode("buyer123"));
                buyer.setRole(Role.BUYER);
                buyer.setFullName("Nguyen Van Buyer");
                buyer.setPhone("0911222333");
                buyer.setAddress("Ha Noi");
                userRepository.save(buyer);
                System.out.println("✅ Created default buyer user");
            }

        };
    }
}
