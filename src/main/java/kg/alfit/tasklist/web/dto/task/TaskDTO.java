package kg.alfit.tasklist.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import kg.alfit.tasklist.domain.task.Status;
import kg.alfit.tasklist.web.dto.validation.OnCreate;
import kg.alfit.tasklist.web.dto.validation.OnUpdate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDTO {

    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    Long id;

    @NotNull(message = "Title must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Title length must be smaller than 255 symbols",
            groups = {OnCreate.class, OnUpdate.class})
    String title;

    @Length(max = 255, message = "Description length must be smaller than 255 symbols",
            groups = {OnCreate.class, OnUpdate.class})
    String description;


    Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime expirationDate;

}
