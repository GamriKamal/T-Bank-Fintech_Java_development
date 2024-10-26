package tbank.mr_irmag.cbr_ru.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertCurrencyRequest {
    @NotBlank(message = "From currency cannot be blank")
    @Size(min = 3, max = 3, message = "From currency must be exactly 3 characters long")
    private String fromCurrency;

    @NotBlank(message = "To currency cannot be blank")
    @Size(min = 3, max = 3, message = "To currency must be exactly 3 characters long")
    private String toCurrency;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;
}

