package tbank.mr_irmag.tbank_kudago_task.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Сущность для хранения данных с response с сайта KudaGo. Данный класс подходит для парсинга categories.")
public class Category {
    @Schema(description = "ID записи", example = "123")
    private Integer id;
    @Schema(description = "Сокращенный код города или название места на английском языке.", example = "airports")
    @NotBlank
    private String slug;
    @Schema(description = "Название места или города.", example = "Развлечения")
    @NotBlank
    private String name;
}
