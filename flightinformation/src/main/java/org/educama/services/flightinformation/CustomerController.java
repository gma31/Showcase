package org.educama.services.flightinformation;

import java.util.List;

import org.educama.services.flightinformation.hello.Airport;
import org.educama.services.flightinformation.hello.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
	@Autowired
	AirportRepository airportRepository;

	@RequestMapping("/airport")
	public List<Airport> getAirport(@RequestParam(value = "iata", defaultValue = "FRA") String iata) {
		return airportRepository.findByIataCode(iata);
	}

	@RequestMapping("/airport/suggestions")
	public List<Airport> getAirportAirport(@RequestParam(value = "term", defaultValue = "FR") String term) {
		return airportRepository.findByIataCodeLike(term);
	}

}
