package org.educama.flightconnection.datafeed;


import org.educama.airline.businessservice.AirlineBusinessService;
import org.educama.airline.model.Airline;
import org.educama.airport.businessservice.AirportBusinessService;
import org.educama.airport.model.Airport;
import org.educama.flightconnection.model.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes the CSV inputstreams containing connections data into connection Objects.
 */
@Component
public class ConnectionCsvDeserializer {
    private final String codeshareYes = "Y";
    private final String codeshareNo = "";
    private final String separator = ",";
    private final String empty = "";

    private AirportBusinessService airportBusinessService;

    private AirlineBusinessService airlineBusinessService;

    @Autowired
    public ConnectionCsvDeserializer(AirportBusinessService airportBusinessService, AirlineBusinessService airlineBusinessService) {
        this.airportBusinessService = airportBusinessService;
        this.airlineBusinessService = airlineBusinessService;
    }

    public List<Connection> deserialize(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        ICsvBeanReader csvBeanReader = new CsvBeanReader(inputStreamReader, CsvPreference.STANDARD_PREFERENCE);
        final String[] header = {"airlineIataCode", null, "sourceAirportIataCode", null, "destinationAirportIataCode", null, "codeshare", "stops", null};
        Connection connection = null;
        List<Connection> connections = new ArrayList<>();
        try {

            while ((connection = csvBeanReader.read(Connection.class, header, getCellProcessors())) != null) {
                System.out.println("deserialized " + connection);
                connections.add(connection);
            }
        } finally {
            if (csvBeanReader != null) {
                csvBeanReader.close();
            }
        }
        return connections;
    }

    public String createCsvContentForConnections(List<Connection> connections) {
        StringBuilder builder = new StringBuilder();
        for (Connection connection : connections) {
            String codeShare = connection.isCodeshare() ? codeshareYes : codeshareNo;
            //@formatter:off
            builder.append(connection.getAirlineIataCode())               .append(separator)  //ID
                    .append(empty)                                        .append(separator)  //airline ID
                    .append(connection.getSourceAirportIataCode())        .append(separator)
                    .append(empty)                                        .append(separator) //source ID
                    .append(connection.getDestinationAirportIataCode())   .append(separator)
                    .append(empty)                                        .append(separator) //destination ID
                    .append(codeShare)                                    .append(separator)
                    .append(connection.getStops())                        .append(separator)
                    .append(empty)                                        .append("\n");     //Equipment
            //@formatter:on
        }
        return builder.toString();
    }

    private CellProcessor[] getCellProcessors() {
        return new CellProcessor[]{
                //@formatter:off
                new Optional(new ParseAirlinesIataCode()), //airline
                null,                                      //airline ID
                new Optional(new ParseAirportIataCode()),  //source airport
                null,                                      //source airport ID
                new Optional(new ParseAirportIataCode()),  //destination airport
                null,                                      //destination airport ID
                new Optional(new ParseCodeShare()),        //codeShare
                new Optional(new ParseInt()),              //stops
                null,                                      //equipment
                 //@formatter:on

        };

    }

    /**
     * custom CellProcessor for parsing the codeshares.
     */
    private class ParseCodeShare extends CellProcessorAdaptor {


        ParseCodeShare() {
            super();
        }

        ParseCodeShare(CellProcessorAdaptor next) {
            super(next);
        }

        @Override
        public Object execute(Object value, CsvContext context) {
            validateInputNotNull(value, context);

            if (codeshareYes.equalsIgnoreCase((String) value)) {
                return next.execute(Boolean.TRUE, context);
            } else {
                if (codeshareNo.equalsIgnoreCase((String) value)) {
                    return next.execute(Boolean.FALSE, context);
                }
            }
            throw new SuperCsvCellProcessorException(
                    String.format("Could not parse '%s' as a codeshare", value),
                    context, this);
        }
    }


    /**
     * Custom CellProcessor for parsing the airports codes.
     */
    private class ParseAirportIataCode extends CellProcessorAdaptor {
        private static final int AIRPORT_IATACODE_LENGTH = 3;
        private static final int AIRPORT_ICAOCODE_LENGTH = 4;

        ParseAirportIataCode() {
            super();
        }

        ParseAirportIataCode(CellProcessorAdaptor next) {
            super(next);
        }

        @Override
        /**
         * The airports codes contained in the csv can be IATA ( 3-letter) codes or ICAO (4-letter) codes.
         */
        public Object execute(Object value, CsvContext context) {
            validateInputNotNull(value, context);
            String airportCode = (String) value;
            if ((airportCode.length() == AIRPORT_IATACODE_LENGTH)) {
                return next.execute(value, context);
            }
            // attempt to convert the icao code to an IATA code. If not possible proceed with the code as it is.
            if (airportCode.length() == AIRPORT_ICAOCODE_LENGTH) {

                Mono<Airport> airportMono = airportBusinessService.findAirportByIcaoCode(airportCode);
                Airport airport = airportMono.block();
                if (airport != null) {
                    return next.execute(airport.getIataCode(), context);
                } else {
                    return next.execute(airportCode, context);
                }
            }
            throw new SuperCsvCellProcessorException(
                    String.format("Could not parse '%s' neither as an IATA nor as an IACAO code for Airports", value),
                    context, this);
        }

    }

    /**
     * custom CellProcessor for parsing the airlines codes.
     */
    private class ParseAirlinesIataCode extends CellProcessorAdaptor {
        private static final int AIRLINE_IATACODE_LENGTH = 2;
        private static final int AIRLINE_ICAOCODE_LENGTH = 3;

        ParseAirlinesIataCode() {
            super();
        }

        ParseAirlinesIataCode(CellProcessorAdaptor next) {
            super(next);
        }

        @Override
        /**
         * The airlines codes contained in the csv can be IATA ( 2-letter) codes or ICAO (3-letter) codes.
         */
        public Object execute(Object value, CsvContext context) {
            validateInputNotNull(value, context);
            String airlineCode = (String) value;
            if ((airlineCode.length() == AIRLINE_IATACODE_LENGTH)) {
                return next.execute(value, context);
            }
            // attempt to convert the icao code to an IATA code. If not possible proceed with the code as it is.
            if (airlineCode.length() == AIRLINE_ICAOCODE_LENGTH) {

                Mono<Airline> airlinesMono = airlineBusinessService.findAirlinesByIcaoCode(airlineCode);
                Airline airline = airlinesMono.block();
                if (airline != null) {
                    return next.execute(airline.getIataCode(), context);
                } else {
                    return next.execute(airlineCode, context);
                }
            }
            throw new SuperCsvCellProcessorException(
                    String.format("Could not parse '%s' neither as an IATA nor as an IACAO code for Airlines", value),
                    context, this);
        }

    }
}
