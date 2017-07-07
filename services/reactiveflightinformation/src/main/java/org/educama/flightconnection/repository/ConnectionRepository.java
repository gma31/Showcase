package org.educama.flightconnection.repository;

import org.educama.flightconnection.model.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * The Repository interface for connections.
 */
public interface ConnectionRepository extends ReactiveMongoRepository<Connection, String> {
     Flux<Connection> findBySourceAirportIataCodeAndDestinationAirportIataCode(String sourceIataCode, String destinationAirportIataCode);

     Flux<Connection> findBySourceAirportIataCodeIgnoreCase(String sourceIataCode);

     Flux<Connection> findBydestinationAirportIataCodeIgnoreCase(String destinationIataCode);
}

