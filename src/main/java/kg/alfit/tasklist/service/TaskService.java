package kg.alfit.tasklist.service;

import kg.alfit.tasklist.domain.task.Task;

import java.util.List;

public interface TaskService {

    Task getById(Long id);

    List<Task> getAllByUserId(Long userId);

    Task update(Task task);

    Task create(Task task);

    void delete(Long id);
}