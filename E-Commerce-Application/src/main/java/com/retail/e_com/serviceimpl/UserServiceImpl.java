package com.retail.e_com.serviceimpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.retail.e_com.cache.CacheStore;
import com.retail.e_com.enums.UserRole;
import com.retail.e_com.exception.EmailNotCorrectException;
import com.retail.e_com.exception.InvalidCreaditionalException;
import com.retail.e_com.exception.InvalidOTPException;
import com.retail.e_com.exception.InvalidUserRoleSpecifiedException;
import com.retail.e_com.exception.OTPExpiredException;
import com.retail.e_com.exception.RegistrationSessionExpiredException;
import com.retail.e_com.exception.UserAlreadyExistByEmailException;
import com.retail.e_com.exception.UserNotLoggedInException;
import com.retail.e_com.jwt.JwtService;
import com.retail.e_com.mail_service.MailService;
import com.retail.e_com.mail_service.MessageModel;
import com.retail.e_com.model.AccessToken;
import com.retail.e_com.model.Customer;
import com.retail.e_com.model.RefreshToken;
import com.retail.e_com.model.Seller;
import com.retail.e_com.model.User;
import com.retail.e_com.repository.AccessTokenRepository;
import com.retail.e_com.repository.RefreshTokenRepository;
import com.retail.e_com.repository.UserRepository;
import com.retail.e_com.requestdto.Attempt;
import com.retail.e_com.requestdto.AuthRequest;
import com.retail.e_com.requestdto.OTPRequest;
import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.AuthResponse;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.service.UserService;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;
import jakarta.mail.MessagingException;

