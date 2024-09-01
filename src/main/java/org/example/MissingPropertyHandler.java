package org.example;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.slf4j.LoggerFactory;

public class MissingPropertyHandler extends DeserializationProblemHandler {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(MissingPropertyHandler.class);

    @Override
    public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser p,
                                         com.fasterxml.jackson.databind.JsonDeserializer<?> deserializer,
                                         Object beanOrClass, String propertyName) {
        logger.warn("Property '{}' is missing or unrecognized in the JSON data for class '{}'", propertyName, beanOrClass.getClass().getName());
        return true;
    }
}
