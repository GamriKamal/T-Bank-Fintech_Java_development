package tbank.mr_irmag.tbank_kudago_task.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность для хранения данных с response с сайта KudaGo. Данный класс подходит для парсинга locations.")
public class Location {
    @Schema(description = "Сокращенный код города", example = "msc")
    @NotBlank
    private String slug;
    @Schema(description = "Название города.", example = "Москва")
    @NotBlank
    private String name;
}
