package kg.alfit.tasklist.repository.mapper;

import kg.alfit.tasklist.domain.task.Task;
import kg.alfit.tasklist.domain.user.Role;
import kg.alfit.tasklist.domain.user.User;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRowMapper {
    @SneakyThrows
    public static User mapRow(ResultSet rs) {
        Set<Role> roles = new HashSet<>();
        while (rs.next()) {
            roles.add(Role.valueOf(rs.getString("role")));
        }
        rs.beforeFirst();
        List<Task> tasks = TaskRowMapper.mapRows(rs);
        rs.beforeFirst();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("name"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRoles(roles);
            user.setTasks(tasks);
            return user;
        }
        return null;
    }
}
