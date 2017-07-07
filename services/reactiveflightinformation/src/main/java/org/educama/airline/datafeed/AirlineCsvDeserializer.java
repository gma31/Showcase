package org.educama.airline.datafeed;

import org.educama.airline.model.Airline;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes the CSV inputstreams containing airlines data into airline Objects.
 */
@Component
public class AirlineCsvDeserializer {
    private final String nill = "\\N";
    private final String empty = "";

    public List<Airline> deserialize(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        ICsvBeanReader csvBeanReader = new CsvBeanReader(inputStreamReader, CsvPreference.STANDARD_PREFERENCE);
        final String[] header = {null, "name", "alias", "iataCode", "icaoCode", "callsign", "country", null};
        Airline airline = null;
        List<Airline> airlines = new ArrayList<>();
        try {

            while ((airline = csvBeanReader.read(Airline.class, header, getCellProcessors())) != null) {
                System.out.println("deserialized " + airline);
                airlines.add(airline);
            }
        } finally {
            if (csvBeanReader != null) {
                csvBeanReader.close();
            }
        }
        return airlines;
    }

    /**
     * custom CellProcessor for parsing the airports aliases.
     */
    private class ParseAlias extends CellProcessorAdaptor {

        @Override
        public <T> T execute(Object value, CsvContext context) {
            validateInputNotNull(value, context);
            if (value.equals(nill)) {
                return next.execute(empty, context);
            }
            return next.execute(value, context);
        }
    }

    private CellProcessor[] getCellProcessors() {
        return new CellProcessor[]{
                //@formatter:off
                null,                           //airline ID
                new Optional(),                 //name
                new Optional(new ParseAlias()), //alias
                new Optional(),                 //iataCode
                new Optional(),                 //icaoCode
                new Optional(),                 //callsign
                new Optional(),                 //country
                null,                           //active
                //@formatter:on
        };

    }

}
