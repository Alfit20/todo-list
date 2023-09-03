package kg.alfit.tasklist.web.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskImageDTO {

    @NotNull(message = "Image must be not null")
    MultipartFile file;
}
