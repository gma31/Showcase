package org.educama.airline.businessservice;

import org.educama.airline.datafeed.AirlineCsvDeserializer;
import org.educama.airline.model.Airline;
import org.educama.airline.repository.AirlineRepository;
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
 * Tests the airline business service.
 */
@RunWith(MockitoJUnitRunner.class)
public class AirlineBusinessServiceUnitTest {

    @Mock
    AirlineRepository airlineRepository;

    @Mock
    MultipartFile file;

    @Mock
    InputStream inputStream;

    @Mock
    AirlineCsvDeserializer airlineCsvDeserializer;

    @Mock
    List<Airline> airlines;

    @InjectMocks
    private AirlineBusinessService cut;

    private final String iata = "ana";


    @Test
    public void findAirlinesByIataCodeRetrievesAirlinesIrrespectiveOfTheCaseOfIATA() {
        cut.findAirlinesByIataCode(iata);
        verify(airlineRepository).findByIataCodeIgnoreCase(iata);
    }

    @Test
    public void importAirlinesReplacesTheContentOfRepository() throws IOException {
        when(file.getInputStream()).thenReturn(inputStream);
        when(airlineCsvDeserializer.deserialize(inputStream)).thenReturn(airlines);
        cut.clearAndImportAirlines(file);
        verify(airlineRepository).deleteAll();
        verify(airlineRepository).save(airlines);

    }

    @Test
    public void findAllAirlinesReturnsAllAirlines() {
        cut.findAllAirlines();
        verify(airlineRepository).findAll();
    }

}
