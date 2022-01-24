package com.example.railwaysystem.connection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Configuration
public class ConnectionConfig {

    @Bean
    CommandLineRunner commandLineRunner(ConnectionRepository repository) {
        return args -> {
            Connection wroclawWarsaw = new Connection(
                    "Wroclaw Main Station",
                    LocalDateTime.of(2022, 1, 24, 10, 30),
                    3,
                    "Warsaw West Station",
                    LocalDateTime.of(2022, 1, 24, 15, 39),
                    "IC",
                    65.75D
            );

            Connection wroclawCracow = new Connection(
                    "Wroclaw Main Station",
                    LocalDateTime.of(2022, Month.JANUARY, 25, 10, 36),
                    2,
                    "Cracow Main Station",
                    LocalDateTime.of(2022, Month.JANUARY, 25, 14, 02),
                    "IC",
                    37.50D
            );

            repository.saveAll(
                    List.of(wroclawWarsaw, wroclawCracow)
            );
        };
    }
}
