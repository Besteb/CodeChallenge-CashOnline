package com.cash_online.Code_Challenge.config;

import com.cash_online.Code_Challenge.models.Loan;
import com.cash_online.Code_Challenge.models.User;
import com.cash_online.Code_Challenge.repositories.LoanRepository;
import com.cash_online.Code_Challenge.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    @Profile("!test")
    CommandLineRunner initDatabase(UserRepository userRepository, LoanRepository loanRepository) {
        User user1 = new User("user1@email.com", "User", "One");
        User user2 = new User("user2@email.com", "User", "Two");

        return args -> {
            log.info("Preloading" + userRepository.save(user1));
            log.info("Preloading" + userRepository.save(user2));
            log.info("Preloading" + loanRepository.save(new Loan(123.00, user1.getId())));
            log.info("Preloading" + loanRepository.save(new Loan(1000.00, user1.getId())));
            log.info("Preloading" + loanRepository.save(new Loan(2300.00, user2.getId())));
        };
    }

}
