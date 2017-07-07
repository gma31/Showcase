package org.educama.airline.repository;

import org.educama.airline.model.Airline;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for airlines.
 */
public interface AirlineRepository extends MongoRepository<Airline, String> {

    List<Airline> findByIataCodeIgnoreCase(String iataCode);

    List<Airline> findByIcaoCodeIgnoreCase(String icaoCode);

    List<Airline> findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(String name, String iataCode, String icaoCode);

    List<Airline> findByIataCodeOrIcaoCodeAllIgnoreCase(String iataCode, String icaoCode);

    default List<Airline> findBySearchTerm(String term) {
        return findTop10ByNameIsStartingWithOrIataCodeIsStartingWithOrIcaoCodeIsStartingWithAllIgnoreCase(term, term, term);
    }
    default List<Airline> findByAirportCode(String term){
        return findByIataCodeOrIcaoCodeAllIgnoreCase(term,term);
    }
}
