package com.groupeight.krypto.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.groupeight.krypto.model.User;

public class AppUserDetails implements UserDetails {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final User user;

    public AppUserDetails(User user) { this.user = user; }

    public String getFirstName() { return user.getFirstName(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public String getPassword() { return user.getPassword(); }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return user.getAuthorities(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return user.isEnabled(); }
}
