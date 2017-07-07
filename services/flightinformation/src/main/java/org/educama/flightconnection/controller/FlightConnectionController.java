package org.educama.flightconnection.controller;

import org.educama.flightconnection.businessservice.FlightConnectionBusinessService;
import org.educama.flightconnection.model.FlightConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Rest controller of the airline resources.
 */
@RestController
public class FlightConnectionController {

    FlightConnectionBusinessService flightConnectionBusinessService;

    @Autowired
    public FlightConnectionController(FlightConnectionBusinessService flightConnectionBusinessService) {
        this.flightConnectionBusinessService = flightConnectionBusinessService;
    }

    /**
     * Retrieves all existing connections  from a source to a destination airport.
     * Retrieves all connection from a source (resp. destination) airport if only the source (resp. the destination) is specified.
     *
     * @param sourceAirportIata      the IATA code of the source airport
     * @param destinationAirportIata the IATA code of the destination airport
     * @return The available flight connections.
     */
    @RequestMapping(value = "/connections")
    public List<FlightConnection> getAllConnectionFromSourceToDestination(@RequestParam(value = "from") String sourceAirportIata, @RequestParam(value = "to") String destinationAirportIata) {
        return flightConnectionBusinessService.findAllConnectionsFromSourceToDestionation(sourceAirportIata, destinationAirportIata);
    }


    /**
     * Replaces the content of the flight connection database with the content of the CSV file
     * the data contained in the csv File.
     *
     * @param file the import file containing the flight connections data
     */
    @RequestMapping(value = "/connections/import/csv", method = RequestMethod.POST)
    @ResponseBody
    public void importConnections(@RequestParam("file") MultipartFile file) throws IOException {
        flightConnectionBusinessService.clearAndImportConnections(file);
    }
}
