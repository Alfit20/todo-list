package kg.alfit.tasklist.repository.impl;

import kg.alfit.tasklist.config.DataSourceConfig;
import kg.alfit.tasklist.domain.exception.ResourceMappingException;
import kg.alfit.tasklist.domain.user.Role;
import kg.alfit.tasklist.domain.user.User;
import kg.alfit.tasklist.repository.UserRepository;
import kg.alfit.tasklist.repository.mapper.UserRowMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

//@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    DataSourceConfig dataSourceConfig;
    String FIND_BY_ID = """
            SELECT u.id user_id, u.name name, u.username username,
            u.password password, ur.role role, t.id task_id, t.title title,
            t.description description, t.expiration_date expiration_date, t.status status
            FROM users u
            LEFT JOIN users_roles ur ON u.id = ur.user_id
            LEFT JOIN users_tasks ut ON u.id = ut.user_id
            LEFT JOIN tasks t ON ut.task_id = t.id
            WHERE u.id = ?
            """;
    String FIND_BY_USERNAME = """
            SELECT u.id user_id, u.name name, u.username username,
            u.password password, ur.role role, t.id task_id, t.title title,
            t.description description, t.expiration_date expiration_date, t.status status
            FROM users u
            LEFT JOIN users_roles ur ON u.id = ur.user_id
            LEFT JOIN users_tasks ut ON u.id = ut.user_id
            LEFT JOIN tasks t ON ut.task_id = t.id
            WHERE u.username = ?
            """;

    String UPDATE = """
            UPDATE users
            SET name = ?, username = ?, password = ?
            WHERE id = ?
            """;

    String CREATE = """
            INSERT INTO users(name, username, password)
            VALUES (?, ?, ?)
            """;

    String INSERT_USER_ROLE = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?, ?)
            """;

    String IS_TASK_OWNER = """
            SELECT EXISTS(
                           SELECT 1
                           FROM users_tasks
                           WHERE user_id = ?
                             AND task_id = ?
                       )
            """;

    String DELETE = """
            DELETE FROM users WHERE id = ?
            """;

    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement
                    (FIND_BY_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while finding user by id");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement
                    (FIND_BY_USERNAME, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while finding user by username");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while updating user");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                rs.next();
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while creating user");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_ROLE);
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, role.name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while inserting user role");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(IS_TASK_OWNER);
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, taskId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while checking if user task owner");
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
            throw new ResourceMappingException("Exception while deleting user");
        }
    }
}
