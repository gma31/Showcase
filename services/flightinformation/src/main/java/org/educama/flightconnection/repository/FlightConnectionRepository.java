package org.educama.flightconnection.repository;

import org.educama.flightconnection.model.FlightConnection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * The Repository interface for connections.
 */
public interface FlightConnectionRepository extends MongoRepository<FlightConnection, String> {
    List<FlightConnection> findBySourceAirportIataCodeAndDestinationAirportIataCode(String sourceIataCode, String destinationAirportIataCode);

    List<FlightConnection> findBySourceAirportIataCodeIgnoreCase(String sourceIataCode);

    List<FlightConnection> findBydestinationAirportIataCodeIgnoreCase(String destinationIataCode);
}
