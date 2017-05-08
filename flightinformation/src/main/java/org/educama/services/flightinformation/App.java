package org.educama.services.flightinformation;

import org.educama.services.flightinformation.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {
    @Autowired
    private AirportRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


}
