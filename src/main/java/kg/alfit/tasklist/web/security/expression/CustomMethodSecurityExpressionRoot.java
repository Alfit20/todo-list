package kg.alfit.tasklist.web.security.expression;

import jakarta.servlet.http.HttpServletRequest;
import kg.alfit.tasklist.domain.user.Role;
import kg.alfit.tasklist.service.UserService;
import kg.alfit.tasklist.web.security.JwtEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements
        MethodSecurityExpressionOperations {
    Object filterObject;
    Object returnObject;
    Object target;
    HttpServletRequest request;
    UserService userService;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    @Override
    public Object getThis() {
        return this.target;
    }

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
