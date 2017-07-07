package org.educama.flightconnection.businessservice;

import org.educama.flightconnection.repository.FlightConnectionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Tests the connection business service.
 */
@RunWith(MockitoJUnitRunner.class)
public class FlightFlightConnectionBusinessServiceUnitTest {

    @Mock
    FlightConnectionRepository flightConnectionRepository;

    @InjectMocks
    FlightConnectionBusinessService cut;


    @Test
    public void findFlightConnectionReturnsEmptyListWhenNoSourceAirport() {
        //Given
        final String destinationAirport = "LAX";
        //Then
        assertThat(cut.findFlightConnection(null, destinationAirport)).isEmpty();
        assertThat(cut.findFlightConnection("", destinationAirport)).isEmpty();
    }

    @Test
    public void findFlightConnectionReturnsEmptyListWhenNoSourceAndNoDestinationAirport() {
        //Given
        final String sourceAirport = "LAX";
        //Then
        assertThat(cut.findFlightConnection(sourceAirport, null)).isEmpty();
        assertThat(cut.findFlightConnection(sourceAirport, "")).isEmpty();
    }

    @Test
    public void findFlightConnectionReturnsListOfConnections() {
        //Given
        final String sourceAirport = "fra";
        final String destinationAirport = "LAX";
        //When
        cut.findFlightConnection(sourceAirport, destinationAirport);
        //Then
        verify(flightConnectionRepository).findBySourceAirportIataCodeAndDestinationAirportIataCode(sourceAirport.toUpperCase(), destinationAirport);
    }

    @Test
    public void findFlightConnectionReturnsConnectionsFromSourceAirportWhenOnlySourceAirportSpecified() {
        //Given
        final String source = "LAX";

        cut.findAllConnectionsFromSourceToDestionation(source, null);
        //Then
        verify(flightConnectionRepository).findBySourceAirportIataCodeIgnoreCase(source.toUpperCase());

    }

    @Test
    public void findFlightConnectionReturnsConnectionsToDestinationAirportWhenOnlyDestinationAirportSpecified() {
        //Given
        final String destination = "LAX";
        //When
        cut.findAllConnectionsFromSourceToDestionation(null, destination);
        //Then
        verify(flightConnectionRepository).findBydestinationAirportIataCodeIgnoreCase(destination.toUpperCase());
    }


}
