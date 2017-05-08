package org.educama.services.flightinformation.Controller;

import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

import org.educama.services.flightinformation.controller.AirportController;
import org.educama.services.flightinformation.datafeed.AirportCsvDeserializer;
import org.educama.services.flightinformation.repositories.Airport;
import org.educama.services.flightinformation.repositories.AirportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by GMA on 08.05.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AirportControllerUnitTest {
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
    private AirportController cut;

    private final String iataLowerCase = "ybb";
    private final String iataUpperCase = "YBB";

    @Test
    public void getAirport_retrievesAirport_irrespectiveOfTheCaseOfIATA() {
        //When
        cut.getAirport(iataLowerCase);
        //Then
        verify(airportRepository).findByIataCode(iataUpperCase);

    }

    @Test
    public void getAirportSuggestions_retrievesAirports_irrespectiveOfTheCaseOfIATA() {
        //When
        cut.getAirportSuggestions(iataLowerCase);
        //Then
        verify(airportRepository).findByIataCodeLike(iataUpperCase);
    }

    @Test
    public void importAirport_replaceTheContentOfRepository() throws IOException {
        //Given
        when(file.getInputStream()).thenReturn(inputStream);
        when(airportCsvDeserializer.deserialize(inputStream)).thenReturn(airportList);

        //Then
        cut.importAirport(file);

        //When
        verify(airportRepository).deleteAll();
        verify(airportRepository).save(airportList);


    }


}
