package com.retail.e_com.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.retail.e_com.model.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails{
	private User user;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().name()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		//it is not expired
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		//it is not locked
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		//not expired
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}

