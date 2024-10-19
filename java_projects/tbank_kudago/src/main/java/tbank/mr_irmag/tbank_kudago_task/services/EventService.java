package tbank.mr_irmag.tbank_kudago_task.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tbank.mr_irmag.tbank_kudago_task.aspect.TimeMeasurable;
import tbank.mr_irmag.tbank_kudago_task.domain.DTO.CurrencyExchangeRateResponse;
import tbank.mr_irmag.tbank_kudago_task.domain.DTO.EventRequest;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.Event;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class EventService {
    @Value("${api.kuda_go.read.url_events}")
    private String eventBaseUrl;

    @Value("${api.kuda_go.read.url_param}")
    private String paramForUrl;

    @Value("${cbr_ru.url_convertCurrency}")
    private String cbrUrl;

    private final ReadKudaGo<Event> readKudaGo;

    @TimeMeasurable
    public Mono<List<Event>> findEventsByBudget(EventRequest eventRequest){
        String eventUrl = formatUrl(eventRequest);
        long startTime = System.currentTimeMillis();
//        CompletableFuture<List<Event>> eventsFuture = CompletableFuture.supplyAsync(() ->
//                readKudaGo.convertJsonToList(eventUrl, Event.class)
//        );
//
//        CompletableFuture<Double> budgetFuture = CompletableFuture.supplyAsync(() -> {
//            if (!eventRequest.getCurrencyCode().equals("RUB")) {
//                var response = readKudaGo.convertJsonToEntity(cbrUrl + eventRequest.getCurrencyCode(),
//                        CurrencyExchangeRateResponse.class);
//                return response.getRate() * eventRequest.getAmount();
//            } else {
//                return eventRequest.getAmount();
//            }
//        });
//
//        eventsFuture.thenAcceptBoth(budgetFuture, (events, convertedBudget) -> {
//            List<Event> filteredEvents = events.stream()
//                    .filter(event -> event.getValueFromPrice() <= convertedBudget)
//                    .collect(Collectors.toList());
//            filteredEvents.forEach(System.out::println);
//        });

        Mono<List<Event>> eventsMono = Mono.fromCallable(() ->
                readKudaGo.convertJsonToList(eventUrl, Event.class)
        );

        Mono<Double> budgetMono = Mono.fromCallable(() -> {
            if (!eventRequest.getCurrencyCode().equals("RUB")) {
                var response = readKudaGo.convertJsonToEntity(cbrUrl + eventRequest.getCurrencyCode(),
                        CurrencyExchangeRateResponse.class);
                return response.getRate() * eventRequest.getAmount();
            } else {
                return eventRequest.getAmount();
            }
        });

        return eventsMono.zipWith(budgetMono)
                .map(tuple -> {
                    List<Event> events = tuple.getT1();
                    Double convertedBudget = tuple.getT2();
                    return events.stream()
                            .filter(event -> event.getValueFromPrice() <= convertedBudget)
                            .collect(Collectors.toList());
                })
                .doOnSuccess(result -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    log.info("Execution completed in {} ms", executionTime);
                })
                .doOnError(error -> {
                    long executionTime = System.currentTimeMillis() - startTime;
                    log.error("Execution failed after {} ms due to error: {}", executionTime, error.getMessage());
                });

    }

    public long convertToUnixTime(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
    }

    private String formatUrl(EventRequest eventRequest){
        String url = eventBaseUrl;

        String dateFrom = (eventRequest.getDateFrom() != null) ? eventRequest.getDateFrom() : LocalDate.now().minusWeeks(1).toString();
        String dateTo = (eventRequest.getDateTo() != null) ? eventRequest.getDateTo() : LocalDate.now().toString();

        if (!dateFrom.isBlank() && !dateTo.isBlank()){
            url += "/?actual_since=" +
                    convertToUnixTime(dateFrom)
                    + "&actual_until=" +
                    convertToUnixTime(dateTo)
                    + paramForUrl;
        } else {
            throw new RuntimeException("Date is blank!");
        }

        return url;
    }

}
