package org.educama.airport.businessservice;


import org.educama.airport.datafeed.AirportCsvDeserializer;
import org.educama.airport.model.Airport;
import org.educama.airport.repository.AirportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the airport business service.
 */
@RunWith(MockitoJUnitRunner.class)
public class AirportBusinessServiceUnitTest {

    @Mock
    AirportRepository airportRepository;

    @Mock
    MultipartFile file;

    @Mock
    InputStream inputStream;


    @Mock
    AirportCsvDeserializer airportCsvDeserializer;

    @Mock
    List<Airport> airportList;

    @InjectMocks
    private AirportBusinessService cut;

    private final String iata = "ybb";

    @Test
    public void getAirportRetrievesAirportIrrespectiveOfTheCaseOfIATA() {
        cut.findAirportByIataCode(iata);
        verify(airportRepository).findByIataCodeIgnoreCase(iata);
    }

    @Test
    public void importAirportReplaceTheContentOfRepository() throws IOException {
        when(file.getInputStream()).thenReturn(inputStream);
        when(airportCsvDeserializer.deserialize(inputStream)).thenReturn(airportList);
        cut.clearAndImportAirports(file);
        verify(airportRepository).deleteAll();
        verify(airportRepository).save(airportList);
    }

    @Test
    public void getAllAirportsReturnsAllAirports() {
        cut.findAllAirports();
        verify(airportRepository).findAll();
    }

}
