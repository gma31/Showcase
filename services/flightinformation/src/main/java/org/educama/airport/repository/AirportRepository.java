package org.educama.airport.repository;

import org.educama.airport.model.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for airports.
 */
public interface AirportRepository extends MongoRepository<Airport, String> {

    List<Airport> findByIataCodeIgnoreCase(String iataCode);

    List<Airport> findByIcaoCodeIgnoreCase(String icaoCode);

    List<Airport> findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(String name, String iataCode, String icaoCode);

    List<Airport> findByIataCodeOrIcaoCodeAllIgnoreCase(String iataCode, String icaoCode);

    default List<Airport> findBySearchTerm(String term) {
        return findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(term, term, term);
    }

    default List<Airport> findByAirportCode(String term) {
        return findByIataCodeOrIcaoCodeAllIgnoreCase(term, term);
    }

}
