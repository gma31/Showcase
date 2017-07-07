package org.educama.airline.controller;

import org.educama.airline.businessservice.AirlineBusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Tests the Airline REST controller.
 */
@RunWith(MockitoJUnitRunner.class)
public class AirlineControllerUnitTest {

    @Mock
    AirlineBusinessService airlineBusinessService;

    @InjectMocks
    AirlineController cut;

    @Test
    public void getAirlinesByIataCodeOrIcaoCodeReturnsEmptyListWhenTermIsEmpty() {
        assertThat(cut.getAirlinesByIataCodeOrIcaoCode(null)).isEmpty();
        assertThat(cut.getAirlinesByIataCodeOrIcaoCode("")).isEmpty();
    }

    @Test
    public void getAirlinesByIataCodeOrIcaoCodeLooksUpBasedOnIataOrIcaoCode() {
        String term = "iata or icao";
        cut.getAirlinesByIataCodeOrIcaoCode(term);
        verify(airlineBusinessService).findByAirlineByIataCodeOrIcaoCode(term);
    }

    @Test
    public void getAirlinesSuggestionsReturnsEmptyListWhenTermIsEmpty() {
        assertThat(cut.getAirlinesSuggestions(null)).isEmpty();
        assertThat(cut.getAirlinesSuggestions("")).isEmpty();
    }

}
