package ru.chat.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserInfo implements UserDetails {
    private User user;
    private AppUser Info;

    public UserInfo() {

    }

    public UserInfo(User _user, AppUser Info) {
        this.user = _user;
        this.Info = Info;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return this.user == null ? null : this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public User getUser() {
        return user;
    }

    public AppUser getInfo () {
        return Info;
    }

    @Override
    public String toString() {
        return "CustomUserDetails [user=" + user + "]";
    }
}
