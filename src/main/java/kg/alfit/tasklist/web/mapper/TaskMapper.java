package kg.alfit.tasklist.web.mapper;

import kg.alfit.tasklist.domain.task.Task;
import kg.alfit.tasklist.web.dto.task.TaskDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDto(Task task);

    List<TaskDTO> toDto(List<Task> tasks);

    Task toEntity(TaskDTO taskDTO);
}