@Service
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private ResponseStructure<UserResponse> structure;
	private SimpleResponseStructure simpleResponse;
	private MailService mailService;
	private JwtService jwtService;
	private RefreshTokenRepository refreshRepository;
	private AccessTokenRepository accessrepository;
	private AuthenticationManager authenticationManager;
	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiration;
	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiration;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, CacheStore<String> otpCache, CacheStore<User> userCache,
			ResponseStructure<UserResponse> structure, SimpleResponseStructure simpleResponse, MailService mailService,
			JwtService jwtService, RefreshTokenRepository refreshRepository, AccessTokenRepository accessrepository,
			AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.structure = structure;
		this.simpleResponse = simpleResponse;
		this.mailService = mailService;
		this.jwtService = jwtService;
		this.refreshRepository = refreshRepository;
		this.accessrepository = accessrepository;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder=passwordEncoder;
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest) {
		if(userRepository.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Faild to register User");
		User user=mapToChildEntity(userRequest);
		String otp=generatedOTP();

		otpCache.add(user.getEmail(), otp);
		userCache.add(user.getEmail(), user);

		//System.err.println(otp);
		try {
			sendOTP(user,otp);
		} catch (MessagingException e) {
			throw new EmailNotCorrectException("Email  is not correct");
		}
		System.out.println(otp);
		return ResponseEntity.ok(simpleResponse.setStatus(HttpStatus.ACCEPTED.value()).setMessage(otp+" Verify OTP sent through the email to complete the registration ! otp expires in 5 minutes"));
	}

	private void sendOTP(User user, String otp) throws MessagingException {
		MessageModel model=MessageModel.builder()
				.to(user.getEmail())
				.subject("Verify your OTP")
				.text(
						"<p>Hii, <br>"
								+"Thanks for your interest in E-Com, "
								+"Please verify your mail Id using the OTP given below. </p>"
								+"<br>"
								+"<h1>"+otp+"</h1>"
								+"<br>"
								+"<p>Please ignore if its not you</p>"
								+"<br>"
								+"With best regards"
								+"<h3>E-Com</h3>"
						)
				.build();

		mailService.sendMailMessage(model);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPRequest otpRequest) {
		if(otpCache.getData(otpRequest.getEmail())==null) throw new OTPExpiredException("otp had expired");
		if(!otpCache.getData(otpRequest.getEmail()).equals(otpRequest.getOtp())) throw new InvalidOTPException("OTP entered is wrong");
		User user=userCache.getData(otpRequest.getEmail());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if(user==null) throw new RegistrationSessionExpiredException("User Registration session expired");
		user.setEmailVerified(true);
		user=userRepository.save(user);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(structure.setData(mapToUserResponse(user))
						.setMessage("OTP verification successful")
						.setStatusCode(HttpStatus.CREATED.value()));
	}

	private String generatedOTP() {
		return String.valueOf(new Random().nextInt(100000,999999));
	}

	private <T extends User> T mapToChildEntity(UserRequest userRequest) {
		UserRole userRole=userRequest.getUserRole();
		User user=null;
		switch(userRole)
		{
		case SELLER ->user= new Seller();
		case CUSTOMER ->user=new Customer();
		default -> throw new InvalidUserRoleSpecifiedException("User entered is not specified");
		}
		user.setDisplayName(userRequest.getName());
		user.setUsername(userRequest.getEmail().split("@gmail.com")[0]);
		user.setEmail(userRequest.getEmail());
		user.setPassword(userRequest.getPassword());
		user.setUserRole(userRole);
		user.setEmailVerified(false);
		user.setDeleted(false);
		return (T)user;
	}

	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.displayName(user.getDisplayName()) 
				.email(user.getEmail())
				.isEmailVerified(user.isEmailVerified())
				.isDeleted(user.isDeleted())
				.userRole(user.getUserRole())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> userLogin(AuthRequest authRequest) {
		String username = authRequest.getUsername().split("@gmail.com")[0];
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));
		if(!authentication.isAuthenticated())throw new 	InvalidCreaditionalException("user is not authenticated");

		SecurityContextHolder.getContext().setAuthentication(authentication);

		HttpHeaders headers=new HttpHeaders();
		return userRepository.findByUsername(username).map(user->{
			generateAccessToken(user,headers);
			generateRefresfToken(user,headers);
			return ResponseEntity.ok().headers(headers).body(new ResponseStructure<AuthResponse>().setStatusCode(HttpStatus.CREATED.value())
					.setMessage("user login successfully")
					.setData(mapToAuthResponse(user)));
		}).get();
	}

	AuthResponse mapToAuthResponse(User user) {
		return AuthResponse.builder()
				.userId(user.getUserId())
				.userRole(user.getUserRole())
				.username(user.getUsername())
				.accessExpiration(accessExpiration)
				.refreshExpiration(refreshExpiration)
				.build();
	}

	private void generateRefresfToken(User user, HttpHeaders headers) {
		String token = jwtService.generateAccessToken(user.getUsername(),user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE,configureCookie("rt",token,refreshExpiration));
		RefreshToken refreshToken=new RefreshToken();
		refreshToken.setToken(token);
		refreshToken.setBlocked(false);
		refreshToken.setExpiration(LocalDateTime.now().plusSeconds(refreshExpiration));
		refreshToken.setUser(user);
		refreshRepository.save(refreshToken);

	}
	private String configureCookie(String name, String value, long maxAge) {
		return ResponseCookie.from(name,value)
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(Duration.ofMillis(maxAge))
				.sameSite("Lax").build().toString();
	}

	private void generateAccessToken(User user, HttpHeaders headers) {
		String token = jwtService.generateAccessToken(user.getUsername(),user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE,configureCookie("at",token,accessExpiration));

		AccessToken accessToken=new AccessToken();
		accessToken.setToken(token);
		accessToken.setBlocked(false);
		accessToken.setExpiration(LocalDateTime.now().plusSeconds(accessExpiration));
		accessToken.setUser(user);
		accessrepository.save(accessToken);
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> userLogout(String refreshToken, String accessToken) {
		HttpHeaders headers=new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, inValidate("at"));
		headers.add(HttpHeaders.SET_COOKIE, inValidate("rt"));
		blockAccessToken(accessToken);
		blockRefreshToken(refreshToken);

		return ResponseEntity.ok().headers(headers).body(simpleResponse.setStatus(HttpStatus.OK.value()).setMessage("Logout Successful"));
	}

	private void blockAccessToken(String accessToken) {
		accessrepository.findByToken(accessToken).ifPresent(at->{
			at.setBlocked(true);
			accessrepository.save(at);
		});
	}

	private void blockRefreshToken(String refreshToken) {
		refreshRepository.findByToken(refreshToken).ifPresent(rt->{
			rt.setBlocked(true);
			refreshRepository.save(rt);
		});
	}

	private String inValidate(String name) {
		return ResponseCookie.from(name,"")
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(0)
				.sameSite("Lax").build().toString();
	}

	public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String accesToken, String refreshToken) {
		System.out.println(refreshToken);
		if(refreshToken==null) {
			throw new UserNotLoggedInException("User is not Logged In");
		}
		if(accesToken!=null)
		{
			accessrepository.findByToken(refreshToken).ifPresent(token->
			{
				token.setBlocked(true);
				accessrepository.save(token);
			});
		}
		Date date=jwtService.getIssueDate(refreshToken);
		String username=jwtService.getUsername(refreshToken);
		System.out.println("USERNAME : "+username);
		HttpHeaders headers=new HttpHeaders();
		return userRepository.findByUsername(username).map(user->{
			if(date.before(new Date()))
				generateAccessToken(user, headers);
			else
				headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt", refreshToken, refreshExpiration));
			generateAccessToken(user, headers);
			return ResponseEntity.ok().headers(headers).body(new ResponseStructure<AuthResponse>().setStatusCode(HttpStatus.OK.value())
					.setMessage("Refresh Login Successful")
					.setData(mapToAuthResponse(user)));
		}).get();
	}
}
