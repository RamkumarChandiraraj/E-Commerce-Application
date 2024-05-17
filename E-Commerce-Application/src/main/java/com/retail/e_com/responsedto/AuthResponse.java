package com.retail.e_com.responsedto;

import com.retail.e_com.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AuthResponse {
	private int userId;
	private String username;
	private Long accessExpiration;
	private Long refreshExpiration;
	private UserRole userRole;
}
