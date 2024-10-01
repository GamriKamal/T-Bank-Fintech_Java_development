package tbank.mr_irmag.tbank_kudago_task.services;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public List<T> convertJsonToList(String url, Class<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        ResponseEntity<String> response = makeRequest(url);
        List<T> list = null;
        if (response != null) {
            try {
                list = objectMapper.readValue(response.getBody(),
                        typeFactory.constructCollectionType(List.class, type));
            } catch (JsonProcessingException e) {
                logger.error(e.getLocalizedMessage());
            }
        } else {
            logger.debug("The answer is null!");
        }
        return list;
    }

    @Schema(description = "Делает запрос к указанному URL и возвращает ответ.")
    private ResponseEntity<String> makeRequest(String url) {
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(url, String.class);
            logger.debug("The response was received successfully! {}", response.getBody());
        } catch (RestClientException | NullPointerException e) {
            logger.error("Error occurred: {}", e.getLocalizedMessage());
        }
        return response;
    }
}
