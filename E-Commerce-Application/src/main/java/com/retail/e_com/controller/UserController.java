package com.retail.e_com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.jwt.JwtService;
import com.retail.e_com.requestdto.AuthRequest;
import com.retail.e_com.requestdto.OTPRequest;
import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.AuthResponse;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.service.UserService;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(allowCredentials = "true",origins = "http://localhost:5173")
public class UserController {
	private UserService userService;
	//private JwtService jwtService;

	@PostMapping("/users")
	public ResponseEntity<SimpleResponseStructure> userRegistration(@RequestBody @Valid UserRequest userRequest)
	{
		return userService.userRegistration(userRequest);
	}
	@PostMapping("/verify-email")
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(@RequestBody OTPRequest otpRequest)
	{
		return userService.verifyOTP(otpRequest);
	}
	//	@GetMapping("/test")
	//	public String test(@RequestParam String username,@RequestParam String role) {
	//		return jwtService.genaretAccessToken(username,role);
	//	}

	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> userLogin(@RequestBody AuthRequest authRequest)
	{
		return userService.userLogin(authRequest);
	}

	@PostMapping("/logout")
	public ResponseEntity<SimpleResponseStructure> logout(@CookieValue(name="rt", required =false) String refreshToken,
			@CookieValue(name="at", required =false) String accessToken){
		return userService.userLogout(refreshToken,accessToken);
	}
	
	@PostMapping("/login/refresh")
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(@CookieValue(name = "at",required = false)String accesToken,
			@CookieValue(name = "rt",required = false)String refreshToken)
	{
		return userService.refreshLogin(accesToken,refreshToken);
	}
}
