package org.educama.services.flightinformation.datafeed;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * Created by GMA on 03.05.2017.
 */
public interface CsvDeserializer<T> {
    /**
     * deserialize the stream into a collection of {@link T}
     * @param in
     * @return the deserialized instances. Otherwise an empty Lit
     */
    public List<T> deserialize(InputStream in) throws IOException;
}
