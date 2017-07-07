package org.educama.airport.controller;

import org.educama.airport.businessservice.AirportBusinessService;
import org.educama.airport.model.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

/**
 * Rest controller of the airport resources.
 */
@RestController
public class AirportController {


    private AirportBusinessService airportBusinessService;

    @Autowired
    public AirportController(AirportBusinessService airportBusinessService) {
        this.airportBusinessService = airportBusinessService;
    }

    /**
     * Retrieves all airports.
     *
     * @return the airports
     */
    @RequestMapping(path = "/airports", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<Airport> getAirports() {
        return airportBusinessService.findAllAirports();
    }

    /**
     * Retrieves an airport by its IATA or ICAO code.
     *
     * @param airportCode the IATA Code
     * @return the airport.
     */
    @RequestMapping(path = "/airports/{airportCode}", produces = TEXT_EVENT_STREAM_VALUE)
    public Mono<Airport> getAirportByIataCodeOrIcaoCode(@PathVariable String airportCode) {
        if (StringUtils.isEmpty(airportCode)) {
            return Mono.empty();
        }
        return airportBusinessService.findAirportsByIataCodeOrIcaoCode(airportCode);
    }

    /**
     * Retrieves a list of airports where the name, IATA Code or ICAO Code starts with a given term.
     *
     * @param term the term to look up
     * @return the list of matching airports.
     */
    @RequestMapping("/airports/suggestions")
    public Flux<Airport> getAirportSuggestions(@RequestParam(value = "term") String term) {
        if (StringUtils.isEmpty(term)) {
            return Flux.empty();
        }
        return airportBusinessService.findAirportSuggestionsBySearchTerm(term);
    }


    /**
     * Replaces the content of the airports database with the content of the CSV file the data contained in the csv
     * File.
     *
     * @param file the import file containing the airport data
     */
    @RequestMapping(value = "/airports/import/csv", method = RequestMethod.POST)
    @ResponseBody
    public void importAirports(@RequestParam("file") MultipartFile file) throws IOException {
        airportBusinessService.clearAndImportAirports(file);
    }
}
