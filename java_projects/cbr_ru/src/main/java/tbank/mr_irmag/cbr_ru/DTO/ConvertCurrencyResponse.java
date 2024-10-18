package tbank.mr_irmag.cbr_ru.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ на запрос конвертации валюты")
public class ConvertCurrencyResponse {

    @Schema(description = "Код исходной валюты", example = "USD")
    private String fromCurrency;

    @Schema(description = "Код валюты назначения", example = "EUR")
    private String toCurrency;

    @Schema(description = "Количество исходной валюты", example = "100")
    private double amount;

    @Schema(description = "Конвертированная сумма в валюте назначения", example = "85.50")
    private double convertedAmount;
}
