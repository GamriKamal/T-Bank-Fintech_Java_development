package org.example;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ParserInfo {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(ParserInfo.class);

    public void parse(String jsonPath, String outputPath) {
        ObjectMapper mapper = new ObjectMapper();
        XmlFormatter xmlFormatter = new XmlFormatter();
        mapper.addHandler(new MissingPropertyHandler());

        CityInfo cityInfo = null;

        try {
            cityInfo = mapper.readValue(new File(jsonPath), CityInfo.class);
        } catch (IOException e){
            logger.error(e.getMessage());
        }

        logger.info("Retrieved info: {}", cityInfo.toString());

        xmlFormatter.toXML(cityInfo, outputPath);
    }
}
