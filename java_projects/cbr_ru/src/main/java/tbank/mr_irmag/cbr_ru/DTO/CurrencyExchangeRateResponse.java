package tbank.mr_irmag.cbr_ru.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ на запрос курса валют")
public class CurrencyExchangeRateResponse {

    @Schema(description = "Код валюты", example = "USD")
    private String currency;

    @Schema(description = "Текущий курс валюты", example = "74.25")
    private double rate;
}
