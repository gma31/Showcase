package org.educama.services.flightinformation.controller;

import org.educama.services.flightinformation.Airport;
import org.educama.services.flightinformation.AirportRepository;
import org.educama.services.flightinformation.datafeed.AirportCsvDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RestController
public class AirportController {
    @Autowired
    AirportRepository airportRepository;
    @Autowired
    AirportCsvDeserializer airportCsvDeserializer;

    @RequestMapping("/airport")
    public List<Airport> getAirport(@RequestParam(value = "iata", defaultValue = "FRA") String iata) {
        return airportRepository.findByIataCode(iata);
    }

    @RequestMapping("/airport/suggestions")
    public List<Airport> getAirportAirport(@RequestParam(value = "term", defaultValue = "FR") String term) {
        return airportRepository.findByIataCodeLike(term);
    }

    /**
     * replace the content of the airports database with the content of the CSV file
     * the data contained in the csv File.
     *
     * @param file the import file containing the airport data
     * @return
     */
    @RequestMapping(value = "/airport/import/csv", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> importAirport(@RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            List<Airport> airports = airportCsvDeserializer.deserialize(file.getInputStream());

            airportRepository.deleteAll();
            airportRepository.save(airports);

        } catch (IOException e) {
            // TODO return a corresponding HTTP status
            e.printStackTrace();

        }
        //TODO this is a default response.properly handle the returned responseEntity
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
