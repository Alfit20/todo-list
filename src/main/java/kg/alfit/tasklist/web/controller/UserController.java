package kg.alfit.tasklist.web.controller;

import kg.alfit.tasklist.domain.task.Task;
import kg.alfit.tasklist.domain.user.User;
import kg.alfit.tasklist.service.TaskService;
import kg.alfit.tasklist.service.UserService;
import kg.alfit.tasklist.web.dto.task.TaskDTO;
import kg.alfit.tasklist.web.dto.user.UserDTO;
import kg.alfit.tasklist.web.dto.validation.OnCreate;
import kg.alfit.tasklist.web.dto.validation.OnUpdate;
import kg.alfit.tasklist.web.mapper.TaskMapper;
import kg.alfit.tasklist.web.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    TaskService taskService;
    UserMapper userMapper;
    TaskMapper taskMapper;

    @GetMapping("{id}")
    public UserDTO getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @GetMapping("{id}/tasks")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long id) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("{id}/tasks")
    public TaskDTO createTask(@PathVariable Long id,
                              @Validated(OnCreate.class) @RequestBody TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        return taskMapper.toDto(taskService.create(task, id));
    }

    @PutMapping
    public UserDTO updateUser(@Validated(OnUpdate.class) @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDto(userService.update(user));
    }

    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.delete(id);
    }
}
