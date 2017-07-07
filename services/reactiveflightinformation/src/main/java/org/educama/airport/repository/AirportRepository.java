package org.educama.airport.repository;

import org.educama.airport.model.Airport;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for airports.
 */
public interface AirportRepository extends ReactiveMongoRepository<Airport, String> {

    Mono<Airport> findByIataCodeIgnoreCase(String iataCode);

    Mono<Airport> findByIcaoCodeIgnoreCase(String icaoCode);

    Flux<Airport> findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(String name, String iataCode, String icaoCode);

    Mono<Airport> findByIataCodeOrIcaoCodeAllIgnoreCase(String iataCode, String icaoCode);

    default Flux<Airport> findBySearchTerm(String term) {
        return findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(term, term, term);
    }

    default Mono<Airport> findByAirportCode(String term) {
        return findByIataCodeOrIcaoCodeAllIgnoreCase(term, term);
    }
}
