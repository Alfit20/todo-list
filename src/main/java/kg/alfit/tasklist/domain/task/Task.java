package kg.alfit.tasklist.domain.task;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task implements Serializable {
    Long id;
    String title;
    String description;
    Status status;
    LocalDateTime expirationDate;

}
