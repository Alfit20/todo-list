package kg.alfit.tasklist.domain.user;

import jakarta.persistence.*;
import kg.alfit.tasklist.domain.task.Task;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String username;
    String password;

    @Transient
    String passwordConfirmation;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles")
    @Enumerated(value = EnumType.STRING)
    Set<Role> roles;

    @CollectionTable(name = "users_tasks")
    @OneToMany
    @JoinColumn(name = "task_id")
    List<Task> tasks;
}
