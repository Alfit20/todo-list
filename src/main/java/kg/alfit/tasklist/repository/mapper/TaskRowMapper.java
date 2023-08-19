package kg.alfit.tasklist.repository.mapper;

import kg.alfit.tasklist.domain.task.Status;
import kg.alfit.tasklist.domain.task.Task;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TaskRowMapper {

    @SneakyThrows
    public static Task mapRow(ResultSet resultSet) {
        if (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            task.setTitle(resultSet.getString("title"));
            task.setDescription(resultSet.getString("description"));
            Timestamp timestamp = resultSet.getTimestamp("expiration_date");
            if (timestamp != null) {
                task.setExpirationDate(timestamp.toLocalDateTime());
            }
            task.setStatus(Status.valueOf(resultSet.getString("status")));
            return task;
        }
        return null;
    }

    @SneakyThrows
    public static List<Task> mapRows(ResultSet resultSet) {
        List<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            if (!resultSet.wasNull()) {
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                Timestamp timestamp = resultSet.getTimestamp("expiration_date");
                if (timestamp != null) {
                    task.setExpirationDate(timestamp.toLocalDateTime());
                }
                task.setStatus(Status.valueOf(resultSet.getString("status")));
                tasks.add(task);
            }
        }
        return tasks;
    }
}
