package kg.alfit.tasklist.web.security.expression;

import kg.alfit.tasklist.domain.user.Role;
import kg.alfit.tasklist.service.UserService;
import kg.alfit.tasklist.web.security.JwtEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("customSecurityExpression")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomSecurityExpression {
    final UserService userService;

    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();

        return userId.equals(id) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean canAccessTask(Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();

        return userService.isTaskOwner(userId, taskId);
    }

    private boolean hasAnyRole(Authentication authentication, Role... roles) {
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

}
