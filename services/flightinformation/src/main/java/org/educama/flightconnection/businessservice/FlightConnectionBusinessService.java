package org.educama.flightconnection.businessservice;

import org.educama.flightconnection.datafeed.ConnectionCsvDeserializer;
import org.educama.flightconnection.model.FlightConnection;
import org.educama.flightconnection.repository.FlightConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Service class for connections.
 */
@Component
public class FlightConnectionBusinessService {

    FlightConnectionRepository flightConnectionRepository;

    ConnectionCsvDeserializer connectionCsvDeserializer;

    @Autowired
    public FlightConnectionBusinessService(FlightConnectionRepository flightConnectionRepository, ConnectionCsvDeserializer connectionCsvDeserializer) {
        this.flightConnectionRepository = flightConnectionRepository;
        this.connectionCsvDeserializer = connectionCsvDeserializer;
    }


    public List<FlightConnection> findFlightConnection(String sourceIataCode, String destinationIataCode) {
        if (StringUtils.isEmpty(sourceIataCode) || StringUtils.isEmpty(destinationIataCode)) {
            return Collections.emptyList();
        }
        return flightConnectionRepository.findBySourceAirportIataCodeAndDestinationAirportIataCode(sourceIataCode.toUpperCase(), destinationIataCode.toUpperCase());
    }

    public void clearAndImportConnections(MultipartFile file) throws IOException {
        List<FlightConnection> flightConnections = connectionCsvDeserializer.deserialize(file.getInputStream());

        flightConnectionRepository.deleteAll();
        flightConnectionRepository.save(flightConnections);
    }

    public List<FlightConnection> findAllConnectionsFromSourceToDestionation(String sourceAirportIataCode, String destinationAirportIataCode) {
        if (StringUtils.isEmpty(sourceAirportIataCode) && StringUtils.isEmpty(destinationAirportIataCode)) {
            return Collections.emptyList();
        }
        if (!StringUtils.isEmpty(sourceAirportIataCode) && StringUtils.isEmpty(destinationAirportIataCode)) {
            return flightConnectionRepository.findBySourceAirportIataCodeIgnoreCase(sourceAirportIataCode);
        }
        if (StringUtils.isEmpty(sourceAirportIataCode) && !StringUtils.isEmpty(destinationAirportIataCode)) {
            return flightConnectionRepository.findBydestinationAirportIataCodeIgnoreCase(destinationAirportIataCode);
        }

        return flightConnectionRepository.findBySourceAirportIataCodeAndDestinationAirportIataCode(sourceAirportIataCode.toUpperCase(), destinationAirportIataCode.toUpperCase());
    }


}
