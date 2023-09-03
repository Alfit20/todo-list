package kg.alfit.tasklist.web.mapper;

import kg.alfit.tasklist.domain.task.TaskImage;
import kg.alfit.tasklist.web.dto.task.TaskImageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDTO> {

}
