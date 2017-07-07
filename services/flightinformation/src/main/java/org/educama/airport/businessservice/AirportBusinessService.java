package org.educama.airport.businessservice;

import org.educama.airport.datafeed.AirportCsvDeserializer;
import org.educama.airport.model.Airport;
import org.educama.airport.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Service class for Airports.
 */
@Component
public class AirportBusinessService {

    private AirportRepository airportRepository;
    private AirportCsvDeserializer airportCsvDeserializer;

    protected static final int MAX_SUGGESTIONS = 10;

    @Autowired
    public AirportBusinessService(AirportRepository airportRepository, AirportCsvDeserializer airportCsvDeserializer) {
        this.airportRepository = airportRepository;
        this.airportCsvDeserializer = airportCsvDeserializer;
    }

    public List<Airport> findAllAirports() {
        return airportRepository.findAll();
    }

    public List<Airport> findAirportByIataCode(String iataCode) {
        if (StringUtils.isEmpty(iataCode)) {
            return Collections.emptyList();
        }
        return airportRepository.findByIataCodeIgnoreCase(iataCode);
    }

    public List<Airport> findAirportByIcaoCode(String icaoCode) {
        if (StringUtils.isEmpty(icaoCode)) {
            return Collections.emptyList();
        }
        return airportRepository.findByIcaoCodeIgnoreCase(icaoCode);
    }

    public List<Airport> findAirportsByIataCodeOrIcaoCode(String airportCode) {
        if (StringUtils.isEmpty(airportCode)) {
            return Collections.emptyList();
        }
        return airportRepository.findByAirportCode(airportCode);
    }

    public List<Airport> findAirportSuggestionsBySearchTerm(String term) {
        if (StringUtils.isEmpty(term)) {
            return Collections.emptyList();
        }
        return airportRepository.findBySearchTerm(term);
    }

    public void clearAndImportAirports(MultipartFile file) throws IOException {
        List<Airport> airports = airportCsvDeserializer.deserialize(file.getInputStream());

        airportRepository.deleteAll();
        airportRepository.save(airports);
    }

}
