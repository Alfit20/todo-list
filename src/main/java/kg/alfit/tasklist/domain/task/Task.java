package kg.alfit.tasklist.domain.task;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    Long id;
    String title;
    String description;
    Status status;
    LocalDateTime expirationDate;

}
