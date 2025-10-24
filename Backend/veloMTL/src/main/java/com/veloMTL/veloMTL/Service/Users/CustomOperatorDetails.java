package com.veloMTL.veloMTL.Service.Users;

import com.veloMTL.veloMTL.Model.Users.Operator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.Collections;

public class CustomOperatorDetails implements UserDetails {

    private final Operator operator;

    public CustomOperatorDetails(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return operator.getPassword();
    }

    @Override
    public String getUsername() {
        return operator.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
