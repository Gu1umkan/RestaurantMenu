package peaksoft.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PROGRAMMER,
    ADMIN,
    CHEF,
    WAITER;

    @Override
    public String getAuthority() {
        return name();
    }
}
