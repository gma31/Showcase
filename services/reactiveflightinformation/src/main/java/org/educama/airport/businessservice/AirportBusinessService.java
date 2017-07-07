package org.educama.airport.businessservice;

import org.educama.airport.datafeed.AirportCsvDeserializer;
import org.educama.airport.model.Airport;
import org.educama.airport.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Airports.
 */
@Component
public class AirportBusinessService {


    private AirportRepository airportRepository;
    private AirportCsvDeserializer airportCsvDeserializer;

    protected static final int MAX_SUGGESTIONS = 10;

    @Autowired
    public AirportBusinessService(AirportRepository airportRepository, AirportCsvDeserializer airportCsvDeserializer) {
        this.airportRepository = airportRepository;
        this.airportCsvDeserializer = airportCsvDeserializer;
    }

    public Flux<Airport> findAllAirports() {
        return airportRepository.findAll()
                .log("org.educama.airport.businessservice");
    }

    public Mono<Airport> findAirportByIataCode(String iataCode) {
        if (StringUtils.isEmpty(iataCode)) {
            return Mono.empty();
        }
        return airportRepository.findByIataCodeIgnoreCase(iataCode)
                .log("org.educama.airport.businessservice");
    }

    public Mono<Airport> findAirportByIcaoCode(String icaoCode) {
        if (StringUtils.isEmpty(icaoCode)) {
            return Mono.empty();
        }
        return airportRepository.findByIcaoCodeIgnoreCase(icaoCode)
                .log("org.educama.airport.businessservice");
    }
    public Mono<Airport>findAirportsByIataCodeOrIcaoCode(String airportCode){
        if (StringUtils.isEmpty(airportCode)) {
            return Mono.empty();
        }
        return airportRepository.findByAirportCode(airportCode);
    }
    public Flux<Airport> findAirportSuggestionsBySearchTerm(String term) {
        if (StringUtils.isEmpty(term)) {
            return Flux.empty();
        }
        return airportRepository.findBySearchTerm(term);
    }

    public void clearAndImportAirports(MultipartFile file) throws IOException {
        List<Airport> airports = airportCsvDeserializer.deserialize(file.getInputStream());
        airportRepository.deleteAll();
        airports.stream()
                .forEach(a -> airportRepository.save(a));
    }

}
