package tbank.mr_irmag.tbank_kudago_task.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Запрос на сброс пароля")
@AllArgsConstructor
public class PasswordResetRequest {

    @Schema(description = "Имя пользователя", example = "Kamal")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Schema(description = "Новый пароль", example = "new_secure_password")
    @NotBlank(message = "Пароль не может быть пустым")
    private String newPassword;

    @Schema(description = "Код подтверждения", example = "0000")
    @NotBlank(message = "Код подтверждения не может быть пустым")
    private String confirmationCode;
}

