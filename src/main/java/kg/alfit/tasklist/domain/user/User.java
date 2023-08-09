package kg.alfit.tasklist.domain.user;

import kg.alfit.tasklist.domain.task.Task;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    String name;
    String username;
    String password;
    String passwordConfirmation;
    Set<Role> roles;
    List<Task> tasks;
}
