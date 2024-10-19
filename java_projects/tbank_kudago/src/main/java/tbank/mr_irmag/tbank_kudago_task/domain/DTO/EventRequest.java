package tbank.mr_irmag.tbank_kudago_task.domain.DTO;

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
public class EventRequest {
    private String dateFrom;
    private String dateTo;
    @NotBlank(message = "From currency cannot be blank")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters long")
    private String currencyCode;
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    public String getCurrencyCode() {
        return currencyCode.toUpperCase();
    }
}
