package tbank.mr_irmag.tbank_kudago_task.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Структура ответа об ошибке для сообщений об ошибках API")
public class ErrorResponse {

    @Schema(description = "Сообщение об ошибке, объясняющее, что пошло не так", example = "Категория не найдена")
    private String message;

    @Schema(description = "Подробная информация об ошибке", example = "Категория с id 123 не была найдена")
    private String details;
}
