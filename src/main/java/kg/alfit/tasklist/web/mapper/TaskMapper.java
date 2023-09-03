package kg.alfit.tasklist.web.mapper;

import kg.alfit.tasklist.domain.task.Task;
import kg.alfit.tasklist.web.dto.task.TaskDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDTO> {
}
