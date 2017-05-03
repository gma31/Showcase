package org.educama.services.flightinformation;

import org.educama.services.flightinformation.hello.Airport;
import org.educama.services.flightinformation.hello.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {
	@Autowired
	private AirportRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			repository.deleteAll();
			repository.save(new Airport("Frankfurt international Airport", "FRA"));
			repository.save(new Airport("Yaounde Nsimalen international Airport", "NSI"));
			repository.save(new Airport("Los Angeles international Airport", "LAX"));
			repository.save(new Airport("Forbes Airport", "FRB"));
			repository.save(new Airport("Franca Airport", "FRC"));
			repository.save(new Airport("Los Alamos County Airport", "LAM"));
			repository.save(new Airport("Al Abraq International Airport", "LAQ"));
			repository.save(new Airport("Ladysmith Airport", "LAY"));
			repository.save(new Airport("Mandera Airport", "NDE"));
			repository.save(new Airport("Sanday Airport", "NDY"));
			repository.save(new Airport("Norseman Airport", "NSM"));
			System.out.println(repository.findByIataCode("FRA"));

		};
	}

}
