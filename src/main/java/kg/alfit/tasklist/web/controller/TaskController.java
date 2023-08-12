package kg.alfit.tasklist.web.controller;

import kg.alfit.tasklist.domain.task.Task;
import kg.alfit.tasklist.service.TaskService;
import kg.alfit.tasklist.web.dto.task.TaskDTO;
import kg.alfit.tasklist.web.dto.validation.OnUpdate;
import kg.alfit.tasklist.web.mapper.TaskMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskController {
    TaskService taskService;
    TaskMapper taskMapper;


    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    public TaskDTO update(@Validated(OnUpdate.class) @RequestBody TaskDTO dto) {
        Task task = taskMapper.toEntity(dto);
        return taskMapper.toDto(taskService.update(task));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
