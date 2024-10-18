package tbank.mr_irmag.tbank_kudago_task.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tbank.mr_irmag.tbank_kudago_task.domain.DTO.CurrencyExchangeRateResponse;
import tbank.mr_irmag.tbank_kudago_task.domain.DTO.EventRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EventServiceTest {

    @Mock
    private ReadKudaGo<Event> readKudaGo;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findEventsByBudget_ValidRequest_ShouldReturnFilteredEvents() {
        // Arrange
        EventRequest request = new EventRequest();
        request.setAmount(1000.0);
        request.setCurrencyCode("USD");

        Event event = new Event();
        event.setPrice("800");

        CurrencyExchangeRateResponse exchangeRateResponse = new CurrencyExchangeRateResponse();
        exchangeRateResponse.setRate(60.0);

        when(readKudaGo.convertJsonToList(any(String.class), any(Class.class)))
                .thenReturn(Collections.singletonList(event));
        when(readKudaGo.convertJsonToEntity(any(String.class), any(Class.class)))
                .thenReturn(exchangeRateResponse);

        // Act
        Mono<List<Event>> result = eventService.findEventsByBudget(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(events -> events.size() == 1 && events.get(0).getValueFromPrice() <= 1000)
                .verifyComplete();
    }

    @Test
    public void findEventsByBudget_CurrencyConversion_ShouldReturnEventsInConvertedBudget() {
        // Arrange
        EventRequest request = new EventRequest();
        request.setAmount(1000.0);
        request.setCurrencyCode("USD");

        Event event = new Event();
        event.setPrice("800");

        CurrencyExchangeRateResponse exchangeRateResponse = new CurrencyExchangeRateResponse();
        exchangeRateResponse.setRate(60.0);

        when(readKudaGo.convertJsonToList(any(String.class), any(Class.class)))
                .thenReturn(Collections.singletonList(event));
        when(readKudaGo.convertJsonToEntity(any(String.class), any(Class.class)))
                .thenReturn(exchangeRateResponse);

        // Act
        Mono<List<Event>> result = eventService.findEventsByBudget(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(events -> events.size() == 1 && events.get(0).getValueFromPrice() <= 60_000)
                .verifyComplete();
    }

    @Test
    public void findEventsByBudget_ErrorDuringRequest_ShouldReturnError() {
        // Arrange
        EventRequest request = new EventRequest();
        request.setAmount(1000.0);
        request.setCurrencyCode("USD");

        when(readKudaGo.convertJsonToList(any(String.class), any(Class.class)))
                .thenThrow(new RuntimeException("API error"));

        // Act
        Mono<List<Event>> result = eventService.findEventsByBudget(request);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("API error"))
                .verify();
    }

    @Test
    public void convertToUnixTime_ValidDate_ShouldReturnCorrectUnixTime() {
        // Act
        long unixTime = eventService.convertToUnixTime("2023-01-01");

        // Assert
        assertEquals(1672531200L, unixTime);
    }
}
