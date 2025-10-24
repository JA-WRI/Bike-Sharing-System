package com.veloMTL.veloMTL.Service.Users;

import com.veloMTL.veloMTL.Model.Users.Rider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomRiderDetails implements UserDetails {

        private final Rider rider;

        public CustomRiderDetails(Rider rider) {
            this.rider = rider;
        }

        public Rider getRider() {
            return rider;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // If you have roles in Rider, map them to GrantedAuthority
            return null; // or Collections.emptyList() if no roles
        }

        @Override
        public String getPassword() {
            return rider.getPassword(); // adjust if you store passwords
        }

        @Override
        public String getUsername() {
            return rider.getEmail(); // use email as username
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


