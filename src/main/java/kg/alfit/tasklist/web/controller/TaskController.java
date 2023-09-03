package kg.alfit.tasklist.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.alfit.tasklist.domain.task.Task;
import kg.alfit.tasklist.domain.task.TaskImage;
import kg.alfit.tasklist.service.TaskService;
import kg.alfit.tasklist.web.dto.task.TaskDTO;
import kg.alfit.tasklist.web.dto.task.TaskImageDTO;
import kg.alfit.tasklist.web.dto.validation.OnUpdate;
import kg.alfit.tasklist.web.mapper.TaskImageMapper;
import kg.alfit.tasklist.web.mapper.TaskMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {
    TaskService taskService;
    TaskMapper taskMapper;

    TaskImageMapper taskImageMapper;


    @Operation(summary = "Get task by id")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @Operation(summary = "Update task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    @PutMapping
    public TaskDTO update(@Validated(OnUpdate.class) @RequestBody TaskDTO dto) {
        Task task = taskMapper.toEntity(dto);
        return taskMapper.toDto(taskService.update(task));
    }
    @Operation(summary = "Delete task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void uploadImage(@PathVariable Long id, @Validated
                            @ModelAttribute TaskImageDTO taskImageDTO) {
        TaskImage image = taskImageMapper.toEntity(taskImageDTO);
        taskService.uploadImage(id, image);
    }
}
