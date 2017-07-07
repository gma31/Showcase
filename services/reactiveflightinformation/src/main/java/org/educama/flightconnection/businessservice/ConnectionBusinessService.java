package org.educama.flightconnection.businessservice;

import org.educama.flightconnection.datafeed.ConnectionCsvDeserializer;
import org.educama.flightconnection.model.Connection;
import org.educama.flightconnection.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

/**
 * Service class for connections.
 */
@Component
public class ConnectionBusinessService {

    ConnectionRepository connectionRepository;

    ConnectionCsvDeserializer connectionCsvDeserializer;

    @Autowired
    public ConnectionBusinessService(ConnectionRepository connectionRepository, ConnectionCsvDeserializer connectionCsvDeserializer) {
        this.connectionRepository = connectionRepository;
        this.connectionCsvDeserializer = connectionCsvDeserializer;
    }


    public void clearAndImportConnections(MultipartFile file) throws IOException {
        List<Connection> connections = connectionCsvDeserializer.deserialize(file.getInputStream());

        connectionRepository.deleteAll();
        connections.stream()
                .forEach(c -> connectionRepository.save(c));
    }

    public Flux<Connection> findAllConnectionsFromSourceToDestionation(String sourceAirportIataCode, String destinationAirportIataCode) {
        if (StringUtils.isEmpty(sourceAirportIataCode) && StringUtils.isEmpty(destinationAirportIataCode)) {
            return Flux.empty();
        }
        if (!StringUtils.isEmpty(sourceAirportIataCode) && StringUtils.isEmpty(destinationAirportIataCode)) {
            return connectionRepository.findBySourceAirportIataCodeIgnoreCase(sourceAirportIataCode)
                    .log("org.educama.flightconnection.businessservice");
        }
        if (StringUtils.isEmpty(sourceAirportIataCode) && !StringUtils.isEmpty(destinationAirportIataCode)) {
            return connectionRepository.findBydestinationAirportIataCodeIgnoreCase(destinationAirportIataCode)
                    .log("org.educama.flightconnection.businessservice");
        }
        return connectionRepository.findBySourceAirportIataCodeAndDestinationAirportIataCode(sourceAirportIataCode.toUpperCase(), destinationAirportIataCode.toUpperCase())
                .log("org.educama.flightconnection.businessservice");
    }


}
