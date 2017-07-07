package org.educama.airline.repository;

import org.educama.airline.model.Airline;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for airlines.
 */
public interface AirlineRepository extends ReactiveMongoRepository<Airline, String> {
    Mono<Airline> findByIataCodeIgnoreCase(String iataCode);

    Mono<Airline> findByIcaoCodeIgnoreCase(String icaoCode);

    Flux<Airline> findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(String name, String iataCode, String icaoCode);

    Mono<Airline> findByIataCodeOrIcaoCodeAllIgnoreCase(String iataCode, String icaoCode);

    default Flux<Airline> findBySearchTerm(String term) {
        return findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(term, term, term);
    }

    default Mono<Airline> findByAirportCode(String term) {
        return findByIataCodeOrIcaoCodeAllIgnoreCase(term, term);
    }
}
