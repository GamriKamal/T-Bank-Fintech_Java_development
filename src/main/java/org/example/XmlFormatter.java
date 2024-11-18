package org.example;

import ch.qos.logback.classic.Logger;
import lombok.Cleanup;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlFormatter {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(XmlFormatter.class);

    public void toXML(CityInfo cityInfo, String outPath) {
        String xmlString = marshalToString(cityInfo);
        if (xmlString != null) {
            Document document = parseXmlToDocument(xmlString);
            if (document != null) {
                logger.debug("Converting xml string to document...");
                writeDocumentToFile(document, outPath);
            }
        }
    }

    private String marshalToString(CityInfo cityInfo) {
        StringWriter stringWriter = new StringWriter();
        try {
            JAXB.marshal(cityInfo, stringWriter);
            return stringWriter.toString();
        } catch (Exception e) {
            logger.error("Error marshalling object to XML: {}", e.getMessage());
            return null;
        }
    }

    private Document parseXmlToDocument(String xmlString) {
        try {
            StringReader stringReader = new StringReader(xmlString);
            InputSource inputSource = new InputSource(stringReader);

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            return documentBuilder.parse(inputSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Error parsing XML string to Document: {}", e.getMessage());
            return null;
        }
    }

    private void writeDocumentToFile(Document document, String outPath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(outPath));

            transformer.transform(domSource, streamResult);
            logger.debug("XML file '{}' created successfully! \n", outPath);
        } catch (TransformerException e) {
            logger.error("Error writing Document to XML file: {}", e.getMessage());
        }
    }
}
