package tbank.mr_irmag.tbank_kudago_task.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;

import java.util.ArrayList;
import java.util.List;

@Service
@Tag(name = "ReadKudaGo<T>", description = "Сервис для чтения данных из KudaGo API.")
public class ReadKudaGo<T> {
    private final Logger logger = LoggerFactory.getLogger(ReadKudaGo.class);
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Schema(description = "Конвертирует JSON-ответ из API в список объектов указанного типа.")
    @TimeMeasurable
    public <T> List<T> convertJsonToList(String url, Class<T> type) {
        logger.info(url);
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<T> list = new ArrayList<>();

        ResponseEntity<String> response = makeRequest(url);

        if (response != null) {
            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode resultsNode = rootNode.get("results");

                if (resultsNode != null && resultsNode.isArray()) {
                    list = objectMapper.readValue(
                            resultsNode.toString(),
                            typeFactory.constructCollectionType(List.class, type)
                    );
                } else {
                    list = objectMapper.readValue(response.getBody(),
                            typeFactory.constructCollectionType(List.class, type));
                }

            } catch (JsonProcessingException e) {
                logger.error("Ошибка обработки JSON: " + e.getLocalizedMessage(), e);
            }
        } else {
            logger.debug("Ответ null!");
        }

        return list;
    }


    @Schema(description = "Конвертирует JSON-ответ из API в объект указанного типа.")
    @TimeMeasurable
    public <T> T convertJsonToEntity(String url, Class<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();

        ResponseEntity<String> response = makeRequest(url);
        T result = null;

        if (response != null) {
            try {
                result = objectMapper.readValue(response.getBody(), type);
            } catch (JsonProcessingException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        } else {
            logger.debug("Ответ null!");
        }

        return result;
    }


    @Schema(description = "Делает запрос к указанному URL и возвращает ответ.")
    private ResponseEntity<String> makeRequest(String url) {
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(url, String.class);
            logger.debug("The response was received successfully! {}", response.getBody());
        } catch (RestClientException e) {
            logger.error(e.getLocalizedMessage());
        }
        return response;
    }
}
