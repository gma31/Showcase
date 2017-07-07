package org.educama.airline.businessservice;

import org.educama.airline.datafeed.AirlineCsvDeserializer;
import org.educama.airline.model.Airline;
import org.educama.airline.repository.AirlineRepository;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for airlines.
 */
@Component
public class AirlineBusinessService {


    private AirlineRepository airlineRepository;


    private AirlineCsvDeserializer airlineCsvDeserializer;

    protected static final int MAX_SUGGESTIONS = 10;

    @Autowired
    public AirlineBusinessService(AirlineRepository airlineRepository, AirlineCsvDeserializer airlineCsvDeserializer) {
        this.airlineRepository = airlineRepository;
        this.airlineCsvDeserializer = airlineCsvDeserializer;
    }

    public Flux<Airline> findAllAirlines() {
        return airlineRepository.findAll()
                .log("org.educama.airline.businessservice");
    }

    public Mono<Airline> findAirlinesByIataCode(String iataCode) {
        if (StringUtils.isEmpty(iataCode)) {
            return Mono.empty();
        }
        return airlineRepository.findByIataCodeIgnoreCase(iataCode)
                .log("org.educama.airline.businessservice");
    }

    public Mono<Airline> findAirlinesByIcaoCode(String icaoCode) {
        if (StringUtils.isEmpty(icaoCode)) {
            Mono.empty();
        }
        return airlineRepository.findByIcaoCodeIgnoreCase(icaoCode)
                .log("org.educama.airline.businessservice");
    }

    public Flux<Airline> findAirlineSuggestionsBySearchTerm(String term) {
        if (StringUtils.isEmpty(term)) {
            return Flux.empty();
        }
        return airlineRepository.findBySearchTerm(term);
    }
    public Mono<Airline> findByAirlineByIataCodeOrIcaoCode(String airportCode) {
        return airlineRepository.findByAirportCode(airportCode);
    }

    public void clearAndImportAirlines(MultipartFile file) throws IOException {
        List<Airline> airlines = airlineCsvDeserializer.deserialize(file.getInputStream());

        airlineRepository.deleteAll();
        airlines.stream()
                .forEach(a -> airlineRepository.save(a));
    }




}
