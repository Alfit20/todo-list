package kg.alfit.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "username", example = "alfa")
    @NotNull(message = "Username must be not null")
    String username;

    @Schema(description = "password", example = "qwe")
    @NotNull(message = "Password must be not null")
    String password;
}
