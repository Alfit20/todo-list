package kg.alfit.tasklist.repository.impl;

import kg.alfit.tasklist.config.DataSourceConfig;
import kg.alfit.tasklist.domain.exception.ResourceMappingException;
import kg.alfit.tasklist.domain.task.Task;
import kg.alfit.tasklist.repository.TaskRepository;
import kg.alfit.tasklist.repository.mapper.TaskRowMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

//@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    DataSourceConfig dataSourceConfig;

    String FIND_BY_ID =
            """
                    SELECT t.id task_id, t.title title, t.description description,
                    t.expiration_date expiration_date, t.status status FROM tasks t
                    WHERE t.id = ?
                    """;
    String FIND_ALL_BY_USER_ID =
            """
                    SELECT t.id task_id, t.title title, t.description description,
                    t.expiration_date expiration_date, t.status status FROM tasks t
                    JOIN users_tasks ut on t.id = ut.task_id
                    WHERE ut.user_id = ?
                     """;

    String ASSIGN = """
            INSERT INTO users_tasks (task_id, user_id)
            VALUES (?, ?)
            """;

    String UPDATE = """
            UPDATE tasks SET title = ?, description = ?, expiration_date = ?, status = ?
            WHERE id = ?
            """;

    String CREATE = """
            INSERT INTO tasks(title, description, expiration_date, status)
            VALUES (?, ?, ?, ?)
            """;

    String DELETE = """
            DELETE FROM tasks WHERE id = ?
            """;


    @Override
    public Optional<Task> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return Optional.ofNullable(TaskRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding user by id");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            preparedStatement.setLong(1, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return TaskRowMapper.mapRows(rs);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while finding all user by id");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN);
            preparedStatement.setLong(1, taskId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while assigning to user");
        }
    }

    @Override
    public void update(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                preparedStatement.setNull(2, Types.VARCHAR);
            } else {
                preparedStatement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                preparedStatement.setNull(3, Types.TIMESTAMP);
            } else {
                preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            preparedStatement.setString(4, task.getStatus().name());
            preparedStatement.setLong(5, task.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating task");
        }
    }

    @Override
    public void create(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                preparedStatement.setNull(2, Types.VARCHAR);
            } else {
                preparedStatement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                preparedStatement.setNull(3, Types.TIMESTAMP);
            } else {
                preparedStatement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            preparedStatement.setString(4, task.getStatus().name());
            preparedStatement.executeUpdate();
            try(ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                task.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating task");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting task");
        }
    }
}
