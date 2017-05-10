package org.educama.services.flightinformation.controller;

import org.educama.services.flightinformation.model.Airport;
import org.educama.services.flightinformation.repository.AirportRepository;
import org.educama.services.flightinformation.datafeed.AirportCsvDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AirportController {
    @Autowired
    AirportRepository airportRepository;
    @Autowired
    AirportCsvDeserializer airportCsvDeserializer;
    protected int maxSuggestions = 10;


    /**
     * Retrieves an airport by its IATA code
     *
     * @param iata the IATA Code
     * @return the airport.
     */
    @RequestMapping("/airports/{iata}")
    public List<Airport> getAirport(@PathVariable String iata) {
        return airportRepository.findByIataCode(iata.toUpperCase());
    }

    /**
     * Retrieves all airports.
     * @return the airports
     */
    @RequestMapping("/airports")
    public List<Airport> getAllAirports(){
        return airportRepository.findAll();
    }

    /**
     * Retrieves the a list of airports which IATA code begin with a given term.
     *
     * @param term the part of the IATA code to be looked up.
     * @return the list of matching airports.
     */
    @RequestMapping("/airports/suggestions")
    public List<Airport> getAirportSuggestions(@RequestParam(value = "term") String term) {

        List<Airport> suggestions = new ArrayList<>();
        if (term == null || term.isEmpty()) {
            return suggestions;
        }
        suggestions = airportRepository.findByIataCodeLike(term.toUpperCase());

        return suggestions.stream()
                .limit(maxSuggestions)
                .collect(Collectors.toList());
    }

    /**
     * Replaces the content of the airports database with the content of the CSV file
     * the data contained in the csv File.
     *
     * @param file the import file containing the airport data
     */
    @RequestMapping(value = "/airport/import/csv", method = RequestMethod.POST)
    public @ResponseBody
    void importAirport(@RequestParam("file") MultipartFile file) throws IOException {


        List<Airport> airports = airportCsvDeserializer.deserialize(file.getInputStream());

        airportRepository.deleteAll();
        airportRepository.save(airports);

    }


}
