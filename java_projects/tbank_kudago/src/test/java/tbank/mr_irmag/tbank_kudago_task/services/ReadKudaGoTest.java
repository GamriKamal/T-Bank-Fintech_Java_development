package tbank.mr_irmag.tbank_kudago_task.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReadKudaGoTest {
    @Mock
    ResponseEntity<String> response;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ReadKudaGo<Category> categoriesReader;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertJsonToList_Success_ReturnResponse() throws JsonProcessingException {
        String jsonResponse = "[{\"name\":\"item1\"}, {\"name\":\"item2\"}]";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity("url", String.class)).thenReturn(responseEntity);

        List<Category> result = categoriesReader.convertJsonToList("url", Category.class);

        assertEquals(2, result.size());
        verify(restTemplate).getForEntity("url", String.class);
    }

    @Test
    public void makeRequest_NotSuccess_ReturnNull() {
        // Arrange
        when(response.getBody()).thenReturn(null);

        // Act
        List<Category> list = categoriesReader.convertJsonToList("url", Category.class);

        // Assert
        assertNull(list);
    }

}